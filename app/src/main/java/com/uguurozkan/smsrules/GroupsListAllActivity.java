/*
 * Copyright (c) 2015.
 * This code belongs to Uğur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class GroupsListAllActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private SmsDetailsDBHelper detailsDB;
    private GroupsDBHelper rulesDB;
    private ListView listViewGroups;
    private GroupsListAdapter groupsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_groups);

        rulesDB = getGroupsDB();
        detailsDB = getSmsDetailsDB();


        //populateSMSRulesDB();
        //populateSMSDetailsDB();

        groupsListAdapter = new GroupsListAdapter(this, rulesDB.getRuleGroupsCursor());
        listViewGroups = (ListView) findViewById(R.id.listViewGroups);
        listViewGroups.setAdapter(groupsListAdapter);
        listViewGroups.setOnItemClickListener(this);
    }

    private GroupsDBHelper getGroupsDB() {
        if (rulesDB == null) {
            rulesDB = new GroupsDBHelper(this);
        }
        return rulesDB;
    }

    private SmsDetailsDBHelper getSmsDetailsDB() {
        if (detailsDB == null) {
            detailsDB = new SmsDetailsDBHelper(this);
        }
        return detailsDB;
    }

    private void populateSMSRulesDB() {
        rulesDB.insertEntry("SPAM", "indirim", null, null);
        rulesDB.insertEntry("SPAM", "kredi", null, null);
        rulesDB.insertEntry("BANK", null, "AKBANK", null);
        rulesDB.insertEntry("Uncategorized", null, null, null);
        rulesDB.insertEntry("TEST", "What!?", "Uğur Özkan", "Hmm.");
        rulesDB.insertEntry(null, "Thx.", "Uğur Özkan", "yw");
        rulesDB.insertEntry("TEST", null, "Uğur Özkan", null);
    }

    private void populateSMSDetailsDB() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        int i = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String sender = cursor.getString(cursor.getColumnIndex(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_ADDRESS)).toString();
                String body = cursor.getString(cursor.getColumnIndex(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_BODY)).toString();
                String date = cursor.getString(cursor.getColumnIndex(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_DATE)).toString();
                String status = cursor.getString(cursor.getColumnIndex(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_READ)).toString();
                detailsDB.insertEntry("Uncategorized", sender, body, date, status);
                i++;
            }
            while (cursor.moveToNext() && i<40);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        groupsListAdapter.notifyDataSetChanged();
        listViewGroups.invalidateViews();
        listViewGroups.refreshDrawableState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailsDB.close();
        rulesDB.close();
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
        if (id == R.id.action_Add) {
            Intent groupAddIntent = new Intent(this, GroupsAddActivity.class);
            startActivity(groupAddIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<String> ruleGroups = rulesDB.getRuleGroups();
        Intent intent = new Intent(this, SmsDetailsListAllActivity.class);
        intent.putExtra("groupName", ruleGroups.get(position));
        startActivity(intent);
    }
}
