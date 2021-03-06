package ug.karuhanga.logrealty.Data;

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

    public String getSummary(){
        return this.toString();
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
