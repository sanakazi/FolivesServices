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
import org.json.JSONObject;

import com.callndata.others.AppConstants;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class FavouriteChangeJSON extends AsyncTask<String, Void, Void> {

	Context context;
	String FavUnfav, TypeIOrC, Id, status = "0", Result;
	ProgressDialog pDialog;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	public FavouriteChangeJSON(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Please Wait...");
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	protected Void doInBackground(String... params) {

		FavUnfav = params[0];
		TypeIOrC = params[1];
		Id = params[2];

		try {

			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
			nameValuePairs.add(new BasicNameValuePair("fav_key", FavUnfav));
			nameValuePairs.add(new BasicNameValuePair("type", TypeIOrC));
			nameValuePairs.add(new BasicNameValuePair("id", Id));

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(AppConstants.FAVOURITE_CHANGE_JSON);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String imgResJSON = reader.readLine();

			JSONObject obj = new JSONObject(imgResJSON);

			status = obj.getString("success");

			if (status.equals("true")) {
				Result = obj.getString("message");
			} else if (status.equals("false")) {
				Result = obj.getString("errors");
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

		Toast.makeText(context, Result, Toast.LENGTH_SHORT).show();

	}
}
