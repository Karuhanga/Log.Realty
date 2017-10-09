package ug.karuhanga.logrealty.Data;

import java.util.Date;

import static ug.karuhanga.logrealty.Helpers.dateToString;
import static ug.karuhanga.logrealty.Helpers.toCurrency;

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
        return toCurrency(amount)+"\n"+dateToString(date)+"\n"+ tenant.getfName()+"- "+tenant.getHouse().getLocation().getName()+" (House "+String.valueOf(tenant.getHouse().getNumber())+")";
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

    public boolean onNewPaymentAdded(){
        tenant.updateRentDue(this);
        return true;
    }

    public boolean onPaymentDeleted(){
        this.amount= 0-amount;
        tenant.updateRentDue(this);
        this.amount= 0-amount;
        return true;
    }
}
