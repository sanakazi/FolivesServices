package com.callndata.adapters;

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
import org.json.JSONObject;

import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.item.MerchantFoodItemListItem;
import com.squareup.picasso.Picasso;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MerchantFoodItemListAdapter extends BaseAdapter {

	String id, state;
	Context context;
	ProgressDialog pDialog;
	MerchantFoodItemListItem MFITlstItem;
	ArrayList<NameValuePair> nameValuePairs;
	ArrayList<MerchantFoodItemListItem> MerchantFoodItemListItemAL = new ArrayList<MerchantFoodItemListItem>();

	public MerchantFoodItemListAdapter(Context context,
			ArrayList<MerchantFoodItemListItem> MerchantFoodItemListItemAL) {
		this.context = context;
		this.MerchantFoodItemListItemAL = MerchantFoodItemListItemAL;
	}

	@Override
	public int getCount() {
		return MerchantFoodItemListItemAL.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view;

		if (convertView == null) {
			view = View.inflate(context, R.layout.item_merchant_food_item_list, null);
		} else {
			view = convertView;
		}

		Holder holder = new Holder();

		holder.imgFood = (ImageView) view.findViewById(R.id.imgFood);
		holder.txtName = (TextView) view.findViewById(R.id.txtName);
		holder.txtMealType = (TextView) view.findViewById(R.id.txtMealType);
		holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);
		holder.swhActive = (Switch) view.findViewById(R.id.swhActive);

		Picasso.with(context).load(MerchantFoodItemListItemAL.get(position).getStrImg()).into(holder.imgFood);
		holder.txtName.setText((MerchantFoodItemListItemAL.get(position).getStrName()).toUpperCase());
		holder.txtMealType.setText(MerchantFoodItemListItemAL.get(position).getStrMealType());
		holder.txtPrice.setText(MerchantFoodItemListItemAL.get(position).getStrPrice());

		String status = MerchantFoodItemListItemAL.get(position).getStrStatus();

		if (status.equals("1")) {
			holder.swhActive.setChecked(true);
		} else {
			holder.swhActive.setChecked(false);
		}

		holder.swhActive.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				id = MerchantFoodItemListItemAL.get(position).getStrid();
				if (isChecked == true) {
					state = "1";
				} else {
					state = "0";
				}
				new FoodStateChangeJSON().execute();
			}
		});

		return view;
	}

	class Holder {

		ImageView imgFood;
		TextView txtName, txtMealType, txtPrice;
		Switch swhActive;

	}

	class FoodStateChangeJSON extends AsyncTask<Void, Void, Void> {

		String status;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Please Wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
				nameValuePairs.add(new BasicNameValuePair("active", state));
				nameValuePairs.add(new BasicNameValuePair("id", id));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(AppConstants.MERCHANT_FOOD_STATUS_CHANGE);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				String imgResJSON = EntityUtils.toString(entity);

				JSONObject obj = new JSONObject(imgResJSON);

				status = obj.getString("success");

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
				//Toast.makeText(context, "Changed...", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "Error...", Toast.LENGTH_SHORT).show();
			}
		}

	}

}
