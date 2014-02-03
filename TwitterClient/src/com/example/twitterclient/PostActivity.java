package com.example.twitterclient;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PostActivity extends Activity {
	EditText etTweet;
	TextView tvUser;
	ImageView ivUserProfile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		etTweet = (EditText) findViewById(R.id.etTweet);
		tvUser = (TextView) findViewById(R.id.tvUser);
		ivUserProfile = (ImageView) findViewById(R.id.ivUserProfile);
		
		String screenName = getIntent().getStringExtra("screenName");
		String profileImageUrl = getIntent().getStringExtra("profileImageUrl");
		
		tvUser.setText('@'+screenName);
		ImageLoader.getInstance().displayImage(profileImageUrl, ivUserProfile);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
		return true;
	}
	
	public void onPostTweetCancel(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	public void onPostTweet(View v) {
		String tweetBody = etTweet.getText().toString();
		
		MainApp.getRestClient().postTweet(tweetBody, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonTweet) {
				Tweet tweet = Tweet.fromJson(jsonTweet);
				
				Intent data = new Intent();
				data.putExtra("tweet", tweet);
				
				setResult(RESULT_OK, data);
				finish();
			}

			@Override
			public void onFailure(Throwable arg0, String message) {
				Toast.makeText(PostActivity.this, "Failed: " + message, Toast.LENGTH_LONG).show();
			}
			
		});
	}
}