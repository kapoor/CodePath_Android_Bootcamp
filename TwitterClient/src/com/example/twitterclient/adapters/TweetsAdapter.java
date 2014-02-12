package com.example.twitterclient.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twitterclient.R;
import com.example.twitterclient.activities.ProfileActivity;
import com.example.twitterclient.activities.TimelineActivity;
import com.example.twitterclient.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet>{
	
	private View tweetItemView;
	private ImageView imageView;
	private TextView nameView;
	private TextView bodyView;
	private TextView timestampView;
	
	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	private void setupViews() {
		imageView = (ImageView) tweetItemView.findViewById(R.id.ivProfile);
		nameView = (TextView) tweetItemView.findViewById(R.id.tvName);
		bodyView = (TextView) tweetItemView.findViewById(R.id.tvBody);
		timestampView = (TextView) tweetItemView.findViewById(R.id.tvTimestamp);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		tweetItemView = convertView;
		if (tweetItemView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			tweetItemView = inflater.inflate(R.layout.tweet_item, null);
		}

		setupViews();

		// Populate view data and add listeners
		final Tweet tweet = getItem(position);

		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);

		String formattedName = "<b>" + tweet.getUser().getName() + "</b>" + " <small><font color='#777777'>@"
				+ tweet.getUser().getScreenName() + "</font></small>";
		nameView.setText(Html.fromHtml(formattedName));
		bodyView.setText(Html.fromHtml(tweet.getBody()));
		timestampView.setText(tweet.getRelativeTimeSpanString());

		// TODO: From helpful hints:
		// Use the setTag method to store the username of the user within the tag of the ImageView.
		// Now you need to use setOnClickListener on the ImageView to launch the ProfileActivity
		// passing the username based on the tag into the intent.
        imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), ProfileActivity.class);
				intent.putExtra(TimelineActivity.userExtra, tweet.getUser());
				getContext().startActivity(intent);
			}
		});
        
		return tweetItemView;
	}

}
