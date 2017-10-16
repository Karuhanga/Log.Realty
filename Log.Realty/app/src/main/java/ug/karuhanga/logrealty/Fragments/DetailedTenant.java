package ug.karuhanga.logrealty.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Tenant;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.Popups.Confirmation;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helpers.REQUEST_CODE_EDIT;
import static ug.karuhanga.logrealty.Helpers.REQUEST_CODE_REPLACE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedTenant.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailedTenant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedTenant extends Fragment implements TextWatcher, View.OnClickListener, ug.karuhanga.logrealty.Listeners.Confirmation, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match

    private Long tenant;
    
    private ImageButton buttonFname;
    private ImageButton buttonSurname;
    private ImageButton buttonEmail;
    private ImageButton buttonContact;
    private ImageButton buttonIdType;
    private ImageButton buttonIDNo;
    private ImageButton buttonHouse;
    private ImageButton buttonDateEntered;
    private ImageButton buttonDateDue;

    private TextView textViewFname;
    private TextView textViewONames;
    private View rowONames;
    private TextView textViewEmail;
    private TextView textViewContact;
    private TextView textViewIdType;
    private TextView textViewIdNo;
    private TextView textViewHouse;
    private TextView textViewDateEntered;
    private TextView textViewDateDue;


    private EditText editTextFname;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private EditText editTextContact;
    private EditText editTextIdType;
    private EditText editTextIdNo;
    private AutoCompleteTextView editTextHouse;
    private DatePicker datePickerDateEntered;
    private DatePicker datePickerDateDue;

    private EditText colored;

    private FloatingActionButton fab;
    private FloatingActionButton fab_delete;

    private int previous_color;
    private int editCount;
    private Tenant tenantObject;
    private List<House> results;
    private ArrayAdapter<House> adapter;
    private House chosen;
    List<Tenant> current;
    boolean house_confirmed;
    boolean due_confirmed;

    private HashMap<String, Boolean> editting= new HashMap<>();

    private OnFragmentInteractionListener mListener;

    public DetailedTenant() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailedTenant.
     */
    // TODO: Rename and change types and number of parameters
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
            tenant = getArguments().getLong("id");
        }
        else{
            List<Tenant> results= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq(0)).list();
            if (results.size()>0){
                tenant= results.get(0).getId();
            }
            else{
                return;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        editting.put("fname", false);
        editting.put("surname", false);
        editting.put("email", false);
        editting.put("contact", false);
        editting.put("idtype", false);
        editting.put("idno", false);
        editting.put("house", false);
        editting.put("entered", false);
        editting.put("due", false);

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.detailed_tenant_fragment, container, false);
        
        buttonFname= view.findViewById(R.id.button_details_tenant_fname);
        buttonSurname= view.findViewById(R.id.button_details_tenant_onames);
        buttonEmail= view.findViewById(R.id.button_details_tenant_email);
        buttonContact= view.findViewById(R.id.button_details_tenant_contact);
        buttonIdType= view.findViewById(R.id.button_details_tenant_idtype);
        buttonIDNo= view.findViewById(R.id.button_details_tenant_id_no);
        buttonHouse= view.findViewById(R.id.button_details_tenant_house);
        buttonDateEntered= view.findViewById(R.id.button_details_tenant_date_entering);
        buttonDateDue= view.findViewById(R.id.button_due_details_tenant);

        textViewFname= view.findViewById(R.id.textView_details_tenant_fname);
        textViewONames= view.findViewById(R.id.textView_details_tenant_onames);
        rowONames= view.findViewById(R.id.row_details_tenant_onames);
        textViewEmail= view.findViewById(R.id.textView_details_tenant_email);
        textViewContact= view.findViewById(R.id.textView_details_tenant_contact);
        textViewIdType= view.findViewById(R.id.textView_details_tenant_idtype);
        textViewIdNo= view.findViewById(R.id.textView_details_tenant_id_no);
        textViewHouse= view.findViewById(R.id.textView_details_tenant_house);
        textViewDateEntered= view.findViewById(R.id.textView_details_tenant_entering);
        textViewDateDue= view.findViewById(R.id.textView_details_tenant_due);

        editTextFname= view.findViewById(R.id.edit_text_details_tenant_fname);
        editTextSurname= view.findViewById(R.id.edit_text_details_tenant_onames);
        editTextEmail= view.findViewById(R.id.edit_text_details_tenant_email);
        editTextContact= view.findViewById(R.id.edit_text_details_tenant_contact);
        editTextIdType= view.findViewById(R.id.edit_text_details_tenant_idtype);
        editTextIdNo= view.findViewById(R.id.edit_text_details_tenant_id_no);
        editTextHouse= view.findViewById(R.id.edit_text_details_tenant_house);
        datePickerDateEntered= view.findViewById(R.id.date_picker_details_tenant_entering);
        datePickerDateDue= view.findViewById(R.id.date_picker_due_details_tenant);

        fab= view.findViewById(R.id.fab_detailed_tenant);
        fab_delete= view.findViewById(R.id.fab_delete_detailed_tenant);

        previous_color= editTextFname.getCurrentTextColor();
        colored= editTextFname;
        editCount= 0;
        tenantObject= Tenant.findById(Tenant.class, tenant);
        house_confirmed= false;
        due_confirmed= false;

        textViewFname.setText(tenantObject.getName());
        textViewONames.setText(tenantObject.getoNames());
        hide(rowONames);
        ((TextView) view.findViewById(R.id.label_details_tenant_fname)).setText("Name");
        textViewEmail.setText(tenantObject.getEmail());
        textViewContact.setText(tenantObject.getContact());
        textViewIdType.setText(tenantObject.getIdType());
        textViewIdNo.setText(tenantObject.getIdNo());
        textViewHouse.setText(tenantObject.getHouse().toString());
        textViewDateEntered.setText(Helpers.dateToString(tenantObject.getEntered()));
        textViewDateDue.setText(Helpers.dateToString(tenantObject.getRentDue()));

        editTextFname.setText(tenantObject.getfName());
        editTextSurname.setText(tenantObject.getoNames());
        editTextEmail.setText(tenantObject.getEmail());
        editTextContact.setText(tenantObject.getContact());
        editTextIdType.setText(tenantObject.getIdType());
        editTextIdNo.setText(tenantObject.getIdNo());
        editTextHouse.setText(tenantObject.getHouse().toString());
        ArrayList<Integer> date= Helpers.breakDate(tenantObject.getEntered());
        datePickerDateEntered.updateDate(date.get(2), date.get(1), date.get(0));
        date= Helpers.breakDate(tenantObject.getRentDue());
        datePickerDateDue.updateDate(date.get(2), date.get(1), date.get(0));

        buttonFname.setOnClickListener(this);
        buttonSurname.setOnClickListener(this);
        buttonEmail.setOnClickListener(this);
        buttonContact.setOnClickListener(this);
        buttonIdType.setOnClickListener(this);
        buttonIDNo.setOnClickListener(this);
        buttonHouse.setOnClickListener(this);
        buttonDateEntered.setOnClickListener(this);
        buttonDateDue.setOnClickListener(this);
        fab.setOnClickListener(this);
        fab_delete.setOnClickListener(this);

        editTextFname.addTextChangedListener(this);
        editTextSurname.addTextChangedListener(this);
        editTextEmail.addTextChangedListener(this);
        editTextContact.addTextChangedListener(this);
        editTextIdType.addTextChangedListener(this);
        editTextIdNo.addTextChangedListener(this);
        editTextHouse.addTextChangedListener(this);

        results= Select.from(House.class).list();
        adapter= new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, results);
        adapter.setNotifyOnChange(true);
        editTextHouse.setAdapter(adapter);
        editTextHouse.setThreshold(1);
        editTextHouse.setOnItemClickListener(this);
        chosen= null;

        return view;
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
    public void onClick(View view) {
        if (view==fab){
            //if not editting
            if (editCount==0){
                //if buttons visible
                if (buttonFname.getVisibility()==View.VISIBLE){
                    fab.setImageResource(R.drawable.icon_edit);
                    hide(buttonFname);
                    hide(rowONames);
                    hide(buttonContact);
                    hide(buttonEmail);
                    hide(buttonHouse);
                    hide(buttonIdType);
                    hide(buttonIDNo);
                    hide(buttonDateDue);
                    hide(buttonDateEntered);
                    hide(rowONames);
                    textViewFname.setText(tenantObject.getName());
                    ((TextView) getView().findViewById(R.id.label_details_tenant_fname)).setText("Name");
                }
                else{
                    fab.setImageResource(R.drawable.ic_close_black_24dp);
                    show(buttonFname);
                    show(rowONames);
                    show(buttonContact);
                    show(buttonEmail);
                    show(buttonHouse);
                    show(buttonIdType);
                    show(buttonIDNo);
                    show(buttonDateDue);
                    show(buttonDateEntered);
                    textViewFname.setText(tenantObject.getfName());
                    ((TextView) getView().findViewById(R.id.label_details_tenant_fname)).setText("First Name");
                }
                return;
            }
            completeEdit();
            return;
        }
        if (view==buttonFname){
            onEditActions(buttonFname, "fname", editTextFname, textViewFname, tenantObject.getfName());
            return;
        }

        if (view==buttonSurname){
            onEditActions(buttonSurname, "surname", editTextSurname, textViewONames, tenantObject.getoNames());
            return;
        }

        if (view==buttonIdType){
            onEditActions(buttonIdType, "idtype", editTextIdType, textViewIdType, tenantObject.getIdType());
            return;
        }

        if (view==buttonIDNo){
            onEditActions(buttonIDNo, "idno", editTextIdNo, textViewIdNo, tenantObject.getIdNo());
            return;
        }

        if (view==buttonHouse){
            onEditActions(buttonHouse, "house", editTextHouse, textViewHouse, tenantObject.getHouse().toString());
            return;
        }

        if (view==buttonEmail){
            onEditActions(buttonEmail, "email", editTextEmail, textViewEmail, tenantObject.getEmail());
            return;
        }

        if (view==buttonContact){
            onEditActions(buttonContact, "contact", editTextContact, textViewContact, tenantObject.getContact());
            return;
        }

        if (view==buttonFname){
            onEditActions(buttonFname, "fname", editTextFname, textViewFname, tenantObject.getfName());
            return;
        }

        if (view==buttonDateEntered){
            if (editting.get("entered")){
                editCount--;
                hide(datePickerDateEntered);
                show(textViewDateEntered);
                ArrayList<Integer> date= Helpers.breakDate(tenantObject.getEntered());
                datePickerDateEntered.updateDate(date.get(2), date.get(1), date.get(0));
                buttonDateEntered.setImageResource(R.drawable.icon_edit);
                editting.remove("entered");
                editting.put("entered", false);
                if (editCount==0){
                    fab.setImageResource(R.drawable.ic_close_black_24dp);
                }
                return;
            }
            editCount++;
            if (editCount==1){
                fab.setImageResource(R.drawable.ic_check_black_24dp);
            }
            hide(textViewDateEntered);
            show(datePickerDateEntered);
            buttonDateEntered.setImageResource(R.drawable.ic_close_black_24dp);
            editting.remove("entered");
            editting.put("entered", true);
            return;
        }

        if (view==buttonDateDue){
            if (editting.get("due")){
                editCount--;
                hide(datePickerDateDue);
                show(textViewDateDue);
                ArrayList<Integer> date= Helpers.breakDate(tenantObject.getRentDue());
                datePickerDateDue.updateDate(date.get(2), date.get(1), date.get(0));
                buttonDateDue.setImageResource(R.drawable.icon_edit);
                editting.remove("due");
                editting.put("due", false);
                if (editCount==0){
                    fab.setImageResource(R.drawable.ic_close_black_24dp);
                }
                return;
            }
            editCount++;
            if (editCount==1){
                fab.setImageResource(R.drawable.ic_check_black_24dp);
            }
            hide(textViewDateDue);
            show(datePickerDateDue);
            buttonDateDue.setImageResource(R.drawable.ic_close_black_24dp);
            editting.remove("due");
            editting.put("due", true);
            return;
        }

        if (view==fab_delete){
            deleteTenant();
        }
    }

    private void deleteTenant() {
        new Confirmation(getContext(), this, "Are you sure?", "Delete this tenant and all related payments:\n"+tenantObject.getName()+"\n"+tenantObject.getHouse().toString(), R.drawable.ic_delete_black_24dp, "Yes", "Cancel", Helpers.REQUEST_CODE_DELETE).show();
    }

    private void completeEdit() {
        Tenant temp= Tenant.findById(Tenant.class, tenantObject.getId());

        if (editCount==0){
            return;
        }
        if (editting.get("fname")){
            String name= editTextFname.getText().toString();
            name= Helpers.cleaner(name);
            if (name==null){
                onError("Please input the Tenant's first name", editTextFname);
                tenantObject= temp;
                return;
            }
            tenantObject.setfName(name);
        }

        if (editting.get("surname")){
            String name= editTextSurname.getText().toString();
            name= Helpers.cleaner(name);
            if (name==null){
                onError("Please input the Tenant's surname", editTextFname);
                tenantObject= temp;
                return;
            }
            tenantObject.setoNames(name);
        }

        if (editting.get("email")){
            String email= editTextEmail.getText().toString();
            if (!email.matches(Helpers.REGEX_EMAIL)){
                onError("Please provide a valid email address", editTextEmail);
                tenantObject= temp;
                return;
            }
            tenantObject.setEmail(email);
        }

        if (editting.get("contact")){
            String contact= editTextContact.getText().toString();
            if (contact.length()!=10){
                onError("Please input a valid contact", editTextContact);
                tenantObject= temp;
                return;
            }
            try {
                Long.parseLong(contact);
            }catch(Exception e){
                onError("Please input a valid contact", editTextContact);
                return;
            }
            tenantObject.setContact(contact);
        }

        if (editting.get("idtype")){
            String idtype= editTextIdType.getText().toString();
            idtype= Helpers.cleaner(idtype);
            if (idtype==null){
                onError("Please input the type of ID this Tenant presented", editTextIdType);
                tenantObject= temp;
                return;
            }
            tenantObject.setIdType(idtype);
        }

        if (editting.get("idno")){
            String idno= editTextIdNo.getText().toString();
            idno= Helpers.cleaner(idno);
            if (idno==null){
                onError("Please input this Tenant's ID number", editTextIdNo);
                tenantObject= temp;
                return;
            }
            tenantObject.setIdNo(idno);
        }

        if (editting.get("entered")){
            Date entered= Helpers.makeDate(datePickerDateEntered.getDayOfMonth(), datePickerDateEntered.getMonth(), datePickerDateEntered.getYear());
            if (entered==null){
                Toast.makeText(getContext(), "Please input an entry date", Toast.LENGTH_SHORT).show();
                tenantObject= temp;
                return;
            }
            tenantObject.setEntered(entered);
        }

        if (editting.get("due")){
            Date due= Helpers.makeDate(datePickerDateDue.getDayOfMonth(), datePickerDateDue.getMonth(), datePickerDateDue.getYear());
            if (due==null){
                Toast.makeText(getContext(), "Please pick a new due date", Toast.LENGTH_SHORT).show();
                tenantObject= temp;
                return;
            }
            if (!due_confirmed){
                new ug.karuhanga.logrealty.Popups.Confirmation(getContext(), this, "Are you sure?", "Change this Tenant's due date:\n"+tenantObject.getName()+"\n"+tenantObject.getHouse().toString(), R.drawable.ic_edit_black_24dp, "Yes", "No, leave as is", REQUEST_CODE_EDIT).show();
                return;
            }
            tenantObject.setRentDue(due);
        }

        if (editting.get("house")){
            if (chosen==null) {
                onError("Please pick a house for this tenant", editTextFname);
                tenantObject = temp;
                return;
            }
            if (!house_confirmed){
                current= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("house")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq("0")).list();
                if (current.size()>0){
                    new ug.karuhanga.logrealty.Popups.Confirmation(getContext(), this, "Are you sure?", "Replace:\n"+current.get(0).getName()+" in "+current.get(0).getHouse().getLocation().getName(), R.drawable.ic_edit_black_24dp, "Yes", "Pick a different House", REQUEST_CODE_REPLACE).show();
                    return;
                }
            }
            tenantObject.setHouse(chosen);
            current.get(0).setEx(true);
            current.get(0).save();
        }

        if (editting.get("house")){
            house_confirmed= false;
            textViewHouse.setText(tenantObject.getHouse().toString());
            onClick(buttonHouse);
        }

        if (editting.get("fname")){
            textViewFname.setText(tenantObject.getfName());
            onClick(buttonFname);
        }

        if (editting.get("surname")){
            textViewONames.setText(tenantObject.getoNames());
            onClick(buttonSurname);
        }

        if (editting.get("email")){
            textViewEmail.setText(tenantObject.getEmail());
            onClick(buttonEmail);
        }

        if (editting.get("contact")){
            textViewContact.setText(tenantObject.getContact());
            onClick(buttonContact);
        }

        if (editting.get("idtype")){
            textViewIdType.setText(tenantObject.getIdType());
            onClick(buttonIdType);
        }

        if (editting.get("idno")){
            textViewIdNo.setText(tenantObject.getIdNo());
            onClick(buttonIDNo);
        }

        if (editting.get("due")){
            textViewDateDue.setText(Helpers.dateToString(tenantObject.getRentDue()));
            onClick(buttonDateDue);
        }

        if (editting.get("entered")){
            textViewDateEntered.setText(Helpers.dateToString(tenantObject.getEntered()));
            onClick(buttonSurname);
        }

        tenantObject.save();
        onClick(fab);
        Toast.makeText(getContext(), "Completed!", Toast.LENGTH_SHORT).show();
    }

    private void onEditActions(ImageButton button, String editKey, EditText editText, TextView textView, String defaultText){
        if (editting.get(editKey)){
            editCount--;
            hide(editText);
            show(textView);
            editText.setText(defaultText);
            button.setImageResource(R.drawable.icon_edit);
            editting.remove(editKey);
            editting.put(editKey, false);
            if (editCount==0){
                fab.setImageResource(R.drawable.ic_close_black_24dp);
            }
            return;
        }
        editCount++;
        if (editCount==1){
            fab.setImageResource(R.drawable.ic_check_black_24dp);
        }
        hide(textView);
        show(editText);
        button.setImageResource(R.drawable.ic_close_black_24dp);
        editting.remove(editKey);
        editting.put(editKey, true);
        return;
    }

    private void onError(String notif, EditText item) {
        item.setTextColor(Color.RED);
        Toast.makeText(getContext(), notif, Toast.LENGTH_SHORT).show();
        colored= item;
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

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (result &&(requestCode==Helpers.REQUEST_CODE_DELETE)){
            tenantObject.delete();
            scrollToNext();
            return;
        }

        if (requestCode==Helpers.REQUEST_CODE_REPLACE){
            if (result){
                house_confirmed= true;
                completeEdit();
            }
            return;
        }

        if (requestCode== REQUEST_CODE_EDIT){
            if (result){
                due_confirmed= true;
                completeEdit();
            }
            else{
                onClick(buttonDateDue);
                completeEdit();
            }
            return;
        }
    }

    private void scrollToNext() {
        if (mListener != null) {
            mListener.onItemDeleted(tenant);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        chosen= (House) adapterView.getItemAtPosition(i);
        if (chosen.equals(tenantObject.getHouse())){
            chosen= null;
            onClick(buttonHouse);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onItemDeleted(Long id);
    }
}
