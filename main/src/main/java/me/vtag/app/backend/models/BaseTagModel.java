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
    public int followers;
    public String url;

    public int updates;
    public boolean following;
    public boolean isPrivate;
    public boolean isMulti;

    public HashMap<String, Integer> contributors;
    public HashMap<String, UserModel> users;

    public long[] vids;
    public List<VideoModel> videodetails;

    public String next_cursor;
    public String[] related;

    //activity
}
