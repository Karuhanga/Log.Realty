package ug.karuhanga.logrealty.data;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by karuhanga on 8/25/17.
 */

public class Payment extends SugarRecord {
    private Date date;
    private int amount;

    private Tenant tenant;
    private House house;


    public Payment() {
    }

    public Payment(Date date, int amount, Tenant tenant, House house) {
        this.date = date;
        this.amount = amount;
        this.tenant = tenant;
        this.house = house;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
