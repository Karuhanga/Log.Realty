package ug.karuhanga.logrealty.Controllers;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Models.House;
import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.R;
import ug.karuhanga.logrealty.Views.Confirmation;
import ug.karuhanga.logrealty.Views.DetailedHouse;

import static ug.karuhanga.logrealty.Helper.AMOUNT_MINIMUM_RENT;
import static ug.karuhanga.logrealty.Helper.REQUEST_CODE_DELETE;
import static ug.karuhanga.logrealty.Helper.getRentBelowMinNotif;
import static ug.karuhanga.logrealty.Helper.log;
import static ug.karuhanga.logrealty.Helper.toCurrency;

/**
 * Created by karuhanga on 12/14/17.
 * Logic controller for {@link DetailedHouse}
 */

class DetailedHouseController implements DetailedHouse.DetailedHouseActivityExternalInterface, Confirmation.ConfirmationExternalInterface {

    private Controller.DetailedHouseControllerExternalInterface dashboard;
    private House house;
    private Location chosen;
    private final int NUMBER_AND_DESCRIPTION= 2;
    private final int DESCRIPTION_ONLY= 1;
    private final int ERROR= -1;

    DetailedHouseController(Controller.DetailedHouseControllerExternalInterface dashboard) {
        this.dashboard= dashboard;
    }

    @Override
    public void setHouse(long id) {
        try {
            this.house= House.findById(House.class, id);
        }catch (Exception e){
            this.house= Select.from(House.class).limit("1").first();
        }

        if (house==null){
            this.house= Select.from(House.class).limit("1").first();
        }
    }

    @Override
    public List<Location> getLocations() {
        return Select.from(Location.class).list();
    }

    @Override
    public void deleteHouse() {
        new Confirmation(dashboard.requestContext(), this, "Are you sure?", "Delete this house and its tenant:\n"+house.summarize(), R.drawable.ic_delete_black_24dp, "Yes", "Cancel", REQUEST_CODE_DELETE).show();
    }

    @Override
    public void setChosenLocation(Location location) {
        this.chosen= location;
    }

    @Override
    public String getLocationName() {
        return this.house.getLocation().getName();
    }

    @Override
    public void editLocation() {
        long count= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(house.getNumber())).count();
        count+=Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(0)).count();
        if (count>0){
            dashboard.complainAboutLocation("This house already exists");
            return;
        }
        house.setLocation(chosen);
    }

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (result && requestCode==REQUEST_CODE_DELETE){
            long id= house.getId();
            house.delete();
            dashboard.onHouseDeleted(id);
        }
    }

    @Override
    public void editInformation(){
        //if previously not checked
        if (isASingleHouse()){
            dashboard.onInfoEditComplete();
            return;
        }

        //Otherwise
        //More than one house
        if (Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(house.getLocation())).count()>1){
            dashboard.complainAboutHouseInformation("This location already has multiple houses");
            return;
        }

        house.setNumber(0);
        house.setDescription("");
        house.save();
    }

    @Override
    public void editInformation(int number, String description) {
        //Previously single house
        if (isASingleHouse()){
            //Valid numbers only
            if (number<1){
                dashboard.complainAboutHouseInformation("Invalid house number");
                return;
            }
            house.setNumber(number);
            house.setDescription(description);
            house.save();
            dashboard.onInfoEditComplete();
            return;
        }

        switch (evaluateData(number)){
            case NUMBER_AND_DESCRIPTION:
                house.setNumber(number);
                house.setDescription(description);
                house.save();
                dashboard.onInfoEditComplete();
                return;
            case DESCRIPTION_ONLY:
                house.setDescription(description);
                house.save();
                dashboard.onInfoEditComplete();
                return;
            default:
                log("Detailed House Controller: Evaluate data returning with error");
        }
    }

    private int evaluateData(int number) {

        if (number==house.getNumber()){
            return DESCRIPTION_ONLY;
        }

        //Valid numbers only
        if (number<1){
            dashboard.complainAboutHouseInformation("Invalid house number");
            return ERROR;
        }

        //Another house with similar house number?
        if (Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(house.getLocation())).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(number)).count()>0){
            dashboard.complainAboutHouseInformation("House already added");
            return ERROR;
        }

        return NUMBER_AND_DESCRIPTION;
    }

    @Override
    public void editRent() {
        house.setRent(house.getLocation().getDefaultRent());
        house.save();
        dashboard.onRentEditComplete();
    }

    @Override
    public void editRent(long rent) {
        if (rent<AMOUNT_MINIMUM_RENT){
            dashboard.complainAboutRent(getRentBelowMinNotif());
            return;
        }
        house.setRent(rent);
        house.save();
        dashboard.onRentEditComplete();
    }

    @Override
    public String getRent() {
        return toCurrency(house.getRent());
    }

    @Override
    public String getDescription() {
        return house.getDescription();
    }

    @Override
    public String getNumber() {
        return String.valueOf(house.getNumber());
    }

    @Override
    public boolean isASingleHouse() {
        return 0==house.getNumber();
    }
}
