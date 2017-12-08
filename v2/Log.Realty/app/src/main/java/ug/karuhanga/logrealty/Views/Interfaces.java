package ug.karuhanga.logrealty.Views;

import java.util.List;

import ug.karuhanga.logrealty.Models.Listable;

/**
 * Created by karuhanga on 12/8/17.
 */

public class Interfaces {
    public interface GistExternalInterface{

        int getType();

        List<Listable> getCoreData();
    }
}
