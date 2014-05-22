package me.vtag.app.backend;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import java.util.List;

/**
 * Created by nageswara on 5/21/14.
 */
public class VtagHttpClient extends AsyncHttpClient {

    private CookieStore cookies = null;
    public VtagHttpClient(Context context) {
        super();
        cookies = new PersistentCookieStore(context);
        setCookieStore(cookies);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected RequestHandle sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, ResponseHandlerInterface responseHandler, Context context) {
        refreshCookies(uriRequest);
        return super.sendRequest(client, httpContext, uriRequest, contentType, responseHandler, context);
    }

    private void refreshCookies(HttpUriRequest uriRequest) {
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
        uriRequest.setHeader("Cookie", phpSession);
    }

}
