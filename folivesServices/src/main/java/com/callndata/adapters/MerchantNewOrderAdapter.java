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
import com.folives.item.NewOrderItem;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MerchantNewOrderAdapter extends BaseAdapter {

	Context context;

	ProgressDialog pDialog;
	ArrayList<NameValuePair> nameValuePairs;
	ArrayList<NewOrderItem> NewOrderItemAL = new ArrayList<NewOrderItem>();

	public MerchantNewOrderAdapter(Context context, ArrayList<NewOrderItem> NewOrderItemAL) {
		this.context = context;
		this.NewOrderItemAL = NewOrderItemAL;
	}

	@Override
	public int getCount() {
		return NewOrderItemAL.size();
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
			view = View.inflate(context, R.layout.item_order_list, null);
		} else {
			view = convertView;
		}
		final Holder holder = new Holder();

		holder.llStatus = (LinearLayout) view.findViewById(R.id.llStatus);
		holder.txtOrderNum = (TextView) view.findViewById(R.id.txtOrderNum);
		holder.txtTimeStamp = (TextView) view.findViewById(R.id.txtTimeStamp);
		holder.txtDownArrow = (TextView) view.findViewById(R.id.txtDownArrow);
		holder.txtStatus = (TextView) view.findViewById(R.id.txtStatus);
		holder.txtAmt = (TextView) view.findViewById(R.id.txtAmt);
		holder.txtCashMode = (TextView) view.findViewById(R.id.txtCashMode);

		holder.txtOrderNum.setText("ORDER# " + NewOrderItemAL.get(position).getOrderNumber() + " - "
				+ NewOrderItemAL.get(position).getName());
		holder.txtTimeStamp.setText(NewOrderItemAL.get(position).getTimeStamp());
		holder.txtAmt.setText(NewOrderItemAL.get(position).getAmount());
		holder.txtStatus.setText(NewOrderItemAL.get(position).getStatus());
		holder.txtCashMode.setText(NewOrderItemAL.get(position).getPayment_type());

		if (NewOrderItemAL.get(position).getStats_id().equals("0")) {
			holder.llStatus.setEnabled(false);
			holder.txtDownArrow.setVisibility(View.INVISIBLE);
		} else {
			holder.llStatus.setEnabled(true);
			holder.txtDownArrow.setVisibility(View.VISIBLE);
		}

		holder.llStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(context, holder.llStatus);
				popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {

						String id, status, status_id;
						id = NewOrderItemAL.get(position).getOrderNumber();
						status = item.getTitle().toString();

						if (status.equals("Preparing")) {
							status_id = "25";
						} else if (status.equals("Prepared")) {
							status_id = "50";
						} else {
							status_id = "75";
						}
						holder.txtStatus.setText(status);
						new ChangeOrderStatusJSON().execute(id, status, status_id);
						return true;
					}
				});
				popup.show();
			}
		});

		return view;
	}

	class Holder {
		LinearLayout llStatus;
		TextView txtOrderNum, txtTimeStamp, txtAmt, txtStatus, txtDownArrow, txtCashMode;
	}

	class ChangeOrderStatusJSON extends AsyncTask<String, Void, Void> {

		String NewOrderStatus = "0";
		String id, status, status_id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Updating...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {

			try {
				id = params[0];
				status = params[1];
				status_id = params[2];

				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
				nameValuePairs.add(new BasicNameValuePair("id", id));
				nameValuePairs.add(new BasicNameValuePair("status", status));
				nameValuePairs.add(new BasicNameValuePair("status_id", status_id));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(AppConstants.MERCHANT_CHANGE_ORDER_STATUS);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);

				// int responseCode = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				String responseBody = EntityUtils.toString(entity);

				JSONObject Mobj = new JSONObject(responseBody);
				NewOrderStatus = Mobj.getString("success");
				if (NewOrderStatus.equals("true")) {

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
				// holder.txtStatus.setText(status);
				Toast.makeText(context, "Status Updated.", Toast.LENGTH_SHORT).show();
			} else if (NewOrderStatus.equals("false")) {
				Toast.makeText(context, "Oops ! Somthing went wrong.", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "Server Error...", Toast.LENGTH_SHORT).show();
			}

		}
	}

}
