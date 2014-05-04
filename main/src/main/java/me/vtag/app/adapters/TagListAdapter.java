package me.vtag.app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.vtag.app.R;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.VideoMetaModel;
import me.vtag.app.backend.models.VideoModel;
import me.vtag.app.views.TagListItemView;

/**
 * Created by nmannem on 30/10/13.
 */
public class TagListAdapter extends ArrayAdapter<BaseTagModel> {
    private final Activity context;
    private final List<BaseTagModel> objects;

    public TagListAdapter(Activity context, int textViewResourceId, List<BaseTagModel> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseTagModel meta = objects.get(position);
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
