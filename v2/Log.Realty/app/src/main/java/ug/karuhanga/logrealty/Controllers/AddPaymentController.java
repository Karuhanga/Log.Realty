package ug.karuhanga.logrealty.Controllers;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Helper;
import ug.karuhanga.logrealty.Models.Payment;
import ug.karuhanga.logrealty.Models.Tenant;
import ug.karuhanga.logrealty.Views.AddPayment;

/**
 * Created by karuhanga on 12/5/17.
 */

public class AddPaymentController implements AddPayment.AddPaymentActivityExternalInterface {

    Controller.AddPaymentControllerExternalInterface dashboard;
    Tenant chosen;

    public AddPaymentController(Controller.AddPaymentControllerExternalInterface dashboard){
        this.dashboard= dashboard;
        chosen= null;
    }


    @Override
    public List<Tenant> getTenantData() {
        return Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq(Helper.FALSE)).list();
    }

    @Override
    public void onTenantPick(Tenant tenant) {
        if (tenant==null){
            dashboard.raise(Helper.ERROR_REQUIRED);
            return;
        }
        this.chosen= tenant;
    }

    @Override
    public void submit() {
        if (chosen==null){
            dashboard.raise(Helper.ERROR_REQUIRED);
            return;
        }

        Payment created= new Payment(Helper.getTodaysDate(), chosen, 1);
        finish(created.save(), created.toString());
    }

    @Override
    public void submit(long amount) {
        if (chosen==null){
            dashboard.raise(Helper.ERROR_REQUIRED);
            return;
        }

        Payment created= new Payment(Helper.getTodaysDate(), chosen, amount);
        finish(created.save(), created.toString());
    }

    private void finish(long id, String item){
        dashboard.finish(id, item);
    }
}
