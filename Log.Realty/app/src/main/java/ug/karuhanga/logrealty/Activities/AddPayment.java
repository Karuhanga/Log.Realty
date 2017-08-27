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
import android.widget.ListAdapter;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Calendar;
import java.util.List;

import ug.karuhanga.logrealty.Data.Payment;
import ug.karuhanga.logrealty.Data.Tenant;
import ug.karuhanga.logrealty.R;

public class AddPayment extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {

    FloatingActionButton fab;
    EditText editTextAmount;
    AutoCompleteTextView editTextTenant;
    ArrayAdapter<Tenant> adapter;
    List<Tenant> results;
    Tenant chosen;
    private int previousColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        editTextTenant= (AutoCompleteTextView) findViewById(R.id.edit_text_add_payment_tenant);
        editTextAmount= (EditText) findViewById(R.id.edit_text_add_payment_amount);
        previousColor= editTextTenant.getCurrentTextColor();
        chosen= null;
        adapter= new ArrayAdapter<Tenant>(this, R.layout.layout_list_item_dropdown, R.id.textView_listItem_dropDown, results);

        adapter.setNotifyOnChange(true);
        adapter.clear();
        editTextTenant.setAdapter(adapter);
        editTextTenant.setOnItemClickListener(this);


        fab.setOnClickListener(this);
    }

    private void addPayment(){
        int amount;
        try{
            amount= Integer.valueOf(editTextAmount.getText().toString());
        }catch (NumberFormatException e){
            onAmountError();
            return;
        }

        Payment payment= new Payment(Calendar.getInstance().getTime(), amount, chosen);
        payment.save();
        boolean successful= chosen.updateRentDue(payment);
        if (!successful){
            payment.delete();
            onPaymentUnsuccessful();
            return;
        }
        chosen.save();
        Intent finisher= new Intent();
        finisher.putExtra("details", "Added\n"+payment.toString());
        finish();
    }
    
    protected void onAmountError(){
        Toast.makeText(this, "Amount Error", Toast.LENGTH_SHORT).show();
    }

    protected void onPaymentUnsuccessful(){
        Toast.makeText(this, "Payment Unsuccessful", Toast.LENGTH_SHORT).show();
    }

    protected void clearAutoCompleteOptions(){
        adapter.clear();
        editTextTenant.dismissDropDown();
    }

    protected void populateOptions(){
        adapter.notifyDataSetChanged();
        editTextTenant.showDropDown();
    }

    protected void onNoMatch(){
        editTextTenant.setTextColor(Color.RED);
    }

    protected void updateOptions(String soFar){
        if (soFar==null || soFar.length()<1){
            clearAutoCompleteOptions();
            return;
        }
        while (! results.isEmpty()){
            results.remove(0);
        }

        results= Select.from(Tenant.class).where(Condition.prop("fName").like(soFar+"%")).list();
        results.addAll(Select.from(Tenant.class).where(Condition.prop("oNames").like(soFar+"%")).list());

        if (results.isEmpty()){
            onNoMatch();
            return;
        }

        populateOptions();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fab)){
            addPayment();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String soFar= editable.toString();
        updateOptions(soFar);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        editTextTenant.setTextColor(previousColor);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        chosen= results.get(i);
    }
}
