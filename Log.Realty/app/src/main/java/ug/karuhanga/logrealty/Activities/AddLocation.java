package ug.karuhanga.logrealty.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.R;

public class AddLocation extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private EditText location;
    private EditText rent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab_add_location);
        location= (EditText) findViewById(R.id.edit_text_add_location_location);
        rent= (EditText) findViewById(R.id.edit_text_add_location_amount);

        fab.setOnClickListener(this);
    }

    private void addLocation() {
        //TODO Add Error checking
        String name= location.getText().toString();
        int amount= Integer.valueOf(rent.getText().toString());
        if (amount< Helpers.AMOUNT_MINIMUM_RENT){
            Toast.makeText(this, "Rent must be >=250,000/=", Toast.LENGTH_SHORT).show();
            return;
        }
        name= Helpers.toFirstsCapital(name);
        new Location(name, amount).save();
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fab)){
            addLocation();
        }
    }
}
