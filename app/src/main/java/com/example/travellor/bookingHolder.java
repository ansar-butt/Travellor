package com.example.travellor;

public class bookingHolder implements IModel {
    public String hotelLocation;
    public String hotelAddress;
    public String bookingEnd;

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String x) {
        hotelAddress = x;
    }

    public String getBookingEnd() {
        return bookingEnd;
    }

    public void setBookingEnd(String bookingEnd) {
        this.bookingEnd = bookingEnd;
    }

    public String getHotelLocation() {
        return hotelLocation;
    }

    public void setHotelLocation(String hotelLocation) {
        this.hotelLocation = hotelLocation;
    }
}
