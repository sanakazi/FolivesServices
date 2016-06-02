package com.callndata.adapters;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.example.folivesservices.R;
import com.folives.item.MerchantCouponItem;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchantCouponHistoryAdapter extends BaseAdapter {

	Context context;
	String id, state;
	MerchantCouponItem item;
	ProgressDialog pDialog;
	ArrayList<NameValuePair> nameValuePairs;
	ArrayList<MerchantCouponItem> MerchantCouponItemAL = new ArrayList<MerchantCouponItem>();

	public MerchantCouponHistoryAdapter(Context context, ArrayList<MerchantCouponItem> MerchantCouponItemAL) {
		this.context = context;
		this.MerchantCouponItemAL = MerchantCouponItemAL;
	}

	@Override
	public int getCount() {
		return MerchantCouponItemAL.size();
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
			view = View.inflate(context, R.layout.item_merchant_coupon_history, null);
		} else {
			view = convertView;
		}

		Holder holder = new Holder();

		holder.txtName = (TextView) view.findViewById(R.id.txtName);
		holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);
		holder.imgFood = (ImageView) view.findViewById(R.id.imgFood);

		holder.txtName.setText((MerchantCouponItemAL.get(position).getName()).toUpperCase());

		if (MerchantCouponItemAL.get(position).getType().equals("percentage")) {
			holder.imgFood.setImageResource(R.drawable.percentage);
			holder.txtPrice.setText(MerchantCouponItemAL.get(position).getAmount() + " %");
		} else {
			holder.imgFood.setImageResource(R.drawable.fixed);
			holder.txtPrice.setText("₹ " + MerchantCouponItemAL.get(position).getAmount());
		}

		return view;
	}

	class Holder {

		ImageView imgFood;
		TextView txtName, txtPrice;
	}

}
