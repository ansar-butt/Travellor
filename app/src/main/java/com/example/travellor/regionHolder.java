package com.example.travellor;

public class regionHolder implements IModel {
    private String regionName;

    regionHolder() {
        regionName = "";
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String _name) {
        regionName = _name;
    }

}
