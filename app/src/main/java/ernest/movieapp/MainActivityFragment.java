package ernest.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

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
public class MainActivityFragment extends Fragment {

    private List<MovieItem> mItems;
    private MovieViewAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private MovieDbService mMovieService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the items list
        mItems = Lists.newArrayList();
        RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api.themoviedb.org/3")
            .build();
        mMovieService = restAdapter.create(MovieDbService.class);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_popularity) {
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
            return true;
        } else if (id == R.id.sort_highest_rated) {
            mMovieService
                .getMovieByRating()
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
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override public void onItemClick(MovieViewAdapter adapter, View view, int position) {
                Timber.w("pos: %d item: %s", position, adapter.getItem(position).title);
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtras(adapter.getItem(position).to());
                startActivity(detailIntent);
            }
        });

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
                    mAdapter.addAll(pagedResponse.movies);
                }
            });

        return fragmentView;
    }

}
