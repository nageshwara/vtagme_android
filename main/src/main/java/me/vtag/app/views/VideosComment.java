package me.vtag.app.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.R;
import me.vtag.app.UsernameTokenizer;
import me.vtag.app.adapters.CommentsListAdapter;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.CommentModel;
import me.vtag.app.backend.models.VideoModel;

/**
 * Created by anuraag on 20/6/14.
 */
public class VideosComment extends Fragment implements
        TokenCompleteTextView.TokenListener,
        TextWatcher{
    View mMainView;
    ListView mCommentsListView;
    public MultiAutoCompleteTextView mTagAutoCompletionView;

    private CommentsListAdapter mCommentsListAdapter;

    private int mSuggestionTagSelected = 0;
    private String mSelectedTag = "";
    private List<CommentModel> mCommentslist;
    private VideoModel mVideoModel;
    //float px;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mVideoModel = getArguments().getParcelable("video");
        mMainView = inflater.inflate(R.layout.video_comments_list, container, false);
//        final EditText mTagAutoCompletionView = (EditText) mMainView.findViewById(R.id.comments_input);

        mTagAutoCompletionView = (MultiAutoCompleteTextView) mMainView.findViewById(R.id.multiAutoCompleteTextView);
        mTagAutoCompletionView.setTokenizer(new UsernameTokenizer());
        mTagAutoCompletionView.addTextChangedListener(this);
        mTagAutoCompletionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSuggestionTagSelected = 1;
                mSelectedTag = (String) adapterView.getItemAtPosition(i);
                Log.w("Selected string is " + mSelectedTag, "Myapp");
                int length = mTagAutoCompletionView.getText().length();
                Log.w("Html set up " + mTagAutoCompletionView.getText().subSequence(0, length - mSelectedTag.length() - 1), "Myapp ");
                String[] tem = mTagAutoCompletionView.getText().toString().split("#");
                String temp = tem[0];//+"#";
                int j = 1;
                if (mTagAutoCompletionView.getText().toString().charAt(0) == '#') {
                    j = 0;
                }
                for (; j < tem.length; j++) {
                    String i1 = tem[j];
                    String[] tem11 = i1.split(" ");
                    temp = temp + "#<font color='red'>" + tem11[0] + "</font>" + " " + i1.substring(tem11[0].length() + 1);
                }
                if (temp.length() == 1 && temp == "#") {
                    temp = "#";
                }
                mTagAutoCompletionView.setText(Html.fromHtml(temp), TextView.BufferType.SPANNABLE);
//                mTagAutoCompletionView.setText(Html.fromHtml(mTagAutoCompletionView.getText().subSequence(0, length - mSelectedTag.length()-1) + "<font color='red'>" + mSelectedTag + "</font>"), TextView.BufferType.SPANNABLE); //13 for the length of <mark></mark>
                Log.w("" + mTagAutoCompletionView.getText(), "Myapp ");
                mTagAutoCompletionView.setSelection(length - 1);
            }
        });

        Button Submit = (Button) mMainView.findViewById(R.id.comments_input_submit);
        Submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String text = mTagAutoCompletionView.getText().toString();//mTagAutoCompletionView.FullText;
                Log.w("before addComments " + mTagAutoCompletionView.getText(), "Myapp ");
                VtagClient.getAPI().addComments("5233675348213760",text,"8742","{}", new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> hashtagModelResponse) {
                    }
                });
            }
        });
        mCommentsListView = (ListView) mMainView.findViewById(R.id.comments_listview);

        mCommentslist = mVideoModel.goofs;
        CommentModel temp = new CommentModel();
        temp.c = "apple is bad";
        mCommentslist.add(temp);

        mCommentsListAdapter = new CommentsListAdapter(getActivity(),0, mCommentslist);
        mCommentsListView.setAdapter(mCommentsListAdapter);

        /*Resources r = getResources();
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 62, r.getDisplayMetrics()) + .5f;
        Log.w("Comments Size of image "+Float.toString(px),"Myapp ");

        ViewGroup.LayoutParams params = mCommentsListView.getLayoutParams();
        params.height = ((int) px)*mCommentsListAdapter.getCount();
        mCommentsListView.setLayoutParams(params);
        mCommentsListView.requestLayout();*/

        return mMainView;
    }

    @Override
    public void onTokenAdded(Object token) {

    }

    @Override
    public void onTokenRemoved(Object token) {

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    public class SuggestTagsAsyncTask extends AsyncTask<String, String, String> {
        public String data;
        public List<String> suggest;
        public ArrayAdapter<String> aAdapter;

        @Override protected String doInBackground(String... key) {
            Log.w("doInBackground "+key[0],"Myapp ");
            String[] newText1 = key[0].split(" ");
            String newText = newText1[newText1.length-1];
            newText1 = newText.split("#");
            if(newText1.length > 1) {
                newText = newText1[newText1.length - 1];
            }
            suggest = new ArrayList<String>();

            Log.w("doInBackground "+newText,"Myapp ");
            try {
                HttpClient hClient = new DefaultHttpClient();
                HttpGet hGet = new HttpGet("http://192.168.1.2:8080/tagsearch/"+newText);

                ResponseHandler<String> rHandler = new BasicResponseHandler();
                data = hClient.execute(hGet, rHandler);
                Log.w(""+data.toString(),"Myapp ");
                String s2= data.toString().replaceAll("\"", "");
                s2=s2.replace(" ","");
                s2=s2.replace("[", "");
                s2=s2.replace("]", "");
                String[] tags = s2.split(",");

//                String[] tags = data.toString().split(" \" ");
                for (int i = 0;i<tags.length;i++) {
                    Log.w("i : "+tags[i],"Myapp");
                        suggest.add(tags[i]+" ");
                }
            } catch (Exception e) {
                Log.w("Error", e.getMessage());
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.w("Something called blah blah "+Integer.toString(suggest.size()),"Myapp ");
                    MultiAutoCompleteTextView inputEditText = (MultiAutoCompleteTextView) getActivity().findViewById(R.id.multiAutoCompleteTextView);
                    aAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_dropdown_item_1line, suggest);
                    inputEditText.setAdapter(aAdapter);
                    aAdapter.notifyDataSetChanged();
                    inputEditText.showDropDown();
                }
            });

            return null;
        }

    }

    private int CommentsLength = 0;
    private int SpacePressed = 0;
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int cursor = start;
        int length = s.length();
        Log.w("mSuggestionTagSelected is "+Integer.toString(mSuggestionTagSelected)+"onTextChanged "+s.toString() +" starts at : "+Integer.toString(start)+" s length is "+Integer.toString(s.length()),"Myapp ");
/*
        if(mSuggestionTagSelected == 2) {
            mSuggestionTagSelected = 0;
            SpacePressed = 1;
            return;
        }
        */
//        else if(mSuggestionTagSelected == 1) {
            mSuggestionTagSelected = 2;
/*
            Log.w("Prediction Mission accomplished!","Myapp ");
            String[] hashSplit = s.toString().split("@");
            String lasthash = hashSplit[hashSplit.length-1];
            */
//            String[] hashSplit1 = lasthash.split(" ");
//            Log.w("hashSplit1 length is "+Integer.toString(hashSplit1.length),"Myapp ");
//            Log.w("hashSplit1 "+hashSplit1.toString(),"Myapp ");
//            String lasthash1 = hashSplit1[hashSplit1.length-1];
            /*
            Log.w("Selected string is "+mSelectedTag,"Myapp");

            Log.w("Html set up "+s.subSequence(0, length - mSelectedTag.length()),"Myapp ");
            mTagAutoCompletionView.setText(Html.fromHtml(s.subSequence(0, length - mSelectedTag.length()) + "<mark>" + mSelectedTag + "</mark>"), TextView.BufferType.SPANNABLE); //13 for the length of <mark></mark>
            */
//                mTagAutoCompletionView.setText(Html.fromHtml("<font color='red'>are you ?? Working???</font>"), TextView.BufferType.SPANNABLE);
//            mTagAutoCompletionView.setSelection(length + 13);
//        }
            String[] hashSplit = s.toString().split("#");
        if (hashSplit.length > 1) {
            String lasthash = hashSplit[hashSplit.length - 1];
            String[] hashSplit1 = lasthash.split(" ");
            Log.w("hashSplit1 length is " + Integer.toString(hashSplit1.length), "Myapp ");
            Log.w("hashSplit1 " + hashSplit1.toString(), "Myapp ");
            String lasthash1 = hashSplit1[hashSplit1.length - 1];
            Log.w("Lasthash is " + lasthash, "Myapp");
/*
            String[] hashSplit = s.toString().split("<mark>");
            String lasthash = hashSplit[hashSplit.length-1];
            String[] hashSplit1 = lasthash.split("</mark>");
            Log.w("hashSplit1 length is "+Integer.toString(hashSplit1.length),"Myapp ");
            String lasthash1 = hashSplit1[hashSplit1.length-1];
            Log.w("Lasthash is "+lasthash,"Myapp");
*/
            if (CommentsLength > s.length() && s.length() > 0) {
                try {
                    Log.w("Backspace pressed !! " + s.toString(), "Myapp ");
                    if (SpacePressed == 0 && hashSplit.length > 1 && hashSplit1.length == 1) {
                        mTagAutoCompletionView.setText(s.subSequence(0, length - lasthash1.length() - 1));
                        mTagAutoCompletionView.setSelection(length - lasthash1.length() - 1);
                    }
                } finally {
                    //nothing
                }
            }
        }
        try {
            if(length > 1) {
                if (s.toString().substring(length - 1).equals(" ")) {
                    SpacePressed = 1;
                }
            } else {
                SpacePressed = 0;
            }
        }
        finally {

        }

        CommentsLength = s.length();
        if (cursor >= s.length()) cursor = s.length()-1;
        if (isValidToken(s, cursor)){
            String token = getToken(s, start);
            new SuggestTagsAsyncTask().execute( token );
        }
    }
    @Override
    public void afterTextChanged(Editable editable) {

    }

    public ArrayAdapter<String> aAdapter;
    void fetchTags(String input) {
        List<String> resultList = null;
        Log.w("fetchTags the token is "+input,"Myapp ");
    }

    /**
     * Checks if the current word being edited is a valid token (e.g. starts with @ and has no spaces)
     * @param text - all text being edited in input
     * @param cursor - current position of text change
     * @return is valid
     */
    private boolean isValidToken(CharSequence text, int cursor){
        int len = 0;
        for (int i=cursor; i>=0; i--){
            len++;
            Log.w("Valid? "+text.charAt(i),"Myapp ");
            if (text.charAt(i) == '#' && len>=2) return true;
            if (text.charAt(i) == ' ') return false;
        }
        return false;
    }
    /**
     * Fetches the current token being edited - assumes valid token (use isValidToken to confirm)
     * @param text
     * @param cursor
     * @return
     */
    private String getToken(CharSequence text, int cursor){
        int start=findTokenStart(text, cursor);
        int end=findTokenEnd(text, cursor);
        return text.subSequence(start, end).toString();
    }
    /**
     * In the current input text, finds the start position of the current token (iterates backwards from current position
     * until finds the token prefix "@")
     * @param text - all text being edited in input
     * @param cursor - current position of text change
     * @return position of token start
     */
    private int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;
        while (i > 0 && text.charAt(i - 1) != '#') { i--; }
        return i;
    }

    /**
     * In the current input text, finds the position of the end of the current token (iterates forwards from current
     * position
     * until finds the the end, e.g. a space or end of all input)
     * @param text - all text being edited in input
     * @param cursor - current position of text change
     * @return position of token end
     */
    private int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();
        while (i < len && text.charAt(i) != ' ' && text.charAt(i) != ',' && text.charAt(i) != '.' ) {
            i++;
        }
        return i;
    }

}
