package com.callndata.Merchnt;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapterM.MerchantNotifications;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantNotificationsItem;

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

/**
 * Created by ravikant on 14-03-2016.
 */
public class NewMerchantNotificationsFragment extends Fragment {

    TextView txtNotifications;
    ListView lstNotifications;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    NewMerchantNotificationsItem NewMerchantNotificationsItemItem;
    ArrayList<NewMerchantNotificationsItem> NewMerchantNotificationsItemAL = new ArrayList<NewMerchantNotificationsItem>();

    MerchantNotifications MNAdapter;

    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.frgmanet_new_merchant_notifications, container, false);

        txtNotifications = (TextView) view.findViewById(R.id.txtNotifications);
        lstNotifications = (ListView) view.findViewById(R.id.lstNotifications);

        new NotificationsJSON().execute();

        return view;
    }

    public class NotificationsJSON extends AsyncTask<Void, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status;

        String TotalNotifications;

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
                httppost = new HttpPost(AppConstants.NEW_MERCHANT_NOTIFICATIONS);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                status = objMain.getString("success");

                if (status.equals("true")) {

                    JSONArray arrNotifications = new JSONArray();
                    arrNotifications = objMain.getJSONArray("notifications");

                    if (arrNotifications.length() != 0) {

                        for (int i = 0; i < arrNotifications.length(); i++) {

                            TotalNotifications = Integer.toString(arrNotifications.length());

                            JSONObject objNotifications = new JSONObject();
                            objNotifications = arrNotifications.getJSONObject(i);

                            NewMerchantNotificationsItemItem = new NewMerchantNotificationsItem();

                            NewMerchantNotificationsItemItem.setNotificationId(objNotifications.getString("id"));
                            NewMerchantNotificationsItemItem.setMessage(objNotifications.getString("notification_message"));
                            NewMerchantNotificationsItemItem.setDatetime(objNotifications.getString("created_at"));

                            JSONObject objUser = new JSONObject();
                            objUser = objNotifications.getJSONObject("user");

                            NewMerchantNotificationsItemItem.setName(objUser.getString("first_name") + " " + objUser.getString("last_name"));
                            NewMerchantNotificationsItemItem.setPic(objUser.getString("profile_photo"));

                            NewMerchantNotificationsItemAL.add(NewMerchantNotificationsItemItem);
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
                txtNotifications.setText(TotalNotifications);
                MNAdapter = new MerchantNotifications(getActivity(), NewMerchantNotificationsItemAL);
                lstNotifications.setAdapter(MNAdapter);
            } else {
                Toast.makeText(getActivity(), "Oops! Try again later...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
