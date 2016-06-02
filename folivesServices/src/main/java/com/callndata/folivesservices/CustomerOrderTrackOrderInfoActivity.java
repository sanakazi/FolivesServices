package com.callndata.folivesservices;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.callndata.adapters.OrderTrackingItemInfoAdapter;
import com.example.folivesservices.R;
import com.folives.item.OrderTrackingFoodItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ravikant on 18-02-2016.
 */
public class CustomerOrderTrackOrderInfoActivity extends Activity {

    String DeliveryTimeHeader, DeliveryTimeOrder, DeliveryAddress, ChefPic, ChefName, TotalAmount;

    TextView txtOrderHeaderTimeSlot, txtDeliveryTimeSlot, txtDeliveryAddress, txtChefName, txtTotalAmount;
    ImageView imgChefPic;
    ListView lstOrders;

    OrderTrackingItemInfoAdapter adapter;
    ArrayList<OrderTrackingFoodItem> OrderTrackingFoodItemAL = new ArrayList<OrderTrackingFoodItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackorder_orderinfo);

        DeliveryTimeHeader = getIntent().getExtras().getString("DeliveryTimeHeader");
        DeliveryTimeOrder = getIntent().getExtras().getString("DeliveryTimeOrder");
        DeliveryAddress = getIntent().getExtras().getString("DeliveryAddress");
        ChefPic = getIntent().getExtras().getString("ChefPic");
        ChefName = getIntent().getExtras().getString("ChefName");
        TotalAmount = getIntent().getExtras().getString("TotalAmount");
        OrderTrackingFoodItemAL = (ArrayList<OrderTrackingFoodItem>) getIntent().getSerializableExtra("ItemAL");

        txtOrderHeaderTimeSlot = (TextView) findViewById(R.id.txtOrderHeaderTimeSlot);
        txtDeliveryTimeSlot = (TextView) findViewById(R.id.txtDeliveryTimeSlot);
        txtDeliveryAddress = (TextView) findViewById(R.id.txtDeliveryAddress);
        txtChefName = (TextView) findViewById(R.id.txtChefName);
        imgChefPic = (ImageView) findViewById(R.id.imgChefPic);
        lstOrders = (ListView) findViewById(R.id.lstOrders);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);


        txtOrderHeaderTimeSlot.setText(DeliveryTimeHeader);
        txtDeliveryTimeSlot.setText(DeliveryTimeOrder);
        txtDeliveryAddress.setText(DeliveryAddress);
        txtChefName.setText(ChefName);
        txtTotalAmount.setText(TotalAmount);

        try {
            Picasso.with(CustomerOrderTrackOrderInfoActivity.this).load(ChefPic).fit().centerCrop().into(imgChefPic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new OrderTrackingItemInfoAdapter(CustomerOrderTrackOrderInfoActivity.this, OrderTrackingFoodItemAL);
        lstOrders.setAdapter(adapter);

    }
}
