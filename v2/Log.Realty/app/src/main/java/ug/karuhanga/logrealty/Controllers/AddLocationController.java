package ug.karuhanga.logrealty.Controllers;


import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.Views.AddLocation;

import static ug.karuhanga.logrealty.Helper.AMOUNT_MINIMUM_RENT;
import static ug.karuhanga.logrealty.Helper.getRentBelowMinNotif;

/**
 * Created by karuhanga on 12/5/17.
 */

public class AddLocationController implements AddLocation.AddLocationActivityExternalInterface {

    Controller.AddLocationControllerExternalInterface dashboard;

    AddLocationController(Controller.AddLocationControllerExternalInterface dashboard){
        this.dashboard= dashboard;
    }

    @Override
    public void onSubmit(String location, long rent) {
        if (belowThreshold(rent)){
            dashboard.raise(getRentBelowMinNotif());
            return;
        }

        if (locationExists(location)){
            dashboard.complainAboutLocation("Already added location");
            return;
        }

        Location created= new Location(location, rent);
        finish(created.save(), created.toString());
    }

    private boolean locationExists(String location){
        if (Select.from(Location.class).where(Condition.prop(NamingHelper.toSQLNameDefault("name")).eq(location)).count()>0){
            return true;
        }
        return false;
    }

    private boolean belowThreshold(long rent){
        return rent<AMOUNT_MINIMUM_RENT;
    }

    private void finish(long id, String item){
        dashboard.finish(id, item);
    }
}
