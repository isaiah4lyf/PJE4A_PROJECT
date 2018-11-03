package com.example.isaia.sss_mobile_app.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SSS_DATABASE_V13.db";
    public static final String CONTACTS_TABLE_NAME = "Login_State";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "User_Name";
    public static final String CONTACTS_COLUMN_PASSWORD = "Password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table Accuracy_Management " +
                        "(id integer primary key, Check_Accuracy text,Preview_Running_Status text)"
        );
        db.execSQL(
                "create table Login_State " +
                        "(id integer primary key, User_Name text,Password text)"
        );

        db.execSQL(
                "create table Face_Recognition_Settings " +
                        "(id integer primary key, Image_Prediction_Service_Status text,Image_Prediction_Interval text, Image_Upload_Service_Status text, Verification_Interval text)"
        );

        db.execSQL(
                "create table Voice_Recognition_Settings " +
                        "(id integer primary key, Voice_Prediction_Service_Status text,Voice_Upload_Service_Status text)"
        );

        db.execSQL(
                "create table Users " +
                        "(id integer primary key, User_ID text,User_Name text)"
        );

        db.execSQL(
                "create table User_Applications " +
                        "(id integer primary key, User_ID text,Application_Name text, Application_Access_Name text)"
        );

        db.execSQL(
                "create table User_Registration_Table " +
                        "(id integer primary key, User_Name_String text,Phone_Number text, Password_String text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Login_State");
        db.execSQL("DROP TABLE IF EXISTS Face_Recognition_Settings");
        db.execSQL("DROP TABLE IF EXISTS Voice_Recognition_Settings");
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS User_Applications");
        onCreate(db);
    }




    //Login Functions
    public boolean insert_user_reg_temp_data(String User_Name_String, String Phone_Number,String Password_String) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS User_Registration_Table");
            db.execSQL(
                    "create table User_Registration_Table " +
                            "(id integer primary key, User_Name_String text,Phone_Number text, Password_String text)"
            );
            ContentValues contentValues = new ContentValues();
            contentValues.put("User_Name_String", User_Name_String);
            contentValues.put("Phone_Number", Phone_Number);
            contentValues.put("Password_String", Password_String);
            db.insert("User_Registration_Table", null, contentValues);

        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }
    public String User_Name_String()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from User_Registration_Table", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("User_Name_String"));
    }
    public String Phone_Number()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from User_Registration_Table", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("Phone_Number"));
    }
    public boolean drop_user_reg_temp_data() {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS User_Registration_Table");
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }
    public String Password_String()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from User_Registration_Table", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("Password_String"));
    }
    public boolean insert_Login_State (String User_Name, String Password) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS Login_State");
            db.execSQL(
                    "create table Login_State" +
                            "(id integer primary key, User_Name text,Password text)"
            );
            ContentValues contentValues = new ContentValues();
            contentValues.put("User_Name", User_Name);
            contentValues.put("Password", Password);
            db.insert("Login_State", null, contentValues);

        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }


    public String User_Name()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Login_State", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME));
    }
    public String Password()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Login_State", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex(CONTACTS_COLUMN_PASSWORD));
    }
    public int Number_Of_Rows_Settings_Voice(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "Voice_Recognition_Settings");
        return numRows;
    }

    public boolean Insert_Settings_Voice(String Voice_Prediction_Service_Status, String Voice_Upload_Service_Status) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS Voice_Recognition_Settings");
            db.execSQL(
                    "create table Voice_Recognition_Settings" +
                            "(id integer primary key, Voice_Prediction_Service_Status text,Voice_Upload_Service_Status text)"
            );
            ContentValues contentValues = new ContentValues();
            contentValues.put("Voice_Prediction_Service_Status", Voice_Prediction_Service_Status);
            contentValues.put("Voice_Upload_Service_Status", Voice_Upload_Service_Status);
            db.insert("Voice_Recognition_Settings", null, contentValues);

        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }
    public int Number_Of_Rows_Settings_Image(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "Face_Recognition_Settings");
        return numRows;
    }
    public boolean Insert_Settings_Image(String Image_Prediction_Service_Status, String Image_Upload_Interval, String Image_Upload_Service_Status,String Verification_Interval) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS Face_Recognition_Settings");
            db.execSQL(
                    "create table Face_Recognition_Settings" +
                            "(id integer primary key, Image_Prediction_Service_Status text,Image_Upload_Interval text,Image_Upload_Service_Status text, Verification_Interval text)"
            );
            ContentValues contentValues = new ContentValues();
            contentValues.put("Image_Prediction_Service_Status", Image_Prediction_Service_Status);
            contentValues.put("Image_Upload_Interval", Image_Upload_Interval);
            contentValues.put("Image_Upload_Service_Status", Image_Upload_Service_Status);
            contentValues.put("Verification_Interval", Verification_Interval);
            db.insert("Face_Recognition_Settings", null, contentValues);
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }


    /// Users and Applications Management
    public boolean Insert_User(String User_ID, String User_Name) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("User_ID", User_ID);
            contentValues.put("User_Name", User_Name);
            db.insert("Users", null, contentValues);
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }

    public int Number_Of_Users(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "Users");
        return numRows;
    }
    public ArrayList<String> Get_Users() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Users", null );
        res.moveToFirst();
        while(res.isAfterLast() == false)
        {
            array_list.add(res.getString(res.getColumnIndex("User_Name")));
            res.moveToNext();
        }
        return array_list;
    }
    public Integer Remove_User (String User_Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Users",
                "User_Name = ? ",
                new String[] { User_Name });
    }







    ///Accuracy Management
    public boolean insert_Accuracy_Management(String Check_Accuracy, String Preview_Running_Status) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS Accuracy_Management");
            db.execSQL(
                    "create table Accuracy_Management" +
                            "(id integer primary key, Check_Accuracy text,Preview_Running_Status text)"
            );
            ContentValues contentValues = new ContentValues();
            contentValues.put("Check_Accuracy", Check_Accuracy);
            contentValues.put("Preview_Running_Status", Preview_Running_Status);
            db.insert("Accuracy_Management", null, contentValues);
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }
    public int Number_Of_Rows_insert_Accuracy_Management(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "Accuracy_Management");
        return numRows;
    }

    public String Get_Check_Accuracy()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Accuracy_Management", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("Check_Accuracy"));
    }
    public String Get_Preview_Running_Status()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Accuracy_Management", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("Preview_Running_Status"));
    }
    public boolean Update_insert_Accuracy_Management(String Check_Accuracy, String Preview_Running_Status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Check_Accuracy", Check_Accuracy);
        contentValues.put("Preview_Running_Status", Preview_Running_Status);
        db.update("Accuracy_Management", contentValues, "id = ? ", new String[] { Integer.toString(1) } );
        return true;
    }




    // Settings Functions
    ////Image Prediction
    public String Get_Image_Prediction_Service_Status()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Face_Recognition_Settings", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("Image_Prediction_Service_Status"));
    }
    public String Get_Image_Upload_Interval()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Face_Recognition_Settings", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("Image_Upload_Interval"));
    }
    public String Get_Image_Upload_Service_Status()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Face_Recognition_Settings", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("Image_Upload_Service_Status"));
    }
    public String Get_Image_Verification_Interval()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Face_Recognition_Settings", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("Verification_Interval"));
    }
    public boolean Update_Face_Recog_Settings(String Image_Prediction_Service_Status, String Image_Upload_Interval, String Image_Upload_Service_Status,String Verification_Interval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Image_Prediction_Service_Status", Image_Prediction_Service_Status);
        contentValues.put("Image_Upload_Interval", Image_Upload_Interval);
        contentValues.put("Image_Upload_Service_Status", Image_Upload_Service_Status);
        contentValues.put("Verification_Interval", Verification_Interval);
        db.update("Face_Recognition_Settings", contentValues, "id = ? ", new String[] { Integer.toString(1) } );
        return true;
    }




    ////Voice Prediction
    public String Get_Voice_Prediction_Service_Status()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Voice_Recognition_Settings", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("Voice_Prediction_Service_Status"));
    }

    public String Get_Voice_Upload_Service_Status()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Voice_Recognition_Settings", null );
        res.moveToLast();
        return res.getString(res.getColumnIndex("Voice_Upload_Service_Status"));
    }
    public boolean Update_Voice_Settings(String Voice_Prediction_Service_Status, String Voice_Upload_Service_Status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Voice_Prediction_Service_Status", Voice_Prediction_Service_Status);
        contentValues.put("Voice_Upload_Service_Status", Voice_Upload_Service_Status);
        db.update("Voice_Recognition_Settings", contentValues, "id = ? ", new String[] { Integer.toString(1) } );
        return true;
    }








    //Other
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Login_State where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String Password, String User_Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("User_Name", User_Name);
        contentValues.put("Password", Password);
        db.update("Login_State", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Login_State",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> get_Login_State() {
        ArrayList<String> array_list = new ArrayList<String>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Login_State", null );
        res.moveToFirst();
        while(res.isAfterLast() == false)
        {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}