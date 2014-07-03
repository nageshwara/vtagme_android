package me.vtag.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.backend.models.CommentModel;
import me.vtag.app.backend.models.CommentsDescribeModel;
import me.vtag.app.backend.models.PanelListItemModel;
import me.vtag.app.backend.models.UserModel;

/**
 * Created by anuraag on 19/6/14.
 */
public class CommentsListAdapter extends ArrayAdapter<CommentsDescribeModel> {
    private Context context;
    private ArrayList<CommentsDescribeModel> usermodels;

    public CommentsListAdapter(Context context, int layoutResourceId, ArrayList<CommentsDescribeModel> usermodels){
        super(context, layoutResourceId, usermodels);
        this.context = context;
        this.usermodels = usermodels;
    }

    @Override
    public int getCount() {
        return usermodels.size();
    }

    @Override
    public CommentsDescribeModel getItem(int position) {
        return usermodels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.panel_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
        txtCount.setVisibility(View.INVISIBLE);
        txtTitle.setText(usermodels.get(position).c);

        Picasso.with(this.getContext()).load("http://lh6.googleusercontent.com/-zL2hGAaDwmU/AAAAAAAAAAI/AAAAAAAB1uE/0DFjl0Aqotw/s120-c/photo.jpg")
                .into(imgIcon);

        Log.w("THe height of gajira "+Float.toString(convertView.getHeight()),"Myapp ");

        return convertView;
    }
}
