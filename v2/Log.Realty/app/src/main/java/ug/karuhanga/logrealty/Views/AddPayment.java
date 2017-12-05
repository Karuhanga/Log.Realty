package ug.karuhanga.logrealty.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import ug.karuhanga.logrealty.Controllers.Controller;
import ug.karuhanga.logrealty.Helper;
import ug.karuhanga.logrealty.Models.Tenant;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.APP_TAG;

public class AddPayment extends AppCompatActivity implements Controller.AddPaymentControllerExternalInterface, AdapterView.OnItemClickListener {


    //Activity Visual Components
    @BindView(R.id.edit_text_add_payment_tenant) AutoCompleteTextView editTextTenant;
    @BindView(R.id.check_box_add_payment_single_month) CheckBox checkBoxSingleMonth;
    @BindView(R.id.edit_text_add_payment_amount) EditText editTextAmount;

    //Module Controller
    AddPaymentActivityExternalInterface controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_activity);
        controller= Controller.injectAddPaymentActivityExternalInterface(this);
        editTextTenant.setOnItemClickListener(this);
        editTextTenant.setThreshold(1);
    }

    @Override
    protected void onResume(){
        editTextTenant.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, getController().getTenantData()));
        super.onResume();
    }

    @Override
    public void raise(String message) {
        editTextTenant.setError(message);
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
        getController().onTenantPick((Tenant) adapterView.getItemAtPosition(i));
        editTextAmount.requestFocus();
    }

    public interface AddPaymentActivityExternalInterface{
        List<Tenant> getTenantData();

        void onTenantPick(Tenant itemAtPosition);

        void submit();

        void submit(long amount);
    }

    @OnClick(R.id.fab_add_payment)
    public void attemptSubmit(){
        if (input_is_valid()){
            if (checkBoxSingleMonth.isChecked()){
                getController().submit();
            }
            else{
                getController().submit(getAmount());
            }
        }
    }

    private void hide(final EditText view) {
        view.animate().scaleY(0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.setScaleY(1f);
                view.setVisibility(View.GONE);
            }
        }).start();
    }

    private void show(EditText view) {
        view.setScaleY(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().scaleY(1f);
    }

    @OnCheckedChanged(R.id.check_box_add_payment_single_month)
    public void toggleMonth(CheckBox checkBox){
        if (checkBox.isChecked()){
            hide(editTextAmount);
        }
        else{
            show(editTextAmount);
        }
    }

    private AddPaymentActivityExternalInterface getController(){
        if (controller == null){
            controller= Controller.injectAddPaymentActivityExternalInterface(this);
        }
        return controller;
    }

    private boolean input_is_valid(){
        if (empty(editTextTenant)){
            editTextTenant.setError(Helper.ERROR_REQUIRED);
            return false;
        }

        if (!checkBoxSingleMonth.isChecked()){
            if (empty(editTextAmount)){
                editTextAmount.setError(Helper.ERROR_REQUIRED);
                return false;
            }
        }

        return true;
    }

    private long getAmount(){
        long amount= 0;
        try {
            amount= Long.valueOf(editTextAmount.getText().toString());
        }catch (NumberFormatException e){
            Log.d(APP_TAG, "Long Conversion failed");
        }
        return amount;
    }

    private boolean empty(EditText element){
        return element.getText().toString().length() == 0;
    }
}
