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
public class CustomerForgotPasswordOTP extends Activity {

    String OTP, email;
    EditText txtOTP;
    ImageView imgConfirm;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_forgotpassword_otp);

        email = getIntent().getExtras().getString("email");

        txtOTP = (EditText) findViewById(R.id.txtOTP);
        imgConfirm = (ImageView) findViewById(R.id.imgConfirm);

        imgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTP = txtOTP.getText().toString();
                new CustomerForgotPasswordOTPGenerationJSON().execute(email, OTP);
            }
        });
    }

    class CustomerForgotPasswordOTPGenerationJSON extends AsyncTask<String, Void, Void> {

        String status = "0";
        String email, OTP;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerForgotPasswordOTP.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            HttpPost httppost;
            email = params[0];
            OTP = params[1];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("code", OTP));

                HttpClient httpclient = new DefaultHttpClient();
                if (AppConstants.LOGIN_FLAG.equals("chefin")) {
                    httppost = new HttpPost(AppConstants.MERCHANT_VERIFY_USER_EMAILCODE);
                } else {
                    httppost = new HttpPost(AppConstants.CUSTOMER_VERIFY_USER_EMAILCODE);
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
                Toast.makeText(CustomerForgotPasswordOTP.this, "OTP Confirmed.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerForgotPasswordOTP.this, CustomerResetPasswordAtivity.class);
                intent.putExtra("email", email);
                intent.putExtra("otp", OTP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(CustomerForgotPasswordOTP.this, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
