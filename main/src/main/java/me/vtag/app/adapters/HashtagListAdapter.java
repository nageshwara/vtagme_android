package me.vtag.app.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.views.TagListItemView;

/**
 * Created by nmannem on 30/10/13.
 */
public class HashtagListAdapter extends ArrayAdapter<HashtagModel> {
    private final Activity context;
    private final List<HashtagModel> objects;

    public HashtagListAdapter(Activity context, int textViewResourceId, List<HashtagModel> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashtagModel meta = objects.get(position);
        TagListItemView tagCardView = null;
        if (convertView == null) {
            tagCardView = new TagListItemView(getContext());
        } else {
            tagCardView = (TagListItemView) convertView;
        }
        tagCardView.setModel(meta);
        return tagCardView;
    }
}
