package com.example.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TodoActivity extends Activity {
	
	/*
	private Enum activityCode {
		EditItemActivity;
	};
	*/
	
	private final int REQUESTED_ACTIVITY_CODE = 1;
	
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);

		etNewItem = (EditText) findViewById(R.id.etNewItem);
		lvItems = (ListView) findViewById(R.id.lvItems);
		
		readItems();
		todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);

		lvItems.setAdapter(todoAdapter);
		
		setupListViewListener();
	}

	
	private void setupListViewListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int index, long id) {
				
				// NOTE: There are 2 ways to update the list data.
				// You can either remove the data from the adapter itself, or modify the
				// backing ArrayList and notify the adapter
				
				// todoAdapter.remove(object);
				
				todoItems.remove(index);
				todoAdapter.notifyDataSetChanged();
				
				writeItems();
				return false;
			}
		});

		lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int index, long id) {

				// Call the Edit activity and pass the item body to it
				Intent i = new Intent(getApplicationContext(), EditItemActivity.class);

				// Put "extras" into the bundle for access in the second activity
				String textToEdit = ((TextView)item).getText().toString();
				i.putExtra("textToEdit", textToEdit); 
				i.putExtra("textItemIndex", index);
				
				// Navigate to the second activity
				startActivityForResult(i, REQUESTED_ACTIVITY_CODE);
			}

		});
	}

	public void onAddedItem(View v) {
		String itemText = etNewItem.getText().toString();
		todoAdapter.add(itemText);	 // NOTE: Adding an item to the adapter automatically refreshes the view
		etNewItem.setText("");
		
		writeItems();
	}
	
	private void readItems() {	
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");

		try {
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			todoItems = new ArrayList<String>();
		}
	}
	
	private void writeItems() {		
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");

		try {
			FileUtils.writeLines(todoFile, todoItems);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestedActivityCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestedActivityCode == REQUESTED_ACTIVITY_CODE) {
			
			// Extract values from the result and update the edited text
			String updatedText = data.getExtras().getString("updatedText");
			int textItemIndex = data.getExtras().getInt("textItemIndex");
			
			todoItems.set(textItemIndex, updatedText);
			todoAdapter.notifyDataSetChanged();
		}
	} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}

}
