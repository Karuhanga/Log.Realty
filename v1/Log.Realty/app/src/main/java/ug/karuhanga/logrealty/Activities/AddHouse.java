package ug.karuhanga.logrealty.Activities;

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

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.R;

public class AddHouse extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    FloatingActionButton fab;
    EditText editTextNumber;
    AutoCompleteTextView editTextLocation;
    CheckBox checkBoxSingleHouse;
    CheckBox checkBoxDefaultRent;
    EditText description;
    EditText editTextRentPaid;
    ArrayAdapter<Location> adapter;
    List<Location> results;
    Location chosen;
    private int previousColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_house_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab_add_house);
        checkBoxSingleHouse= (CheckBox) findViewById(R.id.check_box_single_house_add_house);
        checkBoxDefaultRent= (CheckBox) findViewById(R.id.check_box_add_house_location_rent);
        editTextLocation= (AutoCompleteTextView) findViewById(R.id.edit_text_add_house_location);
        editTextNumber= (EditText) findViewById(R.id.edit_text_add_house_number);
        editTextRentPaid= (EditText) findViewById(R.id.edit_text_add_house_rent_paid);
        description= (EditText) findViewById(R.id.edit_text_add_house_description);
        previousColor= editTextLocation.getCurrentTextColor();
        chosen= null;
        adapter= null;

        editTextLocation.setOnItemClickListener(this);
        editTextLocation.setThreshold(1);
        editTextLocation.addTextChangedListener(this);
        editTextNumber.addTextChangedListener(this);
        editTextRentPaid.addTextChangedListener(this);
        description.addTextChangedListener(this);
        checkBoxSingleHouse.setOnCheckedChangeListener(this);
        checkBoxDefaultRent.setOnCheckedChangeListener(this);

        results= Select.from(Location.class).list();
        adapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, results);
        adapter.setNotifyOnChange(true);
        editTextLocation.setAdapter(adapter);

        fab.setOnClickListener(this);
    }

    private void addHouse(){
        int number;
        String desc;
        int rentPaid;
        House house;
         //Must have a location
        if (chosen==null){
            onError(editTextLocation, "Please pick a house location or add one first");
            return;
        }

        //Only one single house
        List<House> results= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(0)).list();
        if (!(results.isEmpty())){
            onError(editTextNumber, "This house was already added");
            return;
        }

        //No description and house number
        if (checkBoxSingleHouse.isChecked()){

            results= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).list();
            if (!(results.isEmpty())){
                onError(editTextNumber, "This house was already added");
                return;
            }

            //No rent
            if (checkBoxDefaultRent.isChecked()){
                house= new House(chosen);
                house.save();
                Intent finisher= new Intent();
                finisher.putExtra("details", house.toString());
                finish();
                return;
            }

            //else get rent paid
            try{
                rentPaid= Integer.valueOf(editTextRentPaid.getText().toString());
            }catch (NumberFormatException e){
                onError(editTextRentPaid, "Please input a rent amount");
                return;
            }
            if (rentPaid< Helpers.AMOUNT_MINIMUM_RENT){
                onError(editTextRentPaid, "Rent must be at least UgShs.250,000/=");
                return;
            }
            house= new House(chosen, rentPaid);
            house.save();
            Intent finisher= new Intent();
            finisher.putExtra("details", house.toString());
            finish();
            return;
        }

        desc= description.getText().toString();
        if (desc.length()<1){
            onError(description, "Please provide a small description");
            return;
        }

        try{
            number= Integer.valueOf(editTextNumber.getText().toString());
        }catch (NumberFormatException e){
            onError(editTextNumber, "Please input a house number");
            return;
        }
        List<House> responses= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(number)).list();
        if (!(responses.isEmpty())){
            onError(editTextNumber, "This house was added already");
            return;
        }
        if (number==0){
            onError(editTextNumber, "Please input a valid house number");
            return;
        }

        if (checkBoxDefaultRent.isChecked()){
            house= new House(number, desc, chosen);
            house.save();
            Intent finisher= new Intent();
            finisher.putExtra("details", house.toString());
            finish();
            return;
        }

        try{
            rentPaid= Integer.valueOf(editTextRentPaid.getText().toString());
        }catch (NumberFormatException e){
            onError(editTextRentPaid, "Please input a rent amount");
            return;
        }
        if (rentPaid< Helpers.AMOUNT_MINIMUM_RENT){
            onError(editTextRentPaid, "Rent must be at least UgShs.250,000/=");
            return;
        }

        house= new House(number,desc, chosen, rentPaid);
        house.save();
        Intent finisher= new Intent();
        finisher.putExtra("details", house.toString());
        finish();
    }

    protected void onError(EditText view, String notif){
        view.setTextColor(Color.RED);
        Toast.makeText(this, notif, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fab)){
            addHouse();
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
                description.setVisibility(View.GONE);
            }
            else{
                editTextNumber.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
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
}
