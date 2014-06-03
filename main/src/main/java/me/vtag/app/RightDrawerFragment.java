package me.vtag.app;

import android.support.v4.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.views.RightPanelViewFragment;
import me.vtag.app.views.rightpanel.TagContextFragment;

import me.vtag.app.backend.models.PanelListItemModel;

public class RightDrawerFragment extends Fragment {
    private View mRightPanelContainerView;
    public RightDrawerFragment() {
    }

    public void new_tag_clicked(BaseTagModel tag) {
        if (getActivity() == null) return;

//        RightPanelViewFragment activeFragment = new RightPanelViewFragment(tag);
        TagContextFragment activeFragment = new TagContextFragment(tag);
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.right_panel_container, activeFragment).addToBackStack(null).commit();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRightPanelContainerView = inflater.inflate(
                R.layout.fragment_right_panel, container, false);
        return mRightPanelContainerView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
