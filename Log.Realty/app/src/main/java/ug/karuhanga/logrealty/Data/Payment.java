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
    private int rate;

    private Long tenant;


    public Payment() {
    }

    public Payment(Date date, int amount, Tenant tenant) {
        this.date = date;
        this.amount = amount;
        this.tenant = tenant.getId();
        this.rate= tenant.getHouse().getRent();
    }

    public Payment(Date date, Tenant tenant, int months){
        this.date = date;
        this.rate= tenant.getHouse().getRent();
        this.amount = this.rate*months;
        this.tenant = tenant.getId();
    }

    @Override
    public String toString(){
        return toCurrency(amount)+" paid by "+getTenant().getName();
    }

    @Override
    public String getSummary(){
        Tenant tenantObject= getTenant();
        return toCurrency(amount)+"\n"+dateToString(date)+"\n"+ tenantObject.getName()+"- "+tenantObject.getHouse().getLocation().getName()+" (House "+String.valueOf(tenantObject.getHouse().getNumber())+")";
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
        return Tenant.findById(Tenant.class, this.tenant);
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant.getId();
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean onNewPaymentAdded(){
        getTenant().updateRentDue(this);
        return true;
    }

    @Override
    protected boolean onDelete(){
        this.amount= 0-amount;
        getTenant().updateRentDue(this);
        this.amount= 0-amount;
        //TODO Add Error Checking
        return true;
    }
}
