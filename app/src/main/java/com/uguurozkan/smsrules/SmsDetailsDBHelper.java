/*
 * Copyright (c) 2015.
 * This code belongs to Uğur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by Uğur Özkan on 6/4/2015.
 */
public class SmsDetailsDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME                = "SmsDetailsAndRules.db";
    public static final String SMS_DETAILS_TABLE_NAME       = "SmsDetailsDB";
    public static final String SMS_DETAILS_COLUMN_ID        = "id";
    public static final String SMS_DETAILS_COLUMN_GROUP     = "ruleGroup";
    public static final String SMS_DETAILS_COLUMN_ADDRESS   = "address";
    public static final String SMS_DETAILS_COLUMN_BODY      = "body";
    public static final String SMS_DETAILS_COLUMN_DATE      = "date";
    public static final String SMS_DETAILS_COLUMN_READ      = "read";
    private HashMap hp;

    public SmsDetailsDBHelper(Context context) {
        this(context, DATABASE_NAME, null, 2);
    }

    public SmsDetailsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                        + SMS_DETAILS_TABLE_NAME     + " ("
                        + SMS_DETAILS_COLUMN_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + SMS_DETAILS_COLUMN_GROUP   + " TEXT, "
                        + SMS_DETAILS_COLUMN_ADDRESS + " TEXT, "
                        + SMS_DETAILS_COLUMN_BODY    + " TEXT, "
                        + SMS_DETAILS_COLUMN_DATE    + " TEXT, "
                        + SMS_DETAILS_COLUMN_READ    + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SMS_DETAILS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertEntry(String group, String sender, String smsBody, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = populateContentValues(group, sender, smsBody, date, status);
        db.insert(SMS_DETAILS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateEntry(Integer id, String group, String sender, String smsBody, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = populateContentValues(group, sender, smsBody, date, status);
        db.update(SMS_DETAILS_TABLE_NAME, contentValues, SMS_DETAILS_COLUMN_ID + "=", new String[]{Integer.toString(id)});
        return true;
    }

    private ContentValues populateContentValues(String group, String sender, String smsBody, String date, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SMS_DETAILS_COLUMN_GROUP, group);
        contentValues.put(SMS_DETAILS_COLUMN_ADDRESS, sender);
        contentValues.put(SMS_DETAILS_COLUMN_BODY, smsBody);
        contentValues.put(SMS_DETAILS_COLUMN_DATE, date);
        contentValues.put(SMS_DETAILS_COLUMN_READ, status);
        return contentValues;
    }

    public Integer deleteEntry(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SMS_DETAILS_TABLE_NAME, SMS_DETAILS_COLUMN_ID + "=", new String[]{Integer.toString(id)});
    }

    public Cursor getDataById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM " + SMS_DETAILS_TABLE_NAME +
                " WHERE " + SMS_DETAILS_COLUMN_ID + "=" + id, null);
        return cursor;
    }

    public Cursor getDataBy(String columnName, String value) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM " + SMS_DETAILS_TABLE_NAME +
                " WHERE " + columnName + "=" + value, null);
        return cursor;
    }

    public int getNumberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SMS_DETAILS_TABLE_NAME);
        return numRows;
    }

}
