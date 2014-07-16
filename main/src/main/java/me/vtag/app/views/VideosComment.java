package me.vtag.app.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
/*
        mTagAutoCompletionView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                Log.w("key pressed ","Myapp ");
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.w(""+mTagAutoCompletionView.getSelectionStart(),"Myapp ");
                    Log.w("BackSpace pressed ","Myapp ");
                }
                return false;
            }
        });
*/
        /*
        mTagAutoCompletionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                int j = 1;
                mSelectedTag = (String) adapterView.getItemAtPosition(i);
                String Comment = mTagAutoCompletionView.getText().toString();
                int lengthOfComment = Comment.length();
                String[] splitByDoubleHash = Comment.split("##");
                String NewComment = "";
                if(!(Comment.substring(0,2).equals("##"))) {
                    NewComment = splitByDoubleHash[0];
                }
                else {
                    j = 0;
                }

                int flag = 0;
                for(;j<splitByDoubleHash.length;j++) {
                    flag = 1;
                    NewComment = NewComment + "<font color='red'>##" + splitByDoubleHash[j].split(" ")[0]+ "</font> ";
                    String[] splitBySingleHash = splitByDoubleHash[j].substring(splitByDoubleHash[j].split(" ")[0].length()+1).split("#");
                    int k = 1;
                    if(splitBySingleHash[0].substring(0,1).equals("#")) {
                        k = 0;
                    }
                    for(;k<splitBySingleHash.length;k++) {
                        NewComment = NewComment + "#<font color='red'>" + splitBySingleHash[k].split(" ")[0] + "</font> ";
                        NewComment = NewComment + splitBySingleHash[k].substring(splitBySingleHash[k].split(" ")[0].length()+1);
                    }
                }

                if(flag == 0) {
                    Log.w("The flag is 0","Myapp ");
                    String[] splitBySingleHash = Comment.split("#");
                    int k = 1;
                    if(!(Comment.substring(0,1).equals("#"))) {
                        NewComment = splitBySingleHash[0];
                    }
                    else {
                        k = 0;
                    }
                    for(;k<splitBySingleHash.length;k++) {
                        NewComment = NewComment + "<font color='red'>#" + splitBySingleHash[k].split(" ")[0] + "</font> ";
                        NewComment = NewComment + splitBySingleHash[k].substring(splitBySingleHash[k].split(" ")[0].length()+1);
                    }
                }
                mTagAutoCompletionView.setText(Html.fromHtml(NewComment), TextView.BufferType.SPANNABLE);
                Log.w("" + mTagAutoCompletionView.getText() + " length is "+ mTagAutoCompletionView.getText().length() + " newcomment length  "+NewComment.length(), "Myapp ");
                mTagAutoCompletionView.setSelection(mTagAutoCompletionView.length());
            }
        });
*/
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
                HttpGet hGet = new HttpGet("http://192.168.1.4:8080/tagsearch/"+newText);

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
    private int once = 0,prev_length = 0;
    private String prevText = "";
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int cursor = start;
        int length = s.length();
        Log.w("s is "+s+" start "+start+" before "+before+" count "+count,"Myapp");
        Log.w("is it space ? "+(s.charAt(start+count-1)==' '),"Myapp ");
        Log.w("The new letter added is "+s.charAt(start+count-1),"Myapp ");
        Log.w("cursor at "+mTagAutoCompletionView.getSelectionStart(),"Myapp ");

        if(s.length() < prev_length) {
            Log.w("Backspace pressed","Myapp");
            int presentPosition = mTagAutoCompletionView.getSelectionStart();
            String beforeComment,afterComment;
            Log.w("cursor at "+presentPosition,"Myapp ");
            for(int i = presentPosition-1;i>=0 ;i--) {
                if(s.charAt(i) == ' ' || prevText.charAt(presentPosition) == ' ') {
                    break;
                }
                if(s.charAt(i) == '#') {
                    if(i>=1) {
                        if (s.charAt(i - 1) == '#') {
                            beforeComment = s.subSequence(0,i-1).toString();
                            afterComment = s.subSequence(presentPosition,s.length()).toString();
                            mTagAutoCompletionView.setText(beforeComment+afterComment);
                            break;
                        }
                    }
                    beforeComment = s.subSequence(0,i).toString();
                    afterComment = s.subSequence(presentPosition,s.length()).toString();
                    mTagAutoCompletionView.setText(beforeComment+afterComment);
                }
            }
            prev_length = s.length();
            return;
        }
        prev_length = s.length();

        if(s.charAt(start+count-1)==' ' && once == 0) {     //the variable once is used to avoid recursion
            //what i do here is that, every time a new word is completed (indicated by entering space) i convert
            //all the words starting with # or ## (ie, hashtags) i put the word(or tag) into the HTML tag (<font></font>)
            //to highlight the tag.....
            once = 1;
            int j = 1;
            String Comment = mTagAutoCompletionView.getText().toString();
            int lengthOfComment = Comment.length();
            String[] splitByDoubleHash = Comment.split("##");
            String NewComment = "";
            if(!(Comment.substring(0,2).equals("##")) && splitByDoubleHash.length > 1) {
//                NewComment = splitByDoubleHash[0];

                String[] splitBySingleHash = splitByDoubleHash[0].split("#");
                int k = 1;
                if(!(splitByDoubleHash[0].substring(0,1).equals("#"))) {
                    NewComment = NewComment + splitBySingleHash[0];
                }
                else {
                    Log.w("First one is # "+splitBySingleHash[1],"Myapp ");
//                    k = 0;
                }
                for(;k<splitBySingleHash.length;k++) {
                    NewComment = NewComment + "<font color='red'>#" + splitBySingleHash[k].split(" ")[0] + "</font> ";
                        Log.w("yen   "+splitBySingleHash[k].split(" ")[k].length() + " "+splitBySingleHash[k].length(),"Myapp ");
                        NewComment = NewComment + splitBySingleHash[k].substring(splitBySingleHash[k].split(" ")[0].length() + 1);
                }
            }
            else {
//                j = 0;
            }

            int flag = 0;
            for(;j<splitByDoubleHash.length;j++) {
                flag = 1;
                NewComment = NewComment + "<font color='red'>##" + splitByDoubleHash[j].split(" ")[0]+ "</font> ";
                String[] splitBySingleHash = splitByDoubleHash[j].substring(splitByDoubleHash[j].split(" ")[0].length()+1).split("#");
                int k = 1;
                if(!(splitByDoubleHash[j].substring(splitByDoubleHash[j].split(" ")[0].length()+1).substring(0,1).equals("#"))) {
                    NewComment = NewComment + splitBySingleHash[0];
                }
                else {
                    k = 0;
                }
                for(;k<splitBySingleHash.length;k++) {
                    NewComment = NewComment + "<font color='red'>#" + splitBySingleHash[k].split(" ")[0] + "</font> ";
                    NewComment = NewComment + splitBySingleHash[k].substring(splitBySingleHash[k].split(" ")[0].length()+1);
                }
            }

            if(flag == 0) {
                Log.w("The flag is 0","Myapp ");
                String[] splitBySingleHash = Comment.split("#");
                int k = 1;
                if(!(Comment.substring(0,1).equals("#"))) {
                    NewComment = splitBySingleHash[0];
                }
                else {
//                    k = 0;
                }
                for(;k<splitBySingleHash.length;k++) {
                    NewComment = NewComment + "<font color='red'>#" + splitBySingleHash[k].split(" ")[0] + "</font> ";
                    NewComment = NewComment + splitBySingleHash[k].substring(splitBySingleHash[k].split(" ")[0].length()+1);
                }
            }
            mTagAutoCompletionView.setText(Html.fromHtml(NewComment), TextView.BufferType.SPANNABLE);
            Log.w("" + mTagAutoCompletionView.getText() + " length is "+ mTagAutoCompletionView.getText().length() + " newcomment length  "+NewComment.length(), "Myapp ");
            mTagAutoCompletionView.setSelection(mTagAutoCompletionView.length());

        }
        else {
            once = 0;
        }

        CommentsLength = s.length();
//        Log.w("before isValidToken "+s.charAt(cursor),"Myapp ");
        if (cursor >= s.length()) cursor = s.length()-1;
        if (isValidToken(s, cursor)){
            Log.w("Valid","Myapp ");
            String token = getToken(s, start);
            new SuggestTagsAsyncTask().execute( token );
        }
        prevText = mTagAutoCompletionView.getText().toString();
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
        Log.w("isValidToken func and the whole text is "+(String) text.toString(),"Myapp ");
        Log.w("the letter at cursor is "+text.toString().charAt(cursor),"Myapp ");
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