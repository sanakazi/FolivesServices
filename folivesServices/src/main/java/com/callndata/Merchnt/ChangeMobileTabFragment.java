package com.callndata.Merchnt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.folivesservices.R;

/**
 * Created by ravikant on 22-04-2016.
 */
public class ChangeMobileTabFragment extends Fragment{

    EditText etMobile;
    ImageView imgUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newmerchant_changemobile_tab, container, false);

        etMobile = (EditText) view.findViewById(R.id.etMobile);
        imgUpdate = (ImageView) view.findViewById(R.id.imgUpdate);

        return view;
    }


}
