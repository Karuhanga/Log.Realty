package ug.karuhanga.logrealty.Data;

import android.util.Log;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Helpers;

import static ug.karuhanga.logrealty.Helpers.APP_TAG;

/**
 * Created by karuhanga on 8/25/17.
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

    public House(Location location, int rent) {
        this.location = location.getId();
        this.number= 0;
        this.rent= rent;
        this.description= "Single House";
    }

    public House(int number, String description, Location location, int rent) {
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
        return getLocation()+"\nHouse "+String.valueOf(number)+": "+description;
    }

    @Override
    public String summarize(){
        if (number==0){
            return getLocation().getName()+"\n"+ Helpers.toCurrency(rent);
        }
        return "House "+String.valueOf(number)+": "+description+"\n"+getLocation()+"\n"+Helpers.toCurrency(rent);
    }

    public long getRent() {
        return rent;
    }

    public void setRent(int rent) {
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

    static long getHouseCountAtLocation(Location location){
        long result= 0;
        try {
           result= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(location)).count();
        }catch (Exception e){
            Log.d(APP_TAG, e.toString());
        }
        return result;
    }
}
