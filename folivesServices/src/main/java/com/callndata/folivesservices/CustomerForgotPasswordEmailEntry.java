package com.callndata.folivesservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by ravikant on 22-02-2016.
 */
public class CustomerForgotPasswordEmailEntry extends Activity {

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    String email;
    EditText etEmail;
    ImageView imgContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        etEmail = (EditText) findViewById(R.id.etEmail);
        imgContinue = (ImageView) findViewById(R.id.imgContinue);

        imgContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = etEmail.getText().toString();
                new CustomerForgotPasswordOTPGenerationJSON().execute(email);
            }
        });

    }

    class CustomerForgotPasswordOTPGenerationJSON extends AsyncTask<String, Void, Void> {

        String status = "0";
        String email;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerForgotPasswordEmailEntry.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            HttpPost httppost;
            email = params[0];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", email));

                HttpClient httpclient = new DefaultHttpClient();
                if (AppConstants.LOGIN_FLAG.equals("chefin")) {
                    httppost = new HttpPost(AppConstants.MERCHANT_FORGOT_PASSWORD_EMAIL_SUBMIT);
                } else {
                    httppost = new HttpPost(AppConstants.CUSTOMER_FORGOT_PASSWORD_EMAIL_SUBMIT);
                }
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
                Toast.makeText(CustomerForgotPasswordEmailEntry.this, "Check your E-Mail for OTP", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerForgotPasswordEmailEntry.this, CustomerForgotPasswordOTP.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(CustomerForgotPasswordEmailEntry.this, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
