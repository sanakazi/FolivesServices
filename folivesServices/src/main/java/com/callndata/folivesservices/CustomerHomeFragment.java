package com.callndata.folivesservices;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapters.CustomerHomeRecentOrders;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.item.CustomerAddressItem;
import com.folives.item.CustomerHomeRecentOrdersItem;
import com.squareup.picasso.Picasso;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomerHomeFragment extends Fragment {

    TextView txtUserName, txtUserEmail, txtBalance;
    ListView lstRecentOrders;
    ImageView imgProfilePic;

    View FunctionLayout;
    LinearLayout llListHeader;
    private View stickyViewSpacer;
    private int MAX_ROWS = 20;

    LinearLayout llNewOrders, llOrderHistory, llFavourites, llOffers;
    ArrayList<NameValuePair> nameValuePairs;
    ProgressDialog pDialog, pDialog1;

    CustomerHomeRecentOrdersItem CHROItem;
    String strId, strFName, strSName, strEmail, strProPic, strFbId, strBal;
    CustomerHomeRecentOrders CHROAdapter;
    ArrayList<CustomerHomeRecentOrdersItem> CustomerHomeRecentOrdersItemAL = new ArrayList<CustomerHomeRecentOrdersItem>();

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    String pic;
    SharedPreferences prefs;

    CustomerAddressItem CustomerAddressItemItem;
    ArrayList<CustomerAddressItem> CustomerAddressItemAL = new ArrayList<CustomerAddressItem>();

    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.activity_customer_main, container, false);

        // AppConstants.HomeFlag = "0";

        prefs = getActivity().getSharedPreferences("myPrefs", getActivity().MODE_PRIVATE);

        lstRecentOrders = (ListView) view.findViewById(R.id.lstRecentOrders);
        FunctionLayout = view.findViewById(R.id.llFunctions);
        llListHeader = (LinearLayout) view.findViewById(R.id.llListHeader);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.customerhome_listview_header, null);
        stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

        lstRecentOrders.addHeaderView(listHeader);

        llNewOrders = (LinearLayout) view.findViewById(R.id.llNewOrders);
        llOrderHistory = (LinearLayout) view.findViewById(R.id.llOrderHistory);
        llFavourites = (LinearLayout) view.findViewById(R.id.llFavourites);
        llOffers = (LinearLayout) view.findViewById(R.id.llOffers);

        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) view.findViewById(R.id.txtUserEmail);
        txtBalance = (TextView) view.findViewById(R.id.txtBalance);

        imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);

        llNewOrders.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String Pincode = prefs.getString("pincode", "Place");

                new CustomerAddressJSON().execute();
            }
        });

        llOrderHistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragMgr = getFragmentManager();
                android.support.v4.app.FragmentTransaction xact = fragMgr.beginTransaction();
                xact.add(R.id.main_fragment, new CustomerOrderHistoryFragment(), "Fragment").commit();
            }
        });
        llFavourites.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragMgr = getFragmentManager();
                android.support.v4.app.FragmentTransaction xact = fragMgr.beginTransaction();
                xact.add(R.id.main_fragment, new CustomerFavouriteFragment(), "Fragment").commit();
            }
        });
        llOffers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Offers.....", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), CustomerAddressSelection.class);
                startActivity(intent);

            }
        });

        lstRecentOrders.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (lstRecentOrders.getFirstVisiblePosition() == 0) {
                    View firstChild = lstRecentOrders.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }
                    int heroTopY = stickyViewSpacer.getTop();
                    llListHeader.setY(Math.max(0, heroTopY + topY));
                    FunctionLayout.setY(topY * 0.7f);
                }
            }
        });

        lstRecentOrders.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CustomerOrderTracking.class);
                intent.putExtra("order_id", CustomerHomeRecentOrdersItemAL.get(position - 1).getOrder_id());
                startActivity(intent);
            }
        });

        new CustomerHomeDetailsJSON().execute();

        return view;
    }

    class CustomerHomeDetailsJSON extends AsyncTask<Void, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog1 = new ProgressDialog(getActivity());
            pDialog1.setMessage("Please Wait...");
            pDialog1.setCancelable(false);
            pDialog1.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_HOME_DATA);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                    JSONObject objUser = new JSONObject();
                    JSONArray arrRecentOrders = new JSONArray();

                    objUser = obj.getJSONObject("user");
                    arrRecentOrders = obj.getJSONArray("recent_orders");

                    strId = objUser.getString("id");
                    strFName = objUser.getString("first_name");
                    strSName = objUser.getString("last_name");
                    strEmail = objUser.getString("email");
                    strProPic = objUser.getString("profile_photo");
                    strFbId = objUser.getString("fb_id");
                    strBal = obj.getString("balance");

                    if (strFbId.equals("0")) {
                        pic = AppConstants.Customer_Profile_Image_Path + objUser.getString("profile_photo");
                        AppConstants.Customer_Profile_Pic = pic;
                    } else {
                        pic = strProPic;
                    }
                    JSONObject ordersObj = new JSONObject();
                    for (int i = 0; i < arrRecentOrders.length(); i++) {

                        ordersObj = arrRecentOrders.getJSONObject(i);

                        CHROItem = new CustomerHomeRecentOrdersItem();
                        CHROItem.setOrder_id(ordersObj.getString("id"));
                        CHROItem.setOrder_amt(ordersObj.getString("total_w_tax"));
                        CHROItem.setOrder_date(
                                getTimeAgo(getDateInMillis(ordersObj.getString("created_at")), getActivity()));
                        JSONArray itemArr = new JSONArray();
                        itemArr = ordersObj.getJSONArray("item_names");
                        JSONObject itemObj = new JSONObject();
                        itemObj = itemArr.getJSONObject(0);
                        CHROItem.setItem_name(itemObj.getString("item_name"));
                        CustomerHomeRecentOrdersItemAL.add(CHROItem);
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
            if (pDialog1.isShowing()) {
                pDialog1.dismiss();
            }

            if (status.equals("true")) {

                txtUserName.setText(strFName + " " + strSName);
                txtUserEmail.setText(strEmail);
                txtBalance.setText("₹ " + strBal);

                AppConstants.Customer_Name = strFName + " " + strSName;
                AppConstants.Customer_Email = strEmail;

                SpannableString content = new SpannableString("₹ " + strBal);
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                txtBalance.setText(content);

                AppConstants.Customer_Profile_Pic = pic;

                CHROAdapter = new CustomerHomeRecentOrders(getActivity(), CustomerHomeRecentOrdersItemAL);
                lstRecentOrders.setAdapter(CHROAdapter);

                Picasso.with(getActivity()).load(pic).into(imgProfilePic);

            } else if (status.equals("0")) {
                Toast.makeText(getActivity(), "Server Error...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class CustomerAddressJSON extends AsyncTask<Void, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
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
                    Intent intent = new Intent(getActivity(), CustomerAddressSelection.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), CustomerPinCode.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getActivity(), "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static long getDateInMillis(String srcDate) throws java.text.ParseException {
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        lstRecentOrders.setAdapter(null);
//        new CustomerHomeDetailsJSON().execute();

//            if (AppConstants.PlaceFlag.equals("1") && AppConstants.HomeFlag.equals("0")) {
//                String Pincode = prefs.getString("pincode", "Place");
//
//                if (Pincode.equals("Place") || Pincode.equals(null)) {
//                    Intent intent = new Intent(getActivity(), CustomerPinCode.class);
//                    startActivity(intent);
//                } else {
//                    FragmentManager fragMgr = getFragmentManager();
//                    android.support.v4.app.FragmentTransaction xact = fragMgr.beginTransaction();
//                    xact.replace(R.id.main_fragment, new CustomerFoodItemFragment(), "Fragment").commit();
//                }
//            } else if (AppConstants.UserAddressFlag.equals("1") && AppConstants.HomeFlag.equals("0")) {
//                FragmentManager fragMgr = getFragmentManager();
//                android.support.v4.app.FragmentTransaction xact = fragMgr.beginTransaction();
//                xact.replace(R.id.main_fragment, new CustomerFoodItemFragment(), "Fragment").commit();
//            } else {
////           FragmentManager fragMgr = getFragmentManager();
////           android.support.v4.app.FragmentTransaction xact = fragMgr.beginTransaction();
////           xact.replace(R.id.main_fragment, new CustomerHomeFragment(), "Fragment").commit();
//            }

    }
}
