package ug.karuhanga.logrealty.Data;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.Date;
import java.util.List;

import ug.karuhanga.logrealty.Helpers;

import static ug.karuhanga.logrealty.Helpers.dateToString;
import static ug.karuhanga.logrealty.Helpers.toCurrency;

/**
 * Created by karuhanga on 8/25/17.
 */

public class Tenant extends Record {
    private String fName;
    private String oNames;
    private String email;
    private String contact;
    private Date entered;
    private Date rentDue;
    private String idType;
    private String idNo;
    private boolean ex;
    private long balance;

    private Long house;

    public Tenant(){
    }

    public Tenant(String fName, String oNames, String email, String contact, Date entered, Date countStarts, String idType, String idNo, House house) {
        this.fName = fName;
        this.oNames = oNames;
        this.email = email;
        this.contact = contact;
        this.entered = entered;
        this.rentDue = countStarts;
        this.idType = idType;
        this.idNo = idNo;
        this.house = house.getId();
        this.ex= false;
        this.balance= 0;
    }

    public boolean updateRentDue(final Payment payment){
        Date newDue = Helpers.getLaterDateByMonths(this.rentDue, getPaidDuration(payment));
        if (newDue==null){
            return false;
        }
        this.rentDue= newDue;
        this.save();
        return true;
    }

    private long getPaidDuration(final Payment payment){
        long money_plus_balance= payment.getAmount()+getBalance();
        long months= money_plus_balance/payment.getRate();
        long balance= money_plus_balance- (payment.getRate()*months);
        setBalance(balance);
        return months;
    }

    public boolean onDelete(){
        List<Payment> results= Select.from(Payment.class).where(Condition.prop(NamingHelper.toSQLNameDefault("tenant")).eq(this.getId())).list();
        for (Payment payment : results) {
            payment.delete();
            //TODO Add Error Checking
        }

        List<Notification> notifs= Select.from(Notification.class).where(Condition.prop(NamingHelper.toSQLNameDefault("tenant")).eq(this)).list();
        for (Notification notification : notifs) {
            notification.delete();
            //TODO Add Error Checking
        }
        return true;
    }

    @Override
    public String toString(){
        return getfName()+" "+getoNames()+"\n"+getHouse().getLocation().getName()+"\nDue: "+dateToString(rentDue);
    }

    @Override
    public String summarize(){
        House houseObject= getHouse();
        return getfName()+" "+getoNames()+"\n"+houseObject.getLocation().getName()+"\n"+toCurrency(houseObject.getRent());
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getoNames() {
        return oNames;
    }

    public void setoNames(String oNames) {
        this.oNames = oNames;
    }

    public String getName(){
        return this.fName+" "+this.getoNames();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getEntered() {
        return entered;
    }

    public void setEntered(Date entered) {
        this.entered = entered;
    }

    public Date getRentDue() {
        return rentDue;
    }

    public void setRentDue(Date rentDue) {
        this.rentDue = rentDue;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public House getHouse() {
        return House.findById(House.class, this.house);
    }

    public void setHouse(House house) {
        this.house = house.getId();
    }

    public boolean isEx() {
        return ex;
    }

    public void setEx(boolean ex) {
        this.ex = ex;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
