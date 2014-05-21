package me.vtag.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import me.vtag.app.VtagApplication;
import me.vtag.app.backend.models.BaseTagModel;

/**
 * Created by nageswara on 5/19/14.
 */
public class TagBasedVideoListItemView extends BaseVideoListItemView {
    BaseTagModel mTagModel;
    public TagBasedVideoListItemView(BaseTagModel tagModel, Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTagModel = tagModel;
    }

    public TagBasedVideoListItemView(BaseTagModel tagModel, Context context, AttributeSet attrs) {
        super(context, attrs);
        mTagModel = tagModel;
    }

    public TagBasedVideoListItemView(BaseTagModel tagModel, Context context) {
        super(context);
        mTagModel = tagModel;
    }

    @Override
    public void onClick(View view) {
        VtagApplication.getInstance().getQueueFragment().setTag(mTagModel);
        //WelcomeActivity.mQueueFragment.play(model);
        model.play(getContext());
    }

}
