package me.vtag.app.pages;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

import me.vtag.app.BasePageFragment;
import me.vtag.app.R;
import me.vtag.app.WelcomeActivity;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.vos.LoginVO;

/**
 * Created by nageswara on 5/5/14.
 */
public class LoginPageFragment extends BasePageFragment implements View.OnClickListener {
    public static final int ID = 3;
    private static final int AUTHORIZATION_CODE = 1993;
    private static final int ACCOUNT_CODE = 1601;

    private final String GOOGLE_SCOPE = "profile email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me";
    private final String TWITTER_SCOPE = "https://www.googleapis.com/auth/googletalk";

    public static String FACEBOOK = "facebook";
    public static String GOOGLE = "google";
    public static String TWITTER = "twitter";

    public LoginPageFragment() {
        super(ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        rootView.findViewById(R.id.google_sign_in_button).setOnClickListener(this);
        LoginButton facebookLoginButton = (LoginButton) rootView.findViewById(R.id.facebook_sign_in_button);
        facebookLoginButton.setFragment(this);
        facebookLoginButton.setPublishPermissions(Arrays.asList("user_likes", "publish_actions"));

        uiHelper = new UiLifecycleHelper(getActivity(), new FacebookTokenAcquired());
        uiHelper.onCreate(savedInstanceState);
        return rootView;
    }

    public void onClick(View view) {
        Intent intent = null;
        if (view.getId() == R.id.google_sign_in_button) {
            intent = AccountManager.newChooseAccountIntent(null, null,
                    new String[] { "com.google" }, false, null, null, null, null);
        }

        startActivityForResult(intent, ACCOUNT_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    public static String  getProvider(String accountType) {
        switch(accountType) {
            case "com.google":
                return GOOGLE;
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AUTHORIZATION_CODE) {
                requestToken();
            } else if (requestCode == ACCOUNT_CODE) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                String accountType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
                WelcomeActivity.authPreferences.setUser(accountName, getProvider(accountType));

                // invalidate old tokens which might be cached. we want a fresh
                // one, which is guaranteed to work
                invalidateToken(accountType);
                requestToken();
            }
       }
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void requestToken() {
        Account userAccount = null;
        String user = WelcomeActivity.authPreferences.getUser();
        Account[] accounts = WelcomeActivity.accountManager.getAccounts();
        for (Account account : accounts) {
            if (account.name.equals(user)) {
                userAccount = account;
                break;
            }
        }
        if (getProvider(userAccount.type) == GOOGLE) {
            WelcomeActivity.accountManager.getAuthToken(userAccount, "oauth2:" + GOOGLE_SCOPE, null, getActivity(), new AuthTokenAcquired(), null);
        }
    }
    /**
     * call this method if your token expired, or you want to request a new
     * token for whatever reason. call requestToken() again afterwards in order
     * to get a new token.
     */
    private void invalidateToken(String accountType) {
        WelcomeActivity.accountManager.invalidateAuthToken(accountType, WelcomeActivity.authPreferences.getToken());
        WelcomeActivity.authPreferences.setToken(null);
    }

    private void onLogin() {
        VtagClient.getInstance().auth(new VtagAuthCallback(){
            @Override
            public void onSuccess(LoginVO loginDetails) {
                if (loginDetails.loggedin) {
                    ((WelcomeActivity) getActivity()).browseHomePage();
                } else {
                    ((WelcomeActivity) getActivity()).showSignupPage(loginDetails.email, loginDetails.username);
                }
            }
            @Override
            public void onFailure() {
                ((WelcomeActivity) getActivity()).showLoginPage();
            }
        });
    }

    private void onLogOut() {
        ((WelcomeActivity) getActivity()).showLoginPage();
    }


    private UiLifecycleHelper uiHelper;
    private class FacebookTokenAcquired implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (session.isOpened()) {
                WelcomeActivity.authPreferences.setUser("facebookuser", FACEBOOK);
                WelcomeActivity.authPreferences.setToken(session.getAccessToken());
                LoginPageFragment.this.onLogin();
            } else {
                WelcomeActivity.authPreferences.setToken(null);
                LoginPageFragment.this.onLogOut();
            }
        }
    }

    private class AuthTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            try {
                Bundle bundle = result.getResult();
                Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (launch != null) {
                    startActivityForResult(launch, AUTHORIZATION_CODE);
                } else {
                    String token = bundle
                            .getString(AccountManager.KEY_AUTHTOKEN);
                    WelcomeActivity.authPreferences.setToken(token);
                    LoginPageFragment.this.onLogin();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static interface VtagAuthCallback {
        void onSuccess(LoginVO loginDetails);
        void onFailure();
    }
}