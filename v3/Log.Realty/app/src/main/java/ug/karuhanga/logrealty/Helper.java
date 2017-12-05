package ug.karuhanga.logrealty;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

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


    public static final int FLAG_NO_FLAGS= 0;
    public static final int FLAG_ONE_DAY_TO= -1;
    public static final int FLAG_ONE_DAY_AFTER= 1;
    public static final int FLAG_EVERY_FIVE_DAYS= 400;

    public static final int REQUEST_CODE_DETAILS= 300;
    public static final int REQUEST_CODE_DELETE= 301;
    public static final int REQUEST_CODE_REPLACE= 302;
    public static final int REQUEST_CODE_EDIT= 303;
    public static final int REQUEST_CODE_NOTIFY= 304;

    public static final String SETTINGS_FIRST_TIME= "First_Time";
    public static final String SETTINGS_EMAIL= "Email";
    public static final String SETTINGS_REMINDER_1= "Reminder_1";
    public static final String SETTINGS_REMINDER_2= "Reminder_2";
    public static final String ALLOW_BACKUP= "Backup";
    public static final String TAG_APP_NAME= "Log.Realty";

    public static final String TRUE= "1";
    public static final String FALSE= "0";

    public static final int AMOUNT_MINIMUM_RENT= 250000;

    public static final String REGEX_EMAIL= "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";



    public static String toCurrency(long number){
        String[] result= String.valueOf(number).split("");
        List<String> letters=  new ArrayList<>();
        int length= result.length;
        int count= 0;
        int tracker= 0;
        while (tracker<length){
            if (count==3){
                letters.add(0, ",");
                count= 0;
            }
            else{
                letters.add(0, result[length-tracker-1]);
                count++;
                tracker++;
            }
        }
        return letters.toString()+"/=";
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
}
