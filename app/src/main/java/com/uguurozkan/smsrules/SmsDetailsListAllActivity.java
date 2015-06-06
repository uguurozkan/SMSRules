/*
 * Copyright (c) 2015.
 * This code belongs to Uğur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Uğur Özkan on 6/4/2015.
 */
public class SmsDetailsListAllActivity extends Activity implements AdapterView.OnItemClickListener{

    private SmsDetailsDBHelper detailsDB;
    private ListView listViewSMS;
    private SmsDetailsListAdapter smsDetailsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_sms);

        String groupName = getIntent().getStringExtra("groupName");
        detailsDB = getSmsDetailsDB();

        smsDetailsListAdapter = new SmsDetailsListAdapter(this, detailsDB.getDataBy(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_GROUP, groupName));
        listViewSMS = (ListView) findViewById(R.id.listViewSMS);
        listViewSMS.setAdapter(smsDetailsListAdapter);
        listViewSMS.setOnItemClickListener(this);
    }

    private SmsDetailsDBHelper getSmsDetailsDB() {
        if (detailsDB == null) {
            detailsDB = new SmsDetailsDBHelper(this);
        }
        return detailsDB;
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
