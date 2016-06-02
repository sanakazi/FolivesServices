package com.callndata.folivesservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.item.CustomerAddressItem;

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

/**
 * Created by ravikant on 25-01-2016.
 */
public class CustomerAddressSelection extends Activity {

    public static Activity MyActivityFlag;

    ProgressDialog pDialog;
    private ViewPager viewPager;
    private MyViewPagerAdapter pagerAdapter;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    CustomerAddressItem CustomerAddressItemItem;
    ArrayList<CustomerAddressItem> CustomerAddressItemAL = new ArrayList<CustomerAddressItem>();

    PagerTabStrip pagerTabStrip;
    LinearLayout llAddAddress, llUseThisAddress;
    TextView txtLandmark, txtZipCode;

    ImageView imgEdit, imgDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_select_address);

        MyActivityFlag = this;

        viewPager = (ViewPager) super.findViewById(R.id.viewpager);
        pagerTabStrip = (PagerTabStrip) super.findViewById(R.id.pager_header);
        pagerTabStrip.setTextColor(getResources().getColor(R.color.golden));

        llAddAddress = (LinearLayout) findViewById(R.id.llAddAddress);
        llUseThisAddress = (LinearLayout) findViewById(R.id.llUseThisAddress);
        txtLandmark = (TextView) findViewById(R.id.txtLandmark);
        txtZipCode = (TextView) findViewById(R.id.txtZipCode);

        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        imgDelete = (ImageView) findViewById(R.id.imgDelete);

        llAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerAddressSelection.this, CustomerAddAddress.class);
                startActivity(intent);
                finish();
            }
        });

        llUseThisAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstants.UserZipCode = txtZipCode.getText().toString();
                AppConstants.UserAddressFlag = "1";
                AppConstants.isSetAddressID = CustomerAddressItemAL.get(viewPager.getCurrentItem()).getId();

                Intent intent = new Intent(CustomerAddressSelection.this, CustomerFoodItemFragment.class);
                intent.putExtra("SelectedAddress", CustomerAddressItemAL.get(viewPager.getCurrentItem()).getTitle());
                startActivity(intent);

                //finish();
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                Intent intent = new Intent(CustomerAddressSelection.this, CustomerEditAddress.class);
                intent.putExtra("id", CustomerAddressItemAL.get(position).getId());
                intent.putExtra("nickname", CustomerAddressItemAL.get(position).getTitle());
                intent.putExtra("address", CustomerAddressItemAL.get(position).getAddress());
                intent.putExtra("landmark", CustomerAddressItemAL.get(position).getLandmark());
                intent.putExtra("city", CustomerAddressItemAL.get(position).getCity());
                intent.putExtra("state", CustomerAddressItemAL.get(position).getState());
                intent.putExtra("zipcode", CustomerAddressItemAL.get(position).getZipcode());
                startActivity(intent);
                finish();
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CustomerAddressSelection.this).setTitle("Delete")
                        .setMessage("\nDo you want to delete?\n").setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (CustomerAddressItemAL.size() > 1) {
                                    new CustomerDeleteAddressJSON().execute(CustomerAddressItemAL.get(viewPager.getCurrentItem()).getId());
                                } else {
                                    Toast.makeText(CustomerAddressSelection.this, "Action not allowed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
            }
        });

        new CustomerAddressJSON().execute();
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        ArrayList<ViewGroup> views;
        LayoutInflater inflater;

        TextView Landmark, Zipcode;

        public MyViewPagerAdapter(Activity ctx, TextView Landmark, TextView Zipcode) {
            inflater = LayoutInflater.from(ctx);
            //instantiate your views list
            views = new ArrayList<ViewGroup>(CustomerAddressItemAL.size());
            this.Landmark = Landmark;
            this.Zipcode = Zipcode;
        }

        public void release() {
//            views.clear();
            views = null;
        }

        @Override
        public int getCount() {
            return CustomerAddressItemAL.size();
        }

        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup currentView;
            if (views.size() > position && views.get(position) != null) {
                currentView = views.get(position);
            } else {
                int rootLayout = R.layout.customer_address_pagerview;
                currentView = (ViewGroup) inflater.inflate(rootLayout, container, false);

                ((TextView) currentView.findViewById(R.id.txtAddress)).setText(CustomerAddressItemAL.get(position).getAddress());
//                txtLandmark.setText(CustomerAddressItemAL.get(position).getLandmark().toString());
//                txtZipCode.setText(CustomerAddressItemAL.get(position).getZipcode().toString());

            }
            container.addView(currentView);
            return currentView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            txtLandmark.setText(CustomerAddressItemAL.get(position).getLandmark().toString());
            txtZipCode.setText(CustomerAddressItemAL.get(position).getZipcode().toString());
            return CustomerAddressItemAL.get(position).getTitle();
        }
    }


    class CustomerAddressJSON extends AsyncTask<Void, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerAddressSelection.this);
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
                    pagerAdapter = new MyViewPagerAdapter(CustomerAddressSelection.this, txtLandmark, txtZipCode);
                    viewPager.setAdapter(pagerAdapter);
                    viewPager.setClipToPadding(false);
                    viewPager.setPageMargin(12);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
                            @Override
                            public void transformPage(View page, float position) {

                            }
                        };
                        viewPager.setPageTransformer(true, pageTransformer);

                        for (int i = 0; i < pagerTabStrip.getChildCount(); ++i) {
                            View nextChild = pagerTabStrip.getChildAt(i);
                            if (nextChild instanceof TextView) {
                                TextView textViewToConvert = (TextView) nextChild;
                                textViewToConvert.setTextColor(getResources().getColor(R.color.golden));
                                textViewToConvert.setTextSize(15);
                            }
                        }
                    }
                } else {
                    Toast.makeText(CustomerAddressSelection.this, "No Address...", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CustomerAddressSelection.this, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class CustomerDeleteAddressJSON extends AsyncTask<String, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerAddressSelection.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String id = params[0];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("id", id));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_DELETE_ADDRESS);
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
                CustomerAddressItemAL.remove(viewPager.getCurrentItem());
                viewPager.setAdapter(null);
                pagerAdapter = new MyViewPagerAdapter(CustomerAddressSelection.this, txtLandmark, txtZipCode);
                viewPager.setAdapter(pagerAdapter);
                Toast.makeText(CustomerAddressSelection.this, "Address deleted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CustomerAddressSelection.this, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        pagerAdapter.release();
    }
}
