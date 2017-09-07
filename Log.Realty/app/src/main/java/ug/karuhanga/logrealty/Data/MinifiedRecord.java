package ug.karuhanga.logrealty.Data;

/**
 * Created by karuhanga on 9/1/17.
 */

public class MinifiedRecord {
    private Long id;
    private String description;

    public MinifiedRecord(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public MinifiedRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return description;
    }
}
