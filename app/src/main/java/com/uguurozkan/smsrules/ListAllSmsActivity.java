/*
 * Copyright (c) 2015.
 * This code belongs to U?ur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class ListAllSmsActivity extends Activity implements AdapterView.OnItemClickListener{

    private ListView listViewSMS;
    private Cursor cursor;
    private SMSListAdapter smsListAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_sms);
        listViewSMS = (ListView) findViewById(R.id.listViewSMS);
        cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        smsListAdapter = new SMSListAdapter(this, cursor);
        listViewSMS.setAdapter(smsListAdapter);
        listViewSMS.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textViewSmsSender = (TextView) view.findViewById(R.id.textViewSMSSender);
        TextView textViewSmsBody = (TextView) view.findViewById(R.id.textViewMessageBody);
        String smsSender = textViewSmsSender.getText().toString();
        String smsBody = textViewSmsBody.getText().toString();

        showDetailsDialog(smsSender, smsBody);
    }

    private void showDetailsDialog(String smsSender, String smsBody) {
        AlertDialog smsDetails = new AlertDialog.Builder(this).create();
        smsDetails.setTitle("From: " + smsSender);
        smsDetails.setMessage(smsBody);
        smsDetails.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        });

        smsDetails.show();
    }
}
