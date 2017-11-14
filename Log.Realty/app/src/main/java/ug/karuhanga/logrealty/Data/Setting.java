package ug.karuhanga.logrealty.Data;

/**
 * Created by karuhanga on 11/10/17.
 */

public class Setting extends Record {
    private int name;
    private boolean status;
    private String data;

    public Setting(int name, String data) {
        this.name = name;
        this.data = data;
        status= false;
    }

    public Setting(int name, boolean status) {
        this.name = name;
        this.status = status;
        data= null;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public boolean getStatus() {
        return status;
    }

    public Setting setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
