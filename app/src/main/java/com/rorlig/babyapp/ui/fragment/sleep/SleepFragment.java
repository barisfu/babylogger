package com.rorlig.babyapp.ui.fragment.sleep;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.TimePicker;

import com.gc.materialdesign.views.Button;
import com.j256.ormlite.dao.Dao;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.dao.SleepDao;
import com.rorlig.babyapp.db.BabyLoggerORMLiteHelper;
import com.rorlig.babyapp.otto.SleepLogCreated;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.rorlig.babyapp.ui.widget.DateTimeHeaderFragment;

import java.sql.SQLException;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by rorlig on 7/14/14.
 */
public class SleepFragment extends InjectableFragment implements TimePickerDialog.OnTimeSetListener {

    @ForActivity
    @Inject
    Context context;

    private String TAG = "SleepFragment";



//    @InjectView(R.id.sleep_start_time)
//    RelativeLayout dateRangeStart;

    @InjectView(R.id.sleep_hours)
    TextView sleepHours;

    @InjectView(R.id.sleep_minutes)
    TextView sleepMinutes;

    @InjectView(R.id.save_btn)
    Button saveBtn;

    @InjectView(R.id.two_button_layout)
    LinearLayout editDeleteBtn;



    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;

    private DateTimeHeaderFragment dateTimeHeader;
    private boolean minuteEmpty = true;
    private boolean hourEmpty = true;
    private int id = -1;
    private boolean showEditDelete = false;


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        scopedBus.post(new FragmentCreated("Sleep"));


        if (paramBundle!=null) {
//            dateStartHourTextView.setText(paramBundle.getString(START_HOUR));
//            dateStartMinuteTextView.setText(paramBundle.getString(START_MINUTE));
//            durationHourTextView.setText(paramBundle.getString(END_HOUR));
//            durationMinuteTextView.setText(paramBundle.getString(END_MINUTE));
        } else {
            init();
        }

        saveBtn.setEnabled(false);

        setUpTextWatchers();

//        setSpans();

        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.GRAY);

        if (getArguments()!=null) {
            Log.d(TAG, "arguments are not null");
            id = getArguments().getInt("sleep_id");
            initViews(id);
        }

        sleepMinutes.setOnEditorActionListener(doneActionListener);



//        getActivity().getActionBar().setDisplayHomeAsUpEnabled(fa);

//        scopedBus.post(new UpNavigationEvent);
    }

    private void initViews(int id) {
        Log.d(TAG, "initViews " + id);
        try {
            SleepDao sleepDao = babyLoggerORMLiteHelper.getSleepDao().queryForId(id);
            Log.d(TAG, sleepDao.toString());
            int hours = (int) (sleepDao.getDuration()/60);
            int minutes = (int) (sleepDao.getDuration()%60);

            sleepHours.setText(String.valueOf(hours));
            sleepMinutes.setText(String.valueOf(minutes));
            dateTimeHeader.setDateTime(sleepDao.getDate());
            editDeleteBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);
            showEditDelete = true;
//            if (!sleepHours.getText().equals("")){
//                hourEmpty = false;
//            }

            hourEmpty = sleepHours.getText().equals("");
            minuteEmpty = sleepMinutes.getText().equals("");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @OnClick(R.id.save_btn)
    public void onSaveBtnClicked() {
       createOrEdit();
    }

    @OnClick(R.id.edit_btn)
    public void onEditBtnClicked(){
        Log.d(TAG, "edit btn clicked");
        createOrEdit();
    }

    @OnClick(R.id.delete_btn)
    public void onDeleteBtnClicked(){
        Log.d(TAG, "delete btn clicked");

        try {

            if (id!=-1) {
                Log.d(TAG, "deleting it");
//                daoObject.setId(id);
                babyLoggerORMLiteHelper.getSleepDao().deleteById(id);
//                diaperChangeDao.delete(daoObject);
            }
            scopedBus.post(new SleepLogCreated());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sleep, null);
        ButterKnife.inject(this, view);
        return view;
    }




    private void createOrEdit() {
        Dao<SleepDao, Integer> sleepDao;
        SleepDao daoObject;
        try {

            daoObject = new SleepDao(dateTimeHeader.getEventTime(), getDuration(), dateTimeHeader.getEventTime());
            sleepDao = babyLoggerORMLiteHelper.getSleepDao();

            if (id!=-1) {
                Log.d(TAG, "updating it");
                daoObject.setId(id);
                sleepDao.update(daoObject);
            } else {
                Log.d(TAG, "creating it");
                sleepDao.create(daoObject);
            }

            Log.d(TAG, "created objected " + daoObject);
            closeSoftKeyBoard();
            scopedBus.post(new SleepLogCreated());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString(START_HOUR, dateStartHourTextView.getText().toString());
//        outState.putString(START_MINUTE, dateStartMinuteTextView.getText().toString());
//        outState.putString(END_HOUR, durationHourTextView.getText().toString());
//        outState.putString(END_MINUTE, durationMinuteTextView.getText().toString());

    }

    /*
     * Initializes the hour and minute sleep times...
     * @param null
     * @return null
     */
    private void init(){

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


//        dateStartHourTextView.setText(String.format("%02d", hour));
//        dateStartMinuteTextView.setText(String.format("%02d", minute));
//
//        durationHourTextView.setText(String.format("%02d", 0));
////        durationMinuteTextView.setText("hello");
//        durationMinuteTextView.setText(String.format("%02d", 0));




    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }


//    private Date getStartTime() {
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sleepHours.getText().toString()));
//        c.set(Calendar.MINUTE, Integer.parseInt(sleepMinutes.getText().toString()));
//        return c.getTime();
//
//
//    }
//
    private Long getDuration() {
        int hour = sleepHours.getText().toString().equals("")?0:Integer.parseInt(sleepHours.getText().toString());
        int minute = sleepMinutes.getText().toString().equals("")?0:Integer.parseInt(sleepMinutes.getText().toString());
        return Long.valueOf(hour * 60 + minute);

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
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");
        if (!hourEmpty||!minuteEmpty) {
            Log.d(TAG, "disable the action_add");
            menu.findItem(R.id.action_add).setEnabled(true);
            menu.findItem(R.id.action_add).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_save_white));

        } else {
            menu.findItem(R.id.action_add).setEnabled(false);
            menu.findItem(R.id.action_add).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_save_gray));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_add:
                createOrEdit();
                return true;
            case R.id.action_delete:
                onDeleteBtnClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
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


    private void setUpTextWatchers() {

        sleepMinutes.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = sleepMinutes.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = sleepMinutes.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);
//
//                if ((str.length() == 2 && len < str.length())) {
//
//                    Log.d(TAG, "appending .");
//                    //checking length  for backspace.
//                    sleepMinutes.append(".");
//                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
//                }

                if (str.length() > 0) {
//                    saveBtn.setEnabled(true);
                    minuteEmpty = false;
//                    setSaveEnabled();

                } else {
//                    saveBtn.setEnabled(false);
                    minuteEmpty = true;

                }
                setSaveEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });

        sleepHours.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = sleepHours.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = sleepHours.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);


                if (str.length()>0) {
                    hourEmpty = false;

                } else {
//                    saveBtn.setEnabled(false);
                    hourEmpty = true;

                }

                setSaveEnabled();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });




    }

    private void setSaveEnabled() {
            getActivity().invalidateOptionsMenu();
            if (!minuteEmpty || !hourEmpty) {
                saveBtn.setEnabled(true);
                sleepMinutes.setImeOptions(EditorInfo.IME_ACTION_DONE);

            } else {
                saveBtn.setEnabled(false);
                sleepMinutes.setImeOptions(EditorInfo.IME_ACTION_NONE);

            }
    }


}