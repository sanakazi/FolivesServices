package com.callndata.adapterM;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantWithdrawalsItem;

import java.util.ArrayList;

/**
 * Created by ravikant on 16-03-2016.
 */
public class MerchantWithdrawalsAdapter extends BaseAdapter {

    Context context;
    ArrayList<NewMerchantWithdrawalsItem> NewMerchantWithdrawalsAL = new ArrayList<NewMerchantWithdrawalsItem>();

    public MerchantWithdrawalsAdapter(Context context, ArrayList<NewMerchantWithdrawalsItem> NewMerchantWithdrawalsAL) {
        this.context = context;
        this.NewMerchantWithdrawalsAL = NewMerchantWithdrawalsAL;
    }

    @Override
    public int getCount() {
        return NewMerchantWithdrawalsAL.size();
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
            view = View.inflate(context, R.layout.item_new_merchant_withdrawls, null);
        } else {
            view = convertView;
        }

        final Holder holder = new Holder();

        holder.imgStatus = (ImageView) view.findViewById(R.id.imgStatus);

        holder.txtWithdrawalId = (TextView) view.findViewById(R.id.txtWithdrawalId);
        holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        holder.txtDateTime = (TextView) view.findViewById(R.id.txtDateTime);

        try {
            holder.imgStatus.setImageResource(R.drawable.but_pending);
            holder.txtWithdrawalId.setText(NewMerchantWithdrawalsAL.get(pos).getWithdrawalId());
            holder.txtPrice.setText("₹ " + NewMerchantWithdrawalsAL.get(pos).getAmount());
            holder.txtDateTime.setText(NewMerchantWithdrawalsAL.get(pos).getDateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    class Holder {
        ImageView imgStatus;
        TextView txtWithdrawalId, txtPrice, txtDateTime;
    }
}
