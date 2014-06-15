package me.vtag.app.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.vtag.app.R;
import me.vtag.app.backend.models.UserModel;
import me.vtag.app.backend.models.VideoModel;

public class VideoDetailsFragment extends Fragment {
    private VideoModel mVideoModel;
    private View mMainView;

    private TextView mVideoTitle;
    private TextView mVideoDesc;
    private RelativeLayout mTagsContainer;

    public VideoDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mVideoModel = getArguments().getParcelable("video");

        mMainView = inflater.inflate(R.layout.fragment_videodetails, container, false);
        mVideoTitle = (TextView) mMainView.findViewById(R.id.videoTitle);
        mVideoDesc = (TextView) mMainView.findViewById(R.id.videoDesc);
        mTagsContainer = (RelativeLayout) mMainView.findViewById(R.id.tagsContainer);

        if (mVideoModel != null) {
            mVideoTitle.setText(mVideoModel.video.title);
            mVideoDesc.setText(mVideoModel.video.description);
            mTagsContainer.removeAllViewsInLayout();
        }

        return mMainView;
    }

    @Override
    public void onActivityCreated(Bundle saved) {
        super.onActivityCreated(saved);
    }
}
