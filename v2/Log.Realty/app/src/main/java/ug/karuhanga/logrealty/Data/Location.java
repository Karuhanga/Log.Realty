package ug.karuhanga.logrealty.Data;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

/**
 * Created by karuhanga on 8/25/17.
 */

public class Location extends Record {
    private String name;
    private android.location.Location location;
    private long defaultRent;

    public Location(){

    }

    public Location(String name, android.location.Location location, long defaultRent) {
        this.name = name;
        this.location = location;
        this.defaultRent= defaultRent;
    }

    public Location(String name, long defaultRent) {
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

    public long getDefaultRent() {
        return defaultRent;
    }

    public void setDefaultRent(long defaultRent) {
        this.defaultRent = defaultRent;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public String summarize(){
        long number= requestHouseCount();
        if (number==0){
            return name+"\n"+"No property added";
        }
        else if (number==1){
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

    private long requestHouseCount(){
        return House.getHouseCountAtLocation(this);
    }
}
