package com.callndata.Merchnt;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapterM.MerchantItemReviewAdapter;
import com.callndata.others.AppConstants;
import com.callndata.others.HorizontalPager;
import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantReviewItem;

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
 * Created by ravikant on 16-03-2016.
 */
public class NewMerchantReviewsFragment extends Fragment {

    TextView txtItemCount, txtChefCount;
    HorizontalPager horizontal_pager;
    LinearLayout llFavItems, llFavItemLine, llFavChef, llFavChefLine;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    NewMerchantReviewItem NewMerchantReviewItemItem;
    ArrayList<NewMerchantReviewItem> NewMerchantReviewItemITEMAL = new ArrayList<NewMerchantReviewItem>();
    ArrayList<NewMerchantReviewItem> NewMerchantReviewItemCHEFAL = new ArrayList<NewMerchantReviewItem>();

    String ItemReviewCount, ChefReviewCount;

    MerchantItemReviewAdapter adapter;

    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.fragment_new_merchant_reviews, container, false);

        txtItemCount = (TextView) view.findViewById(R.id.txtItemCount);
        txtChefCount = (TextView) view.findViewById(R.id.txtChefCount);
        llFavItems = (LinearLayout) view.findViewById(R.id.llFavItems);
        llFavItemLine = (LinearLayout) view.findViewById(R.id.llFavItemLine);
        llFavChef = (LinearLayout) view.findViewById(R.id.llFavChef);
        llFavChefLine = (LinearLayout) view.findViewById(R.id.llFavChefLine);
        horizontal_pager = (HorizontalPager) view.findViewById(R.id.horizontal_pager);

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

        horizontal_pager.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener() {

            @Override
            public void onScreenSwitched(int screen) {
                if (screen == 0) {

                    llFavItemLine.setBackgroundColor(getResources().getColor(R.color.golden));
                    llFavChefLine.setBackgroundResource(0);
                } else if (screen == 1) {

                    llFavItemLine.setBackgroundResource(0);
                    llFavChefLine.setBackgroundColor(getResources().getColor(R.color.golden));
                }

            }
        });

        new NewMerchantReviewsJSON().execute();

        return view;
    }

    class NewMerchantReviewsJSON extends AsyncTask<Void, Void, Void> {

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

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_REVIEWS);
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
                    JSONArray arrITEM = new JSONArray();
                    JSONArray arrCHEF = new JSONArray();

                    arrITEM = obj.getJSONArray("item_reviews");
                    arrCHEF = obj.getJSONArray("chef_reviews");

                    for (int i = 0; i < arrITEM.length(); i++) {

                        ItemReviewCount = Integer.toString(arrITEM.length());

                        JSONObject objItem = new JSONObject();
                        objItem = arrITEM.getJSONObject(i);
                        NewMerchantReviewItemItem = new NewMerchantReviewItem();

                        NewMerchantReviewItemItem.setId(objItem.getString("id"));
                        NewMerchantReviewItemItem.setReview(objItem.getString("review"));
                        NewMerchantReviewItemItem.setRating(objItem.getString("rating"));
                        NewMerchantReviewItemItem.setDateTime(objItem.getString("created_at"));

                        JSONObject objUser = new JSONObject();
                        objUser = objItem.getJSONObject("user");

                        NewMerchantReviewItemItem.setName(objUser.getString("first_name") + " " + objUser.getString("last_name"));

                        String FbId = objUser.getString("fb_id");
                        if (FbId.equals("0")) {
                            NewMerchantReviewItemItem.setPic(objUser.getString("profile_photo"));
                        } else {
                            NewMerchantReviewItemItem.setPic(AppConstants.Customer_Profile_Pic + objUser.getString("profile_photo"));
                        }

                        NewMerchantReviewItemITEMAL.add(NewMerchantReviewItemItem);

                    }

                    for (int i = 0; i < arrCHEF.length(); i++) {

                        ChefReviewCount = Integer.toString(arrCHEF.length());

                        JSONObject objChef = new JSONObject();
                        objChef = arrCHEF.getJSONObject(i);
                        NewMerchantReviewItemItem = new NewMerchantReviewItem();

                        NewMerchantReviewItemItem.setId(objChef.getString("id"));
                        NewMerchantReviewItemItem.setReview(objChef.getString("review"));
                        NewMerchantReviewItemItem.setRating(objChef.getString("rating"));
                        NewMerchantReviewItemItem.setDateTime(objChef.getString("created_at"));

                        JSONObject objUser = new JSONObject();
                        objUser = objChef.getJSONObject("user");

                        NewMerchantReviewItemItem.setName(objUser.getString("first_name") + " " + objUser.getString("last_name"));

                        String FbId = objUser.getString("fb_id");
                        if (FbId.equals("0")) {
                            NewMerchantReviewItemItem.setPic(objUser.getString("profile_photo"));
                        } else {
                            NewMerchantReviewItemItem.setPic(AppConstants.Customer_Profile_Pic + objUser.getString("profile_photo"));
                        }

                        NewMerchantReviewItemCHEFAL.add(NewMerchantReviewItemItem);
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

                txtItemCount.setText(Integer.toString(NewMerchantReviewItemITEMAL.size()));
                txtChefCount.setText(Integer.toString(NewMerchantReviewItemCHEFAL.size()));

                for (int i = 0; i < 2; i++) {
                    ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    if (i == 0) {
                        ListView lstItemReviews = new ListView(getActivity());
                        lstItemReviews.setLayoutParams(lparams);

                        if (NewMerchantReviewItemITEMAL.size() > 0) {
                            adapter = new MerchantItemReviewAdapter(getActivity(), NewMerchantReviewItemITEMAL);
                            lstItemReviews.setAdapter(adapter);
                            horizontal_pager.addView(lstItemReviews);
                        } else {
                            ImageView img = new ImageView(getActivity());
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontal_pager.addView(img);
                        }

                    } else if (i == 1) {
                        ListView lstChefReviews = new ListView(getActivity());
                        lstChefReviews.setLayoutParams(lparams);

                        if (NewMerchantReviewItemCHEFAL.size() > 0) {
                            adapter = new MerchantItemReviewAdapter(getActivity(), NewMerchantReviewItemCHEFAL);
                            lstChefReviews.setAdapter(adapter);
                            horizontal_pager.addView(lstChefReviews);
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
