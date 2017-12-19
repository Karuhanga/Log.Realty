package ug.karuhanga.logrealty.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ug.karuhanga.logrealty.Models.Tenant;
import ug.karuhanga.logrealty.R;
import ug.karuhanga.logrealty.Views.AddPayment;
import ug.karuhanga.logrealty.Views.Gist;

import static ug.karuhanga.logrealty.Helper.EX;
import static ug.karuhanga.logrealty.Helper.FALSE;
import static ug.karuhanga.logrealty.Helper.RENT_DUE;
import static ug.karuhanga.logrealty.Helper.dateToString;
import static ug.karuhanga.logrealty.Helper.getLaterDateByDays;

public class Notifier extends IntentService {
    private Context context;

    public Notifier() {
        super("Notifier Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        startNotificationChecks();
    }

    private void startNotificationChecks() {
        checkAndNotifyDayTo();
        checkAndNotifyDayAfter();
        checkAndNotifyDelinquents();
    }

    private void checkAndNotifyDelinquents() {
        List<Tenant> dayTos= getDelinquents();
        for (Tenant tenant : dayTos) {
            notify(getContext(), "Rent Due: "+tenant.getHouse().getLocation().getName(), tenant.getName()+"'s "+"next installment was due on "+dateToString(tenant.getRentDue())+".");
        }
    }

    private void checkAndNotifyDayAfter() {
        checkAndNotify("next installment was due yesterday", getDayAfters());
    }

    private void checkAndNotifyDayTo() {
        checkAndNotify("next rent payment is due tomorrow", getDayTos());
    }

    private void checkAndNotify(String message, List<Tenant> tenants) {
        for (Tenant tenant : tenants) {
            notify(getContext(), "Rent Due: "+tenant.getHouse().getLocation().getName(), tenant.getName()+"'s "+message+".");
        }
    }

    private List<Tenant> getDayTos(){
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Date now= calendar.getTime();
        Date oneDayForward= getLaterDateByDays(now, 2);
        return Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault(EX)).eq(FALSE)).and(Condition.prop(NamingHelper.toSQLNameDefault(RENT_DUE)).gt(now)).and(Condition.prop(NamingHelper.toSQLNameDefault(RENT_DUE)).lt(oneDayForward)).list();
    }

    private List<Tenant> getDayAfters(){
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Date now= calendar.getTime();
        Date oneDayBackwards= getLaterDateByDays(now, -2);
        return Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault(EX)).eq(FALSE)).and(Condition.prop(NamingHelper.toSQLNameDefault(RENT_DUE)).lt(now)).and(Condition.prop(NamingHelper.toSQLNameDefault(RENT_DUE)).gt(oneDayBackwards)).list();
    }

    private List<Tenant> getDelinquents(){
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Date now= calendar.getTime();
        Date fiveDaysBackwards= getLaterDateByDays(now, -5);
        return Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault(EX)).eq(FALSE)).and(Condition.prop(NamingHelper.toSQLNameDefault(RENT_DUE)).lt(now)).and(Condition.prop(NamingHelper.toSQLNameDefault(RENT_DUE)).gt(fiveDaysBackwards)).list();
    }

    public static boolean notify(Context context, String title, String message){
        NotificationCompat.Builder notifBuilder= new NotificationCompat.Builder(context);
        notifBuilder.setSmallIcon(R.drawable.ic_timeline_black_24dp).setContentTitle(title).setContentText(message).setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, Gist.class), 0)).setAutoCancel(true).setOnlyAlertOnce(false);
        notifBuilder.addAction(R.drawable.ic_message_black_24dp, "Message", PendingIntent.getActivity(context, 0, new Intent(context, AddPayment.class), 0));//TODO change to start text
        notifBuilder.addAction(R.drawable.ic_add_circle_outline_black_24dp, "Add Payment", PendingIntent.getActivity(context, 0, new Intent(context, AddPayment.class), 0));

        int notifID= (int) System.currentTimeMillis();
        ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).notify(notifID, notifBuilder.build());
        return true;
    }

    public Context getContext() {
        if (context==null){
            this.context= getBaseContext();
        }
        return context;
    }
}
