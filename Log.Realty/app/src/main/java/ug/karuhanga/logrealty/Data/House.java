package ug.karuhanga.logrealty.Data;

/**
 * Created by karuhanga on 8/25/17.
 */

public class House extends Record {
    private int number;
    private int rent;
    private String description;

    private Location location;
    private Tenant tenant;

    public House(){}

    public House(int number, String description, Location location) {
        this.number = number;
        this.description = description;
        this.location = location;
        this.rent= location.getDefaultRent();
    }

    public House(int number, int rent, Location location, String description) {
        this.number = number;
        this.rent = rent;
        this.description= description;
        this.location = location;
    }

    @Override
    public String toString(){
        //TODO Add tenant to description
        return "House "+String.valueOf(number)+": "+description+"\n"+location+"\n"+String.valueOf(rent);
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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
