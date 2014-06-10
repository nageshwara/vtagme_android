package me.vtag.app.views;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.vtag.app.R;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.PanelListItemModel;
import me.vtag.app.views.rightpanel.ActivityListFragment;

/**
 * Created by anuraag on 20/5/14.
 */
public class RightPanelViewFragment extends Fragment {

    private View mLeftPanelContainerView;
    private BaseTagModel tagModel;
    private List<PanelListItemModel> relatedtags = new ArrayList<>();
    private List<PanelListItemModel> activities = new ArrayList<>();

    public RightPanelViewFragment(BaseTagModel tagModel){
        super();
        this.tagModel = tagModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLeftPanelContainerView = inflater.inflate(R.layout.left_panel_list, container, false);

        ActivityListFragment activityListFragment = new ActivityListFragment();
        activityListFragment.AddActivityTags(this.tagModel);
        FragmentManager fragmentManager_Acitvities = getActivity().getSupportFragmentManager();
        fragmentManager_Acitvities.beginTransaction().replace(R.id.activities_list_framelayout,activityListFragment).commit();

        return mLeftPanelContainerView;
    }
}
