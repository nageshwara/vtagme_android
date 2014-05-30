package me.vtag.app.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Collection;
import java.util.List;

import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.helpers.VtagmeLoaderView;
import me.vtag.app.views.BaseVideoListItemView;

/**
 * Created by nmannem on 30/10/13.
 */
public class VideoListAdapter extends ArrayAdapter<VideoModel> {
    private final Activity context;
    private final List<VideoModel> objects;
    private final VtagmeLoaderView mLoaderView;
    protected boolean mFetchingNext;

    public VideoListAdapter(Activity context, int resourceId, List<VideoModel> objects, VtagmeLoaderView loaderView) {
        super(context, resourceId, objects);
        this.context = context;
        this.objects = objects;
        mLoaderView = loaderView;
    }

    public VideoListAdapter(Activity context, int resourceId, List<VideoModel> objects) {
        super(context, resourceId, objects);
        this.context = context;
        this.objects = objects;
        mLoaderView = null;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (shouldFetchNextBatch(position))
        {
            fetchNextBatch();
        }

        VideoModel videoModel = objects.get(position);
        BaseVideoListItemView videoCardView = null;
        if (convertView == null) {
            videoCardView = createView();
        } else {
            videoCardView = (BaseVideoListItemView) convertView;
        }
        videoCardView.setModel(videoModel);
        return videoCardView;                
    }

    protected boolean shouldFetchNextBatch(int position) {
        return false;
    }

    protected void fetchNextBatch() {
        if (mFetchingNext) return;

        // Show loading bar.
        if (mLoaderView != null) {
            mLoaderView.showLoading();
        }
        mFetchingNext = true;
    }

    public void appendNextBatch(Collection<VideoModel> newVideos) {
        mFetchingNext = false;
        this.objects.addAll(newVideos);
        this.notifyDataSetChanged();
        // Hide loading bar.
        if (mLoaderView != null) {
            mLoaderView.hideLoading();
        }
    }

    protected BaseVideoListItemView createView() {
        return new BaseVideoListItemView(getContext());
    }
}
