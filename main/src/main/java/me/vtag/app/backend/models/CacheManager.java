package me.vtag.app.backend.models;

import android.util.LruCache;

/**
 * Created by nageswara on 5/20/14.
 */
public class CacheManager {
    private static CacheManager mInstance;
    public static CacheManager getInstance(){
        if (mInstance == null) {
            mInstance = new CacheManager();
        }
        return mInstance;
    }
    private CacheManager() {
        mHashtags = new LruCache<>(100);
        mPrivatetags = new LruCache<>(100);
    };

    private LruCache<String, BaseTagModel> mHashtags;
    private LruCache<String, BaseTagModel> mPrivatetags;

    public BaseTagModel getHashTagModel(String tag) {
        return mHashtags.get(tag);
    }
    public void putHashTagModel(String tag, BaseTagModel tagModel) {
        mHashtags.put(tag, tagModel);
    }

    public BaseTagModel getPrivateTagModel(String tag) {
        return mPrivatetags.get(tag);
    }
    public void putPrivateTagModel(String tag, BaseTagModel tagModel) {
        mPrivatetags.put(tag, tagModel);
    }

}
