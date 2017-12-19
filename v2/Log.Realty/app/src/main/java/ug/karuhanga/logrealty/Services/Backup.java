package ug.karuhanga.logrealty.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.orm.query.Select;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ug.karuhanga.logrealty.Models.House;
import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.Models.Payment;
import ug.karuhanga.logrealty.Models.Setting;
import ug.karuhanga.logrealty.Models.Tenant;
import ug.karuhanga.logrealty.Helper;

import static ug.karuhanga.logrealty.Helper.SPECIAL_CRLF;
import static ug.karuhanga.logrealty.Helper.getBackupDirectory;
import static ug.karuhanga.logrealty.Helper.listFilesInDir;
import static ug.karuhanga.logrealty.Helper.writeToFile;

public class Backup extends Service {
    private List<String> final_data= new ArrayList<>();
    private final String START= "<--START_";
    private final String END= "<--END-->";
    private final String HOUSES= "HOUSES-->";
    private final String LOCATIONS= "LOCATIONS-->";
    private final String PAYMENTS= "PAYMENTS-->";
    private final String SETTINGS= "SETTINGS-->";
    private final String TENANTS= "TENANTS-->";

    private final int MAX_LOCAL_BACKUP_COUNT= 8;

    public Backup() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        cleanup();
        new Runnable(){
            @Override
            public void run() {
                commenceBackup();
            }
        }.run();
        return Service.START_NOT_STICKY;
    }

    private void cleanup() {
        String backupDirectory= getBackupDirectory(getApplicationContext());
        List<String> backups= listFilesInDir(backupDirectory);
        if (backups.size()<=MAX_LOCAL_BACKUP_COUNT){
            return;
        }

        for (long backup : getExpiredBackups(backups)) {
            File file= new File(backupDirectory+ File.separator+ "Backup_"+String.valueOf(backup));
            if (file.exists()){
                file.delete();
            }
        }
    }

    private List<Long> getExpiredBackups(List<String> backups) {
        List<Long> backupDates= new ArrayList<>();
        for (String item : backups) {
            backupDates.add(Long.parseLong(item.split("_")[1]));
        }
        Collections.sort(backupDates);
        return backupDates.subList(0, backupDates.size()-MAX_LOCAL_BACKUP_COUNT);
    }

    private void commenceBackup() {
        ensureLocationExists();
        final_data.clear();
        final_data.add("Backup_"+ String.valueOf(Helper.getTodaysDate().getTime()));
        appendData(LOCATIONS);
        appendData(HOUSES);
        appendData(TENANTS);
        appendData(PAYMENTS);
        appendData(SETTINGS);
        final_data.add(END);
        saveToFile(final_data.get(0));
    }

    private void ensureLocationExists() {

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

    private void saveToFile(String fileName){
        StringBuilder stringBuilder= new StringBuilder("");
        for (String item : final_data) {
            stringBuilder.append(item);
            stringBuilder.append(SPECIAL_CRLF);
        }

        writeToFile(getBackupDirectory(getApplicationContext())+ File.separator+ fileName, stringBuilder.toString());
    }
}
