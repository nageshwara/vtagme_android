package me.vtag.app.views.rightpanel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.PanelListItemModel;

/**
 * Created by anuraag on 25/5/14.
 */
public class RelatedTagsFragment extends Fragment {
    private List<PanelListItemModel> relatedtags = new ArrayList<>();
    private View mRelatedTagList;
    private FragmentActivity fragmentactivity_rightpanelviewfragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRelatedTagList = inflater.inflate(R.layout.related_tag_list, container, false);
        ListView relatedtags_list_view = (ListView) mRelatedTagList.findViewById(R.id.relatedtags_listview);
        relatedtags_list_view.setAdapter(new PanelListAdapter(getActivity(), 0, (ArrayList<PanelListItemModel>) this.relatedtags));

        float pixels1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
        int pixels = (int) (pixels1+.5f);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) relatedtags_list_view.getLayoutParams();
        lp.height = pixels*this.relatedtags.size();
        relatedtags_list_view.setLayoutParams(lp);
        return mRelatedTagList;
    }

    public void AddRelatedTags(BaseTagModel tag) {
        relatedtags.clear();
        for (String i:tag.related){
            relatedtags.add(new PanelListItemModel("related tags", i, 0));
        }

    }

    public void AddFragmentActivity(FragmentActivity fragmentactivity_rightpanelviewfragment) {
        this.fragmentactivity_rightpanelviewfragment = fragmentactivity_rightpanelviewfragment;
    }
}
