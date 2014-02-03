package com.example.twitterclient.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;

public class Tweet extends BaseModel implements Serializable {
	private static final long serialVersionUID = 6875000113655618590L;
	private User user;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	

    public User getUser() {
        return user;
    }

    public String getBody() {
        return getString("text");
    }

    public long getId() {
        return getLong("id");
    }

	public String getTimestamp() {
		Date timestamp = getTimestampAsDate();
		
		if (timestamp == null) return null;
		
		return getTimestampAsDate().toString();
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
		
		if (timestamp == null) return null;
		
		return DateUtils.getRelativeTimeSpanString(timestamp.getTime()).toString();
	}
	
	@SuppressLint("SimpleDateFormat")
	private Date getTimestampAsDate() {
		sdf.setLenient(true);
		try {
			return sdf.parse(getString("created_at"));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
    public boolean isFavorited() {
        return getBoolean("favorited");
    }

    public boolean isRetweeted() {
        return getBoolean("retweeted");
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.jsonObject = jsonObject;
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }

}