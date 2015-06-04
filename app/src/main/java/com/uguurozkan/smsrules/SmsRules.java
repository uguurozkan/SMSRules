/*
 * Copyright (c) 2015.
 * This code belongs to Uğur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class SmsRules extends ActionBarActivity {
    SmsDetailsDBHelper detailsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_rules);


        Intent intent = new Intent(this, ListAllSmsActivity.class);
        startActivity(intent);
        populateDB();
    }

    private void populateDB() {
        detailsDB = new SmsDetailsDBHelper(this);
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String sender = cursor.getString(cursor.getColumnIndex(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_ADDRESS)).toString();
            String body = cursor.getString(cursor.getColumnIndex(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_BODY)).toString();
            String date = cursor.getString(cursor.getColumnIndex(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_DATE)).toString();
            String status = cursor.getString(cursor.getColumnIndex(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_READ)).toString();
            detailsDB.insertEntry("Uncategorized", sender, body, date, status);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sms_rules, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
