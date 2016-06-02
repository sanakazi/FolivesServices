package com.callndata.Merchnt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
//import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.callndata.folivesservices.CustomerPaymentCODFragment;
import com.example.folivesservices.R;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravikant on 03-02-2016.
 */
public class NewMerchantPersonalInfoMain extends AppCompatActivity {

    public static Activity MyActivityFlag;

   // private TabLayout tabLayout;
    private ViewPager viewPager;

    String PaymentOrderId, TransactionId, ToatalAmt;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;

    TextView txtUserName, txtUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.new_merchant_personalinfo_main);

        MyActivityFlag = this;

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) findViewById(R.id.txtUserEmail);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

     //   tabLayout = (TabLayout) findViewById(R.id.tabs);
     //   tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PersonalInfoTabFragment(), "Personal Info");
        adapter.addFragment(new ChangeMobileTabFragment(), "Change Mobile");
        adapter.addFragment(new ChangePasswordFragment(), "Change Password");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
