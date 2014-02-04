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
import android.view.Window;
import android.widget.Toast;

import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.models.User;
import com.example.twitterclient.util.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {

	// Constants
	public static final String LOG_TAG = TimelineActivity.class.getName();
	private static int REQUEST_CODE = 1;

    // Views
    private PullToRefreshListView lvTweets;
    
    // Adapters
    private TweetsAdapter tweetsAdapter;
    private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    
    // Instance variables
    private User user;
    private long minId = -1;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_timeline);
		
        setupViews();
        setupAdapters();

        fetchUserData();
        
        addListeners();
	}
	
    
    private void setupViews() {
    	lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
    }
    
    private void setupAdapters() {
		tweetsAdapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(tweetsAdapter);
    }
    
    private void addListeners() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadDataFromAPI();
            }
        });
        
        // Set a listener to be invoked when the list is pulled down to be refreshed
        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the list contents
            	reloadTweets();

            	// NOTE: Always make sure to always call listView.onRefreshComplete() when loading is done
            	lvTweets.onRefreshComplete();
            }
        });
    }

    public void reloadTweets() {
    	// Reset the parameters to their initial state
        tweetsAdapter.clear();
    	minId = -1;
    	loadDataFromAPI();
    }
    
	public void fetchUserData() {
		
        setProgressBarIndeterminateVisibility(true);

		MainApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonUser) {
				try {
					setTitle("@" + jsonUser.getString("screen_name"));
					user = new User(jsonUser);
					
			        setProgressBarIndeterminateVisibility(false);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable e, String message) {
                Toast.makeText(getApplicationContext(), getString(R.string.could_not_get_user_error), Toast.LENGTH_SHORT).show();

                // Fetch locally stored user record
				user = User.getOfflineUser();
				setTitle("@" + user.getScreenName());

		        setProgressBarIndeterminateVisibility(false);
			}
		});
	}

	
    private void loadDataFromAPI() {
        setProgressBarIndeterminateVisibility(true);

		MainApp.getRestClient().getHomeTimeline(minId, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				
				tweets = Tweet.fromJson(jsonTweets);
				
                // Update the the adapter
                tweetsAdapter.addAll(tweets);

                // Set minId = tweetId of the last tweet - 1 for the next batch of results,
            	// without the "- 1", the last tweet of this batch will be returned back as
                // the first tweet of the next batch
                minId = (tweets.get(tweets.size() - 1)).getTweetId() - 1;
                
		        setProgressBarIndeterminateVisibility(false);
			}
			
            @Override
            public void onFailure(Throwable e, String message) {

                Toast.makeText(getApplicationContext(), getString(R.string.could_not_get_tweets_error), Toast.LENGTH_SHORT).show();
                
                // Fetch locally stored tweets
                tweets = Tweet.getOfflineTweets();
                
                // Update the the adapter
                tweetsAdapter.addAll(tweets);
                
		        setProgressBarIndeterminateVisibility(false);
            }
		});
	}

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Get the results from preferences activity
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){      
            reloadTweets();
        }
    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	
    public void onRefreshAction(MenuItem mi) {
    	reloadTweets();
    }
    
    
    public void onPostAction(MenuItem mi) {
		Intent postIntent = new Intent();
		postIntent.putExtra("screenName", user.getScreenName());
		postIntent.putExtra("profileImageUrl", user.getProfileImageUrl());

		postIntent.setClass(getApplicationContext(), PostActivity.class);
		startActivityForResult(postIntent, REQUEST_CODE);
    }
}
