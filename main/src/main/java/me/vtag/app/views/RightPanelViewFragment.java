package me.vtag.app.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.vtag.app.R;
import me.vtag.app.adapters.PanelListAdapter;
import me.vtag.app.adapters.TagListAdapter;
import me.vtag.app.backend.models.ActivityModel;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.models.PanelListItemModel;

/**
 * Created by anuraag on 20/5/14.
 */
public class RightPanelViewFragment extends Fragment {

    private View mLeftPanelContainerView;
    private List<BaseTagModel> tagModel;
    private List<PanelListItemModel> relatedtags = new ArrayList<>();
    private List<PanelListItemModel> activities = new ArrayList<>();

    /*
    public RightPanelViewFragment(List<BaseTagModel> tagModel){
        this.tagModel = tagModel;
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        float pixels1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
        int pixels = (int) (pixels1);
        LayoutInflater mInflater1 = (LayoutInflater)
                getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View convertView = mInflater1.inflate(R.layout.panel_list_item, null);
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        Log.w("Came to onCreateView RightPanelViewFragment","Myapp ");
        mLeftPanelContainerView = inflater.inflate(R.layout.left_panel_list, container, false);
        Log.w("Came to onCreateView of RightPanelViewFragment after inflating left_panel_list","Myapp ");
        ListView relatedtags_list = (ListView) mLeftPanelContainerView.findViewById(R.id.related_tags_list);
//        relatedtags.setScrollContainer(false);
        relatedtags_list.setAdapter(new PanelListAdapter(getActivity(), 0, (java.util.ArrayList<PanelListItemModel>) this.relatedtags));
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) relatedtags_list.getLayoutParams();

        Log.w(Integer.toString(imgIcon.getHeight())+" "+Integer.toString(convertView.getHeight()),"Myapp 1");
        lp.height = pixels*relatedtags.size();
        relatedtags_list.setLayoutParams(lp);

        ListView activities_list = (ListView) mLeftPanelContainerView.findViewById(R.id.activities_list);
//        activities.setScrollContainer(false);
        activities_list.setAdapter(new PanelListAdapter(getActivity(),0,(java.util.ArrayList<PanelListItemModel>) this.activities));
        ViewGroup.LayoutParams lp1 = (ViewGroup.LayoutParams) activities_list.getLayoutParams();
        lp1.height = pixels*activities.size();
        activities_list.setLayoutParams(lp1);
        return mLeftPanelContainerView;
    }


    public void add_tagdetails(BaseTagModel tag) {
        relatedtags.clear();
        for (String i:tag.related){
            relatedtags.add(new PanelListItemModel("related tags", i, 0));
        }
    }

    public void add_activities(BaseTagModel tag) {
        activities.clear();
        Log.w("In add_activities "+tag.activity.get(0).object.displayName,"Myapp ");
        for (ActivityModel i:tag.activity){
            Log.w(""+i.object.displayName,"Myapp ");
            activities.add(new PanelListItemModel("activities", i.object.displayName, 0));
        }
    }
}
