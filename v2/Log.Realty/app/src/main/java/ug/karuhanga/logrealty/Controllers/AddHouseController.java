package ug.karuhanga.logrealty.Controllers;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Models.House;
import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.Views.AddHouse.AddHouseActivityExternalInterface;

import static ug.karuhanga.logrealty.Helper.AMOUNT_MINIMUM_RENT;
import static ug.karuhanga.logrealty.Helper.ERROR_REQUIRED;
import static ug.karuhanga.logrealty.Helper.getRentBelowMinNotif;

/**
 * Created by karuhanga on 12/5/17.
 */

public class AddHouseController implements AddHouseActivityExternalInterface {

    Controller.AddHouseControllerExternalInterface dashboard;
    Location chosen;

    public AddHouseController(Controller.AddHouseControllerExternalInterface dashboard){
        this.dashboard= dashboard;
        chosen= null;
    }

    @Override
    public void onSubmit(int houseNo, String description, long rentPaid) {
        if (location_is_valid(false) && amount_is_valid(rentPaid) && house_is_valid(houseNo)){
            House created= new House(houseNo, description, chosen, rentPaid);
            finish(created.save(), created.toString());
        }
    }

    @Override
    public void onSubmit(long rentPaid) {
        if (location_is_valid(true) && amount_is_valid(rentPaid)){
            House created= new House(chosen, rentPaid);
            finish(created.save(), created.toString());
        }
    }

    @Override
    public void onSubmit(int houseNo, String description) {
        if (location_is_valid(false) && house_is_valid(houseNo)){
            House created= new House(houseNo, description, chosen);
            finish(created.save(), created.toString());
        }
    }

    @Override
    public void onSubmit() {
        if (location_is_valid(true)){
            House created= new House(chosen);
            finish(created.save(), created.toString());
        }
    }

    @Override
    public List<Location> getLocationData() {
        return Select.from(Location.class).list();
    }

    @Override
    public void onLocationPick(Location location) {
        if (location==null){
            dashboard.complainAboutLocation(ERROR_REQUIRED);
            return;
        }
        this.chosen= location;
    }

    private boolean location_is_valid(boolean singleHouse){
        if (chosen==null){
            dashboard.complainAboutLocation(ERROR_REQUIRED);
            return false;
        }
        if (singleHouse && House.getHouseCountAtLocation(chosen)>0){
            dashboard.complainAboutLocation("This house was already added");
            return false;
        }
        return true;
    }

    private boolean amount_is_valid(long rent){
        boolean valid= rent>=AMOUNT_MINIMUM_RENT;
        if (valid){
            return valid;
        }
        else{
            dashboard.complainAboutRent(getRentBelowMinNotif());
            return false;
        }
    }

    private boolean house_is_valid(int houseNumber){
        if (houseNumber==0){
            dashboard.complainAboutHouse("Invalid house number");
            return false;
        }

        List<House> data= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(houseNumber)).list();
        data.addAll(Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(0)).list());
        if (data.size()>0){
            dashboard.complainAboutHouse("House already added");
            return false;
        }
        return true;
    }

    private void finish(long id, String item){
        dashboard.finish(id, item);
    }
}
