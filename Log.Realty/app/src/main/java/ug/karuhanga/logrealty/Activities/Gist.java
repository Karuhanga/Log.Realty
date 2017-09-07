package ug.karuhanga.logrealty.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
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

import com.orm.SugarContext;

import ug.karuhanga.logrealty.Fragments.EntityInterface;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.R;


public class Gist extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, EntityInterface.OnFragmentInteractionListener {

    private FloatingActionButton fabShowFabs, fabAdd, fabEdit, fabDelete;
    private int currentFragment;
    private Toolbar toolbar;
    private SearchView searchView;

    private Animation rotate_forward, rotate_backward, open, close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        setContentView(R.layout.gist_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) findViewById(R.id.search_view_gist);
        setSupportActionBar(toolbar);
        currentFragment= Helpers.FRAGMENT_NONE;

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add_stuff);
        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit_stuff);
        fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete_stuff);
        fabShowFabs = (FloatingActionButton) findViewById(R.id.fab_crud_ops);

        fabAdd.setOnClickListener(this);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayFragment(Helpers.FRAGMENT_DUE_PAYMENTS);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (searchView.getVisibility()==View.VISIBLE){
            searchView.startAnimation(close);
            searchView.setVisibility(View.GONE);
            findViewById(R.id.menu_item_search).setVisibility(View.VISIBLE);
        }
        else {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gist, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
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
            startActivityForResult(new Intent(Gist.this, Settings.class), Helpers.RESULT_CODE_SETTINGS);
        }
        else if (id == R.id.menu_item_search){
            doStartSearchStuff();
        }

        return super.onOptionsItemSelected(item);
    }

    private void doStartSearchStuff() {
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
        displayFragment(currentFragment);
        switch (requestCode){
            case Helpers.RESULT_CODE_ADD_PAYMENT:
                if (resultCode==RESULT_OK){
                    Snackbar.make(fabAdd, "Added Payment:\n"+data.getStringExtra("details"), Snackbar.LENGTH_LONG);
                }
                else{
                    Snackbar.make(fabAdd, "Failed :(, please try again", Snackbar.LENGTH_SHORT);
                }
                return;
        case Helpers.RESULT_CODE_SETTINGS:
            if (resultCode==RESULT_OK){
                Snackbar.make(fabAdd, "Changed:\n"+data.getStringExtra("details"), Snackbar.LENGTH_LONG);
            }
            else{
                Snackbar.make(fabAdd, "Failed :(, please try again", Snackbar.LENGTH_SHORT);
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
        EntityInterface fragment;
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();

        EntityInterface storedFragment= (EntityInterface) fragmentManager.findFragmentByTag(String.valueOf(entity));
        fragment= (storedFragment==null)? EntityInterface.newInstance(entity) : storedFragment;

        if (currentFragment==Helpers.FRAGMENT_NONE){
            fragmentTransaction.add(R.id.layout_fragment_holder, fragment);
        }
        else{
            if (fragmentManager.findFragmentByTag(String.valueOf(currentFragment))==null){
                fragmentTransaction.addToBackStack(String.valueOf(currentFragment));
            }
            fragmentTransaction.replace(R.id.layout_fragment_holder, fragment);
        }

        fragmentTransaction.commit();
        currentFragment= entity;
        String label= Helpers.getStringByName(this, "fragment_label_"+String.valueOf(currentFragment));
        if (label==null){
            label= getString(R.string.app_name);
        }

        toolbar.setTitle(label);

        return;
    }

    private void toggleVisibility() {
        if (fabAdd.getVisibility()==View.VISIBLE){
            fabShowFabs.startAnimation(rotate_backward);
            fabAdd.startAnimation(close);
            fabDelete.startAnimation(close);
            fabEdit.startAnimation(close);

            fabShowFabs.setImageResource(R.drawable.icon_edit);
            fabAdd.setVisibility(View.GONE);
            fabDelete.setVisibility(View.GONE);
            fabEdit.setVisibility(View.GONE);
        }
        else{
            fabShowFabs.startAnimation(rotate_forward);
            fabAdd.startAnimation(open);
            fabDelete.startAnimation(open);
            fabEdit.startAnimation(open);

            fabShowFabs.setImageResource(R.drawable.ic_replay_black_24dp);
            fabAdd.setVisibility(View.VISIBLE);
            fabDelete.setVisibility(View.VISIBLE);
            fabEdit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fabShowFabs)){
            toggleVisibility();
        }
        else if (view.equals(fabAdd)){
            commenceAddition();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}