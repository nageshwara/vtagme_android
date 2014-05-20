package me.vtag.app.pages.players;

/**
 * Created by nageswara on 5/19/14.
 */
public interface OnPlayerStateChangedListener {
    public void onVideoStarted();
    public void onVideoEnded();
    public void onError(String mesg);
}
