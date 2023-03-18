package com.example.travellor;

import android.os.Bundle;
import android.view.Display;
import android.view.Surface;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class homeActivity extends MainActivity {

    ArrayList<regionHolder> regionList;
    RecyclerView recyclerView;
    customAdapterHome mAdapterHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regionList = operationsBL.getRegionList();

        setContentView(R.layout.activity_main);
        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewMain);
        mAdapterHome = new customAdapterHome(regionList);
        recyclerView.setAdapter(mAdapterHome);

        Display display = this.getDisplay();
        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_0)
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

}
