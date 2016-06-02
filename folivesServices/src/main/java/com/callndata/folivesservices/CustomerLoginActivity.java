package com.callndata.folivesservices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.Merchnt.MerchantMainActivity;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

public class CustomerLoginActivity extends ActionBarActivity
        implements ConnectionCallbacks, OnConnectionFailedListener {

    ImageView imgLogin, imgFb, imgGoogle;
    EditText txtUserName, txtPassword;

    String UserName, Password, error, error_desc, access_token;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;
    ArrayList<String> LoginResKeys = new ArrayList<String>();

    LinearLayout llRegister, llSignIn, llChefIn, llSignInIndicator, llChefInIndicator;

    // Facebook
    private static String FB_APP_KEY = "717319028400897";
    private Facebook facebook = new Facebook(FB_APP_KEY);
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;

    // Google
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "MainActivity";
    private static final int PROFILE_PIC_SIZE = 400;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    SharedPreferences pref;
    String MLoginFlag;

    TextView txtForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        getSupportActionBar().hide();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        imgLogin = (ImageView) findViewById(R.id.imgLogin);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        llRegister = (LinearLayout) findViewById(R.id.llRegister);

        imgFb = (ImageView) findViewById(R.id.imgFb);
        imgGoogle = (ImageView) findViewById(R.id.imgGoogle);

        llSignIn = (LinearLayout) findViewById(R.id.llSignIn);
        llChefIn = (LinearLayout) findViewById(R.id.llChefIn);
        llSignInIndicator = (LinearLayout) findViewById(R.id.llSignInIndicator);
        llChefInIndicator = (LinearLayout) findViewById(R.id.llChefInIndicator);

        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);

        llSignInIndicator.setBackgroundColor(getResources().getColor(R.color.indicator));
        llChefInIndicator.setBackgroundColor(getResources().getColor(R.color.trans));
        AppConstants.LOGIN_FLAG = "signin";

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerLoginActivity.this, CustomerForgotPasswordEmailEntry.class);
                startActivity(intent);
            }
        });

        llSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppConstants.LOGIN_FLAG = "signin";
                llSignInIndicator.setBackgroundColor(getResources().getColor(R.color.indicator));
                llChefInIndicator.setBackgroundColor(getResources().getColor(R.color.trans));
            }
        });

        llChefIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppConstants.LOGIN_FLAG = "chefin";
                llSignInIndicator.setBackgroundColor(getResources().getColor(R.color.trans));
                llChefInIndicator.setBackgroundColor(getResources().getColor(R.color.indicator));
            }
        });

        imgLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UserName = txtUserName.getText().toString().trim();
                Password = txtPassword.getText().toString().trim();

                if (UserName.equals(null) || UserName.isEmpty()) {
                    txtUserName.setError("Enter Username");
                } else if (Password.equals(null) || Password.isEmpty()) {
                    txtPassword.setError("Enter Password");
                } else {
                    if (AppConstants.LOGIN_FLAG.equals("signin")) {
                        LoginResKeys.clear();
                        new Login().execute();
                    } else {
                        LoginResKeys.clear();
                        new LoginMerchant().execute();
                    }
                }
            }
        });

        llRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CustomerLoginActivity.this, CustomerRegistration.class);
                startActivity(intent);
                finish();
            }
        });

        imgFb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginToFacebook();
            }
        });

        imgGoogle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signInWithGplus();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        getProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;

            Intent intent = new Intent(CustomerLoginActivity.this, CustomerMainActivity.class);
            startActivity(intent);
            finish();

            resolveSignInError();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: " + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                Toast.makeText(CustomerLoginActivity.this, personName + " " + email, Toast.LENGTH_SHORT).show();

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;

                // new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(), "Person information is null", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    class Login extends AsyncTask<Void, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerLoginActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
                nameValuePairs.add(new BasicNameValuePair("username", UserName));
                nameValuePairs.add(new BasicNameValuePair("password", Password));
                nameValuePairs.add(new BasicNameValuePair("client_id", "FOLIVES_USER_@2122"));
                nameValuePairs.add(new BasicNameValuePair("client_secret", "USER_@2122"));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.LOGIN_CUSTOMER);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                Iterator<String> iter = obj.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    LoginResKeys.add(key.toString());
                }
                if (LoginResKeys.contains("error")) {
                    error = obj.getString("error");
                    error_desc = obj.getString("error_description");
                    status = "1";
                } else if (LoginResKeys.contains("access_token")) {
                    access_token = obj.getString("access_token");
                    Log.e("Access Token", access_token);
                    status = "2";
                } else {
                    status = "3";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (status.equals("1")) {
                Toast.makeText(CustomerLoginActivity.this, error_desc, Toast.LENGTH_SHORT).show();
            } else if (status.equals("2")) {
                Toast.makeText(CustomerLoginActivity.this, "Login Successful.\nAccess Token is " + access_token,
                        Toast.LENGTH_SHORT).show();

                AppConstants.CUSTOMER_ACCESS_TOKEN = access_token;
                Editor editor = pref.edit();
                editor.putString("MLoginFlag", "1");
                editor.putString("Token", access_token);
                editor.commit();

                Intent intent = new Intent(CustomerLoginActivity.this, CustomerMainActivity.class);//CustomerHomeActivity
                startActivity(intent);
                finish();

            } else if (status.equals("3")) {
                Toast.makeText(CustomerLoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CustomerLoginActivity.this, "No Response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class UserFBLogin extends AsyncTask<String, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerLoginActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String code = params[0];

            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("grant_type", "facebook"));
                nameValuePairs.add(new BasicNameValuePair("client_id", "FOLIVES_USER_@2122"));
                nameValuePairs.add(new BasicNameValuePair("client_secret", "USER_@2122"));
                nameValuePairs.add(new BasicNameValuePair("code", code));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.LOGIN_CUSTOMER);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                Iterator<String> iter = obj.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    LoginResKeys.add(key.toString());
                }
                if (LoginResKeys.contains("error")) {
                    error = obj.getString("error");
                    error_desc = obj.getString("error_description");
                    status = "1";
                } else if (LoginResKeys.contains("access_token")) {
                    access_token = obj.getString("access_token");
                    Log.e("Access Token", access_token);
                    status = "2";
                } else {
                    status = "3";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (status.equals("1")) {
                Toast.makeText(CustomerLoginActivity.this, error_desc, Toast.LENGTH_SHORT).show();
            } else if (status.equals("2")) {
                Toast.makeText(CustomerLoginActivity.this, "Login Successful.\nAccess Token is " + access_token,
                        Toast.LENGTH_SHORT).show();

                AppConstants.CUSTOMER_ACCESS_TOKEN = access_token;
                Editor editor = pref.edit();
                editor.putString("MLoginFlag", "1");
                editor.putString("Token", access_token);
                editor.commit();

                Intent intent = new Intent(CustomerLoginActivity.this, CustomerMainActivity.class);//CustomerHomeActivity
                startActivity(intent);
                finish();

            } else if (status.equals("3")) {
                Toast.makeText(CustomerLoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CustomerLoginActivity.this, "No Response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LoginMerchant extends AsyncTask<Void, Void, Void> {

        String status = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerLoginActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
                nameValuePairs.add(new BasicNameValuePair("username", UserName));
                nameValuePairs.add(new BasicNameValuePair("password", Password));
                nameValuePairs.add(new BasicNameValuePair("client_id", "FOLIVES_MERCHANT_@2122"));
                nameValuePairs.add(new BasicNameValuePair("client_secret", "MERCHANT_@2122"));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.LOGIN_MERCHANT);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                Iterator<String> iter = obj.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    LoginResKeys.add(key.toString());
                }
                if (LoginResKeys.contains("error")) {
                    error = obj.getString("error");
                    error_desc = obj.getString("error_description");
                    status = "1";
                } else if (LoginResKeys.contains("access_token")) {
                    access_token = obj.getString("access_token");
                    status = "2";
                } else {
                    status = "3";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (status.equals("1")) {
                Toast.makeText(CustomerLoginActivity.this, error_desc, Toast.LENGTH_SHORT).show();
            } else if (status.equals("2")) {
                Toast.makeText(CustomerLoginActivity.this, "Login Successful.\nAccess Token is " + access_token,
                        Toast.LENGTH_SHORT).show();

                AppConstants.ACCESS_TOKEN = access_token;
                Editor editor = pref.edit();
                editor.putString("MLoginFlag", "2");
                editor.putString("Token", access_token);
                editor.commit();

                Intent intent = new Intent(CustomerLoginActivity.this, MerchantMainActivity.class);
                startActivity(intent);
                finish();

            } else if (status.equals("3")) {
                Toast.makeText(CustomerLoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CustomerLoginActivity.this, "No Response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loginToFacebook() {

        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            facebook.setAccessToken(access_token);
            Log.d("FB Sessions", "" + facebook.isSessionValid());

//            Intent intent = new Intent(CustomerLoginActivity.this, CustomerMainActivity.class);
//            startActivity(intent);
//            finish();

            new UserFBLogin().execute(facebook.getAccessToken());
        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) {
            facebook.authorize(this, new String[]{"email", "publish_actions"}, new DialogListener() {

                @Override
                public void onCancel() {
                    // Function to handle cancel event
                }

                @Override
                public void onComplete(Bundle values) {
                    // Function to handle complete event
                    // Edit Preferences and update facebook acess_token
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                    Log.e("Access Token", facebook.getAccessToken());

//                    Intent intent = new Intent(CustomerLoginActivity.this, CustomerMainActivity.class);
//                    startActivity(intent);
//                    finish();

                    new UserFBLogin().execute(facebook.getAccessToken());

                }

                @Override
                public void onError(DialogError error) {
                    // Function to handle error

                }

                @Override
                public void onFacebookError(FacebookError fberror) {
                    // Function to handle Facebook errors

                }

            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {

            facebook.authorizeCallback(requestCode, resultCode, data);
        }
    }

    public void logoutFromFacebook() {
        mAsyncRunner.logout(this, new RequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                Log.d("Logout from Facebook", response);
                if (Boolean.parseBoolean(response) == true) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // actions

                        }
                    });
                }
            }

            @Override
            public void onIOException(IOException e, Object state) {
            }

            @Override
            public void onFileNotFoundException(FileNotFoundException e, Object state) {
            }

            @Override
            public void onMalformedURLException(MalformedURLException e, Object state) {
            }

            @Override
            public void onFacebookError(FacebookError e, Object state) {
            }
        });
    }
}
