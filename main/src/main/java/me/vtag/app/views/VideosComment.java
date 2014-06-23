package me.vtag.app.views;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.R;
import me.vtag.app.adapters.CommentsListAdapter;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.CommentModel;
import me.vtag.app.backend.models.CommentsDescribeModel;
import me.vtag.app.backend.models.GetgoofModel;

/**
 * Created by anuraag on 20/6/14.
 */
public class VideosComment extends Fragment {
    View mMainView;
    ArrayList<CommentsDescribeModel> commentslist = new ArrayList<>();
    CommentsListAdapter mCommentsListAdapter;
    ListView mCommentsListView;
    float px;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w("VideosComment view is onCreate","Myapp");
        mMainView = inflater.inflate(R.layout.video_comments_list, container, false);
        final EditText ET = (EditText) mMainView.findViewById(R.id.comments_input);
        Button Submit = (Button) mMainView.findViewById(R.id.comments_input_submit);
        Submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String text = ET.getText().toString();
                Log.w("before addComments","Myapp ");
                VtagClient.getAPI().addComments("5233675348213760",text,"8742","{}", new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> hashtagModelResponse) {
                    }
                });
            }
        });
        mCommentsListView = (ListView) mMainView.findViewById(R.id.comments_listview);
        CommentsDescribeModel temp = new CommentsDescribeModel();
        temp.c = "appel is bad";
        commentslist.add(temp);
        mCommentsListAdapter = new CommentsListAdapter(getActivity(),0,commentslist);
        mCommentsListView.setAdapter(mCommentsListAdapter);
        Resources r = getResources();
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 54, r.getDisplayMetrics()) + .5f;
        Log.w("Comments Size of image "+Float.toString(px),"Myapp ");

        ViewGroup.LayoutParams params = mCommentsListView.getLayoutParams();
        params.height = ((int) px)*mCommentsListAdapter.getCount();
        mCommentsListView.setLayoutParams(params);
        mCommentsListView.requestLayout();

        return mMainView;
    }

    public void fetchComments(String Videoid) {
        VtagClient.getAPI().getComments(Videoid, new Callback<GetgoofModel>() {
            @Override
            public void onResponse(Response<GetgoofModel> hashtagModelResponse) {
                GetgoofModel tagModel = hashtagModelResponse.getResult();
                commentslist = tagModel.video.goofs;
                mCommentsListAdapter.clear();
                mCommentsListAdapter.addAll(commentslist);
                mCommentsListAdapter.notifyDataSetChanged();

                ViewGroup.LayoutParams params = mCommentsListView.getLayoutParams();
                params.height = ((int) px)*mCommentsListAdapter.getCount();
                mCommentsListView.setLayoutParams(params);
                mCommentsListView.requestLayout();
            }
        });
    }
}
