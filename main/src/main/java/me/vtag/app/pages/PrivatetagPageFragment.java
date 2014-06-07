package me.vtag.app.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import me.vtag.app.BasePageFragment;
import me.vtag.app.R;
import me.vtag.app.adapters.TagBasedVideoListAdapter;
import me.vtag.app.backend.models.CacheManager;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.PrivatetagModel;
import me.vtag.app.helpers.VtagmeLoaderView;

/**
 * Created by nmannem on 30/10/13.
 */
public class PrivatetagPageFragment extends BasePageFragment implements VtagmeLoaderView {
    private String mTag;
    private PrivatetagModel mPrivatetagModel;

    private ListView videoListView;
    private FontAwesomeText mLoadingSpinner = null;

    @Override
    public void showLoading() {
        if (mLoadingSpinner != null) {
            mLoadingSpinner.setVisibility(View.VISIBLE);
            mLoadingSpinner.startRotate(getActivity(), true, FontAwesomeText.AnimationSpeed.MEDIUM);
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingSpinner != null) {
            mLoadingSpinner.setVisibility(View.INVISIBLE);
            mLoadingSpinner.stopAnimation();
        }
    }

    public PrivatetagPageFragment() {
        this.mPrivatetagModel = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tag_page_fragment, container, false);
        mLoadingSpinner = (FontAwesomeText) rootView.findViewById(R.id.loadingSpinner);
        videoListView = (ListView) rootView.findViewById(R.id.videoListView);

        //here is your list array
        Bundle bundle = getArguments();
        mTag = bundle.getString("tag");
        PrivatetagModel tagModel = CacheManager.getInstance().getPrivateTagModel(mTag);
        if (tagModel != null) {
            videoListView.setAdapter(new TagBasedVideoListAdapter(mPrivatetagModel, HashtagModel.RECENT_VIDEOS_SORT,
                    getActivity(), R.layout.videocard, this));
        }
        return rootView;
    }
}
