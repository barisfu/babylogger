package com.rorlig.babyapp.ui.fragment.feed;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.dao.BaseDao;
import com.rorlig.babyapp.dao.FeedDao;
import com.rorlig.babyapp.otto.FeedItemClickedEvent;
import com.rorlig.babyapp.otto.events.feed.FeedItemCreatedEvent;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.Feed;
import com.rorlig.babyapp.ui.adapter.DateSectionizer;
import com.rorlig.babyapp.ui.adapter.parse.DiaperChangeAdapter;
import com.rorlig.babyapp.ui.adapter.parse.FeedAdapter;
import com.rorlig.babyapp.ui.fragment.BaseCreateLogFragment;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/18/14.
 * @author gaurav gupta
 * history of feeds changes
 */
public class FeedingListFragment extends BaseInjectableListFragment
        implements  AdapterView.OnItemClickListener {
    @ForActivity
    @Inject
    Context context;


    @InjectView(R.id.feedListView)
    ListView feedListView;

    @InjectView(R.id.emptyView)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;




    @InjectView(R.id.add_bottle_feed)
    FloatingActionButton addBottleFeed;

    @InjectView(R.id.add_breast_feed)
    FloatingActionButton addBreastFeed;


    @InjectView(R.id.feed_item_types)
    FloatingActionsMenu floatingActionsMenu;


//    @InjectView(R.id.feed_list_layout)
//    RelativeLayout feedListLayout;


    private int LOADER_ID=3;
    private List<ParseObject> feedList;
    private FeedAdapter feedAdapter;
    private SimpleSectionAdapter<BaseDao> sectionAdapter;


    public FeedingListFragment() {
        super("Feed");
    }


    @OnClick(R.id.add_item)
    public void onFeedEventAdd(){
//        scopedBus.post(new AddFeedEvent());
        showFeedSelectFragment(new FeedSelectFragment(), "feed_select");
//        scopedBus.post(new AddItemEvent(AddItemTypes.FEED_LOG));
    }

    @OnClick(R.id.add_breast_feed)
    public void onAddBreastFeedBtnClicked(){
        collapseFloatingMenuIfOpen();
        scopedBus.post(new AddItemEvent(AddItemTypes.FEED_NURSING));
    }

    @OnClick(R.id.add_bottle_feed)
    public void onAddBottleFeedBtnClicked(){
        collapseFloatingMenuIfOpen();
        scopedBus.post(new AddItemEvent(AddItemTypes.FEED_BOTTLE));
    }

    private void showFeedSelectFragment(FeedSelectFragment feedSelectFragment, String s) {
        feedSelectFragment.show(getActivity().getSupportFragmentManager(),"feed_select");
    }

    Typeface typeface;

    private String TAG = "FeedingListFragment";

    private EventListener eventListener = new EventListener();

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        feedListView.setEmptyView(emptyView);


        scopedBus.post(new FragmentCreated("Feeding List"));

        feedList = new ArrayList<>();

        updateListView();


//        getLoaderManager().initLoader(LOADER_ID, null, this);




    }

    @Override
    protected void setListResults(List<ParseObject> objects) {

        feedList = objects;

        feedAdapter = new FeedAdapter(getActivity(),
                R.layout.list_item_diaper_change, feedList);
        feedAdapter.update(feedList);
        feedListView.setAdapter(feedAdapter);
        if (feedList.size() > 0) {
            feedListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            feedListView.setOnItemClickListener(this);
        } else {
            feedListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_feeding_list, null);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch");
                return true;
            }
        });
        ButterKnife.inject(this, view);
        return view;
    }

    /*
    * Register to events...
    */
    @Override
    public void onStart(){


        super.onStart();
        Log.d(TAG, "onStart");
        scopedBus.register(eventListener);
        collapseFloatingMenuIfOpen();
//        getLoaderManager().restartLoader(LOADER_ID, null, this);

    }

    /*
     * Unregister from events ...
     */
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
        scopedBus.unregister(eventListener);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {

            case R.id.action_add:
                showFeedSelectFragment(new FeedSelectFragment(), "feed_select");

//                scopedBus.post(new AddItemEvent(AddItemTypes.FEED_BOTTLE));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void onBackPressed() {
//        Fragment fragment =getFragmentManager().findFragmentByTag("feeding_stack");
//        if(fragment!=null) {
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.remove(fragment).commit();
//        }
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_feed, menu);
    }

//    /**
//     * Instantiate and return a new Loader for the given ID.
//     *
//     * @param id   The ID whose loader is to be created.
//     * @param args Any arguments supplied by the caller.
//     * @return Return a new Loader instance that is ready to start loading.
//     */
//    @Override
//    public Loader<List<FeedDao>> onCreateLoader(int id, Bundle args) {
//        Log.d(TAG, "create Loader");
//
//        return new FeedLoader(getActivity());
//    }
//
//
//    @Override
//    public void onLoadFinished(Loader<List<FeedDao>> loader, List<FeedDao> data) {
//        Log.d(TAG, "number of diaper changes " + data.size());
//        Log.d(TAG, "loader finished");
//
//        if (data!=null && data.size()>0) {
//            emptyView.setVisibility(View.GONE);
//            feedListView.setVisibility(View.VISIBLE);
//        } else {
//            emptyView.setVisibility(View.VISIBLE);
//            feedListView.setVisibility(View.GONE);
//        }
//        feedList = data;
//
//        feedAdapter = new FeedAdapter(getActivity(), R.layout.list_item_diaper_change, feedList);
//
////        diaperChangeAdapter.update(diaperChangeDaoList);
//
//        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
//                feedAdapter,
//                R.layout.section_header_blue,
//                R.id.title,
//                new DateSectionizer());
//
//        feedListView.setAdapter(sectionAdapter);
//        feedListView.setOnItemClickListener(this);
////        feedListView.setOnClickListener(this);
//
//    }
//
//    /**
//     * Called when a previously created loader is being reset, and thus
//     * making its data unavailable.  The application should at this point
//     * remove any references it has to the Loader's data.
//     *
//     * @param loader The Loader that is being reset.
//     */
//    @Override
//    public void onLoaderReset(Loader<List<FeedDao>> loader) {
//
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick");
        Log.d(TAG, "item clicked at position " + position + " id " + id);
        Feed feed = (Feed) feedListView.getItemAtPosition(position);
//        Log.d(TAG, "feedDao dao " + feedDao);
        collapseFloatingMenuIfOpen();
        scopedBus.post(new FeedItemClickedEvent(feed));
    }


    // event listener to listen to events on the buss
    private class EventListener {
        public EventListener() {

        }

        @Subscribe
        public void onFeedSavedEvent(ItemCreatedOrChanged event) {
            Log.d(TAG, "onFeedSavedEvent");
            updateListView();
//            finish();
//            showFragment(FeedingListFragment.class, "feeding_list",false);

//            Log.d(TAG, "onDiaperLogCreatedEvent");
//            getLoaderManager().restartLoader(LOADER_ID, null, FeedingListFragment.this);
        }


    }

    /*
     * click on anywhere in fragment ...
     */
    public void fragmentClicked(View v) {
       Log.d(TAG, "fragmentClicked");
      collapseFloatingMenuIfOpen();
    }

    /*
     * collapse floating menu if open
     */
    private void collapseFloatingMenuIfOpen() {
        Log.d(TAG, "collapseFloatingMenu  " + floatingActionsMenu.isExpanded());
        if (floatingActionsMenu.isExpanded()) {
            floatingActionsMenu.collapse();
        }
    }


    //collapse the floating menu on back stack popup....
    @Override
    public void onFragmentResume() {
        collapseFloatingMenuIfOpen();
        super.onFragmentResume();
    }




}
