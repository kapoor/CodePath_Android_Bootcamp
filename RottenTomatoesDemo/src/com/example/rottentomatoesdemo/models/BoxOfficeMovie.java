package com.example.rottentomatoesdemo.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

// Model representing fields from JSON that we want to match, a method to serialize data and to desirialize it
@Table(name = "BoxOfficeMovies")
public class BoxOfficeMovie extends BaseModel {
	
	@Column(name = "title")
    private String title;

	@Column(name = "year")
	private int year;

	@Column(name = "synopsis")
	private String synopsis;

	@Column(name = "posterUrl")
	private String posterUrl;
	
	@Column(name = "criticsScore")
    private int criticsScore;
	
	@Column(name = "castList")
    private String castList;

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public int getCriticsScore() {
        return criticsScore;
    }

    public String getCastList() {
        return castList;
    }
    
    public BoxOfficeMovie() {
    	super();
    }
    
    
    // Returns a BoxOfficeMovie given the expected JSON
    // BoxOfficeMovie.fromJson(movieJsonDictionary)
    // Stores the `title`, `year`, `synopsis`, `poster` and `criticsScore`
    public BoxOfficeMovie(JSONObject jsonObject) {
        try {
            // Deserialize json into object fields
            title = jsonObject.getString("title");
            year = jsonObject.getInt("year");
            synopsis = jsonObject.getString("synopsis");
            posterUrl = jsonObject.getJSONObject("posters").getString("thumbnail");
            criticsScore = jsonObject.getJSONObject("ratings").getInt("critics_score");
            
            ArrayList<String> cl = new ArrayList<String>();	// Construct simple array of cast names
            JSONArray abridgedCast = jsonObject.getJSONArray("abridged_cast");
            for (int i = 0; i < abridgedCast.length(); i++) {
                cl.add(abridgedCast.getJSONObject(i).getString("name"));
            }
            
            castList = TextUtils.join(", ", cl);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
    }
    
    
    // Decodes array of box office movie json results into business model objects
    // BoxOfficeMovie.fromJson(jsonArrayOfMovies)
    public static ArrayList<BoxOfficeMovie> fromJson(JSONArray jsonArray) {
        ArrayList<BoxOfficeMovie> movies = new ArrayList<BoxOfficeMovie>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject movieJSON = null;
            try {
                movieJSON = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            BoxOfficeMovie movie = new BoxOfficeMovie(movieJSON);
            if (movie != null) {
                movie.save();
                movies.add(movie);
            }
        }

        return movies;
    }
    
    public static ArrayList<BoxOfficeMovie> getOfflineMovies() {

    	// Examples:
    	// return new Select().from(SampleModel.class).where("id = ?", id).executeSingle();
    	// return new Select().from(SampleModel.class).orderBy("id DESC").limit("300").execute();
       
    	// Show only top 20 recent movies
    	List<BoxOfficeMovie> movies = new Select().from(BoxOfficeMovie.class).limit(20).execute();
    	return new ArrayList<BoxOfficeMovie>(movies);
    }
}