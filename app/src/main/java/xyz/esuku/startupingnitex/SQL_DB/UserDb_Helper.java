package xyz.esuku.startupingnitex.SQL_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class UserDb_Helper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserDb";

    // Login table name
    private static final String TABLE_USER = "userTbl";
    private static final String TABLE_DOWNLINE = "DownlineTbl";
    private static final String TABLE_CARD = "CardTbl";

    public UserDb_Helper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);

    }

//    public UserDb_Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " +TABLE_USER +  "(" +
                "user_id INTEGER ," +
                "user_name TEXT, " +
                "full_name TEXT, " +
                "phone_no TEXT, " +
                "age TEXT, " +
                "sex TEXT, " +
                "city TEXT, " +
                "business_name TEXT, " +
                "website_url TEXT, " +
                "user_image TEXT, " +
                "user_interest TEXT," +
                "user_experience TEXT, " +
                "user_stage TEXT, " +
                "email TEXT," +
                "service_type TEXT," +
                "service_status TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String table                =TABLE_USER;
        String[] columns            =null;
        String selection            =null;
        String[] selectionArgs      =null;
        String groupBy              =null;
        String having               =null;
        String orderBy              =null;
        String limit                =null;

        Cursor cursor   = db.query(table,columns,selection,selectionArgs,groupBy,having,orderBy,limit);

        if(cursor.moveToNext()){
            user.put("user_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("user_id"))));
            user.put("user_name", String.valueOf(cursor.getString(cursor.getColumnIndex("user_name"))));
            user.put("full_name", String.valueOf(cursor.getString(cursor.getColumnIndex("full_name"))));
            user.put("phone_no", String.valueOf(cursor.getString(cursor.getColumnIndex("phone_no"))));
            user.put("age", String.valueOf(cursor.getString(cursor.getColumnIndex("age"))));
            user.put("sex", String.valueOf(cursor.getString(cursor.getColumnIndex("sex"))));
            user.put("city", String.valueOf(cursor.getString(cursor.getColumnIndex("city"))));
            user.put("business_name", String.valueOf(cursor.getString(cursor.getColumnIndex("business_name"))));
            user.put("website_url", String.valueOf(cursor.getString(cursor.getColumnIndex("website_url"))));
            user.put("user_image", String.valueOf(cursor.getString(cursor.getColumnIndex("user_image"))));
            user.put("user_interest", String.valueOf(cursor.getString(cursor.getColumnIndex("user_interest"))));
            user.put("user_experience", String.valueOf(cursor.getString(cursor.getColumnIndex("user_experience"))));
            user.put("user_stage", String.valueOf(cursor.getString(cursor.getColumnIndex("user_stage"))));
            user.put("email", String.valueOf(cursor.getString(cursor.getColumnIndex("user_stage"))));
            user.put("service_type", String.valueOf(cursor.getString(cursor.getColumnIndex("service_type"))));
            user.put("service_status", String.valueOf(cursor.getString(cursor.getColumnIndex("service_status"))));

        }

        db.close();


         Log.e("Fetching UserDAta", user.toString());
        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        //db.delete(TABLE_CARD, null, null);
        db.close();

        Log.d("Deleted all user", "Table Deleted");
    }

    public void update_profile(String user_name, String full_name, String phone_no, String city, String business_name, String website_url){
        SQLiteDatabase db = this.getWritableDatabase();

        String table = TABLE_USER;
        ContentValues values = new ContentValues();
        values.put("full_name", full_name);
        values.put("phone_no", phone_no);
        values.put("city", city);
        values.put("business_name", business_name);
        values.put("website_url", website_url);
//        values.put("email", email);
//        values.put("age", age);
//        values.put("sex", sex);

        String whereClause = "user_name =?";
        String[] whereArgs = {user_name};
        db.update(table, values, whereClause, whereArgs);
        db.close();
    }

    public void update_profile_pic(String user_name, String user_img) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = TABLE_USER;
        ContentValues values = new ContentValues();
        values.put("user_image", user_img);

        String whereClause = "user_name =?";
        String[] whereArgs = {user_name};
        db.update(table, values, whereClause, whereArgs);
        db.close();
    }



    public void update_service_stauts(String user_name, String service_status, String service_type) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = TABLE_USER;
        ContentValues values = new ContentValues();
        values.put("service_status", service_status);
        values.put("service_type", service_type);

        String whereClause = "user_name =?";
        String[] whereArgs = {user_name};
        db.update(table, values, whereClause, whereArgs);
        db.close();
    }

    public void DeleteUserDB_n_otherTableifExist(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM "+ TABLE_USER ;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount >0) {
            //
            deleteUser();
            Log.e("DeleteUser","Deleting DB");
        }
    }



}
