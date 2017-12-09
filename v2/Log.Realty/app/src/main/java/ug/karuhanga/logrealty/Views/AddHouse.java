package ug.karuhanga.logrealty.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import ug.karuhanga.logrealty.Controllers.Controller;
import ug.karuhanga.logrealty.Helper;
import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.APP_TAG;
import static ug.karuhanga.logrealty.Helper.hide;
import static ug.karuhanga.logrealty.Helper.show;

public class AddHouse extends AppCompatActivity implements Controller.AddHouseControllerExternalInterface, AdapterView.OnItemClickListener {

    //Constants
    private final int SINGLE_HOUSE_DEFAULT_RENT= 0;
    private final int SINGLE_HOUSE_UNIQUE_RENT= 1;
    private final int MULTIPLE_HOUSE_DEFAULT_RENT = 2;
    private final int MULTIPLE_HOUSE_UNIQUE_RENT = 3;
    private final int INVALID_INPUT= -1;

    //Activity Visual Components
    @BindView(R.id.edit_text_add_house_location) AutoCompleteTextView editTextLocation;
    @BindView(R.id.check_box_add_house_single_house) CheckBox checkBoxSingleHouse;
    @BindView(R.id.edit_text_add_house_number) EditText editTextNumber;
    @BindView(R.id.edit_text_add_house_description) EditText editTextDescription;
    @BindView(R.id.check_box_add_house_location_rent) CheckBox checkBoxDefaultRent;
    @BindView(R.id.edit_text_add_house_rent_paid) EditText editTextRentPaid;
//    @BindView(R.id.toolbar_add_house) Toolbar toolbar;

    //Module Controller
    AddHouseActivityExternalInterface controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_house_activity);
        ButterKnife.bind(this);
        controller= Controller.injectAddHouseActivityExternalInterface(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextLocation.setOnItemClickListener(this);
        editTextLocation.setThreshold(1);
    }

    @Override
    protected void onResume(){
        editTextLocation.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, getController().getLocationData()));
        super.onResume();
    }

    @OnClick(R.id.fab_add_house)
    public void attemptSubmit(){
        switch (validateInput()){
            case SINGLE_HOUSE_DEFAULT_RENT:
                getController().onSubmit();
                break;
            case SINGLE_HOUSE_UNIQUE_RENT:
                getController().onSubmit(getRent());
                break;
            case MULTIPLE_HOUSE_DEFAULT_RENT:
                getController().onSubmit(getHouseNumber(), getDescription());
                break;
            case MULTIPLE_HOUSE_UNIQUE_RENT:
                getController().onSubmit(getHouseNumber(), getDescription(), getRent());
                break;
            default:
                //TODO Something fancier for error notification
        }
    }

    @OnCheckedChanged({R.id.check_box_add_house_location_rent, R.id.check_box_add_house_single_house})
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton==checkBoxSingleHouse){
            if (b){
                hide(editTextNumber);
                hide(editTextDescription);
            }
            else{
                show(editTextNumber);
                show(editTextDescription);
            }
        }
        else if (compoundButton==checkBoxDefaultRent){
            if (b){
                hide(editTextRentPaid);
            }
            else{
                show(editTextRentPaid);
            }
        }
    }

    @Override
    public void complainAboutLocation(String message) {
        editTextLocation.setError(message);
        editTextLocation.requestFocus();
    }

    @Override
    public void complainAboutHouse(String message) {
        editTextNumber.setError(message);
        editTextNumber.requestFocus();
    }

    @Override
    public void complainAboutRent(String message) {
        editTextRentPaid.setError(message);
        editTextRentPaid.requestFocus();
    }

    @Override
    public void finish(long id, String summary) {
        Intent finisher= new Intent();
        finisher.putExtra("details", summary);
        finisher.putExtra("id", id);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        getController().onLocationPick((Location) adapterView.getItemAtPosition(i));
        editTextNumber.requestFocus();
    }

    public interface AddHouseActivityExternalInterface{
        void onSubmit(int houseNo, String description, long rentPaid);
        void onSubmit(long rentPaid);
        void onSubmit(int houseNo, String description);
        void onSubmit();
        List<Location> getLocationData();
        void onLocationPick(Location location);
    }

    private int validateInput(){
        if (checkBoxSingleHouse.isChecked()){
            if (checkBoxDefaultRent.isChecked()){
                return SINGLE_HOUSE_DEFAULT_RENT;
            }
            else{
                if (empty(editTextRentPaid)){
                    editTextRentPaid.setError(Helper.ERROR_REQUIRED);
                    editTextRentPaid.requestFocus();
                    return INVALID_INPUT;
                }
                return SINGLE_HOUSE_UNIQUE_RENT;
            }
        }
        else{
            if (empty(editTextNumber)){
                editTextNumber.setError(Helper.ERROR_REQUIRED);
                editTextNumber.requestFocus();
                return INVALID_INPUT;
            }

            if (empty(editTextDescription)){
                editTextDescription.setError(Helper.ERROR_REQUIRED);
                editTextDescription.requestFocus();
                return INVALID_INPUT;
            }

            if (checkBoxDefaultRent.isChecked()){
                return MULTIPLE_HOUSE_DEFAULT_RENT;
            }
            else{
                if (empty(editTextRentPaid)){
                    editTextRentPaid.setError(Helper.ERROR_REQUIRED);
                    editTextRentPaid.requestFocus();
                    return INVALID_INPUT;
                }
                return MULTIPLE_HOUSE_UNIQUE_RENT;
            }
        }
    }

    private boolean empty(EditText element){
        return element.getText().toString().length() == 0;
    }

    private long getRent(){
        long rent= 0;
        try {
            rent= Long.valueOf(editTextRentPaid.getText().toString());
        }catch (NumberFormatException e){
            Log.d(APP_TAG, "Long Conversion failed");
        }
        return rent;
    }

    private int getHouseNumber(){
        int number= 0;
        try {
            number= Integer.valueOf(editTextNumber.getText().toString());
        }catch (NumberFormatException e){
            Log.d(APP_TAG, "Integer Conversion failed");
        }
        return number;
    }

    public String getDescription(){
        return editTextDescription.getText().toString();
    }

    private AddHouseActivityExternalInterface getController(){
        if (controller == null){
            controller= Controller.injectAddHouseActivityExternalInterface(this);
        }
        return controller;
    }
}
