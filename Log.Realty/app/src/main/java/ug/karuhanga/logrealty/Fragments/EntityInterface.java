package ug.karuhanga.logrealty.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.List;

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Data.MinifiedRecord;
import ug.karuhanga.logrealty.Data.Payment;
import ug.karuhanga.logrealty.Data.Tenant;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.Listeners.Confirmation;
import ug.karuhanga.logrealty.Listeners.GistInteractionListener;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helpers.REQUEST_CODE_DELETE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntityInterface.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntityInterface#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntityInterface extends Fragment implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener, GistInteractionListener, Confirmation {
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
    private ArrayAdapter listAdapter;
    private boolean inSelectionProcess;
    private List<MinifiedRecord> selected= new ArrayList<>();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inSelectionProcess= false;
        View view= inflater.inflate(R.layout.entity_interface_fragment, container, false);
        buttonLoadMore= (Button) view.findViewById(R.id.button_load_more_entity_interface_fragment);
        listView= (ListView) view.findViewById(R.id.list_view_entity_interfaces);
        listAdapter= new ArrayAdapter<>(getContext(), R.layout.list_item_entity_interface, R.id.textView_list_item_entity_interface, data);

        listView.setAdapter((ListAdapter) listAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        buttonLoadMore.setOnClickListener(this);

        fetchData(displayNumber);

        int data_amount= data.size();
        if (displayNumber>data_amount){
            displayNumber= data_amount;
            buttonLoadMore.setVisibility(View.GONE);
        }


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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!inSelectionProcess){
            onDetailsRequested(((MinifiedRecord) listAdapter.getItem(adapterView.getPositionForView(view))).getId());
            return;
        }

        MinifiedRecord record= (MinifiedRecord) listAdapter.getItem(adapterView.getPositionForView(view));
        if (selected.contains(record)){
            selected.remove(record);
            unSelectVisual(view);
            if (selected.size()<1){
                inSelectionProcess= false;
            }
            selectionUpdate(selected.size());
            return;
        }
        selected.add(record);
        selectVisual(view);
        selectionUpdate(selected.size());
        return;
    }

    private void selectVisual(View view) {
        view.findViewById(R.id.layout_inner_list_item_entity_interface).setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
    }

    private void unSelectVisual(View view) {
        view.findViewById(R.id.layout_inner_list_item_entity_interface).setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (inSelectionProcess){
            inSelectionProcess= false;
            for (MinifiedRecord record : selected) {
                unSelectVisual(listView.getChildAt(listAdapter.getPosition(record)));
            }
            selected.clear();
            selectionUpdate(selected.size());
            return true;
        }
        selected.clear();
        selected.add((MinifiedRecord) listAdapter.getItem(i));
        selectVisual(view);
        inSelectionProcess= true;
        selectionUpdate(selected.size());
        return true;
    }

    @Override
    public boolean onBackPressed() {
        if (inSelectionProcess){
            inSelectionProcess= false;
            for (MinifiedRecord record : selected) {
                unSelectVisual(listView.getChildAt(listAdapter.getPosition(record)));
            }
            selected.clear();
            selectionUpdate(selected.size());
            return true;
        }
        return false;
    }

    @Override
    public boolean onEditPressed() {
        if (inSelectionProcess){
            inSelectionProcess= false;
            Long id= selected.get(0).getId();
            for (MinifiedRecord record : selected) {
                unSelectVisual(listView.getChildAt(listAdapter.getPosition(record)));
            }
            selected.clear();
            selectionUpdate(selected.size());
            onDetailsRequested(id);
        }
        return true;
    }

    @Override
    public boolean onDeletePressed() {
        if (selected.size()>1){
            new ug.karuhanga.logrealty.Popups.Confirmation(getContext(), this, "Are you sure?", "Delete:\n"+String.valueOf(selected.size())+" items", R.drawable.ic_delete_black_24dp, "Yes", "No", REQUEST_CODE_DELETE).show();
            return false;
        }
        new ug.karuhanga.logrealty.Popups.Confirmation(getContext(), this, "Are you sure?", "Delete:\n"+selected.get(0).getDescription(), R.drawable.ic_delete_black_24dp, "Yes", "No", REQUEST_CODE_DELETE).show();
        return false;
    }

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (result && requestCode==REQUEST_CODE_DELETE){
            performPendingActions();
        }
    }

    private void performPendingActions() {
        if (selected.size()<1){
            return;
            //TODO Notify Caller of failure
        }
        for (MinifiedRecord record:selected) {
            boolean result;
            switch (ENTITY){
                case Helpers.FRAGMENT_LOCATIONS:
                     result= Location.findById(Location.class, record.getId()).delete();
                    if (result){
                    }
                    break;
                case Helpers.FRAGMENT_HOUSES:
                    result= House.findById(House.class, record.getId()).delete();
                    if (result){
                    }
                    break;
                case Helpers.FRAGMENT_TENANTS:
                    result= Tenant.findById(Tenant.class, record.getId()).delete();
                    if (result){
                    }
                    break;
                case Helpers.FRAGMENT_PAYMENTS:
                    Payment payment= Payment.findById(Payment.class, record.getId());

                    //TODO Notify of Success and update details
                    if (payment.delete()) {
                        Toast.makeText(getContext(), "deleted!", Toast.LENGTH_SHORT).show();
                    }
                    //TODO Add Failure Notifs
                    break;
                default:
                    return;
            }
        }
        if (mListener != null) {
            mListener.onCRUDOperationComplete(true,"Deleted:", selected);
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
        void onSelectionUpdate(int selected);
        void onDetailsRequested(Long id);
        public void onCRUDOperationFailed(String notification);
        public void onCRUDOperationComplete(boolean successful, String message, List<MinifiedRecord> record);
    }

    public void selectionUpdate(int selected) {
        if (mListener != null) {
            mListener.onSelectionUpdate(selected);
        }
    }

    public void onDetailsRequested(Long id) {
        if (mListener != null) {
            mListener.onDetailsRequested(id);
        }
    }

    private void fetchData(int limit) {
        data.clear();
        switch (ENTITY){
            case Helpers.FRAGMENT_LOCATIONS:
                List<Location> results;
                results= Select.from(Location.class).limit(String.valueOf(limit)).list();
                for (Location result : results) {
                    data.add(new MinifiedRecord(result.getId(), result.getSummary()));
                }
                break;
            case Helpers.FRAGMENT_HOUSES:
                List<House> results2;
                results2= Select.from(House.class).limit(String.valueOf(limit)).list();
                for (House result : results2) {
                    data.add(new MinifiedRecord(result.getId(), result.getSummary()));
                }
                break;
            case Helpers.FRAGMENT_TENANTS:
                List<Tenant> results3;
                results3= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq("0")).limit(String.valueOf(limit)).list();
                for (Tenant result : results3) {
                    data.add(new MinifiedRecord(result.getId(), result.getSummary()));
                }
                break;
            case Helpers.FRAGMENT_PAYMENTS:
                List<Payment> results4;
                results4= Select.from(Payment.class).limit(String.valueOf(limit)).list();
                for (Payment result : results4) {
                    data.add(new MinifiedRecord(result.getId(), result.getSummary()));
                }
                break;
            default:
                List<Payment> results5;
                results5= Select.from(Payment.class).limit(String.valueOf(limit)).list();
                for (Payment result : results5) {
                    data.add(new MinifiedRecord(result.getId(), result.getSummary()));
                }
                break;
        }
        listAdapter.notifyDataSetChanged();
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


}
