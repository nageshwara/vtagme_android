package me.vtag.app.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import me.vtag.app.BasePageFragment;
import me.vtag.app.R;
import me.vtag.app.adapters.HashtagListAdapter;
import me.vtag.app.adapters.TagListAdapter;
import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.HashtagModel;

/**
 * Created by nmannem on 30/10/13.
 */
public class HashtagsPageFragment extends BasePageFragment {
    public static final int ID = 2;

    private ListView tagListView;
    private List<HashtagModel> tagModels;
    public HashtagsPageFragment(List<HashtagModel> tagModels) {
        super(ID);
        this.tagModels = tagModels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_page_fragment, container, false);
        tagListView = (ListView) rootView.findViewById(R.id.tagListView);
        tagListView.setAdapter(new HashtagListAdapter(getActivity(), R.layout.tagcard, this.tagModels));
        return rootView;
    }
}
