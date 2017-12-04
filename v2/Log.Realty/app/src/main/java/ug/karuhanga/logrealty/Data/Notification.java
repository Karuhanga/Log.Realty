package ug.karuhanga.logrealty.Data;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.Calendar;
import java.util.List;

import ug.karuhanga.logrealty.R;
import ug.karuhanga.logrealty.Receivers.OnAlertTime;

import static android.content.Context.NOTIFICATION_SERVICE;
import static ug.karuhanga.logrealty.Helpers.FLAG_ONE_DAY_AFTER;
import static ug.karuhanga.logrealty.Helpers.FLAG_ONE_DAY_TO;
import static ug.karuhanga.logrealty.Helpers.REQUEST_CODE_NOTIFY;
import static ug.karuhanga.logrealty.Helpers.dateToString;
import static ug.karuhanga.logrealty.Helpers.getTodaysDate;

/**
 * Created by karuhanga on 10/20/17.
 */

public class Notification extends Record {
    private Tenant tenant;
    private int statusFlag;

    public Notification() {
    }

    public Notification(Tenant tenant) {
        this.tenant = tenant;
        this.statusFlag= FLAG_ONE_DAY_TO;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public static boolean schedule(Context context, Tenant tenant, boolean newPayment){
        if (newPayment){
            List<Notification> results= Select.from(Notification.class).where(Condition.prop(NamingHelper.toSQLNameDefault("tenant")).eq(tenant)).list();
            if (results.size()>0){
                results.get(0).delete();
            }
        }
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notifIntent= new Intent(context, OnAlertTime.class);
        int flag= FLAG_ONE_DAY_TO;

        Calendar calendar= Calendar.getInstance();
        calendar.setTime(tenant.getRentDue());
        notifIntent.putExtra("title", "Rent Due: "+tenant.getHouse().getLocation().getName());
        notifIntent.putExtra("date", calendar.getTimeInMillis());

        List<Notification> notifs= Select.from(Notification.class).where(Condition.prop(NamingHelper.toSQLNameDefault("tenant")).eq(tenant)).list();

        if (notifs.size()<1){
            flag= FLAG_ONE_DAY_TO;
            notifIntent.putExtra("message", tenant.getName()+"'s next rent installment is due tomorrow");
            Notification notification= new Notification(tenant);
            notification.save();
            notifIntent.putExtra("notif", notification.getId());
        }
        else if(notifs.get(0).getStatusFlag()==FLAG_ONE_DAY_TO){
            flag= FLAG_ONE_DAY_AFTER;
            notifs.get(0).setStatusFlag(FLAG_ONE_DAY_AFTER);
            notifIntent.putExtra("message", tenant.getName()+"'s rent installment was due yesterday");
            notifs.get(0).save();
            notifIntent.putExtra("notif", notifs.get(0).getId());
        }
        else{
            flag= notifs.get(0).getStatusFlag()+5;
            notifs.get(0).setStatusFlag(flag);
            notifIntent.putExtra("message", tenant.getName()+"'s rent installment was due on "+dateToString(tenant.getRentDue()));
            notifs.get(0).save();
            notifIntent.putExtra("notif", notifs.get(0).getId());
        }

        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+flag);

        if (calendar.getTime().before(getTodaysDate())){
            return schedulePaymentNotification(context, tenant, false);
        }

        PendingIntent alarmIntent= PendingIntent.getBroadcast(context, REQUEST_CODE_NOTIFY, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

        return true;
    }

    public static boolean restore(Context context, Notification notification){
        Tenant tenant= notification.getTenant();
        //TODO message action
        //TODO
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notifIntent= new Intent(context, OnAlertTime.class);

        Calendar calendar= Calendar.getInstance();
        calendar.setTime(tenant.getRentDue());
        notifIntent.putExtra("title", "Rent Due: "+tenant.getHouse().getLocation().getName());
        notifIntent.putExtra("date", calendar.getTimeInMillis());

        switch (notification.getStatusFlag()){
            case FLAG_ONE_DAY_TO:
                notifIntent.putExtra("message", tenant.getName()+"'s next rent installment is due tomorrow");
                notifIntent.putExtra("notif", notification.getId());
                break;
            case FLAG_ONE_DAY_AFTER:
                notifIntent.putExtra("message", tenant.getName()+"'s rent installment was due yesterday");
                notifIntent.putExtra("notif", notification.getId());
                break;
            default:
                notifIntent.putExtra("message", tenant.getName()+"'s rent installment was due on "+dateToString(tenant.getRentDue()));
                notifIntent.putExtra("notif", notification.getId());
                break;
        }

        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+notification.getStatusFlag());

        if (calendar.getTime().before(getTodaysDate())){
            return schedulePaymentNotification(context, tenant, true);
        }


        PendingIntent alarmIntent= PendingIntent.getBroadcast(context, REQUEST_CODE_NOTIFY, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

        return true;
    }

    public static boolean displayNotification(Context context, String title, String message){
        NotificationCompat.Builder notifBuilder= new NotificationCompat.Builder(context);
        notifBuilder.setSmallIcon(R.drawable.ic_timeline_black_24dp).setContentTitle(title).setContentText(message).setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, Gist.class), 0)).setAutoCancel(true).setOnlyAlertOnce(false);
        notifBuilder.addAction(R.drawable.ic_person_add_black_24dp, "Message", PendingIntent.getActivity(context, 0, new Intent(context, AddPayment.class), 0));//change to start text
        notifBuilder.addAction(R.drawable.ic_add_black_24dp, "Add Payment", PendingIntent.getActivity(context, 0, new Intent(context, AddPayment.class), 0));

        int notifID= (int) System.currentTimeMillis();
        ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).notify(notifID, notifBuilder.build());
        return true;
    }

    public String summarize(){
        return toString();
    }
}
