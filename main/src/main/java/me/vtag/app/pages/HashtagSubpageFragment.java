package me.vtag.app.pages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import me.vtag.app.R;
import me.vtag.app.adapters.VideoListAdapter;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.views.QuickReturnListView;
import me.vtag.app.views.RelatedTagsHListView;

/**
 * Created by nmannem on 30/10/13.
 */
public class HashtagSubpageFragment extends ListFragment {
    private QuickReturnListView videoListView;
    private View mQuickReturnView;
    private RelatedTagsHListView mRelatedTagsView;
    private View mHeaderView;

    private VideoListAdapter mAdapter;
    private HashtagModel.OnSortChangeListener mOnSortChangeListener;

    private BootstrapButton recentSorter;
    private BootstrapButton popularSorter;
    private BootstrapButton mineSorter;

    public HashtagSubpageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tag_details_and_videos, null);
        mHeaderView = inflater.inflate(R.layout.fragment_tag_details_and_videos_header, null);

        mQuickReturnView = rootView.findViewById(R.id.sticky);
        mRelatedTagsView = (RelatedTagsHListView) rootView.findViewById(R.id.relatedTags);
        View.OnClickListener sortClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BootstrapButton button = (BootstrapButton) view;
                boolean result = false;
                if (mOnSortChangeListener != null) {
                    if (button == recentSorter) {
                        result = mOnSortChangeListener.onChange(HashtagModel.RECENT_VIDEOS_SORT);
                    } else if (button == popularSorter) {
                        result = mOnSortChangeListener.onChange(HashtagModel.POPULAR_VIDEOS_SORT);
                    } else {
                        result = mOnSortChangeListener.onChange(HashtagModel.MY_VIDEOS_SORT);
                    }
                }

                if (!result) {
                    return;
                }

                recentSorter.setBootstrapType("default");
                popularSorter.setBootstrapType("default");
                mineSorter.setBootstrapType("default");
                button.setBootstrapType("success");
            }
        };

        recentSorter = (BootstrapButton) rootView.findViewById(R.id.recent_sorter);
        popularSorter = (BootstrapButton) rootView.findViewById(R.id.popular_sorter);
        mineSorter = (BootstrapButton) rootView.findViewById(R.id.mine_sorter);
        recentSorter.setOnClickListener(sortClickListener);
        popularSorter.setOnClickListener(sortClickListener);
        mineSorter.setOnClickListener(sortClickListener);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        videoListView = (QuickReturnListView) getListView();
        videoListView.addHeaderView(mHeaderView);

        if (videoListView != null && mAdapter != null) {
            videoListView.setAdapter(mAdapter);
        }
        createQuickReturnListeners();
    }

    public void setSortChangeListener(HashtagModel.OnSortChangeListener onSortChangeListener)  {
        mOnSortChangeListener = onSortChangeListener;
    }

    public void renderVideoListAndRelatedTags(VideoListAdapter adapter, String[] relatedTags) {
        mAdapter = adapter;
        mRelatedTagsView.setTags(relatedTags);
        if (videoListView != null && mAdapter != null) {
            videoListView.setAdapter(mAdapter);
        }

    }

    private int mCachedVerticalScrollRange;
    private int mQuickReturnHeight;
    private AbsListView.OnScrollListener mScrollListener;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;

    private static final int STATE_ONSCREEN = 0;
    private static final int STATE_OFFSCREEN = 1;
    private static final int STATE_RETURNING = 2;
    private int mState = STATE_ONSCREEN;
    private int mScrollY;
    private int mMinRawY = 0;

    private void createQuickReturnListeners() {
        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mQuickReturnHeight = mQuickReturnView.getHeight();
                videoListView.computeScrollY();
                mCachedVerticalScrollRange = videoListView.getListHeight();
            }
        };
        mScrollListener = new AbsListView.OnScrollListener() {
            @SuppressLint("NewApi")
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                mScrollY = 0;
                int translationY = 0;

                if (videoListView.scrollYIsComputed()) {
                    mScrollY = videoListView.getComputedScrollY();
                }

                int rawY =  mHeaderView.getTop() - Math.min(
                        mCachedVerticalScrollRange
                                - videoListView.getHeight(), mScrollY);

                switch (mState) {
                    case STATE_OFFSCREEN:
                        if (rawY <= mMinRawY) {
                            mMinRawY = rawY;
                        } else {
                            mState = STATE_RETURNING;
                        }
                        translationY = rawY;
                        break;

                    case STATE_ONSCREEN:
                        if (rawY < -mQuickReturnHeight) {
                            mState = STATE_OFFSCREEN;
                            mMinRawY = rawY;
                        }
                        translationY = rawY;
                        break;

                    case STATE_RETURNING:
                        translationY = (rawY - mMinRawY) - mQuickReturnHeight;
                        if (translationY > 0) {
                            translationY = 0;
                            mMinRawY = rawY - mQuickReturnHeight;
                        }

                        if (rawY > 0) {
                            mState = STATE_ONSCREEN;
                            translationY = rawY;
                        }

                        if (translationY < -mQuickReturnHeight) {
                            mState = STATE_OFFSCREEN;
                            mMinRawY = rawY;
                        }
                        break;
                }
                mQuickReturnView.setTranslationY(translationY);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        };
        videoListView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        videoListView.setOnScrollListener(mScrollListener);
    }
}
