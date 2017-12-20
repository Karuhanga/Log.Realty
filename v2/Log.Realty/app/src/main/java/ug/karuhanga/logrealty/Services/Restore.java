package ug.karuhanga.logrealty.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ug.karuhanga.logrealty.Helper;
import ug.karuhanga.logrealty.Models.House;
import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.Models.Payment;
import ug.karuhanga.logrealty.Models.Setting;
import ug.karuhanga.logrealty.Models.Tenant;

import static ug.karuhanga.logrealty.Helper.SPECIAL_CRLF;

public class Restore extends Service {
    private List<String> final_data= new ArrayList<>();
    private final String START= "<--START_";
    private final String END= "<--END-->";
    private final String HOUSES= "HOUSES-->";
    private final String LOCATIONS= "LOCATIONS-->";
    private final String PAYMENTS= "PAYMENTS-->";
    private final String SETTINGS= "SETTINGS-->";
    private final String TENANTS= "TENANTS-->";
    public Restore() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        //TODO Check for possible previous Backup without UI detriment and no unnecessary wait... Could Possibly be done by caller
        new Runnable(){
            @Override
            public void run() {
                commenceRestore();
            }
        }.run();
        return Service.START_NOT_STICKY;
    }

    private void commenceRestore() {
        readFromFile(selectFile());
        final_data.remove(0);//TODO Find use for Backup Date
        fullDataWipe();
        restore(LOCATIONS);
        restore(HOUSES);
        restore(TENANTS);
        restore(PAYMENTS);
        restore(SETTINGS);
    }

    private void fullDataWipe() {

        //Wipe Settings
        Setting.deleteAll(Setting.class);

        //Wipe Payments
        Payment.deleteAll(Payment.class);

        //Wipe Tenants
        Tenant.deleteAll(Tenant.class);

        //Wipe Houses
        House.deleteAll(House.class);

        //Wipe Locations
        Location.deleteAll(Location.class);
    }

    private void readFromFile(String path){
        String data= Helper.readFromFile(path);
        final_data.clear();
        String[] elements= data.split(SPECIAL_CRLF);
        Collections.addAll(final_data, elements);
    }

    private String selectFile(){
        //TODO Implement Backup file selection
        return "";
    }

    private boolean restore(String data_set){
        String data;
        Gson gson= new Gson();
        if (!Objects.equals(final_data.get(0), START + data_set)){
            return false;
        }
        final_data.remove(0);
        switch (data_set){
            case LOCATIONS:
                data= final_data.remove(0);
                while (!Objects.equals(data, END)){
                    gson.fromJson(data, Location.class).save();
                    data= final_data.remove(0);
                }
                break;
            case HOUSES:
                data= final_data.remove(0);
                while (!Objects.equals(data, END)){
                    gson.fromJson(data, House.class).save();
                    data= final_data.remove(0);
                }
                break;
            case TENANTS:
                data= final_data.remove(0);
                while (!Objects.equals(data, END)){
                    gson.fromJson(data, Tenant.class).save();
                    data= final_data.remove(0);
                }
                break;
            case PAYMENTS:
                data= final_data.remove(0);
                while (!Objects.equals(data, END)){
                    gson.fromJson(data, Payment.class).save();
                    data= final_data.remove(0);
                }
                break;
            case SETTINGS:
                data= final_data.remove(0);
                while (!Objects.equals(data, END)){
                    gson.fromJson(data, Setting.class).save();
                    data= final_data.remove(0);
                }
                break;
        }
        return true;
    }
}
