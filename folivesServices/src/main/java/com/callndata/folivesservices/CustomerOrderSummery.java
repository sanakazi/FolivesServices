package com.callndata.folivesservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapters.CustomerBreakfastOrderSummeryAdapter;
import com.callndata.adapters.CustomerDinnerOrderSummeryAdapter;
import com.callndata.adapters.CustomerLunchOrderSummeryAdapter;
import com.callndata.adapters.CustomerMidnightOrderSummeryAdapter;
import com.callndata.others.AppConstants;
import com.callndata.others.DataBaseHandler;
import com.example.folivesservices.R;
import com.folives.item.CustomerOrderSummeryItem;
import com.folives.item.CustomerPlaceOrderItem;
import com.folives.item.FoodCartCheckout;

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
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerOrderSummery extends Activity {

    public static Activity MyActivityFlag;

    TextView txtTotalPrice;
    ListView lstMidNight, lstBreakfast, lstLunch, lstDinner;
    LinearLayout llMidNight, llBreakFast, llLunch, llDinner;
    FoodCartCheckout FoodCartCheckoutItem;
    DataBaseHandler db;

    CustomerOrderSummeryItem CustomerOrderSummeryItemitem;
    ArrayList<CustomerOrderSummeryItem> MidnightAL = new ArrayList<CustomerOrderSummeryItem>();
    ArrayList<CustomerOrderSummeryItem> BreakFastAL = new ArrayList<CustomerOrderSummeryItem>();
    ArrayList<CustomerOrderSummeryItem> LunchAL = new ArrayList<CustomerOrderSummeryItem>();
    ArrayList<CustomerOrderSummeryItem> DinnerAL = new ArrayList<CustomerOrderSummeryItem>();

    CustomerMidnightOrderSummeryAdapter adapterMN;
    CustomerBreakfastOrderSummeryAdapter adapterBF;
    CustomerLunchOrderSummeryAdapter adapterLN;
    CustomerDinnerOrderSummeryAdapter adapterDN;

    RelativeLayout rlPayment;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;

    JSONObject CartObj = new JSONObject();
    JSONArray CartArr = new JSONArray();


    RelativeLayout rlMNDS, rlMNDA, rlBFDS, rlBFDA, rlLNDS, rlLNDA, rlDNDS, rlDNDA;

    ArrayList<String> MNSlotAL = new ArrayList<String>();
    ArrayList<String> BFSlotAL = new ArrayList<String>();
    ArrayList<String> LNSlotAL = new ArrayList<String>();
    ArrayList<String> DNSlotAL = new ArrayList<String>();

    TextView txtMNDA, txtBFDA, txtLNDA, txtDNDA;
    TextView txtMNDS, txtBFDS, txtLNDS, txtDNDS;

    String strDate;
    String strItem;
    String DeliverSlotStr = "0";
    String DeliveryAddressStr = "[";
    HashMap<String, String> DeliveryTimeSlotHM = new HashMap<String, String>();
    ArrayList<String> DeliveryAddressAL = new ArrayList<String>();

    String PaymentOrderId, TransactionId, TotalAmount;
    String SelectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_order_summery_fragment);

        MyActivityFlag = this;
        SelectedAddress = getIntent().getExtras().getString("SelectedAddress");

        db = new DataBaseHandler(CustomerOrderSummery.this);
        strItem = getIntent().getExtras().getString("strItem");

        Intent intent = getIntent();
        DeliveryTimeSlotHM = (HashMap<String, String>) intent.getSerializableExtra("TimeSlotIdHM");

        strDate = getIntent().getExtras().getString("strDate");

        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        lstMidNight = (ListView) findViewById(R.id.lstMidnight);
        lstBreakfast = (ListView) findViewById(R.id.lstBreakfast);
        lstLunch = (ListView) findViewById(R.id.lstLunch);
        lstDinner = (ListView) findViewById(R.id.lstDinner);

        llMidNight = (LinearLayout) findViewById(R.id.llMidNight);
        llBreakFast = (LinearLayout) findViewById(R.id.llBreakFast);
        llLunch = (LinearLayout) findViewById(R.id.llLunch);
        llDinner = (LinearLayout) findViewById(R.id.llDinner);

        rlPayment = (RelativeLayout) findViewById(R.id.rlPayment);

        rlMNDS = (RelativeLayout) findViewById(R.id.rlMNDS);
        rlMNDA = (RelativeLayout) findViewById(R.id.rlMNDA);

        rlBFDS = (RelativeLayout) findViewById(R.id.rlBFDS);
        rlBFDA = (RelativeLayout) findViewById(R.id.rlBFDA);

        rlLNDS = (RelativeLayout) findViewById(R.id.rlLNDS);
        rlLNDA = (RelativeLayout) findViewById(R.id.rlLNDA);

        rlDNDS = (RelativeLayout) findViewById(R.id.rlDNDS);
        rlDNDA = (RelativeLayout) findViewById(R.id.rlDNDA);

        txtMNDA = (TextView) findViewById(R.id.txtMNDA);
        txtBFDA = (TextView) findViewById(R.id.txtBFDA);
        txtLNDA = (TextView) findViewById(R.id.txtLNDA);
        txtDNDA = (TextView) findViewById(R.id.txtDNDA);

        txtMNDS = (TextView) findViewById(R.id.txtMNDS);
        txtBFDS = (TextView) findViewById(R.id.txtBFDS);
        txtLNDS = (TextView) findViewById(R.id.txtLNDS);
        txtDNDS = (TextView) findViewById(R.id.txtDNDS);

        llMidNight.setVisibility(View.GONE);
        llBreakFast.setVisibility(View.GONE);
        llLunch.setVisibility(View.GONE);
        llDinner.setVisibility(View.GONE);

//        txtMNDA.setText("SelectedAddress");
//        txtBFDA.setText("SelectedAddress");
//        txtLNDA.setText("SelectedAddress");
//        txtDNDA.setText("SelectedAddress");

        Cursor CursorMidnight;
        Cursor CursorBreakfast;
        Cursor CursorLunch;
        Cursor CursorDinner;

        MNSlotAL = AppConstants.MNDelSlotAL;
        BFSlotAL = AppConstants.BFDelSlotAL;
        LNSlotAL = AppConstants.LNDelSlotAL;
        DNSlotAL = AppConstants.DNDelSlotAL;

        db.open();
        CursorMidnight = db.FetchSpecificData("midnight");
        CursorBreakfast = db.FetchSpecificData("breakfast");
        CursorLunch = db.FetchSpecificData("Lunch");
        CursorDinner = db.FetchSpecificData("dinner");


        if (CursorMidnight.getCount() > 0) {

            if (CursorMidnight.moveToFirst()) {
                do {
                    CustomerOrderSummeryItemitem = new CustomerOrderSummeryItem();

                    CustomerOrderSummeryItemitem.setMenu_id(CursorMidnight.getString(CursorMidnight.getColumnIndex("column_menu_id")));
                    CustomerOrderSummeryItemitem.setId(CursorMidnight.getString(CursorMidnight.getColumnIndex("column_food_id")));
                    CustomerOrderSummeryItemitem.setName(CursorMidnight.getString(CursorMidnight.getColumnIndex("column_food_name")));
                    CustomerOrderSummeryItemitem.setPrice(CursorMidnight.getString(CursorMidnight.getColumnIndex("column_food_price")));
                    CustomerOrderSummeryItemitem.setCnt(CursorMidnight.getString(CursorMidnight.getColumnIndex("column_cart_count")));
                    CustomerOrderSummeryItemitem.setImg(CursorMidnight.getString(CursorMidnight.getColumnIndex("column_food_image")));
                    CustomerOrderSummeryItemitem.setQtyAvl(CursorMidnight.getString(CursorMidnight.getColumnIndex("column_food_qty")));

                    MidnightAL.add(CustomerOrderSummeryItemitem);

                } while (CursorMidnight.moveToNext());
            }


            if (MidnightAL.size() == 0) {
                llMidNight.setVisibility(View.GONE);
            } else {
                llMidNight.setVisibility(View.VISIBLE);
                adapterMN = new CustomerMidnightOrderSummeryAdapter(CustomerOrderSummery.this, MidnightAL, txtTotalPrice);
                lstMidNight.setAdapter(adapterMN);
            }

        } else {
            llMidNight.setVisibility(View.GONE);
            Toast.makeText(CustomerOrderSummery.this, "Null Midnight Cursor", Toast.LENGTH_SHORT).show();
        }

        //db.close();

        //db.open();
        if (CursorBreakfast.getCount() > 0) {
            if (CursorBreakfast.moveToFirst()) {
                do {
                    CustomerOrderSummeryItemitem = new CustomerOrderSummeryItem();

                    CustomerOrderSummeryItemitem.setMenu_id(CursorBreakfast.getString(CursorBreakfast.getColumnIndex("column_menu_id")));
                    CustomerOrderSummeryItemitem.setId(CursorBreakfast.getString(CursorBreakfast.getColumnIndex("column_food_id")));
                    CustomerOrderSummeryItemitem.setName(CursorBreakfast.getString(CursorBreakfast.getColumnIndex("column_food_name")));
                    CustomerOrderSummeryItemitem.setPrice(CursorBreakfast.getString(CursorBreakfast.getColumnIndex("column_food_price")));
                    CustomerOrderSummeryItemitem.setCnt(CursorBreakfast.getString(CursorBreakfast.getColumnIndex("column_cart_count")));
                    CustomerOrderSummeryItemitem.setImg(CursorBreakfast.getString(CursorBreakfast.getColumnIndex("column_food_image")));
                    CustomerOrderSummeryItemitem.setQtyAvl(CursorBreakfast.getString(CursorBreakfast.getColumnIndex("column_food_qty")));

                    BreakFastAL.add(CustomerOrderSummeryItemitem);

                } while (CursorBreakfast.moveToNext());
            }


            if (BreakFastAL.size() == 0) {
                llBreakFast.setVisibility(View.GONE);
            } else {
                llBreakFast.setVisibility(View.VISIBLE);
                adapterBF = new CustomerBreakfastOrderSummeryAdapter(CustomerOrderSummery.this, BreakFastAL, txtTotalPrice);
                lstBreakfast.setAdapter(adapterBF);
            }
        } else {
            llBreakFast.setVisibility(View.GONE);
        }


        //  db.close();


        //db.open();
        if (CursorLunch.getCount() > 0) {


            if (CursorLunch.moveToFirst()) {
                do {
                    CustomerOrderSummeryItemitem = new CustomerOrderSummeryItem();

                    CustomerOrderSummeryItemitem.setMenu_id(CursorLunch.getString(CursorLunch.getColumnIndex("column_menu_id")));
                    CustomerOrderSummeryItemitem.setId(CursorLunch.getString(CursorLunch.getColumnIndex("column_food_id")));
                    CustomerOrderSummeryItemitem.setName(CursorLunch.getString(CursorLunch.getColumnIndex("column_food_name")));
                    CustomerOrderSummeryItemitem.setPrice(CursorLunch.getString(CursorLunch.getColumnIndex("column_food_price")));
                    CustomerOrderSummeryItemitem.setCnt(CursorLunch.getString(CursorLunch.getColumnIndex("column_cart_count")));
                    CustomerOrderSummeryItemitem.setImg(CursorLunch.getString(CursorLunch.getColumnIndex("column_food_image")));
                    CustomerOrderSummeryItemitem.setQtyAvl(CursorLunch.getString(CursorLunch.getColumnIndex("column_food_qty")));

                    LunchAL.add(CustomerOrderSummeryItemitem);

                } while (CursorLunch.moveToNext());
            }


            if (LunchAL.size() == 0) {
                llLunch.setVisibility(View.GONE);
            } else {
                llLunch.setVisibility(View.VISIBLE);
                adapterLN = new CustomerLunchOrderSummeryAdapter(CustomerOrderSummery.this, LunchAL, txtTotalPrice);
                lstLunch.setAdapter(adapterLN);
            }
        } else {
            llLunch.setVisibility(View.GONE);
        }
        //  db.close();


        //db.open();
        if (CursorDinner.getCount() > 0) {


            if (CursorDinner.moveToFirst()) {
                do {
                    CustomerOrderSummeryItemitem = new CustomerOrderSummeryItem();

                    CustomerOrderSummeryItemitem.setMenu_id(CursorDinner.getString(CursorDinner.getColumnIndex("column_menu_id")));
                    CustomerOrderSummeryItemitem.setId(CursorDinner.getString(CursorDinner.getColumnIndex("column_food_id")));
                    CustomerOrderSummeryItemitem.setName(CursorDinner.getString(CursorDinner.getColumnIndex("column_food_name")));
                    CustomerOrderSummeryItemitem.setPrice(CursorDinner.getString(CursorDinner.getColumnIndex("column_food_price")));
                    CustomerOrderSummeryItemitem.setCnt(CursorDinner.getString(CursorDinner.getColumnIndex("column_cart_count")));
                    CustomerOrderSummeryItemitem.setImg(CursorDinner.getString(CursorDinner.getColumnIndex("column_food_image")));
                    CustomerOrderSummeryItemitem.setQtyAvl(CursorDinner.getString(CursorDinner.getColumnIndex("column_food_qty")));

                    DinnerAL.add(CustomerOrderSummeryItemitem);

                } while (CursorDinner.moveToNext());
            }


            if (DinnerAL.size() == 0) {
                llDinner.setVisibility(View.GONE);
            } else {
                llDinner.setVisibility(View.VISIBLE);
                adapterDN = new CustomerDinnerOrderSummeryAdapter(CustomerOrderSummery.this, DinnerAL, txtTotalPrice);
                lstDinner.setAdapter(adapterDN);
            }
        } else {
            llDinner.setVisibility(View.GONE);
        }

        //db.close();


        //db.open();
        txtTotalPrice.setText(db.SumOfPrice());
        db.close();

        rlPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CartObj = new JSONObject();
                try {

                    CustomerPlaceOrderItem CustomerPlaceOrderItemItem;
                    ArrayList<CustomerPlaceOrderItem> CustomerPlaceOrderItemAL = new ArrayList<CustomerPlaceOrderItem>();

                    //
                    //For Delivery Slot
                    if (MidnightAL.size() > 0) {
                        CartObj.put("time_slot_id", DeliveryTimeSlotHM.get(txtMNDS.getText().toString()));
                        CartObj.put("delivery_time", txtMNDS.getText().toString());

                        DeliverSlotStr = CartObj.toString();
                    }
                    if (BreakFastAL.size() > 0) {
                        CartObj.put("time_slot_id", DeliveryTimeSlotHM.get(txtBFDS.getText().toString()));
                        CartObj.put("delivery_time", txtBFDS.getText().toString());

                        DeliverSlotStr = DeliverSlotStr + "," + CartObj.toString();
                    }
                    if (LunchAL.size() > 0) {
                        CartObj.put("time_slot_id", DeliveryTimeSlotHM.get(txtLNDS.getText().toString()));
                        CartObj.put("delivery_time", txtLNDS.getText().toString());

                        DeliverSlotStr = DeliverSlotStr + "," + CartObj.toString();
                    }
                    if (DinnerAL.size() > 0) {
                        CartObj.put("time_slot_id", DeliveryTimeSlotHM.get(txtDNDS.getText().toString()));
                        CartObj.put("delivery_time", txtDNDS.getText().toString());

                        DeliverSlotStr = DeliverSlotStr + "," + CartObj.toString();
                    }

                    String validation = DeliverSlotStr.substring(0, 2);
                    if (validation.equals("0,")) {
                        DeliverSlotStr = DeliverSlotStr.substring(2, DeliverSlotStr.length());
                    }
                    //
                    DeliverSlotStr = "[" + DeliverSlotStr + "]";


                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    for (int i = 0; i < DeliveryAddressAL.size(); i++) {
                        DeliveryAddressStr = DeliveryAddressStr + DeliveryAddressAL.get(i);
                    }
                    DeliveryAddressStr = DeliveryAddressStr + "]";
                } catch (Exception e) {
                    e.printStackTrace();
                }


                new OrderPlaceJSON().execute(strItem, DeliverSlotStr, DeliveryAddressStr);
//                Intent intent = new Intent(getActivity(), CustomerPaymentMain.class);
//                startActivity(intent);
            }
        });

        rlMNDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] MNDelSlotArr = MNSlotAL.toArray(new String[MNSlotAL.size()]);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CustomerOrderSummery.this);
                dialogBuilder.setTitle("Delivery Slot");
                dialogBuilder.setItems(MNDelSlotArr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        String selectedText = MNDelSlotArr[item].toString();  //Selected item in listview
                        txtMNDS.setText(selectedText);
                    }
                });
                AlertDialog alertDialogObject = dialogBuilder.create();
                alertDialogObject.show();
            }
        });

        rlBFDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] BFDelSlotArr = BFSlotAL.toArray(new String[BFSlotAL.size()]);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CustomerOrderSummery.this);
                dialogBuilder.setTitle("Delivery Slot");
                dialogBuilder.setItems(BFDelSlotArr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        String selectedText = BFDelSlotArr[item].toString();  //Selected item in listview
                        txtBFDS.setText(selectedText);
                    }
                });
                AlertDialog alertDialogObject = dialogBuilder.create();
                alertDialogObject.show();
            }
        });

        rlLNDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] LNDelSlotArr = LNSlotAL.toArray(new String[LNSlotAL.size()]);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CustomerOrderSummery.this);
                dialogBuilder.setTitle("Delivery Slot");
                dialogBuilder.setItems(LNDelSlotArr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        String selectedText = LNDelSlotArr[item].toString();  //Selected item in listview
                        txtLNDS.setText(selectedText);
                    }
                });
                AlertDialog alertDialogObject = dialogBuilder.create();
                alertDialogObject.show();
            }
        });

        rlDNDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] DNDelSlotArr = DNSlotAL.toArray(new String[DNSlotAL.size()]);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CustomerOrderSummery.this);
                dialogBuilder.setTitle("Delivery Slot");
                dialogBuilder.setItems(DNDelSlotArr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        String selectedText = DNDelSlotArr[item].toString();  //Selected item in listview
                        txtDNDS.setText(selectedText);
                    }
                });
                AlertDialog alertDialogObject = dialogBuilder.create();
                alertDialogObject.show();
            }
        });

        rlMNDA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerOrderSummery.this, CustomerFoodDeliveryAddressSelection.class);
                intent.putExtra("slotId", "1");
                startActivity(intent);
            }
        });
        rlBFDA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerOrderSummery.this, CustomerFoodDeliveryAddressSelection.class);
                intent.putExtra("slotId", "2");
                startActivity(intent);
            }
        });
        rlLNDA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerOrderSummery.this, CustomerFoodDeliveryAddressSelection.class);
                intent.putExtra("slotId", "3");
                startActivity(intent);
            }
        });
        rlDNDA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerOrderSummery.this, CustomerFoodDeliveryAddressSelection.class);
                intent.putExtra("slotId", "4");
                startActivity(intent);
            }
        });
    }


    public class OrderPlaceJSON extends AsyncTask<String, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CustomerOrderSummery.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String strItem = params[0];
                String DeliverySlot = params[1];
                String DeliveryAddress = params[2];

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("item", strItem));
                nameValuePairs.add(new BasicNameValuePair("post_code", AppConstants.UserZipCode));
                nameValuePairs.add(new BasicNameValuePair("date", strDate));
                nameValuePairs.add(new BasicNameValuePair("delivery_slot", DeliverySlot));
                nameValuePairs.add(new BasicNameValuePair("address", DeliveryAddress));


                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_ORDER_PLACE);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                    PaymentOrderId = obj.getString("payment_order_id");
                    TransactionId = obj.getString("transaction_id");
                    TotalAmount = obj.getString("total");
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
                Toast.makeText(CustomerOrderSummery.this, "Verified...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerOrderSummery.this, CustomerPaymentMain.class);
                intent.putExtra("PaymentOrderId", PaymentOrderId);
                intent.putExtra("TransactionId", TransactionId);
                intent.putExtra("TotalAmount", TotalAmount);
                startActivity(intent);
            } else {
                Toast.makeText(CustomerOrderSummery.this, "Verification Failed...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (AppConstants.isSetAddress.equals("1")) {
            txtMNDA.setText(AppConstants.isSetAddressValue);

            JSONObject objAddr = new JSONObject();
            try {
                objAddr.put("time_slot_id", "1");
                objAddr.put("address_id", AppConstants.isSetAddressID);
                DeliveryAddressAL.add(objAddr.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (AppConstants.isSetAddress.equals("2")) {
            txtBFDA.setText(AppConstants.isSetAddressValue);

            JSONObject objAddr = new JSONObject();
            try {
                objAddr.put("time_slot_id", "2");
                objAddr.put("address_id", AppConstants.isSetAddressID);
                DeliveryAddressAL.add(objAddr.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (AppConstants.isSetAddress.equals("3")) {
            txtLNDA.setText(AppConstants.isSetAddressValue);

            JSONObject objAddr = new JSONObject();
            try {
                objAddr.put("time_slot_id", "3");
                objAddr.put("address_id", AppConstants.isSetAddressID);
                DeliveryAddressAL.add(objAddr.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (AppConstants.isSetAddress.equals("4")) {
            txtDNDA.setText(AppConstants.isSetAddressValue);

            JSONObject objAddr = new JSONObject();
            try {
                objAddr.put("time_slot_id", "4");
                objAddr.put("address_id", AppConstants.isSetAddressID);
                DeliveryAddressAL.add(objAddr.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
