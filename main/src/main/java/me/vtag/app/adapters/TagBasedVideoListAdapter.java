package me.vtag.app.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.views.BaseVideoListItemView;
import me.vtag.app.views.TagBasedVideoListItemView;

/**
 * Created by nmannem on 30/10/13.
 */
public class TagBasedVideoListAdapter extends VideoListAdapter {
    private BaseTagModel mTagModel;
    public TagBasedVideoListAdapter(BaseTagModel tagModel, Activity context, int resourceId, List<VideoModel> objects) {
        super(context, resourceId, objects);
        mTagModel = tagModel;
    }
    protected BaseVideoListItemView createView() {
        return new TagBasedVideoListItemView(mTagModel, getContext());
    }
}
