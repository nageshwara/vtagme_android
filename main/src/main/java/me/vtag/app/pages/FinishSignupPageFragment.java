package me.vtag.app.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.BasePageFragment;
import me.vtag.app.HomeActivity;
import me.vtag.app.LoginActivity;
import me.vtag.app.R;
import me.vtag.app.VtagApplication;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.AuthPreferences;
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
    protected boolean supportsActionBar() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finish_signup, container, false);
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

        if (email_text != null) {
            email.setText(email_text);
        }

        if (username_text != null) {
            username.setText(username_text);
        }

        BootstrapButton mSignupButton = (BootstrapButton) rootView.findViewById(R.id.signup_button);
        mSignupButton.setOnClickListener(this);
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
        VtagClient.getAPI().finishSignup(username.getText().toString(),
                email.getText().toString(),
                password.getText().toString(), new Callback<LoginVO>() {
                    @Override
                    public void onResponse(Response<LoginVO> loginVOResponse) {
                        LoginVO loginDetails = loginVOResponse.getResult();
                        if (loginDetails != null) {
                            if (loginDetails.loggedin) {
                                AuthPreferences authPreferences = VtagApplication.getInstance().authPreferences;
                                authPreferences.setUser(username.getText().toString(), "own");
                                Log.w("Came to FinishSignupPageFragment","Myapp ");
                                Log.w(authPreferences.getUser(),"Myapp ");
//                                authPreferences.setToken(user.access_token);

                                Intent intent = null;
                                intent = new Intent(VtagApplication.getInstance(), HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            } else {
                                // Show an error..
                            }
                        } else {
                            Toast.makeText(getActivity(), "Couldnt connect to our backend! Please check your connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}