package me.vtag.app.views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import me.vtag.app.R;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.helpers.StringUtil;

/**
 * Created by nageswara on 5/3/14.
 */
public class BaseVideoListItemView extends FrameLayout implements View.OnClickListener {

    protected View view;
    protected VideoModel model;

    private TextView title;
    private TextView likes;
    private TextView views;
    private TextView duration;
    private ImageView mThumbnail;

    public int position;

    public BaseVideoListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public BaseVideoListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BaseVideoListItemView(Context context) {
        super(context);
        initView();
    }

    protected void initView() {
        view = inflate(getContext(), R.layout.videocard, null);
        addView(view);
        mThumbnail = (ImageView) view.findViewById(R.id.imageView);
        title = (TextView) view.findViewById(R.id.videoTitleView);
        likes = (TextView) view.findViewById(R.id.likesCount);
        views = (TextView) view.findViewById(R.id.viewsCount);
        duration = (TextView) view.findViewById(R.id.durationtxt);
        this.setOnClickListener(this);
    }

    public void setModel(final VideoModel model) {
        this.model = model;
        title.setText(model.video.title);
        likes.setText(StringUtil.formatNumber(model.video.likes));
        views.setText(StringUtil.formatNumber(model.video.views));
        duration.setText(StringUtil.durationFromSeconds(model.video.duration));
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(6)
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .oval(false)
                .build();
        Picasso.with(this.getContext()).load(model.video.thumb).fit().centerCrop().transform(transformation).into(mThumbnail);
    }

    @Override
    public void onClick(View view) {
        /**
         * If Tag is different, set the tag in the queue.
         */
        model.play(getContext());
    }
}
