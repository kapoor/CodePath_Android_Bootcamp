package com.example.tipcalculator;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TipCalculatorActivity extends Activity {
	
	private final String className = this.getClass().getName();
	
	private EditText etTotalAmount;
	private TextView lblTipAmount;
	private EditText etCustomTipAmount;

	private float selectedTipPercentage = 0.0f;
	
	private static final DecimalFormat df = new DecimalFormat();
	
	public static final String FOO_KEY = "foo";
	public static final String BAR_KEY = "bar";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calculator);

		etTotalAmount = (EditText) findViewById(R.id.etTotalAmount);
		lblTipAmount = (TextView) findViewById(R.id.lblTipAmount);
		etCustomTipAmount = (EditText) findViewById(R.id.etCustomTipAmount);
		
		// Set the decimal format
		df.setMaximumFractionDigits(2);
		
		
		// Add listener to update tip automatically if total amount is changed
		TextWatcher etTotalAmountTextWatcher = new TextWatcher() {
	        public void afterTextChanged(Editable s) { 
	    		lblTipAmount.setText("$" + df.format(getTotalAmount() * selectedTipPercentage));
	        }
	        
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){
	        }
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        }
		};
	
		etTotalAmount.addTextChangedListener(etTotalAmountTextWatcher);

		
		// Add listener to update tip automatically if custom tip amount is specified
		TextWatcher etCustomTipAmountTextWatcher = new TextWatcher() {
	        public void afterTextChanged(Editable s) {
	    		lblTipAmount.setText("$" + df.format(getTotalAmount() * getCustomTipPercentage()));
	        }
	        
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){
	        }
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        }
		};
	
		etCustomTipAmount.addTextChangedListener(etCustomTipAmountTextWatcher);
		

		// Show/hide soft keyboard when edit texts get/lose focus
		etTotalAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
	    		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		        if (hasFocus) {
		    		imm.showSoftInput(etTotalAmount, InputMethodManager.SHOW_IMPLICIT);
		        }
		        else {
		        	imm.hideSoftInputFromWindow(etTotalAmount.getWindowToken(),0);
		        }
		    }
		});
		
		etCustomTipAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
	    		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		        if (hasFocus) {
		    		imm.showSoftInput(etCustomTipAmount, InputMethodManager.SHOW_IMPLICIT);
		        }
		        else {
		        	imm.hideSoftInputFromWindow(etCustomTipAmount.getWindowToken(),0); 
		        }
		    }
		});

	}

	private float getCustomTipPercentage() {
		try {
			selectedTipPercentage = Float.parseFloat(etCustomTipAmount.getText().toString())/100;
		}
		catch (NumberFormatException nfe) {
			Log.d(className, "Parsed invalid custom tip amount");
		}

		return selectedTipPercentage;
	}
	
	private float getTotalAmount() {
		float totalAmount = 0;
		
		try {
			totalAmount = Float.parseFloat(etTotalAmount.getText().toString());
		}
		catch (NumberFormatException nfe) {
			Log.d(className, "Parsed invalid total amount");
		}

		return totalAmount;
	}
	
	public void onTipClick(View v) {
		Button btn = (Button)v;
		
		switch (btn.getId()) {
		case R.id.btnTenPercent:
			selectedTipPercentage = 0.1f;
			break;
		case R.id.btnFifteenPercent:
			selectedTipPercentage = 0.15f;
			break;
		case R.id.btnTwentyPercent:
			selectedTipPercentage = 0.2f;
			break;
		}
		
		lblTipAmount.setText("$" + df.format(getTotalAmount() * selectedTipPercentage));
	}
	
	
	// Code for settings menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tip_calculator, menu);
		return true;
	}

	public void onSettingsAction(MenuItem mi) {
	     Toast.makeText(this, "Settings Clicked!", Toast.LENGTH_SHORT).show();
	     Intent i = new Intent(this, SettingsActivity.class);
	     i.putExtra(FOO_KEY, "Hello");
	     i.putExtra(BAR_KEY, "World");
	     startActivity(i);
	}
	
}
