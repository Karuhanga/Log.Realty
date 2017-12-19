package ug.karuhanga.logrealty.Controllers;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import ug.karuhanga.logrealty.Models.Setting;
import ug.karuhanga.logrealty.Receivers.OnBackupTime;
import ug.karuhanga.logrealty.Receivers.OnCheckForNotificationsTime;
import ug.karuhanga.logrealty.Views.Starter;

import static ug.karuhanga.logrealty.Helper.SETTINGS_ALLOW_BACKUP;
import static ug.karuhanga.logrealty.Helper.SETTINGS_FIRST_TIME;
import static ug.karuhanga.logrealty.Helper.SETTINGS_USER_NAME;
import static ug.karuhanga.logrealty.Helper.log;

/**
 * Created by karuhanga on 12/19/17.
 * Logic Controller for {@link Starter}
 */

class StarterController implements Starter.StarterActivityExternalInterface {
    private final int NOT_INITIALISED = -1;

    private Controller.StarterControllerExternalInterface dashboard;
    private int firstLaunch;

    StarterController(Controller.StarterControllerExternalInterface dashboard) {
        this.dashboard= dashboard;
        firstLaunch= NOT_INITIALISED;
    }

    private void doTestingStuff() {

    }

    @Override
    public void startInits() {
        doTestingStuff();
        if (!firstLaunch()){
            dashboard.onActionsCompleted();
            return;
        }

        dashboard.startFirstLaunchActions();

        if (backupAvailable()){
            startRestore();
        }

        confirmAllSettingsExist();
        dashboard.onActionsCompleted();
    }

    private void startRestore() {

    }

    private boolean backupAvailable() {
        return false;
    }

    private void confirmAllSettingsExist() {
        confirmFirstTime();
        confirmUserName();
        confirmBackup();
        confirmActionSchedules();
    }

    private void confirmActionSchedules() {
        OnCheckForNotificationsTime.setUpNotifierSchedule(dashboard.requestContext());
        OnBackupTime.setUpBackupSchedule(dashboard.requestContext());
    }

    private void confirmBackup() {
        long results= Select.from(Setting.class).where(Condition.prop(NamingHelper.toSQLNameDefault("name")).eq(SETTINGS_ALLOW_BACKUP)).count();
        if (results<1){
            new Setting(SETTINGS_ALLOW_BACKUP, true).save();
        }
    }

    private void confirmUserName() {
        long results= Select.from(Setting.class).where(Condition.prop(NamingHelper.toSQLNameDefault("name")).eq(SETTINGS_USER_NAME)).count();
        if (results<1){
            new Setting(SETTINGS_USER_NAME, "").save();
        }
    }

    private void confirmFirstTime() {
        long results= Select.from(Setting.class).where(Condition.prop(NamingHelper.toSQLNameDefault("name")).eq(SETTINGS_FIRST_TIME)).count();
        if (results<1){
            new Setting(SETTINGS_FIRST_TIME, false).save();
        }
    }

    private boolean firstLaunch() {
        if (firstLaunch== NOT_INITIALISED){
            setFirstLaunch();
        }

        return firstLaunch == 1;
    }

    private void setFirstLaunch() {
        Setting result= Select.from(Setting.class).where(Condition.prop(NamingHelper.toSQLNameDefault("name")).eq(SETTINGS_FIRST_TIME)).first();
        try {
            firstLaunch= (result.getStatus())? 1:0;
        }catch (Exception e){
            log("Starter Controller: First Time, "+e.toString());
            firstLaunch= 1;
        }
    }


}
