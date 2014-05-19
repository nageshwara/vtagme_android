package me.vtag.app.pages.players;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import me.vtag.app.R;
import me.vtag.app.backend.models.VideoMetaModel;

/**
 * Created by nageswara on 5/4/14.
 */
public class YoutubePlayerFragment extends BasePlayerFragment {
    public static String YOUTUBE_API_KEY = "AIzaSyBmlPp_uA1HddVULhDpsLVjX1q7GRqc7Eg";

    private VideoMetaModel meta;
    private YouTubePlayerSupportFragment player;

    public YoutubePlayerFragment(VideoMetaModel meta) {
        this.meta = meta;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.youtubevideo_fragment, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        player = (YouTubePlayerSupportFragment)getFragmentManager().findFragmentById(R.id.videoplayer);
        player.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener(){
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
            }
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
                }
                player.cueVideo(meta.typeid);
                player.play();
            }
        });
        super.onResume();
    }
}
