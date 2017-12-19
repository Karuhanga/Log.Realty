package ug.karuhanga.logrealty.Controllers;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ug.karuhanga.logrealty.Models.House;
import ug.karuhanga.logrealty.Models.Tenant;
import ug.karuhanga.logrealty.R;
import ug.karuhanga.logrealty.Views.Confirmation;
import ug.karuhanga.logrealty.Views.DetailedTenant;

import static ug.karuhanga.logrealty.Helper.ERROR_REQUIRED;
import static ug.karuhanga.logrealty.Helper.FALSE;
import static ug.karuhanga.logrealty.Helper.REQUEST_CODE_DELETE;
import static ug.karuhanga.logrealty.Helper.REQUEST_CODE_EDIT;
import static ug.karuhanga.logrealty.Helper.REQUEST_CODE_REPLACE;
import static ug.karuhanga.logrealty.Helper.breakDate;
import static ug.karuhanga.logrealty.Helper.dateToString;
import static ug.karuhanga.logrealty.Helper.log;

/**
 * Created by karuhanga on 12/15/17.
 * Logic Controller for @link{Views.DetailedTenant}
 */

class DetailedTenantController implements DetailedTenant.DetailedTenantActivityExternalInterface, Confirmation.ConfirmationExternalInterface {

    private Controller.DetailedTenantControllerExternalInterface dashboard;
    private Tenant tenant;
    private Date newDue;
    private House newHouse;

    DetailedTenantController(Controller.DetailedTenantControllerExternalInterface dashboard) {
        this.dashboard= dashboard;
    }

    @Override
    public void setTenant(long id) {
        try {
            this.tenant= Tenant.findById(Tenant.class, id);
        }catch (Exception e){
            log("DetailedTenantController: Receiving invalid Id");
            this.tenant= Select.from(Tenant.class).limit("1").first();
        }

        if (this.tenant==null){
            this.tenant= Select.from(Tenant.class).limit("1").first();
        }
    }

    @Override
    public List<House> getHouses() {
        return Select.from(House.class).list();
    }

    @Override
    public String getFName() {
        return tenant.getfName();
    }

    @Override
    public String getSurname() {
        return tenant.getoNames();
    }

    @Override
    public String getEmail() {
        return tenant.getEmail();
    }

    @Override
    public String getContact() {
        return tenant.getContact();
    }

    @Override
    public String getIdType() {
        return tenant.getIdType();
    }

    @Override
    public String getIdNo() {
        return tenant.getIdNo();
    }

    @Override
    public String getHouse() {
        return tenant.getHouse().toString();
    }

    @Override
    public void deleteTenant() {
        new Confirmation(dashboard.requestContext(), this, "Are you sure?", "Delete this tenant and all related payments:\n"+tenant.getName()+"\n"+tenant.getHouse().toString(), R.drawable.ic_delete_black_24dp, "Yes", "Cancel", REQUEST_CODE_DELETE).show();
    }

    @Override
    public void editName(String firstName, String surname) {
        if (firstName==null || firstName.isEmpty()){
            dashboard.complainAboutFName(ERROR_REQUIRED);
            return;
        }
        if (surname==null || surname.isEmpty()){
            dashboard.complainAboutSurname(ERROR_REQUIRED);
            return;
        }
        tenant.setfName(firstName);
        tenant.setoNames(surname);
        tenant.save();
        dashboard.onEditNameComplete();
    }

    @Override
    public void editEmail(String email) {
        if (email==null || email.isEmpty()){
            dashboard.complainAboutEmail("Invalid Email");
            return;
        }

        tenant.setEmail(email);
        tenant.save();
        dashboard.onEditEmailComplete();
    }

    @Override
    public void editContact(String contact) {
        if (contact==null || contact.isEmpty()){
            dashboard.complainAboutContact("Invalid Contact");
            return;
        }

        tenant.setContact(contact);
        tenant.save();
        dashboard.onEditContactComplete();
    }

    @Override
    public void editIdType(String idType) {
        if (idType==null || idType.isEmpty()){
            dashboard.complainAboutIdType("Invalid");
            return;
        }

        tenant.setIdType(idType);
        tenant.save();
        dashboard.onEditIdTypeComplete();
    }

    @Override
    public void editIdNo(String idNo) {
        if (idNo==null || idNo.isEmpty()){
            dashboard.complainAboutIdNo("Invalid");
            return;
        }

        tenant.setIdNo(idNo);
        tenant.save();
        dashboard.onEditIdNoComplete();
    }

    @Override
    public void editDateDue(Date dateDue) {
        if (dateDue==null){
            dashboard.complainAboutRentDue("Please choose a due date");
            return;
        }

        newDue= dateDue;
        new ug.karuhanga.logrealty.Views.Confirmation(dashboard.requestContext(), this, "Are you sure?", "Change tenant's due date:\n"+tenant.getName()+"\n"+tenant.getHouse().toString(), R.drawable.ic_edit_black_24dp, "Yes", "No, leave as is", REQUEST_CODE_EDIT).show();
    }

    @Override
    public void editDateEntered(Date dateEntered) {
        tenant.setEntered(dateEntered);
        tenant.save();
        dashboard.onEditEnteredComplete();
    }

    @Override
    public void editHouse() {
        if (newHouse==null) {
            dashboard.complainAboutHouse("Please pick a house");
            return;
        }

        List<Tenant> current=  Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("house")).eq(newHouse)).and(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq(FALSE)).list();
        if (current.size()>0){
            new ug.karuhanga.logrealty.Views.Confirmation(dashboard.requestContext(), this, "Are you sure?", "Replace:\n"+current.get(0).getName()+" in "+current.get(0).getHouse().getLocation().getName(), R.drawable.ic_edit_black_24dp, "Yes", "Pick a different House", REQUEST_CODE_REPLACE).show();
            return;
        }

        tenant.setHouse(newHouse);
        tenant.save();
        dashboard.onHouseEditComplete();
    }

    @Override
    public void setNewHouse(House newHouse) {
        this.newHouse= newHouse;
    }

    @Override
    public String getDateEntered() {
        return dateToString(tenant.getEntered());
    }

    @Override
    public String getDateDue() {
        return dateToString(tenant.getRentDue());
    }

    @Override
    public ArrayList<Integer> getDatePickerDateEntered() {
        return breakDate(tenant.getEntered());
    }

    @Override
    public ArrayList<Integer> getDatePickerDateDue() {
        return breakDate(tenant.getRentDue());
    }

    @Override
    public String getName() {
        return this.tenant.getName();
    }

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (result){
            switch (requestCode){
                case REQUEST_CODE_DELETE:
                    long id= tenant.getId();
                    tenant.delete();
                    dashboard.onTenantDeleted(id);
                    return;
                case REQUEST_CODE_EDIT:
                    tenant.setRentDue(newDue);
                    tenant.save();
                    dashboard.onEditDueComplete();
                    return;
                case REQUEST_CODE_REPLACE:
                    Tenant previous= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("house")).eq(newHouse)).and(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq(FALSE)).first();
                    previous.setEx(true);
                    previous.save();
                    tenant.setHouse(newHouse);
                    tenant.save();
                    dashboard.onHouseEditComplete();
                    return;
                default:
            }
        }
    }
}
