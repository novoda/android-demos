package com.novoda.demo.movies;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.novoda.demo.movies.databinding.MoviesListCardBinding;
import com.novoda.demo.movies.databinding.MoviesListItemBinding;
import com.novoda.demo.movies.model.Movie;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MOVIE_ITEM = 0;
    private static final int NEXT_PAGE_ITEM = 1;

    private MoviesSate moviesSate = new MoviesSate(new ArrayList<Movie>(), 0);

    public interface Listener {
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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == NEXT_PAGE_ITEM) {
            MoviesListCardBinding itemBinding = MoviesListCardBinding.inflate(layoutInflater, parent, false);
            return new LoadPageItem(itemBinding, listener);
        }

        MoviesListItemBinding itemBinding = MoviesListItemBinding.inflate(layoutInflater, parent, false);

        return new MovieItem(itemBinding, listener);
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

        MoviesListItemBinding binding;

        MovieItem(MoviesListItemBinding binding, final Listener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setListener(listener);

        }

        public void bind(final Movie movie) {
            binding.setMovie(movie);
            binding.executePendingBindings();
        }
    }

    static class LoadPageItem extends RecyclerView.ViewHolder {

        MoviesListCardBinding binding;
        Listener listener;

        LoadPageItem(MoviesListCardBinding binding, Listener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
        }

        public void bind(final int page) {
            Movie movie = new Movie();
            movie.title = "Loading page " + page;

            binding.setMovie(movie);
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
