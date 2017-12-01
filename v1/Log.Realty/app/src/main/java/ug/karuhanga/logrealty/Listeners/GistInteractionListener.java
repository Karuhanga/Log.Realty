package ug.karuhanga.logrealty.Listeners;

import java.util.List;

import ug.karuhanga.logrealty.Data.MinifiedRecord;

/**
 * Created by karuhanga on 9/26/17.
 */

public interface GistInteractionListener {
    boolean onBackPressed();
    boolean onEditPressed();
    boolean onDeletePressed();
    List<MinifiedRecord> getCoreData();
}
