package me.vtag.app.backend;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.lang.reflect.Type;
import java.util.List;

import me.vtag.app.WelcomeActivity;
import me.vtag.app.backend.vos.LoginVO;
import me.vtag.app.models.AuthPreferences;
import me.vtag.app.pages.BaseLoginPageFragment;
import me.vtag.app.pages.social.SocialUser;

/**
 * Created by nmannem on 30/10/13.
 */
public class VtagClient {
    private static final String BASE_URL = "http://10.63.8.204:8080";
    //private static final String BASE_URL = "http://www.vtag.me";
    //private static final String BASE_URL = "http://192.168.0.4:8080";

    private AsyncHttpClient client;
    private static VtagClient instance = null;
    private CookieStore cookies;

    /**
     * Returns singleton instance
     */
    public static VtagClient getInstance() {
        if (instance == null) instance = new VtagClient();
        return instance;
    }

    public VtagClient() {
        client  = new AsyncHttpClient();
        cookies = null;
    }

    public void initalize(Context context) {
        cookies = new PersistentCookieStore(context);
        client.setCookieStore(cookies);
    }

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("vtag", ">> getting http data " + getAbsoluteUrl(url));
        refreshCookies();
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("vtag", ">> posting http data " + getAbsoluteUrl(url));
        refreshCookies();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private void refreshCookies() {
        List<Cookie> cookieList = cookies.getCookies();
        String phpSession = "";
        if (cookieList.isEmpty()) {
            Log.i("TAG", "None");
        } else {
            for (Cookie cookie : cookieList) {
                phpSession = phpSession + cookie.getName()+"=\""+cookie.getValue()+"\";";
            }
            Log.i("session", phpSession);
        }
        client.addHeader("Cookie", phpSession);
    }

    public void auth(final SocialUser user, final BaseLoginPageFragment.VtagAuthCallback callback) {
        RequestParams params = new RequestParams();
        params.add("id", user.id);
        params.add("access_token", user.access_token);
        params.add("provider", user.provider);
        params.add("name", user.name);
        params.add("email", user.email);
        client.post(getAbsoluteUrl("/mobile_signup_process"), params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                Gson gson = new Gson();
                Type listType = new TypeToken<LoginVO>() {}.getType();
                LoginVO loginDetails = gson.fromJson(responseBody, listType);

                // Set Auth preferences ...
                AuthPreferences authPreferences = WelcomeActivity.authPreferences;
                authPreferences.setUser(user.id, user.provider);
                authPreferences.setToken(user.access_token);

                callback.onSuccess(loginDetails);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, java.lang.Throwable e) {
                callback.onFailure(statusCode, e);
            }
        });
    }


    public static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl + "?mobile=true";
    }

    public void finishSignup(String username, String email, String password, final BaseLoginPageFragment.VtagAuthCallback callback) {
        RequestParams params = new RequestParams();
        params.add("username", username);
        params.add("email", email);
        params.add("password", password);
        this.post("/signup_process", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                Gson gson = new Gson();
                Type listType = new TypeToken<LoginVO>() {}.getType();
                LoginVO loginDetails = gson.fromJson(responseBody, listType);
                callback.onSuccess(loginDetails);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, java.lang.Throwable e) {
                callback.onFailure(statusCode, e);
            }
        });
    }

    public void getRootDetails(AsyncHttpResponseHandler responseHandler) {
        this.get("/", new RequestParams(), responseHandler);
    }
    public void getTagDetails(String tag, AsyncHttpResponseHandler responseHandler) {
        this.get("/tag/"+tag, new RequestParams(), responseHandler);
    }
    public void getVideoDetails(String vid, AsyncHttpResponseHandler responseHandler) {
        this.get("/video/"+vid, new RequestParams(), responseHandler);
    }
}
