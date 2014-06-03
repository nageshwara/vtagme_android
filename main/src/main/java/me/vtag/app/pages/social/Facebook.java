package me.vtag.app.pages.social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

/**
 * Created by nageswara on 5/10/14.
 */
public class Facebook extends BaseAuthProvider implements Session.StatusCallback {
    public static String FACEBOOK = "facebook";

    private UiLifecycleHelper uiHelper;
    public Facebook(Activity activity, SocialCallbacks callbacks) {
        super(activity, callbacks);
        uiHelper = new UiLifecycleHelper(mActivity, this);
    }

    @Override
    public void login() {
    }

    @Override
    public void logout() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        uiHelper.onCreate(savedInstanceState);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void call(final Session session, SessionState state, Exception exception) {
        if (session.isOpened()) {
            // make request to the /me API
            Request request = Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser graphUser,
                                                Response response) {
                            if (graphUser != null) {
                                SocialUser user = new SocialUser();
                                user.id = graphUser.getId();
                                user.provider = FACEBOOK;
                                user.name = graphUser.getName();
                                user.email = (String)graphUser.asMap().get("email");
                                user.access_token = session.getAccessToken();
                                onLogin(user);
                            } else {
                                // TODO handle this case.
                            }
                        }
                    });
            Request.executeBatchAsync(request);
        } else {
            onLogout();
        }
    }
}
