package ug.karuhanga.logrealty.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ug.karuhanga.logrealty.Controllers.Controller;
import ug.karuhanga.logrealty.Helper;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.APP_TAG;
import static ug.karuhanga.logrealty.Helper.ERROR_REQUIRED;
import static ug.karuhanga.logrealty.Helper.TAG_APP_NAME;
import static ug.karuhanga.logrealty.Helper.log;

public class AddLocation extends AppCompatActivity implements Controller.AddLocationControllerExternalInterface {

    //Activity Visual Components
    @BindView(R.id.edit_text_add_location_location) EditText editTextLocation;
    @BindView(R.id.edit_text_add_location_amount) EditText editTextRent;
//    @BindView(R.id.toolbar_add_location) Toolbar toolbar;
    //Module Controller
    private AddLocation.AddLocationActivityExternalInterface controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location_activity);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Add Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        controller= Controller.injectAddLocationActivityExternalInterface(this);
    }

    @OnClick(R.id.fab_add_location)
    public void attemptSubmit(){
        if (validateInput()){
            getController().onSubmit(getLocation(), getRent());
        }
    }

    private boolean validateInput(){
        if (empty(editTextLocation)){
            editTextLocation.setError(ERROR_REQUIRED);
            return false;
        }

        if (empty(editTextRent)){
            editTextRent.setError(ERROR_REQUIRED);
            return false;
        }
        return true;
    }

    private String getLocation(){
        String name= editTextLocation.getText().toString();
        name= Helper.cleaner(name);
        if (name == null){
            Log.d(TAG_APP_NAME, "Problem cleaning name");
            name= "Location";
        }
        return name;
    }

    private long getRent(){
        long rent= 0;
        try {
            rent= Long.valueOf(editTextRent.getText().toString());
        }catch (NumberFormatException e){
            Log.d(APP_TAG, "Long Conversion failed");
        }
        return rent;
    }

    private boolean empty(EditText element){
        return element.getText().toString().length() == 0;
    }

    private AddLocationActivityExternalInterface getController(){
        if (controller == null){
            controller= Controller.injectAddLocationActivityExternalInterface(this);
        }
        return controller;
    }

    @Override
    public void raise(String message) {
        editTextRent.setError(message);
        editTextRent.requestFocus();
    }

    @Override
    public void finish(long id, String summary) {
        Intent finisher= new Intent();
        finisher.putExtra("details", summary);
        finisher.putExtra("id", id);
        finish();
    }

    public interface AddLocationActivityExternalInterface{
        void onSubmit(String location, long rent);
    }
}
