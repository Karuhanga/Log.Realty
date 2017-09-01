package ug.karuhanga.logrealty.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Data.MinifiedRecord;
import ug.karuhanga.logrealty.Data.Payment;
import ug.karuhanga.logrealty.Data.Tenant;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntityInterface.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntityInterface#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntityInterface extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    //TODO: BETTER FRGAMENT MANAGEMENT
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int ENTITY;
    private List<MinifiedRecord> data= new ArrayList<>();
    private Fragment fragmentView;
    private int displayNumber;
    private Button buttonLoadMore;
    private ListView listView;
    private Adapter listAdapter;

    private OnFragmentInteractionListener mListener;

    public EntityInterface() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param entity ENTITY.
     * @return A new instance of fragment EntityInterface.
     */
    // TODO: Rename and change types and number of parameters
    public static EntityInterface newInstance(int entity) {
        EntityInterface fragment = new EntityInterface();
        Bundle args = new Bundle();
        args.putInt("ENTITY", entity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ENTITY  = getArguments().getInt("ENTITY");
        }
        displayNumber= 10;
        fetchData(displayNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.entity_interface_fragment, container, false);
        buttonLoadMore= (Button) view.findViewById(R.id.button_load_more_entity_interface_fragment);
        listView= (ListView) view.findViewById(R.id.list_view_entity_interfaces);
        listAdapter= new ArrayAdapter<MinifiedRecord>(getContext(), R.layout.list_item_entity_interface, R.id.list_view_entity_interfaces, data);

        listView.setAdapter((ListAdapter) listAdapter);
        listView.setOnItemClickListener(this);

        buttonLoadMore.setOnClickListener(this);
        int data_amount= data.size();
        if (displayNumber>data_amount){
            displayNumber= data_amount;
            buttonLoadMore.setVisibility(View.GONE);
        }

        //TODO Remove Debug tool
        ((TextView) view.findViewById(R.id.textView_temp1)).setText(Helpers.getStringByName(getContext(), "fragment_label_"+String.valueOf(ENTITY)));

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

    private void fetchData(int limit) {
        data.clear();
        switch (ENTITY){
            case Helpers.FRAGMENT_LOCATIONS:
                List<Location> results;
                results= Select.from(Location.class).list();
                for (Location result : results) {
                    data.add(new MinifiedRecord(result.getId(), result.toString()));
                }
                break;
            case Helpers.FRAGMENT_HOUSES:
                List<House> results2;
                results2= Select.from(House.class).limit(String.valueOf(limit)).list();
                for (House result : results2) {
                    data.add(new MinifiedRecord(result.getId(), result.toString()));
                }
                break;
            case Helpers.FRAGMENT_TENANTS:
                List<Tenant> results3;
                results3= Select.from(Tenant.class).limit(String.valueOf(limit)).list();
                for (Tenant result : results3) {
                    data.add(new MinifiedRecord(result.getId(), result.toString()));
                }
                break;
            case Helpers.FRAGMENT_PAYMENTS:
                List<Payment> results4;
                results4= Select.from(Payment.class).limit(String.valueOf(limit)).list();
                for (Payment result : results4) {
                    data.add(new MinifiedRecord(result.getId(), result.toString()));
                }
                break;
            default:
                List<Payment> results5;
                results5= Select.from(Payment.class).limit(String.valueOf(limit)).list();
                for (Payment result : results5) {
                    data.add(new MinifiedRecord(result.getId(), result.toString()));
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(buttonLoadMore)){
            displayNumber+=10;
            fetchData(displayNumber);
            int data_amount= data.size();
            if (displayNumber>data_amount){
                displayNumber= data_amount;
                buttonLoadMore.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}
