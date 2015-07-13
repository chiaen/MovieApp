package ernest.movieapp.data.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieItem implements Parcelable {

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

    private MovieItem(Parcel in) {
        this.posterURL = in.readString();
        this.title = in.readString();
        this.overview = in.readString();
        this.rating = in.readFloat();
        this.date = in.readString();
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

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterURL);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeFloat(this.rating);
        dest.writeString(this.date);
    }

    public static final Parcelable.Creator<MovieItem> CREATOR
        = new Parcelable.Creator<MovieItem>() {

        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }

    };

}
