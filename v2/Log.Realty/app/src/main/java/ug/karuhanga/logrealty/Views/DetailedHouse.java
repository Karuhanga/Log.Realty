package ug.karuhanga.logrealty.Views;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.HashMap;
import java.util.List;

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.Popups.Confirmation;
import ug.karuhanga.logrealty.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedLocation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailedLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedHouse extends Fragment implements TextWatcher, View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, ug.karuhanga.logrealty.Listeners.Confirmation {
    // TODO: Rename parameter arguments, choose names that match

    private Long house;
    private ArrayAdapter<Location> adapter;
    private List<Location> results;

    private ImageButton buttonLocation;
    private ImageButton buttonInfo;
    private ImageButton buttonRent;

    private TableRow rowSingleHouse;
    private TableRow rowDefaultRent;
    private View labelNumber;
    private View labelDescription;
    private View labelRent;
    private CheckBox checkBoxSingleHouse;
    private CheckBox checkBoxDefaultRent;

    private AutoCompleteTextView editTextLocation;
    private EditText editTextDescription;
    private EditText editTextNumber;
    private EditText editTextRent;

    private EditText colored;

    private TextView textViewLocation;
    private TextView textViewSingleHouse;
    private TextView textViewDescription;
    private TextView textViewNumber;
    private TextView textViewRent;

    private FloatingActionButton fab;
    private FloatingActionButton fab_delete;

    private int previous_color;
    private int editCount;
    private House houseObject;
    private Location chosen;

    private HashMap<String, Boolean> editting= new HashMap<>();

    private OnFragmentInteractionListener mListener;

    public DetailedHouse() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailedHouse.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedHouse newInstance(Long id) {
        DetailedHouse fragment = new DetailedHouse();
        Bundle args = new Bundle();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            house = getArguments().getLong("id");
        }
        editting.put("location", false);
        editting.put("information", false);
        editting.put("rent", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.detailed_house_fragment, container, false);
        buttonLocation= view.findViewById(R.id.button_detailed_house_location);
        buttonInfo= view.findViewById(R.id.button_detailed_house_house_information);
        buttonRent= view.findViewById(R.id.button_detailed_house_amount);
        textViewLocation= view.findViewById(R.id.text_view_detailed_house_location);
        textViewNumber= view.findViewById(R.id.text_view_detailed_house_number);
        textViewDescription= view.findViewById(R.id.text_view_detailed_house_description);
        textViewRent= view.findViewById(R.id.text_view_detailed_house_amount);
        textViewSingleHouse= view.findViewById(R.id.text_view_detailed_house_single_house);

        rowSingleHouse= view.findViewById(R.id.row_detailed_house_single_house);
        rowDefaultRent= view.findViewById(R.id.row_detailed_house_default_rent);
        labelNumber= view.findViewById(R.id.text_view_detailed_house_label_number);
        labelDescription= view.findViewById(R.id.text_view_detailed_house_label_description);
        labelRent= view.findViewById(R.id.text_view_detailed_house_label_amount);
        checkBoxSingleHouse= view.findViewById(R.id.check_box_single_house_add_house);
        checkBoxDefaultRent= view.findViewById(R.id.check_box_detailed_house_default_rent);

        editTextLocation= view.findViewById(R.id.edit_text_detailed_house_location);
        editTextNumber= view.findViewById(R.id.edit_text_detailed_house_number);
        editTextDescription= view.findViewById(R.id.edit_text_detailed_house_description);
        editTextRent= view.findViewById(R.id.edit_text_detailed_house_amount);

        fab= view.findViewById(R.id.fab_detailed_location_edit);
        fab_delete= view.findViewById(R.id.fab_detailed_location_delete);
        checkBoxSingleHouse= view.findViewById(R.id.check_box_detailed_house_single_house);
        checkBoxDefaultRent= view.findViewById(R.id.check_box_detailed_house_default_rent);

        results= Select.from(Location.class).list();
        adapter= new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, results);
        adapter.setNotifyOnChange(true);
        editTextLocation.setAdapter(adapter);
        editTextLocation.setThreshold(1);
        editTextLocation.setOnItemClickListener(this);
        chosen= null;

        previous_color= editTextLocation.getCurrentTextColor();
        colored= editTextLocation;
        editCount= 0;
        houseObject= House.findById(House.class, house);

        textViewLocation.setText(houseObject.getLocation().getName());
        editTextLocation.setText(houseObject.getLocation().getName());

        displayInfo();

        hide(rowDefaultRent);
        textViewRent.setText(Helpers.toCurrency(houseObject.getRent()));
        editTextRent.setText(String.valueOf(houseObject.getRent()));

        buttonLocation.setOnClickListener(this);
        buttonInfo.setOnClickListener(this);
        buttonRent.setOnClickListener(this);
        fab.setOnClickListener(this);
        fab_delete.setOnClickListener(this);
        checkBoxSingleHouse.setOnCheckedChangeListener(this);
        checkBoxDefaultRent.setOnCheckedChangeListener(this);

        editTextLocation.addTextChangedListener(this);
        editTextNumber.addTextChangedListener(this);
        editTextDescription.addTextChangedListener(this);
        editTextRent.addTextChangedListener(this);

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

    @Override
    public void onClick(View view) {
        if (view==fab){
            if (editCount==0){
                if (buttonLocation.getVisibility()==View.VISIBLE){
                    fab.setImageResource(R.drawable.icon_edit);
                    hide(buttonLocation);
                    hide(buttonInfo);
                    hide(buttonRent);
                }
                else{
                    fab.setImageResource(R.drawable.ic_close_black_24dp);
                    show(buttonLocation);
                    show(buttonInfo);
                    show(buttonRent);
                }
                return;
            }
            completeEdit();
            return;
        }
        if (view==buttonLocation){
            if (editting.get("location")){
                editCount--;
                hide(editTextLocation);
                show(textViewLocation);
                editTextLocation.setText(houseObject.getLocation().getName());
                buttonLocation.setImageResource(R.drawable.icon_edit);
                editting.remove("location");
                editting.put("location", false);
                if (editCount==0){
                    fab.setImageResource(R.drawable.ic_close_black_24dp);
                }
                return;
            }
            editCount++;
            if (editCount==1){
                fab.setImageResource(R.drawable.ic_check_black_24dp);
            }
            hide(textViewLocation);
            show(editTextLocation);
            buttonLocation.setImageResource(R.drawable.ic_close_black_24dp);
            editting.remove("location");
            editting.put("location", true);
            return;
        }

        if (view==buttonInfo){
            if (editting.get("information")){
                editCount--;
                hide(editTextNumber);
                hide(editTextDescription);
                hide(checkBoxSingleHouse);
                hide(rowSingleHouse);
                displayInfo();
                buttonInfo.setImageResource(R.drawable.icon_edit);
                editting.remove("information");
                editting.put("information", false);
                if (editCount==0){
                    fab.setImageResource(R.drawable.ic_close_black_24dp);
                }
                return;
            }
            editCount++;
            if (editCount==1){
                fab.setImageResource(R.drawable.ic_check_black_24dp);
            }
            show(checkBoxSingleHouse);
            show(editTextNumber);
            show(editTextDescription);
            show(labelNumber);
            show(labelDescription);
            hideInfo();
            buttonInfo.setImageResource(R.drawable.ic_close_black_24dp);
            editting.remove("information");
            editting.put("information", true);
            return;
        }

        if (view==buttonRent){
            if (editting.get("rent")){
                editCount--;
                hide(editTextRent);
                hide(rowDefaultRent);
                show(textViewRent);
                show(labelRent);
                editTextRent.setText(String.valueOf(houseObject.getRent()));
                buttonRent.setImageResource(R.drawable.icon_edit);
                checkBoxDefaultRent.setChecked(false);
                editting.remove("rent");
                editting.put("rent", false);
                if (editCount==0){
                    fab.setImageResource(R.drawable.ic_close_black_24dp);
                }
                return;
            }
            editCount++;
            if (editCount==1){
                fab.setImageResource(R.drawable.ic_check_black_24dp);
            }
            hide(textViewRent);
            show(editTextRent);
            show(labelRent);
            show(rowDefaultRent);
            buttonRent.setImageResource(R.drawable.ic_close_black_24dp);
            editting.remove("rent");
            editting.put("rent", true);
            return;
        }

        if (view==fab_delete){
            deleteHouse();
            return;
        }
    }

    private void deleteHouse() {
        new Confirmation(getContext(), this, "Are you sure?", "Delete this house and its tenant:\n"+houseObject.getSummary(), R.drawable.ic_delete_black_24dp, "Yes", "Cancel", Helpers.REQUEST_CODE_DELETE).show();
    }

    private void displayInfo(){
        if (houseObject.getNumber()==0){
            hide(textViewNumber);
            hide(labelNumber);
            hide(textViewDescription);
            show(rowSingleHouse);
            show(textViewSingleHouse);
            hide(labelNumber);
            hide(labelDescription);
        }
        else{
            hide(rowSingleHouse);
            hide(textViewSingleHouse);
            textViewNumber.setText("House No. "+String.valueOf(houseObject.getNumber()));
            textViewDescription.setText(houseObject.getDescription());
            editTextNumber.setText(String.valueOf(houseObject.getNumber()));
            editTextDescription.setText(houseObject.getDescription());
            show(textViewNumber);
            show(labelNumber);
            show(textViewDescription);
            show(labelDescription);
        }
    }

    private void hideInfo(){
        if (houseObject.getNumber()==0){
            hide(textViewSingleHouse);
            checkBoxSingleHouse.setChecked(true);
            hide(editTextNumber);
            hide(editTextDescription);
            hide(labelNumber);
            hide(labelDescription);
        }
        else{
            show(rowSingleHouse);
            checkBoxSingleHouse.setChecked(false);
            hide(textViewNumber);
            hide(textViewDescription);
        }
    }

    private void completeEdit() {
        String desc;
        int number;
        int rentPaid;
        House temp= House.findById(House.class, houseObject.getId());

        if (editCount==0){
            return;
        }

        if (editting.get("location")){
            if (chosen==null){
                onError("Invalid Location Name", editTextLocation);
                houseObject= temp;
                return;
            }

            List<House> results= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(houseObject.getNumber())).list();
            results.addAll(Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(0)).list());
            if (!(results.isEmpty())){
                Toast.makeText(getContext(), "This house already exists!", Toast.LENGTH_LONG).show();
                houseObject= temp;
                return;
            }
            houseObject.setLocation(chosen);
        }

        if (editting.get("information")){
            if (checkBoxSingleHouse.isChecked()){
                //if previously not checked
                if (houseObject.getNumber()!=0){
                    List<House> results= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).list();
                    if (!(results.isEmpty())){
                        Toast.makeText(getContext(), "This location already has multiple houses", Toast.LENGTH_LONG).show();
                        houseObject= temp;
                        return;
                    }
                    else{
                        houseObject.setNumber(0);
                    }
                }
                //otherwise change nothing
            }
            else{
                desc= editTextDescription.getText().toString();
                if (desc.length()<1){
                    onError("Please provide a small description", editTextDescription);
                    houseObject= temp;
                    return;
                }
                houseObject.setDescription(desc);

                try{
                    number= Integer.valueOf(editTextNumber.getText().toString());
                }catch (NumberFormatException e){
                    onError("Please input a house number", editTextNumber);
                    houseObject= temp;
                    return;
                }

                List<House> responses= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(number)).list();
                if (!(responses.isEmpty())){
                    onError("This house was already added", editTextNumber);
                    houseObject= temp;
                    return;
                }
                if (number==0){
                    onError("Please input a valid house number", editTextNumber);
                    houseObject= temp;
                    return;
                }

                houseObject.setNumber(number);
            }
        }

        if (editting.get("rent")){
            if (checkBoxDefaultRent.isChecked()){
                houseObject.setRent(houseObject.getLocation().getDefaultRent());
            }
            else{
                try{
                    rentPaid= Integer.valueOf(editTextRent.getText().toString());
                }catch (NumberFormatException e){
                    onError("Please input a rent amount", editTextRent);
                    houseObject=temp;
                    return;
                }
                if (rentPaid< Helpers.AMOUNT_MINIMUM_RENT){
                    onError("Rent must be at least UgShs.250,000/=", editTextRent);
                    houseObject=temp;
                    return;
                }
                houseObject.setRent(rentPaid);
            }
        }

        if (editting.get("location")){
            textViewLocation.setText(houseObject.getLocation().getName());
            onClick(buttonLocation);
        }
        if (editting.get("information")){
            onClick(buttonInfo);
        }
        if (editting.get("rent")){
            textViewRent.setText(Helpers.toCurrency(houseObject.getRent()));
            onClick(buttonRent);
        }

        houseObject.save();
        onClick(fab);
        Toast.makeText(getContext(), "Complete!", Toast.LENGTH_SHORT).show();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        chosen= (Location) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (result &&(requestCode==Helpers.REQUEST_CODE_DELETE)){
            houseObject.delete();
            scrollToNext();
        }
    }

    private void scrollToNext() {
        if (mListener != null) {
            mListener.onItemDeleted(house);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton==checkBoxSingleHouse){
            if (b){
                hide(editTextNumber);
                hide(editTextDescription);
                hide(labelNumber);
                hide(labelDescription);
            }
            else{
                show(editTextNumber);
                show(editTextDescription);
                show(labelNumber);
                show(labelDescription);
            }
        }
        else if (compoundButton==checkBoxDefaultRent){
            if (b){
                hide(editTextRent);
                hide(labelRent);
            }
            else{
                show(editTextRent);
                show(labelRent);
            }
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

        void onItemDeleted(Long id);
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
}
