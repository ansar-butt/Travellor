package com.example.travellor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class customAdapterHome extends RecyclerView.Adapter<customAdapterHome.ViewHolder> {
    private final ArrayList<regionHolder> mRegionHolder;

    customAdapterHome(ArrayList<regionHolder> regionHolderArrayList) {
        mRegionHolder = new ArrayList<>(regionHolderArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(customAdapterHome.ViewHolder holder, int position) {
        holder.getTextView().setText(mRegionHolder.get(position).getRegionName());
        int id = R.drawable.acre;
        try {
            id = R.drawable.class.getField(mRegionHolder.get(position).getRegionName().toLowerCase().replace(" ", "_")).getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.getImageView().setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return mRegionHolder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView regionType;
        ImageView regionImage;
        private final Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            regionType = itemView.findViewById(R.id.regionName);
            regionImage = itemView.findViewById(R.id.regionImage);
            regionImage.setOnClickListener(view -> {
                Intent intent = new Intent(context, regionSelectedActivity.class);
                intent.putExtra("location", mRegionHolder.get(getAdapterPosition()).getRegionName());
                context.startActivity(intent);
            });
        }

        TextView getTextView() {
            return regionType;
        }

        ImageView getImageView() {
            return regionImage;
        }
    }

}
