package com.codepath.apps.twitterclient;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet>{
	
	public TweetsAdapter(Context context) {
		super(context, 0, new ArrayList<Tweet>());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}

		Tweet tweet = getItem(position);

		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);

		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		String formattedName = "<b>" + tweet.getUser().getName() + "</b>" + " <small><font color='#777777'>@"
				+ tweet.getUser().getScreenName() + "</font></small>";
		nameView.setText(Html.fromHtml(formattedName));

		TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
		bodyView.setText(Html.fromHtml(tweet.getBody()));

		TextView timestampView = (TextView) view.findViewById(R.id.tvTimestamp);
		timestampView.setText(tweet.getTimestamp());

		return view;
	}

}
