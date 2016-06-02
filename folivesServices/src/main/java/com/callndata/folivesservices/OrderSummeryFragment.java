package com.callndata.folivesservices;

import java.util.ArrayList;

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

import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.item.NewOrderDetailedViewItem;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class OrderSummeryFragment extends Fragment {

	String id;
	ProgressDialog pDialog;
	TableLayout tlOrderSummery;
	TextView item, price, quantity, totals;
	ArrayList<NameValuePair> nameValuePairs;
	NewOrderDetailedViewItem newOrderDetailedViewItem;
	ArrayList<NewOrderDetailedViewItem> NewOrderDetailedViewItemAL = new ArrayList<NewOrderDetailedViewItem>();

	TextView txtUserName, txtEmail, txtUserAddress, txtDeliveryCharge, txtGrandTotal, txtTimeSlot, txtSubTotal,
			txtOrderDate, txtDeliveryTime, txtUserMob, txtPaymentMethod;
	String userName, userEmail, userAddr, userMob, deliveryCharges, deliveryTime, timeSlot, orderDate, paymentType;
	Button btnAccept, btnDecline, btnClose;

	String OrderUpdateStatus;
	ArrayList<String> ItemPrice = new ArrayList<String>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_order_summery, container, false);

		try {
			Bundle bundle = this.getArguments();
			if (bundle != null) {
				id = bundle.getString("id", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		tlOrderSummery = (TableLayout) view.findViewById(R.id.tlOrderSummery);
		txtUserAddress = (TextView) view.findViewById(R.id.txtUserAddress);
		txtUserName = (TextView) view.findViewById(R.id.txtUserName);
		txtEmail = (TextView) view.findViewById(R.id.txtEmail);
		txtDeliveryCharge = (TextView) view.findViewById(R.id.txtDeliveryCharge);
		txtGrandTotal = (TextView) view.findViewById(R.id.txtGrandTotal);
		txtTimeSlot = (TextView) view.findViewById(R.id.txtTimeSlot);
		txtSubTotal = (TextView) view.findViewById(R.id.txtSubTotal);
		txtOrderDate = (TextView) view.findViewById(R.id.txtOrderDate);
		txtDeliveryTime = (TextView) view.findViewById(R.id.txtDeliveryTime);
		txtUserMob = (TextView) view.findViewById(R.id.txtUserMob);
		txtPaymentMethod = (TextView) view.findViewById(R.id.txtPaymentMethod);
		btnAccept = (Button) view.findViewById(R.id.btnAccept);
		btnDecline = (Button) view.findViewById(R.id.btnDecline);
		btnClose = (Button) view.findViewById(R.id.btnClose);

		btnAccept.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderUpdateStatus = "1";
				new OrderAcceptRejectJSON().execute();
			}
		});
		btnDecline.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderUpdateStatus = "0";
				new OrderAcceptRejectJSON().execute();
			}
		});

		btnClose.setVisibility(View.INVISIBLE);
		new NewOrderListJSON().execute();

		return view;
	}

	class NewOrderListJSON extends AsyncTask<Void, Void, Void> {

		String NewOrderStatus = "0";

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
				nameValuePairs.add(new BasicNameValuePair("id", id));
				nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(AppConstants.MERCHANT_NEW_ORDER_DETAILS);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);

				// int responseCode = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				String responseBody = EntityUtils.toString(entity);

				JSONObject Mobj = new JSONObject(responseBody);
				NewOrderStatus = Mobj.getString("success");
				if (NewOrderStatus.equals("true")) {
					JSONArray arr = new JSONArray();
					arr = Mobj.getJSONArray("new_order_details");
					JSONObject obj = new JSONObject();
					for (int i = 0; i < arr.length(); i++) {
						obj = arr.getJSONObject(i);
						JSONArray orderArr = new JSONArray();

						deliveryCharges = obj.getString("delivery_charge");
						deliveryTime = obj.getString("delivery_time");
						timeSlot = obj.getString("time_slot");
						orderDate = obj.getString("created_at");
						paymentType = obj.getString("payment_type");

						orderArr = obj.getJSONArray("order_details");
						JSONObject orderObject = new JSONObject();
						for (int j = 0; j < orderArr.length(); j++) {
							orderObject = orderArr.getJSONObject(j);
							newOrderDetailedViewItem = new NewOrderDetailedViewItem();

							newOrderDetailedViewItem.setId(orderObject.getString("id"));
							newOrderDetailedViewItem.setOrder_id(orderObject.getString("order_id"));
							newOrderDetailedViewItem.setClient_id(orderObject.getString("client_id"));
							newOrderDetailedViewItem.setItem_id(orderObject.getString("item_id"));
							newOrderDetailedViewItem.setItem_name(orderObject.getString("item_name"));
							newOrderDetailedViewItem.setItem_price(orderObject.getString("normal_price"));
							newOrderDetailedViewItem.setDiscounted_price(orderObject.getString("discounted_price"));
							newOrderDetailedViewItem.setQty(orderObject.getString("qty"));

							NewOrderDetailedViewItemAL.add(newOrderDetailedViewItem);
						}

						JSONObject userObj = new JSONObject();
						userObj = obj.getJSONObject("user");

						userName = userObj.getString("first_name") + " " + userObj.getString("last_name");
						userEmail = userObj.getString("email");
						userMob = userObj.getString("mobile");

						JSONArray userArr = new JSONArray();
						userArr = obj.getJSONArray("address");
						JSONObject userAddrObj = new JSONObject();
						userAddrObj = userArr.getJSONObject(0);

						userAddr = userAddrObj.getString("address");
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

			if (NewOrderStatus.equals("true")) {

				txtUserAddress.setText(userAddr);
				txtUserName.setText(userName);
				txtEmail.setText(userEmail);
				txtDeliveryCharge.setText(deliveryCharges);
				txtTimeSlot.setText(timeSlot);
				txtOrderDate.setText(orderDate);
				txtDeliveryTime.setText(deliveryTime);
				txtUserMob.setText(userMob);
				txtPaymentMethod.setText(paymentType);

				for (int i = 0; i < NewOrderDetailedViewItemAL.size(); i++) {

					TableRow row = new TableRow(getActivity());
					TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
					row.setLayoutParams(lp);

					item = new TextView(getActivity());
					price = new TextView(getActivity());
					quantity = new TextView(getActivity());
					totals = new TextView(getActivity());

					String itemName, itemPrice, itemQty, itemTotal;

					itemName = "  " + NewOrderDetailedViewItemAL.get(i).getItem_name();
					itemPrice = "       " + NewOrderDetailedViewItemAL.get(i).getItem_price();
					itemQty = "                 " + NewOrderDetailedViewItemAL.get(i).getQty();
					itemTotal = "                   "
							+ Float.toString(Float.parseFloat(itemPrice) * Float.parseFloat(itemQty));

					item.setText(itemName);
					price.setText(itemPrice);
					quantity.setText(itemQty);
					totals.setText(itemTotal);

					row.addView(item);
					row.addView(price);
					row.addView(quantity);
					row.addView(totals);
					tlOrderSummery.addView(row, i);

					ItemPrice.add(itemTotal);
				}

				float subTotal = 0;
				for (int i = 0; i < ItemPrice.size(); i++) {
					subTotal = subTotal + Float.parseFloat(ItemPrice.get(i));
				}

				txtSubTotal.setText(Float.toString(subTotal));

				String GrandT = Float.toString(subTotal + Float.parseFloat(deliveryCharges));
				txtGrandTotal.setText(GrandT);

			} else if (NewOrderStatus.equals("false")) {
				Toast.makeText(getActivity(), "Invalid Token", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Server Error...", Toast.LENGTH_SHORT).show();
			}

		}
	}

	class OrderAcceptRejectJSON extends AsyncTask<Void, Void, Void> {

		String OrderStatus = "0";

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
				nameValuePairs.add(new BasicNameValuePair("id", id));
				nameValuePairs.add(new BasicNameValuePair("status", OrderUpdateStatus));
				nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(AppConstants.MERCHANT_ORDER_ACCEPT_REJECT_JSON);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);

				// int responseCode = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				String responseBody = EntityUtils.toString(entity);

				JSONObject Mobj = new JSONObject(responseBody);
				OrderStatus = Mobj.getString("success");

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

			if (OrderStatus.equals("true")) {
				Toast.makeText(getActivity(), "Order Processed.", Toast.LENGTH_SHORT).show();
				Fragment myfragment;
			//	myfragment = new MerchantNewOrderFragment();

				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
			//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
				fragmentTransaction.commit();
			} else if (OrderStatus.equals("false")) {
				Toast.makeText(getActivity(), "Invalid Token", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Server Error...", Toast.LENGTH_SHORT).show();
			}

		}
	}
	
	

	@Override
	public void onResume() {
		super.onResume();

		getView().setFocusableInTouchMode(true);
		getView().requestFocus();
		getView().setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

					Fragment myfragment;
				//	myfragment = new MerchantNewOrderFragment();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
				//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
					fragmentTransaction.commit();

					return true;
				}
				return false;
			}
		});
	}
	

}
