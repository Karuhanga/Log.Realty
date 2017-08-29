package ug.karuhanga.logrealty.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.orm.SugarContext;

import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.R;


public class Gist extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private FloatingActionButton fab;
    private int currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        setContentView(R.layout.gist_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab_add_stuff);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayFragment(Helpers.FRAGMENT_DUE_PAYMENTS);
        currentFragment= Helpers.FRAGMENT_DUE_PAYMENTS;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(Gist.this, Settings.class), Helpers.RESULT_CODE_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Helpers.RESULT_CODE_ADD_PAYMENT:
                if (resultCode==RESULT_OK){
                    Snackbar.make(fab, "Added Payment:\n"+data.getStringExtra("details"), Snackbar.LENGTH_LONG);
                }
                else{
                    Snackbar.make(fab, "Failed :(, please try again", Snackbar.LENGTH_SHORT);
                }
        case Helpers.RESULT_CODE_SETTINGS:
            if (resultCode==RESULT_OK){
                Snackbar.make(fab, "Changed:\n"+data.getStringExtra("details"), Snackbar.LENGTH_LONG);
            }
            else{
                Snackbar.make(fab, "Failed :(, please try again", Snackbar.LENGTH_SHORT);
            }
        }

    }

    protected void commenceAddition(){
        switch (currentFragment){
            case Helpers.FRAGMENT_DUE_PAYMENTS:
                startActivityForResult(new Intent(Gist.this, AddPayment.class), Helpers.RESULT_CODE_ADD_PAYMENT);
            default:
                startActivityForResult(new Intent(Gist.this, AddPayment.class), Helpers.RESULT_CODE_ADD_PAYMENT);
        }
    }

    private void displayFragment(int fragment) {
        //TODO Implement Fragment Transitions
        return;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fab)){
            commenceAddition();
        }
    }
}