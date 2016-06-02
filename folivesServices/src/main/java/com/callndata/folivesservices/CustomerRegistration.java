package com.callndata.folivesservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.callndata.others.AppConstants;
import com.example.folivesservices.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CustomerRegistration extends Activity {

    LinearLayout llNext;
    String FName, SName, mNum, Email, Pass, CPass;
    EditText etFName, etSName, etMNum, etEmail, etPassword, etCPassword;

    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs;

    ImageView imgProfilePic;

    final int GALLERY_CAPTURE = 0;
    // keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    // keep track of cropping intent
    final int PIC_CROP = 2;
    private Uri picUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        llNext = (LinearLayout) findViewById(R.id.llNext);
        etFName = (EditText) findViewById(R.id.etFName);
        etSName = (EditText) findViewById(R.id.etSName);
        etMNum = (EditText) findViewById(R.id.etMNum);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etCPassword = (EditText) findViewById(R.id.etCPassword);

        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);

        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder getImageFrom = new AlertDialog.Builder(CustomerRegistration.this);
                getImageFrom.setTitle("Select:");
                final CharSequence[] opsChars = {"CAMERA", "GALLERY"};
                getImageFrom.setItems(opsChars, new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(captureIntent, CAMERA_CAPTURE);
                        } else if (which == 1) {

                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, GALLERY_CAPTURE);
                        }
                        dialog.dismiss();
                    }
                });

                getImageFrom.show();
            }
        });

        llNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FName = etFName.getText().toString();
                SName = etSName.getText().toString();
                mNum = etMNum.getText().toString();
                Email = etEmail.getText().toString();
                Pass = etPassword.getText().toString();
                CPass = etCPassword.getText().toString();

                if (FName.equals(null) || FName.isEmpty()) {
                    etFName.setError("Enter first name");
                } else if (SName.equals(null) || SName.isEmpty()) {
                    etSName.setError("Enter second name");
                } else if (mNum.equals(null) || mNum.isEmpty()) {
                    etMNum.setError("Enter mobile number");
                } else if (Email.equals(null) || Email.isEmpty()) {
                    etEmail.setError("Enter email address");
                } else if (Pass.equals(null) || Pass.isEmpty()) {
                    etPassword.setError("Enter password");
                } else if (Pass.equals(null) || Pass.isEmpty()) {
                    etCPassword.setError("Confprm your password");
                } else {
                    new CustomerRegistrationJSON().execute(FName, SName, Email, Pass, CPass, mNum);
                }
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CustomerRegistration.RESULT_OK) {
            // user is returning from capturing an image using the camera
            if (requestCode == CAMERA_CAPTURE) {
                // get the Uri for the captured image
                picUri = data.getData();
                // carry out the crop operation
                performCrop();
            } else if (requestCode == GALLERY_CAPTURE) {
                // picUri = data.getData();
                performCrop();
            } else if (requestCode == PIC_CROP) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                // retrieve a reference to the ImageView


                imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
                BitmapDrawable ob = new BitmapDrawable(getResources(), thePic);
                // imgLogo.setBackgroundDrawable(ob);
                imgProfilePic.setImageDrawable(ob);

            }
        }
    }

    public void performCrop() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");


            // indicate selection ratio
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);

            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);


            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(CustomerRegistration.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    class CustomerRegistrationJSON extends AsyncTask<String, Void, Void> {

        String ba1;
        Bitmap bitmap;
        String status = "0";
        String fname, sname, email, pass, cpass, mnum;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CustomerRegistration.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            imgProfilePic.buildDrawingCache();
            bitmap = imgProfilePic.getDrawingCache();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] ba = stream.toByteArray();
            ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);

        }

        @Override
        protected Void doInBackground(String... params) {

            fname = params[0];
            sname = params[1];
            email = params[2];
            pass = params[3];
            cpass = params[4];
            mnum = params[5];

            try {

                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("first_name", fname));
                nameValuePairs.add(new BasicNameValuePair("last_name", sname));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("password", pass));
                nameValuePairs.add(new BasicNameValuePair("confirm_password", cpass));
                nameValuePairs.add(new BasicNameValuePair("mobile", mnum));
                nameValuePairs.add(new BasicNameValuePair("profile_photo", ba1));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConstants.CUSTOMER_REGISTRATION);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String imgResJSON = reader.readLine();

                JSONObject obj = new JSONObject(imgResJSON);

                status = obj.getString("success");

                if (status.equals("true")) {

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

            if (status.equals("true")) {
                Intent intent = new Intent(CustomerRegistration.this, CustomerOTPVerificationActivity.class);
                intent.putExtra("MNum", mnum);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(CustomerRegistration.this, "Some error occured...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
