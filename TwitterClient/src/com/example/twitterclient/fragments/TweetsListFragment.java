package com.example.twitterclient.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.twitterclient.MainApp;
import com.example.twitterclient.R;
import com.example.twitterclient.TweetsAdapter;
import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.util.Constants;
import com.example.twitterclient.util.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

/*
public abstract class TweetsListFragment extends Fragment {
*/
public class TweetsListFragment extends Fragment {
	
    // Views
    private PullToRefreshListView lvTweets;
    
    // Adapters
	TweetsAdapter tweetsAdapter;

	// Instance variables
    private long minId = -1;
    private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    private short completedHttpRequests = 0;
    private Constants.FragmentType ft = Constants.FragmentType.HOME;  // Default

	private static String fragmentTypeCodeExtra = "fragmentTypeCode";

    public void setFragmentType(Constants.FragmentType inFT) {
    	ft = inFT;
    }

    public Constants.FragmentType getFragmentType() {
    	return ft;
    }
    
	public TweetsAdapter getAdapter() {
		return tweetsAdapter;
	}

	/* NEW CODE */
	public static TweetsListFragment newInstance(Constants.FragmentType inFT) {
		TweetsListFragment tlf = new TweetsListFragment();
		Bundle args = new Bundle();
		args.putSerializable(fragmentTypeCodeExtra, inFT);
		tlf.setArguments(args);
		return tlf;
	}
	 
    private void setupViews(View v) {
    	lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
    }
    
    private void setupAdapters() {
    	// NOTE: Since the fragment is retained and reused a lot more than it's enclosing
    	// activity, calling getActivity() may can cause a memory leak because the
    	// adapter will hold a reference to the calling activity each time it's instantiated.
    	// TODO: Is there another right way to do this?
		tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets.setAdapter(tweetsAdapter);
    }
    
    private void setupListeners() {
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

    // Hide the progress bar if all HTTP requests have completed
    private void hideProgressBar() {
    	completedHttpRequests++;
    	if (completedHttpRequests == 0) {
    		getActivity().setProgressBarIndeterminateVisibility(false);
    	}
    }
    
    public void reloadTweets() {
    	// Reset the parameters to their initial state
    	minId = -1;
    	loadDataFromAPI();
    }

    private void loadDataFromAPI() {

        // For first load, show the locally stored tweets cached from the last request
        // and refresh them if the network request succeeds
        if (minId == -1) {
	        tweets = Tweet.getOfflineTweets();
	        if (tweets != null && tweets.size() > 0) {
	        	getAdapter().addAll(tweets);
	        }
        }

        getActivity().setProgressBarIndeterminateVisibility(true);

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				
				tweets = Tweet.fromJson(jsonTweets);
				
				// If there are offline results already loaded for the first request,
				// then clear them first 
				if (minId == -1) {
					getAdapter().clear();
				}
				
                // Update the the adapter
				getAdapter().addAll(tweets);

                // Set minId = tweetId of the last tweet - 1 for the next batch of results,
            	// without the "- 1", the last tweet of this batch will be returned back as
                // the first tweet of the next batch
				if (tweets.size() > 0) { 
					minId = (tweets.get(tweets.size() - 1)).getTweetId() - 1;
				}

				hideProgressBar();
			}
			
            @Override
            public void onFailure(Throwable e, String message) {
            	// NOTE: Since the fragment is retained and reused a lot more than it's enclosing
            	// activity, calling getActivity() may can cause a memory leak because the
            	// adapter will hold a reference to the calling activity each time it's instantiated.
            	// TODO: Is there another right way to do this?
                Toast.makeText(getActivity(), getString(R.string.could_not_get_tweets_error), Toast.LENGTH_SHORT).show();
				hideProgressBar();

            }
        };
		
        // Call the appropriate HTTP method
        switch(getFragmentType()) {
        case HOME:
        	completedHttpRequests--;
        	MainApp.getRestClient().getHomeTimeline(minId, responseHandler);
        	break;
        case MENTIONS:
        	completedHttpRequests--;
        	MainApp.getRestClient().getMentionsTimeline(minId, responseHandler);
        	break;
        case USER:
        	completedHttpRequests--;
        	MainApp.getRestClient().getUserTimeline(minId, responseHandler);
        	break;
        }
	}

    //NOTE: Use this for non-view items
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ft = (Constants.FragmentType) getArguments().getSerializable(fragmentTypeCodeExtra);
	}
	
	//NOTE: Use this for view related items
    @Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {

    	View v = inf.inflate(R.layout.fragment_tweets_list, parent, false);

		setupViews(v);
		setupAdapters();
		setupListeners();

		return v;
	}

}
