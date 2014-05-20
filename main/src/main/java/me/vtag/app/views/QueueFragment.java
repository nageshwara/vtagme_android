package me.vtag.app.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import java.util.List;

import me.vtag.app.R;
import me.vtag.app.adapters.QueueVideoListAdapter;
import me.vtag.app.adapters.VideoListAdapter;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.pages.VideoPlayerActivity;

public class QueueFragment extends Fragment {
    private View mMainView;

    private TextView mQueueTitle;
    private TextView mQueueStatus;

    private FontAwesomeText mNextButton;
    private FontAwesomeText mPrevButton;
    private FontAwesomeText mFavButton;

    private ListView mVideoListView;

    private BaseTagModel mTagModel;
    private int mIndex;
    private VideoListAdapter mVideoList;

    public QueueFragment() {
        super();
        mIndex = 0;
    }

    public void setTag(BaseTagModel tagModel) {
        mTagModel = tagModel;
        createVideoListAdapter();
    }

    public void play(VideoModel model) {
        playAt(mTagModel.videodetails.indexOf(model));
    }
    public void next() {
        playAt(mIndex+1);
    }
    public void prev() {
        playAt(mIndex-1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.video_queue_fragment, container, false);
        mQueueTitle = (TextView) mMainView.findViewById(R.id.queueTitle);
        mQueueStatus = (TextView) mMainView.findViewById(R.id.queueStatus);

        mNextButton = (FontAwesomeText) mMainView.findViewById(R.id.nextButton);
        mPrevButton = (FontAwesomeText) mMainView.findViewById(R.id.prevButton);
        mFavButton = (FontAwesomeText) mMainView.findViewById(R.id.favoriteButton);

        mVideoListView = (ListView) mMainView.findViewById(R.id.videoListView);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });
        return mMainView;
    }

    @Override
    public void onActivityCreated(Bundle saved) {
        super.onActivityCreated(saved);
        createVideoListAdapter();
        refreshUI();
    }

    private void createVideoListAdapter() {
        if (getActivity() != null) {
            mVideoList = new QueueVideoListAdapter(getActivity(), R.layout.videocard, mTagModel.videodetails);
            mVideoListView.setAdapter(mVideoList);
        }
    }

    private void refreshUI() {
        mQueueTitle.setText("#" + mTagModel.tag);
        mQueueStatus.setText(mIndex + "/" + mVideoList.getCount());
    }

    public void playAt(int index) {
        List<VideoModel> videoModels = mTagModel.videodetails;
        int videoListLength = videoModels.size();
        index = (index + videoListLength) % videoListLength;
        VideoModel videoModel = videoModels.get(index);

        if (videoModel == null || getActivity() == null) return;
        mIndex = index;
        // Scroll to the respective list item..
        mVideoListView.smoothScrollToPosition(index);
        videoModel.play(getActivity());
        refreshUI();
    }
}
