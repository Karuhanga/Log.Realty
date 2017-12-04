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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Payment;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.Popups.Confirmation;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helpers.REQUEST_CODE_EDIT;
import static ug.karuhanga.logrealty.Helpers.REQUEST_CODE_REPLACE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedPayment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailedPayment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedPayment extends Fragment implements View.OnClickListener, ug.karuhanga.logrealty.Listeners.Confirmation {

    private Long payment;

    private TextView textViewTenant;
    private TextView textViewDate;
    private TextView textViewRate;
    private TextView textViewAmount;
    private FloatingActionButton fab_delete;

    private Payment paymentObject;

    private OnFragmentInteractionListener mListener;

    public DetailedPayment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailedPayment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedPayment newInstance(Long id) {
        DetailedPayment fragment = new DetailedPayment();
        Bundle args = new Bundle();

        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            payment = getArguments().getLong("id");
        }
        else{
            List<Payment> results= Select.from(Payment.class).list();
            if (results.size()>0){
                payment= results.get(0).getId();
            }
            else{
                return;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.detailed_payment_fragment, container, false);

        textViewTenant= view.findViewById(R.id.textView_tenant_detailed_payment);
        textViewAmount= view.findViewById(R.id.textView_amount_detailed_payment);
        textViewDate= view.findViewById(R.id.textView_date_detailed_payment);
        textViewRate= view.findViewById(R.id.textView_rate_detailed_payment);

        fab_delete= view.findViewById(R.id.fab_delete_detailed_payment);

        paymentObject= Payment.findById(Payment.class, payment);

        textViewTenant.setText(paymentObject.getTenant().getName());
        textViewAmount.setText(Helpers.toCurrency(paymentObject.getAmount()));
        textViewDate.setText(Helpers.dateToString(paymentObject.getDate()));
        textViewRate.setText(Helpers.toCurrency(paymentObject.getRate()));

        fab_delete.setOnClickListener(this);

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
    public void onClick(View view) {
        if (view==fab_delete){
            deletePayment();
        }
    }

    private void deletePayment() {
        new Confirmation(getContext(), this, "Are you sure?", "Delete this payment:\n"+paymentObject.getSummary(), R.drawable.ic_delete_black_24dp, "Yes", "Cancel", Helpers.REQUEST_CODE_DELETE).show();
    }

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (result &&(requestCode==Helpers.REQUEST_CODE_DELETE)){
            paymentObject.delete();
            scrollToNext();
        }
    }

    private void scrollToNext() {
        if (mListener != null) {
            mListener.onItemDeleted(payment);
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
}
