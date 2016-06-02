package com.callndata.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.folivesservices.CustomerChefItemsAndReviews;
import com.callndata.others.DataBaseHandler;
import com.example.folivesservices.R;
import com.folives.item.CustomerFoodMenuItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ravikant on 08-02-2016.
 */
public class FoodCartMaintainAdapter extends BaseAdapter {

    Context context;
    ArrayList<CustomerFoodMenuItem> CustomerFoodMenuItemAL = new ArrayList<CustomerFoodMenuItem>();

    DataBaseHandler db;
    TextView txtTotalPrice;
    RelativeLayout rlCartProCount;
    TextView txtCartProCount;

    HashMap<String, String> CartHM = new HashMap<String, String>();
    ArrayList<String> CartFoodIds = new ArrayList<String>();
    ArrayList<String> CartFoodCartCount = new ArrayList<String>();

    public FoodCartMaintainAdapter(Context context, ArrayList<CustomerFoodMenuItem> CustomerFoodMenuItemAL,
                                   TextView txtTotalPrice, RelativeLayout rlCartProCount, TextView txtCartProCount,
                                   HashMap<String, String> CartHM) {
        this.context = context;
        this.CustomerFoodMenuItemAL = CustomerFoodMenuItemAL;
        this.txtTotalPrice = txtTotalPrice;
        this.rlCartProCount = rlCartProCount;
        this.txtCartProCount = txtCartProCount;
        db = new DataBaseHandler(context);
        this.CartHM = CartHM;
    }

    @Override
    public int getCount() {
        return CustomerFoodMenuItemAL.size();
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
            view = View.inflate(context, R.layout.item_customer_fooditem, null);
        } else {
            view = convertView;
        }

        final Holder holder = new Holder();

        holder.imgFoodImg = (ImageView) view.findViewById(R.id.imgFoodImg);
        holder.imgChefPic = (ImageView) view.findViewById(R.id.imgChefPic);
        holder.imgVegNonveg = (ImageView) view.findViewById(R.id.imgVegNonveg);

        holder.txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
        holder.txtChefName = (TextView) view.findViewById(R.id.txtChefName);
        holder.txtRating = (TextView) view.findViewById(R.id.txtRating);
        holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);

        holder.txtCartCount = (TextView) view.findViewById(R.id.txtCartCount);
        holder.imgAddToCart = (ImageView) view.findViewById(R.id.imgAddToCart);
        holder.rlCartCount = (RelativeLayout) view.findViewById(R.id.rlCartCount);

        holder.imgFav = (ImageView) view.findViewById(R.id.imgFav);

        Picasso.with(context).load(CustomerFoodMenuItemAL.get(position).getFoodImg()).fit().centerCrop()
                .into(holder.imgFoodImg);
        Picasso.with(context).load(CustomerFoodMenuItemAL.get(position).getChefImg()).into(holder.imgChefPic);

        String vn = CustomerFoodMenuItemAL.get(position).getVegNonveg();
        if (vn.equals("vegetarian")) {
            holder.imgVegNonveg.setBackgroundResource(R.drawable.ic_veg);
        } else if (vn.equals("non vegetarian")) {
            holder.imgVegNonveg.setBackgroundResource(R.drawable.ic_nonveg);
        }

        holder.txtFoodName.setText(CustomerFoodMenuItemAL.get(position).getFoodName());
        holder.txtChefName.setText(CustomerFoodMenuItemAL.get(position).getChefName());
        holder.txtRating.setText(CustomerFoodMenuItemAL.get(position).getRating());

        SpannableString content = new SpannableString(CustomerFoodMenuItemAL.get(position).getPrice());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.txtPrice.setText(content);


        holder.imgChefPic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Chef", Toast.LENGTH_SHORT).show();
                context.sendBroadcast(new Intent("start.fragment.action"));

                Intent intent = new Intent(context, CustomerChefItemsAndReviews.class);
                context.startActivity(intent);
            }
        });

        holder.imgAddToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Toast.makeText(context, "Cart", Toast.LENGTH_SHORT).show();

                if (Integer.parseInt(CustomerFoodMenuItemAL.get(pos).getQuantity()) > Integer
                        .parseInt(holder.txtCartCount.getText().toString())) {
                    holder.rlCartCount.setVisibility(View.VISIBLE);
                    holder.txtCartCount
                            .setText(Integer.toString(Integer.parseInt(holder.txtCartCount.getText().toString()) + 1));

                    ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .0f,
                            ScaleAnimation.ABSOLUTE, .0f);
                    scale.setDuration(500);
                    scale.setInterpolator(new OvershootInterpolator());
                    holder.rlCartCount.startAnimation(scale);
                    // holder.imgAddToCart.startAnimation(scale);

                    String menu_id, id, name, price, count, t_price, img, qty, cat;

                    menu_id = CustomerFoodMenuItemAL.get(pos).getMenu_id();
                    id = CustomerFoodMenuItemAL.get(pos).getId();
                    name = CustomerFoodMenuItemAL.get(pos).getFoodName();
                    price = CustomerFoodMenuItemAL.get(pos).getPrice();
                    count = holder.txtCartCount.getText().toString();
                    t_price = Integer.toString(Integer.parseInt(count) * Integer.parseInt(price));
                    img = CustomerFoodMenuItemAL.get(pos).getFoodImg();
                    qty = CustomerFoodMenuItemAL.get(pos).getQuantity();
                    cat = CustomerFoodMenuItemAL.get(pos).getCategory();

                    db.open();

                    if (DataBaseHandler.CheckIsIdExist(id)) {
                        db.updateFoodCart(id, count);
                        db.updateFoodCartTPrice(id, t_price);
                    } else {
                        db.insertDataIntoTable(menu_id, id, name, price, count, t_price, img, qty, cat);
                    }

                    txtTotalPrice.setText(db.SumOfPrice());
                    if (Integer.parseInt(db.getCount()) > 0) {
                        rlCartProCount.setVisibility(View.VISIBLE);
                        txtCartProCount.setText(db.getCount());
                    } else {
                        rlCartProCount.setVisibility(View.GONE);
                    }
                    db.close();
                } else {
                    Toast.makeText(context, CustomerFoodMenuItemAL.get(pos).getFoodName() + " max available is quantity"
                            + CustomerFoodMenuItemAL.get(pos).getQuantity(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        //
        db.open();
        Cursor c = db.FetchFoodIds();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    CartFoodIds.add(c.getString(c.getColumnIndex("column_food_id")));
                    CartFoodCartCount.add(c.getString(c.getColumnIndex("column_cart_count")));
                } while (c.moveToNext());
            }
        }
        db.close();


        for (int i = 0; i < CartFoodIds.size(); i++) {
            if (CustomerFoodMenuItemAL.get(pos).getId().equals(CartFoodIds.get(i))) {
                holder.rlCartCount.setVisibility(View.VISIBLE);
                holder.txtCartCount.setText(CartFoodCartCount.get(i));
                break;
            }
        }
        //

//        if (CartHM.size() != 0 && CartHM.containsKey(CustomerFoodMenuItemAL.get(pos).getId())) {
//            String cnt = CartHM.get(CustomerFoodMenuItemAL.get(pos).getId());
//            if (cnt == null) {
//                holder.rlCartCount.setVisibility(View.GONE);
//            } else {
//                holder.rlCartCount.setVisibility(View.VISIBLE);
//                holder.txtCartCount.setText(cnt);
//            }
//        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return CustomerFoodMenuItemAL.size();
    }

    class Holder {
        RelativeLayout rlCartCount;
        ImageView imgFoodImg, imgChefPic, imgVegNonveg, imgAddToCart, imgFav;
        TextView txtFoodName, txtChefName, txtRating, txtPrice, txtCartCount;
    }
}
