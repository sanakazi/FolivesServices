package com.callndata.adapters;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.folivesservices.R;
import com.folives.item.CustomerHomeRecentOrdersItem;

import java.util.ArrayList;

public class CustomerHomeRecentOrders extends BaseAdapter {

    Context context;
    ArrayList<CustomerHomeRecentOrdersItem> CustomerHomeRecentOrdersItemAL = new ArrayList<CustomerHomeRecentOrdersItem>();

    public CustomerHomeRecentOrders(Context context,
                                    ArrayList<CustomerHomeRecentOrdersItem> CustomerHomeRecentOrdersItemAL) {

        this.context = context;
        this.CustomerHomeRecentOrdersItemAL = CustomerHomeRecentOrdersItemAL;
    }

    @Override
    public int getCount() {
        return CustomerHomeRecentOrdersItemAL.size();
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

        if (convertView == null) {
            view = View.inflate(context, R.layout.item_customer_home_recent_orders, null);
        } else {
            view = convertView;
        }

        Holder holder = new Holder();
        holder.txtOrderNum = (TextView) view.findViewById(R.id.txtOrderNum);
        holder.txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        holder.txtAmt = (TextView) view.findViewById(R.id.txtAmt);
        holder.txtDate = (TextView) view.findViewById(R.id.txtDate);

        holder.txtOrderNum.setText(CustomerHomeRecentOrdersItemAL.get(position).getOrder_id());
        holder.txtItemName.setText(CustomerHomeRecentOrdersItemAL.get(position).getItem_name());
        holder.txtAmt.setText("₹ " + CustomerHomeRecentOrdersItemAL.get(position).getOrder_amt());
        holder.txtDate.setText(CustomerHomeRecentOrdersItemAL.get(position).getOrder_date());

        SpannableString content = new SpannableString("₹ " + CustomerHomeRecentOrdersItemAL.get(position).getOrder_amt());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.txtAmt.setText(content);

        return view;
    }

    class Holder {
        TextView txtOrderNum, txtItemName, txtAmt, txtDate;
    }

}
