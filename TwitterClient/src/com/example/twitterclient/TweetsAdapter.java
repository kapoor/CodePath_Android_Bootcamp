package com.example.twitterclient;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitterclient.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet>{
	
	ImageView imageView;
	TextView nameView;
	TextView bodyView;
	TextView timestampView;
	
	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	private void setupViews(View view) {
		imageView = (ImageView) view.findViewById(R.id.ivProfile);
		nameView = (TextView) view.findViewById(R.id.tvName);
		bodyView = (TextView) view.findViewById(R.id.tvBody);
		timestampView = (TextView) view.findViewById(R.id.tvTimestamp);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}

		setupViews(view);
		
		Tweet tweet = getItem(position);

		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);

		String formattedName = "<b>" + tweet.getUser().getName() + "</b>" + " <small><font color='#777777'>@"
				+ tweet.getUser().getScreenName() + "</font></small>";
		nameView.setText(Html.fromHtml(formattedName));

		bodyView.setText(Html.fromHtml(tweet.getBody()));

		timestampView.setText(tweet.getRelativeTimeSpanString());

		return view;
	}

}
