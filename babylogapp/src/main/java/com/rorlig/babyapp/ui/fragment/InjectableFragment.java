package com.rorlig.babyapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ObjectGraphActivity;
import com.rorlig.babyapp.otto.ScopedBus;
import com.rorlig.babyapp.ui.activity.InjectableActivity;
import com.rorlig.babyapp.ui.activity.LicenseActivity;
import com.rorlig.babyapp.ui.activity.PrefsActivity;
import com.rorlig.babyapp.ui.activity.TutorialActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by admin on 12/15/13.
 */
public class InjectableFragment extends Fragment {

    private static final String TAG = "InjectableFragment";

    @Inject
    public ScopedBus scopedBus;

    public InjectableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().invalidateOptionsMenu();
//        setHasOptionsMenu(true);


    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.menu_add_item, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
//    }

    @Override
    public void onActivityCreated(Bundle paramBundle)
    {
        FragmentActivity localFragmentActivity = getActivity();
        Log.d(TAG, "localFragmentActivity " + localFragmentActivity);
        Log.d(TAG, "localFragmentActivity is instanceof ObjectGraphActivity "
                    + (localFragmentActivity instanceof ObjectGraphActivity));

        Log.d(TAG, "localFragmentActivity is instanceof InjectableActivity "
                + (localFragmentActivity instanceof InjectableActivity));
//        if ((localFragmentActivity instanceof ObjectGraphActivity)) {
            Log.d(TAG, "Injecting Fragment");
            ((InjectableActivity)localFragmentActivity).inject(this);

        super.onActivityCreated(paramBundle);

//        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                startActivity(new Intent(getActivity(), PrefsActivity.class));
                return true;
            case R.id.action_licenses:
                startActivity(new Intent(getActivity(), LicenseActivity.class));
                return true;

            case R.id.action_tutorial:
                Intent tutorialIntent = new Intent(getActivity(), TutorialActivity.class);
//                Bundle args = new Bundle();
//                args.putBoolean("fromLauncher", false);
                tutorialIntent.putExtra("fromLauncher", false);
                startActivity(tutorialIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * called when the fragment comes back on the top on pop from the fragment stack..
     */
    public void onFragmentResume() {

        Log.d(TAG, "onFragment Resume ");

    }

    /*
   * closes the keyboard
   */
    protected void closeSoftKeyBoard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.soft
        if (getActivity().getCurrentFocus()!=null && getActivity().getCurrentFocus().getWindowToken()!=null) {
            Log.d(TAG, "closing the window");
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

    }



}
