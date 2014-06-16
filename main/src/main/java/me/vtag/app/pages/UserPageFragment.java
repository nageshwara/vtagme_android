package me.vtag.app.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.backend.models.UserModel;

/**
 * Created by anuraag on 16/6/14.
 */
public class UserPageFragment extends Fragment {
    private View mMainView;
    UserModel userModel = null;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.user_page, container, false);
        ImageView userDP = (ImageView) mMainView.findViewById(R.id.UserDP);

        if(userModel.pic != null) {
            Picasso.with((HomeActivity) getActivity()).load(userModel.pic)
                    .fit().centerCrop()
                    .into(userDP);
        }

        return mMainView;
    }

    public void addUser(UserModel userModel) {
        this.userModel = userModel;
    }
}
