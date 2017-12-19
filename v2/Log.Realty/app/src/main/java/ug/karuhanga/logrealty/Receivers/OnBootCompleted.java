package ug.karuhanga.logrealty.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by karuhanga on 10/27/17.
 * Gets a heads up when device is restarted
 */

public class OnBootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            performRequiredActions(context);
        }
    }

    private void performRequiredActions(Context context) {
        OnCheckForNotificationsTime.setUpNotifierSchedule(context);
        OnBackupTime.setUpBackupSchedule(context);
    }
}
