package com.callndata.Merchnt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.callndata.adapterM.NewMerchantBankDetailsAdapter;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantBankDetailsItem;

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
 * Created by ravikant on 22-04-2016.
 */
public class NewMerchantBankDetails extends Activity {

    ListView lstBankDetails;
    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    NewMerchantBankDetailsItem NewMerchantBankDetailsItemItem;
    ArrayList<NewMerchantBankDetailsItem> NewMerchantBankDetailsItemAL = new ArrayList<NewMerchantBankDetailsItem>();

    NewMerchantBankDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_merchant_bank_details);

        lstBankDetails = (ListView) findViewById(R.id.lstBankDetails);
        new GetBankDetailsJSON().execute();

    }


    public class GetBankDetailsJSON extends AsyncTask<Void, Integer, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewMerchantBankDetails.this);
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
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_SETTINGS);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                    JSONArray arrBankDetails = obj.getJSONArray("bank_details");

                    for (int i = 0; i < arrBankDetails.length(); i++) {

                        JSONObject objAcc = arrBankDetails.getJSONObject(i);

                        NewMerchantBankDetailsItemItem = new NewMerchantBankDetailsItem();
                        NewMerchantBankDetailsItemItem.setId(objAcc.getString("id"));
                        NewMerchantBankDetailsItemItem.setAccount_name(objAcc.getString("account_name"));
                        NewMerchantBankDetailsItemItem.setAcc_num(objAcc.getString("bank_account_number"));
                        NewMerchantBankDetailsItemItem.setBank_name(objAcc.getString("bank_name"));
                        NewMerchantBankDetailsItemItem.setStatus(objAcc.getString("status"));

                        NewMerchantBankDetailsItemAL.add(NewMerchantBankDetailsItemItem);
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
                adapter = new NewMerchantBankDetailsAdapter(NewMerchantBankDetails.this,NewMerchantBankDetailsItemAL);
                lstBankDetails.setAdapter(adapter);
            } else if (status.equals("0")) {
                Toast.makeText(NewMerchantBankDetails.this, "Oops!Try again later....", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NewMerchantBankDetails.this, "Server error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
