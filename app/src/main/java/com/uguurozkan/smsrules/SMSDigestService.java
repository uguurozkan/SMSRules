/*
 * Copyright (c) 2015.
 * This code belongs to Uğur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SmsDigestService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startPendingActivity();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startPendingActivity() {
        try {
            long interval = 12 * 60 * 1000; // n seconds

            Intent intent = new Intent(this.getApplicationContext(), SmsDigestReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT |  Intent.FILL_IN_DATA);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
