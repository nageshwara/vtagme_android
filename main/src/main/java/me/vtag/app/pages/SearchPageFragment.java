package me.vtag.app.pages;

import android.os.Bundle;
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
import me.vtag.app.R;
import me.vtag.app.adapters.TagAutoCompleteAdapter;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.CacheManager;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.helpers.VtagmeLoaderView;
import me.vtag.app.views.TagsCompletionView;

/**
 * Created by nmannem on 30/10/13.
 */
public class SearchPageFragment extends BasePageFragment implements TokenCompleteTextView.TokenListener, VtagmeLoaderView {
    private List<String> mTags;

    TagsCompletionView completionView;
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


    @Override
    public void onTokenAdded(Object token) {
        mTags.add((String)token);
        updateTokenConfirmation();
        System.out.println();
    }

    @Override
    public void onTokenRemoved(Object token) {
        int index = mTags.indexOf(token);
        mTags.remove(index);
        updateTokenConfirmation();
    }

    private void updateTokenConfirmation() {
        final String tagId = TextUtils.join(",", mTags);
        final String mSortType = HashtagModel.RECENT_VIDEOS_SORT;
        HashtagModel tagModel = CacheManager.getInstance().getHashTagModel(tagId + "_" + mSortType);
        if (tagModel == null) {
            showLoading();
            VtagClient.getAPI().getTagDetails(tagId, mSortType, new Callback<HashtagModel>() {
                @Override
                public void onResponse(Response<HashtagModel> hashtagModelResponse) {
                    HashtagModel tagModel = hashtagModelResponse.getResult();
                    if (tagModel != null) {
                        CacheManager.getInstance().putHashTagModel(tagId + "_" + mSortType, tagModel);
                    } else {
                        Toast.makeText(getContext(), "Couldnt get mHashtagModel details!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
        }
    }


    public SearchPageFragment() {
        mTags = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mLoadingSpinner = (FontAwesomeText) rootView.findViewById(R.id.loadingSpinner);

        completionView = (TagsCompletionView) rootView.findViewById(R.id.searchView);
        completionView.setAdapter(new TagAutoCompleteAdapter(getActivity(), R.layout.auto_completion_string_item));
        completionView.setTokenListener(this);
        completionView.requestFocus();

        //here is your list array
        Bundle bundle = getArguments();
        List<String> tags = bundle.getStringArrayList("tags");
        for (String tag : tags) {
            completionView.addObject(tag);
        }
        return rootView;
    }

    @Override
    public boolean supportsActionBar() {
        return false;
    }
}
