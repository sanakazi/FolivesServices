package com.callndata.folivesservices;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapters.CustomerChefItemsAdapter;
import com.callndata.adapters.CustomerChefReviewAdapter;
import com.callndata.others.AppConstants;
import com.callndata.others.HorizontalPager;
import com.callndata.others.HorizontalPager.OnScreenSwitchListener;
import com.example.folivesservices.R;
import com.folives.item.CustomerChefItemsItem;
import com.folives.item.CustomerChefReviewsItem;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CustomerChefItemsAndReviews extends FragmentActivity {

    HorizontalPager horizontal_pager;
    ImageView imgChefImg, imgRight, imgCustPic, imgFav, imgEdit;
    LinearLayout llItemsLine, llReviewsLine, llItems, llReviews;
    TextView txtRating, txtChefName, txtNotifications, txtItemCount, txtReviewCount;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    String ChefId;
    CustomerChefItemsItem CCIItem;
    ArrayList<CustomerChefItemsItem> CustomerChefItemsItemAL = new ArrayList<CustomerChefItemsItem>();

    CustomerChefReviewsItem CCRItem;
    ArrayList<CustomerChefReviewsItem> CustomerChefReviewsItemAL = new ArrayList<CustomerChefReviewsItem>();

    CustomerChefItemsAdapter ItemAdapter;
    CustomerChefReviewAdapter ReviewAdapter;

    String ChefFav;
    ListView lstReviews;

    String Chef_Id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chef_items_and_reviews);

        Chef_Id = getIntent().getExtras().getString("chefId");

        horizontal_pager = (HorizontalPager) findViewById(R.id.horizontal_pager);

        imgChefImg = (ImageView) findViewById(R.id.imgChefImg);
        imgRight = (ImageView) findViewById(R.id.imgRight);
        imgCustPic = (ImageView) findViewById(R.id.imgCustPic);
        imgFav = (ImageView) findViewById(R.id.imgFav);
        imgEdit = (ImageView) findViewById(R.id.imgEdit);

        txtRating = (TextView) findViewById(R.id.txtRating);
        txtChefName = (TextView) findViewById(R.id.txtChefName);
        txtNotifications = (TextView) findViewById(R.id.txtNotifications);
        txtItemCount = (TextView) findViewById(R.id.txtItemCount);
        txtReviewCount = (TextView) findViewById(R.id.txtReviewCount);

        llItemsLine = (LinearLayout) findViewById(R.id.llItemsLine);
        llReviewsLine = (LinearLayout) findViewById(R.id.llReviewsLine);
        llItems = (LinearLayout) findViewById(R.id.llItems);
        llReviews = (LinearLayout) findViewById(R.id.llReviews);

        lstReviews = new ListView(CustomerChefItemsAndReviews.this);

        Picasso.with(CustomerChefItemsAndReviews.this).load(AppConstants.Customer_Profile_Pic).into(imgCustPic);

        horizontal_pager.setOnScreenSwitchListener(new OnScreenSwitchListener() {

            @Override
            public void onScreenSwitched(int screen) {

                if (screen == 0) {
                    llItemsLine.setBackgroundColor(getResources().getColor(R.color.red));
                    llReviewsLine.setBackgroundResource(0);
                } else if (screen == 1) {
                    llItemsLine.setBackgroundResource(0);
                    llReviewsLine.setBackgroundColor(getResources().getColor(R.color.red));
                }
            }
        });

        llItems.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                llItemsLine.setBackgroundColor(getResources().getColor(R.color.red));
                llReviewsLine.setBackgroundResource(0);

                horizontal_pager.setCurrentScreen(0, true);
            }
        });

        llReviews.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                llItemsLine.setBackgroundResource(0);
                llReviewsLine.setBackgroundColor(getResources().getColor(R.color.red));

                horizontal_pager.setCurrentScreen(1, true);
            }
        });

        imgFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ChefFav.equals("0")) {
                    new FavouriteChangeJSON(CustomerChefItemsAndReviews.this).execute("1", "2", "1"); // FavUnfav,
                    // TypeIOrC,
                    // Id
                    ChefFav = "1";
                    imgFav.setBackgroundResource(R.drawable.ic_favorites_red);
                } else if (ChefFav.equals("1")) {
                    new FavouriteChangeJSON(CustomerChefItemsAndReviews.this).execute("0", "2", "1"); // FavUnfav,
                    // TypeIOrC,
                    // Id
                    ChefFav = "0";
                    imgFav.setBackgroundResource(R.drawable.ic_favorites_heart);
                }
            }
        });

        lstReviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtFoodName, txtOk, txtRatingValue;
                RatingBar ratingbar;
                EditText etReview;
                ImageView imgPic;

                LayoutInflater layoutInflater = (LayoutInflater) CustomerChefItemsAndReviews.this
                        .getSystemService(CustomerChefItemsAndReviews.this.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_review_display, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, true);

                txtFoodName = (TextView) popupView.findViewById(R.id.txtFoodName);
                txtOk = (TextView) popupView.findViewById(R.id.txtOk);
                txtRatingValue = (TextView) popupView.findViewById(R.id.txtRatingValue);
                ratingbar = (RatingBar) popupView.findViewById(R.id.ratingbar);
                etReview = (EditText) popupView.findViewById(R.id.etReview);
                imgPic = (ImageView) popupView.findViewById(R.id.imgPic);

                txtFoodName.setText(CustomerChefReviewsItemAL.get(position).getName());
                txtRatingValue.setText(CustomerChefReviewsItemAL.get(position).getRating());
                ratingbar.setRating(Float.parseFloat(CustomerChefReviewsItemAL.get(position).getRating()));
                etReview.setText(CustomerChefReviewsItemAL.get(position).getReview());

                try {
                    Picasso.with(CustomerChefItemsAndReviews.this).load(CustomerChefReviewsItemAL.get(position).getImage()).into(imgPic);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupWindow.setAnimationStyle(R.style.DialogAnimation);
                popupWindow.showAtLocation(imgFav, Gravity.CENTER, 0, 0);

                txtOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        new ChefInfoJSON().execute(Chef_Id);

    }

    class ChefInfoJSON extends AsyncTask<String, Void, Void> {

        String status = "0";
        String ChefName, ChefImage, ChefRating, ChefVerified, ChefReviewed, ChefItemCount, ChefReviewsCount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerChefItemsAndReviews.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String ChefId = params[0];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("chef_id", ChefId));
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_CHEF_ITEMS_REVIEWS);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {

                    JSONObject chefInfoObj = new JSONObject();
                    chefInfoObj = obj.getJSONObject("chef_details");

                    ChefName = chefInfoObj.getString("name");
                    ChefImage = AppConstants.Customer_Chef_Image_path + chefInfoObj.getString("logo");
                    ChefVerified = chefInfoObj.getString("verified");

                    JSONArray ChefRatingArr = new JSONArray();
                    ChefRatingArr = chefInfoObj.getJSONArray("chef_rating");
                    if (ChefRatingArr.length() > 0) {
                        JSONObject ChefRatingObj = new JSONObject();
                        ChefRatingObj = ChefRatingArr.getJSONObject(0);
                        ChefRating = ChefRatingObj.getString("average");
                    }
                    JSONArray ChefFavArr = new JSONArray();
                    ChefFavArr = chefInfoObj.getJSONArray("favourited_chef");
                    if (ChefFavArr.length() > 0) {
                        if (ChefFavArr.length() == 0) {
                            ChefFav = "0";
                        } else {
                            ChefFav = "1";
                        }
                    }

                    JSONArray ChefCReviewArr = new JSONArray();
                    ChefCReviewArr = chefInfoObj.getJSONArray("reviewed_chef");

                    if (ChefCReviewArr.length() == 0) {
                        ChefReviewed = "0";
                    } else {
                        ChefReviewed = "1";
                    }


                    // For Chef Items and Reviews...

                    JSONArray ChefItemsArr = new JSONArray();
                    JSONArray ChefReviewsArr = new JSONArray();

                    ChefItemsArr = new JSONArray();
                    ChefItemsArr = obj.getJSONArray("chef_items");

                    ChefReviewsArr = new JSONArray();
                    ChefReviewsArr = obj.getJSONArray("chef_reviews");

                    ChefItemCount = Integer.toString(ChefItemsArr.length());
                    ChefReviewsCount = Integer.toString(ChefReviewsArr.length());

                    for (int i = 0; i < ChefItemsArr.length(); i++) {
                        JSONObject ChefItemObj = ChefItemsArr.getJSONObject(i);
                        CCIItem = new CustomerChefItemsItem();

                        CCIItem.setId(ChefItemObj.getString("id"));
                        CCIItem.setName(ChefItemObj.getString("item_name"));
                        CCIItem.setDesc(ChefItemObj.getString("item_description"));
                        CCIItem.setCusine(ChefItemObj.getString("cuisine"));
                        CCIItem.setImage(AppConstants.Customer_Dish_Image_path + ChefItemObj.getString("photo"));
                        CCIItem.setPrice(ChefItemObj.getString("price"));
                        CCIItem.setVegNonVeg(ChefItemObj.getString("dish"));

                        JSONArray RatingArr = new JSONArray();
                        JSONObject RatingObj = new JSONObject();
                        RatingArr = ChefItemObj.getJSONArray("item_rating");
                        if (RatingArr.length() != 0) {
                            RatingObj = RatingArr.getJSONObject(0);
                            CCIItem.setRating(RatingObj.getString("average"));
                        } else {
                            CCIItem.setRating("0");
                        }


                        JSONObject ItemTypeObj = new JSONObject();
                        ItemTypeObj = ChefItemObj.getJSONObject("item_type");
                        CCIItem.setType(ItemTypeObj.getString("type_name"));

                        JSONArray FavArr = new JSONArray();
                        JSONObject FavObj = new JSONObject();
                        FavArr = ChefItemObj.getJSONArray("favourited_item");
                        if (FavArr.length() > 0) {
                            if (FavArr.length() == 0) {
                                CCIItem.setFav("0");
                            } else {
                                CCIItem.setFav("1");
                            }
                        }

                        JSONArray ReviewArr = new JSONArray();
                        ReviewArr = ChefItemObj.getJSONArray("reviewed_item");
                        if (ReviewArr.length() > 0) {
                            if (ReviewArr.length() > 0) {
                                CCIItem.setReviewFlag("1");
                            } else {
                                CCIItem.setReviewFlag("0");
                            }
                        }

                        CustomerChefItemsItemAL.add(CCIItem);
                    }

                    for (int i = 0; i < ChefReviewsArr.length(); i++) {
                        JSONObject ReviewObj = new JSONObject();
                        ReviewObj = ChefReviewsArr.getJSONObject(i);

                        CCRItem = new CustomerChefReviewsItem();

                        CCRItem.setId(ReviewObj.getString("id"));
                        CCRItem.setReview(ReviewObj.getString("review"));
                        CCRItem.setRating(ReviewObj.getString("rating"));
                        CCRItem.setDate(ReviewObj.getString("created_at"));

                        JSONObject UserObj = new JSONObject();
                        UserObj = ReviewObj.getJSONObject("user");

                        CCRItem.setName(UserObj.getString("first_name") + " " + UserObj.getString("last_name"));
                        CCRItem.setImage(AppConstants.Customer_Profile_Image_Path + UserObj.getString("profile_photo"));

                        CustomerChefReviewsItemAL.add(CCRItem);
                    }
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

                txtChefName.setText(ChefName);
                txtRating.setText(ChefRating);
                if (ChefVerified.equals("0")) {
                    imgRight.setVisibility(View.INVISIBLE);
                } else {
                    imgRight.setVisibility(View.VISIBLE);
                }
                Picasso.with(CustomerChefItemsAndReviews.this).load(ChefImage).into(imgChefImg);

                if (ChefFav.equals("0")) {
                    imgFav.setBackgroundResource(R.drawable.ic_favorites_heart);
                } else {
                    imgFav.setBackgroundResource(R.drawable.ic_favorites_red);
                }

                if (ChefReviewed.equals("0")) {
                    imgEdit.setVisibility(View.INVISIBLE);
                } else {
                    imgEdit.setVisibility(View.VISIBLE);
                }

                txtItemCount.setText(ChefItemCount);
                txtReviewCount.setText(ChefReviewsCount);

                LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                for (int i = 0; i < 2; i++) {
                    if (i == 0) {
                        if (CustomerChefItemsItemAL.size() > 0) {
                            ItemAdapter = new CustomerChefItemsAdapter(CustomerChefItemsAndReviews.this,
                                    CustomerChefItemsItemAL, CustomerChefReviewsItemAL);
                            ListView lstItems = new ListView(CustomerChefItemsAndReviews.this);
                            lstItems.setLayoutParams(lparams);
                            lstItems.setAdapter(ItemAdapter);
                            horizontal_pager.addView(lstItems);
                        } else {
                            ImageView img = new ImageView(CustomerChefItemsAndReviews.this);
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontal_pager.addView(img);
                        }
                    } else if (i == 1) {
                        if (CustomerChefReviewsItemAL.size() > 0) {
                            ReviewAdapter = new CustomerChefReviewAdapter(CustomerChefItemsAndReviews.this,
                                    CustomerChefReviewsItemAL);
                            lstReviews.setLayoutParams(lparams);
                            lstReviews.setAdapter(ReviewAdapter);
                            horizontal_pager.addView(lstReviews);
                        } else {
                            ImageView img = new ImageView(CustomerChefItemsAndReviews.this);
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontal_pager.addView(img);
                        }
                    }
                }

            } else if (status.equals("0")) {
                Toast.makeText(CustomerChefItemsAndReviews.this, "Server Error...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CustomerChefItemsAndReviews.this, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
