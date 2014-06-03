package me.vtag.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import me.vtag.app.backend.models.UserModel;
import me.vtag.app.backend.vos.RootVO;
import me.vtag.app.views.leftpanel.LoggedInFragment;
import me.vtag.app.views.leftpanel.NonLoggedInFragment;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class LeftDrawerFragment extends Fragment {
    private View mLeftPanelContainerView;

    private LoggedInFragment mLoggedInFragment;
    private NonLoggedInFragment mNonLoggedInFragment;

    private UserModel mLoggedInUser;
    private RootVO mRootData;

    public LeftDrawerFragment() {
        mLoggedInUser = null;
    }

    public void setLoggedIn(UserModel user, RootVO rootData) {
        mLoggedInUser = user;
        mRootData = rootData;
        refresh();
    }


    private void refresh() {
        Fragment activeFragment;
        if (mLoggedInUser != null) {
            // Now show list of tags.
            if (mLoggedInFragment == null) {
                mLoggedInFragment = new LoggedInFragment();
            }
            mLoggedInFragment.setProfile(mRootData.user);
            mLoggedInFragment.addPrivateTags(mRootData.privatetags);
            mLoggedInFragment.addFollowingTags(mRootData.followingtags);
            mLoggedInFragment.addPublicTags(mRootData.publictags);
            activeFragment = mLoggedInFragment;
        } else {
            if (mNonLoggedInFragment == null) {
                mNonLoggedInFragment = new NonLoggedInFragment();
            }
            activeFragment = mNonLoggedInFragment;
        }

        if (getActivity() == null) return;

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.left_panel_container, activeFragment).commit();
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
        mLeftPanelContainerView = inflater.inflate(
                R.layout.fragment_left_panel, container, false);
        return mLeftPanelContainerView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        refresh();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }*/
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
