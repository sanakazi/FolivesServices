package com.callndata.Merchnt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.callndata.adapterM.PopupAdapter;
import com.callndata.others.AppConstants;
import com.example.folivesservices.R;
import com.folives.MItem.PopupModel;
import com.folives.MItem.PopupModelValues;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NewMerchantFoodItemAddActivity extends FragmentActivity {

    ImageView imgBack,img_add_item,imgFoodImg;
    TextView txt_cuisine,txt_veg_nv,txt_itemtype;
    EditText etItemName,etdesc,etPrice;
    public static ArrayList<PopupModel> arrCusine = new ArrayList<PopupModel>();
    public static ArrayList<PopupModel> arrVegNv = new ArrayList<PopupModel>();
    public static ArrayList<PopupModel> arrItemType = new ArrayList<PopupModel>();

    public static ArrayList<PopupModelValues> arrVegNvValues = new ArrayList<PopupModelValues>();
    public static ArrayList<PopupModelValues> arrItemTypeValues = new ArrayList<PopupModelValues>();

    LinearLayout ll_cusine,ll_veg_nonveg,ll_itemtype;


    ProgressDialog pDialog;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
    private String encoded = " ";
    private String base64;
    private Bitmap bm;

    static  String txt_veg_nvValue,txt_itemtypeValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_merchant_food_item_add);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgFoodImg = (ImageView)findViewById(R.id.imgFoodImg);
        txt_cuisine = (TextView)findViewById(R.id.txt_cuisine);
        txt_veg_nv = (TextView)findViewById(R.id.txt_veg_nv);
        txt_itemtype = (TextView)findViewById(R.id.txt_itemtype);
        etItemName = (EditText)findViewById(R.id.etItemName);
        etdesc = (EditText)findViewById(R.id.etdesc);
        etPrice = (EditText)findViewById(R.id.etPrice);
        img_add_item = (ImageView)findViewById(R.id.img_add_item);
        ll_cusine = (LinearLayout)findViewById(R.id.ll_cusine);
        ll_veg_nonveg = (LinearLayout)findViewById(R.id.ll_veg_nonveg);
        ll_itemtype = (LinearLayout)findViewById(R.id.ll_itemtype);


        new GetDropdownsJSON().execute();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_cusine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopUp_cusine();
            }
        });

        ll_veg_nonveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopUp_mealtype();
            }
        });

        ll_itemtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopUp_itemtype();
            }
        });

        imgFoodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        img_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddNewItemJSON().execute(encoded.toString(),etItemName.getText().toString(),etdesc.getText().toString(),
                        txt_cuisine.getText().toString(),txt_veg_nvValue,txt_itemtypeValue,etPrice.getText().toString());
            }
        });
    }

    // to get dropdowns
    public class GetDropdownsJSON extends AsyncTask<String, Integer, Void> {




        String responseBody;
        HttpPost httppost;
        String status;
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(NewMerchantFoodItemAddActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {


                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));

                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.MERCHANT_FOOD_ITEM_ADD_DROPDOWN);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                Log.w("responsebody", responseBody.toString());
                status = objMain.getString("success");

                if (status.equals("true")) {


                    arrCusine.clear();
                    arrVegNv.clear();
                    arrItemType.clear();
                    JSONArray jsonArraycusine = objMain.optJSONArray("cuisine");
                    for(int i = 0 ; i<jsonArraycusine.length(); i++)
                    {
                        PopupModel sched = new PopupModel();
                        JSONObject jsonObject = jsonArraycusine.getJSONObject(i);
                        String cuisine_name = jsonObject.optString("cuisine_name");
                        //  Log.w("cusine name is" , cuisine_name);
                        sched.setName(cuisine_name);
                        arrCusine.add(sched);
                    }

                    JSONArray jsonArraymeal_type = objMain.optJSONArray("meal_type");
                    for(int i = 0 ; i<jsonArraymeal_type.length(); i++)
                    {
                        PopupModel sched = new PopupModel();
                        PopupModelValues schedValue = new PopupModelValues();
                        JSONObject jsonObject = jsonArraymeal_type.getJSONObject(i);
                        String diet_type_name = jsonObject.optString("diet_type_name");
                        String diet_type_value = jsonObject.optString("diet_type_value");
                        Log.w("diet_type_value", diet_type_value);
                        sched.setName(diet_type_name);
                        schedValue.setNameValue(diet_type_value);
                        arrVegNv.add(sched);
                        arrVegNvValues.add(schedValue);
                    }

                    JSONArray jsonArrayitem_type= objMain.optJSONArray("item_type");
                    for(int i = 0 ; i<jsonArrayitem_type.length(); i++)
                    {
                        PopupModel sched = new PopupModel();
                        PopupModelValues schedValue = new PopupModelValues();
                        JSONObject jsonObject = jsonArrayitem_type.getJSONObject(i);
                        String type_name = jsonObject.optString("type_name");
                        String type_value = jsonObject.optString("type_value");
                        Log.w("type_value" , type_value);
                        sched.setName(type_name);
                        schedValue.setNameValue(type_value);
                        arrItemType.add(sched);
                        arrItemTypeValues.add(schedValue);
                    }

                }
                else if (status.equals("false"))
                {

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
                // Toast.makeText(NewMerchantFoodItemAddActivity.this, "Updated...", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(NewMerchantFoodItemAddActivity.this, "Oops ...Try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // to add new item
    public class AddNewItemJSON extends AsyncTask<String, Integer, Void> {




        String responseBody;
        HttpPost httppost;
        String status;
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(NewMerchantFoodItemAddActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String food_file = params[0];
                String item_name = params[1];
                String description = params[2];
                String cuisine = params[3];
                String meal_type = params[4];
                String item_type = params[5];
                String price = params[6];


                Log.w("params ares" ,  "  item_name=" + item_name +  " , description=" + description+  " , cuisine=" + cuisine
                        +  " , meal_type=" + meal_type+  " , item_type=" + item_type+  " , price=" + price);


                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("access_token", AppConstants.ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("food_file", food_file));
                nameValuePairs.add(new BasicNameValuePair("item_name", item_name));
                nameValuePairs.add(new BasicNameValuePair("description", description));
                nameValuePairs.add(new BasicNameValuePair("cuisine", cuisine));
                nameValuePairs.add(new BasicNameValuePair("meal_type", meal_type));
                nameValuePairs.add(new BasicNameValuePair("item_type", item_type));
                nameValuePairs.add(new BasicNameValuePair("price", price));


                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost(AppConstants.MERCHANT_FOOD_ITEM_ADD);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);

                JSONObject objMain = new JSONObject(responseBody);
                Log.w("responsebody" , responseBody.toString());
                status = objMain.getString("success");

                if (status.equals("true")) {

                }
                else if (status.equals("false"))
                {

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
                Toast.makeText(NewMerchantFoodItemAddActivity.this, "Updated...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NewMerchantFoodItemAddActivity.this, MerchantMainActivity.class);
                i.putExtra("chefFragToOpen", "food_item");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);


            } else {
                Toast.makeText(NewMerchantFoodItemAddActivity.this, "Oops ...Try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }




    //to display cuisine
    private void initiatePopUp_cusine() {
        ListView list_dropdown;
        final Dialog dialog ;
        final TextView item_timeslot1;
        dialog = new Dialog(NewMerchantFoodItemAddActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.list_dropdown);
        final int screenWidth = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        final int screenHeight = dialog.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        dialog.getWindow().setLayout(screenWidth, screenHeight / 3);
        dialog.setCancelable(true);


        list_dropdown = (ListView)dialog.findViewById(R.id.list_dropdown);
        PopupAdapter adapter = new PopupAdapter(NewMerchantFoodItemAddActivity.this, arrCusine);
        list_dropdown.setAdapter(adapter);


        list_dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupModel csm = arrCusine.get(position);
                txt_cuisine.setText(csm.getName().toString());
                dialog.dismiss();
            }

        });


        dialog.show();
    }

    //to display mealtype veg/nv
    private void initiatePopUp_mealtype() {
        ListView list_dropdown;
        final Dialog dialog ;
        final TextView item_timeslot1;
        dialog = new Dialog(NewMerchantFoodItemAddActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.list_dropdown);
        final int screenWidth = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        final int screenHeight = dialog.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        dialog.getWindow().setLayout(screenWidth, screenHeight / 3);
        dialog.setCancelable(true);


        list_dropdown = (ListView)dialog.findViewById(R.id.list_dropdown);
        PopupAdapter adapter = new PopupAdapter(NewMerchantFoodItemAddActivity.this, arrVegNv);
        list_dropdown.setAdapter(adapter);


        list_dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupModel csm = arrVegNv.get(position);
                txt_veg_nv.setText(csm.getName().toString());

                PopupModelValues csmValues = arrVegNvValues.get(position);
                txt_veg_nvValue=  csmValues.getNameValue().toString();
                Log.d("dietype name Value", "csm = " + txt_veg_nvValue);
                dialog.dismiss();
            }

        });


        dialog.show();
    }

    //to display itemtype
    private void initiatePopUp_itemtype() {
        ListView list_dropdown;
        final Dialog dialog ;
        final TextView item_timeslot1;
        dialog = new Dialog(NewMerchantFoodItemAddActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.list_dropdown);
        final int screenWidth = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        final int screenHeight = dialog.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        dialog.getWindow().setLayout(screenWidth, screenHeight / 3);
        dialog.setCancelable(true);


        list_dropdown = (ListView)dialog.findViewById(R.id.list_dropdown);
        PopupAdapter adapter = new PopupAdapter(NewMerchantFoodItemAddActivity.this, arrItemType);
        list_dropdown.setAdapter(adapter);


        list_dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupModel csm = arrItemType.get(position);
                txt_itemtype.setText(csm.getName().toString());
                PopupModelValues csmValues = arrItemTypeValues.get(position);
                txt_itemtypeValue=  csmValues.getNameValue().toString();
                Log.d("txt_itemtypeValue =", txt_itemtypeValue);
                dialog.dismiss();
            }

        });


        dialog.show();
    }

    //to select food image
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(NewMerchantFoodItemAddActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

					/*
					 * create an instance of intent pass action
					 * android.media.action.IMAGE_CAPTURE as argument to launch
					 * camera
					 */
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					/* create instance of File with name img.jpg */
                    File file = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "img.jpg");
					/* put uri as extra in intent object */
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					/*
					 * start activity for result pass intent as argument and request
					 * code
					 */
                    startActivityForResult(intent, 1);

                } else if (items[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 3);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{

            // if request code is same we pass as argument in startActivityForResult
            if (requestCode == 1) {
                // create instance of File with same name we created before to get
                // image from storage
                File file = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "img.jpg");
                // Crop the captured image using an other intent
                try {
				/* the user's device may not support cropping */
                    cropCapturedImage(Uri.fromFile(file));
                } catch (ActivityNotFoundException aNFE) {
                    // display an error message if user device doesn't support
                    String errorMessage = "Sorry - your device doesn't support the crop action!";
                    Toast toast = Toast.makeText(NewMerchantFoodItemAddActivity.this, errorMessage,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else if (requestCode == 2) {
                // Create an instance of bundle and get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap from extras
                Bitmap thePic = extras.getParcelable("data");
                // set image bitmap to image view
                imgFoodImg.setImageBitmap(thePic);

                convertBase64(thePic);
            } else if (requestCode == 3) {
                Uri picUri = data.getData();
                performCrop(picUri);
            } else if (requestCode == 4) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                // set image bitmap to image view
                //iv_profile_picture.setImageBitmap(thePic);

                convertBase64(thePic);
            }

        }catch(NullPointerException e){
            e.printStackTrace();
        }


    }


    // create helping method cropCapturedImage(Uri picUri)
    public void cropCapturedImage(Uri picUri) {
        // call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        // indicate image type and Uri of image
        cropIntent.setDataAndType(picUri, "image/*");
        // set crop properties
        cropIntent.putExtra("crop", "true");
        // indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        // retrieve data on return
        cropIntent.putExtra("return-data", true);
        // start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 2);
    }

    public void performCrop(Uri picUri) {
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
            startActivityForResult(cropIntent, 4);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(NewMerchantFoodItemAddActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void convertBase64(Bitmap bitmap){
        bm = bitmap;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        imgFoodImg.setImageBitmap(bm);
        // img_profile_pic.setScaleY((float) 1.2);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // convrting into base64
		/*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);*/
        byte[] byteArray = bytes.toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.w("Base64encodedvalue:", encoded);
        Log.w("base64 is" ,base64.toString());

      /*  //  new AddProfilePicTask().execute();
        // call webservice here
        asyntask4 objasyntask4 = new asyntask4(getActivity());
        asyntask4.setListner(fragmentmypage.this);

        data.put("UserID", userID);
        data.put("UserProfilePicture", encoded);
        String[] strinputarray = {"http://shaadielephant.com/InsertProfilePicture.asmx/UpdateProfilePicture",
                "UserID="+userID,
                "UserProfilePicture="+base64};
        objasyntask4.execute(strinputarray);*/

    }
}
