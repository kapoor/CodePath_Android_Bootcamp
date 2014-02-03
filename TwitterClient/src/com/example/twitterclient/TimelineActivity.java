package com.example.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.models.User;
import com.example.twitterclient.util.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	// Constants
	public static final String LOG_TAG = TimelineActivity.class.getName();
	private static int REQUEST_CODE = 1;


    // Views
    private ListView lvTweets;
    
    // Adapters
    private TweetsAdapter tweetsAdapter;
    private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    
    // Instance variables
    private User user;
    private long maxId = -1;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
        setupViews();
        setupAdapters();
        addListeners();
        
        getUserData();
        
        runActivity();
	}
	
    
    private void setupViews() {
    	lvTweets = (ListView) findViewById(R.id.lvTweets);
    }
    
    private void setupAdapters() {
		tweetsAdapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(tweetsAdapter);
    }
    
    private void addListeners() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi();
            }
        });
    }

    public void runActivity() {
        tweetsAdapter.clear();
    	customLoadMoreDataFromApi();
    }
    
	public void getUserData() {
		MainApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonUser) {
				try {
					setTitle("@" + jsonUser.getString("screen_name"));
					user = User.fromJson(jsonUser);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String message) {
				Log.d("DEBUG", "Failed to fetch user settings:"
						+ message);
			}
		});
	}

    private void customLoadMoreDataFromApi() {
		MainApp.getRestClient().getHomeTimeline(maxId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				
				if (maxId != -1) {
					// Remove the repeated tweet for maxId which will be returned again
					jsonTweets.remove(0);
				}
				
				tweets = Tweet.fromJson(jsonTweets);
				
                // Update the the adapter
                tweetsAdapter.addAll(tweets);

                // Get the Id of the last tweet and set it as maxId for the next batch of results
                maxId = (tweets.get(tweets.size() - 1)).getId();
			}
			
            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(getApplicationContext(), getString(R.string.could_not_get_tweets), Toast.LENGTH_SHORT).show();
            }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

    
    public void onPostAction(MenuItem mi) {
		Intent postIntent = new Intent();
		postIntent.putExtra("screenName", user.getScreenName());
		postIntent.putExtra("profileImageUrl", user.getProfileImageUrl());

		postIntent.setClass(getApplicationContext(), PostActivity.class);
		startActivityForResult(postIntent, REQUEST_CODE);
    }
}
