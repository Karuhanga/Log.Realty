package ug.karuhanga.logrealty.Viewers.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Controllers.ControlProvider;
import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.R;

public class AddHouse extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, ug.karuhanga.logrealty.Controllers.AddHouse.Slave {

    FloatingActionButton fab;
    EditText editTextNumber;
    AutoCompleteTextView editTextLocation;
    CheckBox checkBoxSingleHouse;
    CheckBox checkBoxDefaultRent;
    EditText editTextDescription;
    EditText editTextRentPaid;
    ArrayAdapter<Location> adapter;
    List<Location> results;

    private Controller controller;

    private int previousColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_house_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViews();
        controller= ControlProvider.provide();

        previousColor= editTextLocation.getCurrentTextColor();
        adapter= null;

        editTextLocation.setOnItemClickListener(this);
        editTextLocation.setThreshold(1);
        editTextLocation.addTextChangedListener(this);
        editTextNumber.addTextChangedListener(this);
        editTextRentPaid.addTextChangedListener(this);
        editTextDescription.addTextChangedListener(this);
        checkBoxSingleHouse.setOnCheckedChangeListener(this);
        checkBoxDefaultRent.setOnCheckedChangeListener(this);
        fab.setOnClickListener(this);

        editTextLocation.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, controller.requestLocationData()));
    }

    private void findViews(){
        fab = (FloatingActionButton) findViewById(R.id.fab_add_house);
        checkBoxSingleHouse= (CheckBox) findViewById(R.id.check_box_add_house_single_house);
        checkBoxDefaultRent= (CheckBox) findViewById(R.id.check_box_add_house_location_rent);
        editTextLocation= (AutoCompleteTextView) findViewById(R.id.edit_text_add_house_location);
        editTextNumber= (EditText) findViewById(R.id.edit_text_add_house_number);
        editTextRentPaid= (EditText) findViewById(R.id.edit_text_add_house_rent_paid;
        editTextDescription= (EditText) findViewById(R.id.edit_text_add_house_description);
    }

    private void onSubmit(){
        int houseNumber=0;
        String description="";
        long rentPaid=0;


        if (!checkBoxSingleHouse.isChecked()){
            //Must input a description
            description= editTextDescription.getText().toString();
            if (description.length()<1){
                onSubmissionError(editTextDescription, "Please provide a small description");
                return;
            }

            try{
                houseNumber= Integer.valueOf(editTextNumber.getText().toString());
            }catch (NumberFormatException e){
                onSubmissionError(editTextNumber, "Please input a house number");
                return;
            }
        }

        if (!checkBoxDefaultRent.isChecked()){
            try{
                rentPaid= Long.valueOf(editTextRentPaid.getText().toString());
            }catch (NumberFormatException e){
                onSubmissionError(editTextRentPaid, "Please input a rent amount");
                return;
            }
        }

        controller.onSubmit(checkBoxSingleHouse.isChecked(), houseNumber, description, checkBoxDefaultRent.isChecked(), rentPaid);
    }

    protected void onSubmissionError(EditText view, String notif){
        view.setTextColor(Color.RED);
        Toast.makeText(this, notif, Toast.LENGTH_SHORT).show();
    }

    @Override
    private void onLocationError(String message){
        onSubmissionError(editTextLocation, message);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fab)){
            onSubmit();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        chosen= (Location) adapterView.getItemAtPosition(i);
        editTextNumber.requestFocus();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        editTextLocation.setTextColor(previousColor);
        editTextNumber.setTextColor(previousColor);
        editTextRentPaid.setTextColor(previousColor);
        description.setTextColor(previousColor);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton==checkBoxSingleHouse){
            if (b){
                editTextNumber.setVisibility(View.GONE);
                editTextDescription.setVisibility(View.GONE);
            }
            else{
                editTextNumber.setVisibility(View.VISIBLE);
                editTextDescription.setVisibility(View.VISIBLE);
            }
        }
        else if (compoundButton==checkBoxDefaultRent){
            if (b){
                editTextRentPaid.setVisibility(View.GONE);
            }
            else{
                editTextRentPaid.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface Controller{
        List<Location> requestLocationData();
        void onSubmit(boolean singleHouse, int houseNumber, String description, boolean defaultRent, long rentPaid);
    }
}
