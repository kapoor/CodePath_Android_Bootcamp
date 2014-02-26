package com.example.mytest;


import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ListView;


public class MainActivity extends FragmentActivity {
	
	ListView lvMessages;
	MessageAdapter adapterMessages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ArrayList<String> messages = new ArrayList<String>();
		messages.add("Message 1");
		messages.add("Message 2");
		messages.add("Message 3");
		
        lvMessages = (ListView) findViewById(R.id.lvMessages);
        adapterMessages = new MessageAdapter(this, messages);
        lvMessages.setAdapter(adapterMessages);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}