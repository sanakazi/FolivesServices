package com.callndata.folivesservices;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapters.CustomerHomeRecentOrders;
import com.callndata.others.AppConstants;
import com.callndata.others.HorizontalPager;
import com.callndata.others.HorizontalPager.OnScreenSwitchListener;
import com.example.folivesservices.R;
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

public class CustomerOrderHistoryFragment extends Fragment {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    TextView txtPendingCount, txtHistoryCount;
    LinearLayout llPending, llHistory, llPendingOrderLine, llOrderHistoryLine;
    HorizontalPager horizontal_pager;
    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    CustomerHomeRecentOrdersItem CHROItem;
    ArrayList<CustomerHomeRecentOrdersItem> PendingOrdersAL = new ArrayList<CustomerHomeRecentOrdersItem>();
    ArrayList<CustomerHomeRecentOrdersItem> OrderHistoryAL = new ArrayList<CustomerHomeRecentOrdersItem>();

    CustomerHomeRecentOrders OrderAdapter;
    ImageView imgProfilePic;

    ListView lstPendingOrders, lstOrderHistory;

    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.activity_customer_order_history, container, false);

        llPending = (LinearLayout) view.findViewById(R.id.llPending);
        llHistory = (LinearLayout) view.findViewById(R.id.llHistory);
        llPendingOrderLine = (LinearLayout) view.findViewById(R.id.llPendingOrderLine);
        llOrderHistoryLine = (LinearLayout) view.findViewById(R.id.llOrderHistoryLine);
        horizontal_pager = (HorizontalPager) view.findViewById(R.id.horizontal_pager);

        txtPendingCount = (TextView) view.findViewById(R.id.txtPendingCount);
        txtHistoryCount = (TextView) view.findViewById(R.id.txtHistoryCount);

        imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);

        lstPendingOrders = new ListView(getActivity());
        lstOrderHistory = new ListView(getActivity());

        try {
            Picasso.with(getActivity()).load(AppConstants.Customer_Profile_Pic).into(imgProfilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new CustomerHistoryDetailsJSON().execute();

        horizontal_pager.setOnScreenSwitchListener(new OnScreenSwitchListener() {

            @Override
            public void onScreenSwitched(int screen) {

                if (screen == 0) {
                    llPendingOrderLine.setBackgroundColor(getResources().getColor(R.color.red));
                    llOrderHistoryLine.setBackgroundResource(0);
                } else if (screen == 1) {
                    llPendingOrderLine.setBackgroundResource(0);
                    llOrderHistoryLine.setBackgroundColor(getResources().getColor(R.color.red));
                }
            }
        });

        llPending.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                llPendingOrderLine.setBackgroundColor(getResources().getColor(R.color.red));
                llOrderHistoryLine.setBackgroundResource(0);

                horizontal_pager.setCurrentScreen(0, true);
            }
        });

        llHistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                llPendingOrderLine.setBackgroundResource(0);
                llOrderHistoryLine.setBackgroundColor(getResources().getColor(R.color.red));

                horizontal_pager.setCurrentScreen(1, true);
            }
        });

        lstPendingOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CustomerOrderTracking.class);
                intent.putExtra("order_id", PendingOrdersAL.get(position).getOrder_id());
                startActivity(intent);
            }
        });
        lstOrderHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CustomerOrderTracking.class);
                intent.putExtra("order_id", OrderHistoryAL.get(position).getOrder_id());
                startActivity(intent);
            }
        });

        return view;
    }

    class CustomerHistoryDetailsJSON extends AsyncTask<Void, Void, Void> {

        String status = "0";
        String pendingCount, historyCount;

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
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_ORDER_HISTORY);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                    JSONArray arrPendingOrders = new JSONArray();
                    JSONArray arrOrderHistory = new JSONArray();

                    arrPendingOrders = obj.getJSONArray("pending_orders");
                    arrOrderHistory = obj.getJSONArray("history_orders");

                    pendingCount = Integer.toString(arrPendingOrders.length());
                    historyCount = Integer.toString(arrOrderHistory.length());

                    for (int i = 0; i < arrPendingOrders.length(); i++) {
                        JSONObject orderObj = arrPendingOrders.getJSONObject(i);
                        CHROItem = new CustomerHomeRecentOrdersItem();

                        CHROItem.setOrder_id(orderObj.getString("id"));
                        CHROItem.setOrder_amt(orderObj.getString("total_w_tax"));
                        CHROItem.setOrder_date(
                                getTimeAgo(getDateInMillis(orderObj.getString("created_at")), getActivity()));

                        JSONArray itemArr = new JSONArray();
                        itemArr = orderObj.getJSONArray("item_names");
                        JSONObject itemObj = new JSONObject();
                        itemObj = itemArr.getJSONObject(0);
                        CHROItem.setItem_name(itemObj.getString("item_name"));

                        PendingOrdersAL.add(CHROItem);
                    }

                    for (int i = 0; i < arrOrderHistory.length(); i++) {
                        JSONObject orderObj = arrOrderHistory.getJSONObject(i);
                        CHROItem = new CustomerHomeRecentOrdersItem();

                        CHROItem.setOrder_id(orderObj.getString("id"));
                        CHROItem.setOrder_amt(orderObj.getString("total_w_tax"));
                        CHROItem.setOrder_date(
                                getTimeAgo(getDateInMillis(orderObj.getString("created_at")), getActivity()));

                        JSONArray itemArr = new JSONArray();
                        itemArr = orderObj.getJSONArray("item_names");
                        JSONObject itemObj = new JSONObject();
                        itemObj = itemArr.getJSONObject(0);
                        CHROItem.setItem_name(itemObj.getString("item_name"));

                        OrderHistoryAL.add(CHROItem);
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

                txtPendingCount.setText(pendingCount);
                txtHistoryCount.setText(historyCount);

                for (int i = 0; i < 2; i++) {
                    LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                    if (i == 0) {

                        lstPendingOrders.setLayoutParams(lparams);
                        OrderAdapter = new CustomerHomeRecentOrders(getActivity(), PendingOrdersAL);
                        lstPendingOrders.setAdapter(OrderAdapter);
                        horizontal_pager.addView(lstPendingOrders);
                    } else if (i == 1) {

                        lstOrderHistory.setLayoutParams(lparams);
                        OrderAdapter = new CustomerHomeRecentOrders(getActivity(), OrderHistoryAL);
                        lstOrderHistory.setAdapter(OrderAdapter);
                        horizontal_pager.addView(lstOrderHistory);
                    }
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
}
