package ug.karuhanga.logrealty.Models;

/**
 * Created by karuhanga on 12/9/17.
 */

public class Summarizable implements Listable {

    private String summary;
    private String string;
    private Long Id;

    public Summarizable(Long Id, String string, String summary){
        this.summary= summary;
        this.Id= Id;
        this.string= string;
    }

    @Override
    public Long getId() {
        return this.Id;
    }

    @Override
    public String toString(){
        return string;
    }

    @Override
    public String summarize() {
        return summary;
    }
}
