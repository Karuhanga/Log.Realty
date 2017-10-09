package ug.karuhanga.logrealty.Activities;

import android.net.Uri;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Data.Payment;
import ug.karuhanga.logrealty.Data.Tenant;
import ug.karuhanga.logrealty.Fragments.DetailedLocation;
import ug.karuhanga.logrealty.Helpers;
import ug.karuhanga.logrealty.R;

public class Details extends AppCompatActivity implements DetailedLocation.OnFragmentInteractionListener, View.OnClickListener {

    private int ENTITY;
    private Long current_id;
    private ImageButton button_left;
    private ImageButton button_right;
    private List<Long> ids= new ArrayList<>();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ItemsStatePagerAdapter mItemsStatePagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ENTITY= getIntent().getIntExtra("entity", Helpers.FRAGMENT_LOCATIONS);
        current_id= getIntent().getLongExtra("id", 1);

        switch (ENTITY){
            case Helpers.FRAGMENT_LOCATIONS:
                List<Location> results1= Select.from(Location.class).list();
                for (Location result : results1) {
                    ids.add(result.getId());
                }
                break;
            case Helpers.FRAGMENT_HOUSES:
                List<House> results2= Select.from(House.class).list();
                for (House result : results2) {
                    ids.add(result.getId());
                }
                break;
            case Helpers.FRAGMENT_TENANTS:
                List<Tenant> results3= Select.from(Tenant.class).list();
                for (Tenant result : results3) {
                    ids.add(result.getId());
                }
            case Helpers.FRAGMENT_PAYMENTS:
                List<Payment> results4= Select.from(Payment.class).list();
                for (Payment result : results4) {
                    ids.add(result.getId());
                }
                break;
            default:
                List<Location> results5= Select.from(Location.class).list();
                for (Location result : results5) {
                    ids.add(result.getId());
                }
                break;
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        button_left= (ImageButton) findViewById(R.id.button_left_details_activity);
        button_right= (ImageButton) findViewById(R.id.button_right_details_activity);

        DisplayMetrics metrics= getResources().getDisplayMetrics();
        int screenWidth= (int) (metrics.widthPixels*0.80);
        int screenHeight= (int) (metrics.heightPixels*0.60);

        getWindow().setLayout(screenWidth, screenHeight);

        setTitle(Helpers.getStringByName(this, "fragment_label_"+String.valueOf(ENTITY)));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mItemsStatePagerAdapter = new ItemsStatePagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mItemsStatePagerAdapter);
        mViewPager.setCurrentItem(ids.indexOf(current_id));

        button_left.setOnClickListener(this);
        button_right.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_locations, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View view) {
        if (view==button_left){
            mViewPager.arrowScroll(View.FOCUS_LEFT);
            return;
        }
        if (view==button_right){
            mViewPager.arrowScroll(View.FOCUS_RIGHT);
            return;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class ItemsStatePagerAdapter extends FragmentStatePagerAdapter {

        public ItemsStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (ENTITY){
                case Helpers.FRAGMENT_LOCATIONS: return DetailedLocation.newInstance(ids.get(position));
                default: return DetailedLocation.newInstance(ids.get(position));
            }

        }

        @Override
        public int getCount() {
            return ids.size();
        }
    }
}
