package me.vtag.app.views.leftpanel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.adapters.PanelListAdapter;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.PrivatetagModel;
import me.vtag.app.backend.models.PublictagModel;
import me.vtag.app.backend.models.UserModel;
import me.vtag.app.backend.models.PanelListItemModel;

public class LoggedInFragment extends Fragment {
    private ArrayList<PanelListItemModel> privateTagListItemModels = new ArrayList<>();
    private ArrayList<PanelListItemModel> publicTagListItemModels = new ArrayList<>();
    private ArrayList<PanelListItemModel> followingTagListItemModels = new ArrayList<>();
    private UserModel mUserModel;

    private ListView mPrivateTagsView;
    private ListView mFollowingTagsView;
    private ListView mPublicTagsView;

    private View mMainView;
    private int mCurrentSelectedPosition = 0;

    private ImageView mProfileImage;
    private TextView mProfileName;

    private View friendsItem;
    public LoggedInFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setProfile(UserModel userModel) {
        mUserModel = userModel;
    }

    public void addPrivateTags(List<PrivatetagModel> privatetags) {
        if (privatetags == null) return;
        for (PrivatetagModel privatetag : privatetags) {
            privateTagListItemModels.add(new PanelListItemModel(LeftDrawerItemType.PRIVATE_TAG.toString(), privatetag.tag, 0));
        }
    }
    public void addPublicTags(List<PublictagModel> publictags) {
        if (publictags == null) return;
        for (PublictagModel publictag : publictags) {
            publicTagListItemModels.add(new PanelListItemModel(LeftDrawerItemType.PRIVATE_TAG.toString(), publictag.tag, 0));
        }
    }
    public void addFollowingTags(List<HashtagModel> followingtags) {
        if (followingtags == null) return;
        for (HashtagModel followingtag : followingtags) {
            followingTagListItemModels.add(new PanelListItemModel(LeftDrawerItemType.PRIVATE_TAG.toString(), followingtag.tag, 0));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_left_panel_loggedin, container, false);
        mFollowingTagsView = (ListView) mMainView.findViewById(R.id.following_tags);
        mPrivateTagsView = (ListView) mMainView.findViewById(R.id.private_tags);
        mPublicTagsView = (ListView) mMainView.findViewById(R.id.public_tags);

        mFollowingTagsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position, mFollowingTagsView, followingTagListItemModels.get(position));
            }
        });
        mPrivateTagsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position, mPrivateTagsView, privateTagListItemModels.get(position));
            }
        });
        mPublicTagsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position, mPublicTagsView, publicTagListItemModels.get(position));
            }
        });

        mProfileImage = (ImageView) mMainView.findViewById(R.id.profile_image);
        mProfileName = (TextView) mMainView.findViewById(R.id.profile_name);
        return mMainView;
    }

    private void selectItem(int position, ListView listView, PanelListItemModel listItemModel) {
        mCurrentSelectedPosition = position;
        listView.setItemChecked(position, true);
        onNavigationDrawerItemSelected(listItemModel);
    }

    @Override
    public void onActivityCreated(Bundle saved) {
        super.onActivityCreated(saved);
        mFollowingTagsView.setAdapter(new PanelListAdapter(getActivity(), 0, followingTagListItemModels));
        mPrivateTagsView.setAdapter(new PanelListAdapter(getActivity(), 0, privateTagListItemModels));
        mPublicTagsView.setAdapter(new PanelListAdapter(getActivity(), 0, publicTagListItemModels));
        mProfileName.setText(mUserModel.displayName);
        Picasso.with(getActivity()).load(mUserModel.pic)
                .fit()
                .centerInside()
                .into(mProfileImage);
    }

    public void onNavigationDrawerItemSelected(PanelListItemModel data) {
        if (data == null) return;

        HomeActivity activity = (HomeActivity) getActivity();
        if (activity == null) return;
        if (LeftDrawerItemType.valueOf(data.getType()) == LeftDrawerItemType.HASHTAG) {
            activity.SetupTabs(data.getTitle());
//            activity.browseHashTag(data.getTitle(),data.getTitle());
        } else if (LeftDrawerItemType.valueOf(data.getType()) == LeftDrawerItemType.HOME) {
            activity.browseHomePage();
        } else if (LeftDrawerItemType.valueOf(data.getType()) == LeftDrawerItemType.PRIVATE_TAG) {
            activity.browsePrivateTag(data.getTitle());
        } else if (LeftDrawerItemType.valueOf(data.getType()) == LeftDrawerItemType.PUBLIC_TAG) {
            activity.SetupTabs(data.getTitle());
//            activity.browseHashTag(data.getTitle(),data.getTitle())
        }
    }

    public static enum LeftDrawerItemType {
        HOME,
        PROFILE,
        FRIENDS,
        PRIVATE_TAG,
        PUBLIC_TAG,
        HASHTAG
    }
}
