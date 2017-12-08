package ug.karuhanga.logrealty.Controllers;


import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.Views.AddLocation;

import static ug.karuhanga.logrealty.Helper.AMOUNT_MINIMUM_RENT;
import static ug.karuhanga.logrealty.Helper.ERROR_BELOW_MIN;

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
            dashboard.raise(ERROR_BELOW_MIN);
            return;
        }

        Location created= new Location(location, rent);
        finish(created.save(), created.toString());
    }

    private boolean belowThreshold(long rent){
        return rent<AMOUNT_MINIMUM_RENT;
    }

    private void finish(long id, String item){
        dashboard.finish(id, item);
    }
}
