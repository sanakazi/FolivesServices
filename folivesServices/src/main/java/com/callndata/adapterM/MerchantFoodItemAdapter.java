package com.callndata.adapterM;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.Merchnt.NewMerchantFoodItemAddActivity;
import com.callndata.Merchnt.NewMerchantFoodItemEditActivity;
import com.callndata.Merchnt.NewMerchantFoodItemFragment;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantFoodListItem;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ravikant on 05-04-2016.
 */
public class MerchantFoodItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<NewMerchantFoodListItem> NewMerchantFoodListItemAL = new ArrayList<NewMerchantFoodListItem>();
    ArrayList<String> TabName = new ArrayList<String>();

   PopupWindow popupWindow;
    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

    static int time_slot_value;   // for passing in ws of add items to dailymenu
    static String date_value;
    public TextView tTimeSlot,tAvlon;

    public MerchantFoodItemAdapter(Context context, ArrayList<NewMerchantFoodListItem> NewMerchantFoodListItemAL) {
        this.context = context;
        this.NewMerchantFoodListItemAL = NewMerchantFoodListItemAL;
    }



    @Override
    public int getCount() {
        return NewMerchantFoodListItemAL.size();
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

        final View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_new_merchant_food_item, null);
        } else {
            view = convertView;
        }

        final Holder holder = new Holder();

        holder.swhState = (Switch) view.findViewById(R.id.swhState);
        holder.txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
        holder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        holder.imgFoodDetailedView = (ImageView) view.findViewById(R.id.imgFoodDetailedView);
        holder.imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        holder.imgFoodImg = (ImageView) view.findViewById(R.id.imgFoodImg);
        holder.imgAddToCart = (ImageView) view.findViewById(R.id.imgAddToCart);
        holder.imgVegNonveg = (ImageView) view.findViewById(R.id.imgVegNonveg);

        holder.txtFoodName.setText(NewMerchantFoodListItemAL.get(position).getFoodName());
        holder.txtPrice.setText("₹ " + NewMerchantFoodListItemAL.get(position).getPrice());

        Picasso.with(context).load(NewMerchantFoodListItemAL.get(position).getPhoto()).fit().centerCrop()
                .into(holder.imgFoodImg);


        holder.imgFoodDetailedView.setTag(position);

        if (NewMerchantFoodListItemAL.get(position).getDietType().equals("Vegetarian")) {
            holder.imgVegNonveg.setBackgroundResource(R.drawable.ic_veg);
        } else {
            holder.imgVegNonveg.setBackgroundResource(R.drawable.ic_nonveg);
        }

        if (NewMerchantFoodListItemAL.get(position).getIsActive().equals("1")) {
            holder.swhState.setChecked(true);
        } else {
            holder.swhState.setChecked(false);
        }


        holder.swhState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new FoodStateChangeJSON().execute(NewMerchantFoodListItemAL.get(position).getFoodId(), "1");
                } else {
                    new FoodStateChangeJSON().execute(NewMerchantFoodListItemAL.get(position).getFoodId(), "0");
                }
            }
        });

        holder.imgDelete.setTag(position);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (Integer) v.getTag();

                new AlertDialog.Builder(context).setTitle("Delete ?")
                        .setMessage("Do you want to delete " + NewMerchantFoodListItemAL.get(pos).getFoodName() + " ?").setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                new FoodItemDeleteJSON().execute(NewMerchantFoodListItemAL.get(pos).getFoodId(), NewMerchantFoodListItemAL.get(pos).getFoodName(), Integer.toString(pos));
                            }
                        }).setNegativeButton(android.R.string.no, null).show();


            }
        });

        holder.imgFoodDetailedView.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              ImageView imgFoodImg, imgClose, imgVegNonVeg, imgEdit;
                                                              TextView txtFoodName, txtFoodDesc, txtCuisine, txtCategory, txtType, txtFoodPrice;
                                                              final int pos = (Integer) v.getTag();
                                                              Log.w(" position is ", pos + " ");
                                                              try {
                                                                  LayoutInflater layoutInflater = (LayoutInflater) context
                                                                          .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                                                                  View popupView = layoutInflater.inflate(R.layout.popup_item_detailed_view, null);
                                                                  popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                                                                          ViewGroup.LayoutParams.MATCH_PARENT, true);
                                                                  popupWindow.setOutsideTouchable(true);
                                                                  popupWindow.setFocusable(true);
                                                                  popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                  // popupWindow.setWidth(700);
                                                                  popupWindow.setAnimationStyle(R.style.DialogAnimation);


                                                                  imgFoodImg = (ImageView) popupView.findViewById(R.id.imgFoodImg);
                                                                  imgClose = (ImageView) popupView.findViewById(R.id.imgClose);
                                                                  imgVegNonVeg = (ImageView) popupView.findViewById(R.id.imgVegNonVeg);
                                                                  imgEdit = (ImageView) popupView.findViewById(R.id.imgEdit);


                                                                  txtFoodName = (TextView) popupView.findViewById(R.id.txtFoodName);
                                                                  txtFoodDesc = (TextView) popupView.findViewById(R.id.txtFoodDesc);
                                                                  txtCuisine = (TextView) popupView.findViewById(R.id.txtCuisine);
                                                                  txtType = (TextView) popupView.findViewById(R.id.txtType);
                                                                  txtCategory = (TextView) popupView.findViewById(R.id.txtCategory);
                                                                  txtFoodPrice = (TextView) popupView.findViewById(R.id.txtFoodPrice);


                                                                  popupWindow.showAtLocation(holder.imgFoodDetailedView, Gravity.CENTER, 0, 0);

                                                                  if ((NewMerchantFoodListItemAL.get(pos).getDietType()).equals("Vegetarian")) {
                                                                      imgVegNonVeg.setBackgroundResource(R.drawable.ic_veg);
                                                                  } else {
                                                                      imgVegNonVeg.setBackgroundResource(R.drawable.ic_nonveg);
                                                                  }

                                                                  try {
                                                                      Picasso.with(context).load(NewMerchantFoodListItemAL.get(pos).getPhoto()).fit().centerCrop()
                                                                              .into(imgFoodImg);
//                    Picasso.with(context).load(CustomerFoodMenuItemAL.get(pos).getChefImg()).into(imgChefImg);
                                                                  } catch (Exception e) {
                                                                      e.printStackTrace();
                                                                  }

                                                                  txtFoodName.setText(NewMerchantFoodListItemAL.get(pos).getFoodName());
                                                                  txtFoodDesc.setText(NewMerchantFoodListItemAL.get(pos).getFoodDesc());
                                                                  txtCuisine.setText(NewMerchantFoodListItemAL.get(pos).getCusine());
                                                                  txtCategory.setText(NewMerchantFoodListItemAL.get(pos).getDietType());
                                                                  String Type = NewMerchantFoodListItemAL.get(pos).getItemType();
                                                                  txtType.setText(Type);
                                                                  txtFoodPrice.setText("₹ " + NewMerchantFoodListItemAL.get(pos).getPrice());

                                                                  imgClose.setOnClickListener(new View.OnClickListener() {
                                                                      @Override
                                                                      public void onClick(View v) {
                                                                          popupWindow.dismiss();
                                                                      }
                                                                  });

                                                                  imgEdit.setOnClickListener(new View.OnClickListener() {
                                                                      @Override
                                                                      public void onClick(View v) {
                                                                          Intent intent = new Intent(context, NewMerchantFoodItemEditActivity.class);
                                                                          intent.putExtra("id", NewMerchantFoodListItemAL.get(pos).getFoodId());
                                                                          intent.putExtra("item_name", NewMerchantFoodListItemAL.get(pos).getFoodName());
                                                                          intent.putExtra("item_description", NewMerchantFoodListItemAL.get(pos).getFoodDesc());
                                                                          intent.putExtra("cuisine_name", NewMerchantFoodListItemAL.get(pos).getCusine());
                                                                          intent.putExtra("diet_type_name", NewMerchantFoodListItemAL.get(pos).getDietType());
                                                                          intent.putExtra("diet_type_value", NewMerchantFoodListItemAL.get(pos).getDietTypeValue());

                                                                          Log.w("diet_type_value", NewMerchantFoodListItemAL.get(pos).getDietTypeValue());

                                                                          intent.putExtra("type_name", NewMerchantFoodListItemAL.get(pos).getItemType());
                                                                          intent.putExtra("type_value", NewMerchantFoodListItemAL.get(pos).getItemTypeValue());
                                                                          intent.putExtra("price", NewMerchantFoodListItemAL.get(pos).getPrice());
                                                                          intent.putExtra("pic", NewMerchantFoodListItemAL.get(pos).getPhoto());
                                                                          context.startActivity(intent);
                                                                          popupWindow.dismiss();
                                                                      }
                                                                  });
                                                              } catch (Exception e) {
                                                                  e.printStackTrace();
                                                                  Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();

                                                              }
                                                          }
                                                      }
        );

        holder.imgAddToCart.setTag(position);
        holder.imgAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (Integer)v.getTag();


                //check the current state before we display the screen
                if( holder.swhState.isChecked()) {


                    ImageView imgFoodImg, imgClose, imgVegNonVeg;
                    Button imgUpdate;
                    TextView txtFoodName, txtFoodDesc, txtCuisine, txtType, txtFoodPrice, txtRating;
                    final EditText etQperOrder, etTotQty;
                    RelativeLayout lttslot;
                    LinearLayout ll_availableon;

                    try {
                        LayoutInflater layoutInflater = (LayoutInflater) context
                                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                        View popupView = layoutInflater.inflate(R.layout.popup_item_add_to_daily_menu, null);
                        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT, true);
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.setFocusable(true);
                        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        // popupWindow.setWidth(700);
                        popupWindow.setAnimationStyle(R.style.DialogAnimation);

                        popupWindow.showAtLocation(holder.imgAddToCart, Gravity.CENTER, 0, 0);

                        imgFoodImg = (ImageView) popupView.findViewById(R.id.imgFoodImg);
                        imgClose = (ImageView) popupView.findViewById(R.id.imgClose);
                        imgVegNonVeg = (ImageView) popupView.findViewById(R.id.imgVegNonVeg);
                        imgUpdate = (Button) popupView.findViewById(R.id.imgUpdate);


                        txtFoodName = (TextView) popupView.findViewById(R.id.txtFoodName);
                        txtFoodDesc = (TextView) popupView.findViewById(R.id.txtFoodDesc);
                        txtCuisine = (TextView) popupView.findViewById(R.id.txtCuisine);
                        txtType = (TextView) popupView.findViewById(R.id.txtType);
                        txtFoodPrice = (TextView) popupView.findViewById(R.id.txtFoodPrice);
                        txtRating = (TextView) popupView.findViewById(R.id.txtRating);
                        lttslot = (RelativeLayout) popupView.findViewById(R.id.lttslot);
                        ll_availableon = (LinearLayout) popupView.findViewById(R.id.ll_availableon);

                        etQperOrder = (EditText) popupView.findViewById(R.id.etQperOrder);
                        etTotQty = (EditText) popupView.findViewById(R.id.etTotQty);
                        tAvlon = (TextView) popupView.findViewById(R.id.tAvlon);
                        tTimeSlot = (TextView) popupView.findViewById(R.id.tTimeSlot);


                        lttslot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initiatePopUp1_timeslot();
                            }
                        });

                        ll_availableon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initiatePopUp2_availableOn();
                            }
                        });

                        popupWindow.showAtLocation(holder.imgAddToCart, Gravity.CENTER, 0, 0);

                        if ((NewMerchantFoodListItemAL.get(pos).getDietType()).equals("Vegetarian")) {
                            imgVegNonVeg.setBackgroundResource(R.drawable.ic_veg);
                        } else {
                            imgVegNonVeg.setBackgroundResource(R.drawable.ic_nonveg);
                        }

                        try {
                            Picasso.with(context).load(NewMerchantFoodListItemAL.get(pos).getPhoto()).fit().centerCrop()
                                    .into(imgFoodImg);
//                    Picasso.with(context).load(CustomerFoodMenuItemAL.get(pos).getChefImg()).into(imgChefImg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        txtFoodName.setText(NewMerchantFoodListItemAL.get(pos).getFoodName());
                        txtFoodDesc.setText(NewMerchantFoodListItemAL.get(pos).getFoodDesc());
                        txtCuisine.setText(NewMerchantFoodListItemAL.get(pos).getCusine());

                        String Type = NewMerchantFoodListItemAL.get(pos).getDietType();
                        txtType.setText(Type);
                        txtFoodPrice.setText("₹ " + NewMerchantFoodListItemAL.get(pos).getPrice());
                        txtRating.setText("");//NewMerchantFoodListItemAL.get(pos).getRating());

                        imgUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new UpdateFoodItemDailyMenuJSON().execute(NewMerchantFoodListItemAL.get(pos).getFoodId(), String.valueOf(time_slot_value),
                                        etTotQty.getText().toString(), etQperOrder.getText().toString(), date_value);
                            }
                        });

                        imgClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context,"To add this item, you need to enable it first." , Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }



  //to display timeslot
    private void initiatePopUp1_timeslot()

    {
         final Dialog dialog ;
        final TextView item_timeslot1,item_timeslot2,item_timeslot3,item_timeslot4;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.item_new_merchant_food_item_timeslot);
        final int screenWidth = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        final int screenHeight = dialog.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        dialog.getWindow().setLayout(screenWidth,screenHeight/3);
        dialog.setCancelable(true);



        HashMap<String,String>  hm_tt = new HashMap<String,String>();

        hm_tt = NewMerchantFoodItemFragment.hm_timeslot;


        item_timeslot1 = (TextView)dialog.findViewById(R.id.item_timeslot1);
        item_timeslot2 = (TextView)dialog.findViewById(R.id.item_timeslot2);
        item_timeslot3 = (TextView)dialog.findViewById(R.id.item_timeslot3);
        item_timeslot4 = (TextView)dialog.findViewById(R.id.item_timeslot4);

        item_timeslot1.setText(hm_tt.get("t1"));
        item_timeslot2.setText(hm_tt.get("t2"));
        item_timeslot3.setText(hm_tt.get("t3"));
        item_timeslot4.setText(hm_tt.get("t4"));

        item_timeslot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tTimeSlot.setText(item_timeslot1.getText().toString());
                time_slot_value = 1;
                dialog.dismiss();

            }
        });

        item_timeslot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tTimeSlot.setText(item_timeslot2.getText().toString());
                time_slot_value = 2;
                dialog.dismiss();
            }
        });

        item_timeslot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tTimeSlot.setText(item_timeslot3.getText().toString());
                time_slot_value = 3;
                dialog.dismiss();
            }
        });

        item_timeslot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tTimeSlot.setText(item_timeslot4.getText().toString());
                time_slot_value =4;
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    //to display available on

    private void initiatePopUp2_availableOn()
    {
        final Dialog dialog ;
        final TextView item_timeslot1,item_timeslot2,item_timeslot3,item_timeslot4;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.item_new_merchant_food_item_timeslot);
        final int screenWidth = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        final int screenHeight = dialog.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        dialog.getWindow().setLayout(screenWidth,screenHeight/3);
        dialog.setCancelable(true);






        item_timeslot1 = (TextView)dialog.findViewById(R.id.item_timeslot1);
        item_timeslot2 = (TextView)dialog.findViewById(R.id.item_timeslot2);
        item_timeslot3 = (TextView)dialog.findViewById(R.id.item_timeslot3);
        item_timeslot4 = (TextView)dialog.findViewById(R.id.item_timeslot4);


        final Date today = new Date();
        final Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        final Date dayAftertomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24) + (1000 * 60 * 60 * 24));
        final Date dayAftertomorrow1 = new Date(today.getTime() + (1000 * 60 * 60 * 24) + (1000 * 60 * 60 * 24) + (1000 * 60 * 60 * 24));

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
        final SimpleDateFormat simpleDateformat1 = new SimpleDateFormat("MM/dd/yy");

        String Today,DayTmrw, DayAfterTmrw, DayAfterTmrw1;

        Today  = simpleDateformat.format(today);
        DayTmrw = simpleDateformat.format(tomorrow);
        DayAfterTmrw = simpleDateformat.format(dayAftertomorrow);
        DayAfterTmrw1 = simpleDateformat.format(dayAftertomorrow1);

        item_timeslot1.setText(Today  + "  " + new SimpleDateFormat("dd-MM-yyyy").format(today));
        item_timeslot2.setText(DayTmrw + "  " + new SimpleDateFormat("dd-MM-yyyy").format(tomorrow));
        item_timeslot3.setText(DayAfterTmrw + "  " + new SimpleDateFormat("dd-MM-yyyy").format(dayAftertomorrow));
        item_timeslot4.setText(DayAfterTmrw1 + "  " + new SimpleDateFormat("dd-MM-yyyy").format(dayAftertomorrow1));

        item_timeslot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tAvlon.setText(item_timeslot1.getText().toString());
                date_value = new SimpleDateFormat("yy-MM-dd").format(today);
                dialog.dismiss();

            }
        });

        item_timeslot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tAvlon.setText(item_timeslot2.getText().toString());
                date_value = new SimpleDateFormat("yy-MM-dd").format(tomorrow);
                dialog.dismiss();
            }
        });

        item_timeslot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tAvlon.setText(item_timeslot3.getText().toString());
                date_value = new SimpleDateFormat("yy-MM-dd").format(dayAftertomorrow);
                dialog.dismiss();
            }
        });

        item_timeslot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tAvlon.setText(item_timeslot4.getText().toString());
                date_value = new SimpleDateFormat("yy-MM-dd").format(dayAftertomorrow1);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    class Holder {
        Switch swhState;
        TextView txtFoodName, txtPrice;
        ImageView imgFoodDetailedView, imgDelete, imgFoodImg, imgAddToCart, imgVegNonveg;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return NewMerchantFoodListItemAL.size();
    }

    public class FoodStateChangeJSON extends AsyncTask<String, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status, id, state;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                id = params[0];
                state = params[1];

                Log.w("id is " , id + " ");

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("id", id));
                nameValuePairs.add(new BasicNameValuePair("active", state));

                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.NEW_MERCHANT_FOOD_ITEMS_STATE_CHANGE);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                status = objMain.getString("success");

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
                Toast.makeText(context, "Status Changed.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Oops! Try again later...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class FoodItemDeleteJSON extends AsyncTask<String, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status, id, ItemName, pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                id = params[0];
                ItemName = params[1];
                pos = params[2];

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("id", id));

                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.NEW_MERCHANT_FOOD_ITEMS_DELETE);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                status = objMain.getString("success");

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
                Toast.makeText(context, ItemName + " deleted.", Toast.LENGTH_SHORT).show();
                NewMerchantFoodListItemAL.remove(Integer.parseInt(pos));
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Oops! Try again later...", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public class UpdateFoodItemDailyMenuJSON extends AsyncTask<String, Integer, Void> {




        String responseBody;
        HttpPost httppost;
        String status;
        String msg;

        String ItemId, TimeSlotId, Quantity, QtyPerOrder, Date;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {


                ItemId = params[0];
                TimeSlotId = params[1];
                Quantity = params[2];
                QtyPerOrder = params[3];
                Date = params[4];
                Log.w("add to daily menu ", " ItemId= " + ItemId + "  , TimeSlotId="
                        + TimeSlotId + " ,Quantity =  " + Quantity + "  , QtyPerOrder = " + QtyPerOrder + "  ,Date =  " + Date);


                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("item_id", ItemId));
                nameValuePairs.add(new BasicNameValuePair("time_slot", TimeSlotId));
                nameValuePairs.add(new BasicNameValuePair("quantity", Quantity));
                nameValuePairs.add(new BasicNameValuePair("quantity_per_order", QtyPerOrder));
                nameValuePairs.add(new BasicNameValuePair("date", Date));

                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.MERCHANT_FOOD_ITEM_ADD_TO_DAILY_MENU);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                Log.w("responsebody" , responseBody.toString());
                status = objMain.getString("success");

                if (status.equals("true")) {
                }
                else if (status.equals("false"))
                {
                   /* JSONObject jsonObjErrors = objMain.optJSONObject("errors");
                     if(jsonObjErrors.getString("quantity")!=null)
                     {
                         msg = jsonObjErrors.getString("quantity");
                     }
                    else if(jsonObjErrors.getString("quantity_per_order")!=null)
                    {
                        msg = jsonObjErrors.getString("quantity_per_order");
                    }*/
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
                Toast.makeText(context, "Updated...", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

            } else {
                Toast.makeText(context, "Oops ...Try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
