package ug.karuhanga.logrealty.Controllers;

import android.support.v4.app.Fragment;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.List;

import ug.karuhanga.logrealty.Models.House;
import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.Models.Payment;
import ug.karuhanga.logrealty.Models.Tenant;
import ug.karuhanga.logrealty.Views.DetailedHouse;
import ug.karuhanga.logrealty.Views.DetailedLocation;
import ug.karuhanga.logrealty.Views.DetailedPayment;
import ug.karuhanga.logrealty.Views.DetailedTenant;
import ug.karuhanga.logrealty.Views.Details;

import static ug.karuhanga.logrealty.Helper.FALSE;
import static ug.karuhanga.logrealty.Helper.FRAGMENT_HOUSES;
import static ug.karuhanga.logrealty.Helper.FRAGMENT_LOCATIONS;
import static ug.karuhanga.logrealty.Helper.FRAGMENT_PAYMENTS;
import static ug.karuhanga.logrealty.Helper.FRAGMENT_TENANTS;

/**
 * Created by karuhanga on 12/12/17.
 */

public class DetailsController implements Details.DetailsActivityExternalInterface {
    private List<Long> ids= new ArrayList<>();
    private int entity;
    private Long current_id;
    private Controller.DetailsControllerExternalInterface dashboard;

    public DetailsController(Controller.DetailsControllerExternalInterface dashboard) {
        this.dashboard= dashboard;
    }

    @Override
    public void setCurrent(long id) {
        current_id= id;
    }

    @Override
    public void setEntity(int entity) {
        this.entity = entity;
        populateIDS();
    }

    @Override
    public int getCurrentIndex() {
        return ids.indexOf(current_id);
    }

    //returns a fragment based on the position provided by looking up the id in @link{ids}
    @Override
    public Fragment getItemAtPosition(int position) {
        switch (getEntity()){
            case FRAGMENT_LOCATIONS: return DetailedLocation.newInstance(ids.get(position));
            case FRAGMENT_HOUSES: return DetailedHouse.newInstance(ids.get(position));
            case FRAGMENT_TENANTS: return DetailedTenant.newInstance(ids.get(position));
            case FRAGMENT_PAYMENTS: return DetailedPayment.newInstance(ids.get(position));
            default: return DetailedLocation.newInstance(ids.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

    @Override
    public void notifyItemDeleted(Long item) {
        int previous_index= ids.indexOf(item);
        ids.remove(previous_index);

        //Deletion does not remove item from display, so hard refresh with either left or right swipe
        try {
            //is there item in front?
            ids.get(previous_index);
        } catch (IndexOutOfBoundsException e1){
            try {
                //if not, behind?
                ids.get(previous_index-1);
            }catch (IndexOutOfBoundsException e2){
                dashboard.notifyNoData();
                return;
            }
            dashboard.swipeLeft();
            return;
        }
        dashboard.swipeRight();
    }

    private void populateIDS() {
        ids.clear();
        switch (getEntity()){
            case FRAGMENT_LOCATIONS:
                List<Location> results1= Select.from(Location.class).list();
                for (Location result : results1) {
                    ids.add(result.getId());
                }
                break;
            case FRAGMENT_HOUSES:
                List<House> results2= Select.from(House.class).list();
                for (House result : results2) {
                    ids.add(result.getId());
                }
                break;
            case FRAGMENT_TENANTS:
                List<Tenant> results3= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq(FALSE)).list();
                for (Tenant result : results3) {
                    ids.add(result.getId());
                }
                break;
            case FRAGMENT_PAYMENTS:
                List<Payment> results4= Select.from(Payment.class).list();
                for (Payment result : results4) {
                    ids.add(result.getId());
                }
                break;
            default:
                List<Location> results5= Select.from(Location.class).list();
                for (Location result : results5) {
                    ids.add(result.getId());
                }
                break;
        }
    }

    public int getEntity() {
        return entity;
    }
}
