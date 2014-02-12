package com.example.fragmentdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FirstFragment extends Fragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      // Defines the xml file for the fragment
      View view = inflater.inflate(R.layout.fragment_first, container, false);
      
      // Setup handles to view objects here

      // NOTE: There is no such thing as findViewById() on a fragment. So there is no setContentView magic here
      // Instead you have to findViewById on the view
      TextView tvFirst = (TextView) view.findViewById(R.id.textView1);
      return view;
    }
}
