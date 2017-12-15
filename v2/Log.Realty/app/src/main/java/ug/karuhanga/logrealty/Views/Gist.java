//TODO FUTURE: Improve search interface and options
package ug.karuhanga.logrealty.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.orm.SugarContext;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ug.karuhanga.logrealty.Helper;
import ug.karuhanga.logrealty.Models.Listable;
import ug.karuhanga.logrealty.Models.Setting;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.FRAGMENT_DUE_PAYMENTS;
import static ug.karuhanga.logrealty.Helper.REQUEST_CODE_DETAILS;
import static ug.karuhanga.logrealty.Helper.getStringByName;
import static ug.karuhanga.logrealty.Helper.log;


public class Gist extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EntitySummary.OnFragmentInteractionListener, AdapterView.OnItemClickListener {

    //Activity Visual Components
    @BindView(R.id.toolbar_gist) Toolbar toolbar;
    @BindView(R.id.search_view_gist) ConstraintLayout searchView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view_gist) NavigationView navigationView;
    @BindView(R.id.text_view_gist_search) AutoCompleteTextView searchTextView;
    @BindView(R.id.fab_gist_add) View snackBarHoister;

    private Interfaces.GistExternalInterface currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        setContentView(R.layout.gist_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getNavView().setNavigationItemSelectedListener(this);

        setUserName();

        setCurrentFragment(null);
        displayFragment(FRAGMENT_DUE_PAYMENTS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (searchView.getVisibility()==View.VISIBLE){
            closeSearch();
            return;
        }
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_settings) {
            launchSettings();
        }
        else if (id == R.id.menu_item_search){
            startSearchStuff();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_item_rent_due) {
            displayFragment(FRAGMENT_DUE_PAYMENTS);
        } else if (id == R.id.menu_item_locations) {
            displayFragment(Helper.FRAGMENT_LOCATIONS);
        } else if (id == R.id.menu_item_Houses) {
            displayFragment(Helper.FRAGMENT_HOUSES);
        } else if (id == R.id.menu_item_tenants) {
            displayFragment(Helper.FRAGMENT_TENANTS);
        } else if (id == R.id.menu_item_payments) {
            displayFragment(Helper.FRAGMENT_PAYMENTS);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO Find less crude manner of notifying data update
        //TODO find out why data is not being passed
        displayFragment(getCurrentFragment().getType());
        switch (requestCode){
            case Helper.RESULT_CODE_ADD_PAYMENT:
                if (resultCode==RESULT_OK){
                    Snackbar.make(snackBarHoister, "Added Payment:\n"+data.getStringExtra("details"), Snackbar.LENGTH_LONG).show();
                }
                else{
                    if (data!=null){
                        Snackbar.make(snackBarHoister, "Failed :(, please try again", Snackbar.LENGTH_SHORT).show();
                    }
                }
                return;
            default:
                return;
        }

    }

    @OnClick(R.id.fab_gist_add)
    protected void commenceAddition(){
        switch (getCurrentFragment().getType()){
            case FRAGMENT_DUE_PAYMENTS:
                startActivityForResult(new Intent(Gist.this, AddPayment.class), Helper.RESULT_CODE_ADD_PAYMENT);
                return;
            case Helper.FRAGMENT_LOCATIONS:
                startActivityForResult(new Intent(Gist.this, AddLocation.class), Helper.RESULT_CODE_ADD_LOCATION);
                return;
            case Helper.FRAGMENT_HOUSES:
                startActivityForResult(new Intent(Gist.this, AddHouse.class), Helper.RESULT_CODE_ADD_HOUSE);
                return;
            case Helper.FRAGMENT_TENANTS:
                startActivityForResult(new Intent(Gist.this, AddTenant.class), Helper.RESULT_CODE_ADD_TENANT);
                return;
            case Helper.FRAGMENT_PAYMENTS:
                startActivityForResult(new Intent(Gist.this, AddPayment.class), Helper.RESULT_CODE_ADD_PAYMENT);
                return;
            default:
                return;
        }
    }

    @OnClick(R.id.button_gist_close_search)
    public void closeSearch(){
        if (searchView.getVisibility()==View.VISIBLE){
            searchView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.close));
            searchView.setVisibility(View.GONE);
            ((AutoCompleteTextView) searchView.findViewById(R.id.text_view_gist_search)).setText("");
            findViewById(R.id.menu_item_search).setVisibility(View.VISIBLE);
            return;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        closeSearch();
        onDetailsRequested(((Listable) adapterView.getItemAtPosition(i)).getId());
    }

    @Override
    public void onDetailsRequested(Long id) {
        if (getCurrentFragment().getType()==FRAGMENT_DUE_PAYMENTS){
            log("In Gist: Requesting details on due payments!");
        }
        startActivityForResult(new Intent(this, Details.class).putExtra("entity", getCurrentFragment().getType()).putExtra("id", id), REQUEST_CODE_DETAILS);
    }

    private void displayFragment(int entity) {
        //TODO Test Fragment Transitions
        if (searchView.getVisibility()==View.VISIBLE){
            closeSearch();
        }

        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();

        //check if fragment we need is in memory and set the respective fragment
        Fragment fragment= fragmentManager.findFragmentByTag(String.valueOf(entity));
        if (entity==FRAGMENT_DUE_PAYMENTS){
            if (fragment==null){
                fragment= DuePayments.newInstance();
            }
        }
        else {
            if (fragment==null){
                fragment= EntitySummary.newInstance(entity);
            }
        }

        //if there was no fragment, nothing to save, just display
        if (currentFragment==null){
            fragmentTransaction.add(R.id.container_gist, fragment);
        }
        else{
            //something to save, check if it is possibly already saved, to prevent multiple additions
            if (fragmentManager.findFragmentByTag(String.valueOf(getCurrentFragment().getType()))==null){
                fragmentTransaction.addToBackStack(String.valueOf(getCurrentFragment().getType()));
            }
            fragmentTransaction.replace(R.id.container_gist, fragment);
        }

        fragmentTransaction.commit();
        setCurrentFragment((Interfaces.GistExternalInterface) fragment);
        setGistTitle();

        refreshSearchData();
        return;
    }

    private void startSearchStuff() {
        searchTextView.requestFocus();
        searchTextView.setOnItemClickListener(this);
        searchView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.open));
        searchView.setVisibility(View.VISIBLE);
        findViewById(R.id.menu_item_search).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSearchDataReady(){
        refreshSearchData();
    }

    private void refreshSearchData(){
        ArrayAdapter<Listable> adapter;
        try {
            adapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, getCurrentFragment().getCoreData());
        }catch (NullPointerException e){
            return;
        }
        searchTextView.setAdapter(adapter);
        searchTextView.setThreshold(1);
    }


    private String getUserName(){
        List<Setting> userName= Select.from(Setting.class).where(Condition.prop(NamingHelper.toSQLNameDefault("name")).eq(Helper.SETTINGS_EMAIL)).list();
        String name= userName.isEmpty()? getStringByName(this, "email_default") : userName.get(0).getData();
        return name;
    }

    private void setGistTitle(){
        String label= Helper.getStringByName(this, "fragment_label_"+String.valueOf(getCurrentFragment().getType()));
        log(String.valueOf(getCurrentFragment().getType()));
        if (label==null){
            label= getString(R.string.app_name);
        }

        getSupportActionBar().setTitle(label);
    }

    private void setUserName(){
        ((TextView) getNavView().getHeaderView(0).findViewById(R.id.text_view_2_nav)).setText(getUserName());
    }

    private NavigationView getNavView(){
        return navigationView;
    }

    public Interfaces.GistExternalInterface getCurrentFragment() {
        if (currentFragment==null){
            displayFragment(FRAGMENT_DUE_PAYMENTS);
        }
        return currentFragment;
    }

    public void setCurrentFragment(Interfaces.GistExternalInterface fragment) {
        this.currentFragment = fragment;
    }

    private void launchSettings(){
        //TODO Implement settings open up
//        startActivityForResult(new Intent(Gist.this, Settings.class), RESULT_CODE_SETTINGS);
    }
}