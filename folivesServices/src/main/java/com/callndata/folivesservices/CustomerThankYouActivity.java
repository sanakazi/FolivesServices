package com.callndata.folivesservices;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.folivesservices.R;

/**
 * Created by ravikant on 19-02-2016.
 */
public class CustomerThankYouActivity extends Activity {

    TextView txtOrderId, txtTransactionId;
    RelativeLayout rlOk;

    String OrderId, TransactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        OrderId = getIntent().getExtras().getString("order_id");
        TransactionId = getIntent().getExtras().getString("transaction_id");

        txtOrderId = (TextView) findViewById(R.id.txtOrderId);
        txtTransactionId = (TextView) findViewById(R.id.txtTransactionId);
        rlOk = (RelativeLayout) findViewById(R.id.rlOk);

        txtOrderId.setText(OrderId);
        txtTransactionId.setText(TransactionId);

        rlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerOrderSummery.MyActivityFlag.finish();
                CustomerFoodItemFragment.MyActivityFlag.finish();
                CustomerAddressSelection.MyActivityFlag.finish();
                CustomerPaymentMain.MyActivityFlag.finish();
                CustomerThankYouActivity.this.finish();
            }
        });
    }
}
