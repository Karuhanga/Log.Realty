package ug.karuhanga.logrealty.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by karuhanga on 9/29/17.
 */

public class Confirmation extends AlertDialog implements DialogInterface.OnClickListener {
    ConfirmationExternalInterface caller;
    private int requestCode;

    public Confirmation(Context context){
        super(context);
    }

    public Confirmation(Context context, ConfirmationExternalInterface caller, String title, String message, int icon, String accept, String reject, int requestCode){
        super(context);
        this.caller= caller;
        this.requestCode= requestCode;
        setTitle(title);
        setMessage(message);
        setIcon(icon);
        setButton(BUTTON_POSITIVE, accept, this);
        setButton(BUTTON_NEGATIVE, reject, this);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        caller.onReceiveResult(i==BUTTON_POSITIVE, requestCode);
    }

    public interface ConfirmationExternalInterface {
        void onReceiveResult(boolean result, int requestCode);
    }
}
