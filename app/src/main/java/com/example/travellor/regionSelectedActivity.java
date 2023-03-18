package com.example.travellor;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class regionSelectedActivity extends MainActivity implements Filterable {
    ArrayList<locationBasicData> regionList;
    ArrayList<locationBasicData> mFilteredData;
    RegionFilter filter;
    RecyclerView recyclerView;
    customAdapterRegionSelected mAdapterRegionSelected;
    EditText text;

    //Add Filter View
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String regionName = intent.getStringExtra("location");
        setContentView(R.layout.activity_region_selected);
        regionList = operationsBL.getHotelsInRegion(regionName);
        mFilteredData = regionList;

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewSelectedRegion);
        mAdapterRegionSelected = new customAdapterRegionSelected(regionList);
        recyclerView.setAdapter(mAdapterRegionSelected);
        getText();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private EditText getText(){
        text = findViewById(R.id.locationFilter);

        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                getFilter().filter(text.toString());
            }

        });
        return text;
    }

    @Override
    public Filter getFilter() {
        if (filter==null)
            filter = new RegionFilter();
        return filter;
    }

    private class RegionFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<locationBasicData> filteredList = new ArrayList<>();
                for (int i = 0; i < regionList.size(); i++) {
                    if (regionList.get(i).getLocationName().equals(constraint.toString()))
                        filteredList.add(regionList.get(i));
                }
                results.count = filteredList.size();
                results.values = filteredList;
            } else {
                results.count = regionList.size();
                results.values = regionList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            int size = mAdapterRegionSelected.getItemCount();
            ArrayList<locationBasicData> temp = (ArrayList<locationBasicData>) results.values;
            if (temp.size() > 0) {
                mFilteredData = new ArrayList<>(temp);
                mAdapterRegionSelected.modify(mFilteredData);
                mAdapterRegionSelected.notifyDataSetChanged();
            }
        }

    }

}
