package me.vtag.app.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import me.vtag.app.backend.models.PublictagModel;
import me.vtag.app.views.PublictagListItemView;

/**
 * Created by nmannem on 30/10/13.
 */
public class PublictagListAdapter extends ArrayAdapter<PublictagModel> {
    private final Activity context;
    private final List<PublictagModel> objects;

    public PublictagListAdapter(Activity context, int textViewResourceId, List<PublictagModel> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PublictagModel meta = objects.get(position);
        PublictagListItemView tagCardView = null;
        if (convertView == null) {
            tagCardView = new PublictagListItemView(getContext());
        } else {
            tagCardView = (PublictagListItemView) convertView;
        }
        tagCardView.setModel(meta);
        return tagCardView;
    }
}
