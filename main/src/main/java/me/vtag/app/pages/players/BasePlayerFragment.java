package me.vtag.app.pages.players;

import android.support.v4.app.Fragment;

/**
 * Created by nageswara on 5/4/14.
 */
public class BasePlayerFragment extends Fragment {

    private OnPlayerStateChangedListener mStateChangedListener;
    public BasePlayerFragment(OnPlayerStateChangedListener stateChangedListener) {
        mStateChangedListener = stateChangedListener;
    }
    public void destroy() {
    }

    protected void onVideoEnd() {
        mStateChangedListener.onVideoEnded();
    }

    protected void onVideoStart() {
        mStateChangedListener.onVideoStarted();
    }

    protected void onVideoError() {
        mStateChangedListener.onError("Unknown error");
    }
}
