package me.vtag.app;

import android.accounts.AccountManager;
import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.AuthPreferences;
import me.vtag.app.views.QueueFragment;

/**
 * Created by nageswara on 5/20/14.
 */
public class VtagApplication extends Application {
    private static VtagApplication mInstance;
    public AccountManager accountManager;
    public AuthPreferences authPreferences;
    public QueueFragment mQueueFragment;

    public static VtagApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        mInstance = this;
        accountManager = AccountManager.get(this);
        authPreferences = new AuthPreferences(this);
        super.onCreate();

        VtagClient.getInstance().initalize(this);
        mQueueFragment = new QueueFragment();
    }

    public boolean isUserLoggedin() {
        return authPreferences.getUser() != null;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public QueueFragment getQueueFragment() {
        return mQueueFragment;
    }
}
