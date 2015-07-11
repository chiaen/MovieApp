package ernest.movieapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.common.collect.Lists;
import com.uwetrottmann.tmdb.Tmdb;
import com.uwetrottmann.tmdb.entities.MovieResultsPage;
import com.uwetrottmann.tmdb.enumerations.SortBy;
import com.uwetrottmann.tmdb.services.DiscoverService;

import java.util.List;

import ernest.movieapp.data.api.MovieDbService;
import ernest.movieapp.data.model.MovieItem;
import ernest.movieapp.data.model.PagedResponse;
import retrofit.RestAdapter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<MovieItem> mItems;
    private MovieViewAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private MovieDbService mMovieService;
    private DiscoverService mDiscoverService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.w("OnCreate");

        // initialize the items list
        mItems = Lists.newArrayList();
//        String intersteller = "https://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
//        mItems.add(new MovieViewAdapter.MovieItem(intersteller));
//        mItems.add(new MovieViewAdapter.MovieItem(intersteller));
//        mItems.add(new MovieViewAdapter.MovieItem(intersteller));
//        mItems.add(new MovieViewAdapter.MovieItem(intersteller));
//        mItems.add(new MovieViewAdapter.MovieItem("https://image.tmdb
// .org/t/p/w185//qrFwjJ5nvFnpBCmXLI4YoeHJNBH.jpg"));

        Timber.w("OnCreate done");

        RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api.themoviedb.org/3")
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();
        mMovieService = restAdapter.create(MovieDbService.class);

        Tmdb tmdb = new Tmdb();
        tmdb.setApiKey("22b2875411964b3ccdb006f96d17f5af");
        mDiscoverService = tmdb.discoverService();

    }

    private MovieResultsPage getMovieByPopularity() {
        if (mDiscoverService == null) {
            return null;
        }
        return mDiscoverService.discoverMovie(true, true, null, 1, null, null, null, null, null,
            SortBy.POPULARITY_DESC, null, null, null, null, null, null, null, null, null, null,
            null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        Timber.w("OnCreateView");
        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);

        mLinearLayoutManager = new GridLayoutManager(getActivity(), 2);

        mAdapter = new MovieViewAdapter(getActivity());

        RecyclerView listView = (RecyclerView) fragmentView.findViewById(android.R.id.list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(mLinearLayoutManager);
        listView.setAdapter(mAdapter);



//        Async
//            .fromCallable(new Callable<MovieResultsPage>() {
//
//                @Override public MovieResultsPage call() throws Exception {
//                    MovieResultsPage results = getMovieByPopularity();
//
//                    Timber.w("getting page: " + results.page);
//                    return results;
//                }
//
//            }, Schedulers.io())
//            .subscribe(new Subscriber<MovieResultsPage>() {
//
//                @Override public void onCompleted() {
//
//                }
//
//                @Override public void onError(Throwable e) {
//                    Timber.e(e, "error:" + e);
//                }
//
//                @Override public void onNext(MovieResultsPage movieResultsPage) {
//                    Timber.e("getting item: size" + movieResultsPage.page);
//                }
//            });

        mMovieService
            .getMovieByPopularity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<PagedResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {
                    Timber.w(e, "error: " + e);
                }

                @Override public void onNext(PagedResponse pagedResponse) {
                    Timber.w("getting item: size" + pagedResponse.page);

                    for (MovieItem item : pagedResponse.movies) {
                        Timber.w("name:" + item.title);
                        Timber.w("utl" + item.posterURL);
                    }

                    mAdapter.addAll(pagedResponse.movies);
                }
            });

        return fragmentView;
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Timber.w("OnClick pos: %d, id %d", position, id);
    }
}
