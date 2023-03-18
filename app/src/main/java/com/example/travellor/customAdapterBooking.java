package com.example.travellor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class customAdapterBooking extends RecyclerView.Adapter<customAdapterBooking.ViewHolder> {
    private final ArrayList<bookingHolder> mBookingList;
    private final ArrayList<bookingHolder> mUnBookedList;

    public customAdapterBooking(ArrayList<bookingHolder> bookingList, ArrayList<bookingHolder> unBookedList) {
        mBookingList = bookingList;
        mUnBookedList = unBookedList;
    }

    @NonNull
    @Override
    public customAdapterBooking.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new customAdapterBooking.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getTextView("hotelName").setText(mBookingList.get(position).getHotelLocation());
        holder.getTextView("bookingEnd").setText(mBookingList.get(position).getBookingEnd());
        holder.getButton();
    }

    @Override
    public int getItemCount() {
        return mBookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hotelName;
        TextView bookingEnd;
        Button cancelBooking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hotelName = itemView.findViewById(R.id.bookingLoaction);
            bookingEnd = itemView.findViewById(R.id.bookingEnd);
            cancelBooking = itemView.findViewById(R.id.unBook);

            cancelBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    mUnBookedList.add(mBookingList.get(position));
                    mBookingList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        TextView getTextView(String type) {
            if (type.equals("hotelName"))
                return hotelName;
            else
                return bookingEnd;
        }

        Button getButton() {
            return cancelBooking;
        }
    }
}
