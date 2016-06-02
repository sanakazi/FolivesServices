package com.callndata.folivesservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.folivesservices.R;

/**
 * Created by ravikant on 03-02-2016.
 */
public class CustomerPaymentNetBanking extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.customer_payment_netbanking, container, false);
    }
}
