package me.vtag.app.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.pages.HashtagSubpageFragment;

/**
 * Created by nageswara on 6/3/14.
 */
public class HashtagSortPagerAdapter extends FragmentStatePagerAdapter {

    private String mTag;
    private static String[] SortTypes = {HashtagModel.RECENT_VIDEOS_SORT, HashtagModel.POPULAR_VIDEOS_SORT, HashtagModel.MY_VIDEOS_SORT};
    private static String[] SortTypeTitles = {"Recent", "Popular", "My Videos"};

    public HashtagSortPagerAdapter(String tag, FragmentManager fm) {
        super(fm);
        mTag = tag;
    }

    @Override

    public Fragment getItem(int position) {
        if (position >= SortTypes.length) return null;

        Bundle args = new Bundle();
        args.putString("tag", mTag);
        args.putString("sort", SortTypes[0]);
        HashtagSubpageFragment subpageFragment = new HashtagSubpageFragment();
        subpageFragment.setArguments(args);
        return subpageFragment;
    }

    @Override
    public int getCount() {
        return SortTypes.length;
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return SortTypeTitles[position];
    }
}
