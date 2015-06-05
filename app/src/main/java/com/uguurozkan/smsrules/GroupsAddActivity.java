/*
 * Copyright (c) 2015.
 * This code belongs to Uğur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class GroupsAddActivity extends Activity {

    GroupsDBHelper rulesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_add);
        rulesDB = new GroupsDBHelper(this);
    }

    public void addFilter(View view) {
        String value = ((EditText) findViewById(R.id.editTextValue)).getText().toString();
        String groupName = ((EditText) findViewById(R.id.editTextGroupName)).getText().toString();
        String from = ((EditText) findViewById(R.id.editTextFrom)).getText().toString();

        rulesDB.insertEntry(groupName, value, from);
        finish();
    }

}
