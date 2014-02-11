package com.example.twitterclient.fragments;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twitterclient.MainApp;
import com.example.twitterclient.R;
import com.example.twitterclient.models.User;
import com.example.twitterclient.util.Constants;
import com.example.twitterclient.util.FragmentInterface;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileFragment extends Fragment {

	// Instance variables
	private User user;
    private FragmentInterface fragmentIfListener;

    // Views
	private View fragmentProfileView;
	private TextView tvUser;
	private TextView tvTagline;
	private TextView tvFollowers;
	private TextView tvFollowing;
	private ImageView ivProfileImage;


	public static ProfileFragment newInstance(User user) {
		ProfileFragment profileFragment = new ProfileFragment();
		Bundle args = new Bundle();
		args.putSerializable(Constants.userExtra, user);
		profileFragment.setArguments(args);
		return profileFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get back arguments
		user = (User) getArguments().getSerializable(Constants.userExtra); 
	}

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {

    	fragmentProfileView = inf.inflate(R.layout.fragment_profile, parent, false);

		setupViews();
		loadMyInfo();

		return fragmentProfileView;
	}

    private void setupViews() {
        tvUser = (TextView) fragmentProfileView.findViewById(R.id.tvName);
        tvTagline = (TextView) fragmentProfileView.findViewById(R.id.tvTagline);
        tvFollowers = (TextView) fragmentProfileView.findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) fragmentProfileView.findViewById(R.id.tvFollowing);
        ivProfileImage = (ImageView) fragmentProfileView.findViewById(R.id.ivProfileImage);
    }
    
	
	public void loadMyInfo() {
		
        // Show the locally stored user profile cached from the last request
        // and refresh it if the network request succeeds
		user = User.getOfflineUser();
		if (user != null) {
			populateProfileHeader();
		}

		fragmentIfListener.showProgressBar();
		
		MainApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonUser) {
				user = new User(jsonUser);
				populateProfileHeader();
				fragmentIfListener.hideProgressBar();
			}

			@Override
			public void onFailure(Throwable e, String message) {
                Toast.makeText(getActivity(), getString(R.string.could_not_get_user_error), Toast.LENGTH_SHORT).show();
                fragmentIfListener.hideProgressBar();
			}
		});
	}
	
	private void populateProfileHeader() {
		tvUser.setText(user.getName());
		tvTagline.setText(user.getTagline());
		tvFollowers.setText(user.getFollowersCount() + " Followers");
		tvFollowing.setText(user.getFriendsCount() + " Following");
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
	}
	
    // TODO: Refactor this out in a base class since all fragments should implement this interface
    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
		if (activity instanceof FragmentInterface) {
			fragmentIfListener = (FragmentInterface) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implement FragmentInterface");
		}
    }

}