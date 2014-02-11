package com.codepath.example.basictabsplay;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class MainActivity extends FragmentActivity implements TabListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tab1 = getActionBar()
				.newTab()
				.setText("First")
				.setIcon(R.drawable.ic_launcher)
				.setTabListener(this);
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);
		Tab tab2 = getActionBar()
				.newTab()
				.setText("Second")
				.setIcon(R.drawable.ic_launcher)
				.setTag("HomeTimelineFragment").setTabListener(this);
		actionBar.addTab(tab2);
		Tab tab3 = getActionBar()
				.newTab()
				.setText("Third")
				.setIcon(R.drawable.ic_launcher)
				.setTabListener(this);
		actionBar.addTab(tab3);
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
		if (tab.getText() == "First") {
			fts.replace(R.id.flContainer, FirstFragment.newInstance(10, "First"));	
		} else if (tab.getText() == "Second") {
			fts.replace(R.id.flContainer, FirstFragment.newInstance(20, "Second"));	
		} else if (tab.getText() == "Third") { 
			fts.replace(R.id.flContainer, FirstFragment.newInstance(30, "Third"));	
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		
	}

}
