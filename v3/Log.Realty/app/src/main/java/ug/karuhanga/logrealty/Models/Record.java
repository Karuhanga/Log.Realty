package ug.karuhanga.logrealty.Models;

import com.orm.SugarRecord;

/**
 * Created by karuhanga on 8/27/17.
 */

public abstract class Record extends SugarRecord implements Listable {
    boolean synced= false;

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    protected boolean onDelete(){
        return true;
    }

    @Override
    public boolean delete(){
        boolean result= onDelete();
        return super.delete() && result;
    }

    public boolean equals(Record record){
        return getId()==record.getId();
    }
}
