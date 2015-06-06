/*
 * Copyright (c) 2015.
 * This code belongs to Uğur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Uğur Özkan on 6/4/2015.
 */
public class SMSReceiver extends BroadcastReceiver {

    private GroupsDBHelper rulesDB;
    private SmsDetailsDBHelper detailsDB;
    private Context ctxt;
    private Calendar c = Calendar.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        String address = getSenderNum(intent.getExtras());
        String messageBody = getMessageBody(intent.getExtras());

        this.ctxt = context;
        rulesDB = new GroupsDBHelper(context);
        detailsDB = new SmsDetailsDBHelper(context);
        filterSms(address, messageBody);
        //abortBroadcast();
    }

    private void filterSms(String address, String messageBody) {
        Cursor cursorAll = rulesDB.getAll();

        if (cursorAll != null && cursorAll.moveToFirst()) {
            do {
                String group = cursorAll.getString(cursorAll.getColumnIndex(GroupsDBHelper.SMS_RULES_COLUMN_GROUP));
                String value = cursorAll.getString(cursorAll.getColumnIndex(GroupsDBHelper.SMS_RULES_COLUMN_VALUE));
                String from = cursorAll.getString(cursorAll.getColumnIndex(GroupsDBHelper.SMS_RULES_COLUMN_FROM));
                String reply = cursorAll.getString(cursorAll.getColumnIndex(GroupsDBHelper.SMS_RULES_COLUMN_REPLY));

                if (address.equals(from) && !value.equals("") && messageBody.contains(value)) {
                    filter(address, messageBody, group, reply);
                } else if(address.equals(from)) {
                    filter(address, messageBody, group, reply);
                } else if (messageBody.contains(value)) {
                    filter(address, messageBody, group, reply);
                }
            } while (cursorAll.moveToNext());
        } else {
            if (!rulesDB.getRuleGroups().contains("Uncategorized")) {
                rulesDB.insertEntry("Uncategorized", null, null, null);
            }
            detailsDB.insertEntry("Uncategorized", address, messageBody, c.get(Calendar.SECOND) + "", false + "");
        }
    }

    private void filter(String address, String messageBody, String group, String reply) {
        if (group != null && !group.equals("")) {
            detailsDB.insertEntry(group, address, messageBody, c.get(Calendar.SECOND) + "", false + "");
        }

        if (reply != null && !reply.equals("")) {
            try {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(address, null, reply, null, null);

                showMessageInHistory(address, reply);
            } catch (Exception e) {
                Log.d("SMSM", "couldn't send\n" + e.getCause());
            }
        }
    }

    private void showMessageInHistory(String address, String message) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("body", message);
        ctxt.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
    }

    private String getSenderNum(Bundle intentExtras) {
        if (intentExtras == null)
            return null;

        final Object[] pdus = (Object[]) intentExtras.get("pdus");
        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
        return currentMessage.getDisplayOriginatingAddress();
    }

    private String getMessageBody(Bundle intentExtras) {
        if (intentExtras == null)
            return null;

        String message = "";
        final Object[] pdus = (Object[]) intentExtras.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            message += currentMessage.getDisplayMessageBody();
        }

        return message;
    }
}
