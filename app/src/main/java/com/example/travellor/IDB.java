package com.example.travellor;

import android.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IDB {
    void initDB() throws IOException;

    ArrayList<String> getObjectReference(String identifierValue, ModelType modelType);//Get object from Any Identifier

    ArrayList<IModel> getCollection(ModelType modelType, ArrayList<String> ids) throws Exception; //Gets entire collection of a Particular Type

    IModel getObject(String objectId, ModelType modelType) throws Exception; //returns object with key == objectId

    ArrayList<IModel> getObjectsList(ArrayList<String> objectIds, ModelType modelType) throws Exception; //returns objects list with keys matching objectIds

    ArrayList<IModel> getObjectsList(HashMap<String, Object> attributesToQuery, ModelType modelType) throws Exception; //returns objects list with attributesToQuery Condition

    String addObject(IModel object, ModelType modelType) throws Exception;   //returns objectId of new created object

    boolean removeObject(String objectId, ModelType modelType) throws Exception;  //removes object , and returns boolean

    /*
       updateObject supports any number of Attribute Overwrite operations on a single object at a time
    */
    boolean updateObject(String objectId, HashMap<String, Object> attributesToBeUpdated, ModelType modelType) throws Exception;  //fo update variable(s) operations

    /*
    updateArrayObject supports only one Add/Remove Operation on only one Array (of an object) at a time
     */
    boolean updateArrayObject(String objectId, Pair<String, Object> arrayAttributeToBeUpdated, UpdateOperation updateOperation, ModelType modelType) throws Exception; //for update Array operation


    enum ModelType {
        bookingHolder,
        individualHotel,
        locationBasicData,
        regionHolder,
        user,
        hotel
    }

    enum UpdateOperation {
        Add,
        Remove
    }
}
