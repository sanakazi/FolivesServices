package com.callndata.folivesservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.callndata.adapters.CustomerOrderTrackListAdapter;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.item.CustomerOrderTrackingListItem;
import com.folives.item.OrderTrackingFoodItem;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ravikant on 18-02-2016.
 */
public class CustomerOrderTracking extends Activity {

    ArrayList<NameValuePair> nameValuePairs;
    ProgressDialog pDialog;

    String Order_Id;
    String DeliveryTimeHeader, DeliveryTimeOrder, DeliveryAddress, ChefPic, ChefName, TotalAmount;
    ImageView imgInfo;

    OrderTrackingFoodItem OrderTrackingFoodItemItem;
    ArrayList<OrderTrackingFoodItem> OrderTrackingFoodItemAL = new ArrayList<OrderTrackingFoodItem>();

    ListView lstOrderTracking;

    CustomerOrderTrackingListItem CustomerOrderTrackingListItemItem;
    ArrayList<CustomerOrderTrackingListItem> CustomerOrderTrackingListItemAL = new ArrayList<CustomerOrderTrackingListItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        Order_Id = getIntent().getExtras().getString("order_id");


        imgInfo = (ImageView) findViewById(R.id.imgInfo);
        lstOrderTracking = (ListView) findViewById(R.id.lstOrderTracking);

        new OrderTrackingJSON().execute(Order_Id);

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerOrderTracking.this, CustomerOrderTrackOrderInfoActivity.class);
                intent.putExtra("DeliveryTimeHeader", DeliveryTimeHeader);
                intent.putExtra("DeliveryTimeOrder", DeliveryTimeOrder);
                intent.putExtra("DeliveryAddress", DeliveryAddress);
                intent.putExtra("ChefPic", ChefPic);
                intent.putExtra("ChefName", ChefName);
                intent.putExtra("TotalAmount", TotalAmount);
                intent.putExtra("ItemAL", (Serializable) OrderTrackingFoodItemAL);
                startActivity(intent);
            }
        });
    }

    public class OrderTrackingJSON extends AsyncTask<String, Integer, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CustomerOrderTracking.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String orderId = params[0];

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("order_id", orderId));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_TRACK_ORDER);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {

                    JSONArray arrOrderStatus = new JSONArray();
                    JSONObject objOrderStatus = new JSONObject();

                    arrOrderStatus = obj.getJSONArray("order_status");
                    for (int k = 0; k < arrOrderStatus.length(); k++) {
                        objOrderStatus = arrOrderStatus.getJSONObject(k);

                        CustomerOrderTrackingListItemItem = new CustomerOrderTrackingListItem();
                        CustomerOrderTrackingListItemItem.setOrder_id(objOrderStatus.getString("order_id"));
                        CustomerOrderTrackingListItemItem.setStatus_id(objOrderStatus.getString("stats_id"));
                        CustomerOrderTrackingListItemItem.setCreated_at(objOrderStatus.getString("created_at"));

                        JSONArray arrStatus = new JSONArray();
                        JSONObject objStatus = new JSONObject();

                        arrStatus = objOrderStatus.getJSONArray("status");
                        objStatus = arrStatus.getJSONObject(0);

                        CustomerOrderTrackingListItemItem.setStatus_name(objStatus.getString("order_status_name"));
                        CustomerOrderTrackingListItemAL.add(CustomerOrderTrackingListItemItem);
                    }


                    JSONArray arrOrderDetails = new JSONArray();
                    JSONObject objOrderDetails = new JSONObject();

                    arrOrderDetails = obj.getJSONArray("order_details");
                    for (int i = 0; i < arrOrderDetails.length(); i++) {
                        objOrderDetails = arrOrderDetails.getJSONObject(i);

                        TotalAmount = "₹ " + objOrderDetails.getString("sub_total");
                        DeliveryTimeOrder = objOrderDetails.getString("delivery_time");


                        JSONObject objUserAddr = new JSONObject();
                        objUserAddr = objOrderDetails.getJSONObject("user_order_address");
                        DeliveryAddress = objUserAddr.getString("address_name");

                        JSONObject objChef = new JSONObject();
                        objChef = objOrderDetails.getJSONObject("chef");
                        ChefName = objChef.getString("name");
                        ChefPic = AppConstants.Customer_Chef_Image_path + objChef.getString("logo");

                        JSONArray arrFood = new JSONArray();
                        arrFood = objOrderDetails.getJSONArray("order_details");
                        for (int j = 0; i < arrFood.length(); i++) {

                            JSONObject objItem = new JSONObject();
                            objItem = arrFood.getJSONObject(j);

                            OrderTrackingFoodItemItem = new OrderTrackingFoodItem();
                            OrderTrackingFoodItemItem.setOrder_id(objItem.getString("order_id"));
                            OrderTrackingFoodItemItem.setItem_id(objItem.getString("item_id"));
                            OrderTrackingFoodItemItem.setItem_name(objItem.getString("item_name"));
                            OrderTrackingFoodItemItem.setPrice(objItem.getString("price"));
                            OrderTrackingFoodItemItem.setQty(objItem.getString("qty"));

                            JSONObject objItemPic = new JSONObject();
                            objItemPic = objItem.getJSONObject("item_photo");
                            OrderTrackingFoodItemItem.setPic(AppConstants.Customer_Dish_Image_path + objItemPic.getString("photo"));

                            OrderTrackingFoodItemAL.add(OrderTrackingFoodItemItem);
                        }

                        JSONObject objTimeSlotHeader = new JSONObject();
                        objTimeSlotHeader = objOrderDetails.getJSONObject("time_slot");
                        DeliveryTimeHeader = objTimeSlotHeader.getString("time_slot_name");
                    }
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

                CustomerOrderTrackListAdapter adapter = new CustomerOrderTrackListAdapter(CustomerOrderTracking.this, CustomerOrderTrackingListItemAL);
                lstOrderTracking.setAdapter(adapter);
            } else if (status.equals("0")) {
                Toast.makeText(CustomerOrderTracking.this, "Oops!Try again later....", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CustomerOrderTracking.this, "Server error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
