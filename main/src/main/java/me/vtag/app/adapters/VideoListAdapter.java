package me.vtag.app.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import me.vtag.app.backend.models.VideoMetaModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.views.VideoListItemView;

/**
 * Created by nmannem on 30/10/13.
 */
public class VideoListAdapter extends ArrayAdapter<VideoModel> {
    private final Activity context;
    private final List<VideoModel> objects;

    public VideoListAdapter(Activity context, int textViewResourceId, List<VideoModel> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VideoModel videoModel = objects.get(position);
        VideoListItemView videoCardView = null;
        if (convertView == null) {
            videoCardView = new VideoListItemView(getContext());
        } else {
            videoCardView = (VideoListItemView) convertView;
        }
        videoCardView.setModel(videoModel);
        return videoCardView;                
    }
}
