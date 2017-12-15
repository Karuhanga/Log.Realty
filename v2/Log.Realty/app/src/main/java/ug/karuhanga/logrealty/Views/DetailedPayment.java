package ug.karuhanga.logrealty.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ug.karuhanga.logrealty.Controllers.Controller;
import ug.karuhanga.logrealty.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailedPayment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailedPayment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedPayment extends Fragment implements Controller.DetailedPaymentControllerExternalInterface {

    @BindView(R.id.text_view_detailed_payment_tenant) TextView textViewTenant;
    @BindView(R.id.text_view_detailed_payment_date) TextView textViewDate;
    @BindView(R.id.text_view_detailed_payment_rate) TextView textViewRate;
    @BindView(R.id.text_view_detailed_payment_amount) TextView textViewAmount;

    private OnFragmentInteractionListener mListener;
    private DetailedPaymentActivityExternalInterface controller;
    private Unbinder unbinder;

    public DetailedPayment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailedPayment.
     */
    public static DetailedPayment newInstance(Long id) {
        DetailedPayment fragment = new DetailedPayment();
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
        controller= Controller.injectDetailedPaymentActivityExternalInterface(this);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            controller.setPayment(getArguments().getLong("id"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.detailed_payment_fragment, container, false);
        unbinder= ButterKnife.bind(this, view);

        textViewTenant.setText(controller.getTenant());
        textViewAmount.setText(controller.getAmount());
        textViewDate.setText(controller.getDate());
        textViewRate.setText(controller.getRate());

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
     * UI Interaction Handlers
     */

    @OnClick(R.id.fab_detailed_payment_delete)
    public void onFabDeleteClick() {
        deletePayment();
    }

    /**
     * External Interaction Handlers
     */

    @Override
    public Context requestContext() {
        return mListener.requestContext();
    }

    interface OnFragmentInteractionListener {
        void onItemDeleted(Long id);

        Context requestContext();
    }

    public interface DetailedPaymentActivityExternalInterface{

        void setPayment(long id);

        String getTenant();

        void deletePayment();

        String getAmount();

        String getDate();

        String getRate();
    }

    /**
     * Major Event Starters
     */

    private void deletePayment() {
        getController().deletePayment();
    }

    /**
     * Post the fact methods
     */

    @Override
    public void onPaymentDeleted(long id) {
        mListener.onItemDeleted(id);
    }


    /**
     * Getters and setters
     */

    public DetailedPaymentActivityExternalInterface getController() {
        if (controller==null){
            setController();
        }
        return controller;
    }

    public void setController() {
        this.controller = Controller.injectDetailedPaymentActivityExternalInterface(this);
    }
}
