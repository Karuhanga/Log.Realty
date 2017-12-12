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
import butterknife.OnClick;
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
    // TODO: Rename parameter arguments, choose names that match

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

    private DetailedLocationActivityExternalInterface controller;

    private int editCount;
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
    // TODO: Rename and change types and number of parameters
    public static DetailedLocation newInstance(Long id) {
        DetailedLocation fragment = new DetailedLocation();
        Bundle args = new Bundle();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        controller= Controller.injectDetailedLocationActivityExternalInterface(this);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            controller.setLocation(getArguments().getLong("id"));
        }
        editing.put("location", false);
        editing.put("rent", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.detailed_location_fragment, container, false);

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

    @OnClick(R.id.fab_detailed_location_edit)
    public void onEditFabClick() {
        if (editCount==0){
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
        return;
    }

    @OnClick(R.id.fab_detailed_location_delete)
    public void onDeleteFabClick(){
        deleteLocation();
    }

    @OnClick(R.id.button_detailed_location_location)
    public void onEditLocationClick(){
        if (editing.get("location")){
            editCount--;
            textViewLocation.setText(controller.getLocation());
            editTextLocation.setText(controller.getLocation());
            hide(editTextLocation);
            show(textViewLocation);
            buttonLocation.setImageResource(EDIT);
            editing.remove("location");
            editing.put("location", false);
            if (editCount<1){
                fabEdit.setImageResource(CLOSE);
            }
            return;
        }
        editCount++;
        fabEdit.setImageResource(DONE);
        hide(textViewLocation);
        show(editTextLocation);
        buttonLocation.setImageResource(CLOSE);
        editing.remove("location");
        editing.put("location", true);
        return;
    }

    @OnClick(R.id.button_detailed_location_amount)
    public void onEditAmountClick(){
        if (editing.get("rent")){
            editCount--;
            hide(editTextAmount);
            show(textViewAmount);
            textViewAmount.setText(controller.getAmount());
            editTextAmount.setText(controller.getAmount());
            buttonAmount.setImageResource(EDIT);
            editing.remove("rent");
            editing.put("rent", false);
            if (editCount==0){
                fabEdit.setImageResource(CLOSE);
            }
            return;
        }
        editCount++;
        if (editCount==1){
            fabEdit.setImageResource(DONE);
        }
        hide(textViewAmount);
        show(editTextAmount);
        buttonAmount.setImageResource(CLOSE);
        editing.remove("rent");
        editing.put("rent", true);
        return;
    }

    @Override
    public Context requestContext() {
        return mListener.requestContext();
    }

    @Override
    public void onLocationDeleted(long id) {
        mListener.onItemDeleted(id);
    }

    @Override
    public void complainAboutRent(String message) {
        editTextAmount.setError(message);
    }

    @Override
    public void complainAboutLocation(String message) {
        editTextLocation.setError(message);
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
        void onItemDeleted(Long id);

        Context requestContext();
    }

    public interface DetailedLocationActivityExternalInterface{

        void setLocation(long id);

        String getAmount();

        String getLocation();

        void delete();

        boolean editName(String location);

        boolean editAmount(long amount);
    }

    private void deleteLocation() {
        controller.delete();
    }

    private void commenceEdit() {
        if (editCount<1){
            refresh();
            return;
        }

        if (editing.get("location")){
            if (empty(editTextLocation)){
                editTextLocation.setError(ERROR_REQUIRED);
                return;
            }
            controller.editName(getLocation());
            onEditLocationClick();
        }

        if (editing.get("rent")){
            if (empty(editTextAmount)){
                editTextAmount.setError(ERROR_REQUIRED);
                return;
            }
            if (!controller.editAmount(getAmount())){
                return;
            }
            onEditAmountClick();
        }


        Toast.makeText(mListener.requestContext(), "Complete!", Toast.LENGTH_SHORT).show();
        onEditFabClick();
    }

    private void refresh() {
        editCount= 0;
        editing.clear();
        editing.put("location", false);
        editing.put("rent", false);

        textViewLocation.setText(controller.getLocation());
        textViewAmount.setText(controller.getAmount());
        editTextLocation.setText(controller.getLocation());
        editTextAmount.setText(controller.getAmount());

        buttonLocation.setImageResource(EDIT);
        buttonAmount.setImageResource(EDIT);

        hide(buttonLocation);
        hide(buttonAmount);
        show(textViewLocation);
        show(textViewAmount);
        hide(editTextLocation);
        hide(editTextAmount);
    }

    private String getLocation(){
        String name= editTextLocation.getText().toString();
        name= cleaner(name);
        if (name==null){
            editTextLocation.setError(ERROR_REQUIRED);
        }
        log("In DetailedLocationActivity, error getting location!");
        return "";
    }

    private long getAmount(){
        return Integer.valueOf(editTextAmount.getText().toString());
    }
}
