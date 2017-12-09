package ug.karuhanga.logrealty.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ug.karuhanga.logrealty.Models.House;
import ug.karuhanga.logrealty.Models.Listable;
import ug.karuhanga.logrealty.Models.Location;
import ug.karuhanga.logrealty.Models.Payment;
import ug.karuhanga.logrealty.Models.Summarizable;
import ug.karuhanga.logrealty.Models.Tenant;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.FALSE;
import static ug.karuhanga.logrealty.Helper.FRAGMENT_HOUSES;
import static ug.karuhanga.logrealty.Helper.FRAGMENT_LOCATIONS;
import static ug.karuhanga.logrealty.Helper.FRAGMENT_PAYMENTS;
import static ug.karuhanga.logrealty.Helper.FRAGMENT_TENANTS;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntitySummary.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntitySummary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntitySummary extends Fragment implements ListView.OnItemClickListener, ListView.OnItemLongClickListener, Interfaces.GistExternalInterface {
    // TODO: Rename parameter arguments, choose names that match
    //TODO: BETTER FRGAMENT MANAGEMENT

    @BindView(R.id.button_entity_interface_load_more) Button buttonLoadMore;
    @BindView(R.id.list_view_entity_interface) ListView listView;
    private int ENTITY;
    private List<Listable> data= new ArrayList<>();
    private int displayNumber;
    private ArrayAdapter listAdapter;
    private Unbinder unbinder;

    private OnFragmentInteractionListener mListener;

    public EntitySummary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param entity ENTITY.
     * @return A new instance of fragment EntitySummary.
     */
    // TODO: Rename and change types and number of parameters
    public static EntitySummary newInstance(int entity) {
        EntitySummary fragment = new EntitySummary();
        fragment.setENTITY(entity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.entity_summary_fragment, container, false);
        listAdapter= new ArrayAdapter<>(getContext(), R.layout.entity_summary_list_item, R.id.textView_list_item_entity_interface, data);
        unbinder= ButterKnife.bind(this, view);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        displayNumber=10;
        fetchData(displayNumber);
        onSearchDataReady();
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
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        onDetailsRequested(((Listable) adapterView.getItemAtPosition(i)).getId());
        return;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    @Override
    public int getType() {
        return ENTITY;
    }

    @Override
    public List<Listable> getCoreData() {
        return getData();
    }

    private List<Listable> getData(){
        if (data==null || data.size()==0){
            displayNumber= 10;
            fetchData(displayNumber);
        }
        List<Listable> summarizables= new ArrayList<>();
        for (Listable item :data) {
            summarizables.add(new Summarizable(item.getId(), item.summarize(), item.toString()));
        }
        return summarizables;
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
        void onDetailsRequested(Long id);
        void onSearchDataReady();
    }

    public void onDetailsRequested(Long id) {
        if (mListener != null) {
            mListener.onDetailsRequested(id);
        }
    }

    public void onSearchDataReady() {
        if (mListener != null) {
            mListener.onSearchDataReady();
        }
    }

    private void fetchData(int limit) {
        data.clear();
        switch (ENTITY){
            case FRAGMENT_LOCATIONS:
                List<Location> results;
                results= Select.from(Location.class).limit(String.valueOf(limit)).orderBy(NamingHelper.toSQLNameDefault("name")).list();
                for (Location result : results) {
                    data.add(new Summarizable(result.getId(), result.summarize(), result.toString()));
                }
                break;
            case FRAGMENT_HOUSES:
                List<House> results2;
                results2= Select.from(House.class).limit(String.valueOf(limit)).orderBy(NamingHelper.toSQLNameDefault("number")).orderBy(NamingHelper.toSQLNameDefault("location")).list();
                for (House result : results2) {
                    data.add(new Summarizable(result.getId(), result.summarize(), result.toString()));
                }
                break;
            case FRAGMENT_TENANTS:
                List<Tenant> results3;
                results3= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq(FALSE)).limit(String.valueOf(limit)).orderBy(NamingHelper.toSQLNameDefault("rentDue")).list();
                for (Tenant result : results3) {
                    data.add(new Summarizable(result.getId(), result.summarize(), result.toString()));
                }
                break;
            case FRAGMENT_PAYMENTS:
                List<Payment> results4;
                results4= Select.from(Payment.class).limit(String.valueOf(limit)).list();
                for (Payment result : results4) {
                    data.add(new Summarizable(result.getId(), result.summarize(), result.toString()));
                }
                break;
            default:
                List<Payment> results5;
                results5= Select.from(Payment.class).limit(String.valueOf(limit)).list();
                for (Payment result : results5) {
                    data.add(new Summarizable(result.getId(), result.summarize(), result.toString()));
                }
                break;
        }
        getListAdapter().notifyDataSetChanged();
    }

    @OnClick(R.id.button_entity_interface_load_more)
    public void loadMore(){
        displayNumber+=10;
        fetchData(displayNumber);
        int data_amount= data.size();
        if (displayNumber>data_amount){
            displayNumber= data_amount;
            buttonLoadMore.setVisibility(View.GONE);
        }
    }

    public int getENTITY() {
        return ENTITY;
    }

    public void setENTITY(int ENTITY) {
        this.ENTITY = ENTITY;
    }

    public ArrayAdapter getListAdapter() {
        if (listAdapter==null){
            listAdapter= new ArrayAdapter<>(getContext(), R.layout.entity_summary_list_item, R.id.textView_list_item_entity_interface, data);
        }
        return listAdapter;
    }
}
