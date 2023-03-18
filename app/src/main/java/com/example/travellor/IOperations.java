package com.example.travellor;

import java.io.Serializable;
import java.util.ArrayList;

public interface IOperations extends Serializable {
    boolean authenticateUser(String username, String password);

    String getUsername();

    boolean signUpUser(String username, String password, String Card);

    ArrayList<regionHolder> getRegionList();

    ArrayList<locationBasicData> getHotelsInRegion(String regionName);

    individualHotel getHotel(String address, String location);

    boolean bookHotel(String hotelLocation, String hotelAddress, String userName);

    boolean updatePassword(String userName, String newPassword);

    ArrayList<bookingHolder> getMyBookings(String userName);

    boolean unBookHotel(String hotelLocation, String hotelAddress);

    ArrayList<String> resetHotelBookings(String username);
}
