package com.callndata.folivesservices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

/**
 * Created by ravikant on 03-02-2016.
 */
public class CustomerPaymentCODFragment extends Fragment {

    TextView txtTotalAmt, txtGenerateOPT;
    RelativeLayout rlVerifyOTP;
    EditText etOTP;

    String OTP, MessageId;
    ProgressDialog pDialog, pDialog1, pDialog2;
    ArrayList<NameValuePair> nameValuePairs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_payment_cod, container, false);

        txtTotalAmt = (TextView) view.findViewById(R.id.txtTotalAmt);
        txtGenerateOPT = (TextView) view.findViewById(R.id.txtGenerateOPT);
        rlVerifyOTP = (RelativeLayout) view.findViewById(R.id.rlVerifyOTP);
        etOTP = (EditText) view.findViewById(R.id.etOTP);

        txtGenerateOPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GenerateOTPJSON().execute();
            }
        });

        rlVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTP = etOTP.getText().toString();
                new VerifyOTPJSON().execute(OTP);
            }
        });

        return view;
    }

    public class GenerateOTPJSON extends AsyncTask<Void, Integer, Void> {

        String status = "0";

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
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_COD_GENERATE_OTP);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                    MessageId = obj.getString("message_id").trim();
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
                etOTP.setEnabled(true);
                rlVerifyOTP.setClickable(true);
                Toast.makeText(getActivity(), "OTP Generated.", Toast.LENGTH_SHORT).show();
            } else if (status.equals("0")) {
                Toast.makeText(getActivity(), "Oops!Try again later....", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Server error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class VerifyOTPJSON extends AsyncTask<String, Integer, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(getActivity());
            pDialog1.setCancelable(false);
            pDialog1.setMessage("Please Wait...");
            pDialog1.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String OTP = params[0];

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("otp_code", OTP));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_COD_VERIFY_OTP);
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
            if (pDialog1.isShowing()) {
                pDialog1.dismiss();
            }

            if (status.equals("true")) {
                Toast.makeText(getActivity(), "OTP Verified...", Toast.LENGTH_SHORT).show();
                new CompleteOrderJSON().execute();
            } else if (status.equals("0")) {
                Toast.makeText(getActivity(), "Oops!Try again later....", Toast.LENGTH_SHORT).show();
            } else {
                etOTP.setError("Invalid Code.");
            }
        }
    }

    public class CompleteOrderJSON extends AsyncTask<Void, Integer, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2 = new ProgressDialog(getActivity());
            pDialog2.setCancelable(false);
            pDialog2.setMessage("Please Wait...");
            pDialog2.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("payment_type", "COD"));
                nameValuePairs.add(new BasicNameValuePair("payment_order_id", ((CustomerPaymentMain) getActivity()).PaymentOrderId));
                nameValuePairs.add(new BasicNameValuePair("transaction_id", ((CustomerPaymentMain) getActivity()).TransactionId));
                nameValuePairs.add(new BasicNameValuePair("total", ((CustomerPaymentMain) getActivity()).ToatalAmt));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_COD_COMPLETE_ORDER);
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
            if (pDialog2.isShowing()) {
                pDialog2.dismiss();
            }

            if (status.equals("true")) {
                AppConstants.CompleteFlag = "1";
                Toast.makeText(getActivity(), "Order Completed...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CustomerThankYouActivity.class);
                intent.putExtra("order_id", ((CustomerPaymentMain) getActivity()).PaymentOrderId);
                intent.putExtra("transaction_id", ((CustomerPaymentMain) getActivity()).TransactionId);
                startActivity(intent);

            } else if (status.equals("0")) {
                Toast.makeText(getActivity(), "Oops!Try again later....", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Server error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
