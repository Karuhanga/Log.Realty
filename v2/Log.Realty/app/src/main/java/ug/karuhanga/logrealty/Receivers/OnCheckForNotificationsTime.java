package ug.karuhanga.logrealty.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import java.util.Calendar;

import ug.karuhanga.logrealty.Services.Notifier;

import static ug.karuhanga.logrealty.Helper.REQUEST_CODE_NOTIFY;

/**
 * Created by karuhanga on 10/26/17.
 * Called Everyday to check for due tenants
 */

public class OnCheckForNotificationsTime extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationServiceIntent= new Intent(context, Notifier.class);
        context.startService(notificationServiceIntent);
    }

    public static void setUpNotifierSchedule(Context context){
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent= new Intent(context, OnCheckForNotificationsTime.class);
        PendingIntent alarmIntent= PendingIntent.getBroadcast(context, REQUEST_CODE_NOTIFY, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);

        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }


}
