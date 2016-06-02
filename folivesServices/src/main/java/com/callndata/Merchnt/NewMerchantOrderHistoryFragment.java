package com.callndata.Merchnt;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapterM.MerchantFoodItemDetailedView;
import com.callndata.adapterM.MerchantOrderHistoryAdapter;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.MItem.MerchantFoodItemDetailedViewItem;
import com.folives.MItem.NewMerchantOrderHistoryItem;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ravikant on 16-03-2016.
 */
public class NewMerchantOrderHistoryFragment extends Fragment {

    TextView txtOrderHistoryCount;
    ListView lstOrderHistory;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    NewMerchantOrderHistoryItem NewMerchantOrderHistoryItemItem;
    ArrayList<NewMerchantOrderHistoryItem> NewMerchantOrderHistoryItemAL = new ArrayList<NewMerchantOrderHistoryItem>();

    MerchantOrderHistoryAdapter MOHAdapter;

    ListView lstFoodItems;
    ImageView imgPic, imgAccept, imgDecline;
    TextView txtTotalAmount, txtTimeSlot, txtDeliveryTime, txtDeliveryDate, txtCustName, txtEmail, txtNumber, txtAddress,
            txtLandmark, txtPostcode;

    String DTotalAmt, DTimeSlot, DDeliveryTime, DDeliveryDate, DCustPic, DCustName, DCustEmail, DCustNum, DCustAdd, DLandmark, DZipCode;
    MerchantFoodItemDetailedViewItem MerchantFoodItemDetailedViewItemItem;
    ArrayList<MerchantFoodItemDetailedViewItem> MerchantFoodItemDetailedViewItemAL = new ArrayList<MerchantFoodItemDetailedViewItem>();

    MerchantFoodItemDetailedView MFDAdapter;

    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.fragment_new_merchant_order_history, container, false);

        txtOrderHistoryCount = (TextView) view.findViewById(R.id.txtOrderHistoryCount);
        lstOrderHistory = (ListView) view.findViewById(R.id.lstOrderHistory);

        lstOrderHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                            .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.popup_new_merchant_neworder_detailedview, null);
                    final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT, true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    // popupWindow.setWidth(700);
                    popupWindow.setAnimationStyle(R.style.DialogAnimation);

                    lstFoodItems = (ListView) popupView.findViewById(R.id.lstFoodItems);
                    imgPic = (ImageView) popupView.findViewById(R.id.imgPic);
                    imgAccept = (ImageView) popupView.findViewById(R.id.imgAccept);
                    imgDecline = (ImageView) popupView.findViewById(R.id.imgDecline);
                    txtTotalAmount = (TextView) popupView.findViewById(R.id.txtTotalAmount);
                    txtTimeSlot = (TextView) popupView.findViewById(R.id.txtTimeSlot);
                    txtDeliveryTime = (TextView) popupView.findViewById(R.id.txtDeliveryTime);
                    txtDeliveryDate = (TextView) popupView.findViewById(R.id.txtDeliveryDate);
                    txtCustName = (TextView) popupView.findViewById(R.id.txtCustName);
                    txtEmail = (TextView) popupView.findViewById(R.id.txtEmail);
                    txtNumber = (TextView) popupView.findViewById(R.id.txtNumber);
                    txtAddress = (TextView) popupView.findViewById(R.id.txtAddress);
                    txtLandmark = (TextView) popupView.findViewById(R.id.txtLandmark);
                    txtPostcode = (TextView) popupView.findViewById(R.id.txtPostcode);

                    new FoodDetailedViewJSON().execute(NewMerchantOrderHistoryItemAL.get(position).getOrderId());
                    popupWindow.showAtLocation(txtTimeSlot, Gravity.CENTER, 0, 0);

                    imgAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "Accept Clicked!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    imgDecline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "Declined Clicked!!!", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error : " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });


        new OrderHistoryJSON().execute();
        return view;
    }

    public class OrderHistoryJSON extends AsyncTask<Void, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status;

        String TotalOrderHistory;

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
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.NEW_MERCHANT_ORDER_HISTORY);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                status = objMain.getString("success");

                if (status.equals("true")) {

                    JSONArray arrOrderHistory = new JSONArray();
                    arrOrderHistory = objMain.getJSONArray("order_history");

                    if (arrOrderHistory.length() != 0) {

                        TotalOrderHistory = Integer.toString(arrOrderHistory.length());

                        for (int i = 0; i < arrOrderHistory.length(); i++) {
                            JSONObject objOrderHistory = new JSONObject();
                            objOrderHistory = arrOrderHistory.getJSONObject(i);

                            NewMerchantOrderHistoryItemItem = new NewMerchantOrderHistoryItem();

                            NewMerchantOrderHistoryItemItem.setOrderId(objOrderHistory.getString("id"));
                            NewMerchantOrderHistoryItemItem.setPaymentType(objOrderHistory.getString("payment_type"));
                            NewMerchantOrderHistoryItemItem.setTotalAmt(objOrderHistory.getString("total_w_tax"));
                            NewMerchantOrderHistoryItemItem.setDeliveryStatus(objOrderHistory.getString("status"));
                            NewMerchantOrderHistoryItemItem.setDeliveryStatusId(objOrderHistory.getString("stats_id"));
                            NewMerchantOrderHistoryItemItem.setDeliveryTime(objOrderHistory.getString("delivery_time"));
                            NewMerchantOrderHistoryItemItem.setTimeSlot(objOrderHistory.getString("time_slot"));
                            NewMerchantOrderHistoryItemItem.setDateTime(objOrderHistory.getString("created_at"));


                            JSONObject objUser = new JSONObject();
                            objUser = objOrderHistory.getJSONObject("user");

                            NewMerchantOrderHistoryItemItem.setName(objUser.getString("first_name") + " " + objUser.getString("last_name"));
                            NewMerchantOrderHistoryItemItem.setEmail(objUser.getString("email"));
                            NewMerchantOrderHistoryItemItem.setMobile(objUser.getString("mobile"));

                            String FbId = objUser.getString("fb_id");
                            if (FbId.equals("0")) {
                                NewMerchantOrderHistoryItemItem.setPic(AppConstants.Customer_Profile_Image_Path + objUser.getString("profile_photo"));
                            } else {
                                NewMerchantOrderHistoryItemItem.setPic(objUser.getString("profile_photo"));
                            }


                            JSONObject objTimeSlot = new JSONObject();
                            objTimeSlot = objOrderHistory.getJSONObject("time_slots");

                            NewMerchantOrderHistoryItemItem.setTimeSlotName(objTimeSlot.getString("time_slot_name"));
                            NewMerchantOrderHistoryItemItem.setTimeSlotValue(objTimeSlot.getString("time_slot_value"));


                            NewMerchantOrderHistoryItemAL.add(NewMerchantOrderHistoryItemItem);
                        }
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
                txtOrderHistoryCount.setText(TotalOrderHistory);
                MOHAdapter = new MerchantOrderHistoryAdapter(getActivity(), NewMerchantOrderHistoryItemAL);
                lstOrderHistory.setAdapter(MOHAdapter);
            } else {
                Toast.makeText(getActivity(), "Oops! Try again later...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class FoodDetailedViewJSON extends AsyncTask<String, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String Id = params[0];

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("id", Id));

                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.NEW_MERCHANT_FOOD_NEW_ORDER_DETAIL);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                status = objMain.getString("success");

                if (status.equals("true")) {

                    JSONArray arrFoodDetailed = new JSONArray();
                    arrFoodDetailed = objMain.getJSONArray("new_order_details");

                    if (arrFoodDetailed.length() != 0) {
                        for (int i = 0; i < arrFoodDetailed.length(); i++) {
                            JSONObject objFoodDetailed = new JSONObject();
                            objFoodDetailed = arrFoodDetailed.getJSONObject(i);

                            JSONArray arrFood = new JSONArray();
                            arrFood = objFoodDetailed.getJSONArray("order_details");

                            if (arrFood.length() > 0) {
                                for (int j = 0; j < arrFood.length(); j++) {

                                    JSONObject objFood = new JSONObject();
                                    objFood = arrFood.getJSONObject(j);

                                    MerchantFoodItemDetailedViewItemItem = new MerchantFoodItemDetailedViewItem();
                                    MerchantFoodItemDetailedViewItemItem.setFoodName(objFood.getString("item_name"));
                                    MerchantFoodItemDetailedViewItemItem.setFoodPrice(objFood.getString("price"));
                                    MerchantFoodItemDetailedViewItemItem.setFoodQuantity(objFood.getString("qty"));

                                    JSONObject objPic = new JSONObject();
                                    objPic = objFood.getJSONObject("item_photo");

                                    MerchantFoodItemDetailedViewItemItem.setFoodPic(AppConstants.Merchant_Food_Item_path + objPic.getString("photo"));
                                    MerchantFoodItemDetailedViewItemAL.add(MerchantFoodItemDetailedViewItemItem);
                                }
                            }

                            DTotalAmt = objFoodDetailed.getString("total_w_tax");
                            DDeliveryTime = objFoodDetailed.getString("delivery_time");
                            DDeliveryDate = objFoodDetailed.getString("delivery_date");

                            JSONObject objCust = new JSONObject();
                            objCust = objFoodDetailed.getJSONObject("user");

                            DCustName = objCust.getString("first_name") + " " + objCust.getString("last_name");
                            DCustEmail = objCust.getString("email");
                            DCustNum = objCust.getString("mobile");

                            String FbId = objCust.getString("profile_photo");

                            if (FbId.equals("0")) {
                                DCustPic = AppConstants.Customer_Profile_Pic + objCust.getString("profile_photo");
                            } else {
                                DCustPic = objCust.getString("profile_photo");
                            }

                            JSONArray arrAdd = new JSONArray();
                            arrAdd = objFoodDetailed.getJSONArray("address");
                            JSONObject objAdd = new JSONObject();
                            objAdd = arrAdd.getJSONObject(0);

                            DCustAdd = objAdd.getString("address");
                            DLandmark = objAdd.getString("landmark");
                            DZipCode = objAdd.getString("zipcode");

                            JSONObject objTimeSlot = new JSONObject();
                            objTimeSlot = objFoodDetailed.getJSONObject("time_slots");

                            DTimeSlot = objTimeSlot.getString("time_slot_value").toUpperCase();

                        }
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

                try {
                    Picasso.with(getActivity()).load(DCustPic).fit().centerCrop().into(imgPic);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MFDAdapter = new MerchantFoodItemDetailedView(getActivity(), MerchantFoodItemDetailedViewItemAL);
                lstFoodItems.setAdapter(MFDAdapter);

                txtTotalAmount.setText(DTotalAmt);
                txtTimeSlot.setText(DTimeSlot);
                txtDeliveryTime.setText(DDeliveryTime);
                txtDeliveryDate.setText(DDeliveryDate);
                txtCustName.setText(DCustName);
                txtEmail.setText(DCustEmail);
                txtNumber.setText(DCustNum);
                txtAddress.setText(DCustAdd);
                txtLandmark.setText(DLandmark);
                txtPostcode.setText(DZipCode);

            } else {

            }
        }
    }

}
