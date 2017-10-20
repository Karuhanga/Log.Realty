package ug.karuhanga.logrealty.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ScheduledNotification extends Service {
    public ScheduledNotification() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent==null){

        }
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
