package com.callndata.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.folivesservices.CustomerChefItemsAndReviews;
import com.callndata.others.AppConstants;
import com.callndata.others.DataBaseHandler;
import com.example.folivesservices.R;
import com.folives.item.CustomerFoodMenuItem;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerFoodMenuAdapter extends BaseAdapter {

    Context context;
    ArrayList<CustomerFoodMenuItem> CustomerFoodMenuItemAL = new ArrayList<CustomerFoodMenuItem>();

    DataBaseHandler db;
    TextView txtTotalPrice;
    RelativeLayout rlCartProCount;
    TextView txtCartProCount;

    HashMap<String, String> CartHM = new HashMap<String, String>();

    String MyTimeSlot;
    ArrayList<String> CartFoodIds = new ArrayList<String>();
    ArrayList<String> CartFoodCartCount = new ArrayList<String>();

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;
    static Holder holder1 = null;

    public CustomerFoodMenuAdapter(Context context, ArrayList<CustomerFoodMenuItem> CustomerFoodMenuItemAL,
                                   TextView txtTotalPrice, RelativeLayout rlCartProCount, TextView txtCartProCount,
                                   HashMap<String, String> CartHM, String MyTimeSlot) {
        this.context = context;
        this.CustomerFoodMenuItemAL = CustomerFoodMenuItemAL;
        this.txtTotalPrice = txtTotalPrice;
        this.rlCartProCount = rlCartProCount;
        this.txtCartProCount = txtCartProCount;
        db = new DataBaseHandler(context);
        this.CartHM = CartHM;
        this.MyTimeSlot = MyTimeSlot;
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

        holder1 = holder;
        holder1.imgFav = (ImageView) view.findViewById(R.id.imgFav);

        holder.imgFoodDetailedView = (ImageView) view.findViewById(R.id.imgFoodDetailedView);
        holder.llChefStrip = (LinearLayout) view.findViewById(R.id.llChefStrip);

        Picasso.with(context).load(CustomerFoodMenuItemAL.get(position).getFoodImg()).fit().centerCrop()
                .into(holder.imgFoodImg);
        Picasso.with(context).load(CustomerFoodMenuItemAL.get(position).getChefImg()).into(holder.imgChefPic);

        String vn = CustomerFoodMenuItemAL.get(position).getVegNonveg();
        if (vn.equals("vegetarian")) {
            holder.imgVegNonveg.setBackgroundResource(R.drawable.ic_veg);
        } else if (vn.equals("non vegetarian")) {
            holder.imgVegNonveg.setBackgroundResource(R.drawable.ic_nonveg);
        }
        if ((CustomerFoodMenuItemAL.get(position).getFav()).equals("1")) {
//            holder.imgFav.setBackgroundResource(R.drawable.ic_favorites_red);
            holder.imgFav.setImageResource(R.drawable.ic_favorites_red);
            holder.imgFav.setTag("1");
        } else {
//            holder.imgFav.setBackgroundResource(R.drawable.ic_favorites_heart);
            holder.imgFav.setImageResource(R.drawable.ic_favorites_heart);
            holder.imgFav.setTag("0");
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
                intent.putExtra("chefId", CustomerFoodMenuItemAL.get(pos).getChef_id());
                context.startActivity(intent);
            }
        });

        holder.llChefStrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Chef", Toast.LENGTH_SHORT).show();
                context.sendBroadcast(new Intent("start.fragment.action"));
                Intent intent = new Intent(context, CustomerChefItemsAndReviews.class);
                intent.putExtra("chefId", CustomerFoodMenuItemAL.get(pos).getChef_id());
                context.startActivity(intent);
            }
        });

        holder.imgAddToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Toast.makeText(context, "Cart", Toast.LENGTH_SHORT).show();

                if (Integer.parseInt(CustomerFoodMenuItemAL.get(pos).getQuantity()) > Integer
                        .parseInt(holder.txtCartCount.getText().toString())) {


                    // holder.imgAddToCart.getDrawable().mutate().setAlpha(70);
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
                        rlCartProCount.setVisibility(View.GONE);  //VISIBLE
                        txtCartProCount.setText(db.getCount());
                    } else {
                        rlCartProCount.setVisibility(View.GONE);
                    }
                    db.close();

                    if (Integer.parseInt(CustomerFoodMenuItemAL.get(pos).getQuantity()) == Integer
                            .parseInt(holder.txtCartCount.getText().toString())) {
                        holder.imgAddToCart.getDrawable().mutate().setAlpha(70);
                    }


                } else {
                    holder.imgAddToCart.getDrawable().mutate().setAlpha(70);
//                    Toast.makeText(context, CustomerFoodMenuItemAL.get(pos).getFoodName() + " max available is quantity"
//                            + CustomerFoodMenuItemAL.get(pos).getQuantity(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (holder.imgFav.getTag().equals("1")) {
                        new CustomerFavouriteChangeJSON().execute(CustomerFoodMenuItemAL.get(pos).getId(), "0");
                        holder.imgFav.setImageResource(R.drawable.ic_favorites_heart);
                        CustomerFoodMenuItemAL.get(pos).setFav("0");
                        holder.imgFav.setTag("0");
                    } else if (holder.imgFav.getTag().equals("0")) {
                        new CustomerFavouriteChangeJSON().execute(CustomerFoodMenuItemAL.get(pos).getId(), "1");
                        holder.imgFav.setImageResource(R.drawable.ic_favorites_red);
                        CustomerFoodMenuItemAL.get(pos).setFav("1");
                        holder.imgFav.setTag("1");
                    } else {
                        Toast.makeText(context, "No Image...", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        holder.imgFoodDetailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imgFoodImg, imgClose, imgVegNonVeg, imgChefImg;
                TextView txtFoodName, txtFoodDesc, txtCuisine, txtType, txtFoodPrice, txtChefName, txtRating;

                try {
                    LayoutInflater layoutInflater = (LayoutInflater) context
                            .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.popup_item_detailed_view, null);
                    final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT, true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    // popupWindow.setWidth(700);
                    popupWindow.setAnimationStyle(R.style.DialogAnimation);


                    imgFoodImg = (ImageView) popupView.findViewById(R.id.imgFoodImg);
                    imgClose = (ImageView) popupView.findViewById(R.id.imgClose);
                    imgVegNonVeg = (ImageView) popupView.findViewById(R.id.imgVegNonVeg);
                    imgChefImg = (ImageView) popupView.findViewById(R.id.imgChefImg);

                    txtFoodName = (TextView) popupView.findViewById(R.id.txtFoodName);
                    txtFoodDesc = (TextView) popupView.findViewById(R.id.txtFoodDesc);
                    txtCuisine = (TextView) popupView.findViewById(R.id.txtCuisine);
                    txtType = (TextView) popupView.findViewById(R.id.txtType);
                    txtFoodPrice = (TextView) popupView.findViewById(R.id.txtFoodPrice);
                    txtChefName = (TextView) popupView.findViewById(R.id.txtChefName);
                    txtRating = (TextView) popupView.findViewById(R.id.txtRating);

                    popupWindow.showAtLocation(holder.imgFoodDetailedView, Gravity.CENTER, 0, 0);

                    if ((CustomerFoodMenuItemAL.get(pos).getVegNonveg()).equals("vegetarian")) {
                        imgVegNonVeg.setBackgroundResource(R.drawable.ic_veg);
                    } else if ((CustomerFoodMenuItemAL.get(pos).getVegNonveg()).equals("non vegetarian")) {
                        imgVegNonVeg.setBackgroundResource(R.drawable.ic_nonveg);
                    }

                    try {
                        Picasso.with(context).load(CustomerFoodMenuItemAL.get(pos).getFoodImg()).fit().centerCrop()
                                .into(imgFoodImg);
                        Picasso.with(context).load(CustomerFoodMenuItemAL.get(pos).getChefImg()).into(imgChefImg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    txtFoodName.setText(CustomerFoodMenuItemAL.get(pos).getFoodName());
                    txtFoodDesc.setText(CustomerFoodMenuItemAL.get(pos).getFoodDesc());
                    txtCuisine.setText(CustomerFoodMenuItemAL.get(pos).getCuisine());

                    String Type = CustomerFoodMenuItemAL.get(pos).getFoodType().replace("_", " ");
                    txtType.setText(Type);
                    txtFoodPrice.setText("₹ " + CustomerFoodMenuItemAL.get(pos).getPrice());
                    txtChefName.setText(CustomerFoodMenuItemAL.get(pos).getChefName().toUpperCase());
                    txtRating.setText(CustomerFoodMenuItemAL.get(pos).getRating());

                    imgClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                }
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

                if (Integer.parseInt(CustomerFoodMenuItemAL.get(pos).getQuantity()) == Integer
                        .parseInt(holder.txtCartCount.getText().toString())) {
                    holder.imgAddToCart.getDrawable().mutate().setAlpha(70);
                }
                break;
            }
        }

        int TimeSlotVal = Integer.parseInt(AppConstants.TimeSlotValue);

        if (Integer.parseInt(MyTimeSlot) >= TimeSlotVal) {
            holder.imgAddToCart.setEnabled(true);
        } else {
            holder.imgAddToCart.setEnabled(false);
            holder.imgAddToCart.getDrawable().mutate().setAlpha(70);
            holder.imgAddToCart.invalidate();
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
        LinearLayout llChefStrip;
        ImageView imgFoodImg, imgChefPic, imgVegNonveg, imgAddToCart, imgFav, imgFoodDetailedView;
        TextView txtFoodName, txtChefName, txtRating, txtPrice, txtCartCount;
    }

    class CustomerFavouriteChangeJSON extends AsyncTask<String, Void, Void> {

        String status = "0";
        String id, fav_key;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            id = params[0];
            fav_key = params[1];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("id", id));
                nameValuePairs.add(new BasicNameValuePair("type", "1"));
                nameValuePairs.add(new BasicNameValuePair("fav_key", fav_key));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.FAVOURITE_CHANGE_JSON);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();
                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (status.equals("true")) {
                if (fav_key.equals("0")) {
                    Toast.makeText(context, "Unfavourite", Toast.LENGTH_SHORT).show();
//                    holder1.imgFav.setImageResource(R.drawable.ic_favorites_heart);
//                    holder1.imgFav.setTag("0");
                } else if (fav_key.equals("1")) {
                    Toast.makeText(context, "Favourite", Toast.LENGTH_SHORT).show();
//                    holder1.imgFav.setImageResource(R.drawable.ic_favorites_red);
//                    holder1.imgFav.setTag("1");
                }

            } else if (status.equals("false")) {
                Toast.makeText(context, "Wrong Opertion...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
