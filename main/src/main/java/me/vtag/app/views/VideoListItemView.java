package me.vtag.app.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.vtag.app.CustomImageView;
import me.vtag.app.R;
import me.vtag.app.backend.models.VideoMetaModel;
import me.vtag.app.pages.VideoPlayerActivity;

/**
 * Created by nageswara on 5/3/14.
 */
public class VideoListItemView extends FrameLayout implements View.OnClickListener {

    private View view;
    private VideoMetaModel model;

    public VideoListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public VideoListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VideoListItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        view = inflate(getContext(), R.layout.videocard, null);
        addView(view);
        this.setOnClickListener(this);
    }

    public void setModel(VideoMetaModel model) {
        this.model = model;

        ImageView image = (CustomImageView) view.findViewById(R.id.imageView1);
        TextView text = (TextView) view.findViewById(R.id.videoTitleView);
        TextView text1 = (TextView) view.findViewById(R.id.views);
        TextView text2 = (TextView) view.findViewById(R.id.likes);
        TextView text3 = (TextView) view.findViewById(R.id.duration);


        text.setText(model.title);
        text1.setText("Views: "+Integer.toString(model.views));
        text2.setText( " . "+Integer.toString(model.likes));
        text3.setText(Integer.toString((model.duration) / 3600)+":"+Integer.toString(((model.duration) % 3600)/60)+":"+Integer.toString(((model.duration) % 3600) %60) );
        Picasso.with(this.getContext()).load(model.thumb)
                .resizeDimen(R.dimen.videolist_thumb_width, R.dimen.videolist_thumb_height)
                .centerInside()
                .into(image);
    }

    public VideoMetaModel getModel() {
        return model;
    }

    @Override
    public void onClick(View view) {
        if (model != null) {
            Intent intent = null;
            if(model.type.equals("youtube")) {
                intent = new Intent(getContext(), VideoPlayerActivity.class);
            }
            if (intent != null) {
                intent.putExtra("meta", model);
                ((Activity)getContext()).startActivity(intent);
            }
            //Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), YOUTUBE_API_KEY, holder.model.typeid);
            //startActivity(intent);
        }
    }
}
