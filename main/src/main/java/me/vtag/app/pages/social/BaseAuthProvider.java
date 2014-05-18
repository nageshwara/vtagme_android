package me.vtag.app.pages.social;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by nageswara on 5/10/14.
 */
public abstract class BaseAuthProvider {
    Activity mActivity;
    SocialCallbacks mCallbacks;
    SocialUser mUser;
    public BaseAuthProvider(Activity activity, SocialCallbacks callbacks) {
        mActivity = activity; mCallbacks = callbacks;
    }

    public abstract void login();
    public abstract void logout();

    public void onCreate(Bundle savedInstanceState) {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onDestroy() {
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    protected Dialog onCreateDialog(int id) {
        return null;
    }

    protected void onLogin(SocialUser user) {
        mUser = user;
        mCallbacks.onLogin(mUser);
    }
    protected void onLogout() {
        if (mUser == null) return;
        mCallbacks.onLogout(mUser);

    }

    public static interface SocialCallbacks {
        void onLogin(SocialUser user);
        void onLogout(SocialUser user);
    }
}
