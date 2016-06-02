package com.callndata.adapters;

import java.util.ArrayList;

import com.example.folivesservices.R;
import com.folives.item.NewOrderItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MerchantOrderHistoryAdapter extends BaseAdapter {

	Context context;
	ArrayList<NewOrderItem> OrderHistoryAL = new ArrayList<NewOrderItem>();

	public MerchantOrderHistoryAdapter(Context context, ArrayList<NewOrderItem> OrderHistoryAL) {
		this.context = context;
		this.OrderHistoryAL = OrderHistoryAL;
	}

	@Override
	public int getCount() {
		return OrderHistoryAL.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = View.inflate(context, R.layout.item_order_list, null);
		} else {
			view = convertView;
		}
		Holder holder = new Holder();

		holder.txtOrderNum = (TextView) view.findViewById(R.id.txtOrderNum);
		holder.txtTimeStamp = (TextView) view.findViewById(R.id.txtTimeStamp);
		holder.txtAmt = (TextView) view.findViewById(R.id.txtAmt);
		holder.txtStatus = (TextView) view.findViewById(R.id.txtStatus);
		holder.llCashMode = (LinearLayout) view.findViewById(R.id.llCashMode);
		holder.llStatus = (LinearLayout) view.findViewById(R.id.llStatus);

		holder.txtOrderNum.setText("ORDER# " + OrderHistoryAL.get(position).getOrderNumber() + " - "
				+ OrderHistoryAL.get(position).getName());
		holder.txtTimeStamp.setText(OrderHistoryAL.get(position).getTimeStamp());
		holder.txtAmt.setText(OrderHistoryAL.get(position).getAmount());
		holder.txtStatus.setText(OrderHistoryAL.get(position).getStatus());

		holder.llCashMode.setVisibility(View.GONE);

		if (OrderHistoryAL.get(position).getStatus().equals("Rejected")) {
			holder.llStatus.setBackgroundResource(R.color.red);
		} else {
			holder.llStatus.setBackgroundResource(R.color.green);
		}

		return view;
	}

	class Holder {
		LinearLayout llCashMode, llStatus;
		TextView txtOrderNum, txtTimeStamp, txtAmt, txtStatus;
	}

}
