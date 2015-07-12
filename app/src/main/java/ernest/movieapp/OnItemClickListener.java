package ernest.movieapp;

import android.view.View;

public interface OnItemClickListener {

    void onItemClick(MovieViewAdapter adapter, View view, int position);

}