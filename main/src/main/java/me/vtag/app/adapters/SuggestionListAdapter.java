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

/**
 * Created by anuraag on 19/6/14.
 */
public class SuggestionListAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> usermodels;

    public SuggestionListAdapter(Context context, int layoutResourceId, ArrayList<String> usermodels){
        super(context, layoutResourceId, usermodels);
        this.context = context;
    }

    @Override
    public int getCount() {
        return usermodels.size();
    }

    @Override
    public String getItem(int position) {
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
        txtTitle.setText(usermodels.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("Clicked suggestion tag","Myapp ");
            }
        });


        return convertView;
    }
}
