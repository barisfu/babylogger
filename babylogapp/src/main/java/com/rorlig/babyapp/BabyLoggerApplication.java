package com.rorlig.babyapp;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.rorlig.babyapp.dagger.ApplicationModule;
import com.rorlig.babyapp.dagger.ObjectGraphApplication;
import com.rorlig.babyapp.parse_dao.Baby;
import com.rorlig.babyapp.parse_dao.DiaperChange;
import com.rorlig.babyapp.parse_dao.Feed;
import com.rorlig.babyapp.parse_dao.Growth;
import com.rorlig.babyapp.parse_dao.Milestones;
import com.rorlig.babyapp.parse_dao.Sleep;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheContextUtils;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;

//import com.path.android.jobqueue.JobManager;
//import com.path.android.jobqueue.config.Configuration;
//import com.path.android.jobqueue.log.CustomLogger;

/**
 * Created by gaurav
 */
//@ReportsCrashes(
//        formKey = "", // will not be used
//        mailTo = "guptgaurav@gmail.com"
//)
public class BabyLoggerApplication extends Application implements ObjectGraphApplication {
    private ObjectGraph objectGraph;
    private final String TAG = "BabyLoggerApplication";

    public static final String APPLICATION_ID = "fQ6DxdazosJXTsgxZAxT9izIj6BkgJI9HKUzPlUf";

    public static final String CLIENT_KEY = "oVPe4qrrMe3glqgTPwHQ7tbx1snfq5GvlPBBiMQE";
//    private JobManager jobManager;

    @Override
    public ObjectGraph getApplicationGraph() {
//        initializeDagger();
        Log.d(TAG, "getApplicaitonGraph");
        if (objectGraph==null) initializeDagger();

        return objectGraph;
    }

    @Override
    public void inject(Object paramObject) {
       initializeDagger();
       objectGraph.inject(paramObject);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initializeDagger();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Register subclasses...
        ParseObject.registerSubclass(DiaperChange.class);

        ParseObject.registerSubclass(Feed.class);

        ParseObject.registerSubclass(Growth.class);

        ParseObject.registerSubclass(Milestones.class);

        ParseObject.registerSubclass(Sleep.class);

        ParseObject.registerSubclass(Baby.class);





        // Register subclasses...
//        ParseObject.registerSubclass(DiaperChange.class);
        // Add your initialization code here
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        ParseUser.enableRevocableSessionInBackground();

//        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        ParseInstallation.getCurrentInstallation().saveInBackground();



//        DualCacheContextUtils.setContext(getApplicationContext());

//        configureJobManager();
//        ACRA.init(this);

    }

    protected List<Object> getModules(){
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = new ApplicationModule(this);
//        arrayOfObject[1] = new SchedulerModule();   //todo add test module...
        return Arrays.asList(arrayOfObject);
    }

    protected final void initializeDagger() {
        Log.d(TAG, "initializeDagger");
        if (objectGraph == null) {
            Log.d(TAG, "make object graph");
            ApplicationModule module = new ApplicationModule(this);
//            if (BuildConfig.DEBUG){
//                TestModule testModule  = new TestModule(this);
                objectGraph = ObjectGraph.create(
                        module);
//                objectGraph.inject(GetUserJob.class);
//            }

//            objectGraph.validate();
//            objectGraph.inject(this);
        } else {
            Log.d(TAG, "not null");
        }

    }

//    private void configureJobManager() {
//        Configuration configuration = new Configuration.Builder(this)
//                .customLogger(new CustomLogger() {
//                    private static final String TAG = "JOBS";
//                    @Override
//                    public boolean isDebugEnabled() {
//                        return true;
//                    }
//
//                    @Override
//                    public void d(String text, Object... args) {
//                        Log.d(TAG, String.format(text, args));
//                    }
//
//                    @Override
//                    public void e(Throwable t, String text, Object... args) {
//                        Log.e(TAG, String.format(text, args), t);
//                    }
//
//                    @Override
//                    public void e(String text, Object... args) {
//                        Log.e(TAG, String.format(text, args));
//                    }
//                })
//                .minConsumerCount(1)//always keep at least one consumer alive
//                .maxConsumerCount(3)//up to 3 consumers at a time
//                .loadFactor(3)//3 jobs per consumer
//                .consumerKeepAlive(120)//wait 2 minute
//                .build();
//        jobManager = new JobManager(this, configuration);
//    }
//
//    public JobManager getJobManager() {
//        return jobManager;
//    }




}
