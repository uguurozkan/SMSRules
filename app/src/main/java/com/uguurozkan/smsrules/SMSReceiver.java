/*
 * Copyright (c) 2015.
 * This code belongs to U?ur �zkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;

/**
 * Created by Uğur Özkan on 6/4/2015.
 */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String address = getSenderName(context, intent.getExtras());
        String messageBody = getMessageBody(intent.getExtras());
        filter(address, messageBody);
        //abortBroadcast();
    }

    private void filter(String address, String messageBody) {

    }

    private String getSenderName(Context context, Bundle intentExtras) {
        String contactNum = getSenderNum(intentExtras);
        String contact = contactNum; // just to be sure

        Uri lookupURI = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.decode(contactNum));
        Cursor cursor = context.getContentResolver().query(lookupURI, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null);

        try {
            cursor.moveToFirst();
            contact = cursor.getString(0);
        } catch (Exception e) {
            contact = contactNum;
        } finally {
            cursor.close();
        }

        return contact;
    }

    private String getSenderNum(Bundle intentExtras) {
        if (intentExtras == null)
            return null;

        String number = "";
        final Object[] pdus = (Object[]) intentExtras.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            number += currentMessage.getDisplayOriginatingAddress();
        }
        return number;
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
