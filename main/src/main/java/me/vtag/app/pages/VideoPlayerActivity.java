package me.vtag.app.pages;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import me.vtag.app.R;
import me.vtag.app.VtagApplication;
import me.vtag.app.backend.models.VideoMetaModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.pages.players.BasePlayerFragment;
import me.vtag.app.pages.players.OnPlayerStateChangedListener;
import me.vtag.app.pages.players.VtagPlayerFragment;
import me.vtag.app.views.QueueFragment;
import me.vtag.app.views.VideoDetailsFragment;
import me.vtag.app.views.VideosComment;

public class VideoPlayerActivity extends ActionBarActivity {
    private VideoModel mVideoModel;

    private SlidingUpPanelLayout mSlidingPanelLayout;
    private BasePlayerFragment mCurrentPlayerFragment;
    private FrameLayout mPlayerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mPlayerContainer = (FrameLayout)findViewById(R.id.player_wrapper);
        mSlidingPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        QueueFragment queueFragment = VtagApplication.getInstance().getQueueFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.video_queue_container, queueFragment)
                .commit();

        Intent i = getIntent();
        playVideo((VideoModel) i.getParcelableExtra("video"));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        playVideo((VideoModel) intent.getParcelableExtra("video"));
    }

    private void playVideo(VideoModel videoModel) {
        if (mCurrentPlayerFragment != null) {
            mCurrentPlayerFragment.destroy();
            getSupportFragmentManager().beginTransaction()
                    .remove(mCurrentPlayerFragment)
                    .commit();
        }

        mVideoModel = videoModel;
        VideoMetaModel meta = mVideoModel.video;

        Bundle playerArgs = new Bundle();
        playerArgs.putParcelable("videometa", meta);
        /*if (!meta.type.equals("youtube")) {
            mCurrentPlayerFragment = new YoutubePlayerFragment();
        } else {*/
            mCurrentPlayerFragment = new VtagPlayerFragment();
        //}
        mCurrentPlayerFragment.setArguments(playerArgs);

        Bundle detailsArgs = new Bundle();
        detailsArgs.putParcelable("video", mVideoModel);

        VideoDetailsFragment detailsFragment = new VideoDetailsFragment();
        detailsFragment.setArguments(detailsArgs);

        VideosComment videosComment = new VideosComment();
        videosComment.setArguments(detailsArgs);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movieplayer, mCurrentPlayerFragment)
                .replace(R.id.video_details_container, detailsFragment)
                .replace(R.id.video_comments_container, videosComment)
                .commit();
    }

    private OnPlayerStateChangedListener mOnPlayerStateChangedListener = null;
    public OnPlayerStateChangedListener getPlayerStateChangeListener() {
        if (mOnPlayerStateChangedListener == null) {
            mOnPlayerStateChangedListener = new OnPlayerStateChangedListener() {
                @Override
                public void onVideoStarted() {
                }
                @Override
                public void onVideoEnded() {
                    VtagApplication.getInstance().getQueueFragment().next();
                }
                @Override
                public void onError(String mesg) {
                    VtagApplication.getInstance().getQueueFragment().next();
                }
            };
        }
        return mOnPlayerStateChangedListener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.videoplayer_action_menu, menu);
        return true;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            LinearLayout.LayoutParams parms = (LinearLayout.LayoutParams) mPlayerContainer.getLayoutParams();
            parms.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics());;
            mPlayerContainer.setLayoutParams(parms);
        }
        else {
            LinearLayout.LayoutParams parms = (LinearLayout.LayoutParams) mPlayerContainer.getLayoutParams();
            parms.height = FrameLayout.LayoutParams.MATCH_PARENT;
            mPlayerContainer.setLayoutParams(parms);
        }
    }
}
