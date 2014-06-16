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

import java.util.ArrayList;

import me.vtag.app.R;
import me.vtag.app.backend.models.PanelListItemModel;

/**
 * Created by nageswara on 5/1/14.
 */
public class PanelListAdapter extends ArrayAdapter<PanelListItemModel> {
    private Context context;
    private ArrayList<PanelListItemModel> panelListItemModels;

    public PanelListAdapter(Context context, int layoutResourceId, ArrayList<PanelListItemModel> panelListItemModels){
        super(context, layoutResourceId, panelListItemModels);
        this.context = context;
        this.panelListItemModels = panelListItemModels;
    }

    @Override
    public int getCount() {
        return panelListItemModels.size();
    }

    @Override
    public PanelListItemModel getItem(int position) {
        return panelListItemModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.panel_list_item, null);
        }

//        Log.w("Came to PanelListAdapter", "Myapp ");
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        //imgIcon.setImageResource(panelListItemModels.get(position).getIcon());
        txtTitle.setText(panelListItemModels.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if(panelListItemModels.get(position).getCounterVisibility()){
            txtCount.setText(panelListItemModels.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }
        return convertView;
    }
}
