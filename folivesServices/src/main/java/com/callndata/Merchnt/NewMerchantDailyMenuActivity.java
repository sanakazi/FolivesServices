package com.callndata.Merchnt;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapterM.MerchantDailyMenuAdapter;
import com.callndata.others.AppConstants;
import com.callndata.others.HorizontalPager;
import com.example.folivesservices.R;
import com.folives.MItem.MerchantDailyFoodMenuItem;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewMerchantDailyMenuActivity extends Activity {

    public static ListView lstMN, lstBF, lstLN, lstDN;
    RelativeLayout rlMidnight, rlBreakfast, rlLunch, rlDinner, parentLayout;
    LinearLayout llMNLine, llBFLine, llLLine, llDLine, llDate;
    TextView txtMidnight, txtMidNightTiming, txtBreakfast, txtBreakfastTiming, txtLunch, txtLunchTiming, txtDinner,
            txtDinnerTiming, txtDate;
    HorizontalPager horizontalView;
    LinearLayout llSlideView;
    EditText etPinCode;
    ImageView imgPinCodeSubmit;
    LinearLayout llPlace;
    TextView txtPlace;
    ImageView imgBack;

    LinearLayout llTdoay, llTomorrow, llDayAfterTmrw, llDayAfterTmrw1, llBlackScreen, llTdyLine, llTmrwLine, llDATLine, llDAT1Line;
    TextView txtTodayDate, txtTomorrowDate, txtDayAfterTmrwDate, txtDayAfterTmrwDate1, txtToday, txtTomorrow, txtDayAfterTmrw, txtDayAfterTmrw1;

    int dateSelectedBoolean = 1;

    ProgressDialog pDialog, pDialog1;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    MerchantDailyFoodMenuItem CFMItem;  //Merchant daily food menu item
    MerchantDailyMenuAdapter MDFMadapter;
    ArrayList<MerchantDailyFoodMenuItem> CustomerFoodMenuItemMidNightAL = new ArrayList<MerchantDailyFoodMenuItem>();
    ArrayList<MerchantDailyFoodMenuItem> CustomerFoodMenuItemBreakfastAL = new ArrayList<MerchantDailyFoodMenuItem>();
    ArrayList<MerchantDailyFoodMenuItem> CustomerFoodMenuItemLunchAL = new ArrayList<MerchantDailyFoodMenuItem>();
    ArrayList<MerchantDailyFoodMenuItem> CustomerFoodMenuItemDinnerAL = new ArrayList<MerchantDailyFoodMenuItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_merchant_dailymenu_activity);
        Log.w("Inside", " NewMerchantDailyMenuActivity");
        lstMN = new ListView(NewMerchantDailyMenuActivity.this);
        lstBF = new ListView(NewMerchantDailyMenuActivity.this);
        lstLN = new ListView(NewMerchantDailyMenuActivity.this);
        lstDN = new ListView(NewMerchantDailyMenuActivity.this);

        rlMidnight = (RelativeLayout) findViewById(R.id.rlMidnight);
        rlBreakfast = (RelativeLayout) findViewById(R.id.rlBreakfast);
        rlLunch = (RelativeLayout) findViewById(R.id.rlLunch);
        rlDinner = (RelativeLayout) findViewById(R.id.rlDinner);

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

        imgBack = (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

                LayoutInflater layoutInflater = (LayoutInflater) NewMerchantDailyMenuActivity.this
                        .getSystemService(NewMerchantDailyMenuActivity.this.LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.dialog_next_three_days, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
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

                        llTdyLine.setVisibility(View.VISIBLE);
                        llTmrwLine.setVisibility(View.INVISIBLE);
                        llDATLine.setVisibility(View.INVISIBLE);
                        llDAT1Line.setVisibility(View.INVISIBLE);

                        txtDate.setText(txtTodayDate.getText().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        try {

                            Date date = formatter.parse(txtTodayDate.getText().toString());
                           new MerchantDailyMenuJSON().execute(new SimpleDateFormat("dd-MM-yy").format(date).toString(),(new SimpleDateFormat("yyyy-MM-dd").format(date).toString()));
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

                        llTdyLine.setVisibility(View.INVISIBLE);
                        llTmrwLine.setVisibility(View.VISIBLE);
                        llDATLine.setVisibility(View.INVISIBLE);
                        llDAT1Line.setVisibility(View.INVISIBLE);

                        txtDate.setText(txtTomorrowDate.getText().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        try {

                            Date date = formatter.parse(txtTomorrowDate.getText().toString());
                            new MerchantDailyMenuJSON().execute(new SimpleDateFormat("dd-MM-yy").format(date).toString(),new SimpleDateFormat("yyyy-MM-dd").format(date).toString());
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

                        llTdyLine.setVisibility(View.INVISIBLE);
                        llTmrwLine.setVisibility(View.INVISIBLE);
                        llDATLine.setVisibility(View.VISIBLE);
                        llDAT1Line.setVisibility(View.INVISIBLE);

                        txtDate.setText(txtDayAfterTmrwDate.getText().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        try {

                            Date date = formatter.parse(txtDayAfterTmrwDate.getText().toString());
                           new MerchantDailyMenuJSON().execute(new SimpleDateFormat("dd-MM-yy").format(date).toString(),new SimpleDateFormat("yyyy-MM-dd").format(date).toString());
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

                        llTdyLine.setVisibility(View.INVISIBLE);
                        llTmrwLine.setVisibility(View.INVISIBLE);
                        llDATLine.setVisibility(View.INVISIBLE);
                        llDAT1Line.setVisibility(View.VISIBLE);

                        txtDate.setText(txtDayAfterTmrwDate1.getText().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                        try {

                            Date date = formatter.parse(txtDayAfterTmrwDate1.getText().toString());
                            new MerchantDailyMenuJSON().execute(new SimpleDateFormat("dd-MM-yy").format(date).toString(),new SimpleDateFormat("yyyy-MM-dd").format(date).toString());
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
        Date todayDate = new Date();
       new MerchantDailyMenuJSON().execute(new SimpleDateFormat("dd-MM-yy").format(todayDate),new SimpleDateFormat("yyyy-MM-dd").format(todayDate) );

    }

    class MerchantDailyMenuJSON extends AsyncTask<String, Void, Void> {

        String pin;
        String status = "0";
        // String FTab, STab, TTab, Tab;

        ArrayList<String> TabName = new ArrayList<String>();
        ArrayList<String> TabStartTime = new ArrayList<String>();
        ArrayList<String> TabEndTime = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(NewMerchantDailyMenuActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String strDate = params[0];
            String strDate_yyyyMMdd = params[1];

            try {
                nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                if (!strDate.equals("null")) {
                    nameValuePairs.add(new BasicNameValuePair("date", strDate));
                }

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.NEW_MERCHANT_DAILY_FOODMENU);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                Log.w("response" , response.toString());
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);
                Log.w("obj" , obj.toString());
                status = obj.getString("success");

                if (status.equals("true")) {


                    CustomerFoodMenuItemMidNightAL.clear();
                    CustomerFoodMenuItemBreakfastAL.clear();
                    CustomerFoodMenuItemLunchAL.clear();
                    CustomerFoodMenuItemDinnerAL.clear();


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
                    DietTypeArr = obj.getJSONArray("meal_type");
                    ItemTypeArr = obj.getJSONArray("item_type");

                    // MN
                    for (int i = 0; i < MidnightArr.length(); i++) {
                        JSONObject ObjMN = new JSONObject();
                        ObjMN = MidnightArr.getJSONObject(i);

                        CFMItem = new MerchantDailyFoodMenuItem();
                        CFMItem.setDate(strDate_yyyyMMdd);
                        CFMItem.setDate_ddMMyy(strDate);

                        CFMItem.setMenu_id(ObjMN.getString("id"));
                        CFMItem.setId(ObjMN.getString("item_id"));

                        JSONObject ObjTimeSlot = new JSONObject();
                        ObjTimeSlot = ObjMN.getJSONObject("time_slot");
                        CFMItem.setTime_slot_id(ObjTimeSlot.getString("id"));

                        CFMItem.setQuantity(ObjMN.getString("quantity"));
                        CFMItem.setQtyPerOrder(ObjMN.getString("max_qty_per_order"));
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

//                        JSONArray arrFav = new JSONArray();
//                        arrFav = itemObj.getJSONArray("favourited_item");
//                        if (arrFav.length() == 0) {
                        CFMItem.setFav("0");
//                        } else {
//                            CFMItem.setFav("1");
//                        }

                        CFMItem.setCategory("midnight");
                        CustomerFoodMenuItemMidNightAL.add(CFMItem);
                    }

                    // BF
                    for (int i = 0; i < BreakfastArr.length(); i++) {
                        JSONObject ObjBF = new JSONObject();
                        ObjBF = BreakfastArr.getJSONObject(i);

                        CFMItem = new MerchantDailyFoodMenuItem();
                        CFMItem.setDate(strDate_yyyyMMdd);
                        CFMItem.setDate_ddMMyy(strDate);

                        CFMItem.setMenu_id(ObjBF.getString("id"));
                        CFMItem.setId(ObjBF.getString("item_id"));


                        JSONObject ObjTimeSlot = new JSONObject();
                        ObjTimeSlot = ObjBF.getJSONObject("time_slot");
                        CFMItem.setTime_slot_id(ObjTimeSlot.getString("id"));

                        CFMItem.setQuantity(ObjBF.getString("quantity"));
                        CFMItem.setQtyPerOrder(ObjBF.getString("max_qty_per_order"));
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

//                        JSONArray arrFav = new JSONArray();
//                        arrFav = itemObj.getJSONArray("favourited_item");
//                        if (arrFav.length() == 0) {
                        CFMItem.setFav("0");
//                        } else {
//                            CFMItem.setFav("1");
//                        }
                        CFMItem.setCategory("breakfast");
                        CustomerFoodMenuItemBreakfastAL.add(CFMItem);
                    }

                    // LN
                    for (int i = 0; i < LunchArr.length(); i++) {
                        JSONObject ObjLN = new JSONObject();
                        ObjLN = LunchArr.getJSONObject(i);

                        CFMItem = new MerchantDailyFoodMenuItem();
                        CFMItem.setDate(strDate_yyyyMMdd);
                        CFMItem.setDate_ddMMyy(strDate);

                        CFMItem.setMenu_id(ObjLN.getString("id"));
                        CFMItem.setId(ObjLN.getString("item_id"));

                        JSONObject ObjTimeSlot = new JSONObject();
                        ObjTimeSlot = ObjLN.getJSONObject("time_slot");
                        CFMItem.setTime_slot_id(ObjTimeSlot.getString("id"));

                        CFMItem.setQuantity(ObjLN.getString("quantity"));
                        CFMItem.setQtyPerOrder(ObjLN.getString("max_qty_per_order"));
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

//                        JSONArray arrFav = new JSONArray();
//                        arrFav = itemObj.getJSONArray("favourited_item");
//                        if (arrFav.length() == 0) {
                        CFMItem.setFav("0");
//                        } else {
//                            CFMItem.setFav("1");
//                        }
                        CFMItem.setCategory("Lunch");
                        CustomerFoodMenuItemLunchAL.add(CFMItem);
                    }

                    // DN
                    for (int i = 0; i < DinnerArr.length(); i++) {
                        JSONObject ObjDN = new JSONObject();
                        ObjDN = DinnerArr.getJSONObject(i);

                        CFMItem = new MerchantDailyFoodMenuItem();
                        CFMItem.setDate(strDate_yyyyMMdd);
                        CFMItem.setDate_ddMMyy(strDate);

                        CFMItem.setMenu_id(ObjDN.getString("id"));
                        CFMItem.setId(ObjDN.getString("item_id"));

                        JSONObject ObjTimeSlot = new JSONObject();
                        ObjTimeSlot = ObjDN.getJSONObject("time_slot");
                        CFMItem.setTime_slot_id(ObjTimeSlot.getString("id"));

                        CFMItem.setQuantity(ObjDN.getString("quantity"));
                        CFMItem.setQtyPerOrder(ObjDN.getString("max_qty_per_order"));
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
//                        JSONArray arrFav = new JSONArray();
//                        arrFav = itemObj.getJSONArray("favourited_item");
//                        if (arrFav.length() == 0) {
                        CFMItem.setFav("0");
//                        } else {
//                            CFMItem.setFav("1");
//                        }
                        CFMItem.setCategory("dinner");
                        CustomerFoodMenuItemDinnerAL.add(CFMItem);
                    }

                    List<String> CusineChildList = new ArrayList<String>();
                    List<String> DietTypeChildList = new ArrayList<String>();
                    List<String> ItemTypeChildList = new ArrayList<String>();

                    for (int i = 0; i < CuisineArr.length(); i++) {
                        JSONObject objCusineChild = new JSONObject();
                        objCusineChild = CuisineArr.getJSONObject(i);
                        CusineChildList.add(objCusineChild.getString("cuisine_name"));

                        AppConstants.filter_check_states.put("0", Integer.toString(i));

                    }

                    for (int i = 0; i < DietTypeArr.length(); i++) {
                        JSONObject objDietTypeChild = new JSONObject();
                        objDietTypeChild = DietTypeArr.getJSONObject(i);
                        DietTypeChildList.add(objDietTypeChild.getString("diet_type_name"));

                        AppConstants.filter_check_states.put("1", Integer.toString(i));

                    }

                    for (int i = 0; i < ItemTypeArr.length(); i++) {
                        JSONObject objItemTypeChild = new JSONObject();
                        objItemTypeChild = ItemTypeArr.getJSONObject(i);
                        ItemTypeChildList.add(objItemTypeChild.getString("type_name"));

                        AppConstants.filter_check_states.put("2", Integer.toString(i));

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

                txtMidnight.setText(TabName.get(0).substring(0, 1).toUpperCase() + TabName.get(0).substring(1));
                txtBreakfast.setText(TabName.get(1).substring(0, 1).toUpperCase() + TabName.get(1).substring(1));
                txtLunch.setText(TabName.get(2).substring(0, 1).toUpperCase() + TabName.get(2).substring(1));
                txtDinner.setText(TabName.get(3).substring(0, 1).toUpperCase() + TabName.get(3).substring(1));

                txtMidNightTiming.setText(DateConvert(TabStartTime.get(0)) + " - " + DateConvert(TabEndTime.get(0)));
                txtBreakfastTiming.setText(DateConvert(TabStartTime.get(1)) + " - " + DateConvert(TabEndTime.get(1)));
                txtLunchTiming.setText(DateConvert(TabStartTime.get(2)) + " - " + DateConvert(TabEndTime.get(2)));
                txtDinnerTiming.setText(DateConvert(TabStartTime.get(3)) + " - " + DateConvert(TabEndTime.get(3)));

                horizontalView.removeAllViews();

                ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                for (int i = 0; i < 4; i++) {
                    if (i == 0) {
                        if (CustomerFoodMenuItemMidNightAL.size() > 0) {

                            MDFMadapter = new MerchantDailyMenuAdapter(NewMerchantDailyMenuActivity.this, CustomerFoodMenuItemMidNightAL, "1");
                            lstMN.setLayoutParams(lparams);
                            lstMN.setAdapter(MDFMadapter);

                            horizontalView.addView(lstMN);
                        } else {
                            ImageView img = new ImageView(NewMerchantDailyMenuActivity.this);
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontalView.addView(img);
                        }
                    } else if (i == 1) {
                        if (CustomerFoodMenuItemBreakfastAL.size() > 0) {
                            MDFMadapter = new MerchantDailyMenuAdapter(NewMerchantDailyMenuActivity.this, CustomerFoodMenuItemBreakfastAL, "2");
                            lstBF.setLayoutParams(lparams);
                            lstBF.setAdapter(MDFMadapter);

                            horizontalView.addView(lstBF);
                        } else {
                            ImageView img = new ImageView(NewMerchantDailyMenuActivity.this);
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontalView.addView(img);
                        }
                    } else if (i == 2) {
                        if (CustomerFoodMenuItemLunchAL.size() > 0) {

                            MDFMadapter = new MerchantDailyMenuAdapter(NewMerchantDailyMenuActivity.this, CustomerFoodMenuItemLunchAL, "3");
                            lstLN.setLayoutParams(lparams);
                            lstLN.setAdapter(MDFMadapter);

                            horizontalView.addView(lstLN);
                        } else {
                            ImageView img = new ImageView(NewMerchantDailyMenuActivity.this);
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontalView.addView(img);
                        }
                    } else if (i == 3) {
                        if (CustomerFoodMenuItemDinnerAL.size() > 0) {

                            MDFMadapter = new MerchantDailyMenuAdapter(NewMerchantDailyMenuActivity.this, CustomerFoodMenuItemDinnerAL, "4");
                            lstDN.setLayoutParams(lparams);
                            lstDN.setAdapter(MDFMadapter);

                            horizontalView.addView(lstDN);
                        } else {
                            ImageView img = new ImageView(NewMerchantDailyMenuActivity.this);
                            img.setLayoutParams(lparams);
                            img.setBackgroundResource(R.drawable.blank_design);

                            horizontalView.addView(img);
                        }
                    }
                }

                if (horizontalView.getChildCount() < 1) {
                    horizontalView.setVisibility(View.INVISIBLE);
                }

            } else {
                Toast.makeText(NewMerchantDailyMenuActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
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
        }
    }

    public String DateConvert(String date) {

        String convertedDate = "";

        try {
            String _24HourTime = date;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);

            convertedDate = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDate;
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
}
