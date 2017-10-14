package ug.karuhanga.logrealty.Data;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Helpers;

/**
 * Created by karuhanga on 8/25/17.
 */

public class House extends Record {
    private int number;
    private int rent;
    private String description;

    private Location location;

    public House(){}

    public House(int number, String description, Location location) {
        this.number = number;
        this.description = description;
        this.location = location;
        this.rent= location.getDefaultRent();
    }

    public House(Location location) {
        this.location = location;
        this.number= 0;
        this.rent= location.getDefaultRent();
        this.description= "Single House";
    }

    public House(Location location, int rent) {
        this.location = location;
        this.number= 0;
        this.rent= rent;
        this.description= "Single House";
    }

    public House(int number, String description, Location location, int rent) {
        this.number = number;
        this.rent = rent;
        this.description= description;
        this.location = location;
    }

    @Override
    public String toString(){
        if (number==0){
            return this.location.getName();
        }
        return location+"\nHouse "+String.valueOf(number)+": "+description;
    }

    @Override
    public String getSummary(){
        if (number==0){
            return this.location.getName()+"\n"+ Helpers.toCurrency(rent);
        }
        return "House "+String.valueOf(number)+": "+description+"\n"+location+"\n"+Helpers.toCurrency(rent);
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    protected boolean onDelete(){
        List<Tenant> results= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("house")).eq(this)).list();

        for (Tenant tenant:results ) {
            tenant.delete();
            //TODO Add Error Checking
        }
        return true;
    }
}
