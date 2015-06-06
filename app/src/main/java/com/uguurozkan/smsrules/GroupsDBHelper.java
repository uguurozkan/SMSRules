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

public class GroupsDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME             = "SmsDetailsAndRules.db";
    public static final String SMS_RULES_TABLE_NAME      = "RulesTable";
    public static final String SMS_RULES_COLUMN_ID       = "id";
    public static final String SMS_RULES_COLUMN_GROUP    = "ruleGroup";
    public static final String SMS_RULES_COLUMN_VALUE    = "value";
    public static final String SMS_RULES_COLUMN_FROM     = "fromWhom";
    public static final String SMS_RULES_COLUMN_REPLY    = "reply";

    private SQLiteDatabase db;

    public GroupsDBHelper(Context context) {
        this(context, DATABASE_NAME, null, 1);
    }

    public GroupsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                        + SMS_RULES_TABLE_NAME + " ( "
                        + SMS_RULES_COLUMN_ID + " INTEGER PRIMARY KEY, "
                        + SMS_RULES_COLUMN_GROUP + " TEXT, "
                        + SMS_RULES_COLUMN_VALUE + " TEXT, "
                        + SMS_RULES_COLUMN_FROM + " TEXT, "
                        + SMS_RULES_COLUMN_REPLY + " TEXT )"
        );
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        this.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SMS_RULES_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertEntry(String group, String value, String from, String replyMessage) {
        this.close();
        db = getDatabase(true);
        ContentValues contentValues = populateContentValues(group, value, from, replyMessage);
        db.insert(SMS_RULES_TABLE_NAME, null, contentValues);
        return true;
    }

    private SQLiteDatabase getDatabase(boolean isWritable) {
        if (db != null && db.isOpen()) {
            db.close();
        }

        if (isWritable) {
            return this.getWritableDatabase();
        } else {
            return this.getReadableDatabase();
        }
    }

    public boolean updateEntry(Integer id, String group, String value, String from, String replyMessage) {
        this.close();
        db = getDatabase(true);
        ContentValues contentValues = populateContentValues(group, value, from, replyMessage);
        db.update(SMS_RULES_TABLE_NAME, contentValues, SMS_RULES_COLUMN_ID + "=", new String[]{Integer.toString(id)});
        return true;
    }

    private ContentValues populateContentValues(String ruleGroup, String value, String from, String replyMessage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SMS_RULES_COLUMN_GROUP, ruleGroup);
        contentValues.put(SMS_RULES_COLUMN_VALUE, value);
        contentValues.put(SMS_RULES_COLUMN_FROM, from);
        contentValues.put(SMS_RULES_COLUMN_REPLY, replyMessage);
        return contentValues;
    }

    public Integer deleteEntry(Integer id) {
        this.close();
        db = getDatabase(true);
        return db.delete(SMS_RULES_TABLE_NAME, SMS_RULES_COLUMN_ID + "=", new String[]{Integer.toString(id)});
    }

    public Cursor getDataById(int id) {
        this.close();
        db = getDatabase(false);
        return db.rawQuery("SELECT *" +
                " FROM " + SMS_RULES_TABLE_NAME +
                " WHERE " + SMS_RULES_COLUMN_ID + "=" + id, null);
    }

    public Cursor getDataBy(String columnName, String value) {
        this.close();
        db = getDatabase(false);
        return db.rawQuery("SELECT *" +
                " FROM " + SMS_RULES_TABLE_NAME +
                " WHERE " + columnName + "='" + value + "'", null);
    }

    public int getNumberOfRows() {
        this.close();
        db = getDatabase(false);
        return (int) DatabaseUtils.queryNumEntries(db, SMS_RULES_TABLE_NAME);
    }

    public ArrayList<String> getRuleGroups() {
        this.close();
        db = getDatabase(false);
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + SMS_RULES_COLUMN_GROUP +
                " FROM " + SMS_RULES_TABLE_NAME, null);

        ArrayList<String> ruleGroups = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ruleGroups.add(cursor.getString(cursor.getColumnIndex(SMS_RULES_COLUMN_GROUP)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return ruleGroups;
    }

    public Cursor getRuleGroupsCursor() {
        this.close();
        db = getDatabase(false);
        return db.rawQuery("SELECT DISTINCT " + SMS_RULES_COLUMN_GROUP +
                " FROM " + SMS_RULES_TABLE_NAME, null);
    }

    public Cursor getColumn(String columnName) {
        this.close();
        db = getDatabase(false);
        return db.rawQuery("SELECT DISTINCT " + columnName +
                " FROM " + SMS_RULES_TABLE_NAME, null);
    }

    public Cursor getAll() {
        this.close();
        db = getDatabase(false);
        return db.rawQuery("SELECT DISTINCT * " +
                " FROM " + SMS_RULES_TABLE_NAME, null);
    }

}
