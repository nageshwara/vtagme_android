package me.vtag.app.pages;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import me.vtag.app.BasePageFragment;
import me.vtag.app.R;
import me.vtag.app.adapters.HashtagSortPagerAdapter;
import me.vtag.app.adapters.TagBasedVideoListAdapter;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.helpers.VtagmeLoaderView;

/**
 * Created by nmannem on 30/10/13.
 */
public class HashtagPageFragment extends BasePageFragment {
    public static final int ID = 1;

    private String mTag;

    private ViewPager mViewPager;
    private PagerTitleStrip mViewPagerTitle;

    public HashtagPageFragment() {
        super(ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hashtag_page_fragment, container, false);
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPagerTitle = (PagerTitleStrip) rootView.findViewById(R.id.pager_title_strip);

        //here is your list array
        Bundle bundle = getArguments();
        mTag = bundle.getString("tag");

        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("#" + mTag);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        //actionBar.setSelectedNavigationItem(position);
                    }
                });
        mViewPager.setAdapter(new HashtagSortPagerAdapter(mTag, getFragmentManager()));
        return rootView;
    }
}
