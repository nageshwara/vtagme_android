package me.vtag.app.adapters;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.helpers.VtagmeLoaderView;
import me.vtag.app.views.BaseVideoListItemView;
import me.vtag.app.views.QueueVideoListItemView;
import me.vtag.app.views.TagBasedVideoListItemView;

/**
 * Created by nmannem on 30/10/13.
 */
public class QueueVideoListAdapter extends VideoListAdapter {
    public QueueVideoListAdapter(Context context, int resourceId, List<VideoModel> objects) {
        super(context, resourceId, objects);
    }

    @Override
    protected BaseVideoListItemView createView() {
        return new QueueVideoListItemView(getContext());
    }
}
