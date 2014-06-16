package me.vtag.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import java.util.Map;

import me.vtag.app.pages.FinishSignupPageFragment;
import me.vtag.app.pages.LoginPageFragment;
import me.vtag.app.pages.social.BaseAuthProvider;

public class LoginActivity extends ActionBarActivity {
    private LoginPageFragment mLoginPage;
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
        mLoginPage = new LoginPageFragment();
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mLoginPage)
                .commit();
    }

    private void showSignupPage(String email, String username) {
        // Now show list of tags.
        FinishSignupPageFragment signupPageFragment = new FinishSignupPageFragment();
        Bundle args = new Bundle();
        args.putString(FinishSignupPageFragment.EMAIL_TEXT, email);
        args.putString(FinishSignupPageFragment.USERNAME_TEXT, username);
        signupPageFragment.setArguments(args);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO BIG HACK. Passing activity result from activity to fragment manually.
        if (mLoginPage != null) {
            mLoginPage.onActivityResult(requestCode, resultCode, data);
        }
    }

}
