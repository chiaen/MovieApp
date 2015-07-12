package ernest.movieapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ernest.movieapp.data.model.MovieItem;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        PlaceholderFragment placeholderFragment = new PlaceholderFragment();
        placeholderFragment.setArguments(getIntent().getExtras());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, placeholderFragment)
                .commit();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.detail_screen, container, false);

            TextView view = (TextView) rootView.findViewById(android.R.id.text1);
            MovieItem item = MovieItem.from(this.getArguments());
            view.setText(item.title);

            ImageView poster = (ImageView) rootView.findViewById(R.id.image);
            Picasso
                .with(rootView.getContext())
                .load(item.getPosterUrl())
                .resize(144, 215)
                .centerCrop()
                .into(poster);

            TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
            releaseDate.setText(item.date);
            TextView useRating = (TextView) rootView.findViewById(R.id.user_rating);
            useRating.setText(String.format("%.1f / 10", item.rating));

            TextView overView = (TextView) rootView.findViewById(R.id.movie_overview);
            overView.setText(item.overview);

            return rootView;
        }
    }
}