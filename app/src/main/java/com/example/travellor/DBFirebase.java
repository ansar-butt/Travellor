package com.example.travellor;

import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class DBFirebase implements IDB {
    protected FirebaseFirestore mDatabase;
    protected EnumMap<ModelType, String> attributeMap;
    protected EnumMap<ModelType, String> collectionMap;
    private String packageName;

    @Override
    public void initDB() {
        mDatabase = FirebaseFirestore.getInstance();

        packageName = "com.example.travellor.";

        attributeMap = new EnumMap<>(ModelType.class);
        attributeMap.put(ModelType.bookingHolder, "locationName");
        attributeMap.put(ModelType.regionHolder, "regionName");
        attributeMap.put(ModelType.locationBasicData, "locationName");
        attributeMap.put(ModelType.individualHotel, "locationName");
        attributeMap.put(ModelType.user, "username");
        attributeMap.put(ModelType.hotel, "locationName");

        collectionMap = new EnumMap<>(ModelType.class);
        collectionMap.put(ModelType.bookingHolder, "Hotels");
        collectionMap.put(ModelType.regionHolder, "Regions");
        collectionMap.put(ModelType.locationBasicData, "Hotels");
        collectionMap.put(ModelType.individualHotel, "Hotels");
        collectionMap.put(ModelType.user, "Users");
        collectionMap.put(ModelType.hotel, "Hotels");

//        createSampleDatabase();
//        try {
//            sleep(1000000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public ArrayList<String> getObjectReference(String identifierValue, ModelType modelType) {
        ArrayList<String> ans = new ArrayList<>();

        Task<QuerySnapshot> task =
                mDatabase.collection(collectionMap.get(modelType))
                        .whereEqualTo(attributeMap.get(modelType), identifierValue)
                        .get();

        try {
            awaitCompletion(task);
            if (task.isSuccessful()) {
                QuerySnapshot query = task.getResult();
                if (query != null)
                    for (DocumentSnapshot documentSnapshot : query)
                        ans.add(documentSnapshot.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    public ArrayList<IModel> getCollection(ModelType modelType, ArrayList<String> ids) {
        ArrayList<IModel> ans = new ArrayList<>();

        Task<QuerySnapshot> task =
                mDatabase.collection(collectionMap.get(modelType))
                        .get();

        try {
            awaitCompletion(task);
            if (task.isSuccessful()) {
                QuerySnapshot query = task.getResult();
                if (query != null)
                    for (DocumentSnapshot obj : query) {
                        if (obj != null) {
                            ids.add(obj.getId());
                            ans.add((IModel) obj.toObject(Class.forName(packageName + modelType.toString())));
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    public IModel getObject(String objectId, ModelType modelType) {
        Task<DocumentSnapshot> task =
                mDatabase.collection(collectionMap.get(modelType))
                        .document(objectId)
                        .get();

        IModel temp = null;
        try {
            awaitCompletion(task);
            if (task.isSuccessful()) {
                DocumentSnapshot obj = task.getResult();
                assert obj != null;
                temp = (IModel) obj.toObject(Class.forName(packageName + modelType.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    @Override
    public ArrayList<IModel> getObjectsList(ArrayList<String> objectIds, ModelType modelType) {
        ArrayList<IModel> ans = new ArrayList<>();

        Task<QuerySnapshot> task =
                mDatabase.collection(collectionMap.get(modelType))
                        .get();

        try {
            awaitCompletion(task);
            if (task.isSuccessful()) {
                QuerySnapshot query = task.getResult();
                if (query != null)
                    for (DocumentSnapshot documentSnapshot : query) {
                        if (objectIds.contains(documentSnapshot.getId()))
                            ans.add((IModel) documentSnapshot.toObject(Class.forName(packageName + modelType.toString())));
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    public ArrayList<IModel> getObjectsList(HashMap<String, Object> attributesToQuery, ModelType modelType) {
        return null;
    }

    @Override
    public String addObject(IModel object, ModelType modelType) {
        CollectionReference collection = mDatabase.collection(collectionMap.get(modelType));
        Task<DocumentReference> task = collection.add(object);
        awaitCompletion(task);
        String ans;
        ans = task.getResult().getId();
        return ans;
    }

    @Override
    public boolean removeObject(String objectId, ModelType modelType) {
        DocumentReference document = mDatabase.collection(collectionMap.get(modelType)).document(objectId);
        Task<?> task = document.delete();
        return task.isSuccessful();
    }

    @Override
    public boolean updateObject(String objectId, HashMap<String, Object> attributesToBeUpdated, ModelType modelType) {
        DocumentReference documentReference = mDatabase.collection(collectionMap.get(modelType)).document(objectId);
        Task<?> task = documentReference.update(attributesToBeUpdated);
        awaitCompletion(task);
        task.isSuccessful();
        return task.isSuccessful();
    }

    @Override
    public boolean updateArrayObject(String objectId, Pair<String, Object> arrayAttributeToBeUpdated, UpdateOperation updateOperation, ModelType modelType) {
        return false;
    }

    private void awaitCompletion(Task<?> task) {
        int count = 0;
        try {
            while (!task.isSuccessful() && count < 120) {
                sleep(500);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createSampleDatabase() {
        Map<String, Object> data4;
        CollectionReference cities = mDatabase.collection("Hotels");

        data4 = new HashMap<>();
        data4.put("locationName", "Tulip Hotel");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Indus Plains, Tulip Hotel Multan is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Multan");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Swiss");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Karokaram Mountain, Royal Swiss Lahore is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AsadUllah");
        data4.put("locationAddress", "Lahore");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Nishat");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Arabian Sea, Nishat Multan is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Multan");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Swiss");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Karokaram Mountain, Royal Swiss Gujranwala is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HarisSaeed");
        data4.put("locationAddress", "Gujranwala");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Tulip Hotel");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Changa Manga Forest, Tulip Hotel Karachi is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Karachi");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Heritage Luxury Suites");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Indus Plains, Heritage Luxury Suites Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Marionette");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Changa Manga Forest, Marionette Lahore is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Lahore");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Palm");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Arabian Sea, Royal Palm Lahore is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Lahore");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Palm");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Changa Manga Forest, Royal Palm Gujranwala is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Gujranwala");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Swiss Lounge");
        data4.put("locationRegion", "Deserts");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Thar Desert, Swiss Lounge Multan is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HarisSaeed");
        data4.put("locationAddress", "Multan");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Heritage Luxury Suites");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "15,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Indus Plains, Heritage Luxury Suites Multan is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Multan");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Marionette");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Changa Manga Forest, Marionette Islamabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Islamabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Swiss");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Indus Plains, Royal Swiss Islamabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Islamabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Nishat");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Indus Plains, Nishat Karachi is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Karachi");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "HotelOne");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Changa Manga Forest, HotelOne Lahore is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AnsarButt");
        data4.put("locationAddress", "Lahore");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Palm");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "15,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Changa Manga Forest, Royal Palm Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AnsarButt");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Heritage Luxury Suites");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Karokaram Mountain, Heritage Luxury Suites Neelum is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AsadUllah");
        data4.put("locationAddress", "Neelum");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Swiss");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Changa Manga Forest, Royal Swiss Multan is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Multan");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Indigo Hotel");
        data4.put("locationRegion", "Deserts");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Thar Desert, Indigo Hotel Multan is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HizafaNadeem");
        data4.put("locationAddress", "Multan");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Marionette");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Arabian Sea, Marionette Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AnsarButt");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Clifton");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Arabian Sea, Clifton Lahore is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Lahore");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Heritage Luxury Suites");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "15,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Arabian Sea, Heritage Luxury Suites Gujranwala is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Gujranwala");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Pearl Continental");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "15,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Changa Manga Forest, Pearl Continental Lahore is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AnsarButt");
        data4.put("locationAddress", "Lahore");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Marionette");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Arabian Sea, Marionette Neelum is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HarisSaeed");
        data4.put("locationAddress", "Neelum");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Pearl Continental");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Karokaram Mountain, Pearl Continental Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Indigo Hotel");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Arabian Sea, Indigo Hotel Islamabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HizafaNadeem");
        data4.put("locationAddress", "Islamabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Pearl Continental");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Karokaram Mountain, Pearl Continental Islamabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Islamabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "HotelOne");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Arabian Sea, HotelOne Islamabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Islamabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Nishat");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Arabian Sea, Nishat Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Heritage Luxury Suites");
        data4.put("locationRegion", "Deserts");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Thar Desert, Heritage Luxury Suites Lahore is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Lahore");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Clifton");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Karokaram Mountain, Clifton Gujranwala is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HarisSaeed");
        data4.put("locationAddress", "Gujranwala");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Clifton");
        data4.put("locationRegion", "Deserts");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Thar Desert, Clifton Karachi is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HizafaNadeem");
        data4.put("locationAddress", "Karachi");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Swiss Lounge");
        data4.put("locationRegion", "Deserts");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Thar Desert, Swiss Lounge Neelum is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AnsarButt");
        data4.put("locationAddress", "Neelum");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Clifton");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Indus Plains, Clifton Islamabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Islamabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Indigo Hotel");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "15,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Indus Plains, Indigo Hotel Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Swiss");
        data4.put("locationRegion", "Deserts");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Thar Desert, Royal Swiss Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AnsarButt");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Marionette");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "15,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Changa Manga Forest, Marionette Multan is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HarisSaeed");
        data4.put("locationAddress", "Multan");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Palm");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "15,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Changa Manga Forest, Royal Palm Karachi is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HizafaNadeem");
        data4.put("locationAddress", "Karachi");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Swiss Lounge");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Indus Plains, Swiss Lounge Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Pearl Continental");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Indus Plains, Pearl Continental Neelum is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Neelum");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Heritage Luxury Suites");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Arabian Sea, Heritage Luxury Suites Islamabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Islamabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "HotelOne");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Karokaram Mountain, HotelOne Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Clifton");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Karokaram Mountain, Clifton Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Nishat");
        data4.put("locationRegion", "Deserts");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Thar Desert, Nishat Islamabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HizafaNadeem");
        data4.put("locationAddress", "Islamabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Indigo Hotel");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Indus Plains, Indigo Hotel Lahore is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Lahore");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Tulip Hotel");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Indus Plains, Tulip Hotel Islamabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AnsarButt");
        data4.put("locationAddress", "Islamabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Tulip Hotel");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "15,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Indus Plains, Tulip Hotel Neelum is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Neelum");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Indigo Hotel");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Arabian Sea, Indigo Hotel Neelum is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Neelum");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Swiss");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Karokaram Mountain, Royal Swiss Karachi is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Karachi");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Palm");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Karokaram Mountain, Royal Palm Neelum is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Neelum");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Pearl Continental");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Changa Manga Forest, Pearl Continental Karachi is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Karachi");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Tulip Hotel");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Arabian Sea, Tulip Hotel Gujranwala is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HizafaNadeem");
        data4.put("locationAddress", "Gujranwala");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Nishat");
        data4.put("locationRegion", "Deserts");
        data4.put("rent", "8,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Thar Desert, Nishat Gujranwala is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Gujranwala");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Nishat");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Indus Plains, Nishat Neelum is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Neelum");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Tulip Hotel");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Indus Plains, Tulip Hotel Lahore is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Lahore");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Swiss Lounge");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Arabian Sea, Swiss Lounge Gujranwala is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HarisSaeed");
        data4.put("locationAddress", "Gujranwala");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Pearl Continental");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Arabian Sea, Pearl Continental Multan is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Multan");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Pearl Continental");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Indus Plains, Pearl Continental Gujranwala is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AnsarButt");
        data4.put("locationAddress", "Gujranwala");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Clifton");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "20,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Karokaram Mountain, Clifton Multan is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AnsarButt");
        data4.put("locationAddress", "Multan");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Indigo Hotel");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Karokaram Mountain, Indigo Hotel Gujranwala is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Gujranwala");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "HotelOne");
        data4.put("locationRegion", "Forests");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Changa Manga Forest, HotelOne Gujranwala is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "HarisSaeed");
        data4.put("locationAddress", "Gujranwala");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Swiss Lounge");
        data4.put("locationRegion", "Plains");
        data4.put("rent", "15,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Indus Plains, Swiss Lounge Lahore is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AbdurRaheem");
        data4.put("locationAddress", "Lahore");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Indigo Hotel");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "7,500");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Karokaram Mountain, Indigo Hotel Karachi is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AnsarButt");
        data4.put("locationAddress", "Karachi");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Royal Palm");
        data4.put("locationRegion", "Mountains");
        data4.put("rent", "10,000");
        data4.put("bookingEnd", "");
        data4.put("description", "Overlooking the Karokaram Mountain, Royal Palm Islamabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", false);
        data4.put("bookedBy", "");
        data4.put("locationAddress", "Islamabad");
        cities.document().set(data4);

        data4 = new HashMap<>();
        data4.put("locationName", "Tulip Hotel");
        data4.put("locationRegion", "Sea Side");
        data4.put("rent", "15,000");
        data4.put("bookingEnd", "30-5-2021");
        data4.put("description", "Overlooking the Arabian Sea, Tulip Hotel Faislabad is a part of the most significant chain of hotels in Pakistan. The Hotel is perfect for both business and leisure travellers. Guests can choose from a variety of rooms and suites to enjoy their stay at the five-star hotel.");
        data4.put("booked", true);
        data4.put("bookedBy", "AsadUllah");
        data4.put("locationAddress", "Faislabad");
        cities.document().set(data4);
    }

}
