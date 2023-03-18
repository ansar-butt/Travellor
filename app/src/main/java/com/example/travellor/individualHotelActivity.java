package com.example.travellor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class individualHotelActivity extends MainActivity {
    individualHotel currentHotel;
    Button button;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_individual_location);
        try {
            AdMob();
        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }
        Intent intent = getIntent();
        String address = intent.getStringExtra("locationAddress");
        String location = intent.getStringExtra("locationName");
        currentHotel = operationsBL.getHotel(address, location);

        setView();
    }

    private void AdMob() {
        //"ca-app-pub-6525366256533456/3958182189"
        //Test "ca-app-pub-3940256099942544/1033173712"
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i("TAG", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                // Handle the error
                Log.i("TAG", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                Log.d("TAG", "The ad was dismissed.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when fullscreen content failed to show.
                Log.d("TAG", "The ad failed to show.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                mInterstitialAd = null;
                Log.d("TAG", "The ad was shown.");
            }
        });

    }

    private void setView() {
        ImageView imageView = findViewById(R.id.hotelImage);

        int id = R.drawable.hotel_image;
        try {
            id = R.drawable.class.getField(currentHotel.getLocationName().toLowerCase().replace(" ", "_") + "_" + currentHotel.getLocation().toLowerCase().replace(" ", "_")).getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        imageView.setImageResource(id);

        TextView location = findViewById(R.id.hotelAddress);
        location.setText(currentHotel.getLocation());

        TextView locationName = findViewById(R.id.hotelLocation);
        locationName.setText(currentHotel.getLocationName());

        TextView description = findViewById(R.id.hotelDescription);
        description.setText(currentHotel.getDescription());

        TextView rent = findViewById(R.id.individualHotelRent);
        rent.setText(currentHotel.getRent());

        button = findViewById(R.id.bookingButton);
        if (currentHotel.isBooked())
            button.setText(R.string.unbook);

        button.setOnClickListener(view -> toggleBooking());
    }

    private void toggleBooking() {
        String userName = "";
        File myObj = new File(this.getFilesDir(), "user.txt");
        Scanner myReader = null;
        try {
            myReader = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (myReader != null && myReader.hasNextLine()) {
            userName = myReader.nextLine();
            myReader.close();
        }

        if (!currentHotel.isBooked()) {
            if (operationsBL.bookHotel(currentHotel.getLocationName(), currentHotel.getLocation(), userName)) {
                makeToast("Hotel Booked Successfully");
                button.setText(R.string.unbook);
            } else
                makeToast("We are Unable to Process your Request at the Moment");
        } else if (currentHotel.getUserName().equals(userName)) {
            if (operationsBL.unBookHotel(currentHotel.getLocationName(), currentHotel.getLocation()))
                if (currentHotel.isBooked())
                    button.setText(R.string.book);

        } else
            makeToast("Hotel Booked by Another Member");
        if (mInterstitialAd != null) {
            mInterstitialAd.show(individualHotelActivity.this);
        } else {
            makeToast("The interstitial ad wasn't ready yet.");
        }

    }

    private void makeToast(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }
}
