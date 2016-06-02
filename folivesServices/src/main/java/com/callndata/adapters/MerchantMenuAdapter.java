package com.callndata.adapters;

import java.util.ArrayList;

import com.example.folivesservices.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchantMenuAdapter extends BaseAdapter {

	ArrayList<Integer> imgIcon = new ArrayList<Integer>();
	ArrayList<String> imgName = new ArrayList<String>();
	Context context;

	public MerchantMenuAdapter(Context context, ArrayList<Integer> imgIcon, ArrayList<String> imgName) {

		this.context = context;
		this.imgIcon = imgIcon;
		this.imgName = imgName;
	}

	@Override
	public int getCount() {
		return imgIcon.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {

		View view;

		if (convertView == null) {
			view = View.inflate(context, R.layout.item_merchant_menu, null);
		} else {
			view = convertView;
		}
		Holder holder = new Holder();

		holder.imageIcon = (ImageView) view.findViewById(R.id.imgIcon);
		holder.imageName = (TextView) view.findViewById(R.id.txtIconName);

		holder.imageIcon.setBackgroundResource(imgIcon.get(position));
		holder.imageName.setText(imgName.get(position));

		return view;
	}

	public class Holder {
		ImageView imageIcon;
		TextView imageName;
	}

}
