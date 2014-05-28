package me.vtag.app.backend;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by nageswara on 5/21/14.
 */
public class VtagHttpClient extends AsyncHttpClient {

    private CookieStore cookies = null;
    private ThreadPoolExecutor threadPool;
    private final Map<Context, List<WeakReference<Future<?>>>> requestMap;

    public VtagHttpClient(Context context) {
        super();
        cookies = new PersistentCookieStore(context);
        setCookieStore(cookies);
        threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        requestMap = new WeakHashMap<Context, List<WeakReference<Future<?>>>>();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected RequestHandle sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, ResponseHandlerInterface responseHandler, Context context) {
        refreshCookies();
        return super.sendRequest(client, httpContext, uriRequest, contentType, responseHandler, context);
    }

    private void refreshCookies() {
        List<Cookie> cookieList = cookies.getCookies();
        String phpSession = "";
        if (!cookieList.isEmpty()) {
            for (Cookie cookie : cookieList) {
                phpSession = phpSession + cookie.getName()+"=\""+cookie.getValue()+"\";";
            }
            Log.w("session", phpSession);
            removeHeader("Cookie");
            addHeader("Cookie", phpSession);
        }
    }

}
