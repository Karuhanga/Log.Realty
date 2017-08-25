package ug.karuhanga.logrealty.data;

import com.orm.SugarRecord;

/**
 * Created by karuhanga on 8/25/17.
 */

class House extends SugarRecord {
    private int _id;
    private int rent;
    private String  description;


    private Location location;

    public House(){}

    public House(int _id, String description, Location location) {
        this._id = _id;
        this.description = description;
        this.location = location;
        this.rent= location.getDefaultRent();
    }

    public House(int _id, int rent, Location location, String description) {
        this._id = _id;
        this.rent = rent;
        this.description= description;
        this.location = location;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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
}
