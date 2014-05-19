package me.vtag.app.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import me.vtag.app.R;
import me.vtag.app.backend.models.VideoModel;

public class QueueFragment extends Fragment {
    private View mMainView;

    private TextView mQueueTitle;
    private TextView mQueueStatus;

    private FontAwesomeText mNextButton;
    private FontAwesomeText mPrevButton;
    private FontAwesomeText mFavButton;

    public QueueFragment() {
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

        mQueueTitle.setText("# Tagtitle");
        mQueueStatus.setText("2/5");
        return mMainView;
    }

    @Override
    public void onActivityCreated(Bundle saved) {
        super.onActivityCreated(saved);
    }
}
