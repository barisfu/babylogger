package com.rorlig.babyapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.rorlig.babyapp.ui.fragment.diaper.DiaperChangeStatsType;
import com.rorlig.babyapp.ui.fragment.sleep.SleepStatsFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rorlig on 6/17/15.
 */
public class AppUtils {

    public static final int RESULT_CAMERA_IMAGE_CAPTURE = 1;
    public static final int RESULT_LOAD_IMAGE = 2;
    public static final int RESULT_CROP_IMAGE = 3;

    public static final int PROFILE_ACTIVITY = 1;
    private static String TAG = "AppUtils";

    public static String getCameraDirectory() {
        String dirPath = "";
        //Case for Moto X
        File cameraDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if(cameraDir.exists()) {
            dirPath = cameraDir.getAbsolutePath();
        }
        else {
            //Generic case
            cameraDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if(cameraDir.exists()) {
                dirPath = cameraDir.getAbsolutePath();
            }
            else {
                // If there's no DCIM directory create picutre directory on SDCARD
                cameraDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if(cameraDir.exists()) {
                    dirPath = cameraDir.getAbsolutePath();
                }
                else {

                    try {
                        cameraDir.createNewFile();
                    }
                    catch(IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

        return dirPath;
    }


    public static Spannable getSpannable(CharSequence charSequence, int color) {
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }
        };
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
        Spannable spannable =  Spannable.Factory.getInstance().newSpannable(charSequence);
        spannable.setSpan(cs, 0 , charSequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(foregroundColorSpan, 0 , charSequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }



    public static Drawable getDrawableFromUri(Uri uri, Context context) throws FileNotFoundException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return Drawable.createFromStream(inputStream, uri.toString() );
    }

    public static String getPackageVersionName(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0).versionName;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static void invalidateDiaperChangeCaches(Context context) {

        Log.d(TAG, "invalidateDiaperChangeCaches");
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(DiaperChangeStatsType.WEEKLY.getValue(),"").apply();

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(DiaperChangeStatsType.MONTHLY.getValue(),"").apply();

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(DiaperChangeStatsType.YEARLY.getValue(),"").apply();
    }

    public static void invalidateSleepChangeCaches(Context context) {

        Log.d(TAG, "invalidateSleepChangeCaches");
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(SleepStatsFragment.SleepStatsType.WEEKLY.getValue(),"").apply();

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(SleepStatsFragment.SleepStatsType.MONTHLY.getValue(),"").apply();

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(SleepStatsFragment.SleepStatsType.YEARLY.getValue(),"").apply();
    }


    public static void invalidateParseCache(String parseClass, Context context) {
        ParseObject.unpinAllInBackground(parseClass, new DeleteCallback() {
            @Override
            public void done(ParseException e) {

            }
        });

    }
}
