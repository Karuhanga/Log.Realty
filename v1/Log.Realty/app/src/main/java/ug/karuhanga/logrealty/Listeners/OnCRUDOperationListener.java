package ug.karuhanga.logrealty.Listeners;

import ug.karuhanga.logrealty.Data.MinifiedRecord;
import ug.karuhanga.logrealty.Data.Record;

/**
 * Created by karuhanga on 9/21/17.
 */

public interface OnCRUDOperationListener {
    public void onOperationFailed(String notification);
    public void onOperationComplete(boolean successful, String message, MinifiedRecord record);
}
