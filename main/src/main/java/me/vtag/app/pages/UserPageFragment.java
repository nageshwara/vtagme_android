package me.vtag.app.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.VtagApplication;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.UserModel;

/**
 * Created by anuraag on 16/6/14.
 */
public class UserPageFragment extends Fragment {
    private View mMainView;
    UserModel userModel = null;
    private boolean following = false;
    private ArrayList<UserModel> followingusermodels;
    private FriendsPageFragment friendsPageFragment = new FriendsPageFragment();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.user_page, container, false);
        ImageView userDP = (ImageView) mMainView.findViewById(R.id.UserDP);
        TextView username = (TextView) mMainView.findViewById(R.id.username);
        friendsPageFragment.addFriendsArrayList();

//        ((HomeActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.friends_list, friendsPageFragment).addToBackStack(null).commit();

/*        FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.friends_list, );
        transaction.addToBackStack(null);
        transaction.commit();
*/

        username.setText(userModel.name);

        if(userModel.pic != null) {
            Picasso.with((HomeActivity) getActivity()).load(userModel.pic)
                    .into(userDP);
        }

        final Button follow_friend = (Button) mMainView.findViewById(R.id.follow);

        Log.w(userModel.name,"Myapp");
        for(UserModel i : followingusermodels) {
            if (i.name.equals(userModel.name)) {
                follow_friend.setText("Unfollow");
                following = true;
                break;
            }
        }
        follow_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onToggleSubscription();

                if(following == false) {
                    VtagClient.getAPI().followUser(userModel.id, new Callback<String>() {
                        @Override
                        public void onResponse(Response<String> response) {
                            if (response.getResult() == "true") {
                                following = true;
                                follow_friend.setText("Unfollow");
                            }
                        }
                    });
                }
                else {
                    VtagClient.getAPI().unfollowUser(userModel.id, new Callback<String>() {
                        @Override
                        public void onResponse(Response<String> response) {
                            if (response.getResult() == "true") {
                                following = false;
                                follow_friend.setText("Follow");
                            }
                        }
                    });
                }
            }
        });

        return mMainView;
    }

    private void onToggleSubscription() {

    }

    public void addUser(UserModel userModel, ArrayList<UserModel> followingusermodels) {
        this.userModel = userModel;
        this.followingusermodels = followingusermodels;
    }
}
