package me.vtag.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import it.sephiroth.android.library.widget.HListView;
import me.vtag.app.R;
import me.vtag.app.backend.models.HashtagModel;

/**
 * Created by nageswara on 6/6/14.
 */
public class RelatedTagsHListView extends HListView {
    private ArrayAdapter<String> mAdapter;
    public RelatedTagsHListView(Context context) {
        super(context);
        initializeAdapter(context);
    }

    public RelatedTagsHListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAdapter(context);
    }

    public RelatedTagsHListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeAdapter(context);
    }

    private void initializeAdapter(Context context) {
        mAdapter = new ArrayAdapter<String>(context, R.layout.tag_hlist_card, R.id.tagTitleView);
        this.setAdapter(mAdapter);
    }

    public void setTags(String[] tags) {
        mAdapter.clear();
        mAdapter.addAll(tags);
        if (tags.length == 0) {
            this.setVisibility(INVISIBLE);
        } else {
            this.setVisibility(VISIBLE);
        }
    }
}
