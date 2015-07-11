package ernest.movieapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

public class PagedResponse {

    public final Integer page;

    @SerializedName("results") public final Collection<MovieItem> movies;

    @SerializedName("total_pages") public final Integer pages;

    @SerializedName("total_results") public final Integer results;

    public PagedResponse(Integer page, Collection<MovieItem> movies, Integer pages, Integer results) {
        this.page = page;
        this.movies = movies;
        this.pages = pages;
        this.results = results;
    }

}
