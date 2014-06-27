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
 * Sample token completion view for basic contact info
 *
 * Created on 9/12/13.
 * @author mgod
 */
public class TagsCompletionView extends TokenCompleteTextView {
    public String FullText;
    private String tag;

    public TagsCompletionView(Context context) {
        super(context);
    }

    public TagsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Object object) {
/*
        String p = (String)object;
        Log.w("getViewForObject " + p + " plus " + FullText, "Myapp ");
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = (View)l.inflate(R.layout.tag_token, (ViewGroup) TagsCompletionView.this.getParent(), false);
        TextView fulltext = (TextView) view.findViewById(R.id.FullTextname);
        fulltext.setText(this.FullText);
        TextView tagtext = (TextView) view.findViewById(R.id.name);
        tagtext.setText(p);
        return view;
*/
        String p = (String)object;
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView)l.inflate(R.layout.tag_token, (ViewGroup) TagsCompletionView.this.getParent(), false);
        view.setText(p);
        return view;
    }

    public TagsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        return completionText;
    }
}
