package com.rorlig.babyapp.ui.fragment;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;

/**
 * @author gaurav gupta
 */
public abstract class BaseCreateLogFragment extends InjectableFragment{


    private final String parseClassName;
    private String TAG = "BaseCreateLogFragment";

    public BaseCreateLogFragment(String parseClassName) {
        this.parseClassName = parseClassName;
    }

    public abstract void createOrEdit();

    public void delete(String id) {
        Log.d(TAG, "delete btn clicked");
        ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClassName);
        query.fromLocalDatastore();
        query.getInBackground(id, new GetCallback<ParseObject>() {

                    @Override
                    public void done(ParseObject object, ParseException e) {
                        Log.d(TAG, "done finding growth item to delete");
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                scopedBus.post(new ItemCreatedOrChanged(parseClassName));
                            }
                        });

                    }
                }
        );

    }
}
