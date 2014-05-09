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
import me.vtag.app.pages.LoginPageFragment;

/**
 * Created by nmannem on 30/10/13.
 */
public class VtagClient {
    private static final String BASE_URL = "http://10.63.8.119:8080";
    //private static final String BASE_URL = "http://www.vtag.me";

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
    }

    public void initalize(Context context) {
        cookies = new PersistentCookieStore(context);
        client.setCookieStore(cookies);
    }

    private void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("vtag", ">> getting http data " + getAbsoluteUrl(url));
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    private void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("vtag", ">> posting http data " + getAbsoluteUrl(url));
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public void auth(final LoginPageFragment.VtagAuthCallback callback) {
        AuthPreferences authPreferences = WelcomeActivity.authPreferences;
        RequestParams params = new RequestParams();
        params.add("id", authPreferences.getUser());
        params.add("token", authPreferences.getToken());
        params.add("provider", authPreferences.getProvider());
        client.post(getAbsoluteUrl("/mobile_signup_process"), params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                // Get cookie from here..
                List<Cookie> cookieList = cookies.getCookies();

                Gson gson = new Gson();
                Type listType = new TypeToken<LoginVO>() {}.getType();
                LoginVO loginDetails = gson.fromJson(responseBody, listType);
                callback.onSuccess(loginDetails);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, java.lang.Throwable e) {
                callback.onFailure();
            }
        });
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl + "?mobile=true";
    }

    public void finishSignup(String username, String email, String password, final LoginPageFragment.VtagAuthCallback callback) {
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
                callback.onFailure();
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
