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
    private FontAwesomeText mLoadingSpinner = null;
    private String PresentTab;

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

    public TagPageFragment() {
        super(ID);
        this.tag = null;
    }
    public TagPageFragment(BaseTagModel tag) {
        super(ID);
        this.tag = tag;
    }
    public TagPageFragment(BaseTagModel tag,String PresentTab) {
        super(ID);
        this.tag = tag;
        this.PresentTab = PresentTab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tag_page_fragment, container, false);
        mLoadingView = rootView.findViewById(R.id.loadingView);
        mLoadingSpinner = (FontAwesomeText) rootView.findViewById(R.id.loadingSpinner);
        videoListView = (ListView) rootView.findViewById(R.id.videoListView);
        videoListView.setAdapter(new TagBasedVideoListAdapter(tag, PresentTab, getActivity(), R.layout.videocard, this.tag.videodetails, this));
        hideLoading();
        return rootView;
    }
}
