package me.vtag.app.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import me.vtag.app.backend.models.PrivatetagModel;
import me.vtag.app.backend.models.PrivatetagModel;
import me.vtag.app.views.TagListItemView;

/**
 * Created by nmannem on 30/10/13.
 */
public class PrivatetagListAdapter extends ArrayAdapter<PrivatetagModel> {
    private final Activity context;
    private final List<PrivatetagModel> objects;

    public PrivatetagListAdapter(Activity context, int textViewResourceId, List<PrivatetagModel> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PrivatetagModel meta = objects.get(position);
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
