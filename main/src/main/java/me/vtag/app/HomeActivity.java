package me.vtag.app;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.PrivatetagModel;
import me.vtag.app.backend.vos.RootVO;
import me.vtag.app.backend.models.CacheManager;
import me.vtag.app.helpers.VtagmeLoaderView;
import me.vtag.app.pages.HashtagsPageFragment;
import me.vtag.app.pages.HashtagPageFragment;
import me.vtag.app.pages.HomePageFragment;
import me.vtag.app.pages.PrivatetagPageFragment;
import me.vtag.app.pages.SearchPageFragment;


public class HomeActivity extends SlidingFragmentActivity
        implements SearchView.OnQueryTextListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private static LeftDrawerFragment mLeftDrawerFragment;
    private static RightDrawerFragment mRightDrawerFragment;

    private SearchView mSearchView;
    public SlidingMenu slidingMenu;
//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        setBehindContentView(R.layout.left_drawer_layout);
        slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffsetRes(R.dimen.navigation_drawer_offset);
        slidingMenu.setShadowWidthRes(R.dimen.navigation_drawer_shadow_width);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_home);
        slidingMenu.setSecondaryMenu(R.layout.right_drawer_layout);

        mLeftDrawerFragment = (LeftDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        mRightDrawerFragment = (RightDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_right_drawer_fragment);
        browseHomePage();
    }

    public void newTagCalled(HashtagModel hashtagModel) {
        mRightDrawerFragment.new_tag_clicked(hashtagModel);
    }

    public static final LeftDrawerFragment getLeftDrawerFragment() {
        return mLeftDrawerFragment;
    }

    public static final RightDrawerFragment getRightDrawerFragment() {
        return mRightDrawerFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_action_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_search) {
            browseSearchPage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSearchView(MenuItem searchItem) {
        if (mSearchView == null) return;

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            /*for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }*/
            mSearchView.setSearchableInfo(info);
        }
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
    }

    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void browseSearchPage() {
        SearchPageFragment searchPageFragment = new SearchPageFragment();

        Bundle args = new Bundle();
        ArrayList<String> tags = new ArrayList<>();
        //tags.add("telugu");
        //tags.add("songs");
        args.putStringArrayList("tags",tags);

        searchPageFragment.setArguments(args);
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, searchPageFragment)
                .commit();
    }

    public void browseHomePage() {
        HomePageFragment homePageFragment = new HomePageFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, homePageFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void browseHashTag(String tag) {
        getActionBar().hide();

        Bundle args = new Bundle();
        args.putStringArray("tags", new String[]{tag});
        args.putString("sort", HashtagModel.RECENT_VIDEOS_SORT);
        HashtagPageFragment tagpage = new HashtagPageFragment();
        tagpage.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, tagpage);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void browsePrivateTag(String tag) {
        Bundle args = new Bundle();
        args.putString("tag", tag);

        PrivatetagPageFragment tagpage = new PrivatetagPageFragment();
        tagpage.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, tagpage);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}