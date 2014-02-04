package com.example.rottentomatoesdemo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RottenTomatoesClient {
	
    private static final String API_KEY = "2k5nfxdkffb5vtpnrabyh4af";
    private static final String API_BASE_URL = "http://api.rottentomatoes.com/api/public/v1.0/";
    private static final int httpTimeoutMilliSeconds = 300;
    
    private AsyncHttpClient client;

    public RottenTomatoesClient() {
        client = new AsyncHttpClient();
        client.setTimeout(httpTimeoutMilliSeconds);
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }
    
    // http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?apikey=<key>
    public void getBoxOfficeMovies(JsonHttpResponseHandler handler) {
        String url = getApiUrl("lists/movies/box_office.json");
        RequestParams params = new RequestParams("apikey", API_KEY);
        client.get(url, params, handler);
    }
}