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

import java.util.Calendar;
import java.util.List;

import ug.karuhanga.logrealty.Data.Payment;
import ug.karuhanga.logrealty.Data.Tenant;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helpers.FALSE;

public class AddPayment extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    FloatingActionButton fab;
    EditText editTextAmount;
    AutoCompleteTextView editTextTenant;
    ArrayAdapter<Tenant> adapter;
    List<Tenant> results;
    Tenant chosen;
    private CheckBox checkBoxSingleMonth;
    private int previousColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab_add_payment);
        editTextTenant= (AutoCompleteTextView) findViewById(R.id.edit_text_add_payment_tenant);
        editTextAmount= (EditText) findViewById(R.id.edit_text_add_payment_amount);
        checkBoxSingleMonth= (CheckBox) findViewById(R.id.check_box_add_payment_single_month);
        previousColor= editTextTenant.getCurrentTextColor();
        chosen= null;
        adapter= null;

        editTextTenant.setOnItemClickListener(this);
        editTextTenant.setThreshold(1);
        editTextTenant.addTextChangedListener(this);

        results= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq(FALSE)).list();
        adapter= new ArrayAdapter<>(this, R.layout.list_item_dropdown, R.id.text_view_list_item_drop_down, results);
        adapter.setNotifyOnChange(true);
        editTextTenant.setAdapter(adapter);

        fab.setOnClickListener(this);
        checkBoxSingleMonth.setOnCheckedChangeListener(this);
    }

    private void addPayment(){
        Payment payment;
        int amount= 0;

        if (!checkBoxSingleMonth.isChecked()){
            try{
                amount= Integer.valueOf(editTextAmount.getText().toString());
            }catch (NumberFormatException e){
                onPaymentUnsuccessful("Amount Error");
                return;
            }
        }

        if (chosen==null){
            onNoMatch();
            Toast.makeText(this, "Please pick a Tenant", Toast.LENGTH_SHORT).show();
            return;
        }

        //TODO Add Few Amounts Checking

        if (checkBoxSingleMonth.isChecked()){
            payment= new Payment(Calendar.getInstance().getTime(), chosen, 1);
        }
        else{
            payment= new Payment(Calendar.getInstance().getTime(), amount, chosen);
        }
        if (payment.onNewPaymentAdded(this)){
            payment.save();
        }
        else{
            onPaymentUnsuccessful("Payment Unsuccessful");
            return;
        }
        Intent finisher= new Intent();
        finisher.putExtra("details", payment.toString());
        setResult(RESULT_OK, finisher);
        finish();
    }

    protected void onPaymentUnsuccessful(String notif){
        Toast.makeText(this, notif, Toast.LENGTH_SHORT).show();
    }

    protected void onNoMatch(){
        editTextTenant.setTextColor(Color.RED);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fab)){
            addPayment();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        chosen= (Tenant) adapterView.getItemAtPosition(i);
        editTextAmount.requestFocus();
        editTextTenant.setText(chosen.getfName()+" "+chosen.getoNames());
        //TODO Move to next and indicate something was chosen
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        editTextTenant.setTextColor(previousColor);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton==checkBoxSingleMonth){
            if (b){
                hide(editTextAmount);
            }
            else{
                show(editTextAmount);
            }
        }
    }
}
