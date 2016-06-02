package com.callndata.adapterM;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantOrderHistoryItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ravikant on 16-03-2016.
 */
public class MerchantOrderHistoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<NewMerchantOrderHistoryItem> NewMerchantOrderHistoryItemAL = new ArrayList<NewMerchantOrderHistoryItem>();

    public MerchantOrderHistoryAdapter(Context context, ArrayList<NewMerchantOrderHistoryItem> NewMerchantOrderHistoryItemAL) {
        this.context = context;
        this.NewMerchantOrderHistoryItemAL = NewMerchantOrderHistoryItemAL;
    }


    @Override
    public int getCount() {
        return NewMerchantOrderHistoryItemAL.size();
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
            view = View.inflate(context, R.layout.item_new_merchant_new_order, null);
        } else {
            view = convertView;
        }

        Holder holder = new Holder();

        holder.imgPic = (ImageView) view.findViewById(R.id.imgPic);
        holder.imgStatus = (ImageView) view.findViewById(R.id.imgStatus);

        holder.txtName = (TextView) view.findViewById(R.id.txtName);
        holder.txtOrderNum = (TextView) view.findViewById(R.id.txtOrderNum);
        holder.txtPayType = (TextView) view.findViewById(R.id.txtPayType);
        holder.txtTimeSlot = (TextView) view.findViewById(R.id.txtTimeSlot);
        holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);

        try {
            Picasso.with(context).load(NewMerchantOrderHistoryItemAL.get(position).getPic()).fit().centerCrop()
                    .into(holder.imgPic);
            holder.txtName.setText(NewMerchantOrderHistoryItemAL.get(position).getName());
            holder.txtOrderNum.setText(NewMerchantOrderHistoryItemAL.get(position).getOrderId());
            holder.txtPayType.setText(" " + NewMerchantOrderHistoryItemAL.get(position).getPaymentType() + " ");
            holder.txtTimeSlot.setText(" " + NewMerchantOrderHistoryItemAL.get(position).getTimeSlotValue() + " ");
            holder.txtPrice.setText(" ₹ " + NewMerchantOrderHistoryItemAL.get(position).getTotalAmt() + " ");

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.imgStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Status Clicked !!!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    class Holder {
        ImageView imgPic, imgStatus;
        TextView txtName, txtOrderNum, txtPayType, txtTimeSlot, txtPrice;
    }
}