package com.callndata.Merchnt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapterM.MerchantWithdrawalsAdapter;
import com.callndata.others.AppConstants;
import com.callndata.others.HorizontalPager;
import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantWithdrawalsItem;

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
public class NewMerchantWithdrawlsFragment extends Fragment {

    TextView txtPrice;
    ImageView imgWithdrawal;
    HorizontalPager horizontal_pager;
    TextView txtPendingCount, txtHistoryCount;
    LinearLayout llPending, llPendingLine, llHistory, llHistoryLine, llBlackScreen;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    NewMerchantWithdrawalsItem NewMerchantWithdrawalsItemItem;
    ArrayList<NewMerchantWithdrawalsItem> NewMerchantWithdrawalsPendingAL = new ArrayList<NewMerchantWithdrawalsItem>();
    ArrayList<NewMerchantWithdrawalsItem> NewMerchantWithdrawalsHistoryAL = new ArrayList<NewMerchantWithdrawalsItem>();

    String PendingCount, HistoryCount, WithdrawableAmount;
    MerchantWithdrawalsAdapter adapter;

    ArrayList<String> AccountNumberAL = new ArrayList<String>();
    ArrayList<String> PaymentTypeAL = new ArrayList<String>();
    ArrayList<String> PaymentMethodAL = new ArrayList<String>();

    LayoutInflater layoutInflater;
    PopupWindow popupWindow;
    View popupView;
    ListView lstPending, lstHistory;


    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.fragment_new_merchant_withdrawls, container, false);

        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        txtPendingCount = (TextView) view.findViewById(R.id.txtPendingCount);
        txtHistoryCount = (TextView) view.findViewById(R.id.txtHistoryCount);

        imgWithdrawal = (ImageView) view.findViewById(R.id.imgWithdrawal);

        llPending = (LinearLayout) view.findViewById(R.id.llPending);
        llPendingLine = (LinearLayout) view.findViewById(R.id.llPendingLine);
        llHistory = (LinearLayout) view.findViewById(R.id.llHistory);
        llHistoryLine = (LinearLayout) view.findViewById(R.id.llHistoryLine);
        llBlackScreen = (LinearLayout) view.findViewById(R.id.llBlackScreen);

        horizontal_pager = (HorizontalPager) view.findViewById(R.id.horizontal_pager);


        layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_withdrawl_payment_request, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        lstPending = new ListView(getActivity());
        lstHistory = new ListView(getActivity());

        llPending.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                llPendingLine.setBackgroundColor(getResources().getColor(R.color.golden));
                llHistoryLine.setBackgroundResource(0);
                horizontal_pager.setCurrentScreen(0, true);
            }
        });

        llHistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                llPendingLine.setBackgroundResource(0);
                llHistoryLine.setBackgroundColor(getResources().getColor(R.color.golden));
                horizontal_pager.setCurrentScreen(1, true);
            }
        });

        horizontal_pager.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener() {

            @Override
            public void onScreenSwitched(int screen) {
                if (screen == 0) {
                    llPendingLine.setBackgroundColor(getResources().getColor(R.color.golden));
                    llHistoryLine.setBackgroundResource(0);
                } else if (screen == 1) {
                    llPendingLine.setBackgroundResource(0);
                    llHistoryLine.setBackgroundColor(getResources().getColor(R.color.golden));
                }
            }
        });

        lstPending.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getActivity()).setTitle("Cancel")
                        .setMessage("\nDo you want to cancel withdrwal?\n").setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                new CancelWithdrwalRequestJSON().execute(NewMerchantWithdrawalsPendingAL.get(position).getWithdrawalId());
                            }
                        }).setNegativeButton(android.R.string.no, null).show();

                return false;
            }
        });

        imgWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NewMerchantWithdrawalsPendingAL.size() == 0) {

                    final LinearLayout llPaymentType, llPaymentMethod, llAccount, llRupee, llWithdrawalRequest;
                    final TextView txtPaymentType, txtPaymentMethod, txtAccount;
                    final EditText etRupees;
                    final LinearLayout llBlackScreen1;


                    try {

                        popupWindow.setOutsideTouchable(true);
                        popupWindow.setFocusable(true);
                        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        // popupWindow.setWidth(700);
                        popupWindow.setAnimationStyle(R.style.DialogAnimation);
                        popupWindow.showAtLocation(imgWithdrawal, Gravity.CENTER, 0, 0);

                        llPaymentType = (LinearLayout) popupView.findViewById(R.id.llPaymentType);
                        llPaymentMethod = (LinearLayout) popupView.findViewById(R.id.llPaymentMethod);
                        llAccount = (LinearLayout) popupView.findViewById(R.id.llAccount);
                        llRupee = (LinearLayout) popupView.findViewById(R.id.llRupee);
                        llWithdrawalRequest = (LinearLayout) popupView.findViewById(R.id.llWithdrawalRequest);

                        txtPaymentType = (TextView) popupView.findViewById(R.id.txtPaymentType);
                        txtPaymentMethod = (TextView) popupView.findViewById(R.id.txtPaymentMethod);
                        txtAccount = (TextView) popupView.findViewById(R.id.txtAccount);

                        etRupees = (EditText) popupView.findViewById(R.id.etRupees);
                        llBlackScreen1 = (LinearLayout) popupView.findViewById(R.id.llBlackScreen);

                        llPaymentType.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                TextView txtHeader;
                                final ListView lstData;

                                LayoutInflater layoutInflater1 = (LayoutInflater) getActivity()
                                        .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                                View popupView1 = layoutInflater1.inflate(R.layout.popup_list_view, null);
                                final PopupWindow popupWindow1 = new PopupWindow(popupView1, ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT, true);
                                popupWindow1.setOutsideTouchable(true);
                                popupWindow1.setFocusable(true);
                                popupWindow1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                // popupWindow.setWidth(700);
                                popupWindow1.setAnimationStyle(R.style.DialogAnimation);

                                llBlackScreen1.setVisibility(View.VISIBLE);
                                popupWindow1.showAtLocation(imgWithdrawal, Gravity.CENTER, 0, 0);

                                txtHeader = (TextView) popupView1.findViewById(R.id.txtHeader);
                                lstData = (ListView) popupView1.findViewById(R.id.lstData);

                                txtHeader.setText("Payment Type");
                                lstData.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, PaymentTypeAL));

                                lstData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String strPT = lstData.getItemAtPosition(position).toString();
                                        txtPaymentType.setText(strPT);

                                        if (strPT.equals("Single")) {
                                            llRupee.setVisibility(View.VISIBLE);
                                            etRupees.setHint(WithdrawableAmount);
                                        } else {
                                            llRupee.setVisibility(View.GONE);
                                        }

                                        popupWindow1.dismiss();
                                        llBlackScreen1.setVisibility(View.GONE);
                                    }
                                });

                                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        llBlackScreen1.setVisibility(View.GONE);
                                    }
                                });

                            }
                        });

                        llPaymentMethod.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                TextView txtHeader;
                                final ListView lstData;

                                LayoutInflater layoutInflater1 = (LayoutInflater) getActivity()
                                        .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                                View popupView1 = layoutInflater1.inflate(R.layout.popup_list_view, null);
                                final PopupWindow popupWindow1 = new PopupWindow(popupView1, ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT, true);
                                popupWindow1.setOutsideTouchable(true);
                                popupWindow1.setFocusable(true);
                                popupWindow1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                // popupWindow.setWidth(700);
                                popupWindow1.setAnimationStyle(R.style.DialogAnimation);

                                llBlackScreen1.setVisibility(View.VISIBLE);
                                popupWindow1.showAtLocation(imgWithdrawal, Gravity.CENTER, 0, 0);

                                txtHeader = (TextView) popupView1.findViewById(R.id.txtHeader);
                                lstData = (ListView) popupView1.findViewById(R.id.lstData);

                                txtHeader.setText("Payment Method");
                                lstData.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, PaymentMethodAL));

                                lstData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String strPT = lstData.getItemAtPosition(position).toString();
                                        txtPaymentMethod.setText(strPT);
                                        popupWindow1.dismiss();
                                        llBlackScreen1.setVisibility(View.GONE);
                                    }
                                });

                                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        llBlackScreen1.setVisibility(View.GONE);
                                    }
                                });

                            }
                        });

                        llAccount.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                TextView txtHeader;
                                final ListView lstData;

                                LayoutInflater layoutInflater2 = (LayoutInflater) getActivity()
                                        .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                                View popupView2 = layoutInflater2.inflate(R.layout.popup_list_view, null);
                                final PopupWindow popupWindow2 = new PopupWindow(popupView2, ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT, true);
                                popupWindow2.setOutsideTouchable(true);
                                popupWindow2.setFocusable(true);
                                popupWindow2.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                // popupWindow.setWidth(700);
                                popupWindow2.setAnimationStyle(R.style.DialogAnimation);

                                llBlackScreen1.setVisibility(View.VISIBLE);
                                popupWindow2.showAtLocation(imgWithdrawal, Gravity.CENTER, 0, 0);

                                txtHeader = (TextView) popupView2.findViewById(R.id.txtHeader);
                                lstData = (ListView) popupView2.findViewById(R.id.lstData);

                                txtHeader.setText("Bank Account Number");
                                lstData.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, AccountNumberAL));

                                lstData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String strPT = lstData.getItemAtPosition(position).toString();
                                        txtAccount.setText(strPT);
                                        popupWindow2.dismiss();
                                        llBlackScreen1.setVisibility(View.GONE);
                                    }
                                });

                                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        llBlackScreen1.setVisibility(View.GONE);
                                    }
                                });

                            }
                        });

                        llWithdrawalRequest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String RupeeAmount = "0";

                                if (txtPaymentType.getText().toString().equals("Payment Type")) {
                                    Toast.makeText(getActivity(), "Select Payment Type", Toast.LENGTH_SHORT).show();
                                } else if (txtPaymentMethod.getText().toString().equals("Payment Method")) {
                                    Toast.makeText(getActivity(), "Select Payment Method", Toast.LENGTH_SHORT).show();
                                } else if (txtAccount.getText().toString().equals("Account")) {
                                    Toast.makeText(getActivity(), "Select Account Number", Toast.LENGTH_SHORT).show();
                                } else {

                                    if (txtPaymentType.getText().toString().equals("Single")) {
                                        if (etRupees.getText().toString().isEmpty() || etRupees.getText().toString().equals(null) ||
                                                etRupees.getText().toString().equals("")) {
                                            RupeeAmount = WithdrawableAmount;
                                        } else {
                                            RupeeAmount = etRupees.getText().toString();
                                        }
                                    } else if (txtPaymentType.getText().toString().equals("All Earnings")) {
                                        RupeeAmount = WithdrawableAmount;
                                    }

                                    String PayType, PayMethod, AccNum, Amt;
                                    PayType = txtPaymentType.getText().toString();
                                    PayMethod = txtPaymentMethod.getText().toString();
                                    AccNum = txtAccount.getText().toString();
                                    Amt = RupeeAmount;

                                    new WithdrwalRequestJSON().execute(PayType, Amt, PayMethod, AccNum);
                                    Toast.makeText(getActivity(), RupeeAmount, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                llBlackScreen.setVisibility(View.GONE);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new AlertDialog.Builder(getActivity()).setTitle("Alert")
                            .setMessage("\nCan not proceed.\nYou have pending withdrawls.\n").setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            }).show();
                }
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
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_WITHDRAWALS);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                // String imgResJSON =
                // EntityUtils.toString(response.getEntity());

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {

                    NewMerchantWithdrawalsPendingAL.clear();
                    NewMerchantWithdrawalsHistoryAL.clear();
                    AccountNumberAL.clear();
                    PaymentTypeAL.clear();
                    PaymentMethodAL.clear();


                    JSONArray arrPending = new JSONArray();
                    JSONArray arrHistory = new JSONArray();
                    JSONArray arrAccountNumber = new JSONArray();
                    JSONArray arrPaymentType = new JSONArray();
                    JSONArray arrPaymentMethod = new JSONArray();

                    arrPending = obj.getJSONArray("pending");
                    arrHistory = obj.getJSONArray("history");
                    arrAccountNumber = obj.getJSONArray("bank_details");
                    arrPaymentType = obj.getJSONArray("payment_type");
                    arrPaymentMethod = obj.getJSONArray("payment_method");

                    for (int i = 0; i < arrPending.length(); i++) {

                        PendingCount = Integer.toString(arrPending.length());

                        JSONObject objPending = new JSONObject();
                        objPending = arrPending.getJSONObject(i);

                        NewMerchantWithdrawalsItemItem = new NewMerchantWithdrawalsItem();

                        NewMerchantWithdrawalsItemItem.setWithdrawalId(objPending.getString("id"));
                        NewMerchantWithdrawalsItemItem.setAmount(objPending.getString("amount"));
                        NewMerchantWithdrawalsItemItem.setPaymentType(objPending.getString("payment_type"));
                        NewMerchantWithdrawalsItemItem.setPaymentMethod(objPending.getString("payment_method"));
                        NewMerchantWithdrawalsItemItem.setPaymentStatus(objPending.getString("status"));
                        NewMerchantWithdrawalsItemItem.setBankAccNum(objPending.getString("bank_account_number"));
                        NewMerchantWithdrawalsItemItem.setDateToProcess(objPending.getString("date_to_process"));
                        NewMerchantWithdrawalsItemItem.setDateTime(objPending.getString("created_at"));

                        NewMerchantWithdrawalsPendingAL.add(NewMerchantWithdrawalsItemItem);

                    }

                    for (int i = 0; i < arrHistory.length(); i++) {

                        HistoryCount = Integer.toString(arrHistory.length());

                        JSONObject objHistory = new JSONObject();
                        objHistory = arrHistory.getJSONObject(i);

                        NewMerchantWithdrawalsItemItem = new NewMerchantWithdrawalsItem();

                        NewMerchantWithdrawalsItemItem.setWithdrawalId(objHistory.getString("id"));
                        NewMerchantWithdrawalsItemItem.setAmount(objHistory.getString("amount"));
                        NewMerchantWithdrawalsItemItem.setPaymentType(objHistory.getString("payment_type"));
                        NewMerchantWithdrawalsItemItem.setPaymentMethod(objHistory.getString("payment_method"));
                        NewMerchantWithdrawalsItemItem.setPaymentStatus(objHistory.getString("status"));
                        NewMerchantWithdrawalsItemItem.setBankAccNum(objHistory.getString("bank_account_number"));
                        NewMerchantWithdrawalsItemItem.setDateToProcess(objHistory.getString("date_to_process"));
                        NewMerchantWithdrawalsItemItem.setDateTime(objHistory.getString("created_at"));

                        NewMerchantWithdrawalsHistoryAL.add(NewMerchantWithdrawalsItemItem);
                    }

                    for (int i = 0; i < arrAccountNumber.length(); i++) {
                        JSONObject objAccNum = new JSONObject();
                        objAccNum = arrAccountNumber.getJSONObject(i);
                        AccountNumberAL.add(objAccNum.getString("bank_account_number"));
                    }

                    for (int i = 0; i < arrPaymentType.length(); i++) {
                        JSONObject objPaymentType = new JSONObject();
                        objPaymentType = arrPaymentType.getJSONObject(i);
                        PaymentTypeAL.add(objPaymentType.getString("payment_type_name"));
                    }

                    for (int i = 0; i < arrPaymentMethod.length(); i++) {
                        JSONObject objPaymentMethod = new JSONObject();
                        objPaymentMethod = arrPaymentMethod.getJSONObject(i);
                        PaymentMethodAL.add(objPaymentMethod.getString("payment_method_name"));
                    }

                    WithdrawableAmount = obj.getString("withdrawable_amount");

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
                horizontal_pager.removeAllViews();
                txtPrice.setText("₹ " + WithdrawableAmount);
                txtPendingCount.setText(Integer.toString(NewMerchantWithdrawalsPendingAL.size()));
                txtHistoryCount.setText(Integer.toString(NewMerchantWithdrawalsHistoryAL.size()));

                for (int i = 0; i < 2; i++) {
                    ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    if (i == 0) {

                        lstPending.setLayoutParams(lparams);

                        if (NewMerchantWithdrawalsPendingAL.size() > 0) {
                            adapter = new MerchantWithdrawalsAdapter(getActivity(), NewMerchantWithdrawalsPendingAL);
                            lstPending.setAdapter(adapter);
                            horizontal_pager.addView(lstPending);
                        } else {
                            ImageView img = new ImageView(getActivity());
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontal_pager.addView(img);
                        }

                    } else if (i == 1) {

                        lstHistory.setLayoutParams(lparams);

                        if (NewMerchantWithdrawalsHistoryAL.size() > 0) {
                            adapter = new MerchantWithdrawalsAdapter(getActivity(), NewMerchantWithdrawalsHistoryAL);
                            lstHistory.setAdapter(adapter);
                            horizontal_pager.addView(lstHistory);
                        } else {
                            ImageView img = new ImageView(getActivity());
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontal_pager.addView(img);
                        }
                    }
                }
            } else {
                Toast.makeText(getActivity(), "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class WithdrwalRequestJSON extends AsyncTask<String, Void, Void> {

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
        protected Void doInBackground(String... params) {

            String PayType, PayMethod, AccNum, Amt;
            PayType = params[0];
            Amt = params[1];
            PayMethod = params[2];
            AccNum = params[3];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("payment_type", PayType));
                nameValuePairs.add(new BasicNameValuePair("amount", Amt));
                nameValuePairs.add(new BasicNameValuePair("payment_method", PayMethod));
                nameValuePairs.add(new BasicNameValuePair("bank_account_number", AccNum));


                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_WITHDRAWALS_REQUEST);
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
                Toast.makeText(getActivity(), "Request Successfull !!!", Toast.LENGTH_SHORT).show();
                new NewMerchantWithdrawalJSON().execute();
                popupWindow.dismiss();
            } else {
                Toast.makeText(getActivity(), "Oops!!! Try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class CancelWithdrwalRequestJSON extends AsyncTask<String, Void, Void> {

        String status = "0";
        String WId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {


            WId = params[0];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("withdrawal_id", WId));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_WITHDRAWALS_REQUEST_CANCEL);
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
                Toast.makeText(getActivity(), "Request Cancelled !!!", Toast.LENGTH_SHORT).show();

                horizontal_pager.removeAllViews();
                new NewMerchantWithdrawalJSON().execute();
                popupWindow.dismiss();
            } else {
                Toast.makeText(getActivity(), "Oops!!! Try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
