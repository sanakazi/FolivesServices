package com.callndata.folivesservices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.item.CustomerAddressItem;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

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

public class CustomerMainActivity extends FragmentActivity implements View.OnClickListener {

    ImageView imgMenu;

    private ResideMenu resideMenu;
    private CustomerMainActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemNewOrder;
    private ResideMenuItem itemHistory;
    private ResideMenuItem itemFavourites;
    private ResideMenuItem itemLogout;

    SharedPreferences pref;
    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;

    CustomerAddressItem CustomerAddressItemItem;
    ArrayList<CustomerAddressItem> CustomerAddressItemAL = new ArrayList<CustomerAddressItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentactivity_customer_main);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        mContext = this;
        setUpMenu();
        changeFragment(new CustomerHomeFragment());
    }

    @SuppressWarnings("deprecation")
    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        // set background of menu
        resideMenu.setBackground(R.drawable.login_screen_bg);
        resideMenu.attachToActivity(this);
        // resideMenu.setMenuListener(menuListener);
        // valid scale factor is between 0.0f and 1.0f. leftmenu'width is
        // 150dip.
        resideMenu.setScaleValue(0.6f);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_LEFT);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.ic_veg, "Home");
        itemNewOrder = new ResideMenuItem(this, R.drawable.ic_veg, "New Order");
        itemHistory = new ResideMenuItem(this, R.drawable.ic_veg, "History");
        itemFavourites = new ResideMenuItem(this, R.drawable.ic_veg, "Favourites");
        itemLogout = new ResideMenuItem(this, R.drawable.ic_veg, "Logout");

        itemHome.setOnClickListener(this);
        itemNewOrder.setOnClickListener(this);
        itemHistory.setOnClickListener(this);
        itemFavourites.setOnClickListener(this);
        itemLogout.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemNewOrder, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHistory, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFavourites, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogout, ResideMenu.DIRECTION_LEFT);

        findViewById(R.id.imgMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == itemHome) {
            changeFragment(new CustomerHomeFragment());
        } else if (view == itemNewOrder) {
            //changeFragment(new CustomerFoodItemFragment());
            new CustomerAddressJSON().execute();
        } else if (view == itemHistory) {
            changeFragment(new CustomerOrderHistoryFragment());
        } else if (view == itemFavourites) {
            changeFragment(new CustomerFavouriteFragment());
        } else if (view == itemLogout) {

            new AlertDialog.Builder(this).setTitle("Logout")
                    .setMessage("\nDo you want to logout?\n").setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("MLoginFlag", "0");
                            editor.putString("Token", null);
                            editor.commit();

                            Intent i = new Intent(CustomerMainActivity.this, CustomerLoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
        }

        resideMenu.closeMenu();

    }

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    class CustomerAddressJSON extends AsyncTask<Void, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerMainActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_ADDRESS);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();
                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {

                    JSONArray arrAddress = new JSONArray();
                    arrAddress = obj.getJSONArray("user_address");

                    if (arrAddress.length() > 0) {
                        for (int i = 0; i < arrAddress.length(); i++) {
                            JSONObject objAdd = new JSONObject();
                            objAdd = arrAddress.getJSONObject(i);

                            CustomerAddressItemItem = new CustomerAddressItem();
                            CustomerAddressItemItem.setId(objAdd.getString("id"));
                            CustomerAddressItemItem.setUserid(objAdd.getString("user_id"));
                            CustomerAddressItemItem.setAddress(objAdd.getString("address"));
                            CustomerAddressItemItem.setTitle(objAdd.getString("address_name"));
                            CustomerAddressItemItem.setCity(objAdd.getString("city"));
                            CustomerAddressItemItem.setState(objAdd.getString("state"));
                            CustomerAddressItemItem.setCountry(objAdd.getString("country"));
                            CustomerAddressItemItem.setZipcode(objAdd.getString("zipcode"));
                            CustomerAddressItemItem.setLandmark(objAdd.getString("landmark"));

                            CustomerAddressItemAL.add(CustomerAddressItemItem);

                        }
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
                if (CustomerAddressItemAL.size() > 0) {
                    Intent intent = new Intent(CustomerMainActivity.this, CustomerAddressSelection.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CustomerMainActivity.this, CustomerPinCode.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(CustomerMainActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (AppConstants.HomeFlag.equals("1")) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new CustomerHomeFragment(), "fragment")
//                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
//            Toast.makeText(this, "Main Resume called", Toast.LENGTH_SHORT).show();
//        }
//    }
}
