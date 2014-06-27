package me.vtag.app.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import me.vtag.app.R;

/**
 * Created by anuraag on 24/6/14.
 */
public class CommentBoxTagSuggestionView extends TokenCompleteTextView {

    public String FullText;
    private String tag;
    VideosComment Parent;

    public CommentBoxTagSuggestionView(Context context) {
        super(context);
    }

    public CommentBoxTagSuggestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentBoxTagSuggestionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    void getParentObject(VideosComment Parent) {
        this.Parent = Parent;
    }

    @Override
    protected View getViewForObject(Object object) {
        String p = (String)object;
        Log.w("getViewForObject "+p +" plus "+FullText,"Myapp ");
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = (View)l.inflate(R.layout.comments_tag_token, (ViewGroup) CommentBoxTagSuggestionView.this.getParent(), false);
        TextView fulltext = (TextView) view.findViewById(R.id.FullTextname);
        fulltext.setText(this.FullText);
        TextView tagtext = (TextView) view.findViewById(R.id.name);
        tagtext.setText(p);
        Parent.ET.setText(FullText+" "+p);
        return null;
    }

    @Override
    protected Object defaultObject(String completionText) {
        /*Stupid simple example of guessing if we have an email or not
        int index = completionText.indexOf('@');
        if (index == -1) {
            return new Person(completionText, completionText.replace(" ", "") + "@example.com");
        } else {
            return new Person(completionText.substring(0, index), completionText);
        }*/
        Log.w("CompletionText  "+completionText,"Myapp ");
        return completionText;
    }
}



