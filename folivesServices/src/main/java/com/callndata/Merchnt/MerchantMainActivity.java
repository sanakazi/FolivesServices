package com.callndata.Merchnt;

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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.callndata.folivesservices.CustomerLoginActivity;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
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
import java.util.HashMap;
import java.util.Iterator;

public class MerchantMainActivity extends FragmentActivity implements View.OnClickListener {

    ImageView imgMenu;

    private ResideMenu resideMenu;
    private MerchantMainActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemNewOrder;
    private ResideMenuItem itemHistory;
    private ResideMenuItem itemFavourites;
    private ResideMenuItem itemLogout;

    SharedPreferences pref;
    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;
    ArrayList<String> LoginResKeys = new ArrayList<String>();

    String error, error_desc;
    String ChefId, ChefName, ChefEmail, ChefPic, Balance;
    String NewOrderCount, ReviewCount, FollowersCount, PendingCount, IsActive, NotificationCount;

    ArrayList<String> MenuHeadersAL = new ArrayList<String>();
    HashMap<String, String> MenuHeaderServicesHM = new HashMap<String, String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmerchant_main);
        Log.w("Inside" , "MerchantMainActivity");
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        mContext = this;

        Intent intent = getIntent();
        String openFrag = intent.getStringExtra("chefFragToOpen");

        // attach to current activity;
        resideMenu = new ResideMenu(this);

        fragToOpen(openFrag);
      /*  if(openFrag.equals("dashboard"))
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new Dashboard(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();*/

        new DynamicMenuAndMerchantInfoJSON().execute();
    }


    private  void fragToOpen(String f)
    {   if (f.equals("dashboard")) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new Dashboard(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    } else if (f.equals("chef_daily_menu")) {
        Intent intent = new Intent(MerchantMainActivity.this, NewMerchantDailyMenuActivity.class);
        startActivity(intent);
    } else if (f.equals("followers")) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new FollowersFragment(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    } else if (f.equals("new_orders")) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new NewMerchantNewOrdersFragment(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    } else if (f.equals("notifications")) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new NewMerchantNotificationsFragment(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    } else if (f.equals("order_history")) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new NewMerchantOrderHistoryFragment(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    } else if (f.equals("customer_reviews")) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new NewMerchantReviewsFragment(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    } else if (f.equals("withdrawals")) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new NewMerchantWithdrawlsFragment(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    } else if (f.equals("settings")) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new NewMerchantSettingsFragment(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    } else if (f.equals("food_item")) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new NewMerchantFoodItemFragment(), "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
    }

    @SuppressWarnings("deprecation")
    private void setUpMenu() {

        // set background of menu
        resideMenu.setBackground(R.drawable.login_screen_bg);
        resideMenu.attachToActivity(this);
        // resideMenu.setMenuListener(menuListener);
        // valid scale factor is between 0.0f and 1.0f. leftmenu'width is
        // 150dip.
        resideMenu.setScaleValue(0.6f);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_LEFT);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        for (int i = 0; i < MenuHeadersAL.size(); i++) {

            ResideMenuItem MenuItem = new ResideMenuItem(this, R.drawable.ic_veg, MenuHeadersAL.get(i));
            MenuItem.setOnClickListener(this);
            MenuItem.setTag(i);
            resideMenu.addMenuItem(MenuItem);
        }
        findViewById(R.id.imgMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    @Override
    public void onClick(View view) {

        for (int i = 0; i < MenuHeadersAL.size(); i++) {
            if (view.getTag() == (Object) i) {
                String Clicked = MenuHeaderServicesHM.get(MenuHeadersAL.get(i));
                Toast.makeText(this, Clicked, Toast.LENGTH_SHORT).show();
                if (Clicked.equals("dashboard")) {
                    changeFragment(new Dashboard());
                } else if (Clicked.equals("chef_daily_menu")) {
                    Intent intent = new Intent(MerchantMainActivity.this, NewMerchantDailyMenuActivity.class);
                    startActivity(intent);
                } else if (Clicked.equals("followers")) {
                    changeFragment(new FollowersFragment());
                } else if (Clicked.equals("new_orders")) {
                    changeFragment(new NewMerchantNewOrdersFragment());
                } else if (Clicked.equals("notifications")) {
                    changeFragment(new NewMerchantNotificationsFragment());
                } else if (Clicked.equals("order_history")) {
                    changeFragment(new NewMerchantOrderHistoryFragment());
                } else if (Clicked.equals("customer_reviews")) {
                    changeFragment(new NewMerchantReviewsFragment());
                } else if (Clicked.equals("withdrawals")) {
                    changeFragment(new NewMerchantWithdrawlsFragment());
                } else if (Clicked.equals("settings")) {
                    changeFragment(new NewMerchantSettingsFragment());
                } else if (Clicked.equals("food_item")) {
                    changeFragment(new NewMerchantFoodItemFragment());
                } else if (Clicked.equals("logout")) {

                    new AlertDialog.Builder(this).setTitle("Logout")
                            .setMessage("\nDo you want to logout?\n").setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("MLoginFlag", "0");
                                    editor.putString("Token", null);
                                    editor.commit();

                                    Intent i = new Intent(MerchantMainActivity.this, CustomerLoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).setNegativeButton(android.R.string.no, null).show();
                }
            }
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

    class DynamicMenuAndMerchantInfoJSON extends AsyncTask<Void, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MerchantMainActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("graph_range", "7"));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_DASHBOARD);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();
                JSONObject obj = new JSONObject(imgResJSON);

                Iterator<String> iter = obj.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    LoginResKeys.add(key.toString());
                }
                if (LoginResKeys.contains("error")) {
                    error = obj.getString("error");
                    error_desc = obj.getString("error_description");
                    status = "1";
                } else if (LoginResKeys.contains("success")) {
                    status = obj.getString("success");
                    if (status.equals("true")) {
                        JSONArray arrChefData = new JSONArray();
                        JSONArray arrMenu = new JSONArray();

                        arrChefData = obj.getJSONArray("chef_details");
                        arrMenu = obj.getJSONArray("menu");

                        if (arrChefData.length() > 0) {

                            for (int i = 0; i < arrChefData.length(); i++) {
                                JSONObject objChefData = new JSONObject();
                                objChefData = arrChefData.getJSONObject(i);

                                ChefId = objChefData.getString("id");
                                ChefName = objChefData.getString("name");
                                // ChefEmail = objChefData.getString("email");
                                ChefPic = AppConstants.Customer_Chef_Image_path + objChefData.getString("logo");

                                JSONArray arrPersonalInfo = new JSONArray();
                                arrPersonalInfo = obj.getJSONArray("personal_info");
                                if (arrPersonalInfo.length() > 0) {
                                    JSONObject objPersonalInfo = new JSONObject();
                                    objPersonalInfo = arrPersonalInfo.getJSONObject(0);
                                    ChefEmail = objPersonalInfo.getString("email");
                                }

                                IsActive = objChefData.getString("active");
                                NotificationCount = Integer.toString(Integer.parseInt(objChefData.getString("push_notification")) +
                                        Integer.parseInt(objChefData.getString("email_notification")) +
                                        Integer.parseInt(objChefData.getString("sms_notification")));
                            }
                        }
                        NewOrderCount = obj.getString("new_orders_count");
                        ReviewCount = obj.getString("reviews_count");
                        FollowersCount = obj.getString("followers_count");
                        PendingCount = obj.getString("running_orders_count");
                        Balance = obj.getString("balance");

                        if (arrMenu.length() > 0) {
                            for (int i = 0; i < arrMenu.length(); i++) {
                                JSONObject objMenuData = new JSONObject();
                                objMenuData = arrMenu.getJSONObject(i);
                                MenuHeadersAL.add(objMenuData.getString("menu_name"));
                                MenuHeaderServicesHM.put(objMenuData.getString("menu_name"), objMenuData.getString("menu_link_android"));
                            }
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

            if (status.equals("1")) {
                Toast.makeText(MerchantMainActivity.this, error_desc, Toast.LENGTH_SHORT).show();
            } else if (status.equals("true")) {
                Toast.makeText(MerchantMainActivity.this, "Data Received.", Toast.LENGTH_SHORT).show();
                setUpMenu();
            } else if (status.equals("0")) {
                Toast.makeText(MerchantMainActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MerchantMainActivity.this, "No Response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
