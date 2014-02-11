package com.example.viewpagerdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends FragmentActivity {
	FragmentPagerAdapter adapterViewPager;

    public static class MyPagerAdapter extends FragmentPagerAdapter {
    	private static int NUM_ITEMS = 3;
		
		// NOTE: Fragment adapter!
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        
        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
 
        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
        	//TODO: Don't initialize new instances of Fragments every time
        	// User Dynamic ViewPager Fragments:
        	// 		http://guides.thecodepath.com/android/ViewPager-with-FragmentPagerAdapter#dynamic-viewpager-fragments
        	// This will cache the fragments automatically 
            switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return FirstFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return FirstFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 1 - This will show SecondFragment
                return FirstFragment.newInstance(2, "Page # 3");
            default:
            	return null;
            }
        }
        
        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
        	return "Page " + position;
        }
        
    }

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// NOTE: Connect the ViewPager to Adapter 
		ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
