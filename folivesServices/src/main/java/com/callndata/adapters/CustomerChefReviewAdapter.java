package com.callndata.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.folivesservices.R;
import com.folives.item.CustomerChefReviewsItem;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomerChefReviewAdapter extends BaseAdapter {

    Context context;
    ArrayList<CustomerChefReviewsItem> CustomerChefReviewsItemAL = new ArrayList<CustomerChefReviewsItem>();

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public CustomerChefReviewAdapter(Context context, ArrayList<CustomerChefReviewsItem> CustomerChefReviewsItemAL) {
        this.context = context;
        this.CustomerChefReviewsItemAL = CustomerChefReviewsItemAL;
    }

    @Override
    public int getCount() {
        return CustomerChefReviewsItemAL.size();
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
            view = View.inflate(context, R.layout.item_customer_chef_review, null);
        } else {
            view = convertView;
        }

        Holder holder = new Holder();

        holder.imgPic = (ImageView) view.findViewById(R.id.imgPic);
        holder.txtName = (TextView) view.findViewById(R.id.txtName);
        holder.txtReview = (TextView) view.findViewById(R.id.txtReview);
        holder.txtRating = (TextView) view.findViewById(R.id.txtRating);
        holder.txtDate = (TextView) view.findViewById(R.id.txtDate);

        Picasso.with(context).load(CustomerChefReviewsItemAL.get(position).getImage()).into(holder.imgPic);
        holder.txtName.setText(CustomerChefReviewsItemAL.get(position).getName());
        holder.txtReview.setText(CustomerChefReviewsItemAL.get(position).getReview());
        holder.txtRating.setText(CustomerChefReviewsItemAL.get(position).getRating());
        try {
            holder.txtDate
                    .setText(getTimeAgo(getDateInMillis(CustomerChefReviewsItemAL.get(position).getDate()), context));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    class Holder {
        ImageView imgPic;
        TextView txtName, txtReview, txtRating, txtDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static long getDateInMillis(String srcDate) throws java.text.ParseException {
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

}
