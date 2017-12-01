package ug.karuhanga.logrealty.Data;

import android.app.PendingIntent;

import static ug.karuhanga.logrealty.Helpers.FLAG_ONE_DAY_TO;

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
}
