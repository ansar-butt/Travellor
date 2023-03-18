package com.example.travellor;

public class locationBasicData implements IModel {
    private String location;
    private String locationName;
    private String rent;
    private boolean booked;

    public locationBasicData() {
        location = "Karachi";
        locationName = "Clifton";
        rent = "Rs. 1000";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public void setLoactionName(String name) {
        this.locationName = name;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
