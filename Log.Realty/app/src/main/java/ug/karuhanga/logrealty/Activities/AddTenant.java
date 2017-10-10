package ug.karuhanga.logrealty.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.Date;
import java.util.List;

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Tenant;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.Listeners.Confirmation;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helpers.REQUEST_CODE_REPLACE;

public class AddTenant extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener, Confirmation {

    private EditText first_name;
    private EditText surname;
    private EditText email;
    private EditText contact;
    private EditText id_type;
    private EditText id_no;
    private AutoCompleteTextView house_occupied;
    private DatePicker date_entered;
    private FloatingActionButton fab;
    private EditText colored;
    private int previousColor;

    String fName;
    String oNames;
    String Email;
    String Contact;
    String idType;
    String idNo;
    Date Entered;
    List<Tenant> current;


    private House chosen;
    ArrayAdapter<House> adapter;
    List<House> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tenant_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        first_name= (EditText) findViewById(R.id.edit_text_add_tenant_fname);
        surname= (EditText) findViewById(R.id.edit_text_add_tenant_onames);
        email= (EditText) findViewById(R.id.edit_text_add_tenant_email);
        contact= (EditText) findViewById(R.id.edit_text_add_tenant_contact);
        id_type= (EditText) findViewById(R.id.edit_text_add_tenant_idtype);
        id_no= (EditText) findViewById(R.id.edit_text_add_tenant_idno);
        house_occupied= (AutoCompleteTextView) findViewById(R.id.edit_text_add_tenant_house);
        date_entered= (DatePicker) findViewById(R.id.date_picker_add_tenant_entering);
        fab= (FloatingActionButton) findViewById(R.id.fab_add_tenant);

        previousColor= house_occupied.getCurrentTextColor();
        colored= first_name;
        chosen= null;
        adapter= null;

        house_occupied.setOnItemClickListener(this);
        house_occupied.setThreshold(1);

        results= Select.from(House.class).list();
        adapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, results);
        adapter.setNotifyOnChange(true);
        house_occupied.setAdapter(adapter);

        fab.setOnClickListener(this);



    }

    private void addTenant() {
        fName= first_name.getText().toString();
        fName= Helpers.cleaner(fName);
        if (fName==null){
            onError("Invalid Name", first_name);
            return;
        }

        oNames= surname.getText().toString();
        oNames= Helpers.cleaner(oNames);
        if (oNames==null){
            onError("Invalid Name", surname);
            return;
        }

        Email= email.getText().toString();
        if (!Email.matches(Helpers.REGEX_EMAIL)){
            onError("Invalid Email", email);
            return;
        }

        Contact= contact.getText().toString();
        if (Contact.length()!=10){
            onError("Invalid Contact", contact);
            return;
        }
        try {
            Long.parseLong(Contact);
        }catch(Exception e){
            onError("Invalid Contact", contact);
            return;
        }

        idType= id_type.getText().toString();
        idType= Helpers.cleaner(idType);
        if (idType==null){
            onError("Invalid ID Type", id_type);
            return;
        }

        idNo= id_no.getText().toString();
        idNo= Helpers.cleaner(idNo);
        if (idNo==null){
            onError("Invalid ID Number", id_no);
            return;
        }

        Entered= Helpers.makeDate(date_entered.getDayOfMonth(), date_entered.getMonth(), date_entered.getYear());
        if (Entered==null){
            Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (chosen==null){
            onError("Please pick a House", house_occupied);
            return;
        }

        current= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("house")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq("0")).list();
        if (current.size()>0){
            new ug.karuhanga.logrealty.Popups.Confirmation(this, this, "Are you sure?", "Replace:\n"+current.get(0).getName()+" in "+current.get(0).getHouse().getLocation().getName(), R.drawable.ic_edit_black_24dp, "Yes", "No", REQUEST_CODE_REPLACE).show();
            return;
        }
        else{
            completeAddition();
        }
    }

    private void completeAddition(){
        if (current.size()>0){
            current.get(0).setEx(true);
            current.get(0).save();
        }
        Tenant tenant= new Tenant(fName, oNames, Email, Contact, Entered, idType, idNo, chosen);
        tenant.save();
        //TODO set tenant to house..resolve error causing crush(Weak HashMap)
//        chosen.setTenant(tenant);
//        chosen.save();
        finish();
        //TODO Finish with notification to activity and chance to undo
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fab)){
            addTenant();
        }
    }

    protected void onError(String notif, EditText field){
        field.addTextChangedListener(this);
        field.setTextColor(Color.RED);
        Toast.makeText(this, notif, Toast.LENGTH_SHORT).show();
        colored= field;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        colored.setTextColor(previousColor);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        chosen= (House) adapterView.getItemAtPosition(i);
        date_entered.requestFocus();
    }

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (result && (requestCode==REQUEST_CODE_REPLACE)){
            completeAddition();
        }
    }
}
