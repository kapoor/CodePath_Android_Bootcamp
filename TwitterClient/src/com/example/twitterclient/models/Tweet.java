package com.example.twitterclient.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.twitterclient.util.Constants;

@Table(name = "Tweets")
public class Tweet extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 6875000113655618590L;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
	
	
	// Define database columns and associated fields for ActiveAndroid
	@Column(name = "tweetId", index = true)
	private long tweetId;

	@Column(name = "user")
	private User user;

	@Column(name = "timestamp")
	private String timestamp;

	@Column(name = "body")
	private String body;
	
	@Column(name = "isFavorited")
	private boolean isFavorited;
	
	@Column(name = "isRetweeted")
	private boolean isRetweeted;


	// NOTE: This default no-argument constructor is needed for ActiveAndroid to work well 
	public Tweet() {
		super();
	}
	
    public Tweet(JSONObject jsonObj) {
        try {
            jsonObject = jsonObj;
            user = new User(jsonObj.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Set the instance variables
		tweetId = getLong("id");
		timestamp = getString("created_at");
		body = getString("text");
		isFavorited = getBoolean("favorited");
		isRetweeted = getBoolean("retweeted");
		
		// Persist this object using ActiveAndroid
		save();
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getTweetId() {
        return tweetId;
    }

	public String getRelativeDateTimeString(Context context) {
		Date timestamp = getTimestampAsDate();
		
		if (timestamp == null) return null;
		
		return DateUtils.getRelativeDateTimeString(
	        context,
	        timestamp.getTime(), // The time to display
	        DateUtils.SECOND_IN_MILLIS, // The resolution. This will display only minutes (no "3 seconds ago") 
	        DateUtils.WEEK_IN_MILLIS, 	// The maximum resolution at which the time will switch to default date
	        							// instead of spans. This will not display "3 weeks ago" but a full date instead
	        0).toString();
	}

	public String getRelativeTimeSpanString() {
		Date timestamp = getTimestampAsDate();
		
		if (timestamp == null) { return null; }
		
		return DateUtils.getRelativeTimeSpanString(timestamp.getTime()).toString();
	}
	
	@SuppressLint("SimpleDateFormat")
	private Date getTimestampAsDate() {
		sdf.setLenient(true);
		try {
			return sdf.parse(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
    public boolean isFavorited() {
        return isFavorited;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        // Bulk load the tweets into the DB using transactions
        // This speeds up writes by 100x
        ActiveAndroid.beginTransaction();
        try {
	        for (int i=0; i < jsonArray.length(); i++) {
	            JSONObject tweetJson = null;
	            try {
	                tweetJson = jsonArray.getJSONObject(i);
	            } catch (Exception e) {
	                e.printStackTrace();
	                continue;
	            }
	
	            Tweet tweet = new Tweet(tweetJson);
	            if (tweet != null) {
	            	tweet.save();
	                tweets.add(tweet);
	            }
	        }
        	ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        return tweets;
    }
    
    public static ArrayList<Tweet> getOfflineTweets() {

    	// Examples:
    	// return new Select().from(SampleModel.class).where("id = ?", id).executeSingle();
    	// return new Select().from(SampleModel.class).orderBy("id DESC").limit("300").execute();
       
    	// Show only top 20 recent tweets
    	List<Tweet> tweets = new Select().from(Tweet.class).limit(Constants.tweetsQueryLimit).execute();
    	return new ArrayList<Tweet>(tweets);
    }

}