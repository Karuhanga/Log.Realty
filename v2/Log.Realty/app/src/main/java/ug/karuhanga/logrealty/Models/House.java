package ug.karuhanga.logrealty.Models;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Helper;

import static ug.karuhanga.logrealty.Helper.log;

/**
 * Created by karuhanga on 8/25/17.
 * Basic Data Type and POJO for house data
 */

public class House extends Record {
    private int number;
    private long rent;
    private String description;

    private Long location;

    public House(){}

    public House(int number, String description, Location location) {
        this.number = number;
        this.description = description;
        this.location = location.getId();
        this.rent= location.getDefaultRent();
    }

    public House(Location location) {
        this.location = location.getId();
        this.number= 0;
        this.rent= location.getDefaultRent();
        this.description= "Single House";
    }

    public House(Location location, long rent) {
        this.location = location.getId();
        this.number= 0;
        this.rent= rent;
        this.description= "Single House";
    }

    public House(int number, String description, Location location, long rent) {
        this.number = number;
        this.rent = rent;
        this.description= description;
        this.location = location.getId();
    }

    @Override
    public String toString(){
        if (number==0){
            return getLocation().getName();
        }
        return getLocation()+": House "+String.valueOf(number)+" ("+description+")";
    }

    @Override
    public String summarize(){
        if (number==0){
            return getLocation().getName()+"\n"+ Helper.toCurrency(rent);
        }
        return "House "+String.valueOf(number)+": "+description+"\n"+getLocation()+"\n"+ Helper.toCurrency(rent);
    }

    public long getRent() {
        return rent;
    }

    public void setRent(long rent) {
        this.rent = rent;
    }

    public Location getLocation() {
        return Location.findById(Location.class, this.location);
    }

    public void setLocation(Location location) {
        this.location = location.getId();
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
        List<Tenant> results= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("house")).eq(this.getId())).list();

        for (Tenant tenant:results ) {
            tenant.delete();
            //TODO Add Error Checking
        }
        return true;
    }

    public static long getHouseCountAtLocation(Location location){
        long result= 0;
        try {
           result= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(location)).count();
        }catch (Exception e){
            log(e.toString());
        }
        return result;
    }
}
