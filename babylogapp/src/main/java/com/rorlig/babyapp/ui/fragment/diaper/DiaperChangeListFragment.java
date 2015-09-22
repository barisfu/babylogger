package com.rorlig.babyapp.ui.fragment.diaper;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.androflo.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;
import com.rorlig.babyapp.utils.AppConstants;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//import android.widget.Button;

/**
 * Created by rorlig on 7/18/14.
 * @author gaurav gupta
 * history of diaper changes
 */
public class DiaperChangeListFragment extends BaseInjectableListFragment {

    @ForActivity
    @Inject
    Context context;


//    @InjectView(R.id.diaperchangelist)
//    UltimateRecyclerView diaperChangeListView;

    @InjectView(R.id.emptyView)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;


//    @InjectView(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout swipeRefreshLayout;



    @InjectView(R.id.add_item)
    Button btnDiaperChange;

    @InjectView(R.id.add_diaper_item)
    FloatingActionButton btnAddDiaperChange;


//    private List<ParseObject> diaperChangeList = new ArrayList<>();
//    private DiaperChangeAdapter2 diaperChangeAdapter;
//    private SimpleSectionAdapter<BabyLogBaseParseObject> sectionAdapter;


    Typeface typeface;

    private String TAG = "DiaperChangeListFragment";

//    private EventListener eventListener = new EventListener();
    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;


    public DiaperChangeListFragment() {
        super(AppConstants.PARSE_CLASS_DIAPER);
    }
//    public DiaperChangeListFragment(String parseClassName) {
//        super("DiaperChange");
//    }


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        scopedBus.post(new FragmentCreated("Diaper Change List"));
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        baseParseAdapter2 = new DiaperChangeAdapter2(parseObjectList);
//
////        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter(getActivity().getApplicationContext(),
////                R.layout.section_header, R.id.title, baseParseAdapter2, new DateSectionizer());
//
//
////        ultimateRecyclerView.setAdapter(sectionedRecyclerViewAdapter);
//
//        ultimateRecyclerView.setAdapter(baseParseAdapter2);
//
//        ultimateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        ultimateRecyclerView.enableLoadmore();


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diaperchage_list_2, null);
        ButterKnife.inject(this, view);

        return view;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_diaper_change, menu);
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle item selection
//        switch (item.getItemId()) {
////            case R.id.action_add:
////                scopedBus.post(new AddItemEvent(AddItemTypes.DIAPER_CHANGE));
////                return true;
//            case R.id.action_stats:
//                scopedBus.post(new StatsItemEvent());
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "onContextItemSelected");

        return super.onContextItemSelected(item);
    }

    // -- Button clicks ...
    @OnClick(R.id.add_item)
    public void onDiaperChangeClicked(){
//        scopedBus.post(new AddDiaperChangeEvent());
        addDiaperChange();
    }


    @OnClick(R.id.add_diaper_item)
    public void onDiaperChangeBtnClicked(){
//        scopedBus.post(new AddDiaperChangeEvent());
        addDiaperChange();
    }

    private void addDiaperChange(){
        scopedBus.post(new AddItemEvent(AddItemTypes.DIAPER_CHANGE));
    }

    @Override
    protected void setListResults(List<ParseObject> objects) {
        Log.d(TAG, "setListResults");
        super.setListResults(objects);
//        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter(getActivity().getApplicationContext(),
//                R.layout.section_header, R.id.title, baseParseAdapter2, new DateSectionizer());

//        sectionedRecyclerViewAdapter.setSections(objects);


//        sectionedRecyclerViewAdapter.notifyDataSetChanged();
        ultimateRecyclerView.setAdapter(baseParseAdapter2);


    }


    // -- chart data

    // class to handle event clicks
    private class EventListener {
        private EventListener(){
        }

//        @Subscribe
//        public void onDiaperLogCreatedEvent(DiaperLogCreatedEvent event) {
//            Log.d(TAG, "onDiaperLogCreatedEvent");
//            updateListView();
//        }











    }




}
