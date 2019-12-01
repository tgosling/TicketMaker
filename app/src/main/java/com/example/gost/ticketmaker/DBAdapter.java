package com.example.gost.ticketmaker;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String KEY_ROW_ID = "_tickID";
    static final String KEY_LIC_PLATE = "license";
    static final String KEY_PROV = "province";
    static final String KEY_DATE = "date";
    static final String KEY_TIME = "time";
    static final String KEY_INFRAC = "infraction";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "TicketDB";
    static final String DATABASE_TABLE = "tickets";
    //check version(?)
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE =
            "create table tickets(_tickID integer primary key autoincrement," +
                    "license text not null, province text not null, date text not null, time text not null," +
                    "infraction text not null);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) { super(context, DATABASE_NAME,
                null, DATABASE_VERSION);}

        @Override
        public void onCreate(SQLiteDatabase db){
            try{
                db.execSQL(DATABASE_CREATE);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ",which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS constacts");
            onCreate(db);
        }
    }
}
