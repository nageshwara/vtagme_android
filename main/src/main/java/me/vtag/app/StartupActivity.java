package me.vtag.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import me.vtag.app.pages.VideoPlayerActivity;


public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        if (VtagApplication.getInstance().isUserLoggedin()) {
            showHomeActivity();
        } else {
            showLoginActivity();
        }
        finish();
    }

    private void showLoginActivity() {
        Intent intent = new Intent(VtagApplication.getInstance(), LoginActivity.class);
        startActivity(intent);
    }

    private void showHomeActivity() {
        Intent intent = new Intent(VtagApplication.getInstance(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
