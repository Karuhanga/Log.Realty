package ug.karuhanga.logrealty.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import java.util.Date;

import ug.karuhanga.logrealty.Models.Notification;

import static ug.karuhanga.logrealty.Models.Notification.displayNotification;

/**
 * Created by karuhanga on 10/26/17.
 */

public class OnAlertTime extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        Notification notification= Notification.findById(Notification.class, intent.getLongExtra("notif", 0));

        //do nothing if
        if (notification==null || notification.getTenant().getRentDue().after(new Date(intent.getLongExtra("date", 0)))){
            return;
        }

        displayNotification(context, intent.getStringExtra("title"), intent.getStringExtra("message"));
        Notification.schedule(context, notification.getTenant(), false);
    }


}
