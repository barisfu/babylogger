package com.rorlig.babylog.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.events.growth.GrowthItemCreated;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.otto.events.stats.StatsItemEvent;
import com.rorlig.babylog.scheduler.TypeFaceManager;
import com.rorlig.babylog.ui.fragment.growth.GrowthFragment;
import com.rorlig.babylog.ui.fragment.growth.GrowthListFragment;
import com.rorlig.babylog.ui.fragment.growth.GrowthStatsFragment;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

//import com.rorlig.babylog.common.AppConstants;
//import com.rorlig.babylog.otto.events.filter.DistanceFilterChanged;
//import com.rorlig.babylog.otto.events.filter.FilterChangedEvent;
//import com.rorlig.babylog.otto.events.filter.LocationChangedEvent;
//import com.rorlig.babylog.otto.events.ui.EventSelectedEvent;
//import com.rorlig.babylog.otto.events.ui.MenuItemSelectedEvent;
//import com.rorlig.babylog.otto.events.ui.TimeFilterChanged;
//import com.rorlig.babylog.ui.fragment.ConnectionsFragment;
//import com.rorlig.babylog.ui.fragment.LoggingFragment;
//import com.rorlig.babylog.ui.fragment.MessagesFragment;
//import com.rorlig.babylog.ui.fragment.MyEventsFragment;

/**
 * Created by admin on 12/15/13.
 */
public class GrowthActivity extends InjectableActivity {

    //todo still figure out left + right toggle speeds...


    @Inject
    ActionBar actionBar;

    @Inject
    Gson gson;

    @Inject
    TypeFaceManager typeFaceManager;




    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String TAG  = "GrowthActivity";

    private EventListener eventListener = new EventListener();


    /*
   * Define a request code to send to Google Play services
   * This code is returned in Activity.onActivityResult
   */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        String intentString = getIntent().getStringExtra("intent");

        Log.d(TAG, " " +  intentString);

//        if (intentString!=null && intentString.equals("growth_activity")){
//            showFragment(GrowthFragment.class, "growth_fragment", false);
//
//        } else  {
            showFragment(GrowthListFragment.class, "growth_list_fragment", false);

//        }

//        showFragment(GrowthListFragment.class, "growth_fragment", false);
//        } else {
//
//        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the option menu items..
//        getMenuInflater().inflate(R.menu.menu_add_item, menu);

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        switch (item.getItemId()){

            case R.id.action_licenses:
                break;


        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    /*
     * Class to swap fragments in and out
     */

    private void showFragment(Class<?> paramClass, String paramString, boolean addToBackStack){
        Log.d(TAG, "showFragment for " + paramClass);

        FragmentManager localFragmentManager = getSupportFragmentManager();



        Fragment localFragment = localFragmentManager.findFragmentById(R.id.fragment_container);

        if ((localFragment==null)||(!paramClass.isInstance(localFragment))){
            try {
                Log.d(TAG, "replacing fragments");

                if (addToBackStack) {

                    localFragment = (Fragment)paramClass.newInstance();
                    localFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, localFragment)
                            .addToBackStack("growth_stack")
                            .commit();

                } else {
                    localFragment = (Fragment)paramClass.newInstance();
                    localFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, localFragment)
                            .commitAllowingStateLoss();
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onStart(){
        super.onStart();
        scopedBus.register(eventListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        scopedBus.unregister(eventListener);
    }

    private void startActivity(Class<?> paramClass,String paramString){
        Intent intent = new Intent(this, paramClass);
        startActivity(intent);
    }

    private class EventListener {
        private EventListener(){
        }

        @Subscribe
        public void onItemAddedEvent(AddItemEvent addItemEvent){
            Log.d(TAG, "onItemAddedEvent");
            switch (addItemEvent.getItemType()) {
                case GROWTH_LOG:
                    showFragment(GrowthFragment.class, "growth_fragment", true);
                    break;
            }
        }

        @Subscribe
        public void onGrowthItemCreated(GrowthItemCreated event) {
//            Log.d(TAG, "onFeedSavedEvent");
//            finish();
            showFragment(GrowthListFragment.class, "growth_list_fragment",true);
        }

        @Subscribe
        public void onStatsItemEvent(StatsItemEvent statsItemEvent) {
                    showFragment(GrowthStatsFragment.class, "growth_stats_fragment", true);
        }



    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving event list");
    }



}
