package com.example.twitterclient.models;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Users")
public class User extends BaseModel implements Serializable {
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

	
	// NOTE: This default no-argument constructor is needed for ActiveAndroid to work well 
	public User() {
		super();
	}
	
    public User(JSONObject jsonObj) {
        try {
            jsonObject = jsonObj;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        // Set the instance variables
		name = getString("name");
		userId = getLong("id");
		screenName = getString("screen_name");
		profileImageUrl = getString("profile_image_url");
		profileBackgroundImageUrl = getString("profile_background_image_url");
		numTweets = getInt("statuses_count");
		followersCount = getInt("followers_count");
		friendsCount = getInt("friends_count");
		tagline = getString("description");
		
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