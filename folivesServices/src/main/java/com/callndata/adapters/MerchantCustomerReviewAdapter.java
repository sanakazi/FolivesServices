package com.callndata.adapters;

import java.util.ArrayList;

import com.example.folivesservices.R;
import com.folives.item.MerchantReviewItem;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchantCustomerReviewAdapter extends BaseAdapter {

	Context context;
	MerchantReviewItem merchantreviewitem;
	ArrayList<MerchantReviewItem> MerchantReviewItemAL = new ArrayList<MerchantReviewItem>();

	public MerchantCustomerReviewAdapter(Context context, ArrayList<MerchantReviewItem> MerchantReviewItemAL) {

		this.context = context;
		this.MerchantReviewItemAL = MerchantReviewItemAL;
	}

	@Override
	public int getCount() {
		return MerchantReviewItemAL.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view;

		if (convertView == null) {
			view = View.inflate(context, R.layout.item_merchant_customer_review, null);
		} else {
			view = convertView;
		}

		Holder holder = new Holder();

		holder.txtName = (TextView) view.findViewById(R.id.txtName);
		holder.txtMonth = (TextView) view.findViewById(R.id.txtMonth);
		holder.txtSmilyValue = (TextView) view.findViewById(R.id.txtSmilyValue);
		holder.txtDesc = (TextView) view.findViewById(R.id.txtDesc);

		holder.imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
		holder.imgSil1 = (ImageView) view.findViewById(R.id.imgSil1);
		holder.imgSil2 = (ImageView) view.findViewById(R.id.imgSil2);
		holder.imgSil3 = (ImageView) view.findViewById(R.id.imgSil3);
		holder.imgSil4 = (ImageView) view.findViewById(R.id.imgSil4);
		holder.imgSil5 = (ImageView) view.findViewById(R.id.imgSil5);

		// Transformation transformation = new
		// RoundedTransformationBuilder().borderColor(Color.TRANSPARENT)
		// .borderWidthDp(1).cornerRadiusDp(30).oval(false).build();

		
		Picasso.with(context).load(MerchantReviewItemAL.get(position).getStrImg()).into(holder.imgProfilePic);

		holder.txtName.setText((MerchantReviewItemAL.get(position).getStrName()).toUpperCase());
		holder.txtDesc.setText(MerchantReviewItemAL.get(position).getStrDesc().trim());
		holder.txtMonth.setText(MerchantReviewItemAL.get(position).getStrDate());
		holder.txtSmilyValue.setText("(" + MerchantReviewItemAL.get(position).getStrRating() + ")");

		float rating = Float.parseFloat(MerchantReviewItemAL.get(position).getStrRating());

		if (rating == 0.0) {
			holder.imgSil1.setImageResource(R.drawable.smiley_gray);
			holder.imgSil2.setImageResource(R.drawable.smiley_gray);
			holder.imgSil3.setImageResource(R.drawable.smiley_gray);
			holder.imgSil4.setImageResource(R.drawable.smiley_gray);
			holder.imgSil5.setImageResource(R.drawable.smiley_gray);

		} else if (rating == 0.5) {

			holder.imgSil1.setImageResource(R.drawable.smiley_half);
			holder.imgSil2.setImageResource(R.drawable.smiley_gray);
			holder.imgSil3.setImageResource(R.drawable.smiley_gray);
			holder.imgSil4.setImageResource(R.drawable.smiley_gray);
			holder.imgSil5.setImageResource(R.drawable.smiley_gray);

		} else if (rating == 1.0) {

			holder.imgSil1.setImageResource(R.drawable.smiley);
			holder.imgSil2.setImageResource(R.drawable.smiley_gray);
			holder.imgSil3.setImageResource(R.drawable.smiley_gray);
			holder.imgSil4.setImageResource(R.drawable.smiley_gray);
			holder.imgSil5.setImageResource(R.drawable.smiley_gray);

		} else if (rating == 1.5) {

			holder.imgSil1.setImageResource(R.drawable.smiley);
			holder.imgSil2.setImageResource(R.drawable.smiley_half);
			holder.imgSil3.setImageResource(R.drawable.smiley_gray);
			holder.imgSil4.setImageResource(R.drawable.smiley_gray);
			holder.imgSil5.setImageResource(R.drawable.smiley_gray);

		} else if (rating == 2.0) {

			holder.imgSil1.setImageResource(R.drawable.smiley);
			holder.imgSil2.setImageResource(R.drawable.smiley);
			holder.imgSil3.setImageResource(R.drawable.smiley_gray);
			holder.imgSil4.setImageResource(R.drawable.smiley_gray);
			holder.imgSil5.setImageResource(R.drawable.smiley_gray);

		} else if (rating == 2.5) {

			holder.imgSil1.setImageResource(R.drawable.smiley);
			holder.imgSil2.setImageResource(R.drawable.smiley);
			holder.imgSil3.setImageResource(R.drawable.smiley_half);
			holder.imgSil4.setImageResource(R.drawable.smiley_gray);
			holder.imgSil5.setImageResource(R.drawable.smiley_gray);

		} else if (rating == 3.0) {

			holder.imgSil1.setImageResource(R.drawable.smiley);
			holder.imgSil2.setImageResource(R.drawable.smiley);
			holder.imgSil3.setImageResource(R.drawable.smiley);
			holder.imgSil4.setImageResource(R.drawable.smiley_gray);
			holder.imgSil5.setImageResource(R.drawable.smiley_gray);

		} else if (rating == 3.5) {

			holder.imgSil1.setImageResource(R.drawable.smiley);
			holder.imgSil2.setImageResource(R.drawable.smiley);
			holder.imgSil3.setImageResource(R.drawable.smiley);
			holder.imgSil4.setImageResource(R.drawable.smiley_half);
			holder.imgSil5.setImageResource(R.drawable.smiley_gray);

		} else if (rating == 4.0) {
			holder.imgSil1.setImageResource(R.drawable.smiley);
			holder.imgSil2.setImageResource(R.drawable.smiley);
			holder.imgSil3.setImageResource(R.drawable.smiley);
			holder.imgSil4.setImageResource(R.drawable.smiley);
			holder.imgSil5.setImageResource(R.drawable.smiley_gray);

		} else if (rating == 4.5) {

			holder.imgSil1.setImageResource(R.drawable.smiley);
			holder.imgSil2.setImageResource(R.drawable.smiley);
			holder.imgSil3.setImageResource(R.drawable.smiley);
			holder.imgSil4.setImageResource(R.drawable.smiley);
			holder.imgSil5.setImageResource(R.drawable.smiley_half);

		} else if (rating == 5.0) {

			holder.imgSil1.setImageResource(R.drawable.smiley);
			holder.imgSil2.setImageResource(R.drawable.smiley);
			holder.imgSil3.setImageResource(R.drawable.smiley);
			holder.imgSil4.setImageResource(R.drawable.smiley);
			holder.imgSil5.setImageResource(R.drawable.smiley);

		}

		return view;
	}

	class Holder {
		TextView txtName, txtMonth, txtSmilyValue, txtDesc;
		ImageView imgProfilePic, imgSil1, imgSil2, imgSil3, imgSil4, imgSil5;
	}
}
