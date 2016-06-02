package com.callndata.others;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.folivesservices.R;

public class MyPagerAdapter extends PagerAdapter {

    Context context;

    public MyPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View instantiateItem(View collection, int position) {

        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.layout.item_order_list;
                break;
            case 1:
                resId = R.layout.item_order_list;
                break;
            case 2:
                resId = R.layout.item_order_list;
                break;
        }

//        View view = inflater.inflate(resId, null);
//
//        ((ViewPager) collection).addView(view, 0);

        for (int i = 0; i < 5; i++) {
            Button btn = new Button(context);
            btn.setText(i+"");
            ((ViewPager) collection).addView(btn, i);
        }

        //views.put(position, view);
        return collection;


    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
