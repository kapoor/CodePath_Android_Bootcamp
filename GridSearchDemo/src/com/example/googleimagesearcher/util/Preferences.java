package com.example.googleimagesearcher.util;

import java.io.Serializable;

public class Preferences implements Serializable{
    
    private static final long serialVersionUID = 6000975446647267854L;

    public static String PREFERENCES_BUNDLE_PARAM = "preferences";

    public String imageSize, imageColor, imageType, searchSite;

    public Preferences(String inImageSize, String inImageColor, String inImageType, String inSearchSite) {
        imageSize = inImageSize;
        imageColor = inImageColor;
        imageType = inImageType;
        searchSite = (inSearchSite == null || inSearchSite.trim().isEmpty()) ? "google.com" : inSearchSite;
    }
}
