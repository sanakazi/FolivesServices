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
public class ChangePasswordFragment extends Fragment {

    EditText etPassword, etCPassword;
    ImageView imgUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newmerchant_changepassword_tab, container, false);

        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etCPassword = (EditText) view.findViewById(R.id.etCPassword);
        imgUpdate = (ImageView) view.findViewById(R.id.imgUpdate);

        return view;
    }

}
