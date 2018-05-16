package com.novoda.demo.movies;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.novoda.demo.movies.MoviesSate;
import com.novoda.demo.movies.model.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MOVIE_ITEM = 0;
    private static final int NEXT_PAGE_ITEM = 1;

    private MoviesSate moviesSate = new MoviesSate(new ArrayList<Movie>(), 0);

    interface Listener {
        void onMovieSelected(Movie movie);

        void onPageLoadRequested(int page);
    }

    private final Listener listener;

    public MoviesAdapter(Listener listener) {
        this.listener = listener;
        setHasStableIds(true);
    }

    public void setMoviesSate(MoviesSate moviesSate) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MoviesDiffCallback(this.moviesSate, moviesSate), true);
        this.moviesSate = moviesSate;
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_card, parent, false);
        if (viewType == 1) {
            return new LoadPageItem(view, listener);
        }
        return new MovieItem(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieItem) {
            ((MovieItem) holder).bind(moviesSate.get(position));
        } else {
            ((LoadPageItem) holder).bind(moviesSate.pageNumber());
        }
    }

    @Override
    public int getItemCount() {
        return moviesSate.size() + 1;
    }

    @Override
    public int getItemViewType(final int position) {
        return moviesSate.size() == position ? NEXT_PAGE_ITEM : MOVIE_ITEM;
    }

    @Override
    public long getItemId(final int position) {
        if (position == moviesSate.size()) {
            return Long.MAX_VALUE;
        }
        return moviesSate.get(position).id.hashCode();
    }

    class MovieItem extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_item_title) TextView text;
        @BindView(R.id.movie_item_poster) ImageView poster;
        @BindView(R.id.movie_item_rating) TextView rating;

        MovieItem(View itemView, final Listener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != -1) {
                        listener.onMovieSelected(moviesSate.get(adapterPosition));
                    }
                }
            });
        }

        public void bind(final Movie movie) {
            text.setText(movie.title);
            rating.setText(Double.toString(movie.rating));
            Glide.with(itemView.getContext()).load(movie.posterUrl()).into(poster);
        }
    }

    static class LoadPageItem extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_item_title) TextView text;
        Listener listener;

        LoadPageItem(View itemView, Listener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        public void bind(final int page) {
            text.setText("Loading page " + page);
            listener.onPageLoadRequested(page);
        }
    }

    private static class MoviesDiffCallback extends DiffUtil.Callback {

        private final MoviesSate oldMovies;
        private final MoviesSate newMovies;

        public MoviesDiffCallback(MoviesSate oldMovies, MoviesSate newMovies) {
            this.oldMovies = oldMovies;
            this.newMovies = newMovies;
        }

        @Override
        public int getOldListSize() {
            return oldMovies.size() + 1;
        }

        @Override
        public int getNewListSize() {
            return newMovies.size() + 1;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            if (oldItemPosition == oldMovies.size()) {
                return false;
            }
            if (newItemPosition == newMovies.size()) {
                return false;
            }
            return oldMovies.get(oldItemPosition).id.equals(newMovies.get(newItemPosition).id);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            if (oldItemPosition == oldMovies.size()) {
                return false;
            }
            if (newItemPosition == newMovies.size()) {
                return false;
            }
            return oldMovies.get(oldItemPosition).equals(newMovies.get(newItemPosition));
        }
    }

}
