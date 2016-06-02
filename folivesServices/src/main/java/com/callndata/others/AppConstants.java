package com.callndata.others;

import java.util.ArrayList;
import java.util.HashMap;

public class AppConstants {

    public static String Customer_Profile_Pic = "";
    public static String Customer_Name = "";
    public static String Customer_Email = "";
    public static String Customer_Pincode = "Place";
    public static String ACCESS_TOKEN = "0";
    public static String CUSTOMER_ACCESS_TOKEN = "0";
    public static String FAVOURITE_CHANGE_JSON = "http://production.folives.com/api/android/v1/favourite_change";

    public static String LOGIN_CUSTOMER = "http://production.folives.com/oauth-user/token";
    public static String LOGIN_MERCHANT = "http://production.folives.com/oauth-merchant/token";

    public static String Merchant_Logo_Path = "http://www.production.folives.com/assets/images/merchant/logo/";
    public static String Merchant_Banner_Path = "http://www.production.folives.com/assets/images/merchant/cover/";
    public static String Merchant_Food_Item_path = "http://www.production.folives.com/assets/images/merchant/dish/";
    public static String Merchant_Review_Profile_path = "http://www.production.folives.com/assets/images/profiles/user/";
    public static String Merchant_New_Food_Item_default_Image = "http://www.production.folives.com/assets/images/merchant/dish/food-item-default.jpg";

    public static String Customer_Chef_Image_path = "http://www.production.folives.com/assets/images/merchant/logo/";
    public static String Customer_Dish_Image_path = "http://www.production.folives.com/assets/images/merchant/dish/";
    public static String Customer_Profile_Image_Path = "http://www.production.folives.com/assets/images/profiles/user/";

    public static String Merchant_Profile_Pic = "http://www.production.folives.com/assets/images/profiles/merchant/";
    public static String MERCHANT_LOGO_UPLOAD = "http://production.folives.com/api/android/v1/update_merchant_logo";
    public static String MERCHANT_BANNER_UPLOAD = "http://production.folives.com/api/android/v1/update_merchant_cover";
    public static String MERCHANT_LOGO_BANNER_GET = "http://production.folives.com/api/android/v1/merchant_settings";
    public static String MERCHANT_OPTIONS_UPDATE = "http://production.folives.com/api/android/v1/update_merchant_settings";
    public static String MERCHANT_PROFILE_PIC_INFO = "http://production.folives.com/api/android/v1/profile_info";
    public static String MERCHANT_FOOD_ITEM_LIST = "http://production.folives.com/api/android/v1/food_item";
    public static String MERCHANT_MORE_FOOD_ITEM_LOAD = "http://production.folives.com/api/android/v1/load_more_food_item";
    public static String MERCHANT_REVIEW_LIST = "http://production.folives.com/api/android/v1/customer_reviews";
    public static String MERCHANT_FOOD_STATUS_CHANGE = "http://production.folives.com/api/android/v1/food_item_status_change";

    public static String MERCHNAT_NEW_FOOD_ITEM_VALUES = "http://production.folives.com/api/android/v1/add_new_food_item_dropdown";
    public static String MERCHANT_UPLOAD_NEW_FOOD_ITEM = "http://production.folives.com/api/android/v1/add_new_food_item";
    public static String MERCHANT_COUPON = "http://production.folives.com/api/android/v1/coupons";
    public static String MERCHANT_COUPON_STATUS_CHANGE = "http://production.folives.com/api/android/v1/coupons_status_change";
    public static String MERCHANT_NEW_COUPON = "http://production.folives.com/api/android/v1/add_new_coupon";
    public static String MERCHANT_DASHBOARD_VALUES = "http://production.folives.com/api/android/v1/dashboard";
    public static String MERCHANT_NEW_ORDER = "http://production.folives.com/api/android/v1/new_orders";
    public static String MERCHANT_ORDER_HISTORY = "http://production.folives.com/api/android/v1/order_history";
    public static String MERCHANT_NEW_ORDER_DETAILS = "http://production.folives.com/api/android/v1/new_order_details";
    public static String MERCHANT_ORDER_ACCEPT_REJECT_JSON = "http://production.folives.com/api/android/v1/update_new_order_details";
    public static String MERCHANT_CHANGE_ORDER_STATUS = "http://production.folives.com/api/android/v1/update_new_order_status";
    public static String MERCHANT_ORDER_HISTORY_SUMMERY = "http://production.folives.com/api/android/v1/order_history_details";
    public static String MERCHANT_FOOD_ITEM_DELETE = "http://production.folives.com/api/android/v1/delete_food_item";
    public static String MERCHANT_FOOD_ITEM_UPDATE = "http://production.folives.com/api/android/v1/update_food_item";
    public static String MERCHANT_FOOD_ITEM_ADD_TO_DAILY_MENU= "http://production.folives.com/api/android/v1/add_to_daily_menu";
    public static String MERCHANT_FOOD_ITEM_ADD= "http://production.folives.com/api/android/v1/add_new_food_item"; // to add item  to food menu list
    public static String MERCHANT_FOOD_ITEM_ADD_DROPDOWN= "http://production.folives.com/api/android/v1/food_item_dropdowns"; // to get dropdown to add item  to food menu list
    public static String MERCHANT_FOOD_ITEM_DETAILS = "http://production.folives.com/api/android/v1/food_item_details";
    public static String MERCHANT_COUPON_DETAILED_VIEW = "http://production.folives.com/api/android/v1/coupon_details";
    public static String MERCHANT_COUPON_DELETE = "http://production.folives.com/api/android/v1/delete_coupon";
    public static String MERCHANT_COUPON_EDIT = "http://production.folives.com/api/android/v1/update_coupon_details";
    public static String MERCHANT_COUPON_LOAD_MORE = "http://production.folives.com/api/android/v1/load_more_coupons";


    public static String CUSTOMER_REGISTRATION = "http://production.folives.com/api/android/v1/user_register";
    public static String CUSTOMER_OTP_VERIFICATION = "http://production.folives.com/api/android/v1/verify_mobile";
    public static String CUSTOMER_HOME_DATA = "http://production.folives.com/api/android/v1/home";
    public static String CUSTOMER_ORDER_HISTORY = "http://production.folives.com/api/android/v1/history";
    public static String CUSTOMER_FAVOURITE = "http://production.folives.com/api/android/v1/favourites";
    public static String CUSTOMER_UNFAVOURITE = "http://production.folives.com/api/android/v1/unfavourite";
    public static String CUSTOMER_FOOD_ITEMS = "http://production.folives.com/api/android/v1/daily_menu";
    public static String CUSTOMER_PIN_CODE = "http://production.folives.com/api/android/v1/post_code";
    public static String CUSTOMER_CHEF_ITEMS_REVIEWS = "http://production.folives.com/api/android/v1/chef";
    public static String CUSTOMER_ADDRESS = "http://production.folives.com/api/android/v1/user_address";
    public static String CUSTOMER_ADD_ADDRESS = "http://production.folives.com/api/android/v1/add_address";
    public static String CUSTOMER_DELETE_ADDRESS = "http://production.folives.com/api/android/v1/delete_address";
    public static String CUSTOMER_UPDATE_ADDRESS = "http://production.folives.com/api/android/v1/update_address";
    public static String CUSTOMER_CART_VERIFY = "http://production.folives.com/api/android/v1/cart_verify";
    public static String CUSTOMER_ORDER_PLACE = "http://production.folives.com/api/android/v1/order_place";
    public static String CUSTOMER_ORDER_CANCEL = "http://production.folives.com/api/android/v1/order_cancel";
    public static String CUSTOMER_CHEF_ITEM_REVIEWS = "http://production.folives.com/api/android/v1/chef_item_reviews";
    public static String CUSTOMER_ITEM_REVIEW = "http://production.folives.com/api/android/v1/add_item_review";
    public static String CUSTOMER_COD_GENERATE_OTP = "http://production.folives.com/api/android/v1/generate_cod_otp";
    public static String CUSTOMER_COD_VERIFY_OTP = "http://production.folives.com/api/android/v1/verify_cod_otp";
    public static String CUSTOMER_COD_COMPLETE_ORDER = "http://production.folives.com/api/android/v1/order_complete";
    public static String CUSTOMER_TRACK_ORDER = "http://production.folives.com/api/android/v1/track_order";

    public static String CUSTOMER_FORGOT_PASSWORD_EMAIL_SUBMIT = "http://production.folives.com/api/android/v1/forgot_user_password";
    public static String CUSTOMER_VERIFY_USER_EMAILCODE = "http://production.folives.com/api/android/v1/verify_user_email_code";
    public static String CUSTOMER_CHANGE_PASSWORD = "http://production.folives.com/api/android/v1/change_user_password";

    public static String MERCHANT_FORGOT_PASSWORD_EMAIL_SUBMIT = "http://production.folives.com/api/android/v1/forgot_chef_password";
    public static String MERCHANT_VERIFY_USER_EMAILCODE = "http://production.folives.com/api/android/v1/verify_chef_email_code";
    public static String MERCHANT_CHANGE_PASSWORD = "http://production.folives.com/api/android/v1/change_chef_password";

    public static String NEW_MERCHANT_DASHBOARD = "http://production.folives.com/api/android/v1/dashboard";
    public static String NEW_MERCHANT_ACCOUNT_STATUS_CHANGE = "http://production.folives.com/api/android/v1/chef_account_status_change";
    public static String NEW_MERCHANT_DAILY_FOODMENU = "http://production.folives.com/api/android/v1/chef_daily_menu";
    public static String NEW_MERCHANT_DAILY_FOODMENU_QTY_UPDATE = "http://production.folives.com/api/android/v1/update_daily_menu_food_item";
    public static String NEW_MERCHANT_FOLLOWERS = "http://production.folives.com/api/android/v1/followers";
    public static String NEW_MERCHANT_NEW_ORDERS = "http://production.folives.com/api/android/v1/new_orders";
    public static String NEW_MERCHANT_NOTIFICATIONS = "http://production.folives.com/api/android/v1/notifications";
    public static String NEW_MERCHANT_FOOD_NEW_ORDER_DETAIL = "http://production.folives.com/api/android/v1/new_order_details";
    public static String NEW_MERCHANT_ORDER_HISTORY = "http://production.folives.com/api/android/v1/order_history";
    public static String NEW_MERCHANT_REVIEWS = "http://production.folives.com/api/android/v1/customer_reviews";
    public static String NEW_MERCHANT_WITHDRAWALS = "http://production.folives.com/api/android/v1/withdrawals";
    public static String NEW_MERCHANT_WITHDRAWALS_REQUEST = "http://production.folives.com/api/android/v1/withdrawal_request";
    public static String NEW_MERCHANT_WITHDRAWALS_REQUEST_CANCEL = "http://production.folives.com/api/android/v1/cancel_withdrawal_request";
    public static String NEW_MERCHANT_SETTINGS = "http://production.folives.com/api/android/v1/settings";
    public static String NEW_MERCHANT_SETTINGS_CHANGE = "http://production.folives.com/api/android/v1/general_settings_status_change";
    public static String NEW_MERCHANT_FOOD_ITEMS = "http://production.folives.com/api/android/v1/food_item";
    public static String NEW_MERCHANT_FOOD_ITEMS_STATE_CHANGE = "http://production.folives.com/api/android/v1/food_item_status_change";
    public static String NEW_MERCHANT_FOOD_ITEMS_DELETE = "http://production.folives.com/api/android/v1/delete_food_item";

    public static String LOGIN_FLAG = "signin";

    public static String deltaS = "";
    public static String scrollS = "";
    public static String PlaceFlag = "0";

    public static HashMap<String, String> filter_check_states = new HashMap<String, String>();

    public static String UserAddressId = "";
    public static String UserAddress = "";
    public static String UserZipCode = "";
    public static String UserAddressFlag = "0";
    public static String TimeSlotValue = "0";

    public static ArrayList<String> MNDelSlotAL = new ArrayList<String>();
    public static ArrayList<String> BFDelSlotAL = new ArrayList<String>();
    public static ArrayList<String> LNDelSlotAL = new ArrayList<String>();
    public static ArrayList<String> DNDelSlotAL = new ArrayList<String>();

    public static String HomeFlag = "0";
    public static String isSetAddress = "0";
    public static String isSetAddressID = "0";
    public static String isSetAddressValue = "0";
    public static String CompleteFlag = "0";
}
