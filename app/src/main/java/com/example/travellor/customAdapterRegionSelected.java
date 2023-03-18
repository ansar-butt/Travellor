package com.example.travellor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class customAdapterRegionSelected extends RecyclerView.Adapter<customAdapterRegionSelected.ViewHolder> {
    private ArrayList<locationBasicData> mRegionHolder;

    public customAdapterRegionSelected(ArrayList<locationBasicData> mRegionHolder) {
        this.mRegionHolder = new ArrayList<>(mRegionHolder);
    }

    @NonNull
    @Override
    public customAdapterRegionSelected.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.region_selected_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(customAdapterRegionSelected.ViewHolder holder, int position) {
        holder.getTextView("location").setText(mRegionHolder.get(position).getLocation());
        holder.getTextView("locationName").setText(mRegionHolder.get(position).getLocationName());
        holder.getTextView("rent").setText(mRegionHolder.get(position).getRent());
        int id = R.drawable.hotel_image;
        try {
            id = R.drawable.class.getField(mRegionHolder.get(position).getLocationName().toLowerCase().replace(" ", "_") + "_" + mRegionHolder.get(position).getLocation().toLowerCase().replace(" ", "_")).getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        holder.getImageView().setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return mRegionHolder.size();
    }

    public void modify(ArrayList<locationBasicData> mFilteredData) {
        mRegionHolder = mFilteredData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected final Context context;
        TextView locationName;
        TextView location;
        TextView rent;
        ImageView locationImage;
        Button booked;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            locationName = itemView.findViewById(R.id.hotelName);
            locationName.setOnClickListener(new clickEvent());

            locationImage = itemView.findViewById(R.id.locationImage);
            locationImage.setOnClickListener(new clickEvent());

            location = itemView.findViewById(R.id.hotelLocationRegion);
            location.setOnClickListener(new clickEvent());

            rent = itemView.findViewById(R.id.hotelRent);
            rent.setOnClickListener(new clickEvent());
        }

        TextView getTextView(String type) {
            if (type.equals("locationName"))
                return locationName;
            else if (type.equals("location"))
                return location;
            else
                return rent;
        }

        ImageView getImageView() {
            return locationImage;
        }

        public class clickEvent implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, individualHotelActivity.class);
                intent.putExtra("locationAddress", mRegionHolder.get(getAdapterPosition()).getLocation());
                intent.putExtra("locationName", mRegionHolder.get(getAdapterPosition()).getLocationName());
                context.startActivity(intent);
            }
        }
    }
}
