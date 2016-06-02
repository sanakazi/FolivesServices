package com.callndata.Merchnt;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapterM.NewMerchantDashboardAdapter;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
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
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ravikant on 09-03-2016.
 */
public class Dashboard extends Fragment {

    ListView lstRecentOrders;
    LinearLayout llListHeader;
    LinearLayout llFunctions;

    View FunctionLayout;
    private View stickyViewSpacer;
    ArrayList<String> data = new ArrayList<String>();
    NewMerchantDashboardAdapter newMerchantDashboardAdapter;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;
    ArrayList<String> LoginResKeys = new ArrayList<String>();

    String error, error_desc;
    String ChefId, ChefName, ChefEmail, ChefPic, Balance;
    String NewOrderCount, ReviewCount, FollowersCount, PendingCount, IsActive, NotificationCount;

    ImageView imgProfilePic;
    TextView txtUserName, txtUserEmail, txtBalance;
    TextView txtNotification, txtNewOrderCount, txtReviewsCount, txtFollowersCount, txtPendingCount;

    Switch swhActive;

    LinearLayout llChart;

    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.merchant_activity_dashboard, container, false);
        Log.w("Inside", "Dashboard");
        lstRecentOrders = (ListView) view.findViewById(R.id.lstRecentOrders);
        FunctionLayout = view.findViewById(R.id.llFunctions);
        llListHeader = (LinearLayout) view.findViewById(R.id.llListHeader);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.customerhome_listview_header, null);
        stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

        imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);

        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) view.findViewById(R.id.txtUserEmail);
        txtBalance = (TextView) view.findViewById(R.id.txtBalance);

        txtNotification = (TextView) view.findViewById(R.id.txtNotification);
        txtNewOrderCount = (TextView) view.findViewById(R.id.txtNewOrderCount);
        txtReviewsCount = (TextView) view.findViewById(R.id.txtReviewsCount);
        txtFollowersCount = (TextView) view.findViewById(R.id.txtFollowersCount);
        txtPendingCount = (TextView) view.findViewById(R.id.txtPendingCount);

        swhActive = (Switch) view.findViewById(R.id.swhActive);
        llChart = (LinearLayout) view.findViewById(R.id.llChart);
        lstRecentOrders.addHeaderView(listHeader);

//
//        data.add("One");
//        data.add("Two");
//        data.add("Three");
//        data.add("Four");
//        data.add("Five");
//        data.add("Six");
//        data.add("Seven");
//        data.add("Eight");
//        data.add("Nine");
//        data.add("Ten");
        newMerchantDashboardAdapter = new NewMerchantDashboardAdapter(getActivity(), data );
        lstRecentOrders.setAdapter(newMerchantDashboardAdapter);
     //   lstRecentOrders.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data));


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

        swhActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new MerchntAccountStatusJSON().execute("1");
                } else {
                    new MerchntAccountStatusJSON().execute("0");
                }
            }
        });
        new DashbordJSON().execute();

        return view;
    }


    class DashbordJSON extends AsyncTask<Void, Void, Void> {

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
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("graph_range", "100"));

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
                                //ChefEmail = objChefData.getString("email");
                                ChefPic = AppConstants.Customer_Chef_Image_path + objChefData.getString("logo");
                                IsActive = objChefData.getString("active");

                                JSONArray arrPersonalInfo = new JSONArray();
                                arrPersonalInfo = obj.getJSONArray("personal_info");
                                if (arrPersonalInfo.length() > 0) {
                                    JSONObject objPersonalInfo = new JSONObject();
                                    objPersonalInfo = arrPersonalInfo.getJSONObject(0);
                                    ChefEmail = objPersonalInfo.getString("email");
                                }
                            }
                        }
                        NotificationCount = obj.getString("new_notification_count");
                        NewOrderCount = obj.getString("new_orders_count");
                        ReviewCount = obj.getString("reviews_count");
                        FollowersCount = obj.getString("followers_count");
                        PendingCount = obj.getString("running_orders_count");
                        Balance = obj.getString("balance");
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
                Toast.makeText(getActivity(), error_desc, Toast.LENGTH_SHORT).show();
            } else if (status.equals("true")) {
                Toast.makeText(getActivity(), "Data Received.", Toast.LENGTH_SHORT).show();

                Picasso.with(getActivity()).load(ChefPic).into(imgProfilePic);
                txtUserName.setText(ChefName);
                txtUserEmail.setText(ChefEmail);
                txtBalance.setText(Balance);
                txtNotification.setText(NotificationCount);
                txtNewOrderCount.setText(NewOrderCount);
                txtReviewsCount.setText(ReviewCount);
                txtFollowersCount.setText(FollowersCount);
                txtPendingCount.setText(PendingCount);

                if (IsActive.equals("1")) {
                    swhActive.setChecked(true);
                } else {
                    swhActive.setChecked(false);
                }

            } else if (status.equals("0")) {
                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "No Response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class MerchntAccountStatusJSON extends AsyncTask<String, Void, Void> {

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
        protected Void doInBackground(String... params) {
            String state = params[0];
            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("active", state));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_ACCOUNT_STATUS_CHANGE);
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
                Toast.makeText(getActivity(), error_desc, Toast.LENGTH_SHORT).show();
            } else if (status.equals("true")) {
                Toast.makeText(getActivity(), "Status Updated.", Toast.LENGTH_SHORT).show();
            } else if (status.equals("0")) {
                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "No Response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
