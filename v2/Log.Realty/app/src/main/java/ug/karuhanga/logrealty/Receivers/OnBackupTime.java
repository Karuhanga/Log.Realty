package ug.karuhanga.logrealty.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ug.karuhanga.logrealty.Services.Backup;

import static ug.karuhanga.logrealty.Helper.REQUEST_CODE_BACKUP;

public class OnBackupTime extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent backupServiceIntent= new Intent(context, Backup.class);
        context.startService(backupServiceIntent);
    }

    public static void setUpBackupSchedule(Context context){
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent backupIntent= new Intent(context, OnBackupTime.class);
        PendingIntent alarmIntent= PendingIntent.getBroadcast(context, REQUEST_CODE_BACKUP, backupIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 3);

        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, alarmIntent);
    }
}
