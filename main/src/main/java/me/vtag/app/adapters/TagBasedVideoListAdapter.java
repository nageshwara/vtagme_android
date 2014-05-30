package me.vtag.app.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.helpers.VtagmeLoaderView;
import me.vtag.app.views.BaseVideoListItemView;
import me.vtag.app.views.TagBasedVideoListItemView;

/**
 * Created by nmannem on 30/10/13.
 */
public class TagBasedVideoListAdapter extends VideoListAdapter {
    private BaseTagModel mTagModel;
    public TagBasedVideoListAdapter(BaseTagModel tagModel, Activity context, int resourceId, List<VideoModel> objects, VtagmeLoaderView loaderView) {
        super(context, resourceId, objects, loaderView);
        mTagModel = tagModel;
    }

    @Override
    protected boolean shouldFetchNextBatch(int position) {
        if (position == (this.getCount()-3) && mTagModel.has_next)
        {
            return true;
        }
        return false;
    }

    @Override
    protected void fetchNextBatch() {
        if (mTagModel.next_cursor == null) return;
        super.fetchNextBatch();
        VtagClient.getAPI().getTagDetailsAdvanced(mTagModel.id, "featured", mTagModel.next_cursor, new Callback<HashtagModel>() {
            @Override
            public void onResponse(Response<HashtagModel> hashtagModelResponse) {
                HashtagModel tagModel = hashtagModelResponse.getResult();
                if (tagModel != null) {
                    //mTagModel = tagModel;
                    mTagModel.has_next = tagModel.has_next;
                    mTagModel.next_cursor = tagModel.next_cursor;
                    appendNextBatch(tagModel.videodetails);
                } else {
                    // TODO throw an error.
                }
            }
        });
    }

    @Override
    protected BaseVideoListItemView createView() {
        return new TagBasedVideoListItemView(mTagModel, getContext());
    }
}
