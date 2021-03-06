package ug.karuhanga.logrealty.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarContext;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Data.MinifiedRecord;
import ug.karuhanga.logrealty.Data.Setting;
import ug.karuhanga.logrealty.Fragments.DuePayments;
import ug.karuhanga.logrealty.Fragments.EntityInterface;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.Listeners.GistInteractionListener;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helpers.ACTION_ADD;
import static ug.karuhanga.logrealty.Helpers.ACTION_DELETE;
import static ug.karuhanga.logrealty.Helpers.ACTION_SHOW;
import static ug.karuhanga.logrealty.Helpers.FRAGMENT_DUE_PAYMENTS;
import static ug.karuhanga.logrealty.Helpers.REQUEST_CODE_DETAILS;
import static ug.karuhanga.logrealty.Helpers.RESULT_CODE_REFRESH;
import static ug.karuhanga.logrealty.Helpers.RESULT_CODE_SETTINGS;
import static ug.karuhanga.logrealty.Helpers.getStringByName;


public class Gist extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, EntityInterface.OnFragmentInteractionListener, AdapterView.OnItemClickListener {

    private FloatingActionButton fabShowFabs, fabEdit, fabDelete;
    private int currentFragment;
    private Fragment currentFrag;
    private Toolbar toolbar;
    private ConstraintLayout searchView;
    private int selected;
    List<MinifiedRecord> coreData;

    private Animation rotate_forward, rotate_backward, open, close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        setContentView(R.layout.gist_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (ConstraintLayout) findViewById(R.id.search_view_gist);
        setSupportActionBar(toolbar);
        currentFragment= Helpers.FRAGMENT_NONE;
        selected= 0;

        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit_stuff);
        fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete_stuff);
        fabShowFabs = (FloatingActionButton) findViewById(R.id.fab_crud_ops);

        fabEdit.setOnClickListener(this);
        fabDelete.setOnClickListener(this);
        fabShowFabs.setOnClickListener(this);

        rotate_forward= AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotate_backward= AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        open= AnimationUtils.loadAnimation(this, R.anim.fab_open);
        close= AnimationUtils.loadAnimation(this, R.anim.fab_close);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_gist);
        navigationView.setNavigationItemSelectedListener(this);
        List<Setting> ewrp= Select.from(Setting.class).where(Condition.prop(NamingHelper.toSQLNameDefault("name")).eq(Helpers.SETTINGS_EMAIL)).list();
        String dxrs= ewrp.isEmpty()? getStringByName(this, "email_default") : ewrp.get(0).getData();
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.text_view_2_nav)).setText(dxrs);

        displayFragment(Helpers.FRAGMENT_DUE_PAYMENTS);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (searchView.getVisibility()==View.VISIBLE){
            closeSearch(null);
            return;
        }

        if (currentFrag instanceof GistInteractionListener){
            if (((GistInteractionListener) currentFrag).onBackPressed()){
                return;
            }
        }
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        super.onBackPressed();
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
            startActivityForResult(new Intent(Gist.this, Settings.class), RESULT_CODE_SETTINGS);
        }
        else if (id == R.id.menu_item_search){
            doStartSearchStuff();
        }

        return super.onOptionsItemSelected(item);
    }

    private void doStartSearchStuff() {
        coreData= ((GistInteractionListener) currentFrag).getCoreData();
        ArrayAdapter<MinifiedRecord> adapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, coreData);
        AutoCompleteTextView search_text_view= ((AutoCompleteTextView) searchView.findViewById(R.id.text_view_gist_search));
        search_text_view.setAdapter(adapter);
        search_text_view.setThreshold(1);
        search_text_view.requestFocus();
        search_text_view.setOnItemClickListener(this);
        searchView.startAnimation(open);
        searchView.setVisibility(View.VISIBLE);
        findViewById(R.id.menu_item_search).setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_item_rent_due) {
            displayFragment(Helpers.FRAGMENT_DUE_PAYMENTS);
        } else if (id == R.id.menu_item_locations) {
            displayFragment(Helpers.FRAGMENT_LOCATIONS);
        } else if (id == R.id.menu_item_Houses) {
            displayFragment(Helpers.FRAGMENT_HOUSES);
        } else if (id == R.id.menu_item_tenants) {
            displayFragment(Helpers.FRAGMENT_TENANTS);
        } else if (id == R.id.menu_item_payments) {
            displayFragment(Helpers.FRAGMENT_PAYMENTS);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO Find less crude manner of notifying data update
        //TODO find out why data is not being passed
        displayFragment(currentFragment);
        switch (requestCode){
            case Helpers.RESULT_CODE_ADD_PAYMENT:
                if (resultCode==RESULT_OK){
                    Snackbar.make(fabShowFabs, "Added Payment:\n"+data.getStringExtra("details"), Snackbar.LENGTH_LONG).show();
                }
                else{
                    if (data!=null){
                        Snackbar.make(fabShowFabs, "Failed :(, please try again", Snackbar.LENGTH_SHORT).show();
                    }
                }
                return;
            case RESULT_CODE_SETTINGS:
                if (resultCode==RESULT_OK){
                    Snackbar.make(fabShowFabs, "Changed:\n"+data.getStringExtra("details"), Snackbar.LENGTH_LONG);
                }
                else{
                    Snackbar.make(fabShowFabs, "Failed :(, please try again", Snackbar.LENGTH_SHORT);
                }
                return;
            case REQUEST_CODE_DETAILS:
                if (resultCode==RESULT_CODE_REFRESH){
                    displayFragment(currentFragment);
                }
                return;

            default:
                return;
        }

    }

    protected void commenceAddition(){
        switch (currentFragment){
            case Helpers.FRAGMENT_DUE_PAYMENTS:
                startActivityForResult(new Intent(Gist.this, AddPayment.class), Helpers.RESULT_CODE_ADD_PAYMENT);
                return;
            case Helpers.FRAGMENT_LOCATIONS:
                startActivityForResult(new Intent(Gist.this, AddLocation.class), Helpers.RESULT_CODE_ADD_LOCATION);
                return;
            case Helpers.FRAGMENT_HOUSES:
                startActivityForResult(new Intent(Gist.this, AddHouse.class), Helpers.RESULT_CODE_ADD_HOUSE);
                return;
            case Helpers.FRAGMENT_TENANTS:
                startActivityForResult(new Intent(Gist.this, AddTenant.class), Helpers.RESULT_CODE_ADD_TENANT);
                return;
            case Helpers.FRAGMENT_PAYMENTS:
                startActivityForResult(new Intent(Gist.this, AddPayment.class), Helpers.RESULT_CODE_ADD_PAYMENT);
                return;
            default:
                return;
        }
    }

    private void displayFragment(int entity) {
        //TODO Test Fragment Transitions
        onSelectionUpdate(0);

        if (searchView.getVisibility()==View.VISIBLE){
            closeSearch(null);
            return;
        }

        Fragment fragment;
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();

        //check if fragment we need is in memory and set the respective fragment
        Fragment storedFragment= fragmentManager.findFragmentByTag(String.valueOf(entity));
        if (entity==FRAGMENT_DUE_PAYMENTS){
            fragment= (storedFragment == null) ? DuePayments.newInstance() : storedFragment;
        }
        else {
            fragment = (storedFragment == null) ? EntityInterface.newInstance(entity) : storedFragment;
        }


        //if there was no fragment, nothing to save, just display
        if (currentFragment==Helpers.FRAGMENT_NONE){
            fragmentTransaction.add(R.id.container_gist, fragment);
        }
        else{
            //something to save, check if it is possibly already saved, to prevent multiple additions
            if (fragmentManager.findFragmentByTag(String.valueOf(currentFragment))==null){
                fragmentTransaction.addToBackStack(String.valueOf(currentFragment));
            }
            fragmentTransaction.replace(R.id.container_gist, fragment);
        }

        fragmentTransaction.commit();
        currentFragment= entity;
        currentFrag= fragment;
        String label= Helpers.getStringByName(this, "fragment_label_"+String.valueOf(currentFragment));
        if (label==null){
            label= getString(R.string.app_name);
        }

        toolbar.setTitle(label);
        selected= 0;
        return;
    }

    private void toggleFabVisibility() {
        if (fabEdit.getVisibility()==View.VISIBLE){
            fabShowFabs.startAnimation(rotate_backward);
            fabDelete.startAnimation(close);
            fabEdit.startAnimation(close);

            fabShowFabs.setImageResource(R.drawable.icon_edit);
            fabDelete.setVisibility(View.GONE);
            fabEdit.setVisibility(View.GONE);
        }
        else{
            fabShowFabs.startAnimation(rotate_forward);
            fabDelete.startAnimation(open);
            fabEdit.startAnimation(open);

            fabShowFabs.setImageResource(R.drawable.ic_replay_black_24dp);
            fabDelete.setVisibility(View.VISIBLE);
            fabEdit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fabShowFabs)){
            if (currentFragment==Helpers.FRAGMENT_DUE_PAYMENTS || (selected==0)){
                commenceAddition();
                return;
            }

            if (selected>1){
                if (currentFrag instanceof GistInteractionListener){
                    ((GistInteractionListener) currentFrag).onDeletePressed();
                }
                return;
            }

            toggleFabVisibility();
        }
        else if (view.equals(fabDelete)){
            ((GistInteractionListener) currentFrag).onDeletePressed();
            return;
        }
        else if (view==fabEdit){
            ((GistInteractionListener) currentFrag).onEditPressed();
        }
    }

    @Override
    public void onSelectionUpdate(int selected) {
        this.selected= selected;

        switch (selected){
            case 0:
                if (fabEdit.getVisibility()==View.VISIBLE){
                    toggleFabVisibility();
                }
                convertActionFab(ACTION_ADD);
                return;
            case 1:
                convertActionFab(ACTION_SHOW);
                return;
            default:
                if (fabEdit.getVisibility()==View.VISIBLE){
                    toggleFabVisibility();
                }
                convertActionFab(ACTION_DELETE);
        }
    }

    @Override
    public void onDetailsRequested(Long id) {
        startActivityForResult(new Intent(this, Details.class).putExtra("entity", currentFragment).putExtra("id", id), REQUEST_CODE_DETAILS);
    }

    @Override
    public void onCRUDOperationFailed(String notification) {

    }

    @Override
    public void onCRUDOperationComplete(boolean successful, String message, List<MinifiedRecord> records) {
        displayFragment(currentFragment);
        String data= "";
        for (MinifiedRecord record : records) {
            data+= "\n"+record.getDescription();
        }
        Snackbar.make(fabShowFabs, message+": "+data, Snackbar.LENGTH_SHORT).show();
        //TODO Set undo action
    }

    private void convertActionFab(int ACTION){
        switch (ACTION){
            case ACTION_ADD:
                fabShowFabs.setImageResource(R.drawable.ic_add_black_24dp);
                return;
            case ACTION_DELETE:
                fabShowFabs.setImageResource(R.drawable.ic_delete_black_24dp);
                return;
            default:
                fabShowFabs.setImageResource(R.drawable.icon_edit);
        }
    }

    public void closeSearch(View view){
        if (searchView.getVisibility()==View.VISIBLE){
            searchView.startAnimation(close);
            searchView.setVisibility(View.GONE);
            ((AutoCompleteTextView) searchView.findViewById(R.id.text_view_gist_search)).setText("");
            findViewById(R.id.menu_item_search).setVisibility(View.VISIBLE);
            return;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        closeSearch(null);
        onDetailsRequested(((MinifiedRecord) adapterView.getItemAtPosition(i)).getId());
    }
}