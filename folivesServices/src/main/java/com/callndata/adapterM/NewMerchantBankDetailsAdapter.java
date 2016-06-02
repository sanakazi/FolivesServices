package com.callndata.adapterM;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.folivesservices.R;
import com.folives.MItem.NewMerchantBankDetailsItem;

import java.util.ArrayList;

/**
 * Created by ravikant on 15-03-2016.
 */
public class NewMerchantBankDetailsAdapter extends BaseAdapter {

    Context context;
    ArrayList<NewMerchantBankDetailsItem> NewMerchantBankDetailsItemAL = new ArrayList<NewMerchantBankDetailsItem>();

    public NewMerchantBankDetailsAdapter(Context context, ArrayList<NewMerchantBankDetailsItem> NewMerchantBankDetailsItemAL) {
        this.context = context;
        this.NewMerchantBankDetailsItemAL = NewMerchantBankDetailsItemAL;
    }

    @Override
    public int getCount() {
        return NewMerchantBankDetailsItemAL.size();
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
            view = View.inflate(context, R.layout.item_new_merchant_bank_details, null);
        } else {
            view = convertView;
        }

        Holder holder = new Holder();

        holder.imgVerified = (ImageView) view.findViewById(R.id.imgVerified);

        holder.txtName = (TextView) view.findViewById(R.id.txtName);
        holder.txtBank = (TextView) view.findViewById(R.id.txtBank);

        try {
            holder.txtName.setText(NewMerchantBankDetailsItemAL.get(pos).getAccount_name());
            holder.txtBank.setText(NewMerchantBankDetailsItemAL.get(pos).getBank_name() + " - " + NewMerchantBankDetailsItemAL.get(pos).getAcc_num());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    class Holder {

        ImageView imgVerified;
        TextView txtName, txtBank;

    }
}
