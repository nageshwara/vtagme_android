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
import android.widget.EditText;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Type;
import java.util.Arrays;

import me.vtag.app.BasePageFragment;
import me.vtag.app.R;
import me.vtag.app.WelcomeActivity;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.vos.LoginVO;

/**
 * Created by nageswara on 5/5/14.
 */
public class FinishSignupPageFragment extends BasePageFragment implements View.OnClickListener {
    public static final int ID = 4;
    public static final String EMAIL_TEXT = "signup_email";
    public static final String USERNAME_TEXT = "signup_username";

    private EditText email;
    private EditText username;
    private EditText password;

    private String email_text;
    private String username_text;

    public FinishSignupPageFragment(String email_text, String username_text) {
        super(ID);
        this.email_text = email_text;
        this.username_text = username_text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finish_signup, container, false);
        rootView.findViewById(R.id.signup_button).setOnClickListener(this);
        email = (EditText)rootView.findViewById(R.id.email_input);
        username = (EditText)rootView.findViewById(R.id.username_input);
        password = (EditText)rootView.findViewById(R.id.password_input);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EMAIL_TEXT)) {
                email_text = savedInstanceState.getString(EMAIL_TEXT);
            }
            if (savedInstanceState.containsKey(USERNAME_TEXT)) {
                username_text = savedInstanceState.getString(USERNAME_TEXT);
            }
        }

        email.setText(email_text);
        username.setText(username_text);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(EMAIL_TEXT, email.getText().toString());
        savedInstanceState.putString(USERNAME_TEXT, username.getText().toString());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        // Finish the sign up..
        VtagClient.getInstance().finishSignup(username.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                new LoginPageFragment.VtagAuthCallback() {
                    @Override
                    public void onSuccess(LoginVO loginDetails) {
                        if (loginDetails.loggedin) {
                            ((WelcomeActivity) getActivity()).browseHomePage();
                        } else {
                            // Show Error Message..
                        }
                    }
                    @Override
                    public void onFailure() {
                        // Show Error
                    }
                });
    }
}