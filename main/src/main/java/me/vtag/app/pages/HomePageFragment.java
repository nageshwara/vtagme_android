package me.vtag.app.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import java.util.ArrayList;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.BasePageFragment;
import me.vtag.app.R;
import me.vtag.app.VtagApplication;
import me.vtag.app.adapters.HashtagListAdapter;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.vos.RootVO;
import me.vtag.app.helpers.VtagmeLoaderView;

/**
 * Created by nmannem on 30/10/13.
 */
public class HomePageFragment extends BasePageFragment implements VtagmeLoaderView {
    private ListView tagListView;
    private HashtagListAdapter tagListAdapter;

    public HomePageFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_page_fragment, container, false);
        mLoadingSpinner = (FontAwesomeText) rootView.findViewById(R.id.loadingSpinner);
        tagListView = (ListView) rootView.findViewById(R.id.tagListView);
        tagListAdapter = new HashtagListAdapter(getActivity(), R.layout.tagcard, new ArrayList<HashtagModel>());
        tagListView.setAdapter(tagListAdapter);
        fetchRootData();
        return rootView;
    }

    private void fetchRootData() {
        RootVO cachedData = VtagApplication.getInstance().getRootData();
        if (cachedData == null) {
            showLoading();
            VtagClient.getAPI().getBootstrap(new Callback<RootVO>() {
                @Override
                public void onResponse(Response<RootVO> rootVOResponse) {
                    RootVO rootData = rootVOResponse.getResult();
                    if (rootData != null) {
                        // Now show list of tags.
                        VtagApplication.getInstance().setRootData(rootData);
                        populateData(rootData);
                    } else {
                        Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    hideLoading();
                }
            });
        } else {
            populateData(cachedData);
        }
    }

    private void populateData(RootVO rootData) {
        tagListAdapter.clear();
        tagListAdapter.addAll(rootData.toptags.texttags);
        tagListAdapter.addAll(rootData.toptags.tagcards);
        tagListAdapter.addAll(rootData.toptags.tagrows);
        tagListAdapter.notifyDataSetChanged();
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
        return true;
    }
}