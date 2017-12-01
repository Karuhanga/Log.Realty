package ug.karuhanga.logrealty.Receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import ug.karuhanga.logrealty.Data.Notification;
import ug.karuhanga.logrealty.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static ug.karuhanga.logrealty.Helpers.displayNotif;
import static ug.karuhanga.logrealty.Helpers.getTodaysDate;
import static ug.karuhanga.logrealty.Helpers.schedulePaymentNotification;

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

        displayNotif(context, intent.getStringExtra("title"), intent.getStringExtra("message"));
        schedulePaymentNotification(context, notification.getTenant(), false);
    }


}
