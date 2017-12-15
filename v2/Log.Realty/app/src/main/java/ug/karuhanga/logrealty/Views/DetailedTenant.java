package ug.karuhanga.logrealty.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ug.karuhanga.logrealty.Controllers.Controller;
import ug.karuhanga.logrealty.Models.House;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.ERROR_REQUIRED;
import static ug.karuhanga.logrealty.Helper.REGEX_EMAIL;
import static ug.karuhanga.logrealty.Helper.cleaner;
import static ug.karuhanga.logrealty.Helper.empty;
import static ug.karuhanga.logrealty.Helper.hide;
import static ug.karuhanga.logrealty.Helper.log;
import static ug.karuhanga.logrealty.Helper.makeDate;
import static ug.karuhanga.logrealty.Helper.show;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedTenant.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailedTenant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedTenant extends Fragment implements AdapterView.OnItemClickListener, Controller.DetailedTenantControllerExternalInterface {

    @BindView(R.id.row_detailed_tenant_onames) View rowONames;
    @BindView(R.id.text_view_detailed_tenant_label_fname) TextView textViewLabelFName;
    
    @BindView(R.id.button_detailed_tenant_name) ImageButton buttonName;
    @BindView(R.id.button_detailed_tenant_email) ImageButton buttonEmail;
    @BindView(R.id.button_detailed_tenant_contact) ImageButton buttonContact;
    @BindView(R.id.button_detailed_tenant_idtype) ImageButton buttonIdType;
    @BindView(R.id.button_detailed_tenant_id_no) ImageButton buttonIDNo;
    @BindView(R.id.button_detailed_tenant_house) ImageButton buttonHouse;
    @BindView(R.id.button_detailed_tenant_date_entered) ImageButton buttonDateEntered;
    @BindView(R.id.button_detailed_tenant_date_due) ImageButton buttonDateDue;

    @BindView(R.id.text_view_detailed_tenant_fname) TextView textViewName;
    @BindView(R.id.text_view_detailed_tenant_email) TextView textViewEmail;
    @BindView(R.id.text_view_detailed_tenant_contact) TextView textViewContact;
    @BindView(R.id.text_view_detailed_tenant_idtype) TextView textViewIdType;
    @BindView(R.id.text_view_detailed_tenant_id_no) TextView textViewIdNo;
    @BindView(R.id.text_view_detailed_tenant_house) TextView textViewHouse;
    @BindView(R.id.text_view_detailed_tenant_date_entered) TextView textViewDateEntered;
    @BindView(R.id.text_view_detailed_tenant_date_due) TextView textViewDateDue;

    @BindView(R.id.edit_text_detailed_tenant_fname) EditText editTextFName;
    @BindView(R.id.edit_text_detailed_tenant_surname) EditText editTextSurname;
    @BindView(R.id.edit_text_detailed_tenant_email) EditText editTextEmail;
    @BindView(R.id.edit_text_detailed_tenant_contact) EditText editTextContact;
    @BindView(R.id.edit_text_detailed_tenant_idtype) EditText editTextIdType;
    @BindView(R.id.edit_text_detailed_tenant_id_no) EditText editTextIdNo;
    @BindView(R.id.edit_text_detailed_tenant_house) AutoCompleteTextView editTextHouse;
    @BindView(R.id.date_picker_detailed_tenant_date_entered) DatePicker datePickerDateEntered;
    @BindView(R.id.date_picker_detailed_tenant_date_due) DatePicker datePickerDateDue;

    @BindView(R.id.fab_detailed_tenant_edit) FloatingActionButton fabEdit;

    private final int EDIT= R.drawable.ic_edit_black_24dp;
    private final int DONE= R.drawable.ic_check_black_24dp;
    private final int CLOSE= R.drawable.ic_close_black_24dp;

    private final String NAME= "name";
    private final String EMAIL= "email";
    private final String CONTACT= "contact";
    private final String IDTYPE= "idtype";
    private final String IDNO= "idno";
    private final String HOUSE= "house";
    private final String ENTERED= "entered";
    private final String DUE= "due";

    private HashMap<String, Boolean> editing = new HashMap<>();

    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;
    private DetailedTenantActivityExternalInterface controller;

    public DetailedTenant() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailedTenant.
     */
    public static DetailedTenant newInstance(Long id) {
        DetailedTenant fragment = new DetailedTenant();
        Bundle args = new Bundle();

        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getController().setTenant(getArguments().getLong("id"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.detailed_tenant_fragment, container, false);
        unbinder= ButterKnife.bind(this, view);

        refreshViews();

        ArrayAdapter<House> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, getController().getHouses());
        adapter.setNotifyOnChange(true);
        editTextHouse.setAdapter(adapter);
        editTextHouse.setThreshold(1);
        editTextHouse.setOnItemClickListener(this);

        return view;
    }

    /**
     * Major Process Starters
     */

    private void commenceEdit() {
        if (allEditsComplete()){
            return;
        }

        //Name
        if (editing.get(NAME)){
            if (empty(editTextFName)){
                editTextFName.setError(ERROR_REQUIRED);
                return;
            }

            if (empty(editTextSurname)){
                editTextSurname.setError(ERROR_REQUIRED);
                return;
            }

            getController().editName(getFirstName(), getSurname());
        }

        //Email
        if (editing.get(EMAIL)){
            if (!email_is_valid()){
                return;
            }
            getController().editEmail(getEmail());
        }

        //Contact
        if (editing.get(CONTACT)){
            if (!contact_is_valid()){
                return;
            }
            getController().editContact(getContact());
        }

        //IdType
        if (editing.get(IDTYPE)){
            if (empty(editTextIdType)){
                editTextIdType.setError(ERROR_REQUIRED);
                return;
            }
            getController().editIdType(getIdType());
        }

        //IdNo.
        if (editing.get(IDNO)){
            if (empty(editTextIdNo)){
                editTextIdNo.setError(ERROR_REQUIRED);
                return;
            }
            getController().editIdNo(getIdNo());
        }

        if (editing.get("house")){
            getController().editHouse();
        }

        //Date Entered
        if (editing.get("entered")){
            getController().editDateEntered(getDateEntered());
        }

        if (editing.get("due")){
            getController().editDateDue(getDateDue());
        }
    }

    private void deleteTenant() {
        getController().deleteTenant();
    }

    /**
     * UI Updaters
     */

    private void refreshViews(){
        hide(rowONames);
        textViewName.setText(getController().getName());
        textViewEmail.setText(getController().getEmail());
        textViewContact.setText(getController().getContact());
        textViewIdType.setText(getController().getIdType());
        textViewIdNo.setText(getController().getIdNo());
        textViewHouse.setText(getController().getHouse());
        textViewDateEntered.setText(getController().getDateEntered());
        textViewDateDue.setText(getController().getDateDue());

        editTextFName.setText(getController().getFName());
        editTextSurname.setText(getController().getSurname());
        editTextEmail.setText(getController().getEmail());
        editTextContact.setText(getController().getContact());
        editTextIdType.setText(getController().getIdType());
        editTextIdNo.setText(getController().getIdNo());
        editTextHouse.setText(getController().getHouse());
        ArrayList<Integer> date= getController().getDatePickerDateEntered();
        datePickerDateEntered.updateDate(date.get(2), date.get(1), date.get(0));
        date= getController().getDatePickerDateDue();
        datePickerDateDue.updateDate(date.get(2), date.get(1), date.get(0));

        editing.put(NAME, false);
        editing.put(EMAIL, false);
        editing.put(CONTACT, false);
        editing.put(IDTYPE, false);
        editing.put(IDNO, false);
        editing.put(HOUSE, false);
        editing.put(ENTERED, false);
        editing.put(DUE, false);
    }

    private void onEditActions(ImageButton button, String editKey, EditText editText, TextView textView, String defaultText){
        if (editing.get(editKey)){
            hide(editText);
            show(textView);
            editText.setText(defaultText);
            button.setImageResource(EDIT);
            editing.put(editKey, false);
            if (allEditsComplete()){
                fabEdit.setImageResource(CLOSE);
            }
            return;
        }
        hide(textView);
        show(editText);
        button.setImageResource(CLOSE);
        editing.put(editKey, true);
        if (!allEditsComplete()){
            fabEdit.setImageResource(DONE);
        }
    }

    private void onEditSurname(){
        if (editing.get(NAME)){
            hide(rowONames);
            editTextSurname.setText(getController().getSurname());
            textViewLabelFName.setText(getString(R.string.name));
            return;
        }
        show(rowONames);
        textViewLabelFName.setText(getString(R.string.first_name));
    }

    /**
     * UI Interactions
     */

    @OnClick(R.id.fab_detailed_tenant_edit)
    public void onEditFabClicked(){
        //if not editing
        if (allEditsComplete()){
            //if buttons visible
            if (buttonName.getVisibility()==View.VISIBLE){
                fabEdit.setImageResource(EDIT);
                hide(buttonName);
                hide(buttonContact);
                hide(buttonEmail);
                hide(buttonHouse);
                hide(buttonIdType);
                hide(buttonIDNo);
                hide(buttonDateDue);
                hide(buttonDateEntered);
            }
            else{
                fabEdit.setImageResource(CLOSE);
                show(buttonName);
                show(buttonContact);
                show(buttonEmail);
                show(buttonHouse);
                show(buttonIdType);
                show(buttonIDNo);
                show(buttonDateDue);
                show(buttonDateEntered);
            }
            return;
        }
        commenceEdit();
    }

    @OnClick(R.id.fab_detailed_tenant_delete)
    public void onDeleteFabClicked(){
        deleteTenant();
    }

    @OnClick(R.id.button_detailed_tenant_name)
    public void onNameButtonClicked(){
        onEditSurname();
        onEditActions(buttonName, NAME, editTextFName, textViewName, getController().getFName());
    }

    @OnClick(R.id.button_detailed_tenant_email)
    public void onEmailButtonClicked(){
        onEditActions(buttonEmail, EMAIL, editTextEmail, textViewEmail, getController().getEmail());
    }

    @OnClick(R.id.button_detailed_tenant_contact)
    public void onContactButtonClicked(){
        onEditActions(buttonContact, CONTACT, editTextContact, textViewContact, getController().getContact());
    }

    @OnClick(R.id.button_detailed_tenant_idtype)
    public void onIdTypeButtonClicked(){
        onEditActions(buttonIdType, IDTYPE, editTextIdType, textViewIdType, getController().getIdType());
    }

    @OnClick(R.id.button_detailed_tenant_id_no)
    public void onIdNoButtonClicked(){
        onEditActions(buttonIDNo, IDNO, editTextIdNo, textViewIdNo, getController().getIdNo());
    }

    @OnClick(R.id.button_detailed_tenant_house)
    public void onHouseButtonClicked(){
        onEditActions(buttonHouse, HOUSE, editTextHouse, textViewHouse, getController().getHouse());
    }

    @OnClick(R.id.button_detailed_tenant_date_entered)
    public void onDateEnteredButtonClicked(){
        if (editing.get("entered")){
            hide(datePickerDateEntered);
            show(textViewDateEntered);
            ArrayList<Integer> date= getController().getDatePickerDateEntered();
            datePickerDateEntered.updateDate(date.get(2), date.get(1), date.get(0));
            buttonDateEntered.setImageResource(EDIT);
            editing.put("entered", false);
            if (allEditsComplete()){
                fabEdit.setImageResource(CLOSE);
            }
            return;
        }
        hide(textViewDateEntered);
        show(datePickerDateEntered);
        buttonDateEntered.setImageResource(CLOSE);
        editing.put("entered", true);
        if (!allEditsComplete()){
            fabEdit.setImageResource(DONE);
        }
    }

    @OnClick(R.id.button_detailed_tenant_date_due)
    public void onDateDueButtonClicked(){
        if (editing.get("due")){
            hide(datePickerDateDue);
            show(textViewDateDue);
            ArrayList<Integer> date= getController().getDatePickerDateDue();
            datePickerDateDue.updateDate(date.get(2), date.get(1), date.get(0));
            buttonDateDue.setImageResource(EDIT);
            editing.put("due", false);
            if (allEditsComplete()){
                fabEdit.setImageResource(CLOSE);
            }
            return;
        }
        hide(textViewDateDue);
        show(datePickerDateDue);
        buttonDateDue.setImageResource(CLOSE);
        editing.put("due", true);
        if (!allEditsComplete()){
            fabEdit.setImageResource(DONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        getController().setNewHouse((House) adapterView.getItemAtPosition(i));
    }

    /**
     * Post the fact methods
     */
    private void onAllEditsDone() {
        if (allEditsComplete()){
            onEditFabClicked();
        }
        Toast.makeText(requestContext(), "Completed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTenantDeleted(long id) {
        if (mListener != null) {
            mListener.onItemDeleted(id);
        }
    }

    @Override
    public void onHouseEditComplete() {
        textViewHouse.setText(getController().getHouse());
        onHouseButtonClicked();
        onAllEditsDone();
    }

    @Override
    public void onEditContactComplete() {
        textViewContact.setText(getController().getContact());
        onContactButtonClicked();
        onAllEditsDone();
    }

    @Override
    public void onEditIdTypeComplete() {
        textViewIdType.setText(getController().getIdType());
        onIdTypeButtonClicked();
        onAllEditsDone();
    }

    @Override
    public void onEditEmailComplete() {
        textViewEmail.setText(getController().getEmail());
        onEmailButtonClicked();
        onAllEditsDone();
    }

    @Override
    public void onEditNameComplete() {
        textViewName.setText(getController().getName());
        onNameButtonClicked();
        onAllEditsDone();
    }

    @Override
    public void onEditIdNoComplete() {
        textViewIdNo.setText(getController().getIdNo());
        onIdNoButtonClicked();
        onAllEditsDone();
    }

    @Override
    public void onEditEnteredComplete() {
        textViewDateEntered.setText(getController().getDateEntered());
        onDateEnteredButtonClicked();
        onAllEditsDone();
    }

    @Override
    public void onEditDueComplete() {
        textViewDateDue.setText(getController().getDateDue());
        onDateDueButtonClicked();
        onAllEditsDone();
    }

    private boolean allEditsComplete() {
        return  !editing.get(NAME) &&
                !editing.get(EMAIL) &&
                !editing.get(CONTACT) &&
                !editing.get(IDTYPE) &&
                !editing.get(IDNO) &&
                !editing.get(HOUSE) &&
                !editing.get(ENTERED) &&
                !editing.get(DUE);
    }


    /**
     * Lifecycle methods
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * Getters and Setters
     */
    public void setController() {
        this.controller = Controller.injectDetailedTenantActivityExternalInterface(this);
    }

    public String getEmail() {
        return editTextEmail.getText().toString();
    }

    private String getFirstName() {
        String name= editTextFName.getText().toString();
        name= cleaner(name);
        if (name==null){
            log("Detailed Tenant View: Problem Cleaning Text");
        }
        return name;
    }

    private String getSurname() {
        String name= editTextSurname.getText().toString();
        name= cleaner(name);
        if (name==null){
            log("Detailed Tenant View: Problem Cleaning Text");
        }
        return name;
    }

    private String getContact() {
        return editTextContact.getText().toString();
    }

    private String getIdType() {
        String idtype = editTextIdType.getText().toString();
        idtype = cleaner(idtype);
        if (idtype == null) {
            log("Detailed Tenant: Problem cleaning idType");
        }
        return idtype;
    }

    private Date getDateEntered() {
        Date entered = makeDate(datePickerDateEntered.getDayOfMonth(), datePickerDateEntered.getMonth(), datePickerDateEntered.getYear());
        if (entered == null) {
            log("Detailed Tenant: Problem making date entered");
        }
        return entered;
    }

    private Date getDateDue() {
        Date due= makeDate(datePickerDateDue.getDayOfMonth(), datePickerDateDue.getMonth(), datePickerDateDue.getYear());
        if (due==null){
            log("Detailed Tenant: Problem making date due");
        }
        return due;
    }

    private String getIdNo() {
        String idno= editTextIdNo.getText().toString();
        idno= cleaner(idno);
        if (idno==null){
            log("Detailed Tenant View: Failed to clean IdNo.");
        }
        return idno;
    }

    /**
     * Validators
     */

    private boolean email_is_valid(){
        if (empty(editTextEmail)){
            editTextEmail.setError(ERROR_REQUIRED);
            return false;
        }

        String email= editTextEmail.getText().toString();
        if (!email.matches(REGEX_EMAIL)){
            editTextEmail.setError("Invalid Email Address");
            return false;
        }
        return true;
    }

    private boolean contact_is_valid(){
        if (empty(editTextContact)){
            editTextContact.setError(ERROR_REQUIRED);
            return false;
        }

        String contact= getContact();

        if (contact.length() != 10) {
            editTextContact.setError("Invalid Contact");
            return false;
        }

        try {
            Long.parseLong(contact);
        } catch (Exception e) {
            editTextContact.setError("Invalid Contact");
            return false;
        }
        return true;
    }


    /**
     * Error Notifiers
     */
    @Override
    public void complainAboutHouse(String message) {
        editTextHouse.setError(message);
    }

    @Override
    public void complainAboutRentDue(String message) {
        Toast.makeText(requestContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void complainAboutFName(String message) {
        editTextFName.setError(message);
    }

    @Override
    public void complainAboutSurname(String message) {
        editTextSurname.setError(message);
    }

    @Override
    public void complainAboutEmail(String message) {
        editTextEmail.setError(message);
    }

    @Override
    public void complainAboutContact(String message) {
        editTextContact.setError(message);
    }

    @Override
    public void complainAboutIdType(String message) {
        editTextIdType.setError(message);
    }

    @Override
    public void complainAboutIdNo(String message) {
        editTextIdNo.setError(message);
    }


    /**
     * External Interaction Methods Interfaces
     */

    interface OnFragmentInteractionListener {
        void onItemDeleted(Long id);

        Context requestContext();
    }

    public interface DetailedTenantActivityExternalInterface{

        void setTenant(long id);

        List<House> getHouses();

        String getFName();

        String getSurname();

        String getEmail();

        String getContact();

        String getIdType();

        String getIdNo();

        String getHouse();

        void deleteTenant();

        void editName(String firstName, String surname);

        void editEmail(String email);

        void editContact(String contact);

        void editIdType(String idType);

        void editIdNo(String idNo);

        void editDateDue(Date dateDue);

        void editDateEntered(Date dateEntered);

        void editHouse();

        void setNewHouse(House newHouse);

        String getDateEntered();

        String getDateDue();

        ArrayList<Integer> getDatePickerDateEntered();

        ArrayList<Integer> getDatePickerDateDue();

        String getName();
    }

    public DetailedTenantActivityExternalInterface getController() {
        if (this.controller== null){
            setController();
        }

        return this.controller;
    }

    @Override
    public Context requestContext() {
        if (mListener==null){
            return null;
        }
        return mListener.requestContext();
    }
}
