package com.callndata.Merchnt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.callndata.others.AppConstants;
import com.example.folivesservices.R;

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
 * Created by ravikant on 17-03-2016.
 */
public class NewMerchantSettingsFragment extends Fragment {

    LinearLayout llPersonalInformations, llBankDetails;
    Switch swhEmailNotification, swhSMSNotification, swhPushNotification, swhOrderingEnabled;

    ProgressDialog pDialog, pDialog1;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();

    String PushN, EmailN, SmsN, OrderingN;

    String SwhFlag = "0";

    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.fragment_new_merchant_settings, container, false);

        llPersonalInformations = (LinearLayout) view.findViewById(R.id.llPersonalInformations);
        llBankDetails = (LinearLayout) view.findViewById(R.id.llBankDetails);

        swhEmailNotification = (Switch) view.findViewById(R.id.swhEmailNotification);
        swhSMSNotification = (Switch) view.findViewById(R.id.swhSMSNotification);
        swhPushNotification = (Switch) view.findViewById(R.id.swhPushNotification);
        swhOrderingEnabled = (Switch) view.findViewById(R.id.swhOrderingEnabled);

        swhEmailNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (SwhFlag.equals("1")) {
                    if (isChecked) {
                        new SettingsChangeJSON().execute("email_notification", "1");
                    } else {
                        new SettingsChangeJSON().execute("email_notification", "0");
                    }
                }
            }
        });

        swhSMSNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (SwhFlag.equals("1")) {
                    if (isChecked) {
                        new SettingsChangeJSON().execute("sms_notification", "1");
                    } else {
                        new SettingsChangeJSON().execute("sms_notification", "0");
                    }
                }
            }
        });

        swhPushNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (SwhFlag.equals("1")) {
                    if (isChecked) {
                        new SettingsChangeJSON().execute("push_notification", "1");
                    } else {
                        new SettingsChangeJSON().execute("push_notification", "0");
                    }
                }
            }
        });

        swhOrderingEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (SwhFlag.equals("1")) {
                    if (isChecked) {
                        new SettingsChangeJSON().execute("ordering_enabled", "1");
                    } else {
                        new SettingsChangeJSON().execute("ordering_enabled", "0");
                    }
                }
            }
        });

        llPersonalInformations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewMerchantPersonalInfoMain.class);
                startActivity(intent);
            }
        });

        llBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewMerchantBankDetails.class);
                startActivity(intent);
            }
        });

        new NewMerchantWithdrawalJSON().execute();

        return view;
    }

    class NewMerchantWithdrawalJSON extends AsyncTask<Void, Void, Void> {

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
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_SETTINGS);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                    JSONArray arrSettings = new JSONArray();

                    arrSettings = obj.getJSONArray("settings");

                    for (int i = 0; i < arrSettings.length(); i++) {

                        JSONObject objSettings = new JSONObject();
                        objSettings = arrSettings.getJSONObject(i);

                        PushN = objSettings.getString("push_notification");
                        EmailN = objSettings.getString("email_notification");
                        SmsN = objSettings.getString("sms_notification");
                        OrderingN = objSettings.getString("ordering_enabled");
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

                SwhFlag = "0";

                if (PushN.equals("1")) {
                    swhPushNotification.setChecked(true);
                } else {
                    swhPushNotification.setChecked(false);
                }

                if (EmailN.equals("1")) {
                    swhEmailNotification.setChecked(true);
                } else {
                    swhEmailNotification.setChecked(false);
                }

                if (SmsN.equals("1")) {
                    swhSMSNotification.setChecked(true);
                } else {
                    swhSMSNotification.setChecked(false);
                }

                if (OrderingN.equals("1")) {
                    swhOrderingEnabled.setChecked(true);
                } else {
                    swhOrderingEnabled.setChecked(false);
                }

                SwhFlag = "1";

            } else {
                Toast.makeText(getActivity(), "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class SettingsChangeJSON extends AsyncTask<String, Void, Void> {

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
        protected Void doInBackground(String... params) {

            String Type, Value;
            Type = params[0];
            Value = params[1];

            try {
                nameValuePairs1 = new ArrayList<NameValuePair>();
                nameValuePairs1.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs1.add(new BasicNameValuePair("type", Type));
                nameValuePairs1.add(new BasicNameValuePair("value", Value));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_SETTINGS_CHANGE);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
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
            if (pDialog1.isShowing()) {
                pDialog1.dismiss();
            }

            if (status.equals("true")) {
                Toast.makeText(getActivity(), "Setting Changed...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
