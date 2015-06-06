/*
 * Copyright (c) 2015.
 * This code belongs to Uğur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmsDigestReceiver extends BroadcastReceiver {
    public SmsDigestReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent scheduledIntent = new Intent(context, GroupsListAllActivity.class);
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(scheduledIntent);
    }
}
