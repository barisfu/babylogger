package com.rorlig.babyapp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rorlig.babyapp.R;
import com.rorlig.babyapp.model.diaper.DiaperChangeEnum;
import com.rorlig.babyapp.parse_dao.DiaperChange;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
/**
 * @author gaurav gupta
 */
public class DiaperChangeView extends RelativeLayout {


    private final Context context;
    private SimpleDateFormat simpleDateFormat;

    //    @InjectView(R.id.diaperWetChecked)
    ImageView diaperWetChecked;

//    @InjectView(R.id.diaperPoopChecked)
    ImageView diaperPoopChecked;

//    @InjectView(R.id.diaperChangeTime)
    TextView textViewTime;

//    @InjectView(R.id.poopColor)
    ImageView poopColor;

//    @InjectView(R.id.poopTexture)
    TextView textViewPoopTexture;

//    @InjectView(R.id.notesContent)
    TextView notesContent;

//    @InjectView(R.id.incidentDetails)
    TextView incidentDetails;

//    @InjectView(R.id.row2)
    LinearLayout row2;

    protected DiaperChange diaperChange;
    private String TAG = "DiaperChangeView";


//    public ViewHolder(View view){
//
//    }

    public DiaperChangeView(Context context) {
        super(context);
        this.context = context;
        init();




//        ButterKnife.inject(this, view);
//        view.setTag(this);
    }

    private void init() {
//        View v = LayoutInflater.from(context).inflate(R.layout.list_item_diaper_change, this);
//        initializeViews();
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }


    public DiaperChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DiaperChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    protected void onFinishInflate() {
        this.diaperWetChecked = (ImageView) findViewById(R.id.diaperWetChecked);
        this.diaperPoopChecked = (ImageView) findViewById(R.id.diaperPoopChecked);
        this.textViewTime = (TextView) findViewById(R.id.diaperChangeTime);
        this.poopColor = (ImageView) findViewById(R.id.poopColor);
        this.textViewPoopTexture = (TextView) findViewById(R.id.poopTexture);
        this.notesContent = (TextView) findViewById(R.id.notes_content);
        this.incidentDetails = (TextView) findViewById(R.id.incidentDetails);
        this.row2 = (LinearLayout) findViewById(R.id.row2);

    }

    private void initializeViews() {
        this.diaperWetChecked = (ImageView) findViewById(R.id.diaperWetChecked);
        this.diaperPoopChecked = (ImageView) findViewById(R.id.diaperPoopChecked);
        this.textViewTime = (TextView) findViewById(R.id.diaperChangeTime);
        this.poopColor = (ImageView) findViewById(R.id.poopColor);
        this.textViewPoopTexture = (TextView) findViewById(R.id.poopTexture);
        this.notesContent = (TextView) findViewById(R.id.notes_content);
        this.incidentDetails = (TextView) findViewById(R.id.incidentDetails);
        this.row2 = (LinearLayout) findViewById(R.id.row2);
    }


    public void setModel(DiaperChange diaperChange) {
        this.diaperChange = diaperChange;
//        Log.d(TAG, new Gson().toJson(diaperChange));
        bindModel();
    }

    private void bindModel() {
//        Log.d(TAG, diaperChange.toString());
        textViewTime.setText(simpleDateFormat.format(diaperChange.getLogCreationDate()));
        setPoopColor();
        setPoopTexture();
        setDiaperIncidentType();
        setDiaperChangeType();

        notesContent.setText(diaperChange.getDiaperChangeNotes()!=null ? "" + diaperChange.getDiaperChangeNotes(): "");

    }

    private void setDiaperChangeType() {

//        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
//            if (DiaperChange.getDiaperChangeEventType()== DiaperChangeEnum.BOTH) {
//                diaperWetChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
//                diaperPoopChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
//            } else if (DiaperChange.getDiaperChangeEventType()==DiaperChangeEnum.WET){
//                diaperWetChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
//                diaperPoopChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_unselected));
//                row2.setVisibility(View.GONE);
//            } else {
//                diaperWetChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_unselected));
//                diaperPoopChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
//            }
//        } else {

//        Log.d(TAG, "diaperChange Type " + diaperChange.getDiaperChangeEventType());
            if (diaperChange.getDiaperChangeEventType().equals(DiaperChangeEnum.BOTH.toString())) {
                diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
                diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
            } else if (diaperChange.getDiaperChangeEventType().equals(DiaperChangeEnum.WET.toString())){
                diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
                diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_unselected));
                row2.setVisibility(View.GONE);
            } else {
                diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_unselected));
                diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
            }
//        }

    }


    private void setPoopColor() {
        if (diaperChange.getPoopColor()!=null) {
            switch (diaperChange.getPoopColor()) {
                case "COLOR_1":
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_1));
                    break;
                case "COLOR_2":
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_2));
                    break;
                case "COLOR_3":
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_3));
                    break;
                case "COLOR_4":
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_4));
                    break;
                case "COLOR_5":
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_5));
                    break;
                case "COLOR_6":
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_6));
                    break;
                case "COLOR_7":
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_7));
                    break;
                case "COLOR_8":
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_8));
                    break;
                default:
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.white));
                break;



            }
        } else poopColor.setBackgroundColor(context.getResources().getColor(R.color.white));

    }

    private void setPoopTexture() {
        if (diaperChange.getPoopTexture()!=null) {
            textViewPoopTexture.setText(diaperChange.getPoopTexture().toString());
        } else {
            textViewPoopTexture.setText("N/A");
        }
    }

    private void setDiaperIncidentType() {
        if (diaperChange.getDiaperChangeIncidentType()!=null) {
            incidentDetails.setText(diaperChange.getDiaperChangeIncidentType().toString());
        }

    }


}
