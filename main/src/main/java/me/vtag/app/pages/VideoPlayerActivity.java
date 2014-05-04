package me.vtag.app.pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import me.vtag.app.R;
import me.vtag.app.backend.models.VideoMetaModel;
import me.vtag.app.pages.players.BasePlayerFragment;
import me.vtag.app.pages.players.YoutubePlayerFragment;

public class VideoPlayerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        Intent i = getIntent();
        VideoMetaModel meta = i.getParcelableExtra("meta");
        BasePlayerFragment playerFragment = null;
        if (meta.type == "youtube") {
            playerFragment = new YoutubePlayerFragment(meta);
        } else {
            Toast.makeText(this, "Sorry, we dont support " + meta.type + " yet :(", Toast.LENGTH_LONG).show();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_container, playerFragment)
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
