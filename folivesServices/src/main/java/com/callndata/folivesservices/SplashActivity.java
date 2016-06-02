package com.callndata.folivesservices;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;

import com.callndata.Merchnt.MerchantMainActivity;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;

public class SplashActivity extends ActionBarActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences pref;
    String MLoginFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                MLoginFlag = pref.getString("MLoginFlag", null);

                if (MLoginFlag == null || MLoginFlag.equals("0")) {
                    Intent i = new Intent(SplashActivity.this, CustomerLoginActivity.class);
                    startActivity(i);
                } else if (MLoginFlag.equals("1")) {
                    AppConstants.CUSTOMER_ACCESS_TOKEN = pref.getString("Token", null);
                    Intent i = new Intent(SplashActivity.this, CustomerMainActivity.class);
                    startActivity(i);
                } else if (MLoginFlag.equals("2")) {
                    AppConstants.ACCESS_TOKEN = pref.getString("Token", null);
                    Intent i = new Intent(SplashActivity.this, MerchantMainActivity.class);
                    i.putExtra("chefFragToOpen","dashboard");
                    startActivity(i);
                }

                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
