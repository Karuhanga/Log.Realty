package ug.karuhanga.logrealty.Data;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Helpers;

/**
 * Created by karuhanga on 8/25/17.
 */

public class Location extends Record {
    private String name;
    private android.location.Location location;
    private int defaultRent;

    public Location(){

    }

    public Location(String name, android.location.Location location, int defaultRent) {
        this.name = name;
        this.location = location;
        this.defaultRent= defaultRent;
    }

    public Location(String name, int defaultRent) {
        this.name = name;
        this.location = null;
        this.defaultRent= defaultRent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public android.location.Location getLocation() {
        return location;
    }

    public void setLocation(android.location.Location location) {
        this.location = location;
    }

    public int getDefaultRent() {
        return defaultRent;
    }

    public void setDefaultRent(int defaultRent) {
        this.defaultRent = defaultRent;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public String getSummary(){
        int number= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(this)).list().size();
        if (number==1){
            return name+"\n"+String.valueOf(number)+" property";
        }
        else{
            return name+"\n"+String.valueOf(number)+" properties";
        }
    }

    @Override
    protected boolean onDelete(){
        List<House> results= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(this)).list();

        for (House house:results ) {
            house.delete();
            //TODO Add Error Checking
        }
        return true;
    }
}
