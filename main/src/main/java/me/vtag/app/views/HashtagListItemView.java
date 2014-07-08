package me.vtag.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.VtagApplication;
import me.vtag.app.adapters.SampleVideosAdapter;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.CacheManager;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.helpers.StringUtil;
import me.vtag.app.helpers.VtagmeCallback;

/**
 * Created by nageswara on 5/3/14.
 */
public class HashtagListItemView extends FrameLayout implements View.OnClickListener {

    static int apple= 0;
    private View view;
    private HashtagModel model;

    private ImageView mImage;
    private TextView mTitle;
    private TextView mFollowers;
    private TextView mVideos;
    private FontAwesomeText mSubscribe;
    private ListView mVideosSampleList;

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
/*        view = inflate(getContext(), R.layout.home_page_tags_view,null);
        addView(view);
        mImage = (ImageView) view.findViewById(R.id.imageView);
        mTitle = (TextView) view.findViewById(R.id.tagTitleView);
        mVideosSampleList = (ListView) view.findViewById(R.id.video_samples_list);
*/
//        view = inflate(getContext(), R.layout.tagcard, null);
        view = inflate(getContext(), R.layout.home_page_tags_view,null);
        addView(view);
        mSubscribe = (FontAwesomeText) view.findViewById(R.id.subscribeButton);
        mImage = (ImageView) view.findViewById(R.id.imageView);
        mTitle = (TextView) view.findViewById(R.id.tagTitleView);

        mVideosSampleList = (ListView) view.findViewById(R.id.video_samples_list);
        TextView tagDetails = (TextView) view.findViewById(R.id.more);

//        mFollowers = (TextView) view.findViewById(R.id.followersCount);
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
    }
    private HashtagModel tagModel = null;
    private SampleVideosAdapter mAdapter = null;

    public void setModel(HashtagModel model) {
        this.model = model;

        mTitle.setText("#" + model.tag);
//        mFollowers.setText(StringUtil.formatNumber(model.followers));
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

        mAdapter = new SampleVideosAdapter(this.getContext(), R.layout.videocard, model);
        mVideosSampleList.setAdapter(mAdapter);

        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View listItem = mAdapter.getView(i, null, mVideosSampleList);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = mVideosSampleList.getLayoutParams();
        params.height = totalHeight + (mVideosSampleList.getDividerHeight() * (mAdapter.getCount() - 1));
        mVideosSampleList.setLayoutParams(params);
        mVideosSampleList.requestLayout();


//        refresh();
        final String tag_id = model.id;
/*
        if(apple == 0) {
            VtagClient.getAPI().getTagDetails(model.id, "featured", new Callback<HashtagModel>() {
                @Override
                public void onResponse(Response<HashtagModel> hashtagModelResponse) {
                    tagModel = hashtagModelResponse.getResult();
                    if (tagModel != null) {
                        Log.w("onResponse " + tag_id+" length is "+tagModel.videodetails.size(), "Myapp");
                        CacheManager.getInstance().putHashTagModel(tag_id + "_featured", tagModel);
                        //HomeActivity.getRightDrawerFragment().new_tag_clicked(hashtagModel);
//                    renderVideoList(tagModel);
                        mAdapter.clear();
                        mAdapter.addAll(tagModel.videodetails.subList(1,4));
                        mAdapter.notifyDataSetChanged();
                        mVideosSampleList.setAdapter(mAdapter);
                        Log.w("height is "+Float.toString(mVideosSampleList.getHeight()),"Myapp");
//                        mVideosSampleList.setDividerHeight(2dp);
                    } else {
//                    Toast.makeText(getContext(), "Couldnt get mHashtagModel details!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            apple = 1;
        }

        if(tagModel == null) {
            List<VideoModel> temp = new ArrayList<>();
            mAdapter = new SampleVideosAdapter(this.getContext(), R.layout.videocard, temp);
        }
//        mVideosSampleList.setAdapter(mAdapter);
*/
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
