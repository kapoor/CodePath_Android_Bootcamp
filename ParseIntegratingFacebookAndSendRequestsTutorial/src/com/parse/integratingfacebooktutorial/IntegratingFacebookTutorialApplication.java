package com.parse.integratingfacebooktutorial;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class IntegratingFacebookTutorialApplication extends Application {

	static final String TAG = "MyApp";

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "4UgZtiv4tiEQrmPVGsct6XS6SVLGnrXA0kNggThY",
				"P4L7TLK7dlWh94cUTR6R0GYs8mDos5savWVlWXqV");

		// Set your Facebook App Id in strings.xml
		ParseFacebookUtils.initialize(getString(R.string.app_id));
	}

}
