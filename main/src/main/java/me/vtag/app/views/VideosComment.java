package me.vtag.app.views;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.Response;
import me.vtag.app.R;
import me.vtag.app.UsernameTokenizer;
import me.vtag.app.adapters.CommentsListAdapter;
import me.vtag.app.adapters.CommentsTagSuggestionAdapter;
import me.vtag.app.backend.VtagClient;
import me.vtag.app.backend.models.CommentModel;
import me.vtag.app.backend.models.CommentsDescribeModel;
import me.vtag.app.backend.models.GetgoofModel;
import me.vtag.app.helpers.VtagmeLoaderView;

/**
 * Created by anuraag on 20/6/14.
 */
public class VideosComment extends Fragment implements
        TokenCompleteTextView.TokenListener,
        TextWatcher{
    View mMainView;
    private int SuggestionTagSelected = 0;
    private String SelectedTag = "";
    ArrayList<CommentsDescribeModel> commentslist = new ArrayList<>();
    CommentsListAdapter mCommentsListAdapter;
    ListView mCommentsListView;
    public MultiAutoCompleteTextView ET;

    float px;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.video_comments_list, container, false);
//        final EditText ET = (EditText) mMainView.findViewById(R.id.comments_input);

        ET = (MultiAutoCompleteTextView) mMainView.findViewById(R.id.multiAutoCompleteTextView);
        ET.setTokenizer(new UsernameTokenizer());
        ET.addTextChangedListener(this);
        ET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SuggestionTagSelected = 1;
                SelectedTag = (String) adapterView.getItemAtPosition(i);
                int length = ET.getText().length();
                ET.setText(Html.fromHtml(ET.getText().subSequence(0, length - SelectedTag.length()-1) + "<font color='red'>" + SelectedTag + "</font>"), TextView.BufferType.SPANNABLE); //13 for the length of <mark></mark>
                ET.setSelection(length-1);

            }
        });

        Button Submit = (Button) mMainView.findViewById(R.id.comments_input_submit);
        Submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String text = ET.getText().toString();//ET.FullText;
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
//        private Activity context;
        public String data;
        public List<String> suggest;
        public ArrayAdapter<String> aAdapter;
/*
        public SuggestTagsAsyncTask(Activity cntxt) {
            context = cntxt;
        }
*/
        @Override protected String doInBackground(String... key) {
            String[] newText1 = key[0].split(" ");
            String newText = newText1[newText1.length-1];
            newText1 = newText.split("@");
            if(newText1.length > 1) {
                newText = newText1[newText1.length - 1];
            }
            try {
                HttpClient hClient = new DefaultHttpClient();
                HttpGet hGet = new HttpGet("http://192.168.1.4:8080/tagsearch/"+newText);

                ResponseHandler<String> rHandler = new BasicResponseHandler();
                data = hClient.execute(hGet, rHandler);
                suggest = new ArrayList<String>();
                Log.w(""+data.toString(),"Myapp ");
                String s2= data.toString().replaceAll("\"", "");
                s2=s2.replace(" ","");
                s2=s2.replace("[", "");
                s2=s2.replace("]", "");
                String[] tags = s2.split(",");

//                String[] tags = data.toString().split(" \" ");
                for (int i = 0;i<tags.length;i++) {
                        suggest.add(tags[i]+" ");
                }
            } catch (Exception e) {
                Log.w("Error", e.getMessage());
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
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
        String[] hashSplit = s.toString().split("@");
        String lasthash = hashSplit[hashSplit.length-1];
        String[] hashSplit1 = lasthash.split(" ");
        String lasthash1 = hashSplit1[hashSplit1.length-1];
        if(CommentsLength > s.length() && s.length() > 0) {
            if (SpacePressed == 0 && hashSplit.length > 1 && hashSplit1.length == 1) {
                ET.setText(s.subSequence(0, length - lasthash1.length()-1)); //13 for the length of <mark></mark>
                ET.setSelection(length-lasthash1.length()-1);
            }
        }
        if (s.toString().substring(length-1).equals(" ")){
            SpacePressed = 1;
        }
        else
        {
            SpacePressed = 0;
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
            if (text.charAt(i) == '@' && len>=2) return true;
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
        while (i > 0 && text.charAt(i - 1) != '@') { i--; }
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
