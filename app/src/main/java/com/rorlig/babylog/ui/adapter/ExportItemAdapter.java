package com.rorlig.babylog.ui.adapter;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.ui.activity.InjectableActivity;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

//import com.gc.materialdesign.views.CheckBox;

/**
 * Created by rorlig on 5/29/15.
 */
public class ExportItemAdapter extends ArrayAdapter<ItemModel> {

    private final String TAG = "ExportItemAdapter";
    private final TypedArray iconArr;
    private final Activity context;
    private ArrayList<Parcelable> logListItem;


    @Inject
    Bus bus;


    /**
     * Constructor
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     * @param logListItem
     */
    public ExportItemAdapter(Activity context, int resource, List<Parcelable> logListItem) {
        super(context, R.layout.list_item_export);
        this.context = context;
        this.logListItem = new ArrayList<Parcelable>(logListItem);
        iconArr = context.getResources().obtainTypedArray(R.array.itemIcons);

        ((InjectableActivity) context).inject(this);
    }


    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ItemModel item = (ItemModel) logListItem.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_export, parent, false);
            viewHolder = new ViewHolder(convertView);
            viewHolder.itemLabel = (TextView) convertView.findViewById(R.id.log_item_label);
            viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.icon_image);
            viewHolder.itemCheckBox = (CheckBox) convertView.findViewById(R.id.item_checkbox);
            viewHolder.itemCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    item.setItemChecked(cb.isChecked());
                }
            });

//            Log.d(TAG, "position " + position + " item " + item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemLabel.setText(item.getItemName());

        viewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(iconArr.getResourceId(position,0)));

        viewHolder.itemCheckBox.setChecked(item.isItemChecked());

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

        TextView itemLabel;


        ImageView itemImage;

        CheckBox itemCheckBox;




        public ViewHolder(View view){
            view.setTag(this);
        }
    }

    public ArrayList<Parcelable> getLogListItem() {
        return logListItem;
    }


}
