package ernest.movieapp.data.api;

import ernest.movieapp.data.model.PagedResponse;
import retrofit.http.GET;
import rx.Observable;

public interface MovieDbService {

    @GET("/discover/movie?sort_by=popularity.desc&api_key=22b2875411964b3ccdb006f96d17f5af")
    Observable<PagedResponse> getMovieByPopularity();

    @GET("/discover/movie?sort_by=vote_average.desc&api_key=22b2875411964b3ccdb006f96d17f5af")
    Observable<PagedResponse> getMovieByRating();

}
