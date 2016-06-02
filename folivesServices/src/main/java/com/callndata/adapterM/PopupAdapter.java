package com.callndata.adapterM;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.folivesservices.R;
import com.folives.MItem.PopupModel;

import java.util.ArrayList;

/**
 * Created by Callndata on 5/13/2016.
 */
public class PopupAdapter extends BaseAdapter {


    private Activity activity;
    private ArrayList data;
    private LayoutInflater inflater=null;

    PopupModel tempValues=null;
    int i=0;

    public PopupAdapter(Activity a, ArrayList d) {

        /********** Take passed values **********/
        activity = a;
        data=d;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public  class ViewHolder{

        public TextView txt_1;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_dropdown, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txt_1=(TextView)vi.findViewById(R.id.txt_1);

            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.txt_1.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (PopupModel) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.txt_1.setText( tempValues.getName() );

        }
        return vi;
    }




}


