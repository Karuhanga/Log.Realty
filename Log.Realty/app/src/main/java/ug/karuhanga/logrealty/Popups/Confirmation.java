package ug.karuhanga.logrealty.Popups;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by karuhanga on 9/29/17.
 */

public class Confirmation extends AlertDialog implements DialogInterface.OnClickListener {
    ug.karuhanga.logrealty.Listeners.Confirmation caller;
    private int requestCode;
    public Confirmation(Context context){
        super(context);
        //TODO Check for wrong class cast
    }

    public Confirmation(Context context, ug.karuhanga.logrealty.Listeners.Confirmation caller, String title, String message, int icon, String accept, String reject, int requestCode){
        super(context);
        this.caller= caller;
        this.requestCode= requestCode;
        //TODO Check for wrong class cast
        setTitle(title);
        setMessage(message);
        setIcon(icon);
        setButton(BUTTON_POSITIVE, accept, this);
        setButton(BUTTON_NEGATIVE, reject, this);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i!=BUTTON_POSITIVE){
            return;
        }
        caller.onReceiveResult(true, requestCode);
        return;
    }
}
