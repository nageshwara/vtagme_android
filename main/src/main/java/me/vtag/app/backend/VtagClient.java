package me.vtag.app.backend;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;

import ly.apps.android.rest.client.DefaultRestClientImpl;
import ly.apps.android.rest.client.RestClient;
import ly.apps.android.rest.client.RestServiceFactory;
import ly.apps.android.rest.converters.impl.JacksonBodyConverter;

/**
 * Created by nmannem on 30/10/13.
 */
public class VtagClient {
    //private static final String BASE_URL = "http://10.63.8.213:8080";
    //private static final String BASE_URL = "http://www.vtag.me";
    private static final String BASE_URL = "http://192.168.1.5:8080";

    private AsyncHttpClient client;
    private static VtagClient instance = null;

    private static VtagAPI api;

    /**
     * Returns singleton instance
     */
    public static VtagClient getInstance() {
        if (instance == null) instance = new VtagClient();
        return instance;
    }

    public VtagClient() {
    }

    public void initalize(Context context) {
        client  = new VtagHttpClient(context);
        RestClient restClient = new DefaultRestClientImpl(client, new VtagQueryParamsConverter(), new JacksonBodyConverter());
        api = RestServiceFactory.getService(BASE_URL, VtagAPI.class, restClient);
    }

    public static VtagAPI getAPI() {
        return api;
    }

    public static String getUrl(String component) {
        return BASE_URL + component;
    }
}
