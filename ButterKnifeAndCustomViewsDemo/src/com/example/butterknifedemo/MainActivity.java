package com.example.butterknifedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {

	/*
	@InjectView(R.id.btnPerform) Button btnPerform;
	@InjectView(R.id.tvLabel) TextView tvLabel;
	
	@OnClick(R.id.btnPerform)
	public void onLabelClick(Button b) {
		tvLabel.setText("Hello World");
	}
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// NOTE NOTE NOTE: Immediately call this after setContentView
		// This is the secret sauce that makes ButterKnife very fast!
		ButterKnife.inject(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
