package me.vtag.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.helpers.StringUtil;

/**
 * Created by nageswara on 5/3/14.
 */
public class TagListItemView extends FrameLayout implements View.OnClickListener {

    private View view;
    private BaseTagModel model;

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
        this.setOnClickListener(this);
    }

    public void setModel(BaseTagModel model) {
        this.model = model;

        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        TextView title = (TextView) view.findViewById(R.id.tagTitleView);
        TextView followers = (TextView) view.findViewById(R.id.followersCount);
        TextView videos = (TextView) view.findViewById(R.id.videosCount);

        title.setText(model.tag);
        followers.setText(StringUtil.formatNumber(model.followers));
        videos.setText(StringUtil.formatNumber(model.videocount));

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(8)
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .oval(false)
                .build();
        Picasso.with(this.getContext()).load(model.videodetails.get(0).video.thumb)
                .fit().centerCrop()
                .transform(transformation)
                .into(image);
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
