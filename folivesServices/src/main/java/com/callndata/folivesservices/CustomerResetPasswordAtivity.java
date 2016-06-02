package com.callndata.folivesservices;

import android.app.Activity;
import android.app.ProgressDialog;
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
public class CustomerResetPasswordAtivity extends Activity {

    EditText txtPassword, txtConfirmPassword;
    ImageView imgLogin;

    String email, pass, cpass, OTP;
    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reset_password);

        email = getIntent().getExtras().getString("email");
        OTP = getIntent().getExtras().getString("otp");

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);

        imgLogin = (ImageView) findViewById(R.id.imgLogin);

        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = txtPassword.getText().toString();
                cpass = txtConfirmPassword.getText().toString();


                    new CustomerForgotPasswordOTPGenerationJSON().execute(email, pass, cpass, OTP);

            }
        });
    }

    class CustomerForgotPasswordOTPGenerationJSON extends AsyncTask<String, Void, Void> {

        String status = "0";
        String email;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerResetPasswordAtivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            HttpPost httppost;
            email = params[0];
            String pass = params[1];
            String confirmpass = params[2];
            String OTP = params[3];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("new_password", pass));
                nameValuePairs.add(new BasicNameValuePair("confirm_password", confirmpass));
                nameValuePairs.add(new BasicNameValuePair("code", OTP));

                HttpClient httpclient = new DefaultHttpClient();

                if (AppConstants.LOGIN_FLAG.equals("chefin")) {
                    httppost = new HttpPost(AppConstants.MERCHANT_CHANGE_PASSWORD);
                } else {
                    httppost = new HttpPost(AppConstants.CUSTOMER_CHANGE_PASSWORD);
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
                Toast.makeText(CustomerResetPasswordAtivity.this, "Password Changed.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(CustomerResetPasswordAtivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
