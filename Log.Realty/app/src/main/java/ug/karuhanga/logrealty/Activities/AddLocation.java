package ug.karuhanga.logrealty.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.R;

public class AddLocation extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private FloatingActionButton fab;
    private EditText location;
    private EditText rent;
    private EditText colored;
    private int previous_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab_add_location);
        location= (EditText) findViewById(R.id.edit_text_add_location_location);
        rent= (EditText) findViewById(R.id.edit_text_add_location_amount);
        previous_color= location.getCurrentTextColor();

        fab.setOnClickListener(this);
        location.addTextChangedListener(this);
        rent.addTextChangedListener(this);
        colored=location;
    }

    private void addLocation() {
        //TODO Add Error checking
        String name= location.getText().toString();
        int amount= Integer.valueOf(rent.getText().toString());
        if (amount< Helpers.AMOUNT_MINIMUM_RENT){
            onError("Rent must be >=250,000/=", rent);
            return;
        }
        name= Helpers.cleaner(name);
        if (name==null){
            onError("Invalid Location Name", location);
            return;
        }
        new Location(name, amount).save();
        finish();
    }

    private void onError(String notif, EditText item) {
        item.setTextColor(Color.RED);
        Toast.makeText(this, notif, Toast.LENGTH_SHORT).show();
        colored= item;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fab)){
            addLocation();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        colored.setTextColor(previous_color);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
