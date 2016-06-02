package com.callndata.folivesservices;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapters.CustomerFoodMenuAdapter;
import com.callndata.adapters.ExpandableListAdapter;
import com.callndata.adapters.FoodCartAdapter;
import com.callndata.others.AppConstants;
import com.callndata.others.DataBaseHandler;
import com.callndata.others.HorizontalPager;
import com.example.folivesservices.R;
import com.folives.item.CustomerFoodMenuItem;
import com.folives.item.FoodCartCheckout;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CustomerFoodItemFragment extends FragmentActivity {

    public static Activity MyActivityFlag;

    RelativeLayout rlMidnight, rlBreakfast, rlLunch, rlDinner, parentLayout;
    TextView txtMidnight, txtMidNightTiming, txtBreakfast, txtBreakfastTiming, txtLunch, txtLunchTiming, txtDinner,
            txtDinnerTiming, txtDate;
    LinearLayout llMNLine, llBFLine, llLLine, llDLine, llDate;
    ListView lstFoodItem;
    private int MAX_ROWS = 20;
    HorizontalPager horizontalView;

    ProgressDialog pDialog, pDialog1;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    CustomerFoodMenuItem CFMItem;
    CustomerFoodMenuAdapter CFMadapter;
    ArrayList<CustomerFoodMenuItem> CustomerFoodMenuItemMidNightAL = new ArrayList<CustomerFoodMenuItem>();
    ArrayList<CustomerFoodMenuItem> CustomerFoodMenuItemBreakfastAL = new ArrayList<CustomerFoodMenuItem>();
    ArrayList<CustomerFoodMenuItem> CustomerFoodMenuItemLunchAL = new ArrayList<CustomerFoodMenuItem>();
    ArrayList<CustomerFoodMenuItem> CustomerFoodMenuItemDinnerAL = new ArrayList<CustomerFoodMenuItem>();

    LinearLayout llSlideView;
    private int slideViewHeight;
    private boolean isShowingBox;

    String PinCode;
    EditText etPinCode;
    ImageView imgPinCodeSubmit, imgFilter;
    LinearLayout llPlace;
    TextView txtPlace;

    SharedPreferences prefs;
    LinearLayout llTdoay, llTomorrow, llDayAfterTmrw, llDayAfterTmrw1, llBlackScreen, llTdyLine, llTmrwLine, llDATLine, llDAT1Line;
    TextView txtTodayDate, txtTomorrowDate, txtDayAfterTmrwDate, txtDayAfterTmrwDate1, txtToday, txtTomorrow, txtDayAfterTmrw, txtDayAfterTmrw1;

    ArrayList<String> CustomerFoodFilterHeader = new ArrayList<String>();
    HashMap<String, List<String>> CustomerFoodFilterHeadersChild = new HashMap<String, List<String>>();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;

    public static TextView txtPlaceDialog;
    private static TextView txtCartProCount, txtTotalPrice;
    ImageView imgDialogPinCodeSubmit, imgCart, imgClose;
    RelativeLayout rlCartProCount;
    TextView txtTPrice;

    DataBaseHandler db;
    FoodCartAdapter FCAdapter;
    ListView lstCartFood;

    FoodCartCheckout FoodCartCheckoutItem;
    ArrayList<FoodCartCheckout> FoodCartCheckoutAL = new ArrayList<FoodCartCheckout>();

    HashMap<String, String> CartHM = new HashMap<String, String>();
    public static ListView lstMN, lstBF, lstLN, lstDN;

    CustomerFoodMenuAdapter adapterCartMaintainMN, adapterCartMaintainBF, adapterCartMaintainLN, adapterCartMaintainDN;

    int dateSelectedBoolean = 1;

    JSONObject CartObj = new JSONObject();
    JSONArray CartArr = new JSONArray();
    String CartStr;

    PopupWindow popupWindow;
    HashMap<String, String> DeliveryTimeSlot = new HashMap<String, String>();

    String SelectedAddress;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_food_items);

        MyActivityFlag = this;

        AppConstants.HomeFlag = "1";
        SelectedAddress = getIntent().getExtras().getString("SelectedAddress");

        prefs = CustomerFoodItemFragment.this.getSharedPreferences("myPrefs", CustomerFoodItemFragment.this.MODE_PRIVATE);
        db = new DataBaseHandler(CustomerFoodItemFragment.this);
        db.open();
        db.deleteAllRecords();
        db.close();

        lstMN = new ListView(CustomerFoodItemFragment.this);
        lstBF = new ListView(CustomerFoodItemFragment.this);
        lstLN = new ListView(CustomerFoodItemFragment.this);
        lstDN = new ListView(CustomerFoodItemFragment.this);

        rlMidnight = (RelativeLayout) findViewById(R.id.rlMidnight);
        rlBreakfast = (RelativeLayout) findViewById(R.id.rlBreakfast);
        rlLunch = (RelativeLayout) findViewById(R.id.rlLunch);
        rlDinner = (RelativeLayout) findViewById(R.id.rlDinner);
        rlCartProCount = (RelativeLayout) findViewById(R.id.rlCartProCount);

        llMNLine = (LinearLayout) findViewById(R.id.llMNLine);
        llBFLine = (LinearLayout) findViewById(R.id.llBFLine);
        llLLine = (LinearLayout) findViewById(R.id.llLLine);
        llDLine = (LinearLayout) findViewById(R.id.llDLine);
        llDate = (LinearLayout) findViewById(R.id.llDate);

        txtMidnight = (TextView) findViewById(R.id.txtMidnight);
        txtMidNightTiming = (TextView) findViewById(R.id.txtMidNightTiming);

        txtBreakfast = (TextView) findViewById(R.id.txtBreakfast);
        txtBreakfastTiming = (TextView) findViewById(R.id.txtBreakfastTiming);

        txtLunch = (TextView) findViewById(R.id.txtLunch);
        txtLunchTiming = (TextView) findViewById(R.id.txtLunchTiming);

        txtDinner = (TextView) findViewById(R.id.txtDinner);
        txtDinnerTiming = (TextView) findViewById(R.id.txtDinnerTiming);

        horizontalView = (HorizontalPager) findViewById(R.id.horizontal_pager);
        horizontalView.setCurrentScreen(1, true);

        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        llSlideView = (LinearLayout) findViewById(R.id.llSlideView);
        llBlackScreen = (LinearLayout) findViewById(R.id.llBlackScreen);

        txtDate = (TextView) findViewById(R.id.txtDate);

        etPinCode = (EditText) findViewById(R.id.etPinCode);
        imgPinCodeSubmit = (ImageView) findViewById(R.id.imgPinCodeSubmit);
        llPlace = (LinearLayout) findViewById(R.id.llPlace);
        txtPlace = (TextView) findViewById(R.id.txtPlace);

        imgFilter = (ImageView) findViewById(R.id.imgFilter);
        imgCart = (ImageView) findViewById(R.id.imgCart);

        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        txtCartProCount = (TextView) findViewById(R.id.txtCartProCount);

        imgBack = (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                Cursor c = db.fetchAllData();
                if (c.getCount() > 0) {
                    new AlertDialog.Builder(CustomerFoodItemFragment.this).setTitle("Cancel Order ?")
                            .setMessage("\nOrder will be cancel.\nDo you want to proceed?\n").setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
                                }
                            }).setNegativeButton(android.R.string.no, null).show();
                } else {
                    finish();
                }
                db.close();
            }
        });

        rlMidnight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // txtMidnight.setAlpha(100);
                txtMidNightTiming.setVisibility(View.VISIBLE);
                txtBreakfastTiming.setVisibility(View.INVISIBLE);
                txtLunchTiming.setVisibility(View.INVISIBLE);
                txtDinnerTiming.setVisibility(View.INVISIBLE);

                llMNLine.setBackgroundColor(getResources().getColor(R.color.white));
                llBFLine.setBackgroundResource(0);
                llLLine.setBackgroundResource(0);
                llDLine.setBackgroundResource(0);

                int textViewColor = txtMidnight.getTextColors().getDefaultColor();
                txtMidnight.setTextColor(Color.argb(500, Color.red(textViewColor), Color.green(textViewColor),
                        Color.blue(textViewColor)));

                int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
                txtBreakfast.setTextColor(Color.argb(128, Color.red(textViewColor1), Color.green(textViewColor1),
                        Color.blue(textViewColor1)));

                int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
                txtLunch.setTextColor(Color.argb(128, Color.red(textViewColor2), Color.green(textViewColor2),
                        Color.blue(textViewColor2)));

                int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
                txtDinner.setTextColor(Color.argb(128, Color.red(textViewColor3), Color.green(textViewColor3),
                        Color.blue(textViewColor3)));

                horizontalView.setCurrentScreen(0, true);

            }
        });

        rlBreakfast.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // txtMidnight.setAlpha(100);
                txtMidNightTiming.setVisibility(View.INVISIBLE);
                txtBreakfastTiming.setVisibility(View.VISIBLE);
                txtLunchTiming.setVisibility(View.INVISIBLE);
                txtDinnerTiming.setVisibility(View.INVISIBLE);

                llMNLine.setBackgroundResource(0);
                llBFLine.setBackgroundColor(getResources().getColor(R.color.white));
                llLLine.setBackgroundResource(0);
                llDLine.setBackgroundResource(0);

                int textViewColor = txtMidnight.getTextColors().getDefaultColor();
                txtMidnight.setTextColor(Color.argb(128, Color.red(textViewColor), Color.green(textViewColor),
                        Color.blue(textViewColor)));

                int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
                txtBreakfast.setTextColor(Color.argb(500, Color.red(textViewColor1), Color.green(textViewColor1),
                        Color.blue(textViewColor1)));

                int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
                txtLunch.setTextColor(Color.argb(128, Color.red(textViewColor2), Color.green(textViewColor2),
                        Color.blue(textViewColor2)));

                int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
                txtDinner.setTextColor(Color.argb(128, Color.red(textViewColor3), Color.green(textViewColor3),
                        Color.blue(textViewColor3)));

                horizontalView.setCurrentScreen(1, true);
            }
        });

        rlLunch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // txtMidnight.setAlpha(100);
                txtMidNightTiming.setVisibility(View.INVISIBLE);
                txtBreakfastTiming.setVisibility(View.INVISIBLE);
                txtLunchTiming.setVisibility(View.VISIBLE);
                txtDinnerTiming.setVisibility(View.INVISIBLE);

                llMNLine.setBackgroundResource(0);
                llBFLine.setBackgroundResource(0);
                llLLine.setBackgroundColor(getResources().getColor(R.color.white));
                llDLine.setBackgroundResource(0);

                int textViewColor = txtMidnight.getTextColors().getDefaultColor();
                txtMidnight.setTextColor(Color.argb(128, Color.red(textViewColor), Color.green(textViewColor),
                        Color.blue(textViewColor)));

                int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
                txtBreakfast.setTextColor(Color.argb(128, Color.red(textViewColor1), Color.green(textViewColor1),
                        Color.blue(textViewColor1)));

                int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
                txtLunch.setTextColor(Color.argb(500, Color.red(textViewColor2), Color.green(textViewColor2),
                        Color.blue(textViewColor2)));

                int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
                txtDinner.setTextColor(Color.argb(128, Color.red(textViewColor3), Color.green(textViewColor3),
                        Color.blue(textViewColor3)));

                horizontalView.setCurrentScreen(2, true);
            }
        });

        rlDinner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // txtMidnight.setAlpha(100);
                txtMidNightTiming.setVisibility(View.INVISIBLE);
                txtBreakfastTiming.setVisibility(View.INVISIBLE);
                txtLunchTiming.setVisibility(View.INVISIBLE);
                txtDinnerTiming.setVisibility(View.VISIBLE);

                llMNLine.setBackgroundResource(0);
                llBFLine.setBackgroundResource(0);
                llLLine.setBackgroundResource(0);
                llDLine.setBackgroundColor(getResources().getColor(R.color.white));

                int textViewColor = txtMidnight.getTextColors().getDefaultColor();
                txtMidnight.setTextColor(Color.argb(128, Color.red(textViewColor), Color.green(textViewColor),
                        Color.blue(textViewColor)));

                int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
                txtBreakfast.setTextColor(Color.argb(128, Color.red(textViewColor1), Color.green(textViewColor1),
                        Color.blue(textViewColor1)));

                int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
                txtLunch.setTextColor(Color.argb(128, Color.red(textViewColor2), Color.green(textViewColor2),
                        Color.blue(textViewColor2)));

                int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
                txtDinner.setTextColor(Color.argb(500, Color.red(textViewColor3), Color.green(textViewColor3),
                        Color.blue(textViewColor3)));

                horizontalView.setCurrentScreen(3, true);
            }
        });

        horizontalView.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener() {

            @Override
            public void onScreenSwitched(int screen) {
                if (screen == 0) {
                    //makes timing visible or invisible
                    txtMidNightTiming.setVisibility(View.VISIBLE);
                    txtBreakfastTiming.setVisibility(View.INVISIBLE);
                    txtLunchTiming.setVisibility(View.INVISIBLE);
                    txtDinnerTiming.setVisibility(View.INVISIBLE);

                    //makes the underline visible or invisible
                    llMNLine.setBackgroundColor(getResources().getColor(R.color.white));
                    llBFLine.setBackgroundResource(0);
                    llLLine.setBackgroundResource(0);
                    llDLine.setBackgroundResource(0);

                    int textViewColor = txtMidnight.getTextColors().getDefaultColor();
                    txtMidnight.setTextColor(Color.argb(500, Color.red(textViewColor), Color.green(textViewColor),
                            Color.blue(textViewColor)));

                    int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
                    txtBreakfast.setTextColor(Color.argb(128, Color.red(textViewColor1), Color.green(textViewColor1),
                            Color.blue(textViewColor1)));

                    int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
                    txtLunch.setTextColor(Color.argb(128, Color.red(textViewColor2), Color.green(textViewColor2),
                            Color.blue(textViewColor2)));

                    int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
                    txtDinner.setTextColor(Color.argb(128, Color.red(textViewColor3), Color.green(textViewColor3),
                            Color.blue(textViewColor3)));

                } else if (screen == 1) {
                    txtMidNightTiming.setVisibility(View.INVISIBLE);
                    txtBreakfastTiming.setVisibility(View.VISIBLE);
                    txtLunchTiming.setVisibility(View.INVISIBLE);
                    txtDinnerTiming.setVisibility(View.INVISIBLE);

                    llMNLine.setBackgroundResource(0);
                    llBFLine.setBackgroundColor(getResources().getColor(R.color.white));
                    llLLine.setBackgroundResource(0);
                    llDLine.setBackgroundResource(0);

                    int textViewColor = txtMidnight.getTextColors().getDefaultColor();
                    txtMidnight.setTextColor(Color.argb(128, Color.red(textViewColor), Color.green(textViewColor),
                            Color.blue(textViewColor)));

                    int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
                    txtBreakfast.setTextColor(Color.argb(500, Color.red(textViewColor1), Color.green(textViewColor1),
                            Color.blue(textViewColor1)));

                    int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
                    txtLunch.setTextColor(Color.argb(128, Color.red(textViewColor2), Color.green(textViewColor2),
                            Color.blue(textViewColor2)));

                    int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
                    txtDinner.setTextColor(Color.argb(128, Color.red(textViewColor3), Color.green(textViewColor3),
                            Color.blue(textViewColor3)));

                } else if (screen == 2) {
                    txtMidNightTiming.setVisibility(View.INVISIBLE);
                    txtBreakfastTiming.setVisibility(View.INVISIBLE);
                    txtLunchTiming.setVisibility(View.VISIBLE);
                    txtDinnerTiming.setVisibility(View.INVISIBLE);

                    llMNLine.setBackgroundResource(0);
                    llBFLine.setBackgroundResource(0);
                    llLLine.setBackgroundColor(getResources().getColor(R.color.white));
                    llDLine.setBackgroundResource(0);

                    int textViewColor = txtMidnight.getTextColors().getDefaultColor();
                    txtMidnight.setTextColor(Color.argb(128, Color.red(textViewColor), Color.green(textViewColor),
                            Color.blue(textViewColor)));

                    int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
                    txtBreakfast.setTextColor(Color.argb(128, Color.red(textViewColor1), Color.green(textViewColor1),
                            Color.blue(textViewColor1)));

                    int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
                    txtLunch.setTextColor(Color.argb(500, Color.red(textViewColor2), Color.green(textViewColor2),
                            Color.blue(textViewColor2)));

                    int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
                    txtDinner.setTextColor(Color.argb(128, Color.red(textViewColor3), Color.green(textViewColor3),
                            Color.blue(textViewColor3)));

                } else if (screen == 3) {
                    txtMidNightTiming.setVisibility(View.INVISIBLE);
                    txtBreakfastTiming.setVisibility(View.INVISIBLE);
                    txtLunchTiming.setVisibility(View.INVISIBLE);
                    txtDinnerTiming.setVisibility(View.VISIBLE);

                    llMNLine.setBackgroundResource(0);
                    llBFLine.setBackgroundResource(0);
                    llLLine.setBackgroundResource(0);
                    llDLine.setBackgroundColor(getResources().getColor(R.color.white));

                    int textViewColor = txtMidnight.getTextColors().getDefaultColor();
                    txtMidnight.setTextColor(Color.argb(128, Color.red(textViewColor), Color.green(textViewColor),
                            Color.blue(textViewColor)));

                    int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
                    txtBreakfast.setTextColor(Color.argb(128, Color.red(textViewColor1), Color.green(textViewColor1),
                            Color.blue(textViewColor1)));

                    int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
                    txtLunch.setTextColor(Color.argb(128, Color.red(textViewColor2), Color.green(textViewColor2),
                            Color.blue(textViewColor2)));

                    int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
                    txtDinner.setTextColor(Color.argb(500, Color.red(textViewColor3), Color.green(textViewColor3),
                            Color.blue(textViewColor3)));

                }

            }
        });

        etPinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerFoodItemFragment.this, PinCodeActivity.class);
                startActivity(intent);
            }
        });

        imgPinCodeSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isShowingBox = false;
                slideOut(500, 0);

                SharedPreferences.Editor edt = prefs.edit();
                edt.putString("pincode", AppConstants.Customer_Pincode);
                edt.commit();

                String pin = prefs.getString("pincode", "400053");
                pin = ("<b>" + pin.substring(7, pin.length()) + "</b><br>" + pin.substring(0, 6)).replace("-", "");
                txtPlace.setText(Html.fromHtml(pin));

                Date todayDate = new Date();
                new CustomerFavouriteJSON().execute(new SimpleDateFormat("dd-MM-yy").format(todayDate));
            }
        });

        final ViewTreeObserver vto = parentLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < 16) {
                    parentLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    parentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                slideViewHeight = llSlideView.getHeight();

            }
        });

        llPlace.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // llSlideView.setVisibility(View.VISIBLE);
                // etPinCode.setText(prefs.getString("pincode", "Place"));
                // clickHandler();

                try {
                    LayoutInflater layoutInflater = (LayoutInflater) CustomerFoodItemFragment.this
                            .getSystemService(CustomerFoodItemFragment.this.LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.dialog_places, null);
                    final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    txtPlaceDialog = (TextView) popupView.findViewById(R.id.txtPlaceDialog);
                    imgDialogPinCodeSubmit = (ImageView) popupView.findViewById(R.id.imgDialogPinCodeSubmit);

                    // txtPlaceDialog.setText(prefs.getString("pincode",
                    // "Place"));
                    txtPlaceDialog.setText(txtPlace.getText().toString());

                    // popupWindow.setWidth(700);
                    popupWindow.setAnimationStyle(R.style.DialogAnimation);

                    if (!CustomerFoodItemFragment.this.isFinishing()) {
                        popupWindow.showAsDropDown(llPlace, -10, 10);
                    }
                    llBlackScreen.setVisibility(View.VISIBLE);

                    txtPlaceDialog.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(CustomerFoodItemFragment.this, PinCodeActivity.class);
                            startActivity(intent);
                            popupWindow.dismiss();
                        }
                    });

                    imgDialogPinCodeSubmit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            llBlackScreen.setVisibility(View.GONE);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CustomerFoodItemFragment.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        llDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Date today = new Date();
                final Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
                final Date dayAftertomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24) + (1000 * 60 * 60 * 24));
                final Date dayAftertomorrow1 = new Date(today.getTime() + (1000 * 60 * 60 * 24) + (1000 * 60 * 60 * 24) + (1000 * 60 * 60 * 24));

                SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
                final SimpleDateFormat simpleDateformat1 = new SimpleDateFormat("MM/dd/yy");

                String DayTmrw, DayAfterTmrw, DayAfterTmrw1;

                DayTmrw = simpleDateformat.format(tomorrow);
                DayAfterTmrw = simpleDateformat.format(dayAftertomorrow);
                DayAfterTmrw1 = simpleDateformat.format(dayAftertomorrow1);

                LayoutInflater layoutInflater = (LayoutInflater) CustomerFoodItemFragment.this
                        .getSystemService(CustomerFoodItemFragment.this.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.dialog_next_three_days, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                llTdoay = (LinearLayout) popupView.findViewById(R.id.llTdoay);
                llTomorrow = (LinearLayout) popupView.findViewById(R.id.llTomorrow);
                llDayAfterTmrw = (LinearLayout) popupView.findViewById(R.id.llDayAfterTmrw);
                llDayAfterTmrw1 = (LinearLayout) popupView.findViewById(R.id.llDayAfterTmrw1);

                txtTodayDate = (TextView) popupView.findViewById(R.id.txtTodayDate);
                txtTomorrowDate = (TextView) popupView.findViewById(R.id.txtTomorrowDate);
                txtDayAfterTmrwDate = (TextView) popupView.findViewById(R.id.txtDayAfterTmrwDate);
                txtDayAfterTmrwDate1 = (TextView) popupView.findViewById(R.id.txtDayAfterTmrwDate1);

                txtToday = (TextView) popupView.findViewById(R.id.txtToday);
                txtTomorrow = (TextView) popupView.findViewById(R.id.txtTomorrow);
                txtDayAfterTmrw = (TextView) popupView.findViewById(R.id.txtDayAfterTmrw);
                txtDayAfterTmrw1 = (TextView) popupView.findViewById(R.id.txtDayAfterTmrw1);

                llTdyLine = (LinearLayout) popupView.findViewById(R.id.llTdyLine);
                llTmrwLine = (LinearLayout) popupView.findViewById(R.id.llTmrwLine);
                llDATLine = (LinearLayout) popupView.findViewById(R.id.llDATLine);
                llDAT1Line = (LinearLayout) popupView.findViewById(R.id.llDATLine1);

                txtTodayDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(today));
                txtTomorrowDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(tomorrow));
                txtDayAfterTmrwDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(dayAftertomorrow));
                txtDayAfterTmrwDate1.setText(new SimpleDateFormat("dd-MM-yyyy").format(dayAftertomorrow1));

                txtTomorrow.setText(DayTmrw);
                txtDayAfterTmrw.setText(DayAfterTmrw);
                txtDayAfterTmrw1.setText(DayAfterTmrw1);

                popupWindow.setAnimationStyle(R.style.DialogAnimation);
                popupWindow.showAsDropDown(llDate, -10, 10);
                llBlackScreen.setVisibility(View.VISIBLE);

                if (dateSelectedBoolean == 1) {

                    llTdyLine.setVisibility(View.VISIBLE);
                    llTmrwLine.setVisibility(View.INVISIBLE);
                    llDATLine.setVisibility(View.INVISIBLE);
                    llDAT1Line.setVisibility(View.INVISIBLE);

                } else if (dateSelectedBoolean == 2) {

                    llTdyLine.setVisibility(View.INVISIBLE);
                    llTmrwLine.setVisibility(View.VISIBLE);
                    llDATLine.setVisibility(View.INVISIBLE);
                    llDAT1Line.setVisibility(View.INVISIBLE);

                } else if (dateSelectedBoolean == 3) {

                    llTdyLine.setVisibility(View.INVISIBLE);
                    llTmrwLine.setVisibility(View.INVISIBLE);
                    llDATLine.setVisibility(View.VISIBLE);
                    llDAT1Line.setVisibility(View.INVISIBLE);

                } else if (dateSelectedBoolean == 4) {

                    llTdyLine.setVisibility(View.INVISIBLE);
                    llTmrwLine.setVisibility(View.INVISIBLE);
                    llDATLine.setVisibility(View.INVISIBLE);
                    llDAT1Line.setVisibility(View.VISIBLE);

                }

                llTdoay.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dateSelectedBoolean = 1;

//                        llTdoay.setBackground(getResources().getDrawable(R.drawable.customborder));
//                        llTomorrow.setBackground(getResources().getDrawable(R.drawable.customblackborder));
//                        llDayAfterTmrw.setBackground(getResources().getDrawable(R.drawable.customblackborder));

//                        txtToday.setTextColor(getResources().getColor(R.color.red));
//                        txtTomorrow.setTextColor(getResources().getColor(R.color.border_color));
//                        txtDayAfterTmrw.setTextColor(getResources().getColor(R.color.border_color));
//                        txtDayAfterTmrw1.setTextColor(getResources().getColor(R.color.border_color));
//
//                        txtTodayDate.setTextColor(getResources().getColor(R.color.red));
//                        txtTomorrowDate.setTextColor(getResources().getColor(R.color.border_color));
//                        txtDayAfterTmrwDate.setTextColor(getResources().getColor(R.color.border_color));
//                        txtDayAfterTmrwDate1.setTextColor(getResources().getColor(R.color.border_color));

                        llTdyLine.setVisibility(View.VISIBLE);
                        llTmrwLine.setVisibility(View.INVISIBLE);
                        llDATLine.setVisibility(View.INVISIBLE);
                        llDAT1Line.setVisibility(View.INVISIBLE);

                        txtDate.setText(txtTodayDate.getText().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        try {

                            Date date = formatter.parse(txtTodayDate.getText().toString());
                            new CustomerFavouriteJSON().execute(new SimpleDateFormat("dd-MM-yy").format(date).toString());
                            popupWindow.dismiss();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                llTomorrow.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dateSelectedBoolean = 2;

//                        llTdoay.setBackground(getResources().getDrawable(R.drawable.customblackborder));
//                        llTomorrow.setBackground(getResources().getDrawable(R.drawable.customborder));
//                        llDayAfterTmrw.setBackground(getResources().getDrawable(R.drawable.customblackborder));

//                        txtToday.setTextColor(getResources().getColor(R.color.border_color));
//                        txtTomorrow.setTextColor(getResources().getColor(R.color.red));
//                        txtDayAfterTmrw.setTextColor(getResources().getColor(R.color.border_color));
//                        txtDayAfterTmrw1.setTextColor(getResources().getColor(R.color.border_color));
//
//                        txtTodayDate.setTextColor(getResources().getColor(R.color.border_color));
//                        txtTomorrowDate.setTextColor(getResources().getColor(R.color.red));
//                        txtDayAfterTmrwDate.setTextColor(getResources().getColor(R.color.border_color));

                        llTdyLine.setVisibility(View.INVISIBLE);
                        llTmrwLine.setVisibility(View.VISIBLE);
                        llDATLine.setVisibility(View.INVISIBLE);
                        llDAT1Line.setVisibility(View.INVISIBLE);

                        txtDate.setText(txtTomorrowDate.getText().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        try {

                            Date date = formatter.parse(txtTomorrowDate.getText().toString());
                            new CustomerFavouriteJSON().execute(new SimpleDateFormat("dd-MM-yy").format(date).toString());
                            popupWindow.dismiss();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });

                llDayAfterTmrw.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dateSelectedBoolean = 3;

//                        llTdoay.setBackground(getResources().getDrawable(R.drawable.customblackborder));
//                        llTomorrow.setBackground(getResources().getDrawable(R.drawable.customblackborder));
//                        llDayAfterTmrw.setBackground(getResources().getDrawable(R.drawable.customborder));

//                        txtToday.setTextColor(getResources().getColor(R.color.border_color));
//                        txtTomorrow.setTextColor(getResources().getColor(R.color.border_color));
//                        txtDayAfterTmrw.setTextColor(getResources().getColor(R.color.red));
//                        txtDayAfterTmrw1.setTextColor(getResources().getColor(R.color.border_color));
//
//
//                        txtTodayDate.setTextColor(getResources().getColor(R.color.border_color));
//                        txtTomorrowDate.setTextColor(getResources().getColor(R.color.border_color));
//                        txtDayAfterTmrwDate.setTextColor(getResources().getColor(R.color.red));
//                        txtDayAfterTmrwDate1.setTextColor(getResources().getColor(R.color.red));

                        llTdyLine.setVisibility(View.INVISIBLE);
                        llTmrwLine.setVisibility(View.INVISIBLE);
                        llDATLine.setVisibility(View.VISIBLE);
                        llDAT1Line.setVisibility(View.INVISIBLE);

                        txtDate.setText(txtDayAfterTmrwDate.getText().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        try {

                            Date date = formatter.parse(txtDayAfterTmrwDate.getText().toString());
                            new CustomerFavouriteJSON().execute(new SimpleDateFormat("dd-MM-yy").format(date).toString());
                            popupWindow.dismiss();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });

                llDayAfterTmrw1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dateSelectedBoolean = 4;


//                        txtToday.setTextColor(getResources().getColor(R.color.border_color));
//                        txtTomorrow.setTextColor(getResources().getColor(R.color.border_color));
//                        txtDayAfterTmrw.setTextColor(getResources().getColor(R.color.red));
//
//                        txtTodayDate.setTextColor(getResources().getColor(R.color.border_color));
//                        txtTomorrowDate.setTextColor(getResources().getColor(R.color.border_color));
//                        txtDayAfterTmrwDate.setTextColor(getResources().getColor(R.color.red));

                        llTdyLine.setVisibility(View.INVISIBLE);
                        llTmrwLine.setVisibility(View.INVISIBLE);
                        llDATLine.setVisibility(View.INVISIBLE);
                        llDAT1Line.setVisibility(View.VISIBLE);

                        txtDate.setText(txtDayAfterTmrwDate1.getText().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        try {

                            Date date = formatter.parse(txtDayAfterTmrwDate1.getText().toString());
                            new CustomerFavouriteJSON().execute(new SimpleDateFormat("dd-MM-yy").format(date).toString());
                            popupWindow.dismiss();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        llBlackScreen.setVisibility(View.GONE);
                    }
                });

            }
        });

        imgFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = (LayoutInflater) CustomerFoodItemFragment.this
                        .getSystemService(CustomerFoodItemFragment.this.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.dialog_customer_food_filter, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                expListView = (ExpandableListView) popupView.findViewById(R.id.lstExp);

                listAdapter = new ExpandableListAdapter(CustomerFoodItemFragment.this, CustomerFoodFilterHeader,
                        CustomerFoodFilterHeadersChild);
                expListView.setAdapter(listAdapter);
                //popupWindow.setWidth(100);
                // popupWindow.setAnimationStyle(R.style.DialogAnimation);
                popupWindow.showAsDropDown(imgFilter, -10, 10);
                //llBlackScreen.setVisibility(View.VISIBLE);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        llBlackScreen.setVisibility(View.GONE);
                    }
                });

            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.open();
                Cursor csr = db.fetchAllData();
                int cnt = csr.getCount();
                db.close();

                if (cnt > 0) {
                    LayoutInflater layoutInflater = (LayoutInflater) CustomerFoodItemFragment.this
                            .getSystemService(CustomerFoodItemFragment.this.LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.dialog_checkout, null);
                    popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT, true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    popupWindow.showAtLocation(txtPlace, Gravity.TOP, 0, 0);

                    lstCartFood = (ListView) popupView.findViewById(R.id.lstCartFood);
                    txtTPrice = (TextView) popupView.findViewById(R.id.txtTPrice);
                    imgClose = (ImageView) popupView.findViewById(R.id.imgClose);

                    RelativeLayout rlCheckout = (RelativeLayout) popupView.findViewById(R.id.rlCheckout);

                    db.open();
                    Cursor c = db.fetchAllData();
                    FoodCartCheckoutAL.clear();
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                FoodCartCheckoutItem = new FoodCartCheckout();

                                FoodCartCheckoutItem.setMenu_id(c.getString(c.getColumnIndex("column_menu_id")));
                                FoodCartCheckoutItem.setId(c.getString(c.getColumnIndex("column_food_id")));
                                FoodCartCheckoutItem.setName(c.getString(c.getColumnIndex("column_food_name")));
                                FoodCartCheckoutItem.setPrice(c.getString(c.getColumnIndex("column_food_price")));
                                FoodCartCheckoutItem.setCount(c.getString(c.getColumnIndex("column_cart_count")));
                                FoodCartCheckoutItem
                                        .setT_price(c.getString(c.getColumnIndex("column_cart_total_price")));
                                FoodCartCheckoutItem.setImg(c.getString(c.getColumnIndex("column_food_image")));
                                FoodCartCheckoutItem.setQty(c.getString(c.getColumnIndex("column_food_qty")));

                                FoodCartCheckoutAL.add(FoodCartCheckoutItem);
//                                try {
//                                    CartObj.put("id", c.getString(c.getColumnIndex("column_menu_id")));
//                                    CartObj.put("cart_qty", c.getString(c.getColumnIndex("column_cart_count")));
//
//                                    CartArr.put(CartObj);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }

                            } while (c.moveToNext());
                        }
                    }

                    txtTPrice.setText(db.SumOfPrice());

                    db.close();

                    FCAdapter = new FoodCartAdapter(CustomerFoodItemFragment.this, FoodCartCheckoutAL, txtTPrice);
                    lstCartFood.setAdapter(FCAdapter);

                    // popupWindow.setAnimationStyle(R.style.DialogAnimation);
                    popupWindow.showAsDropDown(txtDinner, -10, 10);
                    llBlackScreen.setVisibility(View.VISIBLE);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            llBlackScreen.setVisibility(View.GONE);
                            UpdateAdapter();
                        }
                    });

                    imgClose.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                    rlCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < FoodCartCheckoutAL.size(); i++) {

                                try {
                                    CartObj.put("id", FoodCartCheckoutAL.get(i).getMenu_id());
                                    CartObj.put("cart_qty", FoodCartCheckoutAL.get(i).getCount());

                                    //CartArr.put(CartObj);
                                    if (i == 0) {
                                        CartStr = CartObj.toString();
                                    } else {
                                        CartStr = CartStr + "," + CartObj.toString();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            CartStr = "[" + CartStr + "]";

                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                            try {
//                                Date date = formatter.parse(txtDate.getText().toString());

                                String dateStr = txtDate.getText().toString();
                                DateFormat readFormat = new SimpleDateFormat("EEE d MMM yyyy");

                                DateFormat writeFormat = new SimpleDateFormat("dd-MM-yy");
                                Date date = null;
                                try {
                                    date = readFormat.parse(dateStr);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String formattedDate = "";
                                if (date != null) {
                                    formattedDate = writeFormat.format(date);
                                }
                                new CartVerifyJSON().execute(CartStr, formattedDate);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                            lstMN.setAdapter(null);
                            lstBF.setAdapter(null);
                            lstLN.setAdapter(null);
                            lstDN.setAdapter(null);

                            if (CustomerFoodMenuItemMidNightAL.size() > 0) {
                                adapterCartMaintainMN = new CustomerFoodMenuAdapter(CustomerFoodItemFragment.this, CustomerFoodMenuItemMidNightAL,
                                        txtTotalPrice, rlCartProCount, txtCartProCount, CartHM, "1");
                                lstMN.setAdapter(adapterCartMaintainMN);
                            }

                            if (CustomerFoodMenuItemBreakfastAL.size() > 0) {
                                adapterCartMaintainBF = new CustomerFoodMenuAdapter(CustomerFoodItemFragment.this, CustomerFoodMenuItemBreakfastAL,
                                        txtTotalPrice, rlCartProCount, txtCartProCount, CartHM, "2");
                                lstBF.setAdapter(adapterCartMaintainBF);
                            }


                            if (CustomerFoodMenuItemLunchAL.size() > 0) {
                                adapterCartMaintainLN = new CustomerFoodMenuAdapter(CustomerFoodItemFragment.this, CustomerFoodMenuItemLunchAL,
                                        txtTotalPrice, rlCartProCount, txtCartProCount, CartHM, "3");
                                lstLN.setAdapter(adapterCartMaintainLN);
                            }

                            if (CustomerFoodMenuItemDinnerAL.size() > 0) {
                                adapterCartMaintainDN = new CustomerFoodMenuAdapter(CustomerFoodItemFragment.this, CustomerFoodMenuItemDinnerAL,
                                        txtTotalPrice, rlCartProCount, txtCartProCount, CartHM, "4");
                                lstDN.setAdapter(adapterCartMaintainDN);
                            }
                            llBlackScreen.setVisibility(View.GONE);

                            txtTotalPrice.setText(txtTPrice.getText().toString());
                        }
                    });
                }
            }
        });

        // DateFormat mediumDf = DateFormat.getDateInstance(DateFormat.FULL);
        // txtDate.setText(mediumDf.format(new Date()));

        SimpleDateFormat formatter = new SimpleDateFormat("EEE d MMM yyyy");
        Date today = new Date();
        txtDate.setText(formatter.format(today));

//        String pin = prefs.getString("pincode", "400053");
//        pin = ("<b>" + pin.substring(7, pin.length()) + "</b><br>" + pin.substring(0, 6)).replace("-", "");
//        txtPlace.setText(Html.fromHtml(pin));

        // new CustomerFavouriteJSON().execute();

        if (AppConstants.UserAddressFlag.equals("1")) {

            Date todayDate = new Date();
            new CustomerFavouriteJSON().execute(new SimpleDateFormat("dd-MM-yy").format(todayDate));
        } else {
            if (AppConstants.Customer_Pincode.equals(null) || AppConstants.Customer_Pincode.equals("Place")) {
                String pin = prefs.getString("pincode", "400053");
                pin = ("<b>" + pin.substring(7, pin.length()) + "</b><br>" + pin.substring(0, 6)).replace("-", "");
                txtPlace.setText(Html.fromHtml(pin));
                Date todayDate = new Date();
                new CustomerFavouriteJSON().execute(new SimpleDateFormat("dd-MM-yy").format(todayDate));
            } else {
                String pin = AppConstants.Customer_Pincode;
                pin = ("<b>" + pin.substring(7, pin.length()) + "</b><br>" + pin.substring(0, 6)).replace("-", "");
                txtPlace.setText(Html.fromHtml(pin));
                Date todayDate = new Date();
                new CustomerFavouriteJSON().execute(new SimpleDateFormat("dd-MM-yy").format(todayDate));
            }
        }

    }


    // Ajit
    private void UpdateAdapter() {
        db.open();
        Cursor c = db.fetchAllData();

        String count = Integer.toString(c.getCount());
        String totalAmount = db.SumOfPrice();

        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    String id = c.getString(c.getColumnIndex("column_food_id"));
                    String cart_value = c.getString(c.getColumnIndex("column_cart_count"));

                    CartHM.put(id, cart_value);

                } while (c.moveToNext());
            }
        }
        db.close();

        txtCartProCount.setText(count);
        txtTotalPrice.setText(totalAmount);

    }

    class CustomerFavouriteJSON extends AsyncTask<String, Void, Void> {

        String pin;
        String status = "0";
        // String FTab, STab, TTab, Tab;

        ArrayList<String> TabName = new ArrayList<String>();
        ArrayList<String> TabStartTime = new ArrayList<String>();
        ArrayList<String> TabEndTime = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerFoodItemFragment.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String strDate = params[0];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();

                if (AppConstants.UserAddressFlag.equals("1")) {
                    pin = AppConstants.UserZipCode;
                } else {
                    if (AppConstants.Customer_Pincode.equals(null) || AppConstants.Customer_Pincode.equals("Place")) {
                        pin = prefs.getString("pincode", "400053").substring(0, 6);
                    } else {
                        pin = (AppConstants.Customer_Pincode).substring(0, 6);
                    }
                }

                nameValuePairs.add(new BasicNameValuePair("post_code", pin));
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                if (!strDate.equals("null")) {
                    nameValuePairs.add(new BasicNameValuePair("date", strDate));
                }

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_FOOD_ITEMS);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                Log.w("response" , response.toString());
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                    Log.w("success", "true");
                    CustomerFoodMenuItemMidNightAL.clear();
                    CustomerFoodMenuItemBreakfastAL.clear();
                    CustomerFoodMenuItemLunchAL.clear();
                    CustomerFoodMenuItemDinnerAL.clear();

                    CustomerFoodFilterHeader.clear();
                    CustomerFoodFilterHeadersChild.clear();

                    JSONArray arrTimeSlot = new JSONArray();
                    arrTimeSlot = obj.getJSONArray("time_slot_names");

                    for (int i = 0; i < arrTimeSlot.length(); i++) {
                        JSONObject TabObj = new JSONObject();
                        TabObj = arrTimeSlot.getJSONObject(i);

                        TabName.add(TabObj.getString("time_slot_value"));
                        TabStartTime.add(TabObj.getString("start_time"));
                        TabEndTime.add(TabObj.getString("end_time"));
                    }

                    JSONArray MidnightArr = new JSONArray();
                    JSONArray BreakfastArr = new JSONArray();
                    JSONArray LunchArr = new JSONArray();
                    JSONArray DinnerArr = new JSONArray();

                    JSONArray CuisineArr = new JSONArray();
                    JSONArray DietTypeArr = new JSONArray();
                    JSONArray ItemTypeArr = new JSONArray();

                    MidnightArr = obj.getJSONArray("time_slot_1");
                    BreakfastArr = obj.getJSONArray("time_slot_2");
                    LunchArr = obj.getJSONArray("time_slot_3");
                    DinnerArr = obj.getJSONArray("time_slot_4");

                    CuisineArr = obj.getJSONArray("cuisine");
                    DietTypeArr = obj.getJSONArray("diet_type");
                    ItemTypeArr = obj.getJSONArray("item_type");

                    // MN
                    for (int i = 0; i < MidnightArr.length(); i++) {
                        JSONObject ObjMN = new JSONObject();
                        ObjMN = MidnightArr.getJSONObject(i);

                        CFMItem = new CustomerFoodMenuItem();

                        CFMItem.setMenu_id(ObjMN.getString("id"));
                        CFMItem.setId(ObjMN.getString("item_id"));
                        CFMItem.setQuantity(ObjMN.getString("max_qty_per_order"));
                        CFMItem.setFoodType(ObjMN.getString("item_type"));

                        JSONObject itemObj = new JSONObject();
                        itemObj = ObjMN.getJSONObject("items");
                        CFMItem.setFoodImg(AppConstants.Customer_Dish_Image_path + itemObj.getString("photo"));
                        CFMItem.setFoodName(itemObj.getString("item_name"));
                        CFMItem.setVegNonveg(itemObj.getString("dish"));
                        CFMItem.setPrice(itemObj.getString("price"));
                        CFMItem.setFoodDesc(itemObj.getString("item_description"));
                        CFMItem.setCuisine(itemObj.getString("cuisine"));

                        JSONObject chefObj = new JSONObject();
                        chefObj = itemObj.getJSONObject("merchant");
                        CFMItem.setChef_id(chefObj.getString("id"));
                        CFMItem.setChefName(chefObj.getString("name"));
                        CFMItem.setChefImg(AppConstants.Customer_Chef_Image_path + chefObj.getString("logo"));

                        JSONArray arrRating = new JSONArray();
                        arrRating = itemObj.getJSONArray("item_rating");
                        JSONObject objRating = new JSONObject();
                        if (arrRating.length() == 0) {
                            CFMItem.setRating("0");
                        } else {
                            objRating = arrRating.getJSONObject(0);
                            CFMItem.setRating(objRating.getString("average"));
                        }

                        JSONArray arrFav = new JSONArray();
                        arrFav = itemObj.getJSONArray("favourited_item");
                        if (arrFav.length() == 0) {
                            CFMItem.setFav("0");
                        } else {
                            CFMItem.setFav("1");
                        }

                        CFMItem.setCategory("midnight");
                        CustomerFoodMenuItemMidNightAL.add(CFMItem);
                    }

                    // BF
                    for (int i = 0; i < BreakfastArr.length(); i++) {
                        JSONObject ObjBF = new JSONObject();
                        ObjBF = BreakfastArr.getJSONObject(i);

                        CFMItem = new CustomerFoodMenuItem();

                        CFMItem.setMenu_id(ObjBF.getString("id"));
                        CFMItem.setId(ObjBF.getString("item_id"));
                        CFMItem.setQuantity(ObjBF.getString("max_qty_per_order"));
                        CFMItem.setFoodType(ObjBF.getString("item_type"));

                        JSONObject itemObj = new JSONObject();
                        itemObj = ObjBF.getJSONObject("items");
                        CFMItem.setFoodImg(AppConstants.Customer_Dish_Image_path + itemObj.getString("photo"));
                        CFMItem.setFoodName(itemObj.getString("item_name"));
                        CFMItem.setVegNonveg(itemObj.getString("dish"));
                        CFMItem.setPrice(itemObj.getString("price"));
                        CFMItem.setFoodDesc(itemObj.getString("item_description"));
                        CFMItem.setCuisine(itemObj.getString("cuisine"));

                        JSONObject chefObj = new JSONObject();
                        chefObj = itemObj.getJSONObject("merchant");
                        CFMItem.setChef_id(chefObj.getString("id"));
                        CFMItem.setChefName(chefObj.getString("name"));
                        CFMItem.setChefImg(AppConstants.Customer_Chef_Image_path + chefObj.getString("logo"));

                        JSONArray arrRating = new JSONArray();
                        arrRating = itemObj.getJSONArray("item_rating");
                        JSONObject objRating = new JSONObject();
                        if (arrRating.length() == 0) {
                            CFMItem.setRating("0");
                        } else {
                            objRating = arrRating.getJSONObject(0);
                            CFMItem.setRating(objRating.getString("average"));
                        }

                        JSONArray arrFav = new JSONArray();
                        arrFav = itemObj.getJSONArray("favourited_item");
                        if (arrFav.length() == 0) {
                            CFMItem.setFav("0");
                        } else {
                            CFMItem.setFav("1");
                        }
                        CFMItem.setCategory("breakfast");
                        CustomerFoodMenuItemBreakfastAL.add(CFMItem);
                    }

                    // LN
                    for (int i = 0; i < LunchArr.length(); i++) {
                        JSONObject ObjLN = new JSONObject();
                        ObjLN = LunchArr.getJSONObject(i);

                        CFMItem = new CustomerFoodMenuItem();

                        CFMItem.setMenu_id(ObjLN.getString("id"));
                        CFMItem.setId(ObjLN.getString("item_id"));
                        CFMItem.setQuantity(ObjLN.getString("max_qty_per_order"));
                        CFMItem.setFoodType(ObjLN.getString("item_type"));

                        JSONObject itemObj = new JSONObject();
                        itemObj = ObjLN.getJSONObject("items");
                        CFMItem.setFoodImg(AppConstants.Customer_Dish_Image_path + itemObj.getString("photo"));
                        CFMItem.setFoodName(itemObj.getString("item_name"));
                        CFMItem.setVegNonveg(itemObj.getString("dish"));
                        CFMItem.setPrice(itemObj.getString("price"));
                        CFMItem.setFoodDesc(itemObj.getString("item_description"));
                        CFMItem.setCuisine(itemObj.getString("cuisine"));

                        JSONObject chefObj = new JSONObject();
                        chefObj = itemObj.getJSONObject("merchant");
                        CFMItem.setChef_id(chefObj.getString("id"));
                        CFMItem.setChefName(chefObj.getString("name"));
                        CFMItem.setChefImg(AppConstants.Customer_Chef_Image_path + chefObj.getString("logo"));

                        JSONArray arrRating = new JSONArray();
                        arrRating = itemObj.getJSONArray("item_rating");
                        JSONObject objRating = new JSONObject();
                        if (arrRating.length() == 0) {
                            CFMItem.setRating("0");
                        } else {
                            objRating = arrRating.getJSONObject(0);
                            CFMItem.setRating(objRating.getString("average"));
                        }

                        JSONArray arrFav = new JSONArray();
                        arrFav = itemObj.getJSONArray("favourited_item");
                        if (arrFav.length() == 0) {
                            CFMItem.setFav("0");
                        } else {
                            CFMItem.setFav("1");
                        }
                        CFMItem.setCategory("Lunch");
                        CustomerFoodMenuItemLunchAL.add(CFMItem);
                    }

                    // DN
                    for (int i = 0; i < DinnerArr.length(); i++) {
                        JSONObject ObjDN = new JSONObject();
                        ObjDN = DinnerArr.getJSONObject(i);

                        CFMItem = new CustomerFoodMenuItem();

                        CFMItem.setMenu_id(ObjDN.getString("id"));
                        CFMItem.setId(ObjDN.getString("item_id"));
                        CFMItem.setQuantity(ObjDN.getString("max_qty_per_order"));
                        CFMItem.setFoodType(ObjDN.getString("item_type"));

                        JSONObject itemObj = new JSONObject();
                        itemObj = ObjDN.getJSONObject("items");
                        CFMItem.setFoodImg(AppConstants.Customer_Dish_Image_path + itemObj.getString("photo"));
                        CFMItem.setFoodName(itemObj.getString("item_name"));
                        CFMItem.setVegNonveg(itemObj.getString("dish"));
                        CFMItem.setPrice(itemObj.getString("price"));
                        CFMItem.setFoodDesc(itemObj.getString("item_description"));
                        CFMItem.setCuisine(itemObj.getString("cuisine"));

                        JSONObject chefObj = new JSONObject();
                        chefObj = itemObj.getJSONObject("merchant");
                        CFMItem.setChef_id(chefObj.getString("id"));
                        CFMItem.setChefName(chefObj.getString("name"));
                        CFMItem.setChefImg(AppConstants.Customer_Chef_Image_path + chefObj.getString("logo"));

                        JSONArray arrRating = new JSONArray();
                        arrRating = itemObj.getJSONArray("item_rating");
                        JSONObject objRating = new JSONObject();
                        if (arrRating.length() == 0) {
                            CFMItem.setRating("0");
                        } else {
                            objRating = arrRating.getJSONObject(0);
                            CFMItem.setRating(objRating.getString("average"));
                        }
                        JSONArray arrFav = new JSONArray();
                        arrFav = itemObj.getJSONArray("favourited_item");
                        if (arrFav.length() == 0) {
                            CFMItem.setFav("0");
                        } else {
                            CFMItem.setFav("1");
                        }
                        CFMItem.setCategory("dinner");
                        CustomerFoodMenuItemDinnerAL.add(CFMItem);
                    }

                    CustomerFoodFilterHeader.add("Cuisine");
                    CustomerFoodFilterHeader.add("Diet Type");
                    CustomerFoodFilterHeader.add("Item Type");

                    List<String> CusineChildList = new ArrayList<String>();
                    List<String> DietTypeChildList = new ArrayList<String>();
                    List<String> ItemTypeChildList = new ArrayList<String>();

                    for (int i = 0; i < CuisineArr.length(); i++) {
                        JSONObject objCusineChild = new JSONObject();
                        objCusineChild = CuisineArr.getJSONObject(i);
                        CusineChildList.add(objCusineChild.getString("cuisine_name"));

                        AppConstants.filter_check_states.put("0", Integer.toString(i));
                        System.out.println("0" + Integer.toString(i));
                    }

                    for (int i = 0; i < DietTypeArr.length(); i++) {
                        JSONObject objDietTypeChild = new JSONObject();
                        objDietTypeChild = DietTypeArr.getJSONObject(i);
                        DietTypeChildList.add(objDietTypeChild.getString("diet_type_name"));

                        AppConstants.filter_check_states.put("1", Integer.toString(i));
                        System.out.println("1" + Integer.toString(i));
                    }

                    for (int i = 0; i < ItemTypeArr.length(); i++) {
                        JSONObject objItemTypeChild = new JSONObject();
                        objItemTypeChild = ItemTypeArr.getJSONObject(i);
                        ItemTypeChildList.add(objItemTypeChild.getString("type_name"));

                        AppConstants.filter_check_states.put("2", Integer.toString(i));
                        System.out.println("2" + Integer.toString(i));
                    }

                    CustomerFoodFilterHeadersChild.put(CustomerFoodFilterHeader.get(0), CusineChildList);
                    CustomerFoodFilterHeadersChild.put(CustomerFoodFilterHeader.get(1), DietTypeChildList);
                    CustomerFoodFilterHeadersChild.put(CustomerFoodFilterHeader.get(2), ItemTypeChildList);

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

                db.open();
                db.deleteAllRecords();
                txtCartProCount.setText(db.getCount());
                db.close();

                Toast.makeText(CustomerFoodItemFragment.this, "Pin : " + pin, Toast.LENGTH_SHORT).show();
                if (AppConstants.UserAddressFlag.equals("1")) {
                    llPlace.setVisibility(View.INVISIBLE);
                }

                txtMidnight.setText(TabName.get(0).substring(0, 1).toUpperCase() + TabName.get(0).substring(1));
                txtBreakfast.setText(TabName.get(1).substring(0, 1).toUpperCase() + TabName.get(1).substring(1));
                txtLunch.setText(TabName.get(2).substring(0, 1).toUpperCase() + TabName.get(2).substring(1));
                txtDinner.setText(TabName.get(3).substring(0, 1).toUpperCase() + TabName.get(3).substring(1));

                txtMidNightTiming.setText(DateConvert(TabStartTime.get(0)) + " - " + DateConvert(TabEndTime.get(0)));
                txtBreakfastTiming.setText(DateConvert(TabStartTime.get(1)) + " - " + DateConvert(TabEndTime.get(1)));
                txtLunchTiming.setText(DateConvert(TabStartTime.get(2)) + " - " + DateConvert(TabEndTime.get(2)));
                txtDinnerTiming.setText(DateConvert(TabStartTime.get(3)) + " - " + DateConvert(TabEndTime.get(3)));

                horizontalView.removeAllViews();

                LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                for (int i = 0; i < 4; i++) {
                    if (i == 0) {
                        if (CustomerFoodMenuItemMidNightAL.size() > 0) {
                            CFMadapter = new CustomerFoodMenuAdapter(CustomerFoodItemFragment.this, CustomerFoodMenuItemMidNightAL,
                                    txtTotalPrice, rlCartProCount, txtCartProCount, CartHM, "1");
                            // ListView lstMN = new ListView(getActivity());
                            lstMN.setLayoutParams(lparams);
                            lstMN.setAdapter(CFMadapter);

                            horizontalView.addView(lstMN);
                        } else {
                            ImageView img = new ImageView(CustomerFoodItemFragment.this);
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontalView.addView(img);
                        }
                    } else if (i == 1) {
                        if (CustomerFoodMenuItemBreakfastAL.size() > 0) {
                            CFMadapter = new CustomerFoodMenuAdapter(CustomerFoodItemFragment.this, CustomerFoodMenuItemBreakfastAL,
                                    txtTotalPrice, rlCartProCount, txtCartProCount, CartHM, "2");
                            // ListView lstBF = new ListView(getActivity());
                            lstBF.setLayoutParams(lparams);
                            lstBF.setAdapter(CFMadapter);

                            horizontalView.addView(lstBF);
                        } else {
                            ImageView img = new ImageView(CustomerFoodItemFragment.this);
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontalView.addView(img);
                        }
                    } else if (i == 2) {
                        if (CustomerFoodMenuItemLunchAL.size() > 0) {
                            CFMadapter = new CustomerFoodMenuAdapter(CustomerFoodItemFragment.this, CustomerFoodMenuItemLunchAL,
                                    txtTotalPrice, rlCartProCount, txtCartProCount, CartHM, "3");
                            // ListView lstLN = new ListView(getActivity());
                            lstLN.setLayoutParams(lparams);
                            lstLN.setAdapter(CFMadapter);

                            horizontalView.addView(lstLN);
                        } else {
                            ImageView img = new ImageView(CustomerFoodItemFragment.this);
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontalView.addView(img);
                        }
                    } else if (i == 3) {
                        if (CustomerFoodMenuItemDinnerAL.size() > 0) {
                            CFMadapter = new CustomerFoodMenuAdapter(CustomerFoodItemFragment.this, CustomerFoodMenuItemDinnerAL,
                                    txtTotalPrice, rlCartProCount, txtCartProCount, CartHM, "4");
                            // ListView lstDN = new ListView(getActivity());
                            lstDN.setLayoutParams(lparams);
                            lstDN.setAdapter(CFMadapter);

                            horizontalView.addView(lstDN);
                        } else {
                            ImageView img = new ImageView(CustomerFoodItemFragment.this);
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontalView.addView(img);
                        }
                    }
                }

                if (horizontalView.getChildCount() < 1) {
                    // horizontalView.setVisibility(View.INVISIBLE);
                }

            } else {
                Toast.makeText(CustomerFoodItemFragment.this, "Server Error...", Toast.LENGTH_SHORT).show();
            }

            //
            // Date now = new Date();

            Date d = new Date(new Date().getTime());
            String now = new SimpleDateFormat("HH:mm").format(d);

            ArrayList<String> timeStart = new ArrayList<String>();
            ArrayList<String> timeEnd = new ArrayList<String>();

            timeStart.add("00:00");
            timeStart.add("03:01");
            timeStart.add("11:31");
            timeStart.add("16:31");

            timeEnd.add("03:00");
            timeEnd.add("11:30");
            timeEnd.add("16:30");
            timeEnd.add("24:00");

            for (int i = 0; i < timeStart.size(); i++) {
                boolean a, b;
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                try {
                    a = _24HourSDF.parse(now).after(_24HourSDF.parse(timeStart.get(i)));
                    b = _24HourSDF.parse(now).before(_24HourSDF.parse(timeEnd.get(i)));

                    if (a && b) {
                        if (i == 0) {
                            Midnight();
                            AppConstants.TimeSlotValue = "1";
                        } else if (i == 1) {
                            Breakfast();
                            AppConstants.TimeSlotValue = "2";
                        } else if (i == 2) {
                            Lunch();
                            AppConstants.TimeSlotValue = "3";
                        } else if (i == 3) {
                            Dinner();
                            AppConstants.TimeSlotValue = "4";
                        }
                        break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //
        }
    }

    public String DateConvert(String date) {

        String convertedDate = "";

        try {
            String _24HourTime = date;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            // System.out.println(_24HourDt);
            convertedDate = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public void clickHandler() {
        if (isShowingBox) {
            // isShowingBox = false;
            slideOut(500, 0);
        } else {
            // isShowingBox = true;
            slideIn(500, 0);
        }
    }

    private void slideIn(int duration, int delay) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(llSlideView, "translationY", -slideViewHeight, 0),
                ObjectAnimator.ofFloat(llSlideView, "alpha", 0, 0.1f, 1));
        set.setStartDelay(delay);
        set.setDuration(duration).start();
    }

    private void slideOut(int duration, int delay) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(llSlideView, "translationY", 0, -slideViewHeight),
                ObjectAnimator.ofFloat(llSlideView, "alpha", 1, 0.5f, 1));
        set.setStartDelay(delay);
        set.setDuration(duration).start();
    }

    @Override
    public void onResume() {
        super.onResume();

//        if (AppConstants.UserAddressFlag.equals("1")) {
//            new CustomerFavouriteJSON().execute("null");
//        } else {
//            if (AppConstants.Customer_Pincode.equals(null) || AppConstants.Customer_Pincode.equals("Place")) {
//                String pin = prefs.getString("pincode", "400053");
//                pin = ("<b>" + pin.substring(7, pin.length()) + "</b><br>" + pin.substring(0, 6)).replace("-", "");
//                txtPlace.setText(Html.fromHtml(pin));
//                new CustomerFavouriteJSON().execute("null");
//            } else {
//                String pin = AppConstants.Customer_Pincode;
//                pin = ("<b>" + pin.substring(7, pin.length()) + "</b><br>" + pin.substring(0, 6)).replace("-", "");
//                txtPlace.setText(Html.fromHtml(pin));
//                new CustomerFavouriteJSON().execute("null");
//            }
//        }
    }

    public void Midnight() {
        txtMidNightTiming.setVisibility(View.VISIBLE);
        txtBreakfastTiming.setVisibility(View.INVISIBLE);
        txtLunchTiming.setVisibility(View.INVISIBLE);
        txtDinnerTiming.setVisibility(View.INVISIBLE);

        llMNLine.setBackgroundColor(getResources().getColor(R.color.white));
        llBFLine.setBackgroundResource(0);
        llLLine.setBackgroundResource(0);
        llDLine.setBackgroundResource(0);

        int textViewColor = txtMidnight.getTextColors().getDefaultColor();
        txtMidnight.setTextColor(Color.argb(500, Color.red(textViewColor), Color.green(textViewColor),
                Color.blue(textViewColor)));

        int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
        txtBreakfast.setTextColor(Color.argb(128, Color.red(textViewColor1), Color.green(textViewColor1),
                Color.blue(textViewColor1)));

        int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
        txtLunch.setTextColor(Color.argb(128, Color.red(textViewColor2), Color.green(textViewColor2),
                Color.blue(textViewColor2)));

        int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
        txtDinner.setTextColor(Color.argb(128, Color.red(textViewColor3), Color.green(textViewColor3),
                Color.blue(textViewColor3)));

        horizontalView.setCurrentScreen(0, true);
    }

    public void Breakfast() {
        txtMidNightTiming.setVisibility(View.INVISIBLE);
        txtBreakfastTiming.setVisibility(View.VISIBLE);
        txtLunchTiming.setVisibility(View.INVISIBLE);
        txtDinnerTiming.setVisibility(View.INVISIBLE);

        llMNLine.setBackgroundResource(0);
        llBFLine.setBackgroundColor(getResources().getColor(R.color.white));
        llLLine.setBackgroundResource(0);
        llDLine.setBackgroundResource(0);

        int textViewColor = txtMidnight.getTextColors().getDefaultColor();
        txtMidnight.setTextColor(Color.argb(128, Color.red(textViewColor), Color.green(textViewColor),
                Color.blue(textViewColor)));

        int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
        txtBreakfast.setTextColor(Color.argb(500, Color.red(textViewColor1), Color.green(textViewColor1),
                Color.blue(textViewColor1)));

        int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
        txtLunch.setTextColor(Color.argb(128, Color.red(textViewColor2), Color.green(textViewColor2),
                Color.blue(textViewColor2)));

        int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
        txtDinner.setTextColor(Color.argb(128, Color.red(textViewColor3), Color.green(textViewColor3),
                Color.blue(textViewColor3)));

        horizontalView.setCurrentScreen(1, true);
    }

    public void Lunch() {
        txtMidNightTiming.setVisibility(View.INVISIBLE);
        txtBreakfastTiming.setVisibility(View.INVISIBLE);
        txtLunchTiming.setVisibility(View.VISIBLE);
        txtDinnerTiming.setVisibility(View.INVISIBLE);

        llMNLine.setBackgroundResource(0);
        llBFLine.setBackgroundResource(0);
        llLLine.setBackgroundColor(getResources().getColor(R.color.white));
        llDLine.setBackgroundResource(0);

        int textViewColor = txtMidnight.getTextColors().getDefaultColor();
        txtMidnight.setTextColor(Color.argb(128, Color.red(textViewColor), Color.green(textViewColor),
                Color.blue(textViewColor)));

        int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
        txtBreakfast.setTextColor(Color.argb(128, Color.red(textViewColor1), Color.green(textViewColor1),
                Color.blue(textViewColor1)));

        int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
        txtLunch.setTextColor(Color.argb(500, Color.red(textViewColor2), Color.green(textViewColor2),
                Color.blue(textViewColor2)));

        int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
        txtDinner.setTextColor(Color.argb(128, Color.red(textViewColor3), Color.green(textViewColor3),
                Color.blue(textViewColor3)));

        horizontalView.setCurrentScreen(2, true);
    }

    public void Dinner() {
        txtMidNightTiming.setVisibility(View.INVISIBLE);
        txtBreakfastTiming.setVisibility(View.INVISIBLE);
        txtLunchTiming.setVisibility(View.INVISIBLE);
        txtDinnerTiming.setVisibility(View.VISIBLE);

        llMNLine.setBackgroundResource(0);
        llBFLine.setBackgroundResource(0);
        llLLine.setBackgroundResource(0);
        llDLine.setBackgroundColor(getResources().getColor(R.color.white));

        int textViewColor = txtMidnight.getTextColors().getDefaultColor();
        txtMidnight.setTextColor(Color.argb(128, Color.red(textViewColor), Color.green(textViewColor),
                Color.blue(textViewColor)));

        int textViewColor1 = txtBreakfast.getTextColors().getDefaultColor();
        txtBreakfast.setTextColor(Color.argb(128, Color.red(textViewColor1), Color.green(textViewColor1),
                Color.blue(textViewColor1)));

        int textViewColor2 = txtLunch.getTextColors().getDefaultColor();
        txtLunch.setTextColor(Color.argb(128, Color.red(textViewColor2), Color.green(textViewColor2),
                Color.blue(textViewColor2)));

        int textViewColor3 = txtDinner.getTextColors().getDefaultColor();
        txtDinner.setTextColor(Color.argb(500, Color.red(textViewColor3), Color.green(textViewColor3),
                Color.blue(textViewColor3)));

        horizontalView.setCurrentScreen(3, true);
    }

    public class CartVerifyJSON extends AsyncTask<String, Integer, Void> {

        String responseBody;
        HttpPost httppost;
        String status;

        String strItem, date;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(CustomerFoodItemFragment.this);
            pDialog1.setCancelable(false);
            pDialog1.setMessage("Please Wait...");
            pDialog1.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                strItem = params[0];
                date = params[1];

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.CUSTOMER_ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("item", strItem));
                nameValuePairs.add(new BasicNameValuePair("post_code", AppConstants.UserZipCode));
                nameValuePairs.add(new BasicNameValuePair("date", date));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_CART_VERIFY);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {
                    JSONArray mainArr = new JSONArray();
                    mainArr = obj.getJSONArray("item_check");
                    for (int i = 0; i < mainArr.length(); i++) {
                        JSONArray internalArr = new JSONArray();
                        internalArr = mainArr.getJSONArray(i);
                        for (int j = 0; j < internalArr.length(); j++) {
                            JSONObject objMain = new JSONObject();
                            objMain = internalArr.getJSONObject(j);

                            JSONObject objTimeSlot = new JSONObject();
                            objTimeSlot = objMain.getJSONObject("time_slot");
                            String slotID = objTimeSlot.getString("id");

                            JSONArray arrDelSlot = new JSONArray();
                            arrDelSlot = objTimeSlot.getJSONArray("delivery_slot");
                            for (int k = 0; k < arrDelSlot.length(); k++) {
                                JSONObject delSlotObj = new JSONObject();
                                delSlotObj = arrDelSlot.getJSONObject(k);

                                String del_slot = delSlotObj.getString("delivery_slot_name");
                                String del_slot_id = delSlotObj.getString("time_slot_id");

                                DeliveryTimeSlot.put(del_slot, del_slot_id);

                                if (slotID.equals("1")) {
                                    AppConstants.MNDelSlotAL.add(del_slot);
                                } else if (slotID.equals("2")) {
                                    AppConstants.BFDelSlotAL.add(del_slot);
                                } else if (slotID.equals("3")) {
                                    AppConstants.LNDelSlotAL.add(del_slot);
                                } else if (slotID.equals("4")) {
                                    AppConstants.DNDelSlotAL.add(del_slot);
                                }
                            }
                        }
                    }
                }
                int a = 10;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog1.isShowing()) {
                pDialog1.dismiss();
            }

            if (status.equals("true")) {

                Toast.makeText(CustomerFoodItemFragment.this, "Verified...", Toast.LENGTH_SHORT).show();
//                FragmentManager fragMgr = getFragmentManager();
//                android.support.v4.app.FragmentTransaction xact = fragMgr.beginTransaction();
//                xact.add(R.id.main_fragment, new CustomerOrderSummery(), "Fragment").commit();

                Intent intent = new Intent(CustomerFoodItemFragment.this, CustomerOrderSummery.class);
                intent.putExtra("strDate", date);
                intent.putExtra("strItem", strItem);
                intent.putExtra("TimeSlotIdHM", DeliveryTimeSlot);
                intent.putExtra("SelectedAddress", SelectedAddress);
                startActivity(intent);

                popupWindow.dismiss();
            } else {
                Toast.makeText(CustomerFoodItemFragment.this, "Verification Failed...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        db.open();
        Cursor c = db.fetchAllData();
        if (c.getCount() > 0) {
            new AlertDialog.Builder(CustomerFoodItemFragment.this).setTitle("Cancel Order ?")
                    .setMessage("\nOrder will be cancel.\nDo you want to proceed?\n").setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
        } else {
            finish();
        }
        db.close();
    }
}
