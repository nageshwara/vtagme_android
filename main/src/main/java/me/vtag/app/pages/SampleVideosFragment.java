package me.vtag.app.pages;

/**
 * Created by anuraag on 5/7/14.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.BasePageFragment;
import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.adapters.TagAutoCompleteAdapter;
import me.vtag.app.adapters.TagBasedVideoListAdapter;
import me.vtag.app.adapters.VideoListAdapter;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.CacheManager;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.helpers.VtagmeLoaderView;
import me.vtag.app.views.QuickReturnListView;
import me.vtag.app.views.RelatedTagsHListView;
import me.vtag.app.views.TagsCompletionView;

/**
 * Created by nmannem on 30/10/13.
 */
public class SampleVideosFragment extends BasePageFragment implements
        VtagmeLoaderView {

    private List<String> mTags;
    private String mTagCacheId;

    private String mSortType;
    private HashtagModel mHashtagModel;
    private ListView videoListView;

    public SampleVideosFragment() {
        this.mHashtagModel = null;
        mTags = new ArrayList<>();
        mSortType = HashtagModel.RECENT_VIDEOS_SORT;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sample_videos_list, null);
        mLoadingSpinner = (FontAwesomeText) rootView.findViewById(R.id.loadingSpinner);
//        mRelatedTagsView = (RelatedTagsHListView) rootView.findViewById(R.id.relatedTags);

        videoListView = (ListView) rootView.findViewById(R.id.video_samples_list_view);
        fetchTagModel();

/*        mActiveFragment = new HashtagSubpageFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.tag_details_container, mActiveFragment).commit();
        */
        return rootView;
    }

    private HashtagSubpageFragment mActiveFragment;
    private void renderVideoList(HashtagModel model) {
        mHashtagModel = model;
        renderVideoListAndRelatedTags(new TagBasedVideoListAdapter(mHashtagModel, mSortType, getActivity(), R.layout.videocard, this), mHashtagModel.related);
    }

    private VideoListAdapter mAdapter;
    private RelatedTagsHListView mRelatedTagsView;

    public void renderVideoListAndRelatedTags(VideoListAdapter adapter, String[] relatedTags) {
        mAdapter = adapter;
        videoListView.setAdapter(mAdapter);
//        mRelatedTagsView.setTags(relatedTags);
/*
        if (videoListView != null && mAdapter != null) {
            videoListView.setAdapter(mAdapter);
        }
*/
    }



    private void fetchTagModel() {
        final String tagId = TextUtils.join(",", mTags);
        mTagCacheId = tagId + "_" + mSortType;
        HashtagModel tagModel = CacheManager.getInstance().getHashTagModel(mTagCacheId);
        if (tagModel == null) {
            showLoading();
            VtagClient.getAPI().getTagDetails(tagId, mSortType, new Callback<HashtagModel>() {
                @Override
                public void onResponse(Response<HashtagModel> hashtagModelResponse) {
                    hideLoading();
                    HashtagModel tagModel = hashtagModelResponse.getResult();
                    if (tagModel != null) {
                        CacheManager.getInstance().putHashTagModel(mTagCacheId, tagModel);
                        //HomeActivity.getRightDrawerFragment().new_tag_clicked(hashtagModel);
                        renderVideoList(tagModel);
                    } else {
                        Toast.makeText(getContext(), "Couldnt get mHashtagModel details!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            renderVideoList(tagModel);
        }
    }

    private FontAwesomeText mLoadingSpinner = null;
    @Override
    public void showLoading() {
        if (mLoadingSpinner != null) {
            mLoadingSpinner.bringToFront();
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

    @Override
    public boolean supportsActionBar() {
        return false;
    }
}