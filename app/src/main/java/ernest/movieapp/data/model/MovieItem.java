package ernest.movieapp.data.model;

import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

public class MovieItem {

    @SerializedName("poster_path") public final String posterURL;

    @SerializedName("original_title") public final String title;

    public final String overview;

    @SerializedName("vote_average") public final float rating;

    @SerializedName("release_date") public final String date;

    public MovieItem(String posterURL, String title, String overview, float rating, String date) {
        this.posterURL = posterURL;
        this.title = title;
        this.overview = overview;
        this.rating = rating;
        this.date = date;
    }

    public String getPosterUrl() {
        return "http://image.tmdb.org/t/p/w185/" + posterURL;
    }

    public Bundle to() {
        Bundle bundle = new Bundle();
        bundle.putString("poster_path", posterURL);
        bundle.putString("original_title", title);
        bundle.putString("overview", overview);
        bundle.putFloat("vote_average", rating);
        bundle.putString("release_date", date);
        return bundle;
    }

    public static MovieItem from(Bundle bundle) {
        return new MovieItem(bundle.getString("poster_path"), bundle.getString("original_title"),
            bundle.getString("overview"), bundle.getFloat("vote_average"),
            bundle.getString("release_date"));
    }

}
