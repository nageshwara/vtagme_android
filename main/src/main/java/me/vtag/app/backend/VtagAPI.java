package me.vtag.app.backend;

import ly.apps.android.rest.cache.CachePolicy;
import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.annotations.Cached;
import ly.apps.android.rest.client.annotations.GET;
import ly.apps.android.rest.client.annotations.POST;
import ly.apps.android.rest.client.annotations.Path;
import ly.apps.android.rest.client.annotations.QueryParam;
import ly.apps.android.rest.client.annotations.RestService;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.PrivatetagModel;
import me.vtag.app.backend.vos.LoginVO;
import me.vtag.app.backend.vos.RootVO;

/**
 * Created by nageswara on 5/20/14
 */
@RestService
public interface VtagAPI {
    @GET("/?mobile=true")
    //@Cached(policy = CachePolicy.NETWORK_ENABLED)
    void getBootstrap(Callback<RootVO> callback);

    @POST("/mobile_signup_process")
    void socailSignup(@QueryParam("id") String id, @QueryParam("provider") String provider,
                      @QueryParam("name") String name, @QueryParam("email") String email,
                      @QueryParam("access_token") String access_token,
                      Callback<LoginVO> callback);
    @POST("/signup_process")
    void finishSignup(@QueryParam("username") String username, @QueryParam("email") String email,
                      @QueryParam("password") String password,
                      Callback<LoginVO> callback);

    @GET("/video/{id}")
    //@Cached(policy = CachePolicy.NETWORK_ENABLED)
    void getVideoDetails(@Path("id") String id, Callback<RootVO> callback);

    @GET("/tag/{id}")
    //@Cached(policy = CachePolicy.NETWORK_ENABLED)
    void getTagDetails(@Path("id") String id, @QueryParam("sorttype") String sortType, Callback<HashtagModel> callback);

    @POST("/tag/{id}")
    void getTagDetailsAdvanced(@Path("id") String id, @QueryParam("sorttype") String sortType, @QueryParam("next_cursor") String nextCursor, Callback<HashtagModel> callback);


    @GET("/privatetag/{id}")
    void getPrivateTagDetails(@Path("id") String id, Callback<PrivatetagModel> callback);

    @GET("/tag/follow/{id}")
    void followTag(@Path("id") String id, @QueryParam("userid") String userid, Callback<String> callback);

    @GET("/tag/unfollow/{id}")
    void unfollowTag(@Path("id") String id, @QueryParam("userid") String userid, Callback<String> callback);

    @GET("/publictag/follow/{id}")
    void followPublictag(@Path("id") String id, @QueryParam("userid") String userid, Callback<String> callback);

    @GET("/publictag/unfollow/{id}")
    void unfollowPublictag(@Path("id") String id, @QueryParam("userid") String userid, Callback<String> callback);

}
