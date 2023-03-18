package com.example.travellor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String username = intent.getStringExtra("username");

        IOperations operations = new Operations();
        ArrayList<String> arr = operations.resetHotelBookings(username);

        String bookingLocation = "";
        for (int i=0; i<arr.size();i++)
            if (i<arr.size()-1){
               String temp = arr.get(i) + ", ";
               bookingLocation += temp;
            }
            else {
                bookingLocation+=arr.get(i);
            }

        String text = "";
        if (arr.size()>0)
            text = "Booking for "+bookingLocation+" has been Cancelled";
        else
            text = "No Bookings Over Due";
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
