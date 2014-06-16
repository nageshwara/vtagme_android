package me.vtag.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.vtag.app.HomeActivity;
import me.vtag.app.R;
import me.vtag.app.backend.models.PanelListItemModel;
import me.vtag.app.backend.models.UserModel;
import me.vtag.app.pages.UserPageFragment;

/**
 * Created by nageswara on 5/1/14.
 */
public class FriendsListAdapter extends ArrayAdapter<UserModel> {
    private Context context;
    private ArrayList<UserModel> usermodels;
    private UserPageFragment mUserPageFragment = new UserPageFragment();

    public FriendsListAdapter(Context context, int layoutResourceId, ArrayList<UserModel> usermodels){
        super(context, layoutResourceId, usermodels);
        this.context = context;
        this.usermodels = usermodels;
    }

    @Override
    public int getCount() {
        return usermodels.size();
    }

    @Override
    public UserModel getItem(int position) {
        return usermodels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.w("Came to getView of PanelListAdapter ","Myapp ");
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.panel_list_item, null);
        }

//        Log.w("Came to PanelListAdapter", "Myapp ");
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
        txtCount.setVisibility(View.INVISIBLE);
        txtTitle.setText(usermodels.get(position).name);

        //imgIcon.setImageResource(usermodels.get(position).getIcon());
        if (usermodels.get(position).pic != null) {
            Log.w("Pic "+usermodels.get(position).pic, "Myapp ");
            Picasso.with(this.getContext()).load(usermodels.get(position).pic)
                    .fit().centerCrop()
                    .into(imgIcon);
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("You clicked this user","Myapp ");
                mUserPageFragment.addUser(usermodels.get(position));
                ((HomeActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.container, mUserPageFragment).addToBackStack(null).commit();
            }
        });
        // displaying count
        // check whether it set visible or not
        return convertView;
    }
}
