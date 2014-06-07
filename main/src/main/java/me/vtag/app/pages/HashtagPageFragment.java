package me.vtag.app.pages;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.BasePageFragment;
import me.vtag.app.R;
import me.vtag.app.adapters.TagBasedVideoListAdapter;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.CacheManager;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.helpers.VtagmeLoaderView;

/**
 * Created by nmannem on 30/10/13.
 */
public class HashtagPageFragment extends BasePageFragment implements VtagmeLoaderView, HashtagModel.OnSortChangeListener {

    private String mTag;
    private String mSortType;
    private HashtagModel mHashtagModel;

    public HashtagPageFragment() {
        this.mHashtagModel = null;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tag_page_fragment, container, false);
        mLoadingSpinner = (FontAwesomeText) rootView.findViewById(R.id.loadingSpinner);
        //here is your list array
        Bundle bundle = getArguments();
        mTag = bundle.getString("tag");
        mSortType = bundle.getString("sort");

        mActiveFragment = new HashtagSubpageFragment();
        mActiveFragment.setSortChangeListener(this);
        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, mActiveFragment);
        transaction.commit();

        fetchTagModel();
        return rootView;
    }

    private void fetchTagModel() {
        HashtagModel tagModel = CacheManager.getInstance().getHashTagModel(mTag+"_"+mSortType);
        if (tagModel == null) {
            showLoading();
            VtagClient.getAPI().getTagDetails(mTag, mSortType, new Callback<HashtagModel>() {
                @Override
                public void onResponse(Response<HashtagModel> hashtagModelResponse) {
                    HashtagModel tagModel = hashtagModelResponse.getResult();
                    if (tagModel != null) {
                        CacheManager.getInstance().putHashTagModel(mTag + "_" + mSortType, tagModel);
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

    private HashtagSubpageFragment mActiveFragment;
    private void renderVideoList(HashtagModel model) {
        mHashtagModel = model;
        mActiveFragment.renderVideoListAndRelatedTags(new TagBasedVideoListAdapter(mHashtagModel, mSortType, getActivity(), R.layout.videocard, this), mHashtagModel.related);

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


}