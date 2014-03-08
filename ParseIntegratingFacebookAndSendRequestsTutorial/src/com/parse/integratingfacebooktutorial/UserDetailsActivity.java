package com.parse.integratingfacebooktutorial;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class UserDetailsActivity extends Activity {

	private ProfilePictureView userProfilePictureView;
	private TextView userNameView;
	private TextView userLocationView;
	private TextView userGenderView;
	private TextView userDateOfBirthView;
	private TextView userRelationshipView;
	private Button sendRequestButton;
	private Button logoutButton;

	
	// NOTE NOTE: Handing requests is outlined here:
	//	 	https://developers.facebook.com/docs/android/send-requests
	
	// The handled request will have the following info:
	// 	target_url=[URL]/?request_ids=[COMMA_SEPARATED_REQUESTIDs]
    //		&ref=notif&fb_source=notification
    //		&app_request_type=user_to_user
	
	private String requestId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.userdetails);

		userProfilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
		userNameView = (TextView) findViewById(R.id.userName);
		userLocationView = (TextView) findViewById(R.id.userLocation);
		userGenderView = (TextView) findViewById(R.id.userGender);
		userDateOfBirthView = (TextView) findViewById(R.id.userDateOfBirth);
		userRelationshipView = (TextView) findViewById(R.id.userRelationship);

		logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogoutButtonClicked();
			}
		});

		sendRequestButton = (Button) findViewById(R.id.sendRequestButton);
		sendRequestButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        sendRequestDialog();        
		    }
		});
		
		
		postToWall("8367311");

		
		// Check for an incoming notification. Save the info
	    Uri intentUri = getIntent().getData();
	    if (intentUri != null) {
	        String requestIdParam = intentUri.getQueryParameter("request_ids");
	        if (requestIdParam != null) {
	            String array[] = requestIdParam.split(",");
	            requestId = array[0];
	            Log.i("TAG", "Request id: " + requestId);
	        }
	    }
	    
		// Fetch Facebook user info if the session is active
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			
			sendRequestButton.setVisibility(View.VISIBLE);
			
			if(requestId != null) {
		        Toast.makeText(getApplicationContext(), "Incoming request", Toast.LENGTH_SHORT).show();
		        getRequestData(requestId);
		        requestId = null;
			}
			
			makeMeRequest();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// Check if the user is currently logged
			// and show any cached content
			updateViewsWithProfileInfo();
		} else {
			// If the user is not logged in, go to the
			// activity showing the login view.
			startLoginActivity();
		}
	}

	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							// Create a JSON object to hold the profile info
							JSONObject userProfile = new JSONObject();
							try {
								// Populate the JSON object
								userProfile.put("facebookId", user.getId());
								userProfile.put("name", user.getName());
								if (user.getLocation() != null && user.getLocation().getProperty("name") != null) {
									userProfile.put("location", (String) user.getLocation().getProperty("name"));
								}
								if (user.getProperty("gender") != null) {
									userProfile.put("gender", (String) user.getProperty("gender"));
								}
								if (user.getBirthday() != null) {
									userProfile.put("birthday", user.getBirthday());
								}
								if (user.getProperty("relationship_status") != null) {
									userProfile.put("relationship_status",
										(String) user.getProperty("relationship_status"));
								}

								// Save the user profile info in a user property
								ParseUser currentUser = ParseUser
										.getCurrentUser();
								currentUser.put("profile", userProfile);
								currentUser.saveInBackground();

								// Show the user info
								updateViewsWithProfileInfo();
							} catch (JSONException e) {
								Log.d(IntegratingFacebookTutorialApplication.TAG,
										"Error parsing returned user data.");
							}

						} else if (response.getError() != null) {
							if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
									|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
								Log.d(IntegratingFacebookTutorialApplication.TAG,
										"The facebook session was invalidated.");
								onLogoutButtonClicked();
							} else {
								Log.d(IntegratingFacebookTutorialApplication.TAG, "Some other error: " + response.getError() .getErrorMessage());
							}
						}
					}
				});
		request.executeAsync();

	}

	private void updateViewsWithProfileInfo() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser.get("profile") != null) {
			JSONObject userProfile = currentUser.getJSONObject("profile");
			try {
				if (userProfile.getString("facebookId") != null) {
					String facebookId = userProfile.get("facebookId")
							.toString();
					userProfilePictureView.setProfileId(facebookId);
				} else {
					// Show the default, blank user profile picture
					userProfilePictureView.setProfileId(null);
				}
				if (userProfile.getString("name") != null) {
					userNameView.setText(userProfile.getString("name"));
				} else {
					userNameView.setText("");
				}
				if (userProfile.getString("location") != null) {
					userLocationView.setText(userProfile.getString("location"));
				} else {
					userLocationView.setText("");
				}
				if (userProfile.getString("gender") != null) {
					userGenderView.setText(userProfile.getString("gender"));
				} else {
					userGenderView.setText("");
				}
				if (userProfile.getString("birthday") != null) {
					userDateOfBirthView.setText(userProfile
							.getString("birthday"));
				} else {
					userDateOfBirthView.setText("");
				}
				if (userProfile.getString("relationship_status") != null) {
					userRelationshipView.setText(userProfile
							.getString("relationship_status"));
				} else {
					userRelationshipView.setText("");
				}
			} catch (JSONException e) {
				Log.d(IntegratingFacebookTutorialApplication.TAG,
						"Error parsing saved user data.");
			}

		}
	}

	private void sendRequestDialog() {
	    Bundle params = new Bundle();
	    params.putString("message", "One of your friends has invitied you to chat on hush!");
	    params.putString("data",
	            "{\"badge_of_awesomeness\":\"1\"," +
	            "\"social_karma\":\"5\"}");

	    WebDialog requestsDialog = (
	        new WebDialog.RequestsDialogBuilder(UserDetailsActivity.this,
	            Session.getActiveSession(),
	            params))
	            .setOnCompleteListener(new OnCompleteListener() {

	                @Override
	                public void onComplete(Bundle values,
	                    FacebookException error) {
	                    if (error != null) {
	                        if (error instanceof FacebookOperationCanceledException) {
	                            Toast.makeText(getApplicationContext(), 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(getApplicationContext(), 
	                                "Network Error", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    } else {
	                        final String requestId = values.getString("request");
	                        if (requestId != null) {
	                            Toast.makeText(getApplicationContext(), 
	                                "Request sent",  
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(getApplicationContext(), 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    }   
	                }

	            })
	            .build();
	    requestsDialog.show();
	}
	
	private void getRequestData(final String inRequestId) {
	    // Create a new request for an HTTP GET with the
	    // request ID as the Graph path.
	    Request request = new Request(Session.getActiveSession(), 
	            inRequestId, null, HttpMethod.GET, new Request.Callback() {

	                @Override
	                public void onCompleted(Response response) {
	                    // Process the returned response
	                    GraphObject graphObject = response.getGraphObject();
	                    FacebookRequestError error = response.getError();
	                    boolean processError = false;
	                    
	                    // Default message
	                    String message = "Incoming request";
	                    if (graphObject != null) {
	                        // Check if there is extra data
	                        if (graphObject.getProperty("data") != null) {
	                            try {
	                                // Get the data, parse info to get the key/value info
	                                JSONObject dataObject = 
	                                new JSONObject((String)graphObject.getProperty("data"));
	                                // Get the value for the key - badge_of_awesomeness
	                                String badge = 
	                                    dataObject.getString("badge_of_awesomeness");
	                                // Get the value for the key - social_karma
	                                String karma = 
	                                    dataObject.getString("social_karma");
	                                // Get the sender's name
	                                JSONObject fromObject = 
	                                    (JSONObject) graphObject.getProperty("from");
	                                String sender = fromObject.getString("name");
	                                String title = sender+" sent you a gift";
	                                // Create the text for the alert based on the sender
	                                // and the data
	                                message = title + "\n\n" + 
	                                    "Badge: " + badge + 
	                                    " Karma: " + karma;
	                            } catch (JSONException e) {
	                            	processError = true;
	                                message = "Error getting request info";
	                            }
	                        } else if (error != null) {
	                        	processError = true;
	                            message = "Error getting request info";
	                        }
	                    }
	                    
	                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	                    
	                    // Delete the request if there was no error in processing it
	                    if (!processError) {
	                        deleteRequest(inRequestId);
	                    }
	                }
	        });
	    // Execute the request asynchronously.
	    Request.executeBatchAsync(request);
	}
	
	private void deleteRequest(String inRequestId) {
	    // Create a new request for an HTTP delete with the
	    // request ID as the Graph path.
	    Request request = new Request(Session.getActiveSession(), 
	        inRequestId, null, HttpMethod.DELETE, new Request.Callback() {

	            @Override
	            public void onCompleted(Response response) {
	                // Show a confirmation of the deletion
	                // when the API call completes successfully.
	                Toast.makeText(getApplicationContext(), "Request deleted", Toast.LENGTH_SHORT).show();
	            }
	        });
	    // Execute the request asynchronously.
	    Request.executeBatchAsync(request);
	}

	protected void postToWall(final String userId)
	{
		// Tutorial: https://developers.facebook.com/docs/reference/dialogs/feed/
		
		/*
			// Using Feed dialog - this asks the poster to fill in something and actually send it to the user
		try {
			Bundle params = new Bundle();
			params.putString("name", "Facebook SDK for Android");// title
			params.putString("caption",
					"Build great social apps and get more installs.");// caption
			params.putString(
					"description",
					"The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
			params.putString("to", userId);

			WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
					UserDetailsActivity.this,
					Session.getActiveSession(), params)).setOnCompleteListener(
					null).build();
			feedDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		

		// NOT WORKING RIGHT NOW
		final Bundle _postParameter = new Bundle();
		 _postParameter.putString("name", "My name");
		 _postParameter.putString("link", "http://testapp.com");
		 _postParameter.putString("picture", "https://www.gravatar.com/avatar/81b7961fd397b3957516277400e5ae2e?s=32&d=identicon&r=PG");
		 _postParameter.putString("caption", "Test caption");
		 _postParameter.putString("description", "test description");

		 final List<String> PERMISSIONS = Arrays.asList("publish_actions");

		 if (Session.getActiveSession() != null)
		 {
		       NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
		       Session.getActiveSession().requestNewPublishPermissions(reauthRequest);
		 }

		this.runOnUiThread(new Runnable()
		{
		    @Override
		    public void run() 
		    {
		        Request request = new Request(Session.getActiveSession(), userId + "/feed", _postParameter, HttpMethod.POST);
		        RequestAsyncTask task = new RequestAsyncTask(request);
		        task.execute();
		    }
		});
		
		//TODO: Implement deep linking - when a user clicks on a chat link, it should take him into the chat
		//	https://developers.facebook.com/docs/android/link-to-your-native-app/
	}
	
	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();

		// Go to the login view
		startLoginActivity();
	}

	private void startLoginActivity() {
		Intent intent = new Intent(this, MyLoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
