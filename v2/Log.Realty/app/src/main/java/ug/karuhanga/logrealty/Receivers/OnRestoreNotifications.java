package ug.karuhanga.logrealty.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.orm.query.Select;

import java.util.List;

import ug.karuhanga.logrealty.Data.Notification;

import static ug.karuhanga.logrealty.Helpers.restorePaymentNotification;

/**
 * Created by karuhanga on 10/27/17.
 */

public class OnRestoreNotifications extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            List<Notification> notifications= Select.from(Notification.class).list();
            for (Notification notification: notifications) {
                restorePaymentNotification(context, notification);
            }
        }
    }
}
