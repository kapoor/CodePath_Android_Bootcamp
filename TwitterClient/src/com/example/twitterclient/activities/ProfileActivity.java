package com.example.twitterclient.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitterclient.MainApp;
import com.example.twitterclient.R;
import com.example.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends Activity {
	
	// Instance variables
    private short remainingHttpRequests = 0;

    // Instance variables
	private User user;

    // Views
	private View fragmentProfileView;
	private TextView tvUser;
	private TextView tvTagline;
	private TextView tvFollowers;
	private TextView tvFollowing;
	private ImageView ivProfileImage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// Pass the user object to the underlying fragments 
		User user = (User) getIntent().getSerializableExtra(TimelineActivity.userExtra);		

		setupViews();
		
		//set the title as the screen name
		getActionBar().setTitle(Html.fromHtml(" <small><font color='#777777'>@" +
			user.getScreenName() + "</font></small>"));
	}
	
    private void setupViews() {
        tvUser = (TextView) fragmentProfileView.findViewById(R.id.tvName);
        tvTagline = (TextView) fragmentProfileView.findViewById(R.id.tvTagline);
        tvFollowers = (TextView) fragmentProfileView.findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) fragmentProfileView.findViewById(R.id.tvFollowing);
        ivProfileImage = (ImageView) fragmentProfileView.findViewById(R.id.ivProfileImage);
    }
    
	public void loadMyInfo() {
		
        // Show the locally stored user profile cached from the last request
        // and refresh it if the network request succeeds
		user = User.getOfflineUser();
		if (user != null) {
			populateProfileHeader();
		}

		showProgressBar();
		
		MainApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonUser) {
				user = new User(jsonUser);
				populateProfileHeader();
				hideProgressBar();
			}

			@Override
			public void onFailure(Throwable e, String message) {
                Toast.makeText(ProfileActivity.this, getString(R.string.could_not_get_user_error), Toast.LENGTH_SHORT).show();
                hideProgressBar();
			}
		});
	}
	
	private void populateProfileHeader() {
		tvUser.setText(user.getName());
		tvTagline.setText(user.getTagline());
		tvFollowers.setText(user.getFollowersCount() + " Followers");
		tvFollowing.setText(user.getFriendsCount() + " Following");
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	
    // TODO: Refactor this out in a base class since all fragments do this
    // NOTE: Progress bar is owned by the activity and can only be hidden by it
    public void showProgressBar() {
    	remainingHttpRequests++;
    	setProgressBarIndeterminateVisibility(true);
    }

    public void hideProgressBar() {
    	if (--remainingHttpRequests == 0) {
    		setProgressBarIndeterminateVisibility(false);
    	}
    }

   
}
