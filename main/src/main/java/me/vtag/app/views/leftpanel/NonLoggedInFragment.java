package me.vtag.app.views.leftpanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.widget.LoginButton;

import java.util.Arrays;

import me.vtag.app.R;
import me.vtag.app.pages.BaseLoginPageFragment;
import me.vtag.app.pages.social.GooglePlus;

public class NonLoggedInFragment extends BaseLoginPageFragment implements View.OnClickListener {
    public NonLoggedInFragment() {
    }

    public void onClick(View view) {
        if (view.getId() == R.id.plus_sign_in_button) {
            login(GooglePlus.GOOGLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_left_panel_nonloggedin, container, false);
        rootView.findViewById(R.id.plus_sign_in_button).setOnClickListener(this);
        LoginButton facebookLoginButton = (LoginButton) rootView.findViewById(R.id.facebook_sign_in_button);
        facebookLoginButton.setFragment(this);
        facebookLoginButton.setPublishPermissions(Arrays.asList("user_likes", "publish_actions"));
        return rootView;
    }
}
