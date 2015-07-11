package ernest.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;

import ernest.movieapp.data.model.MovieItem;
import timber.log.Timber;

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.ViewHolder>
    implements OnItemClickListener {

    private final Context mContext;
    private final List<MovieItem> mItems;

    private LayoutInflater mInflater;

    public MovieViewAdapter(Context context) {
        mContext = context;
        mItems = Lists.newArrayList();
    }

    /**
     * add items to adapter and refresh UI
     *
     * @param items
     */
    public void addAll(Collection<MovieItem> items) {
        Timber.w("addAll");
        synchronized (mItems) {
            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void add(MovieItem item) {
        Timber.w("add");
        int oldPosition = 0;
        synchronized (mItems) {
            oldPosition = mItems.size();
            mItems.add(item);
        }
        notifyItemRangeInserted(oldPosition, 1);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        return new ViewHolder(mInflater.inflate(R.layout.movie_poster, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder viewHolder, int position) {
        MovieItem item = mItems.get(position);
        Picasso
            .with(mContext)
            .load(item.getPosterUrl())
            .resize(185, 277)
            .centerCrop()
            .into(viewHolder.poster);
        viewHolder.bindOnItemClickListener(this);
    }

    @Override public int getItemCount() {
        synchronized (mItems) {
            return mItems.size();
        }
    }

    @Override public void onItemClick(View view, int position) {
        Timber.w("position clicked:"+ position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView poster;
        OnItemClickListener listener;

        public ViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            Timber.w("onClick");
            if (listener == null) {
                return;
            }
            Timber.w("onClick onItemClick");
            listener.onItemClick(v, getLayoutPosition());
        }

        public void bindOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

    }

}