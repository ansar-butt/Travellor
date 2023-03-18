package com.example.travellor;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class bookingActivity extends MainActivity {
    private RecyclerView recyclerView;
    private customAdapterBooking mAdapterBooking;
    private ArrayList<bookingHolder> bookingList;
    private ArrayList<bookingHolder> unBookedList;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_bookings);

        setRecyclerView();

        try {
            AdMob();
        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.bookingList);

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
        }

        bookingList = operationsBL.getMyBookings(userName);

        unBookedList = new ArrayList<>();
        mAdapterBooking = new customAdapterBooking(bookingList, unBookedList);
        recyclerView.setAdapter(mAdapterBooking);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onPause() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(bookingActivity.this);
        }
        super.onPause();
        for (bookingHolder temp : unBookedList) {
            operationsBL.unBookHotel(temp.getHotelLocation(), temp.getHotelAddress());
        }
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
}
