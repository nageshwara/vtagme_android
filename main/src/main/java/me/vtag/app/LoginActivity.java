package me.vtag.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import me.vtag.app.pages.FinishSignupPageFragment;
import me.vtag.app.pages.LoginPageFragment;

public class LoginActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        changeIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        changeIntent(intent);
    }

    private void changeIntent(Intent i) {
        Boolean signup = i.getBooleanExtra("signup", false);
        if (signup) {
            showSignupPage(i.getStringExtra("email"), i.getStringExtra("username"));
        } else {
            if (VtagApplication.getInstance().isUserLoggedin()) {
                showHomeActivity();
            } else {
                showLoginPage();
            }
        }
    }

    private void showLoginPage() {
        // Now show list of tags.
        LoginPageFragment loginPage = new LoginPageFragment();
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, loginPage)
                .commit();
    }

    private void showSignupPage(String email, String username) {
        // Now show list of tags.
        FinishSignupPageFragment signupPageFragment = new FinishSignupPageFragment(email, username);
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, signupPageFragment)
                .commit();
    }

    private void showHomeActivity() {
        Intent intent = new Intent(VtagApplication.getInstance(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
