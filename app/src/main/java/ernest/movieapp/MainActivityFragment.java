package ernest.movieapp;

import android.content.Intent;
import android.content.res.Configuration;
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

import java.util.ArrayList;

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

    private static final String KEY_ITEMS = "items";

    private ArrayList<MovieItem> mItems;
    private MovieViewAdapter mAdapter;
    private MovieDbService mMovieService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api.themoviedb.org/3")
            .build();
        mMovieService = restAdapter.create(MovieDbService.class);
        setHasOptionsMenu(true);
        // initialize the items list
        mItems = Lists.newArrayList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_ITEMS, Lists.newArrayList(mItems));
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
                        mItems = Lists.newArrayList(pagedResponse.movies);
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
                        mItems = Lists.newArrayList(pagedResponse.movies);
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
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        LinearLayoutManager linearLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearLayoutManager = new GridLayoutManager(getActivity(), 3);
            mAdapter = new MovieViewAdapter(getActivity(), 3);
        } else {
            linearLayoutManager = new GridLayoutManager(getActivity(), 5);
            mAdapter = new MovieViewAdapter(getActivity(), 5);
        }

        RecyclerView listView = (RecyclerView) rootView.findViewById(android.R.id.list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override public void onItemClick(MovieViewAdapter adapter, View view, int position) {
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtras(adapter.getItem(position).to());
                startActivity(detailIntent);
            }
        });

        if (savedInstanceState != null) {
            try {
                mItems = savedInstanceState.getParcelableArrayList(KEY_ITEMS);
            } catch (Throwable ignored) {
                Timber.w(ignored, "exception");
            }
        }

        if (mItems != null && mItems.isEmpty()) {
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
                        mItems = Lists.newArrayList(pagedResponse.movies);
                        mAdapter.addAll(pagedResponse.movies);
                    }
                });
        } else {
            mAdapter.addAll(mItems);
        }

        setHasOptionsMenu(true);

        return rootView;
    }

}
