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
import android.widget.EditText;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.R;

public class AddHouse extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {

    FloatingActionButton fab;
    EditText editTextNumber;
    AutoCompleteTextView editTextLocation;
    EditText description;
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

        fab = (FloatingActionButton) findViewById(R.id.fab_add_house);
        editTextLocation= (AutoCompleteTextView) findViewById(R.id.edit_text_add_house_location);
        editTextNumber= (EditText) findViewById(R.id.edit_text_add_house_number);
        description= (EditText) findViewById(R.id.edit_text_add_house_description);
        previousColor= editTextLocation.getCurrentTextColor();
        chosen= null;
        adapter= null;

        editTextLocation.setOnItemClickListener(this);
        editTextLocation.setThreshold(1);
        editTextLocation.addTextChangedListener(this);

        results= Select.from(Location.class).list();
        adapter= new ArrayAdapter<>(this, R.layout.list_item_dropdown, R.id.textView_listItem_dropDown, results);
        adapter.setNotifyOnChange(true);
        editTextLocation.setAdapter(adapter);

        fab.setOnClickListener(this);
    }

    private void addHouse(){
        int number;
        String desc;

        desc= description.getText().toString();

        try{
            number= Integer.valueOf(editTextNumber.getText().toString());
        }catch (NumberFormatException e){
            onNumberError("Number Error");
            return;
        }

        List<House> responses= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(number)).list();
        if (!(responses.isEmpty())){
            onNumberError("A House with this No. already exists");
            return;
        }
        //TODO Check if number already exists
        //TODO Add unique rent entry option

        if (chosen==null){
            onNoMatch();
            return;
        }

        House house= new House(number,desc, chosen);
        house.save();
        Intent finisher= new Intent();
        finisher.putExtra("details", house.toString());
        finish();
    }

    protected void onNumberError(String notif){
        Toast.makeText(this, notif, Toast.LENGTH_SHORT).show();
    }

    protected void onNoMatch(){
        editTextLocation.setTextColor(Color.RED);
        Toast.makeText(this, "Please pick a Location", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
