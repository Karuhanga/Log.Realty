package ug.karuhanga.logrealty.Controllers;

import ug.karuhanga.logrealty.Views.AddHouse.AddHouseActivityExternalInterface;
import ug.karuhanga.logrealty.Views.AddLocation.AddLocationActivityExternalInterface;

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
}
