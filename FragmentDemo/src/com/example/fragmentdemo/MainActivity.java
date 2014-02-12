package com.example.fragmentdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Init activity with a default fragment
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flContainer, new SecondFragment());
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onSecond(View v) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		// NOTE: To get the fragment, you have to use getFragmentById instead of getViewbyId
		// NOTE: To get the fragment, you have to use getFragmentById instead of getViewbyId
		
		// Transactions are used to batch replaces together
		// So you could have many replace statemetns here, 
		// Android will queue them up and apply all of them at once 
		ft.replace(R.id.flContainer, new SecondFragment());
		ft.commit();
	}

	public void onFirst(View v) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flContainer, new FirstFragment());
		ft.commit();
	}
}
