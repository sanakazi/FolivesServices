package com.callndata.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.callndata.others.DataBaseHandler;
import com.example.folivesservices.R;
import com.folives.item.CustomerOrderSummeryItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomerMidnightOrderSummeryAdapter extends BaseAdapter {

    Context context;
    DataBaseHandler db;
    ArrayList<CustomerOrderSummeryItem> CustomerOrderSummeryAL = new ArrayList<CustomerOrderSummeryItem>();

    TextView txtTPrice;

    public CustomerMidnightOrderSummeryAdapter(Context context, ArrayList<CustomerOrderSummeryItem> CustomerOrderSummeryAL, TextView txtTPrice) {
        this.context = context;
        this.CustomerOrderSummeryAL = CustomerOrderSummeryAL;
        db = new DataBaseHandler(context);
        this.txtTPrice = txtTPrice;
    }

    @Override
    public int getCount() {
        return CustomerOrderSummeryAL.size();
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

        holder.txtFoodName.setText(CustomerOrderSummeryAL.get(pos).getName());
        holder.txtPrice.setText(CustomerOrderSummeryAL.get(pos).getPrice());
        holder.txtFoodCartCount.setText(CustomerOrderSummeryAL.get(pos).getCnt());

        Picasso.with(context).load(CustomerOrderSummeryAL.get(position).getImg()).into(holder.imgFood);

        final String id = CustomerOrderSummeryAL.get(pos).getId();

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.open();
                db.deleteRecord(id);
                txtTPrice.setText(db.SumOfPrice());
                db.close();
                CustomerOrderSummeryAL.remove(pos);
                notifyDataSetChanged();
            }
        });

        holder.imgIncrement.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String cnt = Integer.toString(Integer.parseInt(holder.txtFoodCartCount.getText().toString()) + 1);
                if (Integer.parseInt(cnt) <= Integer.parseInt(CustomerOrderSummeryAL.get(pos).getQtyAvl())) {
                    holder.txtFoodCartCount.setText(cnt);
                    String TAmt = Integer
                            .toString(Integer.parseInt(CustomerOrderSummeryAL.get(pos).getPrice()) * Integer.parseInt(cnt));
                    db.open();
                    db.updateRecord(id, cnt, TAmt);
                    txtTPrice.setText(db.SumOfPrice());
                    db.close();
                    CustomerOrderSummeryAL.get(pos).setCnt(cnt);
                    notifyDataSetChanged();
                } else {
//                    Toast.makeText(context, "Maximum availability is " + CustomerOrderSummeryAL.get(pos).getQtyAvl(),
//                            Toast.LENGTH_SHORT).show();

                    holder.imgDecrement.setEnabled(true);
                    holder.imgIncrement.setEnabled(false);
                    holder.imgIncrement.getDrawable().mutate().setAlpha(70);
                }
            }
        });

        holder.imgDecrement.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String cnt = Integer.toString(Integer.parseInt(holder.txtFoodCartCount.getText().toString()) - 1);
                if (Integer.parseInt(cnt) > 0) {
                    holder.txtFoodCartCount.setText(cnt);
                    String TAmt = Integer
                            .toString(Integer.parseInt(CustomerOrderSummeryAL.get(pos).getPrice()) * Integer.parseInt(cnt));
                    db.open();
                    db.updateRecord(id, cnt, TAmt);
                    txtTPrice.setText(db.SumOfPrice());
                    db.close();
                    CustomerOrderSummeryAL.get(pos).setCnt(cnt);
                    notifyDataSetChanged();
                } else {
//                    Toast.makeText(context, "Invalid !!!", Toast.LENGTH_SHORT).show();

                    holder.imgIncrement.setEnabled(true);
                    holder.imgDecrement.setEnabled(false);
                    holder.imgDecrement.getDrawable().mutate().setAlpha(70);
                }
            }
        });

        return view;
    }

    class Holder {

        TextView txtFoodName, txtPrice, txtFoodCartCount;
        ImageView imgFood, imgDecrement, imgIncrement, imgDelete;

    }

}
