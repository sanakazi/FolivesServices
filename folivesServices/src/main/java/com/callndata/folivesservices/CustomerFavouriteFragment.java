package com.callndata.folivesservices;

import android.app.ProgressDialog;
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

import com.callndata.adapters.CustomerFavChefAdapter;
import com.callndata.adapters.CustomerFavFoodAdapter;
import com.callndata.others.AppConstants;
import com.callndata.others.HorizontalPager;
import com.callndata.others.HorizontalPager.OnScreenSwitchListener;
import com.example.folivesservices.R;
import com.folives.item.CustomerFavouriteChefItem;
import com.folives.item.CustomerFavouriteFoodItem;
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

public class CustomerFavouriteFragment extends Fragment {

    LinearLayout llFavItems, llFavItemLine, llFavChef, llFavChefLine;
    HorizontalPager horizontal_pager;
    TextView txtItemCount, txtChefCount;
    ImageView imgProfilePic;

    ProgressDialog pDialog, pDialog1;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    CustomerFavFoodAdapter adapterFavFood;
    CustomerFavChefAdapter adapterFavChef;
    CustomerFavouriteFoodItem CFFoodItem;
    CustomerFavouriteChefItem CFChefItem;
    ArrayList<CustomerFavouriteFoodItem> FavouriteItemsAL = new ArrayList<CustomerFavouriteFoodItem>();
    ArrayList<CustomerFavouriteChefItem> FavouriteChefAL = new ArrayList<CustomerFavouriteChefItem>();

    ListView lstFavChef;

    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.activity_customer_fav, container, false);

        llFavItems = (LinearLayout) view.findViewById(R.id.llFavItems);
        llFavItemLine = (LinearLayout) view.findViewById(R.id.llFavItemLine);
        llFavChef = (LinearLayout) view.findViewById(R.id.llFavChef);
        llFavChefLine = (LinearLayout) view.findViewById(R.id.llFavChefLine);

        horizontal_pager = (HorizontalPager) view.findViewById(R.id.horizontal_pager);
        txtItemCount = (TextView) view.findViewById(R.id.txtItemCount);
        txtChefCount = (TextView) view.findViewById(R.id.txtChefCount);

        imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
        try {
            Picasso.with(getActivity()).load(AppConstants.Customer_Profile_Pic).into(imgProfilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lstFavChef = new ListView(getActivity());

        llFavItems.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                llFavItemLine.setBackgroundColor(getResources().getColor(R.color.red));
                llFavChefLine.setBackgroundResource(0);
                horizontal_pager.setCurrentScreen(0, true);
            }
        });

        llFavChef.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                llFavItemLine.setBackgroundResource(0);
                llFavChefLine.setBackgroundColor(getResources().getColor(R.color.red));
                horizontal_pager.setCurrentScreen(1, true);
            }
        });

        horizontal_pager.setOnScreenSwitchListener(new OnScreenSwitchListener() {

            @Override
            public void onScreenSwitched(int screen) {
                if (screen == 0) {

                    llFavItemLine.setBackgroundColor(getResources().getColor(R.color.red));
                    llFavChefLine.setBackgroundResource(0);
                } else if (screen == 1) {

                    llFavItemLine.setBackgroundResource(0);
                    llFavChefLine.setBackgroundColor(getResources().getColor(R.color.red));
                }

            }
        });

        lstFavChef.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CustomerChefItemsAndReviews.class);
                intent.putExtra("chefId", FavouriteChefAL.get(position).getChefId());
                startActivity(intent);
            }
        });

        new CustomerFavouriteJSON().execute();

        return view;
    }

    class CustomerFavouriteJSON extends AsyncTask<Void, Void, Void> {

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
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_FAVOURITE);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                // String imgResJSON =
                // EntityUtils.toString(response.getEntity());

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                    JSONArray arrFI = new JSONArray();
                    JSONArray arrFC = new JSONArray();

                    arrFI = obj.getJSONArray("favourite_items");
                    arrFC = obj.getJSONArray("favourite_chefs");

                    for (int i = 0; i < arrFI.length(); i++) {
                        JSONObject objItem = new JSONObject();
                        objItem = arrFI.getJSONObject(i);

                        CFFoodItem = new CustomerFavouriteFoodItem();

                        CFFoodItem.setFavId(objItem.getString("favourite_id"));

                        JSONObject FoodDetailsObj = new JSONObject();
                        FoodDetailsObj = objItem.getJSONObject("items");
                        CFFoodItem
                                .setFoodPic(AppConstants.Customer_Dish_Image_path + FoodDetailsObj.getString("photo"));
                        CFFoodItem.setFoodName(FoodDetailsObj.getString("item_name"));

                        JSONObject chefDetailsObj = new JSONObject();
                        chefDetailsObj = FoodDetailsObj.getJSONObject("merchant");

                        CFFoodItem.setChef_id(chefDetailsObj.getString("id"));
                        CFFoodItem.setChefPic(AppConstants.Customer_Chef_Image_path + chefDetailsObj.getString("logo"));
                        CFFoodItem.setChefName(chefDetailsObj.getString("name"));

                        CFFoodItem.setVegNonveg(FoodDetailsObj.getString("dish"));

                        JSONArray FoodRatingArr = new JSONArray();
                        FoodRatingArr = objItem.getJSONArray("item_rating");
                        int cnt = FoodRatingArr.length();
                        if (cnt > 0) {
                            JSONObject FoodRatingObj = new JSONObject();
                            FoodRatingObj = FoodRatingArr.getJSONObject(0);
                            CFFoodItem.setFoodRating(FoodRatingObj.getString("average"));
                        } else {
                            CFFoodItem.setFoodRating("0");
                        }
                        CFFoodItem.setFoodPrice("₹ " + FoodDetailsObj.getString("price"));

                        FavouriteItemsAL.add(CFFoodItem);

                    }

                    for (int i = 0; i < arrFC.length(); i++) {

                        JSONObject objItemFC = new JSONObject();
                        objItemFC = arrFC.getJSONObject(i);
                        CFChefItem = new CustomerFavouriteChefItem();

                        CFChefItem.setFavId(objItemFC.getString("favourite_id"));

                        JSONObject chefDetailsObj = new JSONObject();
                        chefDetailsObj = objItemFC.getJSONObject("chefs");
                        CFChefItem.setChefId(chefDetailsObj.getString("id"));
                        CFChefItem.setChefPic(AppConstants.Customer_Chef_Image_path + chefDetailsObj.getString("logo"));
                        CFChefItem.setChefName(chefDetailsObj.getString("name"));

                        JSONArray chefRatingArr = new JSONArray();
                        chefRatingArr = objItemFC.getJSONArray("chef_rating");
                        JSONObject chefRatingObj = new JSONObject();
                        int cnt = chefRatingArr.length();
                        if (cnt > 0) {
                            chefRatingObj = chefRatingArr.getJSONObject(0);
                            CFChefItem.setChefRating(chefRatingObj.getString("average"));
                        } else {
                            CFChefItem.setChefRating("0");
                        }
                        FavouriteChefAL.add(CFChefItem);
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

                txtItemCount.setText(Integer.toString(FavouriteItemsAL.size()));
                txtChefCount.setText(Integer.toString(FavouriteChefAL.size()));

                for (int i = 0; i < 2; i++) {
                    LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                    if (i == 0) {
                        ListView lstFavFood = new ListView(getActivity());
                        lstFavFood.setLayoutParams(lparams);
                        if (FavouriteItemsAL.size() > 0) {
                            adapterFavFood = new CustomerFavFoodAdapter(getActivity(), FavouriteItemsAL, txtItemCount);
                            lstFavFood.setAdapter(adapterFavFood);
                            horizontal_pager.addView(lstFavFood);
                        } else {
                            ImageView img = new ImageView(getActivity());
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontal_pager.addView(img);
                        }

                    } else if (i == 1) {
                        lstFavChef.setLayoutParams(lparams);
                        if (FavouriteChefAL.size() > 0) {
                            adapterFavChef = new CustomerFavChefAdapter(getActivity(), FavouriteChefAL, txtChefCount);
                            lstFavChef.setAdapter(adapterFavChef);
                            horizontal_pager.addView(lstFavChef);
                        } else {
                            ImageView img = new ImageView(getActivity());
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontal_pager.addView(img);
                        }
                    }
                }
            } else {
                Toast.makeText(getActivity(), "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

}