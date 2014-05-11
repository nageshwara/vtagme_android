package me.vtag.app;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.lang.reflect.Type;

import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.vos.RootVO;
import me.vtag.app.models.AuthPreferences;
import me.vtag.app.models.PanelListItemModel;
import me.vtag.app.pages.FinishSignupPageFragment;
import me.vtag.app.pages.HomePageFragment;
import me.vtag.app.pages.LoginPageFragment;
import me.vtag.app.pages.TagPageFragment;


public class WelcomeActivity extends ActionBarActivity
        implements LeftDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private LeftDrawerFragment mLeftDrawerFragment;
    private RightDrawerFragment mRightDrawerFragment;

    public static AccountManager accountManager;
    public static AuthPreferences authPreferences;

    private ProgressDialog progressDialog;

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

        mTitle = getTitle();
        mHashtags = new LruCache<String, BaseTagModel>(100);

        VtagClient.getInstance().initalize(this);
        accountManager = AccountManager.get(this);
        authPreferences = new AuthPreferences(this);
        if (authPreferences.getUser() != null
                && authPreferences.getToken() != null) {
            browseHomePage();
        } else {
            browseHomePage();
            //showLoginPage();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(PanelListItemModel data) {
        if (data == null) return;
        if (LeftDrawerFragment.LeftDrawerItemType.valueOf(data.getType()) == LeftDrawerFragment.LeftDrawerItemType.HASHTAG) {
            browseHashTag(data.getTitle());
        } else if (LeftDrawerFragment.LeftDrawerItemType.valueOf(data.getType()) == LeftDrawerFragment.LeftDrawerItemType.HOME) {
            browseHomePage();
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

    public void showLoginPage() {
        mTitle = "Login";
        // Now show list of tags.
        LoginPageFragment loginPage = new LoginPageFragment();
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, loginPage)
                .commit();
    }

    public void showSignupPage(String email, String username) {
        mTitle = "Signup";
        // Now show list of tags.
        FinishSignupPageFragment signupPageFragment = new FinishSignupPageFragment(email, username);
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, signupPageFragment)
                .commit();
    }


    public void browseHomePage() {
        mTitle = "Home";
        final Activity activity = this;
        VtagClient.getInstance().getRootDetails(new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                Gson gson = new Gson();
                Type listType = new TypeToken<RootVO>(){}.getType();
                RootVO rootData = gson.fromJson(responseBody, listType);
                // Set up the drawer.
                mLeftDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
                mLeftDrawerFragment.addPrivateTags(rootData.privatetags);
                mLeftDrawerFragment.addFollowingTags(rootData.followingtags);
                mLeftDrawerFragment.addPublicTags(rootData.publictags);

                // Now show list of tags.
                HomePageFragment homepage = new HomePageFragment(rootData.toptags.tagcards);
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
    }


    private LruCache<String, BaseTagModel> mHashtags;
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
    }

    private void browseHashTag(BaseTagModel tagModel) {
        mHashtags.put(tagModel.tag, tagModel);

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
                "Please wait,,,,,,....", true);
    }

    private void hideProgressMessage() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
