package com.rorlig.babylog;

import android.app.Application;
import android.util.Log;

import com.rorlig.babylog.dagger.ApplicationModule;
import com.rorlig.babylog.dagger.ObjectGraphApplication;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

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
        initializeDagger();
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
