package com.example.tipcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		EditText etOne = (EditText) findViewById(R.id.editText1);
		EditText etTwo = (EditText) findViewById(R.id.editText2);
		etOne.setText(getIntent().getStringExtra(TipCalculatorActivity.FOO_KEY));
		etTwo.setText(getIntent().getStringExtra(TipCalculatorActivity.BAR_KEY));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
