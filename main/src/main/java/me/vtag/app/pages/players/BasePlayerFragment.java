package me.vtag.app.pages.players;

import android.app.Activity;
import android.support.v4.app.Fragment;

import me.vtag.app.pages.VideoPlayerActivity;

/**
 * Created by nageswara on 5/4/14.
 */
public class BasePlayerFragment extends Fragment {

    private OnPlayerStateChangedListener mStateChangedListener;
    public BasePlayerFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof VideoPlayerActivity) {
            mStateChangedListener = ((VideoPlayerActivity) activity).getPlayerStateChangeListener();
        }
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
