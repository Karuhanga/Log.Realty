package ug.karuhanga.logrealty.Controllers;

import android.content.Context;

import ug.karuhanga.logrealty.Views.AddHouse.AddHouseActivityExternalInterface;
import ug.karuhanga.logrealty.Views.AddLocation.AddLocationActivityExternalInterface;
import ug.karuhanga.logrealty.Views.AddPayment;
import ug.karuhanga.logrealty.Views.AddTenant.AddTenantActivityExternalInterface;

/**
 * Created by karuhanga on 12/5/17.
 */

public class Controller {
    public interface AddLocationControllerExternalInterface{
        void raise(String message);

        void finish(long id, String summary);
    }

    public static AddLocationActivityExternalInterface injectAddLocationActivityExternalInterface(AddLocationControllerExternalInterface dashboard){
        return new AddLocationController(dashboard);
    }

    public interface AddHouseControllerExternalInterface{
        void raise(String message);

        void complainAboutHouse(String message);

        void complainAboutRent(String message);

        void finish(long id, String summary);
    }

    public static AddHouseActivityExternalInterface injectAddHouseActivityExternalInterface(AddHouseControllerExternalInterface dashboard){
        return new AddHouseController(dashboard);
    }

    public interface AddTenantControllerExternalInterface{
        void raise(String message);

        void finish(long id, String summary);

        Context requestContext();
    }

    public static AddTenantActivityExternalInterface injectAddTenantActivityExternalInterface(AddTenantControllerExternalInterface dashboard){
        return new AddTenantController(dashboard);
    }

    public interface AddPaymentControllerExternalInterface{
        void raise(String message);

        void finish(long id, String summary);
    }

    public static AddPayment.AddPaymentActivityExternalInterface injectAddPaymentActivityExternalInterface(AddPaymentControllerExternalInterface dashboard){
        return new AddPaymentController(dashboard);
    }
}
