package ug.karuhanga.logrealty.Data;

/**
 * Created by karuhanga on 11/10/17.
 */

public class Setting extends Record {
    private String name;
    private boolean status;
    private String data;

    public Setting(){

    }

    public Setting(String name, boolean status) {
        this.name = name;
        this.status = status;
        data= null;
    }

    public Setting(String name, String data) {
        this.name = name;
        this.data = data;
        status= false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

    public Setting setData(String data) {
        this.data = data;
        return this;
    }

    public String toString(){
        return name+": "+((data==null)? String.valueOf(status) : data);
    }

    public String summarize(){
        return toString();
    }
}
