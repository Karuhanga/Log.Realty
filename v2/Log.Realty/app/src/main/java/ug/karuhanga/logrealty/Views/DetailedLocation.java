package ug.karuhanga.logrealty.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ug.karuhanga.logrealty.Controllers.Controller;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.ERROR_REQUIRED;
import static ug.karuhanga.logrealty.Helper.cleaner;
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
public class DetailedLocation extends Fragment implements Controller.DetailedLocationControllerExternalInterface {

    @BindView(R.id.button_detailed_location_location) ImageButton buttonLocation;
    @BindView(R.id.button_detailed_location_amount) ImageButton buttonAmount;
    @BindView(R.id.edit_text_detailed_location_location) EditText editTextLocation;
    @BindView(R.id.edit_text_detailed_location_amount) EditText editTextAmount;
    @BindView(R.id.text_view_detailed_location_location) TextView textViewLocation;
    @BindView(R.id.text_view_detailed_location_amount) TextView textViewAmount;
    @BindView(R.id.fab_detailed_location_edit) FloatingActionButton fabEdit;

    private final int EDIT= R.drawable.ic_edit_black_24dp;
    private final int DONE= R.drawable.ic_check_black_24dp;
    private final int CLOSE= R.drawable.ic_close_black_24dp;

    private final String LOCATION= "location";
    private final String RENT= "rent";

    private DetailedLocationActivityExternalInterface controller= null;
    private Unbinder unbinder;

    private HashMap<String, Boolean> editing = new HashMap<>();

    private OnFragmentInteractionListener mListener;

    public DetailedLocation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailedLocation.
     */
    public static DetailedLocation newInstance(Long id) {
        DetailedLocation fragment = new DetailedLocation();
        Bundle args = new Bundle();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Lifecycle Methods
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setController();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getController().setLocation(getArguments().getLong("id"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.detailed_location_fragment, container, false);
        unbinder= ButterKnife.bind(this, view);

        refresh();
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
     * Major Event Starters
     */

    private void deleteLocation() {
        getController().delete();
    }

    private void commenceEdit() {
        if (allEditsComplete()){
            refresh();
            return;
        }

        if (editing.get(LOCATION)){
            if (empty(editTextLocation)){
                editTextLocation.setError(ERROR_REQUIRED);
                return;
            }
            getController().editName(getLocation());
        }

        if (editing.get(RENT)){
            if (empty(editTextAmount)){
                editTextAmount.setError(ERROR_REQUIRED);
                return;
            }
            getController().editAmount(getAmount());
        }
    }

    /**
     * UI Interactions
     */

    @OnClick(R.id.fab_detailed_location_edit)
    public void onEditFabClick() {
        if (allEditsComplete()){
            if (buttonLocation.getVisibility()==View.VISIBLE){
                fabEdit.setImageResource(EDIT);
                hide(buttonLocation);
                hide(buttonAmount);
            }
            else{
                fabEdit.setImageResource(CLOSE);
                show(buttonLocation);
                show(buttonAmount);
            }
            return;
        }
        commenceEdit();
    }

    @OnClick(R.id.fab_detailed_location_delete)
    public void onDeleteFabClick(){
        deleteLocation();
    }

    @OnClick(R.id.button_detailed_location_location)
    public void onEditLocationClick(){
        if (editing.get(LOCATION)){
            editTextLocation.setText(getController().getLocation());
            hide(editTextLocation);
            show(textViewLocation);
            buttonLocation.setImageResource(EDIT);
            editing.put(LOCATION, false);
            if (allEditsComplete()){
                fabEdit.setImageResource(CLOSE);
            }
            return;
        }
        fabEdit.setImageResource(DONE);
        hide(textViewLocation);
        show(editTextLocation);
        buttonLocation.setImageResource(CLOSE);
        editing.remove("location");
        editing.put("location", true);
        if (!allEditsComplete()){
            fabEdit.setImageResource(DONE);
        }
    }

    @OnClick(R.id.button_detailed_location_amount)
    public void onEditAmountClick(){
        if (editing.get("rent")){
            hide(editTextAmount);
            show(textViewAmount);
            editTextAmount.setText(getController().getNumericalAmount());
            buttonAmount.setImageResource(EDIT);
            editing.put("rent", false);
            if (allEditsComplete()){
                fabEdit.setImageResource(CLOSE);
            }
            return;
        }
        hide(textViewAmount);
        show(editTextAmount);
        buttonAmount.setImageResource(CLOSE);
        editing.put("rent", true);
        if (!allEditsComplete()){
            fabEdit.setImageResource(DONE);
        }
    }

    /**
     * External Interaction Methods and Interfaces
     */

    @Override
    public Context requestContext() {
        return mListener.requestContext();
    }

    @Override
    public void onLocationDeleted(long id) {
        mListener.onItemDeleted(id);
    }

    interface OnFragmentInteractionListener {
        void onItemDeleted(Long id);

        Context requestContext();
    }

    public interface DetailedLocationActivityExternalInterface{

        void setLocation(long id);

        String getAmount();

        String getLocation();

        void delete();

        void editName(String location);

        void editAmount(long amount);

        String getNumericalAmount();
    }

    /**
     * Error Notifiers
     */

    @Override
    public void complainAboutRent(String message) {
        editTextAmount.setError(message);
    }

    @Override
    public void complainAboutLocation(String message) {
        editTextLocation.setError(message);
    }


    /**
     * Post the fact methods
     */

    private void onAllEditsDone() {
        if (allEditsComplete()){
            onEditFabClick();
        }
        Toast.makeText(requestContext(), "Completed!", Toast.LENGTH_SHORT).show();
    }

    private boolean allEditsComplete(){
        return (!editing.get(LOCATION)) && (!editing.get(RENT));
    }

    @Override
    public void onEditLocationComplete() {
        textViewLocation.setText(getController().getLocation());
        onEditLocationClick();
        onAllEditsDone();
    }

    @Override
    public void onEditAmountComplete() {
        textViewAmount.setText(getController().getAmount());
        onEditAmountClick();
        onAllEditsDone();
    }

    /**
     * UI Change methods
     */

    private void refresh() {
        editing.clear();
        editing.put(LOCATION, false);
        editing.put(RENT, false);

        textViewLocation.setText(getController().getLocation());
        textViewAmount.setText(getController().getAmount());
        editTextLocation.setText(getController().getLocation());
        editTextAmount.setText(getController().getNumericalAmount());

        buttonLocation.setImageResource(EDIT);
        buttonAmount.setImageResource(EDIT);

        hide(buttonLocation);
        hide(buttonAmount);
        show(textViewLocation);
        show(textViewAmount);
        hide(editTextLocation);
        hide(editTextAmount);
    }

    /**
     * Getters and setters
     */

    private String getLocation(){
        String name= editTextLocation.getText().toString();
        name= cleaner(name);
        if (name==null){
            log("In DetailedLocationActivity, error getting location!");
            editTextLocation.setError(ERROR_REQUIRED);
        }
        return name;
    }

    private long getAmount(){
        return Integer.valueOf(editTextAmount.getText().toString());
    }

    private DetailedLocationActivityExternalInterface getController(){
        if (controller==null){
            setController();
        }
        return controller;
    }

    private void setController(){
        this.controller= Controller.injectDetailedLocationActivityExternalInterface(this);
    }
}
