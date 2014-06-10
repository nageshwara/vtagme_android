package me.vtag.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by nmannem on 30/10/13.
 */
public class BasePageFragment extends Fragment {
    private Activity mCurrentActivity;
    public BasePageFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        mCurrentActivity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        if (mCurrentActivity.getActionBar() != null) {
            if (!supportsActionBar()) {
                mCurrentActivity.getActionBar().hide();
            } else {
                mCurrentActivity.getActionBar().show();
            }
        }
        super.onStart();
    }

    protected boolean supportsActionBar() {
        return true;
    }
}