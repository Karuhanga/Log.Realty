package ug.karuhanga.logrealty.Data;

import java.util.Date;

import ug.karuhanga.logrealty.Record;

/**
 * Created by karuhanga on 8/25/17.
 */

public class Payment extends Record {
    private Date date;
    private int amount;

    private Tenant tenant;
    private House house;


    public Payment() {
    }

    public Payment(Date date, int amount, Tenant tenant) {
        this.date = date;
        this.amount = amount;
        this.tenant = tenant;
        this.house = tenant.getHouse();
    }

    @Override
    public String toString(){
        return String.valueOf(amount)+", paid by"+ tenant.getfName()+" "+tenant.getoNames();
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
