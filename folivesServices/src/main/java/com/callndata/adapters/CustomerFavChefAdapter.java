package com.callndata.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.item.CustomerFavouriteChefItem;
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

public class CustomerFavChefAdapter extends BaseAdapter {

    Context context;
    ArrayList<CustomerFavouriteChefItem> FavouriteChefAL = new ArrayList<CustomerFavouriteChefItem>();

    ProgressDialog pDialog;
    TextView txtChefCount;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    public CustomerFavChefAdapter(Context context, ArrayList<CustomerFavouriteChefItem> FavouriteChefAL,
                                  TextView txtChefCount) {
        this.context = context;
        this.txtChefCount = txtChefCount;
        this.FavouriteChefAL = FavouriteChefAL;
    }

    @Override
    public int getCount() {
        return FavouriteChefAL.size();
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
            view = View.inflate(context, R.layout.item_customer_fav_chef, null);
        } else {
            view = convertView;
        }

        Holder holder = new Holder();

        holder.imgChefImg = (ImageView) view.findViewById(R.id.imgChefImg);
        holder.txtChefName = (TextView) view.findViewById(R.id.txtChefName);
        holder.txtRating = (TextView) view.findViewById(R.id.txtRating);
        holder.imgUnFavourite = (ImageView) view.findViewById(R.id.imgUnFavourite);

        holder.txtChefName.setText(FavouriteChefAL.get(position).getChefName());
        holder.txtRating.setText(FavouriteChefAL.get(position).getChefRating());
        Picasso.with(context).load(FavouriteChefAL.get(position).getChefPic()).into(holder.imgChefImg);

        holder.imgUnFavourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("Unfavourite")
                        .setMessage("Do you really want to unfavourite it?").setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                new UnFavouriteJSON().execute(FavouriteChefAL.get(pos).getFavId(), Integer.toString(pos));
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
            }
        });

        return view;
    }

    class Holder {

        ImageView imgChefImg, imgUnFavourite;
        TextView txtChefName, txtRating;
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
                nameValuePairs.add(new BasicNameValuePair("type", "2"));
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
                FavouriteChefAL.remove(Integer.parseInt(pos));
                txtChefCount.setText(Integer.toString(FavouriteChefAL.size()));
                notifyDataSetChanged();

            } else {
                Toast.makeText(context, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
