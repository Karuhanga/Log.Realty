package ug.karuhanga.logrealty.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Data.Notification;
import ug.karuhanga.logrealty.Data.Payment;
import ug.karuhanga.logrealty.Data.Setting;
import ug.karuhanga.logrealty.Data.Tenant;
import ug.karuhanga.logrealty.Helpers;

public class Backup extends Service {
    private List<String> final_data= new ArrayList<>();
    private final String START= "<--START_";
    private final String END= "<--END-->";
    private final String NOTIFICATIONS= "NOTIFICATIONS-->";
    private final String HOUSES= "HOUSES-->";
    private final String LOCATIONS= "LOCATIONS-->";
    private final String PAYMENTS= "PAYMENTS-->";
    private final String SETTINGS= "SETTINGS-->";
    private final String TENANTS= "TENANTS-->";

    public Backup() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        final_data.add("Backup_"+ String.valueOf(Helpers.getTodaysDate().getTime()));
        appendData(LOCATIONS);
        appendData(HOUSES);
        appendData(TENANTS);
        appendData(PAYMENTS);
        appendData(NOTIFICATIONS);
        appendData(SETTINGS);
        final_data.add(END);
        saveToFile();
        return Service.START_NOT_STICKY;
    }

    private void appendData(String data_set){
        Gson gson= new Gson();
        final_data.add(START+data_set);
        switch (data_set){
            case LOCATIONS:
                List<Location> locations= Select.from(Location.class).list();
                for (Location location : locations) {
                    final_data.add(gson.toJson(location));
                }
                break;
            case HOUSES:
                List<House> houses= Select.from(House.class).list();
                for (House house : houses) {
                    final_data.add(gson.toJson(house));
                }
                break;
            case TENANTS:
                List<Tenant> tenants= Select.from(Tenant.class).list();
                for (Tenant tenant : tenants) {
                    final_data.add(gson.toJson(tenant));
                }
                break;
            case PAYMENTS:
                List<Payment> payments= Select.from(Payment.class).list();
                for (Payment payment : payments) {
                    final_data.add(gson.toJson(payment));
                }
                break;
            case NOTIFICATIONS:
                List<Notification> notifications= Select.from(Notification.class).list();
                for (Notification notification : notifications) {
                    final_data.add(gson.toJson(notification));
                }
                break;
            case SETTINGS:
                List<Setting> settings= Select.from(Setting.class).list();
                for (Setting setting : settings) {
                    final_data.add(gson.toJson(setting));
                }
                break;
            default:
                break;
        }
        final_data.add(END);
    }

    private void saveToFile(){

    }
}
