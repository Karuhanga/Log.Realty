package ug.karuhanga.logrealty.Controllers;

import android.content.Context;

import ug.karuhanga.logrealty.Views.AddHouse.AddHouseActivityExternalInterface;
import ug.karuhanga.logrealty.Views.AddLocation.AddLocationActivityExternalInterface;
import ug.karuhanga.logrealty.Views.AddPayment.AddPaymentActivityExternalInterface;
import ug.karuhanga.logrealty.Views.AddTenant.AddTenantActivityExternalInterface;
import ug.karuhanga.logrealty.Views.DetailedLocation;
import ug.karuhanga.logrealty.Views.Details.DetailsActivityExternalInterface;

/**
 * Created by karuhanga on 12/5/17.
 */

public class Controller {
    public interface AddLocationControllerExternalInterface{
        void raise(String message);

        void finish(long id, String summary);

        void complainAboutLocation(String message);
    }

    public static AddLocationActivityExternalInterface injectAddLocationActivityExternalInterface(AddLocationControllerExternalInterface dashboard){
        return new AddLocationController(dashboard);
    }

    public interface AddHouseControllerExternalInterface{
        void complainAboutLocation(String message);

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

        void complainAboutHouse(String message);
    }

    public static AddTenantActivityExternalInterface injectAddTenantActivityExternalInterface(AddTenantControllerExternalInterface dashboard){
        return new AddTenantController(dashboard);
    }

    public interface AddPaymentControllerExternalInterface{
        void raise(String message);

        void finish(long id, String summary);

        Context requestContext();

        void complainAboutRent(String message);
    }

    public static AddPaymentActivityExternalInterface injectAddPaymentActivityExternalInterface(AddPaymentControllerExternalInterface dashboard){
        return new AddPaymentController(dashboard);
    }

    public interface DetailsControllerExternalInterface{

        void swipeLeft();

        void notifyNoData();

        void swipeRight();
    }

    public static DetailsActivityExternalInterface injectDetailsActivityExternalInterface(DetailsControllerExternalInterface dashboard){
        return new DetailsController(dashboard);
    }

    public interface DetailedLocationControllerExternalInterface{

        Context requestContext();

        void onLocationDeleted(long id);

        void complainAboutRent(String message);

        void complainAboutLocation(String message);
    }

    public static DetailedLocation.DetailedLocationActivityExternalInterface injectDetailedLocationActivityExternalInterface(DetailedLocationControllerExternalInterface dashboard){
        return new DetailedLocationController(dashboard);
    }
}
