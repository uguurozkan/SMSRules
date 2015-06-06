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

public class SmsDetailsDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME                = "SmsDetailsAndRules.db";
    public static final String SMS_DETAILS_TABLE_NAME       = "SmsDetailsTable";
    public static final String SMS_DETAILS_COLUMN_ID        = "id";
    public static final String SMS_DETAILS_COLUMN_GROUP     = "ruleGroup";
    public static final String SMS_DETAILS_COLUMN_ADDRESS   = "address";
    public static final String SMS_DETAILS_COLUMN_BODY      = "body";
    public static final String SMS_DETAILS_COLUMN_DATE      = "date";
    public static final String SMS_DETAILS_COLUMN_READ      = "read";

    private SQLiteDatabase db;

    public SmsDetailsDBHelper(Context context) {
        this(context, DATABASE_NAME, null, 1);
    }

    public SmsDetailsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                        + SMS_DETAILS_TABLE_NAME + " ("
                        + SMS_DETAILS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + SMS_DETAILS_COLUMN_GROUP + " TEXT, "
                        + SMS_DETAILS_COLUMN_ADDRESS + " TEXT, "
                        + SMS_DETAILS_COLUMN_BODY + " TEXT, "
                        + SMS_DETAILS_COLUMN_DATE + " TEXT, "
                        + SMS_DETAILS_COLUMN_READ + " TEXT)"
        );
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        this.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SMS_DETAILS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertEntry(String group, String sender, String smsBody, String date, String status) {
        this.close();
        db = getDatabase(true);
        ContentValues contentValues = populateContentValues(group, sender, smsBody, date, status);
        db.insert(SMS_DETAILS_TABLE_NAME, null, contentValues);
        return true;
    }

    private SQLiteDatabase getDatabase(boolean isWritable) {
        if (db != null && db.isOpen()) {
            db.close();
        }

        if (isWritable) {
            db = this.getWritableDatabase();
        } else {
            db = this.getReadableDatabase();
        }

        return db;
    }

    public boolean updateEntry(Integer id, String group, String sender, String smsBody, String date, String status) {
        this.close();
        db = getDatabase(true);
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
        this.close();
        db = getDatabase(true);
        return db.delete(SMS_DETAILS_TABLE_NAME, SMS_DETAILS_COLUMN_ID + "=", new String[]{Integer.toString(id)});
    }

    public Cursor getDataById(int id) {
        this.close();
        db = getDatabase(false);
        return db.rawQuery("SELECT *" +
                " FROM " + SMS_DETAILS_TABLE_NAME +
                " WHERE " + SMS_DETAILS_COLUMN_ID + "=" + id, null);
    }

    public Cursor getDataBy(String columnName, String value) {
        this.close();
        db = getDatabase(false);
        return db.rawQuery("SELECT *" +
                " FROM " + SMS_DETAILS_TABLE_NAME +
                " WHERE " + columnName + "='" + value + "'", null);
    }

    public int getNumberOfRows() {
        this.close();
        db = getDatabase(false);
        return (int) DatabaseUtils.queryNumEntries(db, SMS_DETAILS_TABLE_NAME);
    }

}
