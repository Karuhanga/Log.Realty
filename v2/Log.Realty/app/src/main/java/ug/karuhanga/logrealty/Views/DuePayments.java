package ug.karuhanga.logrealty.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ug.karuhanga.logrealty.Models.Listable;
import ug.karuhanga.logrealty.Models.Tenant;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.EX;
import static ug.karuhanga.logrealty.Helper.FALSE;
import static ug.karuhanga.logrealty.Helper.FRAGMENT_DUE_PAYMENTS;
import static ug.karuhanga.logrealty.Helper.getLaterDateByDays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link DuePayments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DuePayments extends Fragment implements Interfaces.GistExternalInterface {

    @BindView(R.id.list_view_entity_interface) ListView listView;
    Button buttonLoadMore;

    private List<Listable> defaulters= new ArrayList<>();
    private Unbinder unbinder;
    private int displayNumber;

    public DuePayments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DuePayments.
     */
    public static DuePayments newInstance() {
        return new DuePayments();
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
        unbinder= ButterKnife.bind(this, view);

        setupLoadMoreButton();
        fetchData(10);
        displayNumber= defaulters.size();
        if (displayNumber<10){
            listView.removeFooterView(buttonLoadMore);
        }
        updateAdapter();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public int getType() {
        return FRAGMENT_DUE_PAYMENTS;
    }

    @Override
    public List<Listable> getCoreData() {
        return defaulters;
    }

    private void fetchData(int length){
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date date= getLaterDateByDays(calendar.getTime(), length);
        defaulters.clear();
        List<Tenant> tenants= Select.from(Tenant.class).limit(String.valueOf(length)).where(Condition.prop(NamingHelper.toSQLNameDefault(EX)).eq(FALSE)).and(Condition.prop(NamingHelper.toSQLNameDefault("rentDue")).lt(date.getTime())).list();
        for (Tenant tenant : tenants) {
            defaulters.add(tenant);
        }
    }

    private void updateAdapter(){
        ListAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.entity_summary_list_item, R.id.textView_list_item_entity_interface, defaulters);
        listView.setAdapter(adapter);
    }

    @OnClick(R.id.button_entity_interface_load_more)
    void addData(){
        displayNumber+=5;
        fetchData(displayNumber);
        if (defaulters.size()<displayNumber){
            listView.removeFooterView(buttonLoadMore);
        }
        updateAdapter();
    }

    private void setupLoadMoreButton() {
        buttonLoadMore= new Button(getContext());
        buttonLoadMore.setText(R.string.load_more);
        buttonLoadMore.setAllCaps(false);
        buttonLoadMore.setTypeface(buttonLoadMore.getTypeface(), Typeface.BOLD_ITALIC);
        buttonLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });
        listView.addFooterView(buttonLoadMore);
    }
}
