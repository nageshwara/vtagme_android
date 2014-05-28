package me.vtag.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.VtagApplication;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.helpers.StringUtil;

/**
 * Created by nageswara on 5/3/14.
 */
public class TagListItemView extends FrameLayout implements View.OnClickListener {

    private View view;
    private BaseTagModel model;

    private ImageView mImage;
    private TextView mTitle;
    private TextView mFollowers;
    private TextView mVideos;
    private FontAwesomeText mSubscribe;

    public TagListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public TagListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TagListItemView(Context context) {
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
                TagListItemView.this.onToggleSubscription();
            }
        });


        if (VtagApplication.getInstance().isUserLoggedin()) {
            mSubscribe.setVisibility(VISIBLE);
        }
        else {
            mSubscribe.setVisibility(INVISIBLE);
        }

    }

    public void setModel(BaseTagModel model) {
        this.model = model;

        mTitle.setText("#" + model.tag);
        mFollowers.setText(StringUtil.formatNumber(model.followers));
        //videos.setText(StringUtil.formatNumber(model.videocount));
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(8)
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .oval(false)
                .build();
        Picasso.with(this.getContext()).load(model.videodetails.get(0).video.thumb)
                .fit().centerCrop()
                .transform(transformation)
                .into(mImage);
        refresh();
    }

    private void refresh() {
        if (model.following) {
            mSubscribe.setIcon("fa-minus-square");
        } else {
            mSubscribe.setIcon("fa-plus-square");
        }
    }

    private void onToggleSubscription() {
        if (model.following) {
            VtagClient.getAPI().getUnFollowTag(model.tag, VtagApplication.getInstance().authPreferences.getUser(), new Callback<String>() {
                @Override
                public void onResponse(Response<String> baseTagModelResponse) {
                    String response_true = baseTagModelResponse.getResult();
                    Log.w("Unfollow UserName is: " + VtagApplication.getInstance().authPreferences.getUser(), "Myapp ");
                    if (response_true == "true") {
                        model.following = !model.following;
                        refresh();
                    }
                }
            });

        }
        else {
            VtagClient.getAPI().getFollowTag(model.tag, VtagApplication.getInstance().authPreferences.getUser(), new Callback<String>() {
                @Override
                public void onResponse(Response<String> baseTagModelResponse) {
                    String response_true = baseTagModelResponse.getResult();
                    Log.w("UserName is: " + VtagApplication.getInstance().authPreferences.getUser(), "Myapp ");
                    if (response_true == "true") {
                        model.following = !model.following;
                        refresh();
                    }
                }
            });
        }

        // Now make a call to backend.
    }

    public BaseTagModel getModel() {
        return model;
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
