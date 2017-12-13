package com.led_on_off.led;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by BiruskSterk on 12/7/2017.
 */

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "gercek_zamanli";//database adý

    private static final String TABLE_NAME = "kullanicilar";
    private static String KULLANICI_ADI = "kullanici_adi";
    private static String SIFRE = "sifre";
    private static String ID = "id";

    private static String SENSOR_ID = "sensor_id";
    private static String SENSOR_ADİ = "sensor_adi";
    private static String SENSOR_DURUMU = "sensor_durumu";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KULLANICI_ADI + " TEXT,"
                + SIFRE + " TEXT,"
                + ")";

        String CREATE_TABLE_2 = "CREATE TABLE " + TABLE_NAME + "("
                + SENSOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SENSOR_ADİ + " TEXT,"
                + SENSOR_DURUMU + " TEXT,"
                + ")";
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void kullaniciEkle(String kullanici_adi, String sifre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KULLANICI_ADI, kullanici_adi);
        values.put(SIFRE, sifre);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void SensorDurumEkle(String sensor_adi, String sensor_durumu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SENSOR_ADİ, sensor_adi);
        values.put(SENSOR_DURUMU, sensor_durumu);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public HashMap<String, String> kullaniciDetay(int id){


        HashMap<String,String> kullanici_1 = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            kullanici_1.put(KULLANICI_ADI, cursor.getString(1));
            kullanici_1.put(SIFRE, cursor.getString(2));

        }
        cursor.close();
        db.close();

        return kullanici_1;
    }

    public HashMap<String, String> sensorDetay(int id){


        HashMap<String,String> sensorler = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            sensorler.put(SENSOR_ADİ, cursor.getString(1));
            sensorler.put(SENSOR_DURUMU, cursor.getString(2));

        }
        cursor.close();
        db.close();

        return sensorler;
    }

    public ArrayList<HashMap<String, String>> kullanicilar(){


        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> kullanicilist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                kullanicilist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();

        return kullanicilist;
    }
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }


    public void resetTables(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

}
