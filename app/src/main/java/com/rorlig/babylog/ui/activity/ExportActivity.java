package com.rorlig.babylog.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.rorlig.babylog.R;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.otto.ItemsSelectedEvent;
import com.rorlig.babylog.ui.fragment.export.ExportFragment;
import com.rorlig.babylog.ui.fragment.home.HomeFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author gaurav gupta
 */
public class ExportActivity extends InjectableActivity {

    @Inject
    SharedPreferences preferences;
    private String TAG = "ExportActivity";
    @Inject
    Gson gson;
    private ArrayList<ItemModel> logs;

    private EventListener eventListener = new EventListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "savedInstance " + savedInstanceState);

        if (savedInstanceState == null) {

            showFragment(ExportFragment.class, "export_fragment", false);
        } else {
//            TestFragment test = (TestFragment) getSupportFragmentManager().findFragmentByTag("your_fragment_tag");
        }
//        showFragment(ExportFragment.class, "export_fragment", false);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            Log.d(TAG, "action_settings");
            startActivity(new Intent(this, PrefsActivity.class));


            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void showFragment(Class<?> paramClass,String paramString, boolean addToStack ){
        Log.d(TAG, "showFragment for " + paramClass);

        FragmentManager localFragmentManager = getSupportFragmentManager();



        Fragment localFragment = localFragmentManager.findFragmentById(R.id.fragment_container);

        if ((localFragment==null)||(!paramClass.isInstance(localFragment))){
            Log.d(TAG, "adding to back stack ");
            try {
                localFragment = (Fragment)paramClass.newInstance();
                if (addToStack) {
                    localFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, localFragment)
                            .addToBackStack("main_screen_stack")
                            .commit();
                } else {
                    localFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, localFragment)
                            .commit();
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private class EventListener {
        private EventListener() {
        }
    }
}
