/*
 * Copyright (c) 2015.
 * This code belongs to Uğur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.Date;

public class SMSDigestService extends Service {

    Date when = new Date(System.currentTimeMillis());

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMainActivity();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startMainActivity() {
        try {
            Intent someIntent = new Intent(this, GroupsListAllActivity.class);

            // note this could be getActivity if you want to launch an activity
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(),
                    0,
                    someIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarms = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            alarms.setRepeating(AlarmManager.RTC_WAKEUP,
                    when.getTime(),
                    AlarmManager.INTERVAL_HALF_DAY,
                    pendingIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //stopSelf();
    }


    public SMSDigestService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
