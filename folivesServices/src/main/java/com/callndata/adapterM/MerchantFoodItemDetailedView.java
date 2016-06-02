package com.callndata.adapterM;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.folivesservices.R;
import com.folives.MItem.MerchantFoodItemDetailedViewItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ravikant on 15-03-2016.
 */
public class MerchantFoodItemDetailedView extends BaseAdapter {

    Context context;
    ArrayList<MerchantFoodItemDetailedViewItem> MerchantFoodItemDetailedViewItemAL = new ArrayList<MerchantFoodItemDetailedViewItem>();


    public MerchantFoodItemDetailedView(Context context, ArrayList<MerchantFoodItemDetailedViewItem> MerchantFoodItemDetailedViewItemAL) {
        this.context = context;
        this.MerchantFoodItemDetailedViewItemAL = MerchantFoodItemDetailedViewItemAL;
    }

    @Override
    public int getCount() {
        return MerchantFoodItemDetailedViewItemAL.size();
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
            view = View.inflate(context, R.layout.item_new_merchant_food_detailedview, null);
        } else {
            view = convertView;
        }

        Holder holder = new Holder();
        holder.imgFood = (ImageView) view.findViewById(R.id.imgFood);
        holder.txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
        holder.txtFoodCartCount = (TextView) view.findViewById(R.id.txtFoodCartCount);
        holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);

        try {
            Picasso.with(context).load(MerchantFoodItemDetailedViewItemAL.get(pos).getFoodPic()).fit().centerCrop().into(holder.imgFood);
            holder.txtFoodName.setText(MerchantFoodItemDetailedViewItemAL.get(pos).getFoodName());
            holder.txtFoodCartCount.setText(MerchantFoodItemDetailedViewItemAL.get(pos).getFoodQuantity());
            holder.txtPrice.setText(MerchantFoodItemDetailedViewItemAL.get(pos).getFoodPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    class Holder {
        ImageView imgFood;
        TextView txtFoodName, txtFoodCartCount, txtPrice;
    }
}
