package com.example.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends Activity {

	private EditText etItemToEdit;
	private int itemPosition; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		etItemToEdit = (EditText) findViewById(R.id.etItemToEdit);

		// Extract extras from the bundle and set the text value to allow edits
		String textToEdit = getIntent().getStringExtra("textToEdit");
		itemPosition = getIntent().getIntExtra("textItemIndex", -1);
		
		etItemToEdit.setText(textToEdit, TextView.BufferType.EDITABLE);
		etItemToEdit.setSelection(etItemToEdit.getText().length());
	}

	public void onSave(View v) {
		
		// Set data to pass data to parent (caller) activity and close this one
		Intent data = new Intent();
		data.putExtra("updatedText", etItemToEdit.getText().toString());
		data.putExtra("textItemIndex", itemPosition);
		setResult(RESULT_OK, data);

		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

}
