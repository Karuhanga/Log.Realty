package ug.karuhanga.logrealty.Controllers;

import android.content.Context;

import ug.karuhanga.logrealty.Views.AddHouse.AddHouseActivityExternalInterface;
import ug.karuhanga.logrealty.Views.AddLocation.AddLocationActivityExternalInterface;
import ug.karuhanga.logrealty.Views.AddPayment.AddPaymentActivityExternalInterface;
import ug.karuhanga.logrealty.Views.AddTenant.AddTenantActivityExternalInterface;
import ug.karuhanga.logrealty.Views.DetailedHouse.DetailedHouseActivityExternalInterface;
import ug.karuhanga.logrealty.Views.DetailedLocation.DetailedLocationActivityExternalInterface;
import ug.karuhanga.logrealty.Views.DetailedPayment.DetailedPaymentActivityExternalInterface;
import ug.karuhanga.logrealty.Views.DetailedTenant.DetailedTenantActivityExternalInterface;
import ug.karuhanga.logrealty.Views.Details.DetailsActivityExternalInterface;
import ug.karuhanga.logrealty.Views.Starter.StarterActivityExternalInterface;

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

        void notifyDataSetChanged();
    }

    public static DetailsActivityExternalInterface injectDetailsActivityExternalInterface(DetailsControllerExternalInterface dashboard){
        return new DetailsController(dashboard);
    }

    public interface DetailedLocationControllerExternalInterface{

        Context requestContext();

        void onLocationDeleted(long id);

        void complainAboutRent(String message);

        void complainAboutLocation(String message);

        void onEditLocationComplete();

        void onEditAmountComplete();
    }

    public static DetailedLocationActivityExternalInterface injectDetailedLocationActivityExternalInterface(DetailedLocationControllerExternalInterface dashboard){
        return new DetailedLocationController(dashboard);
    }

    public interface DetailedPaymentControllerExternalInterface{

        Context requestContext();

        void onPaymentDeleted(long id);
    }

    public static DetailedPaymentActivityExternalInterface injectDetailedPaymentActivityExternalInterface(DetailedPaymentControllerExternalInterface dashboard){
        return new DetailedPaymentController(dashboard);
    }

    public interface DetailedHouseControllerExternalInterface{

        Context requestContext();

        void onHouseDeleted(long id);

        void complainAboutLocation(String message);

        void onLocationEditComplete();

        void onInfoEditComplete();

        void onRentEditComplete();

        void complainAboutRent(String message);

        void complainAboutHouseInformation(String message);
    }

    public static DetailedHouseActivityExternalInterface injectDetailedHouseActivityExternalInterface(DetailedHouseControllerExternalInterface dashboard){
        return new DetailedHouseController(dashboard);
    }

    public interface DetailedTenantControllerExternalInterface{

        Context requestContext();

        void onTenantDeleted(long id);

        void onEditDueComplete();

        void complainAboutHouse(String message);

        void onHouseEditComplete();

        void complainAboutRentDue(String message);

        void complainAboutFName(String message);

        void complainAboutSurname(String message);

        void onEditNameComplete();

        void complainAboutEmail(String message);

        void onEditEmailComplete();

        void complainAboutContact(String message);

        void onEditContactComplete();

        void complainAboutIdType(String message);

        void onEditIdTypeComplete();

        void complainAboutIdNo(String message);

        void onEditIdNoComplete();

        void onEditEnteredComplete();
    }

    public static DetailedTenantActivityExternalInterface injectDetailedTenantActivityExternalInterface(DetailedTenantControllerExternalInterface dashboard){
        return new DetailedTenantController(dashboard);
    }

    public interface StarterControllerExternalInterface{

        Context requestContext();

        void onActionsCompleted();

        void startFirstLaunchActions();
    }

    public static StarterActivityExternalInterface injectStarterActivityctivityExternalInterface(StarterControllerExternalInterface dashboard){
        return new StarterController(dashboard);
    }
}
