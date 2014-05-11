package me.vtag.app.backend;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONObject;

import java.lang.reflect.Type;

import me.vtag.app.WelcomeActivity;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.vos.LoginVO;
import me.vtag.app.models.AuthPreferences;
import me.vtag.app.pages.LoginPageFragment;

/**
 * Created by nmannem on 30/10/13.
 */
public class VtagClient {
    //private static final String BASE_URL = "http://192.168.0.4:8080";
    private static final String BASE_URL = "http://www.vtag.me";

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
        cookies = new BasicCookieStore();
        client  = new AsyncHttpClient();
        client.setCookieStore(cookies);
    }

    public void refreshLoginCookie() {
        Cookie cookie = new BasicClientCookie("auth", WelcomeActivity.authPreferences.getVtagId());
        cookies.addCookie(cookie);
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