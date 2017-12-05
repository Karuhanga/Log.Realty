package ug.karuhanga.logrealty.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orm.query.Select;

import java.util.List;

import ug.karuhanga.logrealty.Models.Notification;

import static ug.karuhanga.logrealty.Models.Notification.restore;

/**
 * Created by karuhanga on 10/27/17.
 */

public class OnRestoreNotifications extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            List<Notification> notifications= Select.from(Notification.class).list();
            for (Notification notification: notifications) {
                restore(context, notification);
            }
        }
    }
}
