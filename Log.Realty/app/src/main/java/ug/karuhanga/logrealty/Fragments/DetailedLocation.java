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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Select;

import java.util.HashMap;

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
public class DetailedLocation extends Fragment implements TextWatcher, View.OnClickListener, ug.karuhanga.logrealty.Listeners.Confirmation {
    // TODO: Rename parameter arguments, choose names that match

    private Long location;
    private ImageButton buttonLocation;
    private ImageButton buttonRent;
    private EditText editTextLocation;
    private EditText editTextRent;
    private EditText colored;
    private TextView textViewLocation;
    private TextView textViewRent;
    private FloatingActionButton fab;
    private FloatingActionButton fab_delete;

    private int previous_color;
    private int editCount;
    private Location locationObject;

    private HashMap<String, Boolean> editting= new HashMap<>();

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
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = getArguments().getLong("id");
        }
        editting.put("location", false);
        editting.put("rent", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.detailed_location_fragment, container, false);
        buttonLocation= view.findViewById(R.id.button_location_fragment_detailed_location);
        buttonRent= view.findViewById(R.id.button_amount_fragment_detailed_location);
        textViewLocation= view.findViewById(R.id.text_view_location_fragment_detailed_location);
        textViewRent= view.findViewById(R.id.text_view_amount_fragment_detailed_location);
        editTextLocation= view.findViewById(R.id.edit_text_location_fragment_detailed_location);
        editTextRent= view.findViewById(R.id.edit_text_amount_fragment_detailed_location);
        fab= view.findViewById(R.id.fab_detailed_location);
        fab_delete= view.findViewById(R.id.fab_delete_detailed_location);

        previous_color= editTextLocation.getCurrentTextColor();
        colored= editTextLocation;
        editCount= 0;
        locationObject= Location.findById(Location.class, location);

        textViewLocation.setText(locationObject.getName());
        textViewRent.setText(Helpers.toCurrency(locationObject.getDefaultRent()));
        editTextLocation.setText(locationObject.getName());
        editTextRent.setText(String.valueOf(locationObject.getDefaultRent()));

        buttonLocation.setOnClickListener(this);
        buttonRent.setOnClickListener(this);
        fab.setOnClickListener(this);
        fab_delete.setOnClickListener(this);

        editTextLocation.addTextChangedListener(this);
        editTextRent.addTextChangedListener(this);



        return view;
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
            if (editCount==0){
                if (buttonLocation.getVisibility()==View.VISIBLE){
                    fab.setImageResource(R.drawable.icon_edit);
                    hide(buttonLocation);
                    hide(buttonRent);
                }
                else{
                    fab.setImageResource(R.drawable.ic_close_black_24dp);
                    show(buttonLocation);
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
                editTextLocation.setText(locationObject.getName());
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

        if (view==buttonRent){
            if (editting.get("rent")){
                editCount--;
                hide(editTextRent);
                show(textViewRent);
                editTextRent.setText(String.valueOf(locationObject.getDefaultRent()));
                buttonRent.setImageResource(R.drawable.icon_edit);
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
            buttonRent.setImageResource(R.drawable.ic_close_black_24dp);
            editting.remove("rent");
            editting.put("rent", true);
            return;
        }

        if (view==fab_delete){
            deleteLocation();
        }
    }

    private void deleteLocation() {
        new Confirmation(getContext(), this, "Are you sure?", "Delete this location and all houses here:\n"+locationObject.getSummary(), R.drawable.ic_delete_black_24dp, "Yes", "Cancel", Helpers.REQUEST_CODE_DELETE).show();
    }

    private void completeEdit() {
        Location temp= Location.findById(Location.class, locationObject.getId());

        if (editCount==0){
            return;
        }
        if (editting.get("location")){
            String name= editTextLocation.getText().toString();
            name= Helpers.cleaner(name);
            if (name==null){
                onError("Invalid Location Name", editTextLocation);
                locationObject= temp;
                return;
            }
            locationObject.setName(name);
        }

        if (editting.get("rent")){
            int amount= Integer.valueOf(editTextRent.getText().toString());
            if (amount< Helpers.AMOUNT_MINIMUM_RENT){
                onError("Rent must be >=250,000/=", editTextRent);
                locationObject= temp;
                return;
            }
            locationObject.setDefaultRent(amount);
        }


        Toast.makeText(getContext(), "Completed!", Toast.LENGTH_SHORT).show();
        if (editting.get("location")){
            textViewLocation.setText(locationObject.getName());
            onClick(buttonLocation);
        }
        if (editting.get("rent")){
            textViewRent.setText(Helpers.toCurrency(locationObject.getDefaultRent()));
            onClick(buttonRent);
        }
        locationObject.save();
        onClick(fab);
    }

    private void onError(String notif, EditText item) {
        item.setTextColor(Color.RED);
        Toast.makeText(getContext(), notif, Toast.LENGTH_SHORT).show();
        colored= item;
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
            locationObject.delete();
            scrollToNext();
        }
    }

    private void scrollToNext() {
        if (mListener != null) {
            mListener.onItemDeleted(location);
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
