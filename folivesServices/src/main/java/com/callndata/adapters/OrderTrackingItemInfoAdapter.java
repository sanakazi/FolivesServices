package com.callndata.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.callndata.others.DataBaseHandler;
import com.example.folivesservices.R;
import com.folives.item.OrderTrackingFoodItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderTrackingItemInfoAdapter extends BaseAdapter {

    Context context;
    DataBaseHandler db;
    ArrayList<OrderTrackingFoodItem> OrderTrackingFoodItemAL = new ArrayList<OrderTrackingFoodItem>();

    TextView txtTPrice;

    public OrderTrackingItemInfoAdapter(Context context, ArrayList<OrderTrackingFoodItem> OrderTrackingFoodItemAL) {
        this.context = context;
        this.OrderTrackingFoodItemAL = OrderTrackingFoodItemAL;
    }

    @Override
    public int getCount() {
        return OrderTrackingFoodItemAL.size();
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
            view = View.inflate(context, R.layout.item_customer_order_summery, null);
        } else {
            view = convertView;
        }

        final Holder holder = new Holder();

        holder.txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
        holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        holder.txtFoodCartCount = (TextView) view.findViewById(R.id.txtFoodCartCount);
        holder.imgFood = (ImageView) view.findViewById(R.id.imgFood);

        holder.imgDecrement = (ImageView) view.findViewById(R.id.imgDecrement);
        holder.imgIncrement = (ImageView) view.findViewById(R.id.imgIncrement);
        holder.imgDelete = (ImageView) view.findViewById(R.id.imgDelete);

        holder.imgDecrement.setVisibility(View.INVISIBLE);
        holder.imgIncrement.setVisibility(View.INVISIBLE);
        holder.imgDelete.setVisibility(View.INVISIBLE);

        holder.txtFoodName.setText(OrderTrackingFoodItemAL.get(pos).getItem_name());
        holder.txtPrice.setText(OrderTrackingFoodItemAL.get(pos).getPrice());
        holder.txtFoodCartCount.setText(OrderTrackingFoodItemAL.get(pos).getQty());
        Picasso.with(context).load(OrderTrackingFoodItemAL.get(position).getPic()).into(holder.imgFood);

        return view;
    }

    class Holder {

        TextView txtFoodName, txtPrice, txtFoodCartCount;
        ImageView imgFood, imgDecrement, imgIncrement, imgDelete;

    }

}
