package com.rorlig.babylog.ui.adapter;

import android.app.Activity;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dao.MilestonesDao;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.ui.activity.InjectableActivity;
import com.rorlig.babylog.ui.fragment.milestones.Milestones;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

//import com.gc.materialdesign.views.CheckBox;

/**
 * Created by rorlig on 5/29/15.
 */
public class MilestonesItemAdapter extends ArrayAdapter<MilestonesDao> {

    private final String TAG = "LogItemAdapter";
    private final TypedArray iconArr;
    private final Activity context;
    //    private final int[] itemStates;
    private List<MilestonesDao> logListItem;
//    private String[] itemNames = {};


    @Inject
    Bus bus;


    /**
     * Constructor
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     * @param logListItem
     */
    public MilestonesItemAdapter(Activity context, int resource, List<MilestonesDao> logListItem) {
    super(context, R.layout.list_item_home);
        this.context = context;
        this.logListItem = logListItem;

        iconArr = context.getResources().obtainTypedArray(R.array.itemIcons);



        Log.d(TAG, ""  + this.logListItem);
//        itemNames = context.getResources().getStringArray(R.array.items);
//
//        itemStates = context.getResources().getIntArray(R.array.itemStates);
//        int index = 0;
//        for (; index<itemStates.length; ++index) {
//            ItemModel itemModel;
//            Log.d(TAG, "itemState " + itemStates[index] + " item " + itemNames[index]);
//            if (itemStates[index]==1) {
//                Log.d(TAG, "1");
//                 itemModel = new ItemModel(itemNames[index], true);
//                itemModel.setItemChecked(true);
//
//            } else  {
//                itemModel = new ItemModel(itemNames[index], false);
//
//            }
//
////            itemModel = new ItemModel(itemNames[index], false);
//
//            logListItem.add(itemModel);

            ((InjectableActivity)context).inject(this);

//            ++index;
        }


//        Log.d(TAG, "listitem size " + logListItem.length);


    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MilestonesDao item = logListItem.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_milestones, parent, false);
            viewHolder = new ViewHolder(convertView);
            viewHolder.logItemLabel = (TextView) convertView.findViewById(R.id.logItemLabel);
            viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.iconImage);
            viewHolder.completionText = (TextView) convertView.findViewById(R.id.date_completed);



//            viewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(iconArr.getResourceId(position,0)));







//            viewHolder.logItemCheckBox.setEnabled(item.isItemChecked());
            Log.d(TAG, "position " + position + " item " + item);

//            if (item.isItemChecked()) {
//                viewHolder.logItemCheckBox.setEnabled(false);
//             }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (item.isCompleted()) {
            viewHolder.itemImage.setImageResource(R.drawable.ic_mood_blue);
        } else  {
            viewHolder.itemImage.setImageResource(R.drawable.ic_mood_black);
        }
        viewHolder.logItemLabel.setText(item.getTitle());
        viewHolder.completionText.setText(item.getCompletionDateRange());

                // Populate the data into the template view using the data object

//        if (item.isItemChecked()) {
//            viewHolder.logItemCheckBox.setEnabled(false);
//        }
//        viewHolder.logItemCheckBox.setText(item);
        // Return the completed view to render on screen
        return convertView;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {

        return logListItem.size();
    }

    /*
               *  View Holder class for individual list items
               */
    public  class ViewHolder {

        TextView logItemLabel;


        ImageView itemImage;


        TextView completionText;



        public ViewHolder(View view){
            view.setTag(this);
        }
    }

//    public ArrayList<ItemModel> getLogListItem() {
//        return logListItem;
//    }


}
