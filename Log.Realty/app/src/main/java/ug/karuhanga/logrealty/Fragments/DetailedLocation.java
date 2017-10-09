package ug.karuhanga.logrealty.Fragments;

import android.content.Context;
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

import com.orm.query.Select;

import java.util.HashMap;

import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedLocation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailedLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedLocation extends Fragment implements TextWatcher, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match

    private Long location;
    private ImageButton buttonLocation;
    private ImageButton buttonRent;
    private EditText editTextLocation;
    private EditText editTextRent;
    private TextView textViewLocation;
    private TextView textViewRent;
    private FloatingActionButton fab;

    private int previousColor;
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

        previousColor= editTextLocation.getCurrentTextColor();
        editCount= 0;
        locationObject= Location.findById(Location.class, location);

        textViewLocation.setText(locationObject.getName());
        textViewRent.setText(Helpers.toCurrency(locationObject.getDefaultRent()));
        editTextLocation.setText(textViewLocation.getText());
        textViewRent.setText(textViewRent.getText());

        buttonLocation.setOnClickListener(this);
        buttonRent.setOnClickListener(this);
        fab.setOnClickListener(this);

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
                    buttonLocation.setVisibility(View.INVISIBLE);
                    buttonRent.setVisibility(View.INVISIBLE);
                }
                else{
                    fab.setImageResource(R.drawable.ic_delete_black_24dp);
                    buttonLocation.setVisibility(View.VISIBLE);
                    buttonRent.setVisibility(View.VISIBLE);
                }
                return;
            }
            completeEdit();
            return;
        }
        if (view==buttonLocation){
            if (editting.get("location")){
                editCount--;
                editTextLocation.setVisibility(View.GONE);
                textViewLocation.setVisibility(View.VISIBLE);
                editTextLocation.setText(textViewLocation.getText());
                buttonLocation.setImageResource(R.drawable.icon_edit);
                editting.remove("location");
                editting.put("location", false);
                if (editCount==0){
                    fab.setImageResource(R.drawable.ic_delete_black_24dp);
                }
                return;
            }
            editCount++;
            if (editCount==1){
                fab.setImageResource(R.drawable.ic_check_black_24dp);
            }
            textViewLocation.setVisibility(View.GONE);
            editTextLocation.setVisibility(View.VISIBLE);
            buttonLocation.setImageResource(R.drawable.ic_delete_black_24dp);
            editting.remove("location");
            editting.put("location", true);
            return;
        }

        if (view==buttonRent){
            if (editting.get("rent")){
                editCount--;
                editTextRent.setVisibility(View.GONE);
                textViewRent.setVisibility(View.VISIBLE);
                editTextRent.setText(textViewRent.getText());
                buttonRent.setImageResource(R.drawable.icon_edit);
                editting.remove("rent");
                editting.put("rent", false);
                if (editCount==0){
                    fab.setImageResource(R.drawable.ic_delete_black_24dp);
                }
                return;
            }
            editCount++;
            if (editCount==1){
                fab.setImageResource(R.drawable.ic_check_black_24dp);
            }
            textViewRent.setVisibility(View.GONE);
            editTextRent.setVisibility(View.VISIBLE);
            buttonRent.setImageResource(R.drawable.ic_delete_black_24dp);
            editting.remove("rent");
            editting.put("rent", true);
            return;
        }
    }

    private void completeEdit() {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

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
    }
}
