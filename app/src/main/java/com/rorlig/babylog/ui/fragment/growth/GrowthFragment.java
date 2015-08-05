package com.rorlig.babylog.ui.fragment.growth;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.j256.ormlite.dao.Dao;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.FeedDao;
import com.rorlig.babylog.dao.GrowthDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.model.feed.FeedType;
import com.rorlig.babylog.otto.events.feed.FeedItemCreatedEvent;
import com.rorlig.babylog.otto.events.growth.GrowthItemCreated;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.widget.DateTimeHeaderFragment;

import java.sql.SQLException;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by gaurav
 * Growth element..
 */
public class GrowthFragment extends InjectableFragment {

    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.notes)
    EditText notesContentTextView;

    @InjectView(R.id.save_btn)
    Button saveBtn;

//    @InjectView(R.id.weight_pounds)
//    EditText weightPoundsEditText;

    @InjectView(R.id.weight_pounds_ounces)
    EditText weightEditText;

    @InjectView(R.id.height_inches)
    EditText heightInchesEditText;

    @InjectView(R.id.head_inches)
    EditText headInchesEditText;

    @InjectView(R.id.notes)
    EditText notes;

    @InjectView(R.id.two_button_layout)
    LinearLayout editDeleteBtn;



    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;

    DateTimeHeaderFragment dateTimeHeader;

    private String TAG = "GrowthFragment";

    private EventListener eventListener = new EventListener();
    private boolean heightEmpty = true;
    private boolean weightEmpty = true;
    private boolean headMeasureEmpty = true;
    private int id = -1;



    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Growth Fragment"));

        saveBtn.setEnabled(false);



        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));
        Log.d(TAG, " green color " + Integer.toString(R.color.primary_green, 16));
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.GREEN);

        notes.setOnEditorActionListener(doneActionListener);


        //initialize views if not creating new feed item
        if (getArguments()!=null) {
            Log.d(TAG, "arguments are not null");
            id = getArguments().getInt("growth_id");
            initViews(id);
        }

        setUpTextWatchers();


    }

    private void initViews(int id) {

        Log.d(TAG, "initViews " + id);
        try {
            GrowthDao growthDao = babyLoggerORMLiteHelper.getGrowthDao().queryForId(id);
            Log.d(TAG, growthDao.toString());
            editDeleteBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);

            //convert weight to pound.ounces

//            String.val
            weightEditText.setText(convertWeightToString(growthDao.getWeight()));
            heightInchesEditText.setText(growthDao.getHeight().toString());
            headInchesEditText.setText(growthDao.getHeadMeasurement().toString());
            notes.setText(growthDao.getNotes());
            dateTimeHeader.setDateTime(growthDao.getDate());



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /*
     * helper method to convert weight (double) into string (pound.ounces)
     * @param Double- weight
     * @return String - value is pound.ounces..
     */
    private String convertWeightToString(Double weight) {
        Double decimalWeight = weight - Math.floor(weight);
        int ounces = (int) ((weight - Math.floor(weight))*16);
        Log.d(TAG, "decimalWeight " + decimalWeight);
        int poundWeight = (int) (weight - decimalWeight);
        Log.d(TAG, "poundWeight" + poundWeight);
        return (Integer.toString(poundWeight) + "." + Integer.toString(ounces));

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

        weightEditText.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = weightEditText.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = weightEditText.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

                if ((str.length() == 2 && len < str.length())) {

                    Log.d(TAG, "appending .");
                    //checking length  for backspace.
                    weightEditText.append(".");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }

                if (str.length() > 0) {
//                    saveBtn.setEnabled(true);
                    weightEmpty = false;
                    setSaveEnabled();

                } else {
                    saveBtn.setEnabled(false);
                    weightEmpty = true;

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });

        heightInchesEditText.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = heightInchesEditText.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = heightInchesEditText.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

                if ((str.length() == 2 && len < str.length())) {

                    Log.d(TAG, "appending .");
                    //checking length  for backspace.
                    heightInchesEditText.append(".");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }

                if (str.length()>0) {
//                    saveBtn.setEnabled(true);
                    heightEmpty = false;
                    setSaveEnabled();

                } else {
                    saveBtn.setEnabled(false);
                    heightEmpty = true;

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });


        headInchesEditText.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = headInchesEditText.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = headInchesEditText.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

                if ((str.length() == 2 && len < str.length())) {

                    Log.d(TAG, "appending .");
                    //checking length  for backspace.
                    headInchesEditText.append(".");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }

                if (str.length()>0) {

//                    saveBtn.setEnabled(true);
                    headMeasureEmpty = false;
                    setSaveEnabled();
                } else {
                    saveBtn.setEnabled(false);
                    headMeasureEmpty = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });


    }
    private void setSaveEnabled() {
        if (!weightEmpty && !heightEmpty) {
            saveBtn.setEnabled(true);
            notes.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else {
            saveBtn.setEnabled(false);
            notes.setImeOptions(EditorInfo.IME_ACTION_NONE);

        }

    }


    @OnClick(R.id.save_btn)
    public void saveBtnClicked() {

        createOrEdit();
//        Dao<GrowthDao, Integer> growthDao;
//        GrowthDao daoObject;
//        Date date = dateTimeHeader.getEventTime();
//
//        String weight = weightEditText.getText().toString();
//
//
//
//
//        Integer weightPounds = Integer.parseInt(weight.substring(0, weight.indexOf(".")));
//
//        Double totalWeight =  weightPounds.doubleValue();
//
//
//        if (weight.length()>3) {
//            Integer weightOunces = Integer.parseInt(weight.substring(3));
//            totalWeight+=weightOunces.doubleValue()/16;
//        }
//
//
//
//        Double height  = Double.parseDouble(heightInchesEditText.getText().toString());
//
//        Double headMeasure = Double.parseDouble(headInchesEditText.getText().toString());
//
//
//
//
//        try {
//            growthDao = babyLoggerORMLiteHelper.getGrowthDao();
//
//
//            daoObject  = new GrowthDao(totalWeight, height, headMeasure, notesContentTextView.getText().toString(), date);
//            growthDao.create(daoObject);
////            feedDao.create(daoObject);
//            Log.d(TAG, "created objected " + daoObject);
//            scopedBus.post(new GrowthItemCreated());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public BabyLoggerORMLiteHelper getBabyLoggerORMLiteHelper() {
        return babyLoggerORMLiteHelper;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_growth, null);
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
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }

    private class EventListener {
        public EventListener() {

        }
    }


    /*
   * creates a temporary growth item from the local view values...
   */
    private GrowthDao createLocalGrowthDao() {

        Date date = dateTimeHeader.getEventTime();

        String weight = weightEditText.getText().toString();


        int indexOfDot = weight.indexOf(".");

        Integer weightPounds = Integer.parseInt(weight.substring(0, indexOfDot==-1? weight.length(): indexOfDot));

        Double totalWeight =  weightPounds.doubleValue();


        if (weight.length()>3) {
            Integer weightOunces = Integer.parseInt(weight.substring(3));
            totalWeight+=weightOunces.doubleValue()/16;
        }



        Double height  = Double.parseDouble(heightInchesEditText.getText().toString());

        Double headMeasure = Double.parseDouble(headInchesEditText.getText().toString());

        return new GrowthDao(totalWeight, height, headMeasure, notesContentTextView.getText().toString(), date);
    }

    @OnClick(R.id.edit_btn)
    public void onEditBtnClicked(){
        Log.d(TAG, "edit btn clicked");
        createOrEdit();
    }

    /*
     */
    private void createOrEdit() {
        Dao<GrowthDao, Integer> growthDao;
        try {

            growthDao = createGrowthDao();
            GrowthDao daoObject = createLocalGrowthDao();

            if (daoObject!=null) {
                if (id!=-1) {
                    Log.d(TAG, "updating it");
                    daoObject.setId(id);
                    growthDao.update(daoObject);
                } else {
                    Log.d(TAG, "creating it");
                    growthDao.create(daoObject);
                }

                Log.d(TAG, "created objected " + daoObject);
                scopedBus.post(new GrowthItemCreated());
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * deletes the feed item...
     */
    @OnClick(R.id.delete_btn)
    public void onDeleteBtnClicked(){
        Log.d(TAG, "delete btn clicked");
        Dao<GrowthDao, Integer> daoObject;

        try {

            daoObject = createGrowthDao();

            if (id!=-1) {
                Log.d(TAG, "updating it");
                daoObject.deleteById(id);
            }
            scopedBus.post(new GrowthItemCreated());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Dao<GrowthDao, Integer> createGrowthDao() throws SQLException {
        return babyLoggerORMLiteHelper.getGrowthDao();
    }
}
