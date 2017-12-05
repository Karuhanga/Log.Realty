package ug.karuhanga.logrealty.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ug.karuhanga.logrealty.Data.MinifiedRecord;
import ug.karuhanga.logrealty.Data.Tenant;
import ug.karuhanga.logrealty.Listeners.Confirmation;
import ug.karuhanga.logrealty.Listeners.GistInteractionListener;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helpers.FALSE;
import static ug.karuhanga.logrealty.Helpers.getLaterDate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DuePayments.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DuePayments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DuePayments extends Fragment implements Confirmation, GistInteractionListener {

    ListView listView;

    private List<Tenant> defaulters= new ArrayList<>();
    private ListAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public DuePayments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DuePayments.
     */
    // TODO: Rename and change types and number of parameters
    public static DuePayments newInstance() {
        DuePayments fragment = new DuePayments();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date date= getLaterDate(Calendar.getInstance().getTime(), 7);
        //TODO Dynamic Selection with load more capability
        defaulters= Select.from(Tenant.class).where(Condition.prop(NamingHelper.toSQLNameDefault("ex")).eq(FALSE)).and(Condition.prop(NamingHelper.toSQLNameDefault("rentDue")).lt(date.getTime())).list();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.entity_interface_fragment, container, false);
        view.findViewById(R.id.button_entity_interface_load_more).setVisibility(View.GONE);

        listView= (ListView) view.findViewById(R.id.list_view_entity_interface);
        adapter= new ArrayAdapter<>(getContext(), R.layout.list_item_entity_interface, R.id.textView_list_item_entity_interface, defaulters);

        listView.setAdapter(adapter);


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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (!result){
            return;
        }
        performPendingAction();
    }

    private void performPendingAction() {
        //TODO Perform actual confirmed action
        return;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean onEditPressed() {
        return false;
    }

    @Override
    public boolean onDeletePressed() {
        return false;
    }

    @Override
    public List<MinifiedRecord> getCoreData() {
        List<MinifiedRecord> results= new ArrayList<>();
        for (Tenant tenant : defaulters) {
            results.add(new MinifiedRecord(tenant.getId(), tenant.getSummary()));
        }
        return results;
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
