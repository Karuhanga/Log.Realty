package ug.karuhanga.logrealty;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by karuhanga on 8/25/17.
 */

public class Helpers {

    public static final int FRAGMENT_DUE_PAYMENTS= 1001;
    public static final int FRAGMENT_LOCATIONS= 1002;
    public static final int FRAGMENT_HOUSES= 1003;
    public static final int FRAGMENT_TENANTS= 1004;
    public static final int FRAGMENT_NONE= -1000;

    public static final int RESULT_CODE_ADD_PAYMENT= 100;
    public static final int RESULT_CODE_SETTINGS= 101;

    public static Date dueUpdater(Date oldDue, int amount, int rate){
        int months= amount/rate;
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
        String date[]= formatter.format(oldDue).split("/");

        int month= Integer.valueOf(date[1]);
        month+= months;
        date[1]= String.valueOf(month);

        Date newDue;

        try {
            newDue= formatter.parse(String.format("%d/%d/%d", date[0], date[1], date[2]));
        } catch (ParseException e) {
            return oldDue;
        }
        return newDue;
    }

    @NonNull
    public static String toFirstsCapital(@NonNull String old){
        String result= old.toLowerCase();
        String[] words= result.split(" ");
        result= "";
        for (String word : words) {
            String first= word.split("")[0].toUpperCase();
            word= word.replaceFirst(word.substring(0, 1), first);
            result+= (word+" ");
        }
        return result.substring(0,result.length());
    }
}
