package me.vtag.app.pages.players;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import me.vtag.app.R;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.VideoMetaModel;

/**
 * Created by nageswara on 5/4/14.
 */
public class VtagPlayerFragment extends BasePlayerFragment {
    private VideoMetaModel meta;
    private WebView mWebView;

    public VtagPlayerFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        meta = getArguments().getParcelable("videometa");

        View rootView = inflater.inflate(R.layout.vtagvideo_fragment, container, false);
        mWebView = (WebView)rootView.findViewById(R.id.videoplayer_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowContentAccess(true);
        mWebView.addJavascriptInterface(this, "vtagAndroid");
        mWebView.loadUrl(VtagClient.getUrl("/showmobilevideo/"+meta.id));
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.loadUrl("about:blank");
        //mWebView.pauseTimers(); //new code
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void onPlayerStateChanged(int state) {
        if (state == 0) {
            onVideoEnd();
        } else if (state == 1) {
            onVideoStart();
        } else if (state == 3) {
            onVideoError();
        }
    }
}
