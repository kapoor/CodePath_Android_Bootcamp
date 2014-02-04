package com.example.rottentomatoesdemo;


/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 * 
 *     RestClient client = RestClientApp.getRestClient();
 *     // use client to send requests to API
 *     
 */
public class MainApp extends com.activeandroid.app.Application {

	@Override
    public void onCreate() {
        super.onCreate();
    }
}