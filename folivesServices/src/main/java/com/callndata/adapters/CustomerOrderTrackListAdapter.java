package com.callndata.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.folivesservices.R;
import com.folives.item.CustomerOrderTrackingListItem;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ravikant on 19-02-2016.
 */
public class CustomerOrderTrackListAdapter extends BaseAdapter {

    Context context;
    ArrayList<CustomerOrderTrackingListItem> CustomerOrderTrackingListItemAL = new ArrayList<CustomerOrderTrackingListItem>();

    public CustomerOrderTrackListAdapter(Context context, ArrayList<CustomerOrderTrackingListItem> CustomerOrderTrackingListItemAL) {
        this.context = context;
        this.CustomerOrderTrackingListItemAL = CustomerOrderTrackingListItemAL;
    }

    @Override
    public int getCount() {
        return CustomerOrderTrackingListItemAL.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        final int pos = position;

        if (convertView == null) {
            view = View.inflate(context, R.layout.item_order_tracking, null);
        } else {
            view = convertView;
        }

        final Holder holder = new Holder();
        holder.txtTime = (TextView) view.findViewById(R.id.txtTime);
        holder.txtTimeSlot = (TextView) view.findViewById(R.id.txtTimeSlot);
        holder.txtStatusName = (TextView) view.findViewById(R.id.txtStatusName);
        holder.imgFoodStatus = (ImageView) view.findViewById(R.id.imgFoodStatus);

        String formattedDate = "", formattedSlot = "";
        //SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa");
        try {
            String dateStr = CustomerOrderTrackingListItemAL.get(pos).getCreated_at();
            DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            DateFormat writeFormat = new SimpleDateFormat("hh:mm");
            DateFormat writeFormatSlot = new SimpleDateFormat("aa");
            Date date = null;
            Date date1 = null;
            try {
                date = readFormat.parse(dateStr);
                date1 = readFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                formattedDate = writeFormat.format(date);
                formattedSlot = writeFormatSlot.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.txtTime.setText(formattedDate);
        holder.txtTimeSlot.setText(formattedSlot.toUpperCase());
        holder.txtStatusName.setText(CustomerOrderTrackingListItemAL.get(pos).getStatus_name());

        try {
            Picasso.with(context).load(R.drawable.booking_accepted).into(holder.imgFoodStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    class Holder {
        TextView txtTime, txtTimeSlot, txtStatusName;
        ImageView imgFoodStatus;
    }
}
