package com.callndata.Merchnt;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.folivesservices.R;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

/**
 * Created by ravikant on 22-04-2016.
 */
public class PersonalInfoTabFragment extends Fragment{

    TextView txtName, txtEmail, txtMobile;
    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newmerchant_personalinfo_tab, container, false);

        txtName = (TextView) view.findViewById(R.id.txtName);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtMobile = (TextView) view.findViewById(R.id.txtMobile);

        return view;
    }

//    public class GetBankDetailsJSON extends AsyncTask<Void, Integer, Void> {
//
//        String status = "0";
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setCancelable(false);
//            pDialog.setMessage("Please Wait...");
//            pDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//
//                nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
//
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_SETTINGS);
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                HttpResponse response = httpclient.execute(httppost);
//
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
//                String imgResJSON = reader.readLine();
//
//                JSONObject obj = new JSONObject(imgResJSON);
//
//                status = obj.getString("success");
//
//                if (status.equals("true")) {
//                    JSONArray arrBankDetails = obj.getJSONArray("bank_details");
//
//                    for (int i = 0; i < arrBankDetails.length(); i++) {
//
//                        JSONObject objAcc = arrBankDetails.getJSONObject(i);
//
//                        NewMerchantBankDetailsItemItem = new NewMerchantBankDetailsItem();
//                        NewMerchantBankDetailsItemItem.setId(objAcc.getString("id"));
//                        NewMerchantBankDetailsItemItem.setAccount_name(objAcc.getString("account_name"));
//                        NewMerchantBankDetailsItemItem.setAcc_num(objAcc.getString("bank_account_number"));
//                        NewMerchantBankDetailsItemItem.setBank_name(objAcc.getString("bank_name"));
//                        NewMerchantBankDetailsItemItem.setStatus(objAcc.getString("status"));
//
//                        NewMerchantBankDetailsItemAL.add(NewMerchantBankDetailsItemItem);
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            if (pDialog.isShowing()) {
//                pDialog.dismiss();
//            }
//
//            if (status.equals("true")) {
//                adapter = new NewMerchantBankDetailsAdapter(NewMerchantBankDetails.this,NewMerchantBankDetailsItemAL);
//                lstBankDetails.setAdapter(adapter);
//            } else if (status.equals("0")) {
//                Toast.makeText(NewMerchantBankDetails.this, "Oops!Try again later....", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(NewMerchantBankDetails.this, "Server error...", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

}
