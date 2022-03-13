package com.mysalonbook.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.mysalonbook.R;
import com.mysalonbook.activity.MainActivity;
import com.mysalonbook.model.Booking;

import java.util.ArrayList;

public class ShowBookingAdapter extends BaseAdapter {
    private ArrayList<Booking> bookingList;
    private MainActivity mainActivity;
    private final String from;

    public ShowBookingAdapter(ArrayList<Booking> bookingList, MainActivity mainActivity, String from) {
        this.bookingList = bookingList;
        this.mainActivity = mainActivity;
        this.from = from;
    }

    @Override
    public int getCount() {
        return bookingList.size();
    }

    @Override
    public Booking getItem(int i) {
        return bookingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder viewHolder;
        String[] slot = {"09:00 AM - 11:00 AM", "11:00 AM - 01:00 PM", "01:00 PM - 03:00 PM", "03:00 PM - 05:00 PM", "05:00 PM - 07:00 PM"};
        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        if (view == null) {
            viewHolder = new MyViewHolder();
            view = inflater.inflate(R.layout.bookings_adapter, viewGroup, false);
            viewHolder.tvJob = view.findViewById(R.id.tv_job);
            viewHolder.tvDateTime = view.findViewById(R.id.tv_date_time);
            viewHolder.tvUser = view.findViewById(R.id.tv_user);
            viewHolder.tvBookingStatus = view.findViewById(R.id.tv_booking_status);
            viewHolder.iv3DotsMenu = view.findViewById(R.id.iv_booking_menu_icon);
            view.setTag(viewHolder);
        } else viewHolder = (MyViewHolder) view.getTag();
        viewHolder.tvJob.setText(getItem(i).getJob());
        String dateTime = getItem(i).getBookedDate() + ", " + slot[getItem(i).getSlot() - 1];
        viewHolder.tvDateTime.setText(dateTime);
        viewHolder.tvUser.setText(getItem(i).getUser());
        String[] jobServed = {"Pending", "Completed"};
//        String[] cancelled = {"No", "Cancelled"};
        String bookingStatus = jobServed[getItem(i).getJobServed()];
        if (getItem(i).getCancelled() == 1) {
            bookingStatus = "Cancelled";
            viewHolder.tvBookingStatus.setTextColor(view.getContext().getResources().getColor(R.color.red));
        }
        viewHolder.tvBookingStatus.setText(bookingStatus);
        if (from.equalsIgnoreCase("old")) viewHolder.iv3DotsMenu.setVisibility(View.GONE);
        viewHolder.iv3DotsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showPopupMenu(viewHolder.iv3DotsMenu, i);
            }
        });
        return view;
    }

    private void showPopupMenu(View view, int i) {
        PopupMenu popup = new PopupMenu(mainActivity, view, Gravity.END);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.booking_menu, popup.getMenu());

        //set menu item click listener here
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(getItem(i).getId(), mainActivity, this));
        popup.show();
    }

    private static class MyViewHolder {
        TextView tvJob, tvDateTime, tvUser, tvBookingStatus;
        ImageView iv3DotsMenu;
    }
}
