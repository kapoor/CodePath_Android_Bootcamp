package com.example.twitterclient;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import eu.erikw.PullToRefreshListView;

public class ProfileActivity extends FragmentActivity {

	// Instance variables
    private User user;
    
    // Views
    TextView tvUser;
    TextView tvTagline;
    TextView tvFollowers;
    TextView tvFollowing;
    ImageView ivProfileImage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		setupViews();
		loadMyInfo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

    private void setupViews() {
        tvUser = (TextView) findViewById(R.id.tvName);
        tvTagline = (TextView) findViewById(R.id.tvTagline);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
    }
    
	
	public void loadMyInfo() {
		
        // Show the locally stored user profile cached from the last request
        // and refresh it if the network request succeeds
		user = User.getOfflineUser();
		if (user != null) {
			populateProfileHeader();
		}

		MainApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonUser) {
				user = new User(jsonUser);
				populateProfileHeader();
				//hideProgressBar();
			}

			@Override
			public void onFailure(Throwable e, String message) {
                Toast.makeText(ProfileActivity.this, getString(R.string.could_not_get_user_error), Toast.LENGTH_SHORT).show();
				//hideProgressBar();
			}
		});
	}
	
	private void populateProfileHeader() {
		getActionBar().setTitle("@" + user.getScreenName());
		tvUser.setText(user.getName());
		tvTagline.setText(user.getTagline());
		tvFollowers.setText(user.getFollowersCount() + " Followers");
		tvFollowing.setText(user.getFriendsCount() + " Following");
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
	}

}
