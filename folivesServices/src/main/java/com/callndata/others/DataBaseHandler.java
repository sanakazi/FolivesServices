package com.callndata.others;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHandler {

    private static final String DATABASE_NAME = "Folives_DB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_FOOD_ITEM_CART = "FoodItemCartTable";

    // FavoriteProductTable attributes
    private static final String MENU_ID = "column_menu_id";
    private static final String FOOD_ID = "column_food_id";
    private static final String FOOD_NAME = "column_food_name";
    private static final String FOOD_PRICE = "column_food_price";
    private static final String FOOD_CART_COUNT = "column_cart_count";
    private static final String FOOD_CART_TOTAL_PRICE = "column_cart_total_price";
    private static final String FOOD_IMAGE = "column_food_image";
    private static final String FOOD_QTY = "column_food_qty";
    private static final String FOOD_CATEGORY = "column_food_category";

    // Create FoodCartTable Query
    private static final String SQL_TABLE_CREATE_QUERY_FOR_FOOD_CART = "CREATE TABLE " + TABLE_NAME_FOOD_ITEM_CART + "("
            + MENU_ID + " TEXT," + FOOD_ID + " TEXT," + FOOD_NAME + " TEXT," + FOOD_PRICE + " INTEGER," + FOOD_CART_COUNT + " INTEGER,"
            + FOOD_CART_TOTAL_PRICE + " INTEGER," + FOOD_IMAGE + " TEXT, " + FOOD_CATEGORY + " TEXT, " + FOOD_QTY + " TEXT" + ")";

    static SQLiteDatabase db;
    DataBaseHelper dbhelper;
    Context ctx;

    public DataBaseHandler(Context ctx) {
        this.ctx = ctx;
        dbhelper = new DataBaseHelper(ctx);
    }

    public static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d("Table", SQL_TABLE_CREATE_QUERY_FOR_FOOD_CART);

            try {
                db.execSQL(SQL_TABLE_CREATE_QUERY_FOR_FOOD_CART);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SQL_TABLE_CREATE_QUERY_FOR_FOOD_CART);
            onCreate(db);
        }

    }

    public DataBaseHandler open() {
        db = dbhelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbhelper.close();
    }

    // ************------FoodCartTable Operations-------************

    public String insertDataIntoTable(String menu_id, String food_id, String food_name, String food_price, String cart_count,
                                      String cart_total_price, String food_image, String food_avl, String food_cat) {

        String returnStatus = "";
        long checkStatus = 0;

        ContentValues values = new ContentValues();
        values.put(MENU_ID, menu_id);
        values.put(FOOD_ID, food_id);
        values.put(FOOD_NAME, food_name);
        values.put(FOOD_PRICE, Integer.parseInt(food_price));
        values.put(FOOD_CART_COUNT, Integer.parseInt(cart_count));
        values.put(FOOD_CART_TOTAL_PRICE, Integer.parseInt(cart_total_price));
        values.put(FOOD_IMAGE, food_image);
        values.put(FOOD_QTY, food_avl);
        values.put(FOOD_CATEGORY, food_cat);

        checkStatus = db.insert(TABLE_NAME_FOOD_ITEM_CART, null, values);
        returnStatus = "inserted";

        if (checkStatus > 0) {

        } else {
            returnStatus = "error";
        }

        return returnStatus;
    }

    // Fetch All Content from FoodItemTable
    public Cursor fetchAllData() {

        String columns[] = {MENU_ID, FOOD_ID, FOOD_NAME, FOOD_PRICE, FOOD_CART_COUNT, FOOD_CART_TOTAL_PRICE, FOOD_IMAGE,
                FOOD_QTY, FOOD_CATEGORY};
        return db.query(TABLE_NAME_FOOD_ITEM_CART, columns, null, null, null, null, null);
    }

    public String getCount() {
        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_NAME_FOOD_ITEM_CART);
        return Long.toString(cnt);
    }

    public static boolean CheckIsIdExist(String food_id) {
        String Query = "Select * from " + TABLE_NAME_FOOD_ITEM_CART + " where " + FOOD_ID + " = " + food_id;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public static Cursor FetchSpecificData(String category) {
        String Query = "Select * from " + TABLE_NAME_FOOD_ITEM_CART + " where " + FOOD_CATEGORY + " = '" + category + "'";
        Cursor cursor = db.rawQuery(Query, null);
        return cursor;
    }

    public static Cursor FetchFoodIds() {
        String Query = "Select " + FOOD_ID + ", " + FOOD_CART_COUNT + " from " + TABLE_NAME_FOOD_ITEM_CART;
        Cursor cursor = db.rawQuery(Query, null);
        return cursor;
    }

    public void updateFoodCart(String food_id, String count) {
        ContentValues values = new ContentValues();
        values.put(FOOD_CART_COUNT, count);
        db.update(TABLE_NAME_FOOD_ITEM_CART, values, FOOD_ID + " = ?", new String[]{food_id});
    }

    public void updateFoodCartTPrice(String food_id, String t_price) {
        ContentValues values = new ContentValues();
        values.put(FOOD_CART_TOTAL_PRICE, t_price);
        db.update(TABLE_NAME_FOOD_ITEM_CART, values, FOOD_ID + " = ?", new String[]{food_id});
    }

    public String SumOfPrice() {

        int amount;
        Cursor c = db.rawQuery("select sum(" + FOOD_CART_TOTAL_PRICE + ") from " + TABLE_NAME_FOOD_ITEM_CART, null);
        if (c.moveToFirst())
            amount = c.getInt(0);
        else
            amount = -1;
        c.close();
        return Integer.toString(amount);
    }

    public void deleteAllRecords() {
        db.execSQL("delete from " + TABLE_NAME_FOOD_ITEM_CART);
    }

    public void deleteRecord(String id) {
        db.execSQL("delete from " + TABLE_NAME_FOOD_ITEM_CART + " where " + FOOD_ID + " = " + id);
    }

    public void updateRecord(String id, String cnt, String TAmt) {
        db.execSQL("UPDATE " + TABLE_NAME_FOOD_ITEM_CART + " SET " + FOOD_CART_COUNT + " = " + cnt + " WHERE " + FOOD_ID
                + " = " + id);
        db.execSQL("UPDATE " + TABLE_NAME_FOOD_ITEM_CART + " SET " + FOOD_CART_TOTAL_PRICE + " = " + TAmt + " WHERE "
                + FOOD_ID + " = " + id);
    }
}
