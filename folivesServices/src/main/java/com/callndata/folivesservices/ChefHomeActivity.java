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

import com.callndata.adapters.MerchantMenuAdapter;
import com.callndata.others.AppConstants;
import com.callndata.others.RoundedImageView;
import com.callndata.others.UICircularImage;
import com.example.folivesservices.R;
import com.squareup.picasso.Picasso;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChefHomeActivity extends FragmentActivity {

	ListView left_drawer;
	DrawerLayout dLayout;
	SharedPreferences pref;
	LinearLayout ll_left_drawer;
	ImageView imgIcon;
	RoundedImageView roundedImage;
	MerchantMenuAdapter menu_adapter;
	ArrayList<Integer> imgIconDrawer = new ArrayList<Integer>();
	ArrayList<String> imgNameDrawer = new ArrayList<String>();

	ProgressDialog pDialog;
	LinearLayout llMenu;
	ArrayList<NameValuePair> nameValuePairs;
	String MName, MRName, MProfilePic, MRole;
	TextView txtMName, txtMRName, txtRole;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chef_home);

		pref = getApplicationContext().getSharedPreferences("MyPref", 0);

		imgIconDrawer.add(R.drawable.dashboard);
		imgIconDrawer.add(R.drawable.info);
		imgIconDrawer.add(R.drawable.setting);
		imgIconDrawer.add(R.drawable.fooditem);
		imgIconDrawer.add(R.drawable.customerreviews);
		imgIconDrawer.add(R.drawable.customerreviews);
		imgIconDrawer.add(R.drawable.setting); // New Orders
		imgIconDrawer.add(R.drawable.setting); // Order History
		imgIconDrawer.add(R.drawable.setting); // logout

		imgNameDrawer.add("DASHBOARD");
		imgNameDrawer.add("CHEF INFO");
		imgNameDrawer.add("SETTINGS");
		imgNameDrawer.add("FOOD ITEMS");
		imgNameDrawer.add("REVIEWS");
		imgNameDrawer.add("COUPON");
		imgNameDrawer.add("NEW ORDER");
		imgNameDrawer.add("ORDER HISTORY");
		imgNameDrawer.add("LOGOUT");

		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		left_drawer = (ListView) findViewById(R.id.left_drawer);
		imgIcon = (ImageView) findViewById(R.id.imgIcon);
		ll_left_drawer = (LinearLayout) findViewById(R.id.ll_left_drawer);

		txtMName = (TextView) findViewById(R.id.txtMName);
		txtMRName = (TextView) findViewById(R.id.txtMRName);
		txtRole = (TextView) findViewById(R.id.txtRole);

		Fragment myfragment;
	//	myfragment = new MerchantDashboardFragment();

		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
	//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
		fragmentTransaction.commit();

		menu_adapter = new MerchantMenuAdapter(ChefHomeActivity.this, imgIconDrawer, imgNameDrawer);
		left_drawer.setAdapter(menu_adapter);

		new GETMerchantJSON().execute();

		llMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dLayout.openDrawer(ll_left_drawer);
			}
		});

		left_drawer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					Fragment myfragment;
				//	myfragment = new MerchantDashboardFragment();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
				//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
					fragmentTransaction.commit();

				} else if (position == 1) {
					Fragment myfragment;
				//	myfragment = new MerchantInfoFragment();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
				//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
					fragmentTransaction.commit();
				} else if (position == 2) {
					Fragment myfragment;
				//	myfragment = new MerchantSettingsFragment();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
				//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
					fragmentTransaction.commit();

				} else if (position == 3) {
					Fragment myfragment;
					//myfragment = new MerchantFoodItemFragment();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
				//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
					fragmentTransaction.commit();
				} else if (position == 4) {
					Fragment myfragment;
				//	myfragment = new MerchantCustomerReviewsFragment();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
				//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
					fragmentTransaction.commit();
				} else if (position == 5) {
					Fragment myfragment;
				//	myfragment = new MerchantCouponsFragment();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
				//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
					fragmentTransaction.commit();
				} else if (position == 6) {
					Fragment myfragment;
				//	myfragment = new MerchantNewOrderFragment();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
				//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
					fragmentTransaction.commit();

				} else if (position == 7) {
					Fragment myfragment;
				//	myfragment = new MerchantOrderHistoryFragment();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
				//	fragmentTransaction.replace(R.id.fragment_switch, myfragment);
					fragmentTransaction.commit();

				} else if (position == 8) {
					Editor editor = pref.edit();
					editor.putString("MLoginFlag", null);
					editor.putString("Token", null);
					editor.commit();

					Intent intent = new Intent(ChefHomeActivity.this, CustomerLoginActivity.class);
					startActivity(intent);
					finish();
				}

				dLayout.closeDrawers();

			}
		});

		ChefHomeActivity.this.registerReceiver(mBroadcastReceiver, new IntentFilter("start.fragment.action"));

	}

	public class GETMerchantJSON extends AsyncTask<Void, Integer, Void> {

		String responseBody;
		HttpPost httppost;
		String status;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChefHomeActivity.this);
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
				httppost = new HttpPost(AppConstants.MERCHANT_PROFILE_PIC_INFO);

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);

				JSONObject objMain = new JSONObject(responseBody);
				status = objMain.getString("success");

				if (status.equals("true")) {
					JSONArray arr = objMain.getJSONArray("merchant_user");
					JSONObject obj = arr.getJSONObject(0);

					JSONObject MInfoObj = new JSONObject();
					MInfoObj = obj.getJSONObject("merchant");

					MRName = MInfoObj.getString("name");
					MName = obj.getString("first_name") + " " + obj.getString("last_name");
					MRole = obj.getString("role");
					MProfilePic = AppConstants.Merchant_Profile_Pic + obj.getString("profile_photo");
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

				Picasso.with(ChefHomeActivity.this).load(MProfilePic).transform(new UICircularImage()).into(imgIcon);

				txtMName.setText(MName);
				txtMRName.setText(MRName);
				txtRole.setText(MRole);

			} else {
				Toast.makeText(ChefHomeActivity.this, "Oops! Try again later...", Toast.LENGTH_SHORT).show();
			}
		}
	}

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
//			Fragment myfragment;
//			myfragment = new CustomerChefItemsAndReviews();
//
//			FragmentManager fm = getFragmentManager();
//			FragmentTransaction fragmentTransaction = fm.beginTransaction();
//			fragmentTransaction.replace(R.id.fragment_switch, myfragment);
//			fragmentTransaction.commit();
		}
	};
}
