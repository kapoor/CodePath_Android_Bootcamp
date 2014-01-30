package com.codepath.example.firstdemo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UsersAdapter extends ArrayAdapter<User> {

	public UsersAdapter(Context context, ArrayList<User> users) {
		super(context, R.layout.item_user, users);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		User user = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_user, null);
		}
		// Lookup view for data population
		TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
		TextView tvHome = (TextView) convertView.findViewById(R.id.tvHometown);
		// Populate the data into the template view using the data object
		tvName.setText(user.name);
		tvHome.setText(user.hometown);
		// Return the completed view to render on screen
		return convertView;
	}
}
