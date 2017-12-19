package ug.karuhanga.logrealty.Views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.orm.query.Select;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ug.karuhanga.logrealty.Models.Setting;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.SETTINGS_ALLOW_BACKUP;
import static ug.karuhanga.logrealty.Helper.SETTINGS_USER_NAME;
import static ug.karuhanga.logrealty.Helper.hide;
import static ug.karuhanga.logrealty.Helper.log;
import static ug.karuhanga.logrealty.Helper.show;

public class Settings extends AppCompatActivity {
    private boolean editting;
    @BindView(R.id.fab_settings) FloatingActionButton fab;
    @BindView(R.id.text_view_settings_email) TextView textViewEmail;
    @BindView(R.id.edit_text_settings_email) EditText editTextEmail;
    @BindView(R.id.check_box_settings_backup) CheckBox checkBoxBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ButterKnife.bind(this);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            log("Settings View: Action Bar access issue");
        }

        editting= false;
        ensureSettingsExist();
        refresh();
    }

    private void ensureSettingsExist() {
        if (Select.from(Setting.class).count()<1){
            new Setting(SETTINGS_USER_NAME, getString(R.string.user_name_default)).save();
            new Setting(SETTINGS_ALLOW_BACKUP, true).save();
        }
    }

    @OnClick(R.id.fab_settings)
    public void onFabClick(){
        if (editting){
            finishEdit();
        }
        else{
            startEdit();
        }
    }

    private void startEdit(){
        editting= true;
        fab.setImageResource(R.drawable.ic_done_black_24dp);
        checkBoxBackup.setClickable(true);
        hide(textViewEmail);
        show(editTextEmail);
    }

    private void finishEdit(){
        editting= false;
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        List<Setting> settings= Select.from(Setting.class).list();
        for (Setting setting:settings){
            switch (setting.getName()){
                case SETTINGS_USER_NAME:
                    setting.setData(editTextEmail.getText().toString()).save();
                    break;
                case SETTINGS_ALLOW_BACKUP:
                    setting.setStatus(checkBoxBackup.isChecked()).save();
                    break;
                default:
                    break;
            }
        }
        refresh();

        Snackbar.make(fab, "All changes were saved", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private void refresh(){
        List<Setting> settings= Select.from(Setting.class).list();
        for (Setting setting:settings){
            switch (setting.getName()){
                case SETTINGS_USER_NAME:
                    editTextEmail.setText(setting.getData());
                    textViewEmail.setText(setting.getData());
                    break;
                case SETTINGS_ALLOW_BACKUP:
                    checkBoxBackup.setChecked(setting.getStatus());
                    break;
                default:
                    break;
            }
        }
        checkBoxBackup.setClickable(false);
        hide(editTextEmail);
        show(textViewEmail);
    }

}
