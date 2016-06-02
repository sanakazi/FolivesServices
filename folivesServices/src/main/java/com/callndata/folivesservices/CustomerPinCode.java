package com.callndata.folivesservices;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.callndata.others.AppConstants;
import com.example.folivesservices.R;

public class CustomerPinCode extends Activity {

    String PinCode;
    EditText etPinCode;
    ImageView imgPinCodeSubmit;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_selectedpin);

        prefs = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);

        etPinCode = (EditText) findViewById(R.id.etPinCode);
        imgPinCodeSubmit = (ImageView) findViewById(R.id.imgPinCodeSubmit);

        // etPinCode.setEnabled(false);

        etPinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerPinCode.this, PinCodeActivity.class);
                startActivity(intent);
            }
        });

        imgPinCodeSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edt = prefs.edit();
                edt.putString("pincode", AppConstants.Customer_Pincode);
                edt.commit();
                AppConstants.PlaceFlag = "1";
                String a = prefs.getString("pincode", "Place");

                Intent intent = new Intent(CustomerPinCode.this, CustomerFoodItemFragment.class);
                startActivity(intent);

                //finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        etPinCode.setText(AppConstants.Customer_Pincode);
    }
}
