package com.example.twitterclient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final String LOG_TAG = TwitterClient.class.getName();

    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "xhOfbD3N4qKtdh25jDY5g";       // Change this
    public static final String REST_CONSUMER_SECRET = "Qp4OKbASd6WQI5FfPCIJMerKJC8RNFmyMCYmsessOV8"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://twitterclient"; // Change this (here and in manifest)
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
    
    public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
    	
    	String url = getApiUrl("statuses/home_timeline.json" + (maxId == -1 ? "" : "?count=21&max_id=" + maxId));
    	client.get(url, null, handler);

    	/*
    		// NOTE: Query string params can be also specified using ResponseParams as:
    	    // Adding params via RequestParams causes Async-Http to throw this exception:
    	    	Authentication error: Unable to respond to any of these challenges: {}

	    	String url = getApiUrl("statuses/home_timeline.json");
	    	RequestParams params = new RequestParams();
	    	if (maxId != -1) {
	        	// Get 21 tweets the second time because the tweet with maxId will be returned back
	    		params.put("count", "21");
	    		params.put("max_id", String.valueOf(maxId));
	    	}
	    	
			client.get(url, params, handler);
    	 */

    	Log.d(LOG_TAG, "URL is = " + url);
    }

    public void getMyInfo(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("account/verify_credentials.json");
    	client.get(url, null, handler);
    }
    
    public void postTweet(String tweetStatus, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", tweetStatus);
    	client.post(url, params, handler);
    }

}