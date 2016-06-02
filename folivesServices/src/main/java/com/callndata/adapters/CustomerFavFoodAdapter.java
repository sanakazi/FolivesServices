package com.callndata.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.folivesservices.CustomerChefItemsAndReviews;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.item.CustomerFavouriteFoodItem;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CustomerFavFoodAdapter extends BaseAdapter {

    Context context;
    TextView txtItemCount;
    ArrayList<CustomerFavouriteFoodItem> FavouriteItemsAL = new ArrayList<CustomerFavouriteFoodItem>();

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    public CustomerFavFoodAdapter(Context context, ArrayList<CustomerFavouriteFoodItem> FavouriteItemsAL, TextView txtItemCount) {
        this.context = context;
        this.txtItemCount = txtItemCount;
        this.FavouriteItemsAL = FavouriteItemsAL;
    }

    @Override
    public int getCount() {
        return FavouriteItemsAL.size();
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
        final int pos = position;

        if (convertView == null) {
            view = View.inflate(context, R.layout.item_customer_fav_food, null);
        } else {
            view = convertView;
        }

        Holder holder = new Holder();

        holder.rlFoodImg = (RelativeLayout) view.findViewById(R.id.rlFoodImg);
        holder.imgChefPic = (ImageView) view.findViewById(R.id.imgChefPic);
        holder.imgVegNonveg = (ImageView) view.findViewById(R.id.imgVegNonveg);
        holder.txtChefName = (TextView) view.findViewById(R.id.txtChefName);
        holder.txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
        holder.txtRating = (TextView) view.findViewById(R.id.txtRating);
        holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        holder.imgFoodImg = (ImageView) view.findViewById(R.id.imgFoodImg);
        holder.imgUnFavourite = (ImageView) view.findViewById(R.id.imgUnFavourite);
        holder.llChefStrip = (LinearLayout) view.findViewById(R.id.llChefStrip);

        holder.txtFoodName.setText(FavouriteItemsAL.get(position).getFoodName());
        holder.txtChefName.setText("Chef " + FavouriteItemsAL.get(position).getChefName());
        holder.txtRating.setText(FavouriteItemsAL.get(position).getFoodRating());
        holder.txtPrice.setText(FavouriteItemsAL.get(position).getFoodPrice());

        SpannableString content = new SpannableString(FavouriteItemsAL.get(position).getFoodPrice());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.txtPrice.setText(content);

        Picasso.with(context).load(FavouriteItemsAL.get(position).getFoodPic()).fit().centerCrop()
                .into(holder.imgFoodImg);
        Picasso.with(context).load(FavouriteItemsAL.get(position).getChefPic()).into(holder.imgChefPic);

        String vn = FavouriteItemsAL.get(position).getVegNonveg();
        if (vn.equals("vegetarian")) {
            holder.imgVegNonveg.setBackgroundResource(R.drawable.ic_veg);
        } else if (vn.equals("non vegetarian")) {
            holder.imgVegNonveg.setBackgroundResource(R.drawable.ic_nonveg);
        }

        holder.imgUnFavourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context).setTitle("Unfavourite")
                        .setMessage("Do you really want to unfavourite it?").setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                new UnFavouriteJSON().execute(FavouriteItemsAL.get(pos).getFavId(), Integer.toString(pos));
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
            }
        });

        holder.imgChefPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerChefItemsAndReviews.class);
                intent.putExtra("chefId", FavouriteItemsAL.get(pos).getChef_id());
                context.startActivity(intent);
            }
        });
        holder.llChefStrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerChefItemsAndReviews.class);
                intent.putExtra("chefId", FavouriteItemsAL.get(pos).getChef_id());
                context.startActivity(intent);
            }
        });

        return view;
    }

    class Holder {

        RelativeLayout rlFoodImg;
        LinearLayout llChefStrip;
        TextView txtChefName, txtFoodName, txtRating, txtPrice;
        ImageView imgChefPic, imgVegNonveg, imgFoodImg, imgUnFavourite;

    }

    class UnFavouriteJSON extends AsyncTask<String, Void, Void> {

        String status = "0", id, pos;

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

            id = params[0];
            pos = params[1];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", id));
                nameValuePairs.add(new BasicNameValuePair("fav_key", "0"));
                nameValuePairs.add(new BasicNameValuePair("type", "1"));
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));

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
                FavouriteItemsAL.remove(Integer.parseInt(pos));
                txtItemCount.setText(Integer.toString(FavouriteItemsAL.size()));
                notifyDataSetChanged();

            } else {
                Toast.makeText(context, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
