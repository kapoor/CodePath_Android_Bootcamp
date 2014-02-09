package com.example.twitterclient.models;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Users")
public class User extends Model implements Serializable {
	private static final long serialVersionUID = -3527299894278558921L;

	// Define database columns and associated fields for ActiveAndroid
	@Column(name = "userId", index = true)
	private long userId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "screenName")
	private String screenName;
	
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	
	@Column(name = "profileBackgroundImageUrl")
	private String profileBackgroundImageUrl;
	
	@Column(name = "numTweets")
	private int numTweets;
	
	@Column(name = "followersCount")
	private int followersCount;
	
	@Column(name = "friendsCount")
	private int friendsCount;
	
	@Column(name = "tagline")
	private String tagline;

	
	// NOTE: This default no-argument constructor is needed for ActiveAndroid to work 
	public User() {
		super();
	}
	
    public User(JSONObject jsonObject) {

        try {
        	// Set the instance variables
    		name = jsonObject.getString("name");
    		userId = jsonObject.getLong("id");
    		screenName = jsonObject.getString("screen_name");
    		profileImageUrl = jsonObject.getString("profile_image_url");
    		profileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
    		numTweets = jsonObject.getInt("statuses_count");
    		followersCount = jsonObject.getInt("followers_count");
    		friendsCount = jsonObject.getInt("friends_count");
    		tagline = jsonObject.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
		
		// Persist this object using ActiveAndroid
		save();
    }
	
	public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public int getNumTweets() {
        return numTweets;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }
    
    public String getTagline() {
    	return tagline;
    }
    
    public List<Tweet> tweets() {
        return getMany(Tweet.class, "Category");
    }
    
    public static User getOfflineUser() {

    	// Examples:
    	// return new Select().from(SampleModel.class).where("id = ?", id).executeSingle();
    	// return new Select().from(SampleModel.class).orderBy("id DESC").limit("300").execute();

    	return new Select()
        .from(User.class)
        .executeSingle();
    }
    
}