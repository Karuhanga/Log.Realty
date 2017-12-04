package ug.karuhanga.logrealty.Controllers;

import ug.karuhanga.logrealty.Viewers.Activities.*;

/**
 * Created by karuhanga on 12/2/17.
 */

public class ControlProvider {
    public static ug.karuhanga.logrealty.Viewers.Activities.AddHouse.Controller provide(AddHouse.Slave slave){
        return new AddHouse(slave);
    }
}
