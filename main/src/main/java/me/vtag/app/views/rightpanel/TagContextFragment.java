package me.vtag.app.views.rightpanel;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import me.vtag.app.R;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.CacheManager;
import me.vtag.app.backend.models.HashtagModel;

/**
 * Created by anuraag on 20/5/14.
 */
public class TagContextFragment extends Fragment {

    private View mtagContextView;
    private TextView mTagTitle;
    private FontAwesomeText mCloseButton;
    private BaseTagModel tagModel;

    public TagContextFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mtagContextView = inflater.inflate(R.layout.tag_context_right_panel_view, container, false);
        mTagTitle = (TextView)mtagContextView.findViewById(R.id.tagTitle);
        mCloseButton = (FontAwesomeText)mtagContextView.findViewById(R.id.closeButton);

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("Close Button", "Myapp ");
            }
        });

        Bundle args = getArguments();
        String tagCacheId = args.getString("tagCacheId");
        int tagType = args.getInt("tagType");

        if (tagType == 0 || tagType == 1) {
            tagModel = CacheManager.getInstance().getHashTagModel(tagCacheId);
        } else if (tagType == 2) {
            tagModel = CacheManager.getInstance().getPrivateTagModel(tagCacheId);
        }

        if (tagModel != null) {
            ActivityListFragment activityListFragment = new ActivityListFragment();
            activityListFragment.AddActivityTags(tagModel);

            mTagTitle.setText("#" + tagModel.tag);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.activities_list_framelayout, activityListFragment)
                    .commit();
        }
        return mtagContextView;
    }
}
