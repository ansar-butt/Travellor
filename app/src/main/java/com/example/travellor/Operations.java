package com.example.travellor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Operations implements IOperations {
    private final IDB db = new DBFirebase();

    public Operations() {
        try {
            db.initDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        ArrayList<String> docRef = db.getObjectReference(username, IDB.ModelType.user);
        try {
            user User = (user) db.getObject(docRef.get(0), IDB.ModelType.user);
            return User.getUsername().equals(username) && User.getPassword().equals(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getUsername() {
        String userName = "";
        try {
            File myObj = new File("user.txt");
            Scanner myReader = new Scanner(myObj);
            if (myReader.hasNextLine()) {
                userName = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return userName;
    }

    @Override
    public boolean signUpUser(String username, String password, String Card) {
        ArrayList<String> users = db.getObjectReference(username, IDB.ModelType.user);
        if (users.size() > 0)
            return false;

        user newUser = new user();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setCard(Card);

        String ref = null;
        try {
            ref = db.addObject(newUser, IDB.ModelType.user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ref != null;
    }

    @Override
    public ArrayList<regionHolder> getRegionList() {
        ArrayList<regionHolder> ans = new ArrayList<>();
        try {
            ArrayList<IModel> tempList = db.getCollection(IDB.ModelType.regionHolder, new ArrayList<>());
            for (IModel temp : tempList)
                ans.add((regionHolder) temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    public ArrayList<locationBasicData> getHotelsInRegion(String regionName) {
        ArrayList<locationBasicData> ans = new ArrayList<>();
        try {
            ArrayList<IModel> tempList = db.getCollection(IDB.ModelType.hotel, new ArrayList<>());
            for (IModel temp : tempList) {
                hotel fullData = (hotel) temp;
                if (fullData.getLocationRegion().equals(regionName)) {
                    locationBasicData locationData = new locationBasicData();
                    locationData.setLoactionName(fullData.getLocationName());
                    locationData.setRent(fullData.getRent());
                    locationData.setLocation(fullData.getLocationAddress());
                    locationData.setBooked(fullData.getBooked());
                    ans.add(locationData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    public individualHotel getHotel(String address, String location) {
        individualHotel locationData = new individualHotel();
        try {
            ArrayList<IModel> tempList = db.getCollection(IDB.ModelType.hotel, new ArrayList<>());
            for (IModel temp : tempList) {
                hotel fullData = (hotel) temp;
                if (fullData.getLocationName().equals(location) && fullData.getLocationAddress().equals(address)) {
                    locationData.setLocationName(fullData.getLocationName());
                    locationData.setLocation(fullData.getLocationAddress());
                    locationData.setRent(fullData.getRent());
                    locationData.setDescription(fullData.getDescription());
                    locationData.setBooked(fullData.getBooked());
                    locationData.setUserName(fullData.getBookedBy());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationData;
    }

    @Override
    public boolean updatePassword(String userName, String newPassword) {
        ArrayList<String> docRef = db.getObjectReference(userName, IDB.ModelType.user);

        HashMap<String, Object> map = new HashMap<>();
        map.put("password", newPassword);
        boolean flag = false;
        try {
            if (db.updateObject(docRef.get(0), map, IDB.ModelType.user))
                flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public ArrayList<bookingHolder> getMyBookings(String userName) {
        ArrayList<bookingHolder> ans = new ArrayList<>();
        try {
            ArrayList<IModel> tempList = db.getCollection(IDB.ModelType.hotel, new ArrayList<>());
            for (IModel temp : tempList) {
                hotel fullData = (hotel) temp;
                if (fullData.getBooked() && fullData.getBookedBy().equals(userName)) {
                    bookingHolder locationData = new bookingHolder();
                    locationData.setHotelLocation(fullData.getLocationName());
                    locationData.setBookingEnd(fullData.getBookingEnd());
                    locationData.setHotelAddress(fullData.getLocationAddress());
                    ans.add(locationData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    public boolean unBookHotel(String hotelLocation, String hotelAddress) {
        ArrayList<String> ids = new ArrayList<>();
        try {
            ArrayList<IModel> tempList = db.getCollection(IDB.ModelType.hotel, ids);
            for (int i = 0; i < tempList.size(); i++) {
                hotel fullData = (hotel) tempList.get(i);
                if (fullData.getLocationAddress().equals(hotelAddress) && fullData.getLocationName().equals(hotelLocation)) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("booked", (Boolean) false);

                    return db.updateObject(ids.get(i), map, IDB.ModelType.hotel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<String> resetHotelBookings(String username) {
        ArrayList<String> ans = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        try {
            ArrayList<IModel> tempList = db.getCollection(IDB.ModelType.hotel, ids);
            for (int i = 0; i < tempList.size(); i++) {
                hotel fullData = (hotel) tempList.get(i);
                if (fullData.getBookedBy().equals(username) && fullData.getBooked()) {
                    Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(fullData.getBookingEnd());
                    Calendar cal = Calendar.getInstance();
                    String sDate2 = cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
                    Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(sDate2);

                    if (date2 != null && date2.after(date1)) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("booked", false);

                        ans.add(fullData.getLocationName());
                        db.updateObject(ids.get(i), map, IDB.ModelType.hotel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    public boolean bookHotel(String hotelLocation, String hotelAddress, String userName) {
        String endDate;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);
        endDate = cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);

        ArrayList<String> ids = new ArrayList<>();
        try {
            ArrayList<IModel> tempList = db.getCollection(IDB.ModelType.hotel, ids);
            for (int i = 0; i < tempList.size(); i++) {
                hotel fullData = (hotel) tempList.get(i);
                if (fullData.getLocationAddress().equals(hotelAddress) && fullData.getLocationName().equals(hotelLocation)) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("booked", (Boolean) true);
                    map.put("bookedBy", userName);
                    map.put("bookingEnd", endDate);

                    return db.updateObject(ids.get(i), map, IDB.ModelType.hotel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
