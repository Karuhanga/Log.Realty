package ug.karuhanga.logrealty.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ug.karuhanga.logrealty.Controllers.Controller;
import ug.karuhanga.logrealty.Controllers.Controller.DetailsControllerExternalInterface;
import ug.karuhanga.logrealty.R;

import static ug.karuhanga.logrealty.Helper.FRAGMENT_LOCATIONS;
import static ug.karuhanga.logrealty.Helper.getStringByName;


public class Details extends AppCompatActivity implements DetailsControllerExternalInterface, DetailedLocation.OnFragmentInteractionListener, DetailedHouse.OnFragmentInteractionListener, DetailedTenant.OnFragmentInteractionListener, DetailedPayment.OnFragmentInteractionListener {

    @BindView(R.id.button_detailed_activity_left) ImageButton buttonLeft;
    @BindView(R.id.button_detailed_activity_right) ImageButton buttonRight;
    @BindView(R.id.container_detailed_activity) ViewPager mViewPager;
    @BindView(R.id.text_view_detailed_activity_details) TextView textViewDetails;

    private ItemsStatePagerAdapter mItemsStatePagerAdapter;
    private DetailsActivityExternalInterface controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.controller= Controller.injectDetailsActivityExternalInterface(this);
        controller.setEntity(getIntent().getIntExtra("entity", FRAGMENT_LOCATIONS));
        controller.setCurrent(getIntent().getLongExtra("id", 1));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        //Hazy look for arrow buttons
        buttonLeft.setAlpha(0.3f);
        buttonRight.setAlpha(0.3f);

        //Size activity to popup
        DisplayMetrics metrics= getResources().getDisplayMetrics();
        int screenWidth= (int) (metrics.widthPixels*0.85);
        int screenHeight= (int) (metrics.heightPixels*0.60);
        getWindow().setLayout(screenWidth, screenHeight);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mItemsStatePagerAdapter = new ItemsStatePagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mItemsStatePagerAdapter);
        mViewPager.setCurrentItem(controller.getCurrentIndex());
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class ItemsStatePagerAdapter extends FragmentStatePagerAdapter {

        ItemsStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return controller.getItemAtPosition(position);
        }

        @Override
        public int getCount() {
            return controller.getItemCount();
        }
    }

    public interface DetailsActivityExternalInterface{

        void setCurrent(long id);

        void setEntity(int entity);

        int getCurrentIndex();

        Fragment getItemAtPosition(int position);

        int getItemCount();


        void notifyItemDeleted(Long item);

        int getEntity();
    }

    @OnClick({R.id.button_detailed_activity_left, R.id.button_detailed_activity_right})
    public void onSwipe(View view) {
        if (view== buttonLeft){
            mViewPager.arrowScroll(View.FOCUS_LEFT);
            return;
        }
        if (view== buttonRight){
            mViewPager.arrowScroll(View.FOCUS_RIGHT);
        }
    }

    @Override
    public void swipeLeft() {
        mViewPager.arrowScroll(View.FOCUS_LEFT);
    }

    @Override
    public void swipeRight() {
        mViewPager.arrowScroll(View.FOCUS_RIGHT);
    }

    @Override
    public void notifyDataSetChanged() {
        mItemsStatePagerAdapter = new ItemsStatePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mItemsStatePagerAdapter);
    }

    @Override
    public void notifyNoData() {
        buttonLeft.setVisibility(View.INVISIBLE);
        buttonRight.setVisibility(View.INVISIBLE);
        textViewDetails.setVisibility(View.VISIBLE);
        textViewDetails.setText("Please add a few "+ getStringByName(this, "fragment_label_"+String.valueOf(controller.getEntity()))+" to begin");
    }

    public void onItemDeleted(Long item) {
        controller.notifyItemDeleted(item);
    }

    @Override
    public Context requestContext() {
        return this;
    }

    @Override
    public void finish(){
//        Intent finisher= new Intent();
        new Intent();
        super.finish();
    }
}
