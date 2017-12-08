package ug.karuhanga.logrealty.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ug.karuhanga.logrealty.Controllers.Controller;
import ug.karuhanga.logrealty.Helper;
import ug.karuhanga.logrealty.Models.House;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.REGEX_EMAIL;
import static ug.karuhanga.logrealty.Helper.makeDate;

public class AddTenant extends AppCompatActivity implements Controller.AddTenantControllerExternalInterface, AdapterView.OnItemClickListener {

    //Activity Visual Components
    @BindView(R.id.edit_text_add_tenant_fname) EditText editTextFirstName;
    @BindView(R.id.edit_text_add_tenant_onames) EditText editTextOtherNames;
    @BindView(R.id.edit_text_add_tenant_email) EditText editTextEmail;
    @BindView(R.id.edit_text_add_tenant_contact) EditText editTextContact;
    @BindView(R.id.edit_text_add_tenant_idtype) EditText editTextIdType;
    @BindView(R.id.edit_text_add_tenant_idno) EditText editTextIdNo;
    @BindView(R.id.edit_text_add_tenant_house) AutoCompleteTextView editTextHouseOccupied;
    @BindView(R.id.date_picker_add_tenant_entering) DatePicker datePickerEntering;
    @BindView(R.id.check_box_add_tenant_use_for_start) CheckBox checkBoxUseEntered;
    @BindView(R.id.date_picker_add_tenant_start_count) DatePicker datePickerCountDate;
    //Text Views group
    EditText[] inputs= {editTextFirstName, editTextOtherNames, editTextEmail, editTextContact, editTextIdType, editTextIdNo, editTextHouseOccupied};

    //Module Controller
    AddTenantActivityExternalInterface controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tenant_activity);
        ButterKnife.bind(this);
        controller= Controller.injectAddTenantActivityExternalInterface(this);
        editTextHouseOccupied.setOnItemClickListener(this);
        editTextHouseOccupied.setThreshold(1);
    }

    @Override
    protected void onResume(){
        editTextHouseOccupied.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, getController().getHouseData()));
        super.onResume();
    }

    public interface AddTenantActivityExternalInterface{
        void onHousePick(House house);
        List<House> getHouseData();
        void submit(String fName, String oNames, String email, String contact, String idType, String idNo, Date entered, Date startCount);
    }

    @Override
    public void raise(String message) {
        editTextHouseOccupied.setError(message);
    }

    @Override
    public void finish(long id, String summary) {
        Intent finisher= new Intent();
        finisher.putExtra("details", summary);
        finisher.putExtra("id", id);
        finish();
    }

    @Override
    public Context requestContext() {
        return this;
    }

    @OnClick(R.id.fab_add_tenant)
    public void attemptSubmit(){
        if (input_is_valid()){
            getController().submit(getData(0), getData(1), getData(2), getData(3), getData(4), getData(5), getDateEntered(), getDateStartCount());
        }
    }

    private AddTenantActivityExternalInterface getController(){
        if (controller == null){
            controller= Controller.injectAddTenantActivityExternalInterface(this);
        }
        return controller;
    }

    private boolean input_is_valid(){
        //Assert not empty
        for (EditText input : inputs) {
            if (empty(input)){
                input.setError(Helper.ERROR_REQUIRED);
                return false;
            }
        }

        //validate email
        if (!editTextEmail.getText().toString().matches(REGEX_EMAIL)){
            editTextEmail.setError("Please input a valid email address");
            return false;
        }

        //validate contact
        String contact= editTextContact.getText().toString();
        if (contact.length()!=10){
            editTextContact.setError("Invalid contact");
            return false;
        }
        try {
            Long.parseLong(contact);
        }catch(Exception e){
            editTextContact.setError("Invalid contact");
            return false;
        }

        //declare success
        return true;
    }

    private String getData(int position){
        String data= "";
        try {
            data= Helper.cleaner(inputs[position].getText().toString());
        }catch (ArrayIndexOutOfBoundsException error){
            Log.d(Helper.TAG_APP_NAME, "Input group error");
        }
        if (data == null){
            data= "";
            Log.d(Helper.TAG_APP_NAME, "Text cleanup failed");
        }
        return data;
    }

    private boolean empty(EditText element){
        return element.getText().toString().length() == 0;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        getController().onHousePick((House) adapterView.getItemAtPosition(i));
        datePickerEntering.requestFocus();
    }

    private Date getDateEntered(){
        return makeDate(datePickerEntering.getDayOfMonth(), datePickerEntering.getMonth(), datePickerEntering.getYear());
    }

    private Date getDateStartCount(){
        if (checkBoxUseEntered.isChecked()){
            return getDateEntered();
        }
        else{
            return makeDate(datePickerCountDate.getDayOfMonth(), datePickerCountDate.getMonth(), datePickerCountDate.getYear());
        }
    }
}
