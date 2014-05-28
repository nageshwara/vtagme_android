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

    private LruCache<String, HashtagModel> mHashtags;
    private LruCache<String, PrivatetagModel> mPrivatetags;

    public HashtagModel getHashTagModel(String tag) {
        return mHashtags.get(tag);
    }
    public void putHashTagModel(String tag, HashtagModel tagModel) {
        mHashtags.put(tag, tagModel);
    }

    public PrivatetagModel getPrivateTagModel(String tag) {
        return mPrivatetags.get(tag);
    }
    public void putPrivateTagModel(String tag, PrivatetagModel tagModel) {
        mPrivatetags.put(tag, tagModel);
    }

}
