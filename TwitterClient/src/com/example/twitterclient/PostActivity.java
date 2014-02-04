package com.example.twitterclient;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PostActivity extends Activity {
	
	public static final String LOG_TAG = TimelineActivity.class.getName();

    // Views
	private EditText etTweet;
	private TextView tvUser;
	private ImageView ivUserProfile;
	private TextView tvRemainingCharacters;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
		setContentView(R.layout.activity_post);

        setupViews();

		String screenName = getIntent().getStringExtra("screenName");
		String profileImageUrl = getIntent().getStringExtra("profileImageUrl");
		
		tvUser.setText('@'+screenName);
		ImageLoader.getInstance().displayImage(profileImageUrl, ivUserProfile);
		
        addListeners();

	}
    
    private void setupViews() {
		etTweet = (EditText) findViewById(R.id.etTweet);
		tvUser = (TextView) findViewById(R.id.tvUser);
		ivUserProfile = (ImageView) findViewById(R.id.ivUserProfile);
		tvRemainingCharacters = (TextView) findViewById(R.id.tvRemainingChars);
    }

    private void addListeners() {
    	etTweet.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            	tvRemainingCharacters.setHint(
        			String.valueOf(
    					getResources().getInteger(
							R.integer.tweet_max_length) - etTweet.getText().toString().length()));
            }
        });
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
		
        setProgressBarIndeterminateVisibility(true);

		MainApp.getRestClient().postTweet(tweetBody, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonTweet) {
				
		        setProgressBarIndeterminateVisibility(false);
		        
				Tweet tweet = new Tweet(jsonTweet);
				
				Intent data = new Intent();
				data.putExtra("tweet", tweet);
				
				setResult(RESULT_OK, data);
				finish();
			}

			@Override
			public void onFailure(Throwable arg0, String message) {
		        setProgressBarIndeterminateVisibility(false);
				Toast.makeText(PostActivity.this, getResources().getString(R.string.could_not_post_tweet_error) + message, Toast.LENGTH_LONG).show();
			}
			
		});
	}
}