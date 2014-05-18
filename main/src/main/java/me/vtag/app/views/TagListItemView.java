package me.vtag.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.vtag.app.R;
import me.vtag.app.WelcomeActivity;
import me.vtag.app.backend.models.BaseTagModel;

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
        TextView text = (TextView) view.findViewById(R.id.tagTitleView);

        text.setText(model.tag);
        Picasso.with(this.getContext()).load(model.videodetails.get(0).video.thumb)
                .resizeDimen(R.dimen.videolist_thumb_width, R.dimen.videolist_thumb_height)
                .centerInside()
                .into(image);
    }

    public BaseTagModel getModel() {
        return model;
    }

    @Override
    public void onClick(View view) {
        if (model != null) {
            if (getContext() instanceof  WelcomeActivity) {
                ((WelcomeActivity) getContext()).browseHashTag(model.tag);
            }
        }
    }
}
