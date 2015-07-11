package ernest.movieapp.data.model;

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

}
