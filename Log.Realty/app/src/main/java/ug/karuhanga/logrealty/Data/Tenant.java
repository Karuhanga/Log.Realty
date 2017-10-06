package ug.karuhanga.logrealty.Data;

import java.util.Date;

import ug.karuhanga.logrealty.Helpers;

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

    private House house;

    public Tenant(){
    }

    public Tenant(String fName, String oNames, String email, String contact, Date entered, String idType, String idNo, House house) {
        this.fName = fName;
        this.oNames = oNames;
        this.email = email;
        this.contact = contact;
        this.entered = entered;
        this.rentDue = entered;
        this.idType = idType;
        this.idNo = idNo;
        this.house = house;
        this.ex= false;
    }

    public boolean updateRentDue(Payment payment){
        Date newDue = Helpers.dueUpdater(this.rentDue, payment.getAmount(), this.house.getRent());
        if (newDue==null){
            return false;
        }
        this.rentDue= newDue;
        return true;
    }

    @Override
    public String toString(){
        return this.fName+" "+this.oNames+"\n"+this.house.toString();
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
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public boolean isEx() {
        return ex;
    }

    public void setEx(boolean ex) {
        this.ex = ex;
    }
}
