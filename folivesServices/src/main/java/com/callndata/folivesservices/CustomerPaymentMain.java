package com.callndata.folivesservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.List;

/**
 * Created by ravikant on 03-02-2016.
 */
public class CustomerPaymentMain extends AppCompatActivity {

    public static Activity MyActivityFlag;

  //  private TabLayout tabLayout;
    private ViewPager viewPager;

    String PaymentOrderId, TransactionId, ToatalAmt;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;

    TextView txtUserName, txtUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.customer_payment);

        MyActivityFlag = this;

        PaymentOrderId = getIntent().getExtras().getString("PaymentOrderId");
        TransactionId = getIntent().getExtras().getString("TransactionId");
        ToatalAmt = getIntent().getExtras().getString("TotalAmount");

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) findViewById(R.id.txtUserEmail);

//        txtUserName.setText(AppConstants.Customer_Name);
//        txtUserEmail.setText(AppConstants.Customer_Email);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

       // tabLayout = (TabLayout) findViewById(R.id.tabs);
      //  tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CustomerPaymentCODFragment(), "COD");
        adapter.addFragment(new CustomerPaymentCDFragment(), "DEBIT/CREDIT");
        adapter.addFragment(new CustomerPaymentCODFragment(), "NET BANKING");
        adapter.addFragment(new CustomerPaymentCODFragment(), "SAVED CARD");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class OrderCancelJSON extends AsyncTask<Void, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CustomerPaymentMain.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("payment_order_id", PaymentOrderId));
                nameValuePairs.add(new BasicNameValuePair("transaction_id", TransactionId));


                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_ORDER_CANCEL);
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
                Toast.makeText(CustomerPaymentMain.this, "Order Cancelled.", Toast.LENGTH_SHORT).show();
                CustomerAddressSelection.MyActivityFlag.finish();
                CustomerFoodItemFragment.MyActivityFlag.finish();
                CustomerOrderSummery.MyActivityFlag.finish();
                CustomerPaymentMain.this.finish();
            } else {
                Toast.makeText(CustomerPaymentMain.this, "Server error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(CustomerPaymentMain.this).setTitle("Cancel Order ?")
                .setMessage("\nOrder will be cancel.\nDo you want to proceed?\n").setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        new OrderCancelJSON().execute();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }
}
