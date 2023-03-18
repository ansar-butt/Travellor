package com.example.travellor;

public class individualHotel implements IModel {
    private String location;
    private String locationName;
    private String rent;
    private String description;
    private boolean booked;
    private String userName;

    public individualHotel() {
        location = "Karachi";
        locationName = "Clifton";
        rent = "Rs. 1000";
        description = "Sea Side View Hotel.";
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

    public void setLocationName(String name) {
        this.locationName = name;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
