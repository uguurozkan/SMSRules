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

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Uğur Özkan on 6/4/2015.
 */
public class GroupsDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME             = "SmsDetailsAndRules.db";
    public static final String SMS_RULES_TABLE_NAME      = "SmsRules";
    public static final String SMS_RULES_COLUMN_ID       = "id";
    public static final String SMS_RULES_COLUMN_GROUP    = "ruleGroup";
    public static final String SMS_RULES_COLUMN_VALUE    = "value";
    public static final String SMS_RULES_COLUMN_FROM     = "fromWhom"; //from
    private ArrayList<String> ruleGroups;

    public GroupsDBHelper(Context context) {
        this(context, DATABASE_NAME, null, 1);
    }

    public GroupsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        ruleGroups = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                        + SMS_RULES_TABLE_NAME      + " ( "
                        + SMS_RULES_COLUMN_ID       + " INTEGER PRIMARY KEY, "
                        + SMS_RULES_COLUMN_GROUP    + " TEXT, "
                        + SMS_RULES_COLUMN_VALUE    + " TEXT, "
                        + SMS_RULES_COLUMN_FROM     + " TEXT )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SMS_RULES_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertEntry(String group, String value, String from) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = populateContentValues(group, value, from);
        db.insert(SMS_RULES_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateEntry(Integer id, String group, String value, String from) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = populateContentValues(group, value, from);
        db.update(SMS_RULES_TABLE_NAME, contentValues, SMS_RULES_COLUMN_ID + "=", new String[]{Integer.toString(id)});
        return true;
    }

    private ContentValues populateContentValues(String ruleGroup, String value, String from) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SMS_RULES_COLUMN_GROUP, ruleGroup);
        contentValues.put(SMS_RULES_COLUMN_VALUE, value);
        contentValues.put(SMS_RULES_COLUMN_FROM, from);
        return contentValues;
    }

    public Integer deleteEntry(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SMS_RULES_TABLE_NAME, SMS_RULES_COLUMN_ID + "=", new String[]{Integer.toString(id)});
    }

    public Cursor getDataById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM " + SMS_RULES_TABLE_NAME +
                " WHERE " + SMS_RULES_COLUMN_ID + "=" + id, null);
        return cursor;
    }

    public Cursor getDataBy(String columnName, String value) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM " + SMS_RULES_TABLE_NAME +
                " WHERE CONTAINS(" + columnName + ", '" + value + "')", null);
        return cursor;
    }

    public int getNumberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SMS_RULES_TABLE_NAME);
        return numRows;
    }

    public ArrayList<String> getRuleGroups() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + SMS_RULES_COLUMN_GROUP +
                " FROM " + SMS_RULES_TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ruleGroups.add(cursor.getString(cursor.getColumnIndex(SMS_RULES_COLUMN_GROUP)));
            } while (cursor.moveToNext());
        }
        return ruleGroups;
    }

    public Cursor getRuleGroupsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + SMS_RULES_COLUMN_GROUP +
                " FROM " + SMS_RULES_TABLE_NAME, null);
        return cursor;
    }
}
