package com.example.gost.ticketmaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String KEY_ROW_ID = "_id";
    static final String KEY_TICK_ID = "tickID";
    static final String KEY_LIC_PLATE = "license";
    static final String KEY_PROV = "province";
    static final String KEY_CAR_MAN = "manufacturer";
    static final String KEY_CAR_MOD = "model";
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
            db.execSQL("DROP TABLE IF EXISTS tickets");
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException{
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() { DBHelper.close();}

    public long insertTicket(String tickID, String date, String timeStamp, String licPlate,
                             String prov, String carMan, String carModel, String infrac){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TICK_ID, tickID);
        initialValues.put(KEY_LIC_PLATE, licPlate);
        initialValues.put(KEY_PROV, prov);
        initialValues.put(KEY_CAR_MAN, carMan);
        initialValues.put(KEY_CAR_MOD, carModel);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_TIME, timeStamp);
        initialValues.put(KEY_INFRAC, infrac);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //public boolean deleteContact(long rowId){

    //}

    public Cursor getAllTickets(){
        return db.query(DATABASE_TABLE, new String[] {KEY_ROW_ID, KEY_TICK_ID, KEY_LIC_PLATE, KEY_CAR_MAN, KEY_CAR_MOD, KEY_DATE,
                        KEY_TIME, KEY_INFRAC}, null, null, null, null, null);
    }

    public Cursor getTicket(long rowId){
        Cursor mCursor =
                db.query(DATABASE_TABLE, new String[] {KEY_ROW_ID, KEY_TICK_ID, KEY_LIC_PLATE, KEY_CAR_MAN, KEY_CAR_MOD, KEY_DATE,
                        KEY_TIME, KEY_INFRAC}, KEY_ROW_ID + "=" + rowId, null, null, null, null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //updateTicket
}
