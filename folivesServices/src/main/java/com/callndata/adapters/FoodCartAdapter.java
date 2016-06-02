package com.callndata.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.callndata.others.DataBaseHandler;
import com.example.folivesservices.R;
import com.folives.item.FoodCartCheckout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodCartAdapter extends BaseAdapter {

    Context context;
    DataBaseHandler db;
    ArrayList<FoodCartCheckout> FoodCartCheckoutAL = new ArrayList<FoodCartCheckout>();

    TextView txtTPrice;

    public FoodCartAdapter(Context context, ArrayList<FoodCartCheckout> FoodCartCheckoutAL, TextView txtTPrice) {
        this.context = context;
        this.FoodCartCheckoutAL = FoodCartCheckoutAL;
        db = new DataBaseHandler(context);

        this.txtTPrice = txtTPrice;

    }

    @Override
    public int getCount() {
        db.open();
        int cnt = Integer.parseInt(db.getCount());
        db.close();
        return cnt;
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
            view = View.inflate(context, R.layout.item_checkout, null);
        } else {
            view = convertView;
        }

        final Holder holder = new Holder();

        holder.txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
        holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        holder.txtCount = (TextView) view.findViewById(R.id.txtCount);
        holder.imgFood = (ImageView) view.findViewById(R.id.imgFood);
        holder.imgDecrement = (ImageView) view.findViewById(R.id.imgDecrement);
        holder.imgIncrement = (ImageView) view.findViewById(R.id.imgIncrement);
        holder.imgDelete = (ImageView) view.findViewById(R.id.imgDelete);

        holder.txtFoodName.setText(FoodCartCheckoutAL.get(pos).getName());
        holder.txtPrice.setText(FoodCartCheckoutAL.get(pos).getPrice());
        holder.txtCount.setText(FoodCartCheckoutAL.get(pos).getCount());

        Picasso.with(context).load(FoodCartCheckoutAL.get(position).getImg()).into(holder.imgFood);

        final String id = FoodCartCheckoutAL.get(pos).getId();

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.open();
                db.deleteRecord(id);
                txtTPrice.setText(db.SumOfPrice());
                db.close();
                FoodCartCheckoutAL.remove(pos);
                notifyDataSetChanged();
            }
        });

        holder.imgIncrement.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String cnt = Integer.toString(Integer.parseInt(holder.txtCount.getText().toString()) + 1);
                if (Integer.parseInt(cnt) <= Integer.parseInt(FoodCartCheckoutAL.get(pos).getQty())) {
                    holder.txtCount.setText(cnt);
                    String TAmt = Integer
                            .toString(Integer.parseInt(FoodCartCheckoutAL.get(pos).getPrice()) * Integer.parseInt(cnt));
                    db.open();
                    db.updateRecord(id, cnt, TAmt);
                    txtTPrice.setText(db.SumOfPrice());
                    db.close();
                    FoodCartCheckoutAL.get(pos).setCount(cnt);
                    notifyDataSetChanged();
                    holder.imgDecrement.getDrawable().mutate().setAlpha(225);
                    if(Integer.parseInt(cnt) == Integer.parseInt(FoodCartCheckoutAL.get(pos).getQty())){
                        holder.imgIncrement.getDrawable().mutate().setAlpha(70);
                    }
                } else {
                    holder.imgIncrement.getDrawable().mutate().setAlpha(70);
//                    Toast.makeText(context, "Maximum   availability is " + FoodCartCheckoutAL.get(pos).getQty(),
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgDecrement.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String cnt = Integer.toString(Integer.parseInt(holder.txtCount.getText().toString()) - 1);
                if (Integer.parseInt(cnt) > 0) {
                    holder.txtCount.setText(cnt);
                    String TAmt = Integer
                            .toString(Integer.parseInt(FoodCartCheckoutAL.get(pos).getPrice()) * Integer.parseInt(cnt));
                    db.open();
                    db.updateRecord(id, cnt, TAmt);
                    txtTPrice.setText(db.SumOfPrice());
                    db.close();
                    FoodCartCheckoutAL.get(pos).setCount(cnt);
                    notifyDataSetChanged();
                    holder.imgIncrement.getDrawable().mutate().setAlpha(225);
                } else {
                    holder.imgDecrement.getDrawable().mutate().setAlpha(70);
//                    Toast.makeText(context, "Invalid !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    class Holder {

        TextView txtFoodName, txtPrice, txtCount;
        ImageView imgFood, imgDecrement, imgIncrement, imgDelete;

    }

}
