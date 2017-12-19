package ug.karuhanga.logrealty;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by karuhanga on 12/1/17.
 */

public class Helper {
    public static final String APP_TAG= "Log.Realty";

    public static final int FRAGMENT_DUE_PAYMENTS= 1001;
    public static final int FRAGMENT_LOCATIONS= 1002;
    public static final int FRAGMENT_HOUSES= 1003;
    public static final int FRAGMENT_TENANTS= 1004;
    public static final int FRAGMENT_PAYMENTS= 1005;
    public static final int FRAGMENT_NONE= -1000;

    public static final int RESULT_CODE_ADD_PAYMENT= 100;
    public static final int RESULT_CODE_SETTINGS= 101;
    public static final int RESULT_CODE_ADD_LOCATION= 102;
    public static final int RESULT_CODE_ADD_HOUSE= 103;
    public static final int RESULT_CODE_ADD_TENANT= 104;

    public static final int RESULT_CODE_REFRESH= 105;

    public static final int FLAG_NO_FLAGS= 0;
    public static final int FLAG_ONE_DAY_TO= -1;
    public static final int FLAG_ONE_DAY_AFTER= 1;
    public static final int FLAG_EVERY_FIVE_DAYS= 400;

    public static final int REQUEST_CODE_DETAILS= 300;
    public static final int REQUEST_CODE_DELETE= 301;
    public static final int REQUEST_CODE_REPLACE= 302;
    public static final int REQUEST_CODE_EDIT= 303;
    public static final int REQUEST_CODE_NOTIFY= 304;
    public static final int REQUEST_CODE_BACKUP= 305;

    public static final String SETTINGS_FIRST_TIME= "First_Time";
    public static final String SETTINGS_USER_NAME= "Email";
    public static final String SETTINGS_REMINDER_1= "Reminder_1";
    public static final String SETTINGS_REMINDER_2= "Reminder_2";
    public static final String SETTINGS_ALLOW_BACKUP = "Backup";
    public static final String TAG_APP_NAME= "Log.Realty";
    public static final String EX= "ex";
    public static final String RENT_DUE= "rentDue";

    public static final String BACKUP_DIR= "Backups";
    public static final String SPECIAL_CRLF= "\nLog\n.\nRealty\n";

    public static final String TRUE= "1";
    public static final String FALSE= "0";

    public static final long AMOUNT_MINIMUM_RENT= 250000;

    public static final String ERROR_REQUIRED= "This is a required field";


    public static final String REGEX_EMAIL= "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";



    public static String toCurrency(long number){
        String[] result= String.valueOf(number).split("");
        List<String> letters=  new ArrayList<>();
        int absLength= result.length-1;
        int count= 0;
        int tracker= 0;
        while (tracker<absLength){
            if (count==3){
                letters.add(0, ",");
                count= 0;
            }
            else{
                letters.add(0, result[absLength-tracker]);
                count++;
                tracker++;
            }
        }
        StringBuilder requested= new StringBuilder();
        for (String letter : letters) {
            requested.append(letter);
        }
        requested.append("/=");
        return requested.toString();
    }

    public static String getRentBelowMinNotif(){
        return "Rent must be at least "+toCurrency(AMOUNT_MINIMUM_RENT);
    }

    public static Date getLaterDateByMonths(Date oldDate, long months){
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
        String date[]= formatter.format(oldDate).split("/");

        int month= Integer.valueOf(date[1]);
        month+= months;
        date[1]= String.valueOf(month);

        Date newDate;

        try {
            newDate= formatter.parse(String.format("%s/%s/%s", date[0], date[1], date[2]));
        } catch (ParseException e) {
            return oldDate;
        }
        return newDate;
    }

    public static Date getLaterDateByDays(Date oldDate, int days){
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
        String date[]= formatter.format(oldDate).split("/");

        int day= Integer.valueOf(date[0]);
        day+= days;
        date[0]= String.valueOf(day);

        Date newDate;

        try {
            newDate= formatter.parse(String.format("%s/%s/%s", date[0], date[1], date[2]));
        } catch (ParseException e) {
            return oldDate;
        }
        return newDate;
    }

    public static Date getTodaysDate(){
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.getTime();
    }

    public static Date makeDate(int day, int month, int year){
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
        Date result= null;
        try {
            result= formatter.parse(String.format("%d/%d/%d", day, month+1, year));
        } catch (ParseException e) {
            result= null;
        }finally {
            return result;
        }
    }

    public static ArrayList<Integer> breakDate(Date date){
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<Integer> result= new ArrayList<>();

        String[] temp= formatter.format(date).split("/");
        for (String item : temp) {
            result.add(Integer.parseInt(item));
        }
        result.set(1, result.get(1)-1);

        return result;
    }

    public static String dateToString(Date date){
        if (date==null){
            return "";
        }
        String[] helper= date.toString().split(" ");
        return helper[0]+", "+helper[2]+" "+helper[1]+" "+helper[5];
    }

    public static String cleaner(String text){
        if (text==null || text.length()<1){
            return null;
        }
        text= text.substring(0, 1).toUpperCase()+ Helper.toFirstsCapital(text);
        return text;
    }

    @NonNull
    public static String toFirstsCapital(String old){
        String result= old.toLowerCase();
        String[] words= result.split(" ");
        result= "";
        for (String word : words) {
            String first= word.split("")[0].toUpperCase();
            word= word.replaceFirst(word.substring(0, 1), first);
            result+= (word+" ");
        }
        return result.trim();
    }

    public static String getStringByName(Context context, String name){
        Resources res= context.getResources();
        int id= res.getIdentifier(name, "string", context.getPackageName());
        String result;
        try {
            result= res.getString(id);
        }catch (Resources.NotFoundException e){
            result= null;
        }
        return result;
    }

    public static void hide(final View view) {
        view.animate().scaleY(0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.setScaleY(1f);
                view.setVisibility(View.GONE);
            }
        }).start();
    }

    public static void show(View view) {
        view.setScaleY(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().scaleY(1f);
    }

    public static void log(String message){
        Log.d(TAG_APP_NAME, message);
    }

    public static boolean empty(EditText element){
        return element.getText().toString().length() == 0;
    }

    public static String readFromFile(String path) {
        String finalInput= "";
        File file= new File(path);
        if (!file.exists()){
            log("Helpers: The file at "+path+" does not exist.");
            return finalInput;
        }

        try {
            InputStream inputStream= new FileInputStream(file);
            int length= inputStream.available();
            byte[] data= new byte[length];
            inputStream.read(data);
            finalInput= new String(data);
        } catch (IOException e) {
            log("Helpers: "+e.toString());
        }

        return finalInput;
    }

    public static boolean writeToFile(String path, String content){
        File file= new File(path);
        if (file.exists()){
            if (!file.delete()){
                log("Helper: File at "+path+" can't be overwritten");
                return false;
            }
        }

        try {
            OutputStream outputStream= new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log("Helpers: "+e.toString());
            return false;
        }
        return true;
    }

    public static List<String> listFilesInDir(String path){
        File dir= new File(path);
        List<String> results= new ArrayList<>();

        if (!dir.exists()) {
            log("Helper: Directory at "+path+" does not exist");
            return results;
        }
        if (!dir.isDirectory()) {
            log("Helper: "+path+" is not a directory");
            return results;
        }

        File[] filesAndDirs= dir.listFiles();
        for (File file : filesAndDirs) {
            if (file.isFile()){
                results.add(file.getName());
            }
        }

        return results;
    }

    public static String getBackupDirectory(Context context){
        File appStorageLocation = context.getFilesDir();
        File backupDir= new File(appStorageLocation.getAbsolutePath()+ File.separator+ BACKUP_DIR);
        if (!backupDir.exists()){
            backupDir.mkdirs();
        }
        return backupDir.getAbsolutePath();
    }
}
