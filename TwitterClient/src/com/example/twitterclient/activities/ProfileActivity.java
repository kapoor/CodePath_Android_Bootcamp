package com.example.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.Menu;

import com.example.twitterclient.R;
import com.example.twitterclient.fragments.ProfileFragment;
import com.example.twitterclient.models.User;
import com.example.twitterclient.util.BaseFragmentInterface;

public class ProfileActivity extends FragmentActivity implements BaseFragmentInterface {
	
	// Instance variables
	private FragmentManager fragmentManager;
	private android.support.v4.app.FragmentTransaction fts;
    private short remainingHttpRequests = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// Pass the user object to the underlying fragments 
		User user = (User) getIntent().getSerializableExtra(TimelineActivity.userExtra);		

		fragmentManager = getSupportFragmentManager();
		fts = fragmentManager.beginTransaction();		
		ProfileFragment profileFragment = ProfileFragment.newInstance(user);
		fts.replace(R.id.frame_container_profile, profileFragment);
		fts.commit();

		//set the title as the screen name
		getActionBar().setTitle(Html.fromHtml(" <small><font color='#777777'>@" +
			user.getScreenName() + "</font></small>"));
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
