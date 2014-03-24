package com.example.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitterclient.R;
import com.example.twitterclient.fragments.TimelineFragment;
import com.example.twitterclient.models.User;
import com.example.twitterclient.util.BaseFragmentInterface;
import com.example.twitterclient.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity implements BaseFragmentInterface {
	
	// Instance variables
    private short remainingHttpRequests = 0;
	private User user;

    // Views
	private TextView tvUser;
	private TextView tvTagline;
	private TextView tvFollowers;
	private TextView tvFollowing;
	private ImageView ivProfileImage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		setupViews();
		
		user = (User) getIntent().getSerializableExtra(TimelineActivity.userExtra);
		
		// Set the ActionBar title
		getActionBar().setTitle(Html.fromHtml(" <small><font color='#777777'>@" + user.getScreenName() + "</font></small>"));
		setProfileDetails();
		
		// Load the fragment
		FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
		TimelineFragment timelineFragment = TimelineFragment.newUserInstance(Constants.FragmentType.USER, user.getUserId());
		fts.replace(R.id.frame_container_profile_timeline, timelineFragment);
		fts.commit();
	}
	
    private void setupViews() {
        tvUser = (TextView) findViewById(R.id.tvName);
        tvTagline = (TextView) findViewById(R.id.tvTagline);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
    }
    
	private void setProfileDetails() {
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
	@Override
    public void showProgressBar() {
    	remainingHttpRequests++;
    	setProgressBarIndeterminateVisibility(true);
    }
	
	@Override
    public void hideProgressBar() {
    	if (--remainingHttpRequests == 0) {
    		setProgressBarIndeterminateVisibility(false);
    	}
    }

   
}
