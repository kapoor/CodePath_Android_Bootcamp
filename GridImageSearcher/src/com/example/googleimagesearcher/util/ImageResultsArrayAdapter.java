package com.example.googleimagesearcher.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.googleimagesearcher.R;
import com.loopj.android.image.SmartImageView;

public class ImageResultsArrayAdapter extends ArrayAdapter<ImageResult> {

    public ImageResultsArrayAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = this.getItem(position);
        SmartImageView sivImage;
        
        // Reuse the view
        if(convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            sivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result, parent, false);
        } else {
            sivImage = (SmartImageView) convertView;
            sivImage.setImageResource(android.R.color.transparent);
        }
    
        // Set the image url so that URL's image can be loaded into this view
        sivImage.setImageUrl(imageInfo.getThumbUrl());
        return sivImage;
    }
}
