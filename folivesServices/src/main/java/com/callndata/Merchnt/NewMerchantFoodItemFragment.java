package com.callndata.Merchnt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.callndata.adapterM.MerchantFoodItemAdapter;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantFoodListItem;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ravikant on 17-03-2016.
 */
public class NewMerchantFoodItemFragment extends Fragment {

    ImageView img_neworder;
    ListView lstFoodItem;
    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    NewMerchantFoodListItem NewMerchantFoodListItemItem;
    ArrayList<NewMerchantFoodListItem> NewMerchantFoodListItemAL = new ArrayList<NewMerchantFoodListItem>();

    MerchantFoodItemAdapter MFAdapter;
    public static ArrayList<String> TabName = new ArrayList<String>();


    public static HashMap<String,String> hm_timeslot ;
    public View onCreateView(LayoutInflater inflater1, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater1.inflate(R.layout.fragment_new_merchant_food_item, container, false);

        lstFoodItem = (ListView) view.findViewById(R.id.lstFoodItem);
        img_neworder = (ImageView) view.findViewById(R.id.img_neworder);
        Date todayDate = new Date();
       new MerchantTimeSlotJSON().execute(new SimpleDateFormat("dd-MM-yy").format(todayDate));
        img_neworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NewMerchantFoodItemAddActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }



    class MerchantTimeSlotJSON extends AsyncTask<String, Void, Void> {


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

            String strDate = params[0];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                if (!strDate.equals("null")) {
                    nameValuePairs.add(new BasicNameValuePair("date", strDate));
                }

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_DAILY_FOODMENU);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                Log.w("response", response.toString());
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);
                status = obj.getString("success");

                if (status.equals("true")) {

                    JSONArray arrTimeSlot = new JSONArray();
                    arrTimeSlot = obj.getJSONArray("time_slot_names");

                    for (int i = 0; i < arrTimeSlot.length(); i++) {
                        JSONObject TabObj = new JSONObject();
                        TabObj = arrTimeSlot.getJSONObject(i);

                        TabName.add(TabObj.getString("time_slot_name"));
                        Log.w("TabName", obj.toString());

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


         /*       txtMidnight.setText(TabName.get(0).substring(0, 1).toUpperCase() + TabName.get(0).substring(1));
                txtBreakfast.setText(TabName.get(1).substring(0, 1).toUpperCase() + TabName.get(1).substring(1));
                txtLunch.setText(TabName.get(2).substring(0, 1).toUpperCase() + TabName.get(2).substring(1));
                txtDinner.setText(TabName.get(3).substring(0, 1).toUpperCase() + TabName.get(3).substring(1));
*/

                hm_timeslot = new HashMap<String,String>();
                hm_timeslot.put("t1" ,TabName.get(0).substring(0, 1).toUpperCase() + TabName.get(0).substring(1) );
                hm_timeslot.put("t2" ,TabName.get(1).substring(0, 1).toUpperCase() + TabName.get(1).substring(1) );
                hm_timeslot.put("t3" ,TabName.get(2).substring(0, 1).toUpperCase() + TabName.get(2).substring(1) );
                hm_timeslot.put("t4" ,TabName.get(3).substring(0, 1).toUpperCase() + TabName.get(3).substring(1) );
                new MerchantFoodItemJSON().execute();


            }
        }
    }

    public class MerchantFoodItemJSON extends AsyncTask<Void, Integer, Void> {

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
        protected Void doInBackground(Void... params) {
            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.NEW_MERCHANT_FOOD_ITEMS);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                status = objMain.getString("success");

                if (status.equals("true")) {

                    NewMerchantFoodListItemAL.clear();

                    JSONArray arrFoodItems = new JSONArray();
                    arrFoodItems = objMain.getJSONArray("food_item");

                    if (arrFoodItems.length() != 0) {
                        for (int i = 0; i < arrFoodItems.length(); i++) {
                            JSONObject objFoodItems = new JSONObject();
                            objFoodItems = arrFoodItems.getJSONObject(i);

                            NewMerchantFoodListItemItem = new NewMerchantFoodListItem();

                            NewMerchantFoodListItemItem.setFoodId(objFoodItems.getString("id"));
                            NewMerchantFoodListItemItem.setFoodName(objFoodItems.getString("item_name"));
                            NewMerchantFoodListItemItem.setFoodDesc(objFoodItems.getString("item_description"));
                            NewMerchantFoodListItemItem.setIsActive(objFoodItems.getString("active"));
                            NewMerchantFoodListItemItem.setCategory(objFoodItems.getString("category"));
                            NewMerchantFoodListItemItem.setPrice(objFoodItems.getString("price"));
                            NewMerchantFoodListItemItem.setPhoto(AppConstants.Customer_Dish_Image_path +objFoodItems.getString("photo"));
                            NewMerchantFoodListItemItem.setCity(objFoodItems.getString("city"));

                            JSONObject objCusine = new JSONObject();
                            objCusine = objFoodItems.getJSONObject("cuisine");
                            NewMerchantFoodListItemItem.setCusine(objCusine.getString("cuisine_name"));

                            JSONObject objItemType = new JSONObject();
                            objItemType = objFoodItems.getJSONObject("item_type");
                            if(objItemType!=null)
                            NewMerchantFoodListItemItem.setItemType(objItemType.getString("type_name"));
                            NewMerchantFoodListItemItem.setItemTypeValue(objItemType.getString("type_value"));
                            Log.w("type_value = " , objItemType.getString("type_value"));


                            JSONObject objDietType = new JSONObject();
                            objDietType = objFoodItems.getJSONObject("diet_type");
                            if(objDietType!=null)
                            NewMerchantFoodListItemItem.setDietType(objDietType.getString("diet_type_name"));
                            NewMerchantFoodListItemItem.setDietTypeValue(objDietType.getString("diet_type_value"));

                            Log.w("diet_type_value = ", objDietType.getString("diet_type_value"));

                            NewMerchantFoodListItemAL.add(NewMerchantFoodListItemItem);
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

                MFAdapter = new MerchantFoodItemAdapter(getActivity(), NewMerchantFoodListItemAL);
                lstFoodItem.setAdapter(MFAdapter);
            } else {

            }
        }
    }


}
