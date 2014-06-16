package me.vtag.app.pages;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.R;
import me.vtag.app.adapters.FriendsListAdapter;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.FriendsListModel;
import me.vtag.app.backend.models.RelationDetails;
import me.vtag.app.backend.models.UserModel;

/**
 * Created by anuraag on 13/6/14.
 */
public class FriendsPageFragment extends Fragment{
    private View mMainView;
    private ListView mfollowingListView;
    private ListView mfollowerListView;
    private ArrayList<UserModel> followingArrayList = new ArrayList<>();
    private ArrayList<UserModel> followerArrayList = new ArrayList<>();
    private FriendsListAdapter mfollowerListAdapter,mfollowingListAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.friends_page_list,container,false);

        UserModel temp = new UserModel();
        temp.id = "1111";
        temp.name = "Christian Bane";
        temp.pic = "http://fbcdn-sphotos-h-a.akamaihd.net/hphotos-ak-xpf1/t1.0-9/1888611_807802405903486_858173511_n.jpg";

        mfollowingListView = (ListView) mMainView.findViewById(R.id.FollowingList);
        mfollowingListAdapter = new FriendsListAdapter(getActivity(), 0, followingArrayList);
        mfollowingListView.setAdapter(mfollowingListAdapter);

        mfollowerListView = (ListView) mMainView.findViewById(R.id.FollowerList);
        mfollowerListAdapter = new FriendsListAdapter(getActivity(), 0, followerArrayList);
        mfollowerListView.setAdapter(mfollowerListAdapter);

//        setListViewHeightBasedOnChildren(mfollowerListView);
//        setListViewHeightBasedOnChildren(mfollowingListView);
//        addFriendsArrayList();

        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, r.getDisplayMetrics()) + 0.5f;

        ViewGroup.LayoutParams params = mfollowingListView.getLayoutParams();
        params.height = ((int) px)*followingArrayList.size();
        mfollowingListView.setLayoutParams(params);
//        listView.requestLayout();
        ViewGroup.LayoutParams params1 = mfollowerListView.getLayoutParams();
        params.height = ((int) px)*followerArrayList.size();
        mfollowerListView.setLayoutParams(params1);

        return mMainView;
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            Log.w("SSSSIIIIIIIIIIZEEEE "+Float.toString(listItem.getMeasuredHeight()),"Myapp ");
            totalHeight += listItem.getMeasuredHeight();
            Log.w("SIze"+Integer.toString(listItem.getMeasuredHeight()),"Myapp ");
        }
/*
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
*/
    }

    public void addFriendsArrayList() {
        Log.w("addFriendsArrayList","Myapp ");

        VtagClient.getAPI().getFriendsList(new Callback<FriendsListModel>() {
            @Override
            public void onResponse(Response<FriendsListModel> hashtagModelResponse) {
                FriendsListModel tagModel11 = hashtagModelResponse.getResult();
                for (RelationDetails i : tagModel11.followers) {
                    UserModel temp = new UserModel();
                    temp.id = i.id;
                    temp.name = i.username;
                    temp.pic = i.pic;
                    Log.w("The name is "+temp.name,"Myapp ");
                    followerArrayList.add(temp);
                }
                for (RelationDetails i : tagModel11.following) {
                    UserModel temp = new UserModel();
                    temp.id = i.id;
                    temp.name = i.username;
                    temp.pic = i.pic;
                    Log.w("The name is "+temp.name,"Myapp ");
                    followingArrayList.add(temp);
                }
/*
                mfollowerListAdapter.clear();
                mfollowerListAdapter.addAll(followerArrayList);

                mfollowingListAdapter.clear();
                mfollowingListAdapter.addAll(followingArrayList);
*/

            }
        });

    }
}