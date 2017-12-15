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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import ug.karuhanga.logrealty.Controllers.Controller;
import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.ERROR_REQUIRED;
import static ug.karuhanga.logrealty.Helper.empty;
import static ug.karuhanga.logrealty.Helper.hide;
import static ug.karuhanga.logrealty.Helper.log;
import static ug.karuhanga.logrealty.Helper.show;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedLocation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailedLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedHouse extends Fragment implements AdapterView.OnItemClickListener, Controller.DetailedHouseControllerExternalInterface {

    private ArrayAdapter<Location> adapter;

    @BindView(R.id.button_detailed_house_location) ImageButton buttonLocation;
    @BindView(R.id.button_detailed_house_house_information) ImageButton buttonInfo;
    @BindView(R.id.button_detailed_house_rent) ImageButton buttonRent;

    @BindView(R.id.row_detailed_house_single_house) TableRow rowSingleHouse;
    @BindView(R.id.row_detailed_house_default_rent) TableRow rowDefaultRent;
    @BindView(R.id.text_view_detailed_house_label_number) View labelNumber;
    @BindView(R.id.text_view_detailed_house_label_description) View labelDescription;
    @BindView(R.id.text_view_detailed_house_label_amount) View labelRent;
    @BindView(R.id.check_box_detailed_house_single_house) CheckBox checkBoxSingleHouse;
    @BindView(R.id.check_box_detailed_house_default_rent) CheckBox checkBoxDefaultRent;

    @BindView(R.id.edit_text_detailed_house_location) AutoCompleteTextView editTextLocation;
    @BindView(R.id.edit_text_detailed_house_description) EditText editTextDescription;
    @BindView(R.id.edit_text_detailed_house_number) EditText editTextNumber;
    @BindView(R.id.edit_text_detailed_house_rent) EditText editTextRent;

    @BindView(R.id.text_view_detailed_house_location) TextView textViewLocation;
    @BindView(R.id.text_view_detailed_house_single_house) TextView textViewSingleHouse;
    @BindView(R.id.text_view_detailed_house_description) TextView textViewDescription;
    @BindView(R.id.text_view_detailed_house_number) TextView textViewNumber;
    @BindView(R.id.text_view_detailed_house_rent) TextView textViewRent;

    @BindView(R.id.fab_detailed_house_edit) FloatingActionButton fabEdit;

    private final int EDIT= R.drawable.ic_edit_black_24dp;
    private final int CLOSE= R.drawable.ic_close_black_24dp;
    private final int DONE= R.drawable.ic_done_black_24dp;

    private HashMap<String, Boolean> editing = new HashMap<>();
    private final int SINGLE_HOUSE= 0;
    private final int MULTIPLE_HOUSES= 2;
    private final int ERROR= -1;
    private final int DEFAULT_RENT= 5;
    private final int UNIQUE_RENT= 6;
    private final String LOCATION= "location";
    private final String INFORMATION= "information";
    private final String RENT= "rent";

    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;
    private DetailedHouseActivityExternalInterface controller= null;

    public DetailedHouse() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailedHouse.
     */
    public static DetailedHouse newInstance(Long id) {
        DetailedHouse fragment = new DetailedHouse();
        Bundle args = new Bundle();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Lifecycle methods
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setController();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getController().setHouse(getArguments().getLong("id"));
        }
        editing.put(LOCATION, false);
        editing.put(INFORMATION, false);
        editing.put(RENT, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.detailed_house_fragment, container, false);
        unbinder= ButterKnife.bind(this, view);

        adapter= new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, getController().getLocations());
        adapter.setNotifyOnChange(true);
        editTextLocation.setAdapter(adapter);
        editTextLocation.setThreshold(1);
        editTextLocation.setOnItemClickListener(this);

        refreshView();
        return view;
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
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * UI Changers
     */

    private void refreshView() {
        textViewLocation.setText(getController().getLocationName());
        editTextLocation.setText(getController().getLocationName());
        showHouseInformation();
        textViewRent.setText(getController().getRent());
        editTextRent.setText(getController().getRent());
    }

    private void hideHouseInformation(){
        if (getController().isASingleHouse()){
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

    private void showHouseInformation(){
        hide(rowDefaultRent);
        if (getController().isASingleHouse()){
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
            textViewNumber.setText("House No. "+getController().getNumber());
            textViewDescription.setText(getController().getDescription());
            editTextNumber.setText(getController().getNumber());
            editTextDescription.setText(getController().getDescription());
            show(textViewNumber);
            show(labelNumber);
            show(textViewDescription);
            show(labelDescription);
        }
    }

    /**
     * Major event triggers
     */

    private void deleteHouse() {
        getController().deleteHouse();
    }

    private void commenceEdit() {
        if (allEditsComplete()){
            refreshView();
            return;
        }

        if (editing.get(LOCATION)){
            if (empty(editTextLocation)){
                editTextLocation.setError(ERROR_REQUIRED);
                return;
            }
            getController().editLocation();
        }

        if (editing.get(INFORMATION)){
            switch (evaluateInformation()){
                case SINGLE_HOUSE: getController().editInformation();
                case MULTIPLE_HOUSES: getController().editInformation(getNumber(), getDescription());
                default: return;
            }
        }

        if (editing.get(RENT)){
            switch (evaluateRent()){
                case DEFAULT_RENT: getController().editRent();
                case UNIQUE_RENT: getController().editRent(getRent());
                default:
            }
        }
    }

    /**
     * Validators
     */

    private int evaluateRent() {
        if (checkBoxDefaultRent.isChecked()){
            return DEFAULT_RENT;
        }
        else{
            if (empty(editTextRent)){
                editTextRent.setError(ERROR_REQUIRED);
                return ERROR;
            }
            return UNIQUE_RENT;
        }
    }

    private int evaluateInformation() {
        if (checkBoxSingleHouse.isChecked()){
            return SINGLE_HOUSE;
        }
        else{
            if (empty(editTextNumber)){
                editTextNumber.setError(ERROR_REQUIRED);
                return ERROR;
            }

            if (empty(editTextDescription)){
                editTextDescription.setError(ERROR_REQUIRED);
                return ERROR;
            }
            return MULTIPLE_HOUSES;
        }
    }

    /**
     * Getters and setters
     */

    private String getDescription() {
        return editTextDescription.getText().toString();
    }

    private int getNumber() {
        int number;
        try{
            number= Integer.valueOf(editTextNumber.getText().toString());
        }catch (NumberFormatException e){
            return 0;
        }
        return number;
    }

    private long getRent() {
        long rentPaid;
        try{
            rentPaid= Long.valueOf(editTextRent.getText().toString());
        }catch (NumberFormatException e){
            return 0;
        }
        return rentPaid;
    }

    /**
     * Error notifiers
     */

    @Override
    public void complainAboutLocation(String message) {
        editTextLocation.setError(message);
    }

    @Override
    public void complainAboutRent(String message) {
        editTextRent.setError(message);
    }

    @Override
    public void complainAboutHouseInformation(String message) {
        editTextNumber.setError(message);
    }


    /**
     * External Interaction Methods and Interfaces
     */

    @Override
    public Context requestContext() {
        if (!(mListener==null)){
            return mListener.requestContext();
        }
        return null;
    }

    interface OnFragmentInteractionListener {

        void onItemDeleted(Long id);

        Context requestContext();
    }

    public interface DetailedHouseActivityExternalInterface{

        void setHouse(long id);

        List<Location> getLocations();

        void deleteHouse();

        void setChosenLocation(Location location);

        String getLocationName();

        void editLocation();

        void editInformation();

        void editInformation(int number, String description);

        void editRent();

        void editRent(long rent);

        String getRent();

        String getDescription();

        String getNumber();

        boolean isASingleHouse();
    }

    /**
     * Post the fact methods
     */

    @Override
    public void onHouseDeleted(long id) {
        if (!(mListener==null)){
            mListener.onItemDeleted(id);
        }
    }

    @Override
    public void onLocationEditComplete() {
        textViewLocation.setText(getController().getLocationName());
        onLocationButtonClicked();
        onAllEditsDone();
    }

    @Override
    public void onInfoEditComplete() {
        onInformationButtonClicked();
        onAllEditsDone();
    }

    @Override
    public void onRentEditComplete() {
        textViewRent.setText(getController().getRent());
        onRentButtonClicked();
        onAllEditsDone();
    }

    private void onAllEditsDone() {
        if (allEditsComplete()){
            onEditFabClick();
        }
        Toast.makeText(requestContext(), "Completed!", Toast.LENGTH_SHORT).show();
    }

    private boolean allEditsComplete(){
        return (!editing.get(LOCATION)) && (!editing.get(INFORMATION)) && (!editing.get(RENT));
    }

    /**
     * Getters and setters
     */

    public DetailedHouseActivityExternalInterface getController() {
        if (controller==null){
            setController();
        }
        return controller;
    }

    public void setController() {
        this.controller = Controller.injectDetailedHouseActivityExternalInterface(this);
    }

    /**
     * UI Interaction Handlers
     */

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        getController().setChosenLocation((Location) adapterView.getItemAtPosition(i));
    }

    @OnCheckedChanged({R.id.check_box_detailed_house_single_house, R.id.check_box_detailed_house_default_rent})
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

    @OnClick(R.id.fab_detailed_house_edit)
    public void onEditFabClick(){
        if (allEditsComplete()){
            if (buttonLocation.getVisibility()==View.VISIBLE){
                fabEdit.setImageResource(EDIT);
                hide(buttonLocation);
                hide(buttonInfo);
                hide(buttonRent);
            }
            else{
                fabEdit.setImageResource(CLOSE);
                show(buttonLocation);
                show(buttonInfo);
                show(buttonRent);
            }
            return;
        }
        commenceEdit();
    }

    @OnClick(R.id.button_detailed_house_location)
    public void onLocationButtonClicked(){
        if (editing.get(LOCATION)){
            if (allEditsComplete()){
                fabEdit.setImageResource(CLOSE);
            }
            hide(editTextLocation);
            show(textViewLocation);
            editTextLocation.setText(getController().getLocationName());
            buttonLocation.setImageResource(EDIT);
            editing.put(LOCATION, false);
            return;
        }
        hide(textViewLocation);
        show(editTextLocation);
        buttonLocation.setImageResource(CLOSE);
        editing.put(LOCATION, true);
        if (!allEditsComplete()){
            fabEdit.setImageResource(DONE);
        }
    }

    @OnClick(R.id.button_detailed_house_house_information)
    public void onInformationButtonClicked(){
        if (editing.get(INFORMATION)){
            hide(editTextNumber);
            hide(editTextDescription);
            hide(checkBoxSingleHouse);
            hide(rowSingleHouse);
            showHouseInformation();
            buttonInfo.setImageResource(EDIT);
            editing.put(INFORMATION, false);
            if (allEditsComplete()){
                fabEdit.setImageResource(R.drawable.ic_close_black_24dp);
            }
            return;
        }
        show(checkBoxSingleHouse);
        show(editTextNumber);
        show(editTextDescription);
        show(labelNumber);
        show(labelDescription);
        hideHouseInformation();
        buttonInfo.setImageResource(CLOSE);
        editing.put(INFORMATION, true);
        if (!allEditsComplete()){
            fabEdit.setImageResource(DONE);
        }
    }

    @OnClick(R.id.button_detailed_house_rent)
    public void onRentButtonClicked(){
        if (editing.get(RENT)){
            hide(editTextRent);
            hide(rowDefaultRent);
            show(textViewRent);
            show(labelRent);
            editTextRent.setText(getController().getRent());
            buttonRent.setImageResource(EDIT);
            checkBoxDefaultRent.setChecked(false);
            editing.put(RENT, false);
            if (allEditsComplete()){
                fabEdit.setImageResource(CLOSE);
            }
            return;
        }
        hide(textViewRent);
        show(editTextRent);
        show(labelRent);
        show(rowDefaultRent);
        buttonRent.setImageResource(CLOSE);
        editing.put(RENT, true);
        if (!allEditsComplete()){
            fabEdit.setImageResource(DONE);
        }
    }

    @OnClick(R.id.fab_detailed_house_delete)
    public void onFabDeleteClicked() {
        deleteHouse();
    }
}
