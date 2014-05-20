package me.vtag.app.pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import me.vtag.app.R;
import me.vtag.app.WelcomeActivity;
import me.vtag.app.backend.models.VideoMetaModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.pages.players.BasePlayerFragment;
import me.vtag.app.pages.players.OnPlayerStateChangedListener;
import me.vtag.app.pages.players.YoutubePlayerFragment;
import me.vtag.app.views.VideoDetailsFragment;

public class VideoPlayerActivity extends ActionBarActivity {
    private VideoModel mVideoModel;
    private SlidingUpPanelLayout mSlidingPanelLayout;
    private BasePlayerFragment mCurrentPlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        mSlidingPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.video_queue_container, WelcomeActivity.mQueueFragment)
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
        if (meta.type.equals("youtube")) {
            mCurrentPlayerFragment = new YoutubePlayerFragment(meta, new OnPlayerStateChangedListener() {
                @Override
                public void onVideoStarted() {
                }
                @Override
                public void onVideoEnded() {
                    WelcomeActivity.mQueueFragment.next();
                }
                @Override
                public void onError(String mesg) {
                    WelcomeActivity.mQueueFragment.next();
                }
            });
        } else {
            Toast.makeText(this, "Sorry, we dont support " + meta.type + " yet :(", Toast.LENGTH_LONG).show();
            return;
        }
        VideoDetailsFragment detailsFragment = new VideoDetailsFragment(mVideoModel);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_container, mCurrentPlayerFragment)
                .replace(R.id.video_details_container, detailsFragment)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.videoplayer_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
