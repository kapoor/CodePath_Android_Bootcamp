package com.example.mytest;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<String> {
	
	Context activityContext;
	
	public MessageAdapter(Context context, ArrayList<String> messages) {
		super(context, 0, messages);
		activityContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		String movie = getItem(position);

		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.message_item, null);
		}
		
        ((TextView) view.findViewById(R.id.message_text)).setText(movie);
        
        final LayerDrawable ld = (LayerDrawable) activityContext.getResources().getDrawable(R.drawable.message_bubble);
        
        //final Drawable d1 = (Drawable) ld.findDrawableByLayerId(R.id.outerRectangle);
        //d1.setColorFilter(0xFF334455, Mode.MULTIPLY);
        //ld.setDrawableByLayerId(R.id.outerRectangle, d1);
        
        ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.setIntrinsicHeight(20);
        rect.setIntrinsicWidth(100);
        rect.getPaint().setColor(Color.BLUE);
        ld.setDrawableByLayerId(R.id.outerRectangle, rect);
        
		return view;
	}
}
