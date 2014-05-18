package me.vtag.app.models;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * Created by nageswara on 5/5/14.
 */
public class AuthPreferences {

    private static final String KEY_USER = "user";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_PROVIDER = "provider";

    private SharedPreferences preferences;

    public AuthPreferences(Context context) {
        preferences = context
                .getSharedPreferences("auth21", Context.MODE_PRIVATE);
    }

    public void setUser(String user, String provider) {
        Editor editor = preferences.edit();
        editor.putString(KEY_USER, user);
        editor.putString(KEY_PROVIDER, provider);
        editor.commit();
    }

    public void setToken(String password) {
        Editor editor = preferences.edit();
        editor.putString(KEY_TOKEN, password);
        editor.commit();
    }

    public String getUser() {
        return preferences.getString(KEY_USER, null);
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public String getProvider() {
        return preferences.getString(KEY_PROVIDER, null);
    }
}