package me.vtag.app.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
public class HashtagSubpageFragment extends BasePageFragment implements VtagmeLoaderView {
    public static final int ID = 1;

    private String mTag;
    private String mSortType;
    private HashtagModel mHashtagModel;

    private ListView videoListView;
    private View mLoadingView = null;
    private FontAwesomeText mLoadingSpinner = null;

    @Override
    public void showLoading() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
            mLoadingSpinner.startRotate(getActivity(), true, FontAwesomeText.AnimationSpeed.MEDIUM);
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.INVISIBLE);
            mLoadingSpinner.stopAnimation();
        }
    }

    public HashtagSubpageFragment() {
        super(ID);
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
        mLoadingView = rootView.findViewById(R.id.loadingView);
        mLoadingSpinner = (FontAwesomeText) rootView.findViewById(R.id.loadingSpinner);
        videoListView = (ListView) rootView.findViewById(R.id.videoListView);

        //here is your list array
        Bundle bundle = getArguments();
        mTag = bundle.getString("tag");
        mSortType = bundle.getString("sort");

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
                    hideLoading();
                }
            });
        } else {
            renderVideoList(tagModel);
        }
        return rootView;
    }

    private void renderVideoList(HashtagModel tagModel) {
        mHashtagModel = tagModel;
        videoListView.setAdapter(new TagBasedVideoListAdapter(mHashtagModel, mSortType, getActivity(), R.layout.videocard, this.mHashtagModel.videodetails, this));
    }
}
