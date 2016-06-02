package com.callndata.folivesservices;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.item.CustomerPinCodeItem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PinCodeActivity extends ActionBarActivity {

	ListView lstPinCode;
	ProgressDialog pDialog;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	ArrayList<String> pinCodeInfoOriginalAL = new ArrayList<String>();
	ArrayList<String> pinCodeInfoAL = new ArrayList<String>();

	boolean flag = true;
	CustomerPinCodeItem CPCItem;
	ArrayList<CustomerPinCodeItem> CustomerPinCodeItemAL = new ArrayList<CustomerPinCodeItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pincode);
		
		//ActionBar bar = getActionBar();
		//bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000ff")));

		lstPinCode = (ListView) findViewById(R.id.lstPinCode);
		new PinCodeJSON().execute();

		lstPinCode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (flag == true) {
					AppConstants.Customer_Pincode = pinCodeInfoOriginalAL.get(position);//.substring(0, 6);
					
				} else {
					AppConstants.Customer_Pincode = pinCodeInfoAL.get(position);//.substring(0, 6);
				}
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity_action, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			public boolean onQueryTextChange(String newText) {

				if (newText.length() == 0) {
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(PinCodeActivity.this,
							android.R.layout.simple_list_item_1, pinCodeInfoOriginalAL);
					lstPinCode.setAdapter(adapter);
					flag = true;
				} else {

					pinCodeInfoAL.clear();

					for (int i = 0; i < CustomerPinCodeItemAL.size(); i++) {

						if (isNumeric(newText)) {

							String pin = CustomerPinCodeItemAL.get(i).getPostCode();
							String subPin = pin.substring(0, newText.length());
							if (newText.equals(subPin)) {
								pinCodeInfoAL.add(CustomerPinCodeItemAL.get(i).getPostCode() + " - "
										+ CustomerPinCodeItemAL.get(i).getAreaName());
							}
						} else {
							String area = CustomerPinCodeItemAL.get(i).getAreaName();
							String subArea = area.substring(0, newText.length());
							newText = newText.toLowerCase();
							subArea = subArea.toLowerCase();
							if (newText.equals(subArea)) {
								pinCodeInfoAL.add(CustomerPinCodeItemAL.get(i).getPostCode() + " - "
										+ CustomerPinCodeItemAL.get(i).getAreaName());
							}
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(PinCodeActivity.this,
								android.R.layout.simple_list_item_1, pinCodeInfoAL);
						lstPinCode.setAdapter(adapter);
					}
					flag = false;
				}

				// Toast.makeText(PinCodeActivity.this, newText,
				// Toast.LENGTH_SHORT).show();
				return true;
			}

			public boolean onQueryTextSubmit(String query) {

				return false;
			}
		};
		searchView.setOnQueryTextListener(queryTextListener);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class PinCodeJSON extends AsyncTask<Void, Void, Void> {

		String status = "0";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(PinCodeActivity.this);
			pDialog.setMessage("Please Wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_PIN_CODE);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				String imgResJSON = reader.readLine();

				JSONObject obj = new JSONObject(imgResJSON);

				status = obj.getString("success");

				if (status.equals("true")) {
					JSONArray arr = new JSONArray();
					arr = obj.getJSONArray("post_codes");

					for (int i = 0; i < arr.length(); i++) {

						JSONObject objAC = new JSONObject();
						objAC = arr.getJSONObject(i);

						CPCItem = new CustomerPinCodeItem();
						CPCItem.setId(objAC.getString("id"));
						CPCItem.setAreaName(objAC.getString("area_name"));
						CPCItem.setPostCode(objAC.getString("post_code"));

						String data = objAC.getString("post_code") + " - " + objAC.getString("area_name");
						pinCodeInfoAL.add(data);
						pinCodeInfoOriginalAL.add(data);

						CustomerPinCodeItemAL.add(CPCItem);
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
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(PinCodeActivity.this,
						android.R.layout.simple_list_item_1, pinCodeInfoOriginalAL);
				lstPinCode.setAdapter(adapter);
			} else {
				Toast.makeText(PinCodeActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
