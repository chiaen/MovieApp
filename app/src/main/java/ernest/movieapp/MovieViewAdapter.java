package ernest.movieapp;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;

import ernest.movieapp.data.model.MovieItem;

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.ViewHolder>
    implements OnItemClickListenerProxy {

    private final Context mContext;
    private final List<MovieItem> mItems;
    private final int mGridWidth;
    private final int mGridHeight;


    private OnItemClickListener mListenerProxy;
    private LayoutInflater mInflater;


    public MovieViewAdapter(Context context, int col) {
        mContext = context;
        mItems = Lists.newArrayList();
        Point point = new Point();
        WindowManager windowManager
            = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);
        this.mGridWidth = point.x / col;
        this.mGridHeight = (int) Math.round((float) mGridWidth * 1.51);
    }

    /**
     * add items to adapter and refresh UI
     *
     * @param items
     */
    public void addAll(Collection<MovieItem> items) {
        synchronized (mItems) {
            mItems.clear();
            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    public MovieItem getItem(int pos) {
        synchronized (mItems) {
            return mItems.get(pos);
        }
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }

        return new ViewHolder(mInflater.inflate(R.layout.movie_poster, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder viewHolder, int position) {
        MovieItem item = mItems.get(position);
        ViewGroup.LayoutParams params = viewHolder.poster.getLayoutParams();
        params.width = mGridWidth;
        params.height = mGridHeight;
        viewHolder.poster.setLayoutParams(params);
        Picasso
            .with(mContext)
            .load(item.getPosterUrl())
            .into(viewHolder.poster);
        viewHolder.bindOnItemClickListener(this);
    }

    @Override public int getItemCount() {
        synchronized (mItems) {
            return mItems.size();
        }
    }

    @Override public void onItemClick(View view, int position) {
        if (mListenerProxy == null) {
            return;
        }
        mListenerProxy.onItemClick(this, view, position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListenerProxy = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView poster;
        OnItemClickListenerProxy listener;

        public ViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            if (listener == null) {
                return;
            }
            listener.onItemClick(v, getLayoutPosition());
        }

        public void bindOnItemClickListener(OnItemClickListenerProxy listener) {
            this.listener = listener;
        }

    }

}