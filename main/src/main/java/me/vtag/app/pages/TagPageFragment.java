package me.vtag.app.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import me.vtag.app.BasePageFragment;
import me.vtag.app.R;
import me.vtag.app.adapters.VideoListAdapter;
import me.vtag.app.backend.models.BaseTagModel;

/**
 * Created by nmannem on 30/10/13.
 */
public class TagPageFragment extends BasePageFragment {
    public static final int ID = 1;

    private BaseTagModel tag;
    private ListView videoListView;

    public static String YOUTUBE_API_KEY = "AIzaSyBmlPp_uA1HddVULhDpsLVjX1q7GRqc7Eg";


    public TagPageFragment() {
        super(ID);
        this.tag = null;
    }
    public TagPageFragment(BaseTagModel tag) {
        super(ID);
        this.tag = tag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tag_page_fragment, container, false);
        videoListView = (ListView) rootView.findViewById(R.id.videoListView);
        videoListView.setAdapter(new VideoListAdapter(getActivity(), R.layout.videocard, this.tag.videodetails));
        return rootView;
    }
}
