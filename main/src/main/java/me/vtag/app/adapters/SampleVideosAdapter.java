package me.vtag.app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Collection;
import java.util.List;

import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.helpers.VtagmeLoaderView;
import me.vtag.app.views.BaseVideoListItemView;
import me.vtag.app.views.TagBasedVideoListItemView;

/**
 * Created by anuraag on 6/7/14.
 */
public class SampleVideosAdapter extends ArrayAdapter<VideoModel> {
    private final Context context;
    private final List<VideoModel> objects;
    private final VtagmeLoaderView mLoaderView;
    private BaseTagModel mTagModel;
    protected boolean mFetchingNext;
/*
    public SampleVideosAdapter(Context context, int resourceId, List<VideoModel> objects, VtagmeLoaderView loaderView) {
        super(context, resourceId, objects);
        Log.w("Contstructor1", "Myapp ");
        this.context = context;
        this.objects = objects;
        mLoaderView = loaderView;
    }
*/
    public SampleVideosAdapter(Context context, int resourceId, HashtagModel objects) {
        super(context, resourceId, objects.videodetails.subList(1,4));
//        Log.w("Contstructor2", "Myapp ");
        this.mTagModel = objects;
        this.context = context;
        this.objects = objects.videodetails.subList(1,4);
        mLoaderView = null;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VideoModel videoModel = objects.get(position);
        Log.w(videoModel.video.title,"Myapp ");
        BaseVideoListItemView videoCardView = null;
        if (convertView == null) {
            videoCardView = createView();
        } else {
            videoCardView = (BaseVideoListItemView) convertView;
        }
        videoCardView.position = position;
        videoCardView.setModel(videoModel);
        Log.w("getView of SampleVideosAdapter "+Float.toString(videoCardView.getHeight()), "Myapp ");
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
        // Hide loading bar.
        if (mLoaderView != null) {
            mLoaderView.hideLoading();
        }

        mFetchingNext = false;
        this.objects.addAll(newVideos);
        this.notifyDataSetChanged();
    }

    protected BaseVideoListItemView createView() {
        return new TagBasedVideoListItemView(mTagModel, getContext());
    }
/*
    protected BaseVideoListItemView createView() {
        return new BaseVideoListItemView(getContext());
    }
    */
}
