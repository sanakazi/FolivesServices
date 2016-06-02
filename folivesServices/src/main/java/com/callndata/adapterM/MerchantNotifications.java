package com.callndata.adapterM;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantNotificationsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ravikant on 15-03-2016.
 */
public class MerchantNotifications extends BaseAdapter {

    Context context;
    ArrayList<NewMerchantNotificationsItem> NewMerchantNotificationsItemAL = new ArrayList<NewMerchantNotificationsItem>();

    public MerchantNotifications(Context context, ArrayList<NewMerchantNotificationsItem> NewMerchantNotificationsItemAL) {
        this.context = context;
        this.NewMerchantNotificationsItemAL = NewMerchantNotificationsItemAL;
    }

    @Override
    public int getCount() {
        return NewMerchantNotificationsItemAL.size();
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

        final int pos = position;

        View view;

        if (convertView == null) {
            view = View.inflate(context, R.layout.item_new_merchant_notifications, null);
        } else {
            view = convertView;
        }

        Holder holder = new Holder();

        holder.imgPic = (ImageView) view.findViewById(R.id.imgPic);
        holder.imgMessage = (ImageView) view.findViewById(R.id.imgMessage);

        holder.txtName = (TextView) view.findViewById(R.id.txtName);
        holder.txtMessage = (TextView) view.findViewById(R.id.txtMessage);
        holder.txtDateTime = (TextView) view.findViewById(R.id.txtDateTime);

        try {
            Picasso.with(context).load(NewMerchantNotificationsItemAL.get(pos).getPic()).fit().centerCrop().into(holder.imgPic);
            holder.txtName.setText(NewMerchantNotificationsItemAL.get(pos).getName());
            holder.txtMessage.setText(NewMerchantNotificationsItemAL.get(pos).getMessage());
            holder.txtDateTime.setText(NewMerchantNotificationsItemAL.get(pos).getDatetime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    class Holder {

        ImageView imgPic, imgMessage;
        TextView txtName, txtMessage, txtDateTime;

    }
}
