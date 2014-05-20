package me.vtag.app;

import android.accounts.AccountManager;
import android.app.Activity;
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

import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.vos.RootVO;
import me.vtag.app.models.AuthPreferences;
import me.vtag.app.pages.FinishSignupPageFragment;
import me.vtag.app.pages.TagsPageFragment;
import me.vtag.app.pages.LoginPageFragment;
import me.vtag.app.pages.TagPageFragment;
import me.vtag.app.views.QueueFragment;


public class WelcomeActivity extends ActionBarActivity
        implements SearchView.OnQueryTextListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private LeftDrawerFragment mLeftDrawerFragment;
    private RightDrawerFragment mRightDrawerFragment;
    private DrawerLayout mDrawerLayout;

    public static AccountManager accountManager;
    public static AuthPreferences authPreferences;

    private ProgressDialog progressDialog;
    private SearchView mSearchView;
    public static QueueFragment mQueueFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mLeftDrawerFragment = (LeftDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mRightDrawerFragment = (RightDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_right_drawer);
        mQueueFragment = new QueueFragment();

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

    public void showLoginPage() {
        mTitle = "Login";
        // Now show list of tags.
        LoginPageFragment loginPage = new LoginPageFragment();
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, loginPage)
                .commit();
        closeDrawers();
    }

    public void showSignupPage() { showSignupPage(null, null); }
    public void showSignupPage(String email) { showSignupPage(email, null); }
    public void showSignupPage(String email, String username) {
        mTitle = "Signup";
        // Now show list of tags.
        FinishSignupPageFragment signupPageFragment = new FinishSignupPageFragment(email, username);
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, signupPageFragment)
                .commit();
        closeDrawers();
    }


    public void browseHomePage() {
        mTitle = "Home";
        final Activity activity = this;
        showProgressMessage();
        Log.w("you are in ", "Myapp");
        VtagClient.getInstance().getRootDetails(new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                Gson gson = new Gson();
                Type listType = new TypeToken<RootVO>(){}.getType();
                RootVO rootData = gson.fromJson(responseBody, listType);
                if (rootData.user == null) {
                    authPreferences.setUser(null, null); // Next time shows login page.
                    mLeftDrawerFragment.setLoggedIn(null, rootData);
                } else {
                    mLeftDrawerFragment.setLoggedIn(rootData.user, rootData);
                    // Store private and public tags in LRU cache..
                    for (BaseTagModel tagModel : rootData.privatetags) {
                        mPrivatetags.put(tagModel.tag, tagModel);
                    }
                }
                // Now show list of tags.
                TagsPageFragment homepage = new TagsPageFragment(rootData.toptags.tagcards);
                // update the main content by replacing fragments
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, homepage)
                        .commit();
                hideProgressMessage();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, java.lang.Throwable e) {
                hideProgressMessage();
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_SHORT).show();
                System.out.println("wow " + e.getMessage());
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
        final Activity activity = this;
        BaseTagModel tagModel = mHashtags.get(tag);
        if (tagModel == null) {
            showProgressMessage();
            VtagClient.getInstance().getTagDetails(tag, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<BaseTagModel>() {
                    }.getType();
                    BaseTagModel tagModel = gson.fromJson(responseBody, listType);
                    browseHashTag(tagModel);
                    hideProgressMessage();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseBody, java.lang.Throwable e) {
                    hideProgressMessage();
                    Toast.makeText(activity, "Couldnt get tag details!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            browseHashTag(tagModel);
        }
        closeDrawers();
    }

    private void browseHashTag(BaseTagModel tagModel) {
        closeDrawers();
        mHashtags.put(tagModel.tag, tagModel);

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
        BaseTagModel tagModel = mPrivatetags.get(tag);
        closeDrawers();
        if (tagModel == null) {
            return;
        } else {
            browsePrivateTag(tagModel);
        }
    }

    private void browsePrivateTag(BaseTagModel tagModel) {
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
