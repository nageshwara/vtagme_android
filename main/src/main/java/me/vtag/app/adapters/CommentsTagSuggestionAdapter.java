package me.vtag.app.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

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

import me.vtag.app.backend.VtagClient;
import me.vtag.app.pages.HashtagPageFragment;
import me.vtag.app.views.VideosComment;

/**
 * Created by anuraag on 24/6/14.
 */
public class CommentsTagSuggestionAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> resultList;
    private static final String LOG_TAG = "TagAutoCompleteAdapter";
    private VideosComment mHashtagPageFragment;

    public CommentsTagSuggestionAdapter(Context context, int textViewResourceId, VideosComment mHashtagPageFragment) {
        super(context, textViewResourceId);
        this.mHashtagPageFragment = mHashtagPageFragment;
    }

    public CommentsTagSuggestionAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                String[] list = constraint.toString().split(" ");
                String tag = list[list.length-1];
                Log.w("The tag is "+tag.substring(1)+" "+Boolean.toString(tag.startsWith("#")),"Myapp ");
                if (constraint != null && constraint.length() > 2 && tag.startsWith("#")==true) {
                    Log.w("Rottai "+constraint.toString(),"Myapp ");
                    // Retrieve the autocomplete results.
                    Log.w("Sottai "+mHashtagPageFragment.ET.getText(),"Myapp ");
                    resultList = autocomplete(tag.substring(1));

                    int length = constraint.length();
                    // Assign the data to the FilterResults
//                    mHashtagPageFragment.ET.FullText = constraint.toString().substring(0,length-tag.length());
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                    if (filterResults.count == 0) {
                        mHashtagPageFragment.onTokenAdded(null);
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url = new URL(VtagClient.getUrl("/tagsearch/" + URLEncoder.encode(input, "utf8")));
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONArray predsJsonArray = new JSONArray(jsonResults.toString());

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getString(i));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
}
