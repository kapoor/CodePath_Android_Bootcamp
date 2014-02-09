package com.example.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    
    // Progress bar manipulation
    private short completedHttpRequests = 0;

    
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
    	minId = -1;
    	loadDataFromAPI();
    }

    // Hide the progress bar if all HTTP requests have completed
    private void hideProgressBar() {
    	completedHttpRequests++;
    	if (completedHttpRequests == 0) {
    		setProgressBarIndeterminateVisibility(false);
    	}
    }
    
	public void fetchUserData() {
		
        // Show the locally stored user profile cached from the last request
        // and refresh it if the network request succeeds
		user = User.getOfflineUser();
		if (user != null) {
			setTitle("@" + user.getScreenName());
		}

        setProgressBarIndeterminateVisibility(true);
        completedHttpRequests--;

		MainApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonUser) {
				user = new User(jsonUser);
				setTitle("@" + user.getScreenName());
				hideProgressBar();
			}

			@Override
			public void onFailure(Throwable e, String message) {
                Toast.makeText(getApplicationContext(), getString(R.string.could_not_get_user_error), Toast.LENGTH_SHORT).show();
				hideProgressBar();
			}
		});
	}

	
    private void loadDataFromAPI() {

        // For first load, show the locally stored tweets cached from the last request
        // and refresh them if the network request succeeds
        if (minId == -1) {
	        tweets = Tweet.getOfflineTweets();
	        if (tweets != null && tweets.size() > 0) {
	        	tweetsAdapter.addAll(tweets);
	        }
        }

        setProgressBarIndeterminateVisibility(true);
        completedHttpRequests--;

		MainApp.getRestClient().getHomeTimeline(minId, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				
				tweets = Tweet.fromJson(jsonTweets);
				
				// If there are offline results already loaded for the first request,
				// then clear them first 
				if (minId == -1) {
					tweetsAdapter.clear();
				}
				
                // Update the the adapter
                tweetsAdapter.addAll(tweets);

                // Set minId = tweetId of the last tweet - 1 for the next batch of results,
            	// without the "- 1", the last tweet of this batch will be returned back as
                // the first tweet of the next batch
                minId = (tweets.get(tweets.size() - 1)).getTweetId() - 1;
                
				hideProgressBar();
			}
			
            @Override
            public void onFailure(Throwable e, String message) {

                Toast.makeText(getApplicationContext(), getString(R.string.could_not_get_tweets_error), Toast.LENGTH_SHORT).show();
				hideProgressBar();

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
		//postIntent.putExtra("user", user.getScreenName());
		//postIntent.putExtra("profileImageUrl", user.getProfileImageUrl());

		postIntent.putExtra("user", user);
		
		postIntent.setClass(getApplicationContext(), PostActivity.class);
		startActivityForResult(postIntent, REQUEST_CODE);
    }
}
