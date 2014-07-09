package me.vtag.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.VtagApplication;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.helpers.VtagmeCallback;

/**
 * Created by nageswara on 5/3/14.
 */
public class HashtagListItemView extends FrameLayout implements View.OnClickListener {
    private View view;
    private HashtagModel model;

    private TextView mTitle;
    private TextView mFollowers;
    private TextView mVideos;
    private FontAwesomeText mSubscribe;
    private LinearLayout mVideosSampleList;
    private BaseVideoListItemView[] mVideoCards;


    private static final int NUM_VIDOES_IN_PREVIEW = 3;

    public HashtagListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public HashtagListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HashtagListItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        view = inflate(getContext(), R.layout.home_page_tags_view, this);
        mSubscribe = (FontAwesomeText) view.findViewById(R.id.subscribeButton);
        mTitle = (TextView) view.findViewById(R.id.tagTitleView);

        mVideosSampleList = (LinearLayout) view.findViewById(R.id.video_samples_list);
        TextView tagDetails = (TextView) view.findViewById(R.id.more);

        mFollowers = (TextView) view.findViewById(R.id.followersCount);
        //mVideos = (TextView) view.findViewById(R.id.videosCount);
        tagDetails.setOnClickListener(this);
        mSubscribe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                HashtagListItemView.this.onToggleSubscription();
            }
        });


        if (VtagApplication.getInstance().isUserLoggedin()) {
            mSubscribe.setVisibility(VISIBLE);
        }
        else {
            mSubscribe.setVisibility(INVISIBLE);
        }

        mVideoCards =  new BaseVideoListItemView[NUM_VIDOES_IN_PREVIEW];
        for (int i=0;i<NUM_VIDOES_IN_PREVIEW;i++) {
            mVideoCards[i] = new BaseVideoListItemView(getContext());
            mVideosSampleList.addView(mVideoCards[i]);
        }
    }

    public void setModel(HashtagModel model) {
        this.model = model;

        mTitle.setText("#" + model.tag);
        mFollowers.setText(""+model.followers);
        for (int i = 0; i < NUM_VIDOES_IN_PREVIEW; i++) {
            VideoModel videoModel  = model.videodetails.get(i);
            mVideoCards[i].setModel(videoModel);
        }
        refresh();
    }

    private void refresh() {
        mSubscribe.stopAnimation();
        if (model.following) {
            mSubscribe.setTextColor(getResources().getColor(R.color.vtag_red));
            mSubscribe.setIcon("fa-minus-square");
        } else {
            mSubscribe.setTextColor(getResources().getColor(R.color.vtag_blue));
            mSubscribe.setIcon("fa-plus-square");
        }
    }

    private void onToggleSubscription() {
        if (model instanceof HashtagModel) {
            mSubscribe.setTextColor(getResources().getColor(R.color.gray));
            mSubscribe.setIcon("fa-spinner");
            mSubscribe.startRotate(getContext(), true, FontAwesomeText.AnimationSpeed.SLOW);
            VtagmeCallback callback = new VtagmeCallback() {
                @Override
                public void onComplete(boolean success) {
                    refresh();
                }
            };
            if (model.following) {
                model.unfollow(callback);
            }
            else {
                model.follow(callback);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (model != null) {
            if (getContext() instanceof HomeActivity) {
                ((HomeActivity) getContext()).browseHashTag(model.tag);
            }
        }
    }
}
