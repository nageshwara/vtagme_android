package me.vtag.app.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import me.vtag.app.BasePageFragment;
import me.vtag.app.R;
import me.vtag.app.adapters.TagBasedVideoListAdapter;
import me.vtag.app.adapters.VideoListAdapter;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.helpers.VtagmeLoaderView;

/**
 * Created by nmannem on 30/10/13.
 */
public class TagPageFragment extends BasePageFragment implements VtagmeLoaderView {
    public static final int ID = 1;

    private BaseTagModel tag;
    private ListView videoListView;
    private View mLoadingView = null;

    @Override
    public void showLoading() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.INVISIBLE);
        }
    }

    public TagPageFragment() {
        super(ID);
        this.tag = null;
    }
    public TagPageFragment(BaseTagModel tag) {
        super(ID);
        this.tag = tag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tag_page_fragment, container, false);
        mLoadingView = rootView.findViewById(R.id.loadingView);
        videoListView = (ListView) rootView.findViewById(R.id.videoListView);
        videoListView.setAdapter(new TagBasedVideoListAdapter(tag, getActivity(), R.layout.videocard, this.tag.videodetails, this));
        return rootView;
    }
}
