package com.callndata.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.folivesservices.FavouriteChangeJSON;
import com.callndata.others.AppConstants;
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

public class CustomerChefItemsAdapter extends BaseAdapter {

    Context context;
    ArrayList<CustomerChefItemsItem> CustomerChefItemsItemAL = new ArrayList<CustomerChefItemsItem>();

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;
    ListView lstReviews;
    View popupView1;
    PopupWindow popupWindow1;

    CustomerChefDetailedViewReviewsAdapter adapter;

    ProgressBar progress;
    LinearLayout llListHeader;
    TextView txtNoPreview;

    CustomerChefReviewsItem CCRItem;
    ArrayList<CustomerChefReviewsItem> CustomerChefReviewsItemAL = new ArrayList<CustomerChefReviewsItem>();

    ImageView imgReview;

    public CustomerChefItemsAdapter(Context context, ArrayList<CustomerChefItemsItem> CustomerChefItemsItemAL, ArrayList<CustomerChefReviewsItem> CustomerChefReviewsItemAL) {
        this.context = context;
        this.CustomerChefItemsItemAL = CustomerChefItemsItemAL;
        this.CustomerChefReviewsItemAL = CustomerChefReviewsItemAL;
    }

    @Override
    public int getCount() {
        return CustomerChefItemsItemAL.size();
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
            view = View.inflate(context, R.layout.item_customer_chef_item, null);
        } else {
            view = convertView;
        }

        final Holder holder = new Holder();
        holder.imgFoodImg = (ImageView) view.findViewById(R.id.imgFoodImg);
        holder.imgFav = (ImageView) view.findViewById(R.id.imgFav);
        holder.imgDetailedView = (ImageView) view.findViewById(R.id.imgDetailedView);
        holder.imgVegNonVeg = (ImageView) view.findViewById(R.id.imgVegNonVeg);

        holder.txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
        holder.txtRating = (TextView) view.findViewById(R.id.txtRating);
        holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);

        Picasso.with(context).load(CustomerChefItemsItemAL.get(position).getImage()).fit().centerCrop()
                .into(holder.imgFoodImg);

        try {
            if (CustomerChefItemsItemAL.get(position).getFav().equals("0")) {
                holder.imgFav.setBackgroundResource(R.drawable.ic_favorites_heart);
            } else {
                holder.imgFav.setBackgroundResource(R.drawable.ic_favorites_red);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (CustomerChefItemsItemAL.get(position).getVegNonVeg().equals("vegetarian")) {
            holder.imgVegNonVeg.setBackgroundResource(R.drawable.ic_veg);
        } else {
            holder.imgVegNonVeg.setBackgroundResource(R.drawable.ic_nonveg);
        }

        holder.imgFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (CustomerChefItemsItemAL.get(pos).getFav().equals("0")) {

                        new FavouriteChangeJSON(context).execute("1", "1", "1"); // FavUnfav,
                        // TypeIOrC,
                        // Id

                        holder.imgFav.setBackgroundResource(R.drawable.ic_favorites_red);
                        CustomerChefItemsItemAL.get(pos).setFav("1");

                    } else if (CustomerChefItemsItemAL.get(pos).getFav().equals("1")) {

                        new FavouriteChangeJSON(context).execute("0", "1", "1"); // FavUnfav,
                        // TypeIOrC,
                        // Id

                        holder.imgFav.setBackgroundResource(R.drawable.ic_favorites_heart);
                        CustomerChefItemsItemAL.get(pos).setFav("0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.imgDetailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View FunctionLayout;

                final View stickyViewSpacer;
                ImageView imgFoodImg, imgClose, imgVegNonVeg;
                TextView txtFoodName, txtFoodDesc, txtCuisine, txtType, txtFoodPrice, txtRating;

                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();

                try {
                    LayoutInflater layoutInflater = (LayoutInflater) context
                            .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.popup_chef_food_details, null);
                    final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT, true);

                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    // popupWindow.setWidth(700);
                    popupWindow.setAnimationStyle(R.style.DialogAnimation);

                    lstReviews = (ListView) popupView.findViewById(R.id.lstReviews);
                    llListHeader = (LinearLayout) popupView.findViewById(R.id.llListHeader);
                    FunctionLayout = popupView.findViewById(R.id.llFunctions);

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View listHeader = inflater.inflate(R.layout.customerhome_listview_header, null);
                    stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

                    lstReviews.addHeaderView(listHeader);

                    imgFoodImg = (ImageView) popupView.findViewById(R.id.imgFoodImg);
                    imgClose = (ImageView) popupView.findViewById(R.id.imgClose);
                    imgVegNonVeg = (ImageView) popupView.findViewById(R.id.imgVegNonVeg);
                    imgReview = (ImageView) popupView.findViewById(R.id.imgReview);

                    txtFoodName = (TextView) popupView.findViewById(R.id.txtFoodName);
                    txtFoodDesc = (TextView) popupView.findViewById(R.id.txtFoodDesc);
                    txtCuisine = (TextView) popupView.findViewById(R.id.txtCuisine);
                    txtType = (TextView) popupView.findViewById(R.id.txtType);
                    txtFoodPrice = (TextView) popupView.findViewById(R.id.txtFoodPrice);
                    txtRating = (TextView) popupView.findViewById(R.id.txtRating);
                    txtNoPreview = (TextView) popupView.findViewById(R.id.txtNoPreview);

                    progress = (ProgressBar) popupView.findViewById(R.id.progressBar);
                    progress.showContextMenu();
                    popupWindow.showAtLocation(holder.imgFoodImg, Gravity.CENTER, 0, 0);

                    if ((CustomerChefItemsItemAL.get(pos).getVegNonVeg()).equals("vegetarian")) {
                        imgVegNonVeg.setBackgroundResource(R.drawable.ic_veg);
                    } else if ((CustomerChefItemsItemAL.get(pos).getVegNonVeg()).equals("non vegetarian")) {
                        imgVegNonVeg.setBackgroundResource(R.drawable.ic_nonveg);
                    }

                    try {
                        Picasso.with(context).load(CustomerChefItemsItemAL.get(pos).getImage()).fit().centerCrop()
                                .into(imgFoodImg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    txtFoodName.setText(CustomerChefItemsItemAL.get(pos).getName());
                    txtFoodDesc.setText(CustomerChefItemsItemAL.get(pos).getDesc());
                    txtCuisine.setText(CustomerChefItemsItemAL.get(pos).getCusine());

                    String Type = CustomerChefItemsItemAL.get(pos).getType();
                    txtType.setText(Type);
                    txtFoodPrice.setText("₹ " + CustomerChefItemsItemAL.get(pos).getPrice());
                    txtRating.setText(CustomerChefItemsItemAL.get(pos).getRating());

                    imgClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                    lstReviews.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                            if (lstReviews.getFirstVisiblePosition() == 0) {
                                View firstChild = lstReviews.getChildAt(0);
                                int topY = 0;
                                if (firstChild != null) {
                                    topY = firstChild.getTop();
                                }
                                int heroTopY = stickyViewSpacer.getTop();
                                llListHeader.setY(Math.max(0, heroTopY + topY + 100));
                                FunctionLayout.setY(topY * 0.9f);
                            }
                        }
                    });

                    if (CustomerChefItemsItemAL.get(pos).getReviewFlag().equals("0")) {
                        imgReview.setVisibility(View.VISIBLE);
                    } else if (CustomerChefItemsItemAL.get(pos).getReviewFlag().equals("1")) {
                        imgReview.setVisibility(View.INVISIBLE);
                    }


                    imgReview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final TextView txtRatingValue;
                            TextView txtBack, txtFoodName, txtDone;
                            RatingBar ratingbar;
                            LinearLayout llMain;
                            final EditText etReview;

                            LayoutInflater layoutInflater = (LayoutInflater) context
                                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                            popupView1 = layoutInflater.inflate(R.layout.popup_write_review, null);
                            popupWindow1 = new PopupWindow(popupView1, ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT, true);

                            popupWindow1.setOutsideTouchable(true);
                            popupWindow1.setFocusable(true);
                            popupWindow1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            popupWindow1.setAnimationStyle(R.style.DialogAnimation);
                            popupWindow1.showAtLocation(holder.imgFoodImg, Gravity.CENTER, 0, 0);

                            txtRatingValue = (TextView) popupView1.findViewById(R.id.txtRatingValue);
                            txtBack = (TextView) popupView1.findViewById(R.id.txtBack);
                            txtFoodName = (TextView) popupView1.findViewById(R.id.txtFoodName);
                            txtDone = (TextView) popupView1.findViewById(R.id.txtDone);

                            ratingbar = (RatingBar) popupView1.findViewById(R.id.ratingbar);
                            etReview = (EditText) popupView1.findViewById(R.id.etReview);

                            txtFoodName.setText(CustomerChefItemsItemAL.get(pos).getName());
                            txtBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow1.dismiss();
                                }
                            });
                            ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                    txtRatingValue.setText(Float.toString(rating));
                                }
                            });

                            txtDone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String review = etReview.getText().toString();

                                    if (!review.equals("") || !review.equals(null)) {
                                        String id, rating, reviewText;
                                        Float rateValF = Float.parseFloat(txtRatingValue.getText().toString());
                                        int rateCalI = Math.round(rateValF);

                                        id = CustomerChefItemsItemAL.get(pos).getId();
                                        rating = Integer.toString(rateCalI);
                                        reviewText = etReview.getText().toString();

                                        new CustomerAddItemReviewJSON().execute(id, reviewText, rating);
                                    } else {
                                        etReview.setError("Write review...");
                                    }
                                }
                            });

                        }
                    });

                    lstReviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            TextView txtFoodName, txtOk, txtRatingValue;
                            RatingBar ratingbar;
                            EditText etReview;
                            ImageView imgPic;

                            LayoutInflater layoutInflater = (LayoutInflater) context
                                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                            View popupView = layoutInflater.inflate(R.layout.popup_review_display, null);
                            final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT, true);

                            txtFoodName = (TextView) popupView.findViewById(R.id.txtFoodName);
                            txtOk = (TextView) popupView.findViewById(R.id.txtOk);
                            txtRatingValue = (TextView) popupView.findViewById(R.id.txtRatingValue);
                            ratingbar = (RatingBar) popupView.findViewById(R.id.ratingbar);
                            etReview = (EditText) popupView.findViewById(R.id.etReview);
                            imgPic = (ImageView) popupView.findViewById(R.id.imgPic);

                            txtFoodName.setText(CustomerChefReviewsItemAL.get(position - 1).getName());
                            txtRatingValue.setText(CustomerChefReviewsItemAL.get(position - 1).getRating());
                            ratingbar.setRating(Float.parseFloat(CustomerChefReviewsItemAL.get(position - 1).getRating()));
                            etReview.setText(CustomerChefReviewsItemAL.get(position - 1).getReview());

                            popupWindow.setOutsideTouchable(true);
                            popupWindow.setFocusable(true);
                            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            popupWindow.setAnimationStyle(R.style.DialogAnimation);
                            popupWindow.showAtLocation(holder.imgFoodImg, Gravity.CENTER, 0, 0);

                            try {
                                Picasso.with(context).load(CustomerChefReviewsItemAL.get(position - 1).getImage()).into(imgPic);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            txtOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                }
                            });
                        }
                    });

                    new ChefItemReviewJSON().execute(CustomerChefItemsItemAL.get(pos).getId());

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.txtFoodName.setText(CustomerChefItemsItemAL.get(position).getName());
        holder.txtRating.setText(CustomerChefItemsItemAL.get(position).getRating());
        holder.txtPrice.setText("₹ " + CustomerChefItemsItemAL.get(position).getPrice());

        SpannableString content = new SpannableString("₹ " + CustomerChefItemsItemAL.get(position).getPrice());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.txtPrice.setText(content);

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return CustomerChefItemsItemAL.size();
    }

    class Holder {
        ImageView imgFoodImg, imgFav, imgDetailedView, imgVegNonVeg;
        TextView txtFoodName, txtRating, txtPrice;
    }

    class ChefItemReviewJSON extends AsyncTask<String, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String item_id = params[0];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("item_id", item_id));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_CHEF_ITEM_REVIEWS);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                    CustomerChefReviewsItemAL.clear();


                    JSONObject objReviews = new JSONObject();
                    JSONArray arrReviews = new JSONArray();

                    objReviews = obj.getJSONObject("item_reviews");
                    arrReviews = objReviews.getJSONArray("item_review");

                    objReviews = obj.getJSONObject("item_reviews");
                    arrReviews = objReviews.getJSONArray("item_review");

                    for (int i = 0; i < arrReviews.length(); i++) {

                        CCRItem = new CustomerChefReviewsItem();

                        JSONObject objUserReview = new JSONObject();
                        objUserReview = arrReviews.getJSONObject(i);

                        CCRItem.setId(objUserReview.getString("item_id"));
                        CCRItem.setReview(objUserReview.getString("review"));
                        CCRItem.setRating(objUserReview.getString("rating"));
                        CCRItem.setDate(objUserReview.getString("created_at"));

                        JSONObject objUser = new JSONObject();
                        objUser = objUserReview.getJSONObject("user");

                        CCRItem.setName(objUser.getString("first_name") + " " + objUser.getString("last_name"));
                        CCRItem.setImage(AppConstants.Customer_Profile_Image_Path + objUser.getString("profile_photo"));

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

                progress.setVisibility(View.INVISIBLE);
                llListHeader.setVisibility(View.VISIBLE);
                if (CustomerChefReviewsItemAL.size() <= 0) {
                    txtNoPreview.setVisibility(View.VISIBLE);
                } else {
                    txtNoPreview.setVisibility(View.INVISIBLE);
                }
                adapter = new CustomerChefDetailedViewReviewsAdapter(context, CustomerChefReviewsItemAL);
                lstReviews.setAdapter(adapter);

            } else {
                Toast.makeText(context, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class CustomerAddItemReviewJSON extends AsyncTask<String, Void, Void> {

        String status = "0";
        String item_id, review, rating;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            item_id = params[0];
            review = params[1];
            rating = params[2];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("chef_id", "1"));
                nameValuePairs.add(new BasicNameValuePair("item_id", item_id));
                nameValuePairs.add(new BasicNameValuePair("review", review));
                nameValuePairs.add(new BasicNameValuePair("rating", rating));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_ITEM_REVIEW);
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
                Toast.makeText(context, "Review Posted...", Toast.LENGTH_SHORT).show();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                popupWindow1.dismiss();
                lstReviews.setAdapter(null);
                imgReview.setVisibility(View.INVISIBLE);
                new ChefItemReviewJSON().execute(item_id);

            } else {
                Toast.makeText(context, "Server Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
