package com.tekrevol.arrowrecovery;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tekrevol.arrowrecovery.activities.MainActivity;
import com.tekrevol.arrowrecovery.libraries.imageloader.CustomImageDownaloder;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager;
import com.tekrevol.arrowrecovery.models.receiving_model.MyObjectBox;

import io.fabric.sdk.android.Fabric;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.objectbox.BoxStore;
import io.reactivex.subjects.PublishSubject;

import static com.tekrevol.arrowrecovery.constatnts.AppConstants.KEY_FIREBASE_TOKEN;
import static com.tekrevol.arrowrecovery.constatnts.AppConstants.KEY_FIREBASE_TOKEN_UPDATED;

/**
 * Created by khanhamza on 09-Mar-17.
 */

public class BaseApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {
    public static final boolean LOG_FLAG = true;
    private static PublishSubject<Pair> publishSubject = PublishSubject.create();
    private static boolean isInBackground = true;
    private static String applicationName;
    private static Context mContext;

    private static BaseApplication baseApplication;
    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        configImageLoader(this);

        baseApplication = this;
        boxStore = MyObjectBox.builder().androidContext(BaseApplication.this).build();

        mContext = this;
        applicationName = getApplicationName(this);

        configureCalligraphyLibrary();

        // TODO: 12/20/2017 Enable it to use Calligraphy font library
//        configureCalligraphyLibrary();

        // TODO: 11/1/2017 Enable Crash Lytics and Never Crash feature before releasing the app
//        Fabric.with(this, new Crashlytics());
//        neverCrash();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        Log.w("FIREBASE", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    // Log and toast
                    System.out.println(token);

                    SharedPreferenceManager.getInstance(getContext()).putValue(KEY_FIREBASE_TOKEN, token);
                    SharedPreferenceManager.getInstance(getContext()).putValue(KEY_FIREBASE_TOKEN_UPDATED, true);



                });

    }
    public static BaseApplication getApp() {
        return baseApplication;
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

    public static Context getContext() {
        return mContext;
    }


    private String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

//    private void configureCalligraphyLibrary() {
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/SanFranciscoRegular.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
//
//    }

    private void configImageLoader(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).displayer(new FadeInBitmapDisplayer(300)).build();
        // Create global configuration and initialize ImageLoaderHelper with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .imageDownloader(new CustomImageDownaloder(context))
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void configureCalligraphyLibrary() {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

    }

    /**
     * A method to perform a restart if crash appear, won't show crash to user and send the report to the Fabric
     */
    private void neverCrash() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, final Throwable paramThrowable) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        SharedPreferenceManager.getInstance().setForcedRestart(true);
                        Crashlytics.logException(paramThrowable);
                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    Log.e("CRASH", "uncaughtException: " + e.getMessage());
                }

//                Log.d("Crash BaseApplication", "uncaughtException: " + SharedPreferenceManager.getInstance().isForcedRestart());
                Log.e("Error" + Thread.currentThread().getStackTrace()[2], paramThrowable.getLocalizedMessage());

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 293, new Intent(getApplicationContext(), MainActivity.class), 0);
                AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 30, pendingIntent);
                System.exit(1);
            }
        });
    }


    public static PublishSubject<Pair> getPublishSubject() {
        return publishSubject;
    }

    public static boolean isInBackground() {
        return isInBackground;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e("abc", "onActivityStarted " + activity.toString() + "");
        if (isInBackground) {
            isInBackground = false;
        }

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isInBackground) {
            isInBackground = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("abc", "onActivityDestroyed " + activity.toString());

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e("abc", "onTrimMemory :- " + level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            isInBackground = true;

        }
    }

    public static String getApplicationName() {
        return applicationName;
    }


}

