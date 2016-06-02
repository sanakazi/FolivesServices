package com.callndata.adapterM;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantFollowersItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MerchantFollowers extends BaseAdapter {

    Context context;
    ArrayList<NewMerchantFollowersItem> NewMerchantFollowersItemAL = new ArrayList<NewMerchantFollowersItem>();

    public MerchantFollowers(Context context, ArrayList<NewMerchantFollowersItem> NewMerchantFollowersItemAL) {
        this.context = context;
        this.NewMerchantFollowersItemAL = NewMerchantFollowersItemAL;
    }


    @Override
    public int getCount() {
        return NewMerchantFollowersItemAL.size();
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
            view = View.inflate(context, R.layout.item_new_merchant_followers, null);
        } else {
            view = convertView;
        }

        Holder holder = new Holder();
        holder.imgPic = (ImageView) view.findViewById(R.id.imgPic);
        holder.txtName = (TextView) view.findViewById(R.id.txtName);

        try {
            Picasso.with(context).load(NewMerchantFollowersItemAL.get(pos).getPic()).fit().centerCrop().into(holder.imgPic);
            holder.txtName.setText(NewMerchantFollowersItemAL.get(pos).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    class Holder {
        ImageView imgPic;
        TextView txtName;
    }
}
