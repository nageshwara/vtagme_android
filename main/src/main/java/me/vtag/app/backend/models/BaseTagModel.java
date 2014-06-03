package me.vtag.app.backend.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nmannem on 30/10/13.
 */
public class BaseTagModel {
    public String id;
    public String tag;
    public String description;
    public int videocount;
    public String url;

    public int featured;
    public int tagtype; // 0 - hashtag, 1 - multitag, 2 - publictag, 3 - privatetag.

    public int updates;
    public boolean following;

    public HashMap<String, Integer> contributors;
    public HashMap<String, UserModel> users;

    public long[] vids;
    public List<VideoModel> videodetails;

    public String next_cursor;
    public boolean has_next;
    public String[] related;

    //activity
    public List<ActivityModel> activity;

    public BaseTagModel() {}
}
