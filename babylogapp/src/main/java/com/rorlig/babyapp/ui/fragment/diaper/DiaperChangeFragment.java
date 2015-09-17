package com.rorlig.babyapp.ui.fragment.diaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.model.diaper.DiaperChangeColorType;
import com.rorlig.babyapp.model.diaper.DiaperChangeEnum;
import com.rorlig.babyapp.model.diaper.DiaperChangeTextureType;
import com.rorlig.babyapp.model.diaper.DiaperIncident;
import com.rorlig.babyapp.otto.events.datetime.DateSetEvent;
import com.rorlig.babyapp.otto.events.datetime.TimeSetEvent;
import com.rorlig.babyapp.otto.events.diaper.DiaperLogCreatedEvent;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.parse_dao.DiaperChange;
import com.rorlig.babyapp.ui.fragment.BaseCreateLogFragment;
import com.rorlig.babyapp.ui.fragment.datetime.DatePickerFragment;
import com.rorlig.babyapp.ui.fragment.datetime.TimePickerFragment;
import com.rorlig.babyapp.ui.widget.DateTimeHeaderFragment;
import com.rorlig.babyapp.utils.AppConstants;
import com.rorlig.babyapp.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.rorlig.babyapp.model.diaper.DiaperChangeColorType.COLOR_1;
import static com.rorlig.babyapp.model.diaper.DiaperChangeColorType.COLOR_2;
import static com.rorlig.babyapp.model.diaper.DiaperChangeColorType.COLOR_3;
import static com.rorlig.babyapp.model.diaper.DiaperChangeColorType.COLOR_4;
import static com.rorlig.babyapp.model.diaper.DiaperChangeColorType.COLOR_5;
import static com.rorlig.babyapp.model.diaper.DiaperChangeColorType.COLOR_6;
import static com.rorlig.babyapp.model.diaper.DiaperChangeColorType.COLOR_7;
import static com.rorlig.babyapp.model.diaper.DiaperChangeColorType.COLOR_8;
import static com.rorlig.babyapp.model.diaper.DiaperChangeColorType.NOT_SPECIFIED;

/**
 * Created by rorlig on 7/14/14.
 * fragment class to track of diaper events....
 */
/**
 * Created by rorlig on 7/14/14.
 * fragment class to track of diaper events....
 */
public class DiaperChangeFragment extends BaseCreateLogFragment {

    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.save_btn)
    Button saveBtn;
//
    @InjectView(R.id.two_button_layout)
    LinearLayout editDeleteBtn;

    @InjectView(R.id.notes)
    EditText notes;

    @InjectView(R.id.diaper_incident_type)
    RadioGroup diaperIncidentType;

    @InjectView(R.id.check_diaper_leak)
    RadioButton checkDiaperLeak;

    @InjectView(R.id.check_no_diaper)
    RadioButton checkNoDiaper;

    @InjectView(R.id.poop_type_layout)
    RelativeLayout poopTypeLayout;

    @InjectView(R.id.poop_color_layout)
    RelativeLayout poopColorLayout;

    @InjectView(R.id.poop_density)
    SeekBar poopDensitySeekBar;



    @InjectView(R.id.poop_type_label)
    TextView poopTypeLabel;


    @InjectView(R.id.diaper_change_type)
    RadioGroup diaperChangeType;

    @InjectView(R.id.poopColorRadioGroup)
    RadioGroup diaperChangeColor;


    private String[] poopDensityLabels = new String[]{"Loose", "Seedy", "Chunky","Hard"};


    private String TAG = "DiaperChangeFragment";

    private EventListener eventListener = new EventListener();

    private Calendar currentDateLong;
    private String id;
    private boolean showEditDelete = false;
    private DiaperChange diaperChange;
    private String uuid;

    public DiaperChangeFragment() {
        super("Diaper");
    }

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);





        poopDensitySeekBar.setMax(99);
        poopDensitySeekBar.setProgress(0); // Set it to zero so it will start at the left-most edge
        poopDensitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "progress" + progress);
                int value = progress / 25;
                poopTypeLabel.setText(poopDensityLabels[value]);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });


        diaperChangeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.diaper_wet) {
                    //hide the texture...
                    poopTypeLayout.setVisibility(View.GONE);
                    poopColorLayout.setVisibility(View.GONE);
                } else {
                    //show the texture....
                    poopTypeLayout.setVisibility(View.VISIBLE);
                    poopColorLayout.setVisibility(View.VISIBLE);
                }
            }
        });






        notes.setOnEditorActionListener(doneActionListener);

        if (getArguments()!=null) {
            Log.d(TAG, "arguments are not null");
            id = getArguments().getString("id");
            uuid = getArguments().getString("uuid");
            position = getArguments().getInt("position");
            Log.d(TAG, "id of the object " + id);
            initViews(uuid);
        }





//        getActivity().getActionBar().setDisplayHomeAsUpEnabled(fa);

//        scopedBus.post(new UpNavigationEvent);
    }

    private void initViews(String uuid) {
        Log.d(TAG, "initViews " + uuid);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Diaper");
//        query.fromLocalDatastore();

        query.whereEqualTo("uuid", uuid);

        query.fromLocalDatastore();
//        query.findInBackground();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.d(TAG, "object " + objects + " e " + e);
                if (objects != null) {
                    Log.d(TAG, "objects size " + objects.size());
                    diaperChange = (DiaperChange) objects.get(0);
                    setDiaperChangeType(diaperChange);
                    setDiaperEventTypeVisibility(diaperChange);
                    setDiaperIncidentType(diaperChange);
                    notes.setText(diaperChange.getDiaperChangeNotes());
                    setDateTimeHeader(diaperChange);
                    showEditDelete = true;
                    editDeleteBtn.setVisibility(View.VISIBLE);
                    saveBtn.setVisibility(View.GONE);
                }

            }


        });
    }

    private void setDateTimeHeader(DiaperChange diaperChangeDao) {
        Log.d(TAG, "setDateTimeHeader");
        dateTimeHeader.setDateTime(diaperChangeDao.getLogCreationDate());
    }

    private void setDiaperChangePoopType(DiaperChange diaperChange) {
        switch (diaperChange.getPoopTexture()) {

        }
    }

    private void setDiaperChangePoopColor(DiaperChange diaperChange) {


        switch (diaperChange.getPoopColor()) {
            case "COLOR_1":
                diaperChangeColor.check(R.id.poopcolor1);
                break;
            case "COLOR_2":
                diaperChangeColor.check(R.id.poopcolor2);
                break;
            case "COLOR_3":
                diaperChangeColor.check(R.id.poopcolor3);
                break;
            case "COLOR_4":
                diaperChangeColor.check(R.id.poopcolor4);
                break;
            case "COLOR_5":
                diaperChangeColor.check(R.id.poopcolor5);
                break;
            case "COLOR_6":
                diaperChangeColor.check(R.id.poopcolor6);
                break;
            case "COLOR_7":
                diaperChangeColor.check(R.id.poopcolor7);
                break;
            case "COLOR_8":
                diaperChangeColor.check(R.id.poopcolor8);
                break;
            default:
                diaperChangeColor.clearCheck();




        }
    }

    private void setDiaperChangeType(DiaperChange diaperChange) {
        switch (diaperChange.getDiaperChangeEventType()) {
            case "Poop":
                diaperChangeType.check(R.id.diaper_pop);
                break;
            case "Wet":
                diaperChangeType.check(R.id.diaper_wet);
                break;
            case "Both":
                diaperChangeType.check(R.id.diaper_both);
                break;
        }
    }

    private void setDiaperEventTypeVisibility(DiaperChange diaperChange) {

        if (!diaperChange.getDiaperChangeEventType().equals("Wet")) {
            poopTypeLayout.setVisibility(View.VISIBLE);
            poopColorLayout.setVisibility(View.VISIBLE);
            setDiaperChangePoopColor(diaperChange);
            setDiaperChangePoopType(diaperChange);
            setPoopTexture(diaperChange);
        }
    }


    private void setDiaperIncidentType(DiaperChange diaperChangeDao) {
        switch (diaperChangeDao.getDiaperChangeIncidentType()) {
            case "NONE":
                diaperIncidentType.check(R.id.check_no_incident);
                break;
            case "NO_DIAPER":
                diaperIncidentType.check(R.id.check_no_diaper);
                break;
            case "DIAPER_LEAK":
                diaperIncidentType.check(R.id.check_diaper_leak);
        }
    }

    private void setPoopTexture(DiaperChange diaperChange) {
        switch (diaperChange.getPoopTexture()) {
            case "LOOSE":
                poopDensitySeekBar.setProgress(0);
                break;
            case "SEEDY":
                poopDensitySeekBar.setProgress(25);
                break;
            case "CHUNKY":
                poopDensitySeekBar.setProgress(50);
                break;
            case "HARD":
                poopDensitySeekBar.setProgress(75);
                break;
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.PURPLE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//            case R.id.action_add:
//                createOrEdit();
////                startActivity(new Intent(getActivity(), PrefsActivity.class));
//                return true;
            case R.id.action_add:
                createOrEdit();
                return true;
            case R.id.action_delete:
                onDeleteBtnClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
    * Register to events...
    */
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
        scopedBus.register(eventListener);
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


    @OnClick(R.id.currentTime)
    public void onCurrentTimeClick(){
        Log.d(TAG, "current time clicked");
        showTimePickerDialog();
    }

    @OnClick(R.id.currentDate)
    public void onCurrentDateClick(){
        Log.d(TAG, "current date clicked");
        showDatePickerDialog();
    }

    @OnClick(R.id.save_btn)
    public void onSaveBtnClicked(){
        Log.d(TAG, "save btn clicked");
        createOrEdit();


    }

    @OnClick(R.id.edit_btn)
      public void onEditBtnClicked(){
        Log.d(TAG, "edit btn clicked");
        createOrEdit();
    }

    @OnClick(R.id.delete_btn)
    public void onDeleteBtnClicked(){
        AppUtils.invalidateDiaperChangeCaches(getActivity().getApplicationContext());
        delete(diaperChange);
//        diaperChange.deleteEventually(new DeleteCallback() {
//            @Override
//            public void done(ParseException e) {
//            }
//        });
//        scopedBus.post(new ItemCreatedOrChanged("Diaper"));

//        delete(id);
    }



    public void createOrEdit() {
            DiaperChange tempDiaperChangeObject = createParseObject();
            if (diaperChange!=null) {

                diaperChange.setDiaperChangeNotes(tempDiaperChangeObject.getDiaperChangeNotes());
                diaperChange.setDiaperChangeEventType(tempDiaperChangeObject.getDiaperChangeEventType());
                diaperChange.setLogCreationDate(tempDiaperChangeObject.getLogCreationDate());
                diaperChange.setDiaperChangeIncidentType(tempDiaperChangeObject.getDiaperChangeIncidentType());
                if (!tempDiaperChangeObject.getDiaperChangeEventType().equals("Wet")){
                    diaperChange.setPoopTexture(tempDiaperChangeObject.getPoopTexture());
                    diaperChange.setPoopColor(tempDiaperChangeObject.getPoopColor());
                }
                saveEventually(diaperChange);

            } else {
//                tempDiaperChangeObject.saveEventually();
                saveEventually(tempDiaperChangeObject);
            }



//        closeSoftKeyBoard();


    }

    /*
     * saves the diaper change object locally or the cloud eventually...
     * //todo if the network is not connected ... dismiss the screen..
     */
    private void saveEventually(final DiaperChange diaperChange) {

        Log.d(TAG, "saveEventually");

//        AppUtils.invalidateDiaperChangeCaches(context);
//        AppUtils.invalidateParseCache("Diaper", getActivity());

//        ParseObject.unpinAllInBackground("Diaper", new DeleteCallback() {
//            @Override
//            public void done(ParseException e) {
//                scopedBus.post(new DiaperLogCreatedEvent());
//
//
//                diaperChange.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        Log.d(TAG, "saving locally");
//                    }
//                });
//
//            }
//        });

//        diaperChange.pinInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                Log.d(TAG, "pinning new object");

//        ParseObject.pin
        diaperChange.pinInBackground("Diaper",
                new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d(TAG, "pinning new object");
                diaperChange.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d(TAG, "saving locally");

                    }
                });
            }
        });
//        diaperChange.saveEventually(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                Log.d(TAG, "posting");
////                AppUtils.invalidateParseCache("Diaper", context);
//            }
//        });
        Log.d(TAG, "scoped bus diaper ");
        scopedBus.post(new ItemCreatedOrChanged("Diaper", position));




//        diaperChange.saveEventually();
//                diaperChange.saveEventually(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        Log.d(TAG, "saving locally");
//
////                        AppUtils.invalidateDiaperChangeCaches(context);
////                        AppUtils.invalidateParseCache("Diaper", context);
//                        scopedBus.post(new DiaperLogCreatedEvent());
//
//
//                    }
//                });
//            }
//        });
    }


    private DiaperChange createParseObject(){
        DiaperChange diaperChange = null;
        switch (diaperChangeType.getCheckedRadioButtonId()) {
            case R.id.diaper_wet:
                diaperChange = new DiaperChange(DiaperChangeEnum.WET, null, null, getDiaperIncident(), notes.getText().toString(), dateTimeHeader.getEventTime());
                break;
            case R.id.diaper_both:

                diaperChange = new DiaperChange(DiaperChangeEnum.BOTH, getDiaperChangeTexture(), getDiaperColor(),
                        getDiaperIncident(), notes.getText().toString(),dateTimeHeader.getEventTime() );
                break;
            default:
                diaperChange = new DiaperChange(DiaperChangeEnum.POOP, getDiaperChangeTexture(), getDiaperColor(),
                        getDiaperIncident(), notes.getText().toString(),dateTimeHeader.getEventTime() );
                break;
        }
        return diaperChange;
    }

    private DiaperChangeColorType getDiaperColor() {
        switch (diaperChangeColor.getCheckedRadioButtonId()) {
            case R.id.poopcolor1:
                return COLOR_1;
            case R.id.poopcolor2:
                return COLOR_2;
            case R.id.poopcolor3:
                return COLOR_3;

            case R.id.poopcolor4:
                return COLOR_4;

            case R.id.poopcolor5:
                return COLOR_5;

            case R.id.poopcolor6:
                return COLOR_6;

            case R.id.poopcolor7:
                return COLOR_7;

            case R.id.poopcolor8:
                return COLOR_8;

        }
        return  NOT_SPECIFIED;
    }



    private DiaperIncident getDiaperIncident() {
        switch (diaperIncidentType.getCheckedRadioButtonId()) {
            case R.id.check_diaper_leak:
                return DiaperIncident.DIAPER_LEAK;

            case R.id.check_no_diaper:
                return DiaperIncident.NO_DIAPER;

            default:
                return DiaperIncident.NONE;
        }
    }

    private DiaperChangeTextureType getDiaperChangeTexture() {
        if (poopDensitySeekBar.getProgress()<25) return DiaperChangeTextureType.LOOSE;
        else if (poopDensitySeekBar.getProgress()<50) return DiaperChangeTextureType.SEEDY;
        else if (poopDensitySeekBar.getProgress()<75) return DiaperChangeTextureType.CHUNKY;
        else return DiaperChangeTextureType.HARD;

    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }


    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datepicker");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (!showEditDelete) {
            inflater.inflate(R.menu.menu_add_item, menu);
        } else {
            inflater.inflate(R.menu.menu_edit_delete_item, menu);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diaper_change, null);
        ButterKnife.inject(this, view);
        return view;
    }


    private class EventListener {
        public EventListener() {

        }

        @Subscribe
        public void onDateChanged(DateSetEvent dateSetEvent){
            Log.d(TAG, "dateSetEvent " + dateSetEvent);
//            currentDateLong = dateSetEvent.getCalendar();
//            currentDate.setText(sdf.format(dateSetEvent.getCalendar().getDate()));
        }

        @Subscribe
        public void onTimeChanged(TimeSetEvent timeSetEvent){
            Log.d(TAG, "timeSetEvent " + timeSetEvent);
//            currentTime.setText(timeSetEvent.getHourOfDay() + ":" + timeSetEvent.getMinute() + " "  + (timeSetEvent.getHourOfDay()>11?"PM":"AM"));
        }
    }

    private EditText.OnEditorActionListener doneActionListener = new EditText.OnEditorActionListener(){

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.d(TAG, "onEditorAction view " + v.getText() + " actionId " + actionId + " event " + event);
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createOrEdit();
                return true;
            }
            return false;
        }
    };
}
