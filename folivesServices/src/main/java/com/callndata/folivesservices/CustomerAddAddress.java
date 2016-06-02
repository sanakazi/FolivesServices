package com.callndata.folivesservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ravikant on 04-02-2016.
 */
public class CustomerAddAddress extends Activity {

    EditText etAddressNick, etFullAddress, etLandmark, etCity, etState, etPostCode;
    String AddressNick, FullAddress, Landmark, City, State, PostCode;
    String AddressFlag;

    LinearLayout llUseThisAddress, llAdd;
    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_add_address);

        etAddressNick = (EditText) findViewById(R.id.etAddressNick);
        etFullAddress = (EditText) findViewById(R.id.etFullAddress);
        etLandmark = (EditText) findViewById(R.id.etLandmark);
        etCity = (EditText) findViewById(R.id.etCity);
        etState = (EditText) findViewById(R.id.etState);
        etPostCode = (EditText) findViewById(R.id.etPostCode);

        llUseThisAddress = (LinearLayout) findViewById(R.id.llUseThisAddress);
        llAdd = (LinearLayout) findViewById(R.id.llAdd);


        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValues();
                new CustomerFavouriteJSON().execute();
            }
        });

    }

    public void getValues() {
        AddressNick = etAddressNick.getText().toString();
        FullAddress = etFullAddress.getText().toString();
        Landmark = etLandmark.getText().toString();
        City = etCity.getText().toString();
        State = etState.getText().toString();
        PostCode = etPostCode.getText().toString();
    }

    class CustomerFavouriteJSON extends AsyncTask<Void, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerAddAddress.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("address_name", AddressNick));
                nameValuePairs.add(new BasicNameValuePair("address", FullAddress));
                nameValuePairs.add(new BasicNameValuePair("landmark", Landmark));
                nameValuePairs.add(new BasicNameValuePair("city", City));
                nameValuePairs.add(new BasicNameValuePair("state", State));
                nameValuePairs.add(new BasicNameValuePair("post_code", PostCode));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_ADD_ADDRESS);
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
                Toast.makeText(CustomerAddAddress.this, "Address Added...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerAddAddress.this, CustomerAddressSelection.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(CustomerAddAddress.this, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
