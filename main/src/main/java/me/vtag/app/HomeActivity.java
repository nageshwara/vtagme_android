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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Type;
import java.util.List;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.PrivatetagModel;
import me.vtag.app.backend.vos.RootVO;
import me.vtag.app.backend.models.CacheManager;
import me.vtag.app.pages.HashtagsPageFragment;
import me.vtag.app.pages.TagsPageFragment;
import me.vtag.app.pages.TagPageFragment;


public class HomeActivity extends ActionBarActivity
        implements SearchView.OnQueryTextListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private LeftDrawerFragment mLeftDrawerFragment;
    private RightDrawerFragment mRightDrawerFragment;
    private DrawerLayout mDrawerLayout;

    private ProgressDialog progressDialog;
    private SearchView mSearchView;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLeftDrawerFragment = (LeftDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mRightDrawerFragment = (RightDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_right_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mLeftDrawerFragment.setUp(R.id.navigation_drawer, mDrawerLayout);
        mRightDrawerFragment.setUp(R.id.navigation_right_drawer, mDrawerLayout);

        mTitle = getTitle();
        mHashtags = new LruCache<String, BaseTagModel>(100);
        mPrivatetags = new LruCache<String, BaseTagModel>(1000);

        accountManager = AccountManager.get(this);
        authPreferences = new AuthPreferences(this);

        VtagClient.getInstance().initalize(this);
//        browseHomePage();
        if (authPreferences.getUser() != null) {
            //browseHomePage();
            showLoginPage();
        } else {
            showLoginPage();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mLeftDrawerFragment.isDrawerOpen() && !mRightDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home_action_menu, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            mSearchView = (SearchView) searchItem.getActionView();
            setupSearchView(searchItem);

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            mSearchView.setSearchableInfo(info);
        }
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
        showProgressMessage();
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
                    HashtagsPageFragment homepage = new HashtagsPageFragment(rootData.toptags.tagcards);
                    // update the main content by replacing fragments
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, homepage)
                            .commit();
                } else {
                    Toast.makeText(HomeActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                hideProgressMessage();
            }
        });
        closeDrawers();
    }

    private void closeDrawers() {
        mLeftDrawerFragment.closeDrawer();
        mRightDrawerFragment.closeDrawer();
    }

    private LruCache<String, BaseTagModel> mHashtags;
    private LruCache<String, BaseTagModel> mPrivatetags;

    public void browseHashTag(String tag) {
        mTitle = tag;
        HashtagModel tagModel = CacheManager.getInstance().getHashTagModel(tag);
        if (tagModel == null) {
            showProgressMessage();
            VtagClient.getAPI().getTagDetails(tag, new Callback<HashtagModel>() {
                @Override
                public void onResponse(Response<HashtagModel> hashtagModelResponse) {
                    HashtagModel tagModel = hashtagModelResponse.getResult();
                    if (tagModel != null) {
                        browseHashTag(tagModel);
                    } else {
                        Toast.makeText(HomeActivity.this, "Couldnt get tag details!", Toast.LENGTH_SHORT).show();
                    }
                    hideProgressMessage();
                }
            });
        } else {
            browseHashTag(tagModel);
        }
        closeDrawers();
    }

    private void browseHashTag(HashtagModel tagModel) {
        closeDrawers();
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
        closeDrawers();
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

    private void showProgressMessage() {
        progressDialog = ProgressDialog.show(this, "Loading..",
                "Please wait..", true);
    }

    private void hideProgressMessage() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
