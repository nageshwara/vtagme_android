package me.vtag.app.pages.social;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by nageswara on 5/10/14.
 */
public class Twitter extends BaseAuthProvider implements AccountManagerCallback<Bundle> {
    private static final int AUTHORIZATION_CODE = 1993;
    private static final int ACCOUNT_CODE = 1601;

    private final String TWITTER_SCOPE = "https://www.googleapis.com/auth/googletalk";
    public static String TWITTER = "twitter";
    public static String TWITTER_SPACE = "com.twitter";

    public Twitter(Activity activity, SocialCallbacks callbacks) {
        super(activity, callbacks);
    }

    @Override
    public void login() {
        Intent intent = AccountManager.newChooseAccountIntent(null, null,
                    new String[] { TWITTER_SPACE }, false, null, null, null, null);
        mActivity.startActivityForResult(intent, ACCOUNT_CODE);
    }

    @Override
    public void logout() {
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

                if (!accountType.equals(TWITTER_SPACE)) return;
                //WelcomeActivity.authPreferences.setUser(accountName, TWITTER);
                invalidateToken(accountType);
                requestToken();
            }
        }
    }

    @Override
    public void run(AccountManagerFuture<Bundle> result) {
        try {
            Bundle bundle = result.getResult();
            Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
            if (launch != null) {
                mActivity.startActivityForResult(launch, AUTHORIZATION_CODE);
            } else {
                String token = bundle
                        .getString(AccountManager.KEY_AUTHTOKEN);
                //WelcomeActivity.authPreferences.setToken(token);
                onLogin(new SocialUser());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void requestToken() {
        Account userAccount = null;
        //String user = WelcomeActivity.authPreferences.getUser();
        /*Account[] accounts = WelcomeActivity.accountManager.getAccounts();
        for (Account account : accounts) {
            if (account.name.equals(user)) {
                userAccount = account;
                break;
            }
        }*/
        //WelcomeActivity.accountManager.getUserData();
        if (userAccount.type == TWITTER_SPACE) {
            //WelcomeActivity.accountManager.getAuthToken(userAccount, "oauth2:" + TWITTER_SCOPE, null, mActivity, this, null);
        }
    }
    /**
     * call this method if your token expired, or you want to request a new
     * token for whatever reason. call requestToken() again afterwards in order
     * to get a new token.
     */
    private void invalidateToken(String accountType) {
        //WelcomeActivity.accountManager.invalidateAuthToken(accountType, WelcomeActivity.authPreferences.getToken());
        //WelcomeActivity.authPreferences.setToken(null);
    }
}
