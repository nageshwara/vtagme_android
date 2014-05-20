package me.vtag.app.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
        Log.w("Came to onCreateView of RightPanelViewFragment","Myapp ");
        mLeftPanelContainerView = inflater.inflate(R.layout.left_panel_list, container, false);
        Log.w("Came to onCreateView of RightPanelViewFragment after inflating left_panel_list","Myapp ");
        ListView relatedtags = (ListView) mLeftPanelContainerView.findViewById(R.id.related_tags_list);
        relatedtags.setAdapter(new PanelListAdapter(getActivity(), 0, (java.util.ArrayList<PanelListItemModel>) this.relatedtags));

        ListView activities = (ListView) mLeftPanelContainerView.findViewById(R.id.activities_list);
        activities.setAdapter(new PanelListAdapter(getActivity(),0,(java.util.ArrayList<PanelListItemModel>) this.activities));
        return mLeftPanelContainerView;
    }


    public void add_tagdetails(BaseTagModel tag) {
        relatedtags.clear();
        for (String i:tag.related){
            relatedtags.add(new PanelListItemModel("related tags",i,0));
        }
    }

    public void add_activities(BaseTagModel tag) {
        activities.clear();
        for (ActivityModel i:tag.activity){
            activities.add(new PanelListItemModel("activities",i.object.displayname,0));
        }
    }
}
