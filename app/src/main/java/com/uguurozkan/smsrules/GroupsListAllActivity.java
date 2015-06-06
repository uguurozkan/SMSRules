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

        initDBs();
        initView();
    }

    private void initDBs() {
        rulesDB = getGroupsDB();
        detailsDB = getSmsDetailsDB();
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

    private void initView() {
        groupsListAdapter = new GroupsListAdapter(this, rulesDB.getRuleGroupsCursor());
        listViewGroups = (ListView) findViewById(R.id.listViewGroups);
        listViewGroups.setAdapter(groupsListAdapter);
        listViewGroups.setOnItemClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        groupsListAdapter.notifyDataSetChanged();
        listViewGroups.invalidateViews();
        listViewGroups.refreshDrawableState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
        } else if (id == R.id.action_Delay) {
            Intent delayService = new Intent(this, SMSDigestService.class);
            startService(delayService);
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
