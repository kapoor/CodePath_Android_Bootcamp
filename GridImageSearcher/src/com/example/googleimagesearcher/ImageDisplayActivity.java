package com.example.googleimagesearcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.googleimagesearcher.util.ImageResult;
import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {

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
        return true;
    }
    
    public void onClose(View v) {
        finish();
    }
}
