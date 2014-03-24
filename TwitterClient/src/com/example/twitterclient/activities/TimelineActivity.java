package com.example.twitterclient.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.twitterclient.MainApp;
import com.example.twitterclient.R;
import com.example.twitterclient.fragments.TimelineFragment;
import com.example.twitterclient.models.User;
import com.example.twitterclient.util.Constants;
import com.example.twitterclient.util.BaseFragmentInterface;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements TabListener, BaseFragmentInterface {

	// Instance variables
	private static int REQUEST_CODE = 1;
    private User user;
    private short remainingHttpRequests = 0;
	public static final String userExtra = "user";
	public static final String userIdExtra = "userId";
	public static final String screenNameExtra = "screenName";

    // Fragments
	private FragmentManager fragmentManager;
	private android.support.v4.app.FragmentTransaction fts;
	private TimelineFragment currentFragment;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_timeline);
		
		// TODO: Refactor all of these methods out into a base class
        setupViews();
        setupFragments();
        setupAdapters();
        setupListeners();
        
        fetchUserData();
        
        setupNavigationTabs();
	}
	
    
    private void setupViews() {
    }
    
    private void setupFragments() {
    	fragmentManager = getSupportFragmentManager();
    }
    
    private void setupNavigationTabs() {
    	ActionBar actionBar = getActionBar();
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	actionBar.setDisplayShowTitleEnabled(true);
    	
    	// TODO: Since the fragments in these tabs might be used elsewhere, you 
    	// should not instantiate tabs with new fragments each time you switch
    	// to this activity
    	// Use SmartFragmentStatePagerAdapter instead to cache the fragments like this:
    	//		https://github.com/thecodepath/android_guides/wiki/ViewPager-with-FragmentPagerAdapter
    	Tab tabHome = actionBar.newTab()
    			.setText(getString(R.string.home))
    			.setTag("HomeTimelineFragment")
    			.setIcon(R.drawable.ic_tab_home)
    			.setTabListener(this);
    	// TODO: Fix this, see TODO above
    	//		.setTabListener(new SupportFragmentTabListener<TimelineFragment>(R.id.frame_container_timeline, this,
    	//				getString(R.string.home), TimelineFragment.class));
    	
    	Tab tabMentions = actionBar.newTab()
    			.setText(getString(R.string.mentions))
    			.setTag("MentionsTimelineFragment")
    			.setIcon(R.drawable.ic_tab_ampersand)
    			.setTabListener(this);
    	// TODO: Fix this, see TODO above
    	//		.setTabListener(new SupportFragmentTabListener<TimelineFragment>(R.id.frame_container_timeline, this,
    	//				getString(R.string.home), TimelineFragment.class));
    	

    	actionBar.addTab(tabHome);
    	actionBar.addTab(tabMentions);
    	actionBar.selectTab(tabHome);
    }
    
    private void setupAdapters() {
    }
    
    private void setupListeners() {
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
   
    public void fetchUserData() {

        // Show the locally stored user profile cached from the last request
        // and refresh it if the network request succeeds
		user = User.getOfflineUser();
		if (user != null) {
			setTitle("@" + user.getScreenName());
		}

        setProgressBarIndeterminateVisibility(true);

		MainApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonUser) {
				user = new User(jsonUser);
				setTitle("@" + user.getScreenName());
				hideProgressBar();
			}

			@Override
			public void onFailure(Throwable e, String message) {
                Toast.makeText(getApplicationContext(), getString(R.string.could_not_get_user_error), Toast.LENGTH_SHORT).show();
				hideProgressBar();
			}
		});
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Get the results from preferences activity
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
        	
        	// Call the fragment's method to reload tweets
        	currentFragment.reloadTweets();
        }
    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	    
    public void onPostAction(MenuItem mi) {
		Intent postIntent = new Intent();
		postIntent.putExtra(userExtra, user);
		postIntent.setClass(getApplicationContext(), PostActivity.class);
		startActivityForResult(postIntent, REQUEST_CODE);
    }

    public void onProfileViewAction(MenuItem mi) {
		Intent profileIntent = new Intent(this, ProfileActivity.class);
		profileIntent.putExtra(userExtra, user);
		startActivity(profileIntent);
    }


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		fts = fragmentManager.beginTransaction();
		
		if(tab.getTag() == "HomeTimelineFragment") {
			currentFragment = TimelineFragment.newInstance(Constants.FragmentType.HOME);
			fts.replace(R.id.frame_container_timeline, currentFragment);
		}
		else {
			currentFragment = TimelineFragment.newInstance(Constants.FragmentType.MENTIONS);
			fts.replace(R.id.frame_container_timeline, currentFragment);
		}
		
		fts.commit();
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
}
