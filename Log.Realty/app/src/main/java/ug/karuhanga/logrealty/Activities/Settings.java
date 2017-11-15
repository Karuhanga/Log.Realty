package ug.karuhanga.logrealty.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import ug.karuhanga.logrealty.Data.Setting;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.R;

public class Settings extends AppCompatActivity {
    private boolean editting;
    private FloatingActionButton fab;
    private TextView textViewEmail;
    private EditText editTextEmail;
    private TextView textViewMessage1;
    private TextView textViewMessage2;
    private EditText editTextMessage1;
    private EditText editTextMessage2;
    private CheckBox checkBoxBackup;

    private List<View> textViews= new ArrayList<>();
    private List<View> editTexts= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editting= false;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        textViewEmail= (TextView) findViewById(R.id.text_view_settings_email);
        textViewMessage1= (TextView) findViewById(R.id.text_view_settings_message_1);
        textViewMessage2= (TextView) findViewById(R.id.text_view_settings_message_2);
        editTextEmail= (EditText) findViewById(R.id.edit_text_settings_email);
        editTextMessage1= (EditText) findViewById(R.id.edit_text_settings_message_1);
        editTextMessage2= (EditText) findViewById(R.id.edit_text_settings_message_2);
        checkBoxBackup= (CheckBox) findViewById(R.id.check_box_backup);

        textViews.add(textViewEmail);
        textViews.add(textViewMessage1);
        textViews.add(textViewMessage2);
        editTexts.add(editTextEmail);
        editTexts.add(editTextMessage1);
        editTexts.add(editTextMessage2);

        refresh();
        checkBoxBackup.setClickable(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editting){
                    finishEdit();
                }
                else{
                    startEdit();
                }
            }
        });
    }

    private void startEdit(){
        editting= true;
        fab.setImageResource(R.drawable.ic_done_black_24dp);
        checkBoxBackup.setClickable(true);

        for (int i = 0; i < 3; i++) {
            hide(textViews.get(i));
            show(editTexts.get(i));
        }

    }

    private void finishEdit(){
        editting= false;
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        checkBoxBackup.setClickable(false);

        for (int i = 0; i < 3; i++) {
            hide(editTexts.get(i));
            show(textViews.get(i));
        }

        List<Setting> settings= Select.from(Setting.class).list();
        for (Setting setting:settings){
            switch (setting.getName()){
                case Helpers.SETTINGS_EMAIL:
                    setting.setData(editTextEmail.getText().toString()).save();
                    break;
                case Helpers.SETTINGS_REMINDER_1:
                    setting.setData(editTextMessage1.getText().toString()).save();
                    break;
                case Helpers.SETTINGS_REMINDER_2:
                    setting.setData(editTextMessage2.getText().toString()).save();
                    break;
                case Helpers.ALLOW_BACKUP:
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
                case Helpers.SETTINGS_EMAIL:
                    editTextEmail.setText(setting.getData());
                    textViewEmail.setText(setting.getData());
                    break;
                case Helpers.SETTINGS_REMINDER_1:
                    editTextMessage1.setText(setting.getData());
                    textViewMessage1.setText(setting.getData());
                    break;
                case Helpers.SETTINGS_REMINDER_2:
                    editTextMessage2.setText(setting.getData());
                    textViewMessage2.setText(setting.getData());
                    break;
                case Helpers.ALLOW_BACKUP:
                    checkBoxBackup.setChecked(setting.getStatus());
                    break;
                default:
                    break;
            }
        }
    }

    private void hide(final View view) {
        view.animate().scaleY(0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.setScaleY(1f);
                view.setVisibility(View.GONE);
            }
        }).start();
    }

    private void show(View view) {
        view.setScaleY(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().scaleY(1f);
    }

}
