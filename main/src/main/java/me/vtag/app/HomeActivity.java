package me.vtag.app;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.LruCache;

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
import me.vtag.app.pages.TagPageFragment;


public class HomeActivity extends SlidingFragmentActivity
        implements SearchView.OnQueryTextListener, VtagmeLoaderView {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private LeftDrawerFragment mLeftDrawerFragment;
    private RightDrawerFragment mRightDrawerFragment;

    private ProgressDialog progressDialog;
    private SearchView mSearchView;

    private CharSequence mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        SlidingMenu leftMenu = getSlidingMenu();
        leftMenu.setMode(SlidingMenu.LEFT_RIGHT);
        setBehindContentView(R.layout.left_drawer_layout);
        leftMenu.setFadeDegree(0.3f);
        leftMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_home);
        leftMenu.setSecondaryMenu(R.layout.right_drawer_layout);

        mLeftDrawerFragment = (LeftDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        mRightDrawerFragment = (RightDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_right_drawer_fragment);

        mTitle = getTitle();
        browseHomePage();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
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


    public void browseHomePage() {
        mTitle = "Home";
        showLoading();
        VtagClient.getAPI().getBootstrap(new Callback<RootVO>() {
            @Override
            public void onResponse(Response<RootVO> rootVOResponse) {
                RootVO rootData = rootVOResponse.getResult();
                if (rootData != null) {
                    if (rootData.user == null) {
                        VtagApplication.getInstance().authPreferences.setUser(null, null); // Next time shows login page.
                        mLeftDrawerFragment.setLoggedIn(null, rootData);
                    } else {
                        mLeftDrawerFragment.setLoggedIn(rootData.user, rootData);
                        // Store private and public tags in LRU cache..
                        for (PrivatetagModel tagModel : rootData.privatetags) {
                            CacheManager.getInstance().putPrivateTagModel(tagModel.tag, tagModel);
                        }
                    }
                    // Now show list of tags.
                    List<HashtagModel> allTopTags = new ArrayList<HashtagModel>();
                    allTopTags.addAll(rootData.toptags.texttags);
                    allTopTags.addAll(rootData.toptags.tagcards);
                    allTopTags.addAll(rootData.toptags.tagrows);
                    HashtagsPageFragment homepage = new HashtagsPageFragment(allTopTags);
                    // update the main content by replacing fragments
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, homepage)
                            .commit();
                } else {
                    Toast.makeText(HomeActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                hideLoading();
            }
        });
    }

    public void browseHashTag(String tag) {
        mTitle = tag;
        HashtagModel tagModel = CacheManager.getInstance().getHashTagModel(tag);
        if (tagModel == null) {
            showLoading();
            VtagClient.getAPI().getTagDetails(tag, new Callback<HashtagModel>() {
                @Override
                public void onResponse(Response<HashtagModel> hashtagModelResponse) {
                    HashtagModel tagModel = hashtagModelResponse.getResult();
                    if (tagModel != null) {
                        browseHashTag(tagModel);
                    } else {
                        Toast.makeText(HomeActivity.this, "Couldnt get tag details!", Toast.LENGTH_SHORT).show();
                    }
                    hideLoading();
                }
            });
        } else {
            browseHashTag(tagModel);
        }
    }

    private void browseHashTag(HashtagModel tagModel) {
        CacheManager.getInstance().putHashTagModel(tagModel.tag, tagModel);

        mRightDrawerFragment.new_tag_clicked(tagModel);
        TagPageFragment tagpage = new TagPageFragment(tagModel);
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, tagpage);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void browsePrivateTag(String tag) {
        mTitle = tag;
        PrivatetagModel tagModel = CacheManager.getInstance().getPrivateTagModel(tag);
        if (tagModel == null) {
            return;
        } else {
            browsePrivateTag(tagModel);
        }
    }

    private void browsePrivateTag(PrivatetagModel tagModel) {
        TagPageFragment tagpage = new TagPageFragment(tagModel);
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, tagpage);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void showLoading() {
        if (progressDialog != null) {
            progressDialog = ProgressDialog.show(this, "Loading..",
                    "Please wait..", true);
        }
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
