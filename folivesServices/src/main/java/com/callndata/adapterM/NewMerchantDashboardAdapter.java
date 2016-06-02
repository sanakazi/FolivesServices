package com.callndata.adapterM;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.folivesservices.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;





/**
 * Created by Callndata on 5/6/2016.
 */
public class NewMerchantDashboardAdapter extends BaseAdapter {

Context context;

    private ArrayList<String> data;

    public NewMerchantDashboardAdapter (Context context, ArrayList<String> data)
    {
        this.context= context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return 0;
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
        final int pos = position;
        View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_new_merchant_dashboard_item, null);
        } else {
            view = convertView;

        }

        GraphView graph = (GraphView)view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
        return view;
    }
}
