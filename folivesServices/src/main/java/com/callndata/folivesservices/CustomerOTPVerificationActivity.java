package com.callndata.folivesservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class CustomerOTPVerificationActivity extends Activity {

    String MNum, code;

    EditText etOTP;
    TextView txtResendOTP;
    LinearLayout llVerify;
    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        MNum = getIntent().getExtras().getString("MNum");

        txtResendOTP = (TextView) findViewById(R.id.txtResendOTP);
        llVerify = (LinearLayout) findViewById(R.id.llVerify);
        etOTP = (EditText) findViewById(R.id.etOTP);

        SpannableString content = new SpannableString("RESEND OTP");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtResendOTP.setText(content);

        llVerify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                code = etOTP.getText().toString();
                new CustomerRegistrationJSON().execute(MNum, code);
            }
        });

    }

    class CustomerRegistrationJSON extends AsyncTask<String, Void, Void> {

        String status = "0";
        String mnum, code;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerOTPVerificationActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {

            mnum = params[0];
            code = params[1];

            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("mobile", mnum));
                nameValuePairs.add(new BasicNameValuePair("code", code));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_OTP_VERIFICATION);
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
                Toast.makeText(CustomerOTPVerificationActivity.this, "Verification done.Please Login.",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerOTPVerificationActivity.this, CustomerLoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(CustomerOTPVerificationActivity.this, "Wrong Code...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
