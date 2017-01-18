package com.turtledev.pocketcounter.mDataBase;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by PavloByinyk.
 *  all data about myDB
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getName();
     private Context context;
     //DB
     static final String DATABASE_NAME = "DBCharges";
     private static int DB_VERSION = 2;
     //TableCharges table
     static final String CHARGES_TABLE_NAME = "TableCharges";
     static final String CHARGES_ID ="_id";
     static final String CHARGES_USER_ID ="_userId";
     public static final String CHARGES_NAME = "Name";
     //TableDetailCharges table
     static final String DETAIL_TABLE_NAME = "TableDetailCharges";
     public static final String PARENT_CHARGES_NAME ="parent_charge_name";
     public static final String DETAIL_CHARGES_ID ="object_ID";
     static final String DETAIL_CHARGES_USER_ID ="_userId";
     static final String DESCRIPTION = "Description";
     static final String TIME = "Time";
     static final String SUM = "Sum";
     //CreateTable
     static final String CREATE_CHARGE_TABLE = "CREATE TABLE "+ CHARGES_TABLE_NAME
            + " ( " + CHARGES_NAME +" STRING PRIMARY KEY ,"
            + CHARGES_ID + " STRING , "
            + CHARGES_USER_ID + " STRING);";
     static final String CREATE_DETAIL_CHARGE_TABLE = "CREATE TABLE "+ DETAIL_TABLE_NAME
             + " ( " + DETAIL_CHARGES_ID + " STRING PRIMARY KEY , "
             + PARENT_CHARGES_NAME + " VARCHAR(255) ,  "
             + DETAIL_CHARGES_USER_ID + " STRING , "
             + DESCRIPTION + " VARCHAR(255) , "
             + TIME + " VARCHAR(255) , "
             + SUM +" REAL);";
     //Drop Table
     static final String DROP_CHARGES_TABLE = "DROP TABLE IF EXISTS " + CHARGES_TABLE_NAME;
     static final String DROP_DETAIL_TABLE = "DROP TABLE IF EXISTS " + DETAIL_TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context=context;
    }
     @Override
     public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_CHARGE_TABLE);
            db.execSQL(CREATE_DETAIL_CHARGE_TABLE);
            Log.e(TAG , "Create DB");
        } catch (SQLException e) {
            Log.e(TAG , "ERROR in CREATE DB");
        }
    }
     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.e(TAG , "UPDATE DB");
            db.execSQL(DROP_CHARGES_TABLE);
            db.execSQL(DROP_DETAIL_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            Log.e(TAG , "ERROR UPDATE DB");
        }
    }
}
