package com.callndata.adapterM;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.MItem.MerchantDailyFoodMenuItem;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ravikant on 10-03-2016.
 */
public class MerchantDailyMenuAdapter extends BaseAdapter {


    Context context;
    ArrayList<MerchantDailyFoodMenuItem> CustomerFoodMenuItemAL = new ArrayList<MerchantDailyFoodMenuItem>();
    String MyTimeSlot;
    ArrayList<String> CartFoodIds = new ArrayList<String>();
    ArrayList<String> CartFoodCartCount = new ArrayList<String>();
    PopupWindow popupWindow;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;
    static Holder holder1 = null;

    public static final String TAG = MerchantDailyMenuAdapter.class.getSimpleName();

    public MerchantDailyMenuAdapter(Context context, ArrayList<MerchantDailyFoodMenuItem> CustomerFoodMenuItemAL, String MyTimeSlot) {
        this.context = context;
        this.CustomerFoodMenuItemAL = CustomerFoodMenuItemAL;
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
            view = View.inflate(context, R.layout.item_new_merchant_daily_food, null);
        } else {
            view = convertView;
        }

        final Holder holder = new Holder();

    /*    ImageView imgFoodImg, imgEdit, imgDelete, imgVegNonveg, imgFoodDetailedView;
        TextView txtFoodName, txtRating, txtPrice;
*/
        holder.imgFoodImg = (ImageView) view.findViewById(R.id.imgFoodImg);
        holder.imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
        holder.imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        holder.imgVegNonveg = (ImageView) view.findViewById(R.id.imgVegNonveg);
        holder.imgFoodDetailedView = (ImageView) view.findViewById(R.id.imgDetailedView);

        holder.txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
        holder.txtRating = (TextView) view.findViewById(R.id.txtRating);
        holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);

        Picasso.with(context).load(CustomerFoodMenuItemAL.get(position).getFoodImg()).fit().centerCrop()
                .into(holder.imgFoodImg);

        String vn = CustomerFoodMenuItemAL.get(position).getVegNonveg();
        if (vn.equals("vegetarian")) {
            holder.imgVegNonveg.setBackgroundResource(R.drawable.ic_veg);
        } else if (vn.equals("non vegetarian")) {
            holder.imgVegNonveg.setBackgroundResource(R.drawable.ic_nonveg);
        }

        holder.txtFoodName.setText(CustomerFoodMenuItemAL.get(position).getFoodName());
        holder.txtRating.setText(CustomerFoodMenuItemAL.get(position).getRating());

        SpannableString content = new SpannableString(CustomerFoodMenuItemAL.get(position).getPrice());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.txtPrice.setText(content);

        holder.imgFoodDetailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imgFoodImg, imgClose, imgVegNonVeg, imgChefImg;
                TextView txtFoodName, txtFoodDesc, txtCuisine,txtCategory, txtType, txtFoodPrice;

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
                    txtCategory = (TextView) popupView.findViewById(R.id.txtCategory);
                    txtType = (TextView) popupView.findViewById(R.id.txtType);
                    txtFoodPrice = (TextView) popupView.findViewById(R.id.txtFoodPrice);


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
                    txtCategory.setText(CustomerFoodMenuItemAL.get(pos).getCategory());

                    String Type = CustomerFoodMenuItemAL.get(pos).getFoodType().replace("_", " ");
                    txtType.setText(Type);
                    txtFoodPrice.setText("₹ " + CustomerFoodMenuItemAL.get(pos).getPrice());


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

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new EditQtyJSON().execute(CustomerFoodMenuItemAL.get(pos).getDate_ddMMyy());


                ImageView imgFoodImg, imgClose, imgVegNonVeg, imgUpdate;
                TextView txtFoodName, txtFoodDesc, txtCuisine, txtCategory, txtType, txtFoodPrice;
                final EditText etAvlQty, etQperOrder;

                try {
                    LayoutInflater layoutInflater = (LayoutInflater) context
                            .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.popup_new_merchant_dailymenu_edit, null);
                     popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT, true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    // popupWindow.setWidth(700);
                    popupWindow.setAnimationStyle(R.style.DialogAnimation);


                    imgFoodImg = (ImageView) popupView.findViewById(R.id.imgFoodImg);
                    imgClose = (ImageView) popupView.findViewById(R.id.imgClose);
                    imgVegNonVeg = (ImageView) popupView.findViewById(R.id.imgVegNonVeg);
                    imgUpdate = (ImageView) popupView.findViewById(R.id.imgUpdate);

                    txtFoodName = (TextView) popupView.findViewById(R.id.txtFoodName);
                    txtFoodDesc = (TextView) popupView.findViewById(R.id.txtFoodDesc);
                    txtCuisine = (TextView) popupView.findViewById(R.id.txtCuisine);
                    txtCategory = (TextView) popupView.findViewById(R.id.txtCategory);
                    txtType = (TextView) popupView.findViewById(R.id.txtType);
                    txtFoodPrice = (TextView) popupView.findViewById(R.id.txtFoodPrice);


                    etAvlQty = (EditText) popupView.findViewById(R.id.etAvlQty);
                    etQperOrder = (EditText) popupView.findViewById(R.id.etQperOrder);

                    popupWindow.showAtLocation(holder.imgFoodDetailedView, Gravity.CENTER, 0, 0);

                    if ((CustomerFoodMenuItemAL.get(pos).getVegNonveg()).equals("vegetarian")) {
                        imgVegNonVeg.setBackgroundResource(R.drawable.ic_veg);
                    } else if ((CustomerFoodMenuItemAL.get(pos).getVegNonveg()).equals("non vegetarian")) {
                        imgVegNonVeg.setBackgroundResource(R.drawable.ic_nonveg);
                    }

                    try {
                        Picasso.with(context).load(CustomerFoodMenuItemAL.get(pos).getFoodImg()).fit().centerCrop()
                                .into(imgFoodImg);
                        //Picasso.with(context).load(CustomerFoodMenuItemAL.get(pos).getChefImg()).into(imgChefImg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    etAvlQty.setText(CustomerFoodMenuItemAL.get(pos).getQuantity());
                    etQperOrder.setText(CustomerFoodMenuItemAL.get(pos).getQtyPerOrder());

                    txtFoodName.setText(CustomerFoodMenuItemAL.get(pos).getFoodName());
                    txtFoodDesc.setText(CustomerFoodMenuItemAL.get(pos).getFoodDesc());
                    txtCuisine.setText(CustomerFoodMenuItemAL.get(pos).getCuisine());
                    txtCategory.setText(CustomerFoodMenuItemAL.get(pos).getCategory());

                    String Type = CustomerFoodMenuItemAL.get(pos).getFoodType().replace("_", " ");
                    txtType.setText(Type);
                    txtFoodPrice.setText("₹ " + CustomerFoodMenuItemAL.get(pos).getPrice());

                    imgClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                    imgUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String DailyMenuId, ItemId, TimeSlotId, Quantity, QtyPerOrder, Date;

                            DailyMenuId = CustomerFoodMenuItemAL.get(pos).getMenu_id();
                            ItemId = CustomerFoodMenuItemAL.get(pos).getId();
                            TimeSlotId = CustomerFoodMenuItemAL.get(pos).getTime_slot_id();
                            Quantity = CustomerFoodMenuItemAL.get(pos).getQuantity();
                            QtyPerOrder = CustomerFoodMenuItemAL.get(pos).getQtyPerOrder();
                            Date = CustomerFoodMenuItemAL.get(pos).getDate();

                           new UpdateChefItemQtyJSON().execute(DailyMenuId, ItemId, TimeSlotId, etAvlQty.getText().toString(), etQperOrder.getText().toString(), Date); // Parameters remaining...
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        ImageView imgFoodImg, imgEdit, imgDelete, imgVegNonveg, imgFoodDetailedView;
        TextView txtFoodName, txtRating, txtPrice;
    }

    public class UpdateChefItemQtyJSON extends AsyncTask<String, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status;

        String DailyMenuId, ItemId, TimeSlotId, Quantity, QtyPerOrder, SelectedDate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                DailyMenuId = params[0];
                ItemId = params[1];
                TimeSlotId = params[2];
                Quantity = params[3];
                QtyPerOrder = params[4];
                SelectedDate = params[5];



                Log.w("chef edit ", "DailyMenuId = " + DailyMenuId + " ,ItemId = " + ItemId +
                        "   ,TimeSlotId = " + TimeSlotId + "  , Quantity = " + Quantity +
                        "  , QtyPerOrder = " + QtyPerOrder + "  , Date = " + SelectedDate);

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("daily_menu_id", DailyMenuId));
                nameValuePairs.add(new BasicNameValuePair("item_id", ItemId));
                nameValuePairs.add(new BasicNameValuePair("time_slot_id", TimeSlotId));
                nameValuePairs.add(new BasicNameValuePair("quantity", Quantity));
                nameValuePairs.add(new BasicNameValuePair("quantity_per_order", QtyPerOrder));
                nameValuePairs.add(new BasicNameValuePair("date", SelectedDate));

                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.NEW_MERCHANT_DAILY_FOODMENU_QTY_UPDATE);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                Log.w("response body" , responseBody);
                status = objMain.getString("success");

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
                Toast.makeText(context, "Updated...", Toast.LENGTH_SHORT).show();
                 popupWindow.dismiss();
            } else {
                Toast.makeText(context, "Oops! Try again later...", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //get details to edit the quantity per order and total qty

    public class EditQtyJSON extends AsyncTask<String, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status;

        String SelectedDate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {


                SelectedDate = params[0];

                Log.w(TAG, "chef edit ,  Date = " + SelectedDate);
                Log.w(TAG, "access token = " + AppConstants.ACCESS_TOKEN);

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("date", SelectedDate));

                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.NEW_MERCHANT_DAILY_FOODMENU);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                Log.w(TAG , responseBody);
                status = objMain.getString("success");

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

        }
    }

}
