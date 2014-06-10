package me.vtag.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.Random;

import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.VtagApplication;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.helpers.StringUtil;
import me.vtag.app.helpers.VtagmeCallback;

/**
 * Created by nageswara on 5/3/14.
 */
public class HashtagListItemView extends FrameLayout implements View.OnClickListener {

    private View view;
    private HashtagModel model;

    private ImageView mImage;
    private TextView mTitle;
    private TextView mFollowers;
    private TextView mVideos;
    private FontAwesomeText mSubscribe;

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
        view = inflate(getContext(), R.layout.tagcard, null);
        addView(view);
        mSubscribe = (FontAwesomeText) view.findViewById(R.id.subscribeButton);
        mImage = (ImageView) view.findViewById(R.id.imageView);
        mTitle = (TextView) view.findViewById(R.id.tagTitleView);
        mFollowers = (TextView) view.findViewById(R.id.followersCount);
        //mVideos = (TextView) view.findViewById(R.id.videosCount);
        this.setOnClickListener(this);
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

    }

    public void setModel(HashtagModel model) {
        this.model = model;

        mTitle.setText("#" + model.tag);
        mFollowers.setText(StringUtil.formatNumber(model.followers));
        //videos.setText(StringUtil.formatNumber(model.videocount));
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(6)
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .oval(false)
                .build();
        Picasso.with(this.getContext()).load(getRandomUrl())
                .fit().centerCrop()
                .transform(transformation)
                .into(mImage);
        refresh();
    }

    private String getRandomUrl() {
        List<VideoModel> videoModelList = model.videodetails;
        Random random = new Random(videoModelList.size());
        return videoModelList.get(random.nextInt() % videoModelList.size()).video.thumb;
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
            // Show loading circle until we get mesg back from server.
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
