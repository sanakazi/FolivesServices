package com.callndata.adapterM;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantReviewItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ravikant on 16-03-2016.
 */
public class MerchantItemReviewAdapter extends BaseAdapter {

    Context context;
    ArrayList<NewMerchantReviewItem> NewMerchantReviewAL = new ArrayList<NewMerchantReviewItem>();

    public MerchantItemReviewAdapter(Context context, ArrayList<NewMerchantReviewItem> NewMerchantReviewAL) {
        this.context = context;
        this.NewMerchantReviewAL = NewMerchantReviewAL;
    }

    @Override
    public int getCount() {
        return NewMerchantReviewAL.size();
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

        final int pos = position;

        View view;

        if (convertView == null) {
            view = View.inflate(context, R.layout.item_new_merchnt_item_reviews, null);
        } else {
            view = convertView;
        }

        final Holder holder = new Holder();

        holder.imgItemImage = (ImageView) view.findViewById(R.id.imgItemImage);

        holder.txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        holder.txtRating = (TextView) view.findViewById(R.id.txtRating);
        holder.txtDateTime = (TextView) view.findViewById(R.id.txtDateTime);
        holder.txtReview = (TextView) view.findViewById(R.id.txtReview);

        try {
            Picasso.with(context).load(NewMerchantReviewAL.get(pos).getPic()).fit().centerCrop().into(holder.imgItemImage);
            holder.txtItemName.setText(NewMerchantReviewAL.get(pos).getName());
            holder.txtRating.setText(NewMerchantReviewAL.get(pos).getRating());
            holder.txtDateTime.setText(NewMerchantReviewAL.get(pos).getDateTime());
            holder.txtReview.setText(NewMerchantReviewAL.get(pos).getReview());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    class Holder {
        ImageView imgItemImage;
        TextView txtItemName, txtRating, txtDateTime, txtReview;
    }
}
