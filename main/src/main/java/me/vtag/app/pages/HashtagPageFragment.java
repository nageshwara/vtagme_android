package me.vtag.app.pages;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.CacheManager;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.helpers.VtagmeLoaderView;
import me.vtag.app.views.TagsCompletionView;

/**
 * Created by nmannem on 30/10/13.
 */
public class HashtagPageFragment extends BasePageFragment implements
        VtagmeLoaderView,
        HashtagModel.OnSortChangeListener,
        TokenCompleteTextView.TokenListener,
        HashtagModel.OnTagsModifiedListener {
    private List<String> mTags;
    TagsCompletionView completionView;

    private String mSortType;
    private HashtagModel mHashtagModel;

    public HashtagPageFragment() {
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
        View rootView = inflater.inflate(R.layout.tag_page_fragment, null);
        mLoadingSpinner = (FontAwesomeText) rootView.findViewById(R.id.loadingSpinner);

        mActiveFragment = new HashtagSubpageFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.tag_details_container, mActiveFragment).commit();
        mActiveFragment.setTagAndSortChangeListener(this, this);


        completionView = (TagsCompletionView) rootView.findViewById(R.id.tagInputView);
        //here is your list array
        Bundle bundle = getArguments();
        mSortType = bundle.getString("sort");
        String[] tags = bundle.getStringArray("tags");
        for (String tag : tags) {
            completionView.addObject(tag);
        }

        completionView.setAdapter(new TagAutoCompleteAdapter(getActivity(), R.layout.auto_completion_string_item));
        completionView.setTokenListener(this);
        return rootView;
    }

    private HashtagSubpageFragment mActiveFragment;
    private void renderVideoList(HashtagModel model) {
        mHashtagModel = model;
        mActiveFragment.renderVideoListAndRelatedTags(new TagBasedVideoListAdapter(mHashtagModel, mSortType, getActivity(), R.layout.videocard, this), mHashtagModel.related);
        HomeActivity.getRightDrawerFragment().showTagModelContext(mHashtagModel);
    }

    @Override
    public void onTokenAdded(Object token) {
        processAdded((String) token);
    }

    @Override
    public void onTokenRemoved(Object token) {
        processRemoved((String) token);
    }

    @Override
    public void onAdded(String tag) {
        completionView.addObject(tag);
    }

    @Override
    public void onRemoved(String tag) {
        completionView.removeObject(tag);
    }

    private void processAdded(String tag) {
        mTags.add(tag);
        fetchTagModel();
    }

    private void processRemoved(String tag) {
        int index = mTags.indexOf(tag);
        mTags.remove(index);
        fetchTagModel();
    }


    private void fetchTagModel() {
        final String tagId = TextUtils.join(",", mTags);
        HashtagModel tagModel = CacheManager.getInstance().getHashTagModel(tagId + "_" + mSortType);
        if (tagModel == null) {
            showLoading();
            VtagClient.getAPI().getTagDetails(tagId, mSortType, new Callback<HashtagModel>() {
                @Override
                public void onResponse(Response<HashtagModel> hashtagModelResponse) {
                    hideLoading();
                    HashtagModel tagModel = hashtagModelResponse.getResult();
                    if (tagModel != null) {
                        CacheManager.getInstance().putHashTagModel(tagId + "_" + mSortType, tagModel);
                        ((HomeActivity) getActivity()).newTagCalled(tagModel);
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
    public boolean onChange(String newSort) {
        if (mSortType.equals(newSort)) return false;
        mSortType = newSort;
        fetchTagModel();
        return true;
    }

    @Override
    public boolean supportsActionBar() {
        return false;
    }
}