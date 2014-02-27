package com.example.animationplay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onAnimate(View v) {
		Button btnAnimate = (Button) findViewById(R.id.btnAnimate);

		// NOTE: All animation is either (transition) Tween or (flip between) Frame
		
		// xBy and yBy are Tweens
		// If you click the button once, it enlarges, but when you click again, it doesn't
		// enlarge, it just moves..
		// So xBy and yBy ?????
		
		// Animations have events and listeners to facilitate animation chaining,
		// so when the first one finishes, you can do another one.. like you can
		// do a slide followed by a bounce

		// Animations need at least API level 14. But to have it supported on older
		// platforms, you can do it using NineOldAndroids (mentioned here:
		// 		https://github.com/thecodepath/android_guides/wiki/Animations
		btnAnimate.animate().alpha(0.5f).rotation(90f).scaleX(1.2f).xBy(100)
			.yBy(100).setDuration(1000).setStartDelay(10).setListener(new AnimatorListenerAdapter() {
				
			@Override
			public void onAnimationStart(Animator animation) {
				Toast.makeText(MainActivity.this, "Started...",
						Toast.LENGTH_SHORT).show();
			};
		});		
	}
	
	public void onAnimate2(View v) {

		Button btnAnimate2 = (Button) findViewById(R.id.btnAnimate2);

		// Property animators are very powerful and let you animare on any property that you 
		// can get or set with. All this works using ObjectAnimators
	 	ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(btnAnimate2, "scaleX", 1.0f, 2.0f);
		scaleAnim.setDuration(3000);
		scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
		scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
		scaleAnim.start();
	}
	
	public void onAnimate3(View v) {

		Button btnAnimate3 = (Button) findViewById(R.id.btnAnimate3);

		// Inflate animation from XML
		Animation animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);  
		// Setup listeners (optional)
		animFadeOut.setAnimationListener(new AnimationListener() {
		    @Override
		    public void onAnimationStart(Animation animation) {
		        // Fires when animation starts
		    }

		    @Override
		    public void onAnimationEnd(Animation animation) {
		       // ...           
		    }

		    @Override
		    public void onAnimationRepeat(Animation animation) {
		       // ...           
		    }
		});
		// start the animation
		btnAnimate3.startAnimation(animFadeOut);
	}
	
	public void onCallSecondActivity(View v) {
		
		// NOTE: You can also do fragment animations as explained in "Fragment Animations"
		// here: https://github.com/thecodepath/android_guides/wiki/Animations
		
		// You can also use this to animate a bunch of images like a gif like
		// this: http://developer.android.com/reference/android/graphics/drawable/AnimationDrawable.html
		// You can also use animations GIF images directly as explained in 
		// "Animated Images" here: here: https://github.com/thecodepath/android_guides/wiki/Animations
		
		Intent i = new Intent(MainActivity.this, SecondActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}
}
