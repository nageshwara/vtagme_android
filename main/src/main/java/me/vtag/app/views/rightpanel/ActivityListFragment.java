package me.vtag.app.views.rightpanel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.vtag.app.R;
import me.vtag.app.adapters.PanelListAdapter;
import me.vtag.app.adapters.RightPanel.ActivityListAdapter;
import me.vtag.app.backend.models.ActivityModel;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.PanelListItemModel;

/**
 * Created by anuraag on 25/5/14.
 */
public class ActivityListFragment extends Fragment {
    private List<PanelListItemModel> activities = new ArrayList<>();
    private View mActivityList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivityList = inflater.inflate(R.layout.activity_list, container, false);
        ListView activities_view = (ListView) mActivityList.findViewById(R.id.activities_listview);
        activities_view.setAdapter(new ActivityListAdapter(getActivity(), 0, (java.util.ArrayList<PanelListItemModel>) this.activities));

        float pixels1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
        int pixels = (int) (pixels1+.5f);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) activities_view.getLayoutParams();
        lp.height = pixels*this.activities.size();
        activities_view.setLayoutParams(lp);

        return mActivityList;
    }

    public void AddActivityTags(BaseTagModel tag) {
        activities.clear();
        for (ActivityModel i:tag.activity){
            activities.add(new PanelListItemModel("activities", i.object.displayName, 0));
        }
    }

}

