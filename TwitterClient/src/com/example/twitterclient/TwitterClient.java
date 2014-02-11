package com.example.twitterclient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.example.twitterclient.util.Constants;
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

    private static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    private static final String REST_URL = "https://api.twitter.com/1.1";
    private static final String REST_CONSUMER_KEY = "xhOfbD3N4qKtdh25jDY5g";
    private static final String REST_CONSUMER_SECRET = "Qp4OKbASd6WQI5FfPCIJMerKJC8RNFmyMCYmsessOV8";
    private static final String REST_CALLBACK_URL = "oauth://twitterclient";
    
    private static final int resultsCount = 20; 
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("count", Integer.toString(resultsCount));
    	if (maxId != -1) {
    		params.put("max_id", Long.toString(maxId));
    	}
    	
    	client.setTimeout(Constants.httpCallTimeoutMilliSeconds);
    	client.get(url, params, handler);
    }

    public void getMyInfo(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("account/verify_credentials.json");
        client.setTimeout(Constants.httpCallTimeoutMilliSeconds);
    	client.get(url, null, handler);
    }
    
    public void postTweet(String tweetStatus, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", tweetStatus);
        client.setTimeout(Constants.httpCallTimeoutMilliSeconds);
    	client.post(url, params, handler);
    }
    
    public void getMentionsTimeline(long maxId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("count", Integer.toString(resultsCount));
    	if (maxId != -1) {
    		params.put("max_id", Long.toString(maxId));
    	}
    	
    	client.setTimeout(Constants.httpCallTimeoutMilliSeconds);
    	client.get(url, params, handler);
    }

    public void getUserTimeline(long maxId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/user_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("count", Integer.toString(resultsCount));
    	if (maxId != -1) {
    		params.put("max_id", Long.toString(maxId));
    	}
    	
    	client.setTimeout(Constants.httpCallTimeoutMilliSeconds);
    	client.get(url, params, handler);
    }

}