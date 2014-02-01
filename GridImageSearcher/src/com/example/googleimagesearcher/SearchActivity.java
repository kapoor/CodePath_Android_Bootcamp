package com.example.googleimagesearcher;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.googleimagesearcher.util.EndlessScrollListener;
import com.example.googleimagesearcher.util.ImageResult;
import com.example.googleimagesearcher.util.ImageResultsArrayAdapter;
import com.example.googleimagesearcher.util.Preferences;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
    
    private static int REQUEST_CODE = 1;
    private static String LOG_TAG = SearchActivity.class.getName();

    private EditText etQuery;
    private Button btnSearch;
    private GridView gvResults;
    
    private String query;
    private Preferences preferences;
    
    private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
    private ImageResultsArrayAdapter imageAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        setupViews();

        preferences = new Preferences(
            getResources().getStringArray(R.array.image_sizes)[0],
            getResources().getStringArray(R.array.image_colors)[0],
            getResources().getStringArray(R.array.image_types)[0],
            ""
        );
        
        btnSearch.setEnabled(false);
        imageAdapter = new ImageResultsArrayAdapter(this, imageResults);
        gvResults.setAdapter(imageAdapter);
        
        addListeners();
    }

    private void addListeners() {

        etQuery.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etQuery.getText().toString().trim().isEmpty()) {
                    btnSearch.setEnabled(false);
                } else {
                    btnSearch.setEnabled(true);
                }
            }
        });

        gvResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
                Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                
                ImageResult imageResult = imageResults.get(position);
                i.putExtra("result", imageResult);
                startActivity(i);
            }
        });
        
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }
    
    private void customLoadMoreDataFromApi(int offset) {

        AsyncHttpClient client = new AsyncHttpClient();
        
        // Example query: https://ajax.googleapis.com/ajax/services/search/images?v=1.0&&as_sitesearch=google.com&imgsz=icon&imgtype=clipart&imgcolor=blue&q=android&rsz=8&start=0
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&" +
                "&as_sitesearch=" + Uri.encode(preferences.searchSite) +
                "&imgsz=" + Uri.encode(preferences.imageSize) +
                "&imgtype=" + Uri.encode(preferences.imageType) +
                "&imgcolor=" + Uri.encode(preferences.imageColor) +
                "&start=" + offset + "&rsz=8" + "&q=" + Uri.encode(query);
        
        Log.d(LOG_TAG, "Search url = " + url);
        
        client.get(url, new JsonHttpResponseHandler() {
            
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray imageJsonResults = null;
                try {
                    imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");

                    // Update the the adapter
                    // Method 1:
                    // imageResults.addAll(ImageResult.fromJSONArray(imageJsonResults));
                    // imageAdapter.notify();
                    
                    // Method 2: Update the adapter directly.
                    // This will update the adapter and mutate the backing array too
                    imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(getApplicationContext(), getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Get the results from preferences activity
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){      
            preferences = (Preferences) data.getSerializableExtra(Preferences.PREFERENCES_BUNDLE_PARAM);
        }
    }
    
    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        gvResults = (GridView) findViewById(R.id.gvResults);
    }
    
    public void onPreferencesAction(MenuItem mi) {
         Intent i = new Intent(this, PreferencesActivity.class);
         startActivityForResult(i, REQUEST_CODE);
         overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
    
    public void onImageSearch(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        // Ensure that the query string isn't empty
        query = etQuery.getText().toString();
        if(query == null || query.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_query), Toast.LENGTH_SHORT).show();
            return;
        }
        
        imageAdapter.clear();
        customLoadMoreDataFromApi(0);
    }
}
