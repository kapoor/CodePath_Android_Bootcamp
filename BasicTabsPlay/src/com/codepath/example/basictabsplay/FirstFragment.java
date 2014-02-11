package com.codepath.example.basictabsplay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FirstFragment extends Fragment {
	int someInt;
	String someTitle;
	
	public static FirstFragment newInstance(int someInt, String someTitle) {
		FirstFragment fragmentDemo = new FirstFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", someInt);
		args.putString("someTitle", someTitle);
		fragmentDemo.setArguments(args);
		return fragmentDemo;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get back arguments
		someInt = getArguments().getInt("someInt", 0);
		someTitle = getArguments().getString("someTitle", "");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_first, null);
		TextView tvLabel = (TextView) v.findViewById(R.id.tvLabel);
		tvLabel.setText(someInt + " " + someTitle);
		return v;
	}
}
