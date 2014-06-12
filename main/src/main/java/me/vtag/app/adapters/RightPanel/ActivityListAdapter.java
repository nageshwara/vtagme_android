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
import me.vtag.app.backend.models.PanelListItemModel;

/**
 * Created by anuraag on 29/5/14.
 */
public class ActivityListAdapter extends ArrayAdapter<PanelListItemModel> {
    private Context context;
    private ArrayList<PanelListItemModel> activityListItemModels;

    public ActivityListAdapter(Context context, int layoutResourceId, ArrayList<PanelListItemModel> activityListItemModels){
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
        return activityListItemModels.get(2*position);
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
        TextView txtDate = (TextView) convertView.findViewById(R.id.date_activity);

        String[] date = activityListItemModels.get(2*position+1).getTitle().split("-");
        String Month = null;
        String Day = date[2].split(" ")[0];
        switch (date[1]) {
            case "01":
                Month = "Jan";
                break;
            case "02":
                Month = "Feb";
                break;
            case "03":
                Month = "Mar";
                break;
            case "04":
                Month = "Apr";
                break;
            case "05":
                Month = "May";
                break;
            case "06":
                Month = "Jun";
                break;
            case "07":
                Month = "July";
                break;
            case "08":
                Month = "Aug";
                break;
            case "09":
                Month = "Sep";
                break;
            case "10":
                Month = "Oct";
                break;
            case "11":
                Month = "Nov";
                break;
            case "12":
                Month = "Dec";
                break;
        }
        //imgIcon.setImageResource(activityListItemModels.get(position).getIcon());
//        Log.w("In ActivityListAdapterPosition is "+Integer.toString(position),"Myapp ");
        txtTitle.setText("Anonymous added"+activityListItemModels.get(2*position).getTitle());
        txtDate.setText(Month+" "+Day);

//        Log.w("THe height is "+Integer.toString(convertView.getHeight()),"Myapp ");

        // displaying count
        // check whether it set visible or not
        if(activityListItemModels.get(2*position).getCounterVisibility()){
            txtCount.setText(activityListItemModels.get(2*position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }
        return convertView;
    }
}
