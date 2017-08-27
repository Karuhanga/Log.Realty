package ug.karuhanga.logrealty;

import com.orm.SugarRecord;

/**
 * Created by karuhanga on 8/27/17.
 */

public abstract class Record extends SugarRecord {
    boolean synced= false;

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
