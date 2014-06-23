package me.vtag.app.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import me.vtag.app.R;
import me.vtag.app.VtagApplication;
import me.vtag.app.adapters.CommentsListAdapter;
import me.vtag.app.backend.models.CommentsDescribeModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.pages.VideoPlayerActivity;

/**
 * Created by anuraag on 22/6/14.
 */
public class VideoQueueAndCommentsFragment extends Fragment {
    View mMainView;
    VideosComment mVideosComment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w("VideosComment view is onCreate", "Myapp");
        mMainView = inflater.inflate(R.layout.video_queue_comments_layout, container, false);

        QueueFragment queueFragment = VtagApplication.getInstance().getQueueFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.video_queue_container, queueFragment)
                .commit();
        Intent i = ((VideoPlayerActivity) getActivity()).getIntent();
        mVideosComment = new VideosComment();
        mVideosComment.fetchComments(((VideoModel) i.getParcelableExtra("video")).id);   //
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.video_comments_container,mVideosComment)
                .commit();


        return mMainView;
    }

}
