package ug.karuhanga.logrealty.Controllers;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.Date;
import java.util.List;

import ug.karuhanga.logrealty.Helper;
import ug.karuhanga.logrealty.Models.House;
import ug.karuhanga.logrealty.Models.Tenant;
import ug.karuhanga.logrealty.R;
import ug.karuhanga.logrealty.Views.AddTenant;
import ug.karuhanga.logrealty.Views.Confirmation;

import static ug.karuhanga.logrealty.Helper.ERROR_REQUIRED;
import static ug.karuhanga.logrealty.Helper.REQUEST_CODE_REPLACE;
import static ug.karuhanga.logrealty.Models.Notification.schedule;

/**
 * Created by karuhanga on 12/5/17.
 */

public class AddTenantController implements AddTenant.AddTenantActivityExternalInterface, Confirmation.ConfirmationExternalInterface {

    private Controller.AddTenantControllerExternalInterface dashboard;
    boolean replace;
    House chosen;

    public AddTenantController(Controller.AddTenantControllerExternalInterface dashboard){
        this.dashboard= dashboard;
        this.replace= false;
        chosen= null;
    }

    @Override
    public void onHousePick(House house) {
        if (house==null){
            dashboard.raise(ERROR_REQUIRED);
            return;
        }

        List<Tenant> current= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("house")).eq(house)).and(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq(Helper.FALSE)).list();
        if (current.size()>0){
            new Confirmation(dashboard.requestContext(), this, "Are you sure?", "Replace:\n"+current.get(0).getName()+" in "+current.get(0).getHouse().getLocation().getName(), R.drawable.ic_edit_black_24dp, "Yes", "Pick a different House", REQUEST_CODE_REPLACE).show();
        }
        this.chosen= house;
    }

    @Override
    public List<House> getHouseData() {
        return Select.from(House.class).list();
    }

    @Override
    public void submit(String fName, String oNames, String email, String contact, String idType, String idNo, Date entered, Date startCount) {
        if (chosen==null){
            dashboard.complainAboutHouse(ERROR_REQUIRED);
            return;
        }
        if (replace){
            List<Tenant> current= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("house")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq(Helper.FALSE)).list();
            if (current.size()>0){
                current.get(0).setEx(true);
                current.get(0).save();
            }
        }
        Tenant tenant= new Tenant(fName, oNames, email, contact, entered, startCount, idType, idNo, chosen);
        tenant.save();
        schedule(dashboard.requestContext(), tenant, true);
        finish(tenant.getId(), tenant.toString());
    }

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (requestCode==REQUEST_CODE_REPLACE){
            if (result){
                replace= result;
            }
            else{
                replace= false;
                chosen= null;
            }
        }
    }

    private void finish(long id, String item){
        dashboard.finish(id, item);
    }
}
