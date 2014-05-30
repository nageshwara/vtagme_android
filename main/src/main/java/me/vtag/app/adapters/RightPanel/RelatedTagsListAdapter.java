package me.vtag.app.adapters.RightPanel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.vtag.app.R;
import me.vtag.app.models.PanelListItemModel;

/**
 * Created by anuraag on 29/5/14.
 */
public class RelatedTagsListAdapter extends ArrayAdapter<PanelListItemModel> {
    private Context context;
    private ArrayList<PanelListItemModel> activityListItemModels;

    public RelatedTagsListAdapter(Context context, int layoutResourceId, ArrayList<PanelListItemModel> activityListItemModels){
        super(context, layoutResourceId, activityListItemModels);
        this.context = context;
        this.activityListItemModels = activityListItemModels;
        Log.w("Came to the ActivityListAdapter class's constructor ", "Myapp ");
    }

    @Override
    public int getCount() {
        return activityListItemModels.size();
    }

    @Override
    public PanelListItemModel getItem(int position) {
        return activityListItemModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.w("Came to getView of ActivityListAdapter ","Myapp ");
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.panel_list_item, null);
        }

//        Log.w("Came to ActivityListAdapter", "Myapp ");
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        //imgIcon.setImageResource(activityListItemModels.get(position).getIcon());
        txtTitle.setText(activityListItemModels.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if(activityListItemModels.get(position).getCounterVisibility()){
            txtCount.setText(activityListItemModels.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }
        return convertView;
    }
}
