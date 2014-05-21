package me.vtag.app.pages;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.vtag.app.BasePageFragment;
import me.vtag.app.HomeActivity;
import me.vtag.app.LoginActivity;
import me.vtag.app.VtagApplication;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.vos.LoginVO;
import me.vtag.app.pages.social.BaseAuthProvider;
import me.vtag.app.pages.social.Facebook;
import me.vtag.app.pages.social.GooglePlus;
import me.vtag.app.pages.social.SocialUser;

/**
 * Created by nageswara on 5/5/14.
 */
public class BaseLoginPageFragment extends BasePageFragment implements LoaderManager.LoaderCallbacks<Cursor>, BaseAuthProvider.SocialCallbacks {
    private Map<String, BaseAuthProvider> authProviderMap;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private boolean isAuthGoingOn = false;

    public BaseLoginPageFragment(int ID) {
        super(ID);
        authProviderMap = new HashMap<>(2);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        authProviderMap.put(Facebook.FACEBOOK, new Facebook(getActivity(), this));
        authProviderMap.put(GooglePlus.GOOGLE, new GooglePlus(getActivity(), this));
    }

    public void login(String provider) {
        if (!authProviderMap.containsKey(provider)) return;
        authProviderMap.get(provider).login();
    }

    @Override
    public void onResume() {
        super.onResume();
        for(Map.Entry<String, BaseAuthProvider> entry : authProviderMap.entrySet()) {
            entry.getValue().onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for(Map.Entry<String, BaseAuthProvider> entry : authProviderMap.entrySet()) {
            entry.getValue().onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(Map.Entry<String, BaseAuthProvider> entry : authProviderMap.entrySet()) {
            entry.getValue().onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for(Map.Entry<String, BaseAuthProvider> entry : authProviderMap.entrySet()) {
            entry.getValue().onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for(Map.Entry<String, BaseAuthProvider> entry : authProviderMap.entrySet()) {
            entry.getValue().onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onLogin(SocialUser user) {
        if (isAuthGoingOn) {
            return;
        }

        showProgress(true);
        isAuthGoingOn = true;
        VtagClient.getInstance().auth(user, new VtagAuthCallback(){
            @Override
            public void onSuccess(LoginVO loginDetails) {
                showProgress(false);
                Intent intent = null;
                if (loginDetails.loggedin) {
                    intent = new Intent(VtagApplication.getInstance(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                } else {
                    intent = new Intent(VtagApplication.getInstance(), LoginActivity.class);
                    intent.putExtra("singup", true);
                    intent.putExtra("email", loginDetails.email);
                    intent.putExtra("username", loginDetails.username);
                }
                startActivity(intent);
            }
            @Override
            public void onFailure(int statusCode, Throwable e) {
                showProgress(false);
                Toast.makeText(getActivity(), "Couldnt connect to our backend! Please check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onLogout(SocialUser user) {
        Intent intent = new Intent(VtagApplication.getInstance(), LoginActivity.class);
        startActivity(intent);
    }


    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    protected void populateAutoComplete() {
        if (Build.VERSION.SDK_INT >= 14) {
            // Use ContactsContract.Profile (API 14+)

            getLoaderManager().initLoader(0, null, this);
        } else if (Build.VERSION.SDK_INT >= 8) {
            // Use AccountManager (API 8+)
            new SetupEmailAutoCompleteTask().execute(null, null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
     * the email text field with results on the main UI thread.
     */
    class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> emailAddressCollection = new ArrayList<String>();

            // Get all emails from the user's contacts and copy them to a list.
            ContentResolver cr = getActivity().getContentResolver();
            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    null, null, null);
            while (emailCur.moveToNext()) {
                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
                        .CommonDataKinds.Email.DATA));
                emailAddressCollection.add(email);
            }
            emailCur.close();
            return emailAddressCollection;
        }

        @Override
        protected void onPostExecute(List<String> emailAddressCollection) {
            addEmailsToAutoComplete(emailAddressCollection);
        }
    }

    protected List<String> cachedEmailAddressCollection;
    protected ArrayAdapter<String> addEmailsToAutoComplete(List<String> emailAddressCollection) {
        cachedEmailAddressCollection = emailAddressCollection;
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        return adapter;
    }

    public static interface VtagAuthCallback {
        void onSuccess(LoginVO loginDetails);
        void onFailure(int statusCode, Throwable e);
    }
}