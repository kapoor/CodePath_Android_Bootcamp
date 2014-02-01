package com.example.googleimagesearcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.example.googleimagesearcher.util.ImageResult;
import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {

	public static final String TAG = ImageDisplayActivity.class.getName();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        
        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
        SmartImageView sivImage = (SmartImageView) findViewById(R.id.sivResult);
        sivImage.setImageUrl(result.getFullUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_display, menu);
        
        MenuItem shareItem = (MenuItem) menu.findItem(R.id.miShare);
        
        // Fetch and store ShareActionProvider
 		ShareActionProvider miShareAction = new ShareActionProvider(ImageDisplayActivity.this);
        miShareAction = (ShareActionProvider) shareItem.getActionProvider();

        shareItem(miShareAction);
        
        // Return true to display menu
        return true;
    }
    
    public void onClose(View v) {
        finish();
    }
    
    public boolean isExternalStorageWritable() {
    	boolean mExternalStorageReadable = false;
    	boolean mExternalStorageWritable = false;
    	String state = Environment.getExternalStorageState();

    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    	    // We can read and write the media
    	    mExternalStorageReadable = mExternalStorageWritable = true;
    	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    	    // We can only read the media
    	    mExternalStorageReadable = true;
    	    mExternalStorageWritable = false;
    	} else {
    	    // Something else is wrong. It may be one of many other states, but all we need
    	    //  to know is we can neither read nor write
    	    mExternalStorageReadable = mExternalStorageWritable = false;
    	}
    	
    	return mExternalStorageWritable;
    }
    
    
	public void shareItem(ShareActionProvider miShareAction) {
		
		// This will fail if the SD card is not writable or
		// if the AVD doesn't have an SD card configured in the settings 
		if(!isExternalStorageWritable()) {
			Toast.makeText(this, getString(R.string.could_not_write_image_to_sd_card), Toast.LENGTH_SHORT).show();
		}

		// Get access to bitmap image from view
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.sivResult);
		Bitmap bitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
		
		// Write image to default external storage directory
		Uri bmpUri = null;
		try {
			// Get the name of the download directory
			String downloadsDirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

			// If the download directory does not exist, then create it
			File downloadsDir = new File(downloadsDirName);
			downloadsDir.mkdirs();
			
			// Create the file to share
			File sdCardFile = new File(downloadsDirName + "share_image.png");
			sdCardFile.createNewFile();
			
			// Write the image loaded in SmartView to the file
			FileOutputStream out = new FileOutputStream(sdCardFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			bmpUri = Uri.fromFile(sdCardFile);
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, getString(R.string.image_could_not_be_shared), Toast.LENGTH_SHORT).show();
			return;
		}

		if (bmpUri != null) {
			
			// Construct a ShareIntent with link to image
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.setType("image/*");
			shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
			
			// Attach the intent to the menu item provider
			miShareAction.setShareIntent(shareIntent);
		} else {
			Toast.makeText(this, getString(R.string.image_could_not_be_shared), Toast.LENGTH_SHORT).show();
		}
	}
}
