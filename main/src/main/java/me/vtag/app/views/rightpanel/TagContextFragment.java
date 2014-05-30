package me.vtag.app.views.rightpanel;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
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
import me.vtag.app.backend.models.PanelListItemModel;
import me.vtag.app.views.rightpanel.ActivityListFragment;
import me.vtag.app.views.rightpanel.RelatedTagsFragment;

/**
 * Created by anuraag on 20/5/14.
 */
public class TagContextFragment extends Fragment {

    private View mLeftPanelContainerView;
    private BaseTagModel tagModel;
    private List<PanelListItemModel> relatedtags = new ArrayList<>();
    private List<PanelListItemModel> activities = new ArrayList<>();

    public TagContextFragment(BaseTagModel tagModel){
        this.tagModel = tagModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLeftPanelContainerView = inflater.inflate(R.layout.right_panel_list, container, false);

        RelatedTagsFragment relatedTagsFragment = new RelatedTagsFragment();
        relatedTagsFragment.AddRelatedTags(this.tagModel);
        FragmentManager fragmentManager_RelatedTags = getActivity().getSupportFragmentManager();
        fragmentManager_RelatedTags.beginTransaction().replace(R.id.related_tags_list_framelayout,relatedTagsFragment).commit();

        ActivityListFragment activityListFragment = new ActivityListFragment();
        activityListFragment.AddActivityTags(this.tagModel);
        FragmentManager fragmentManager_Acitvities = getActivity().getSupportFragmentManager();
        fragmentManager_Acitvities.beginTransaction().replace(R.id.activities_list_framelayout,activityListFragment).commit();

        return mLeftPanelContainerView;
    }
}
