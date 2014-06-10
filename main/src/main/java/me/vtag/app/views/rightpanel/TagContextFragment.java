package me.vtag.app.views.rightpanel;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import me.vtag.app.R;
import me.vtag.app.backend.models.BaseTagModel;

/**
 * Created by anuraag on 20/5/14.
 */
public class TagContextFragment extends Fragment {

    private View mtagContextView;
    private TextView mTagTitle;
    private FontAwesomeText mCloseButton;

    private BaseTagModel tagModel;

    public TagContextFragment(BaseTagModel tagModel){
        this.tagModel = tagModel;
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

            }
        });

        ActivityListFragment activityListFragment = new ActivityListFragment();
        activityListFragment.AddActivityTags(this.tagModel);

        mTagTitle.setText("#" + tagModel.tag);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activities_list_framelayout, activityListFragment)
                .commit();
        return mtagContextView;
    }
}
