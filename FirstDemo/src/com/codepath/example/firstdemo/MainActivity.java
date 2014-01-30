package com.codepath.example.firstdemo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	ListView lvUsers;
	ArrayAdapter<User> adapterUsers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ArrayList<User> users = User.getFakeUsers();
		
		// Style 1
		//adapterUsers = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, users);
		
		// Style 2
		// adapterUsers = new UsersAdapter(this);
		// adapterUsers.addAll(users); // this is not available in API 10
		
		// Style 3
		adapterUsers = new UsersAdapter(this, users);
		
		lvUsers = (ListView) findViewById(R.id.lvUsers);
		lvUsers.setAdapter(adapterUsers);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
