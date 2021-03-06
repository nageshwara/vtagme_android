package me.vtag.app.backend.models;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.VtagApplication;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.helpers.VtagmeCallback;

/**
 * Created by nageswara on 5/27/14.
 */
public class PublictagModel extends BaseTagModel {
    public static final int TYPE = 3;
    public long[] followers;
    public void follow(final VtagmeCallback callback) {
        if (!following) {
            VtagClient.getAPI().followPublictag(id, VtagApplication.getInstance().authPreferences.getUser(), new Callback<String>() {
                @Override
                public void onResponse(Response<String> baseTagModelResponse) {
                    String response_true = baseTagModelResponse.getResult();
                    if (response_true == "true") {
                        following = true;
                        callback.onComplete(true);
                    } else {
                        callback.onComplete(false);
                    }
                }
            });
        } else {
            callback.onComplete(true);
        }
    }

    public void unfollow(final VtagmeCallback callback) {
        if (following) {
            VtagClient.getAPI().unfollowPublictag(id, VtagApplication.getInstance().authPreferences.getUser(), new Callback<String>() {
                @Override
                public void onResponse(Response<String> baseTagModelResponse) {
                    String response_true = baseTagModelResponse.getResult();
                    if (response_true == "true") {
                        following = false;
                        callback.onComplete(true);
                    } else {
                        callback.onComplete(false);
                    }
                }
            });
        } else {
            callback.onComplete(true);
        }
    }
}
