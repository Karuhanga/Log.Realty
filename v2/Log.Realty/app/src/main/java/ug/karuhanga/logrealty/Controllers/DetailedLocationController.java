package ug.karuhanga.logrealty.Controllers;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.R;
import ug.karuhanga.logrealty.Views.Confirmation;
import ug.karuhanga.logrealty.Views.DetailedLocation;

import static ug.karuhanga.logrealty.Helper.AMOUNT_MINIMUM_RENT;
import static ug.karuhanga.logrealty.Helper.REQUEST_CODE_DELETE;
import static ug.karuhanga.logrealty.Helper.getRentBelowMinNotif;
import static ug.karuhanga.logrealty.Helper.toCurrency;

/**
 * Created by karuhanga on 12/12/17.
 * Logic Controller for {@link DetailedLocation}
 */

class DetailedLocationController implements DetailedLocation.DetailedLocationActivityExternalInterface, Confirmation.ConfirmationExternalInterface {
    Controller.DetailedLocationControllerExternalInterface dashboard;
    private Location location;

    DetailedLocationController(Controller.DetailedLocationControllerExternalInterface dashboard) {
        this.dashboard= dashboard;
    }

    @Override
    public void setLocation(long id) {
        try {
            this.location= Location.findById(Location.class, id);
        }catch (Exception e){
            this.location= Select.from(Location.class).limit("1").first();
        }
    }

    @Override
    public String getAmount() {
        return toCurrency(location.getDefaultRent());
    }

    @Override
    public String getLocation() {
        return location.getName();
    }

    @Override
    public void delete() {
        new Confirmation(dashboard.requestContext(), this, "Are you sure?", "Delete this location and all houses here:\n"+location.summarize(), R.drawable.ic_delete_black_24dp, "Yes", "Cancel", REQUEST_CODE_DELETE).show();
    }

    @Override
    public void editName(String location) {
        if (locationExists(location)){
            dashboard.complainAboutLocation("Already added location");
            return;
        }
        this.location.setName(location);
        this.location.save();
        dashboard.onEditLocationComplete();
    }

    @Override
    public void editAmount(long amount) {
        if (amount<AMOUNT_MINIMUM_RENT){
            dashboard.complainAboutRent(getRentBelowMinNotif());
            return;
        }
        location.setDefaultRent(amount);
        location.save();
        dashboard.onEditAmountComplete();
    }

    @Override
    public String getNumericalAmount() {
        return String.valueOf(location.getDefaultRent());
    }

    private boolean locationExists(String location){
        return Select.from(Location.class).where(Condition.prop(NamingHelper.toSQLNameDefault("name")).eq(location)).count() > 0;
    }

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (requestCode==REQUEST_CODE_DELETE){
            if (result){
                long id= location.getId();
                location.delete();
                dashboard.onLocationDeleted(id);
            }
        }
    }
}
