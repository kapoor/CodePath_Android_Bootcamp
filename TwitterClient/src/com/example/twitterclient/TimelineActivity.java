package com.example.twitterclient;

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

import com.example.twitterclient.fragments.HomeTimelineFragment;
import com.example.twitterclient.fragments.MentionsTimelineFragment;
import com.example.twitterclient.fragments.TweetsListFragment;
import com.example.twitterclient.util.FragmentTabListener;

public class TimelineActivity extends FragmentActivity implements TabListener {

	// Constants
	public static final String LOG_TAG = TimelineActivity.class.getName();
	private static int REQUEST_CODE = 1;

    // Fragments
	FragmentManager fragmentManager;
	android.support.v4.app.FragmentTransaction fts;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_timeline);
		
        setupViews();
        setupFragments();
        setupAdapters();
        setupListeners();
        
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
    			.setTabListener(new FragmentTabListener<HomeTimelineFragment>(R.id.frame_container, this,
    					getString(R.string.home), HomeTimelineFragment.class));
    	
    	Tab tabMentions = actionBar.newTab()
    			.setText(getString(R.string.mentions))
    			.setTag("MentionsTimelineFragment")
    			.setIcon(R.drawable.ic_tab_ampersand)
    			.setTabListener(new FragmentTabListener<MentionsTimelineFragment>(R.id.frame_container, this,
    					getString(R.string.mentions), MentionsTimelineFragment.class));
    	
    	actionBar.addTab(tabHome);
    	actionBar.addTab(tabMentions);
    	actionBar.selectTab(tabHome);
    }
    
    private void setupAdapters() {
    }
    
    private void setupListeners() {
    }

    /*
    // Hide the progress bar if all HTTP requests have completed
    private void hideProgressBar() {
		setProgressBarIndeterminateVisibility(false);
    }
    */
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Get the results from preferences activity
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){      
            //reloadTweets();
        }
    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	    
    public void onPostAction(MenuItem mi) {
    	/*
		Intent postIntent = new Intent();
		//postIntent.putExtra("user", user.getScreenName());
		//postIntent.putExtra("profileImageUrl", user.getProfileImageUrl());

		postIntent.putExtra("user", user);
		
		postIntent.setClass(getApplicationContext(), PostActivity.class);
		startActivityForResult(postIntent, REQUEST_CODE);
		*/
    }

    public void onProfileViewAction(MenuItem mi) {
		Intent profileIntent = new Intent(this, ProfileActivity.class);
		startActivity(profileIntent);
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
		fts = fragmentManager.beginTransaction();
		
		if(tab.getTag() == "HomeTimelineFragment") {
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
		}
		else {
			fts.replace(R.id.frame_container, new MentionsTimelineFragment());
		}
		
		fts.commit();
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	}


	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
}
