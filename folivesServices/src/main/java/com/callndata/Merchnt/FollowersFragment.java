package com.callndata.Merchnt;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.callndata.adapterM.MerchantFollowers;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantFollowersItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FollowersFragment extends Fragment {

    GridView gvFollowers;
    TextView txtFollowers;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    NewMerchantFollowersItem Followersitem;
    ArrayList<NewMerchantFollowersItem> NewMerchantFollowersItemAL = new ArrayList<NewMerchantFollowersItem>();
    MerchantFollowers MFAdapter;

    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.fragment_new_merchant_followers, container, false);

        gvFollowers = (GridView) view.findViewById(R.id.gvFollowers);
        txtFollowers = (TextView) view.findViewById(R.id.txtFollowers);
        new FollowersJSON().execute();
        return view;
    }

    public class FollowersJSON extends AsyncTask<Void, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status;

        String TotalFollowers;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.NEW_MERCHANT_FOLLOWERS);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                status = objMain.getString("success");

                if (status.equals("true")) {

                    TotalFollowers = objMain.getString("total_followers");

                    JSONArray arrFollowers = new JSONArray();
                    arrFollowers = objMain.getJSONArray("followers");

                    if (arrFollowers.length() != 0) {
                        for (int i = 0; i < arrFollowers.length(); i++) {
                            JSONObject objFollowers = new JSONObject();
                            objFollowers = arrFollowers.getJSONObject(i);

                            JSONObject objUser = new JSONObject();
                            objUser = objFollowers.getJSONObject("user");

                            Followersitem = new NewMerchantFollowersItem();
                            Followersitem.setFb_id(objUser.getString("fb_id"));
                            Followersitem.setName(objUser.getString("first_name") + " " + objUser.getString("last_name"));
                            if (objUser.getString("fb_id").equals("0")) {
                                Followersitem.setPic(AppConstants.Customer_Profile_Image_Path + objUser.getString("profile_photo"));
                            } else {
                                Followersitem.setPic(objUser.getString("profile_photo"));
                            }
                            NewMerchantFollowersItemAL.add(Followersitem);
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
                txtFollowers.setText(TotalFollowers);
                MFAdapter = new MerchantFollowers(getActivity(), NewMerchantFollowersItemAL);
                gvFollowers.setAdapter(MFAdapter);
            } else {

            }
        }
    }
}
