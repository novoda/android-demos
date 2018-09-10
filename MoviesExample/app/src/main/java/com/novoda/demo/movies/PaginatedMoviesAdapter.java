package com.novoda.demo.movies;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.novoda.demo.movies.databinding.MoviesListItemBinding;
import com.novoda.demo.movies.model.Movie;

public class PaginatedMoviesAdapter extends PagedListAdapter<Movie, PaginatedMoviesAdapter.MovieItem> {

    private MoviesAdapter.Listener listener;

    PaginatedMoviesAdapter(MoviesAdapter.Listener listener) {
        super(new MoviesDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MoviesListItemBinding itemBinding = MoviesListItemBinding.inflate(layoutInflater, parent, false);
        return new MovieItem(itemBinding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItem holder, int position) {
        Movie item = getItem(position);
        holder.bind(item);
    }

    static class MovieItem extends RecyclerView.ViewHolder {
        MoviesListItemBinding binding;

        MovieItem(MoviesListItemBinding binding, final MoviesAdapter.Listener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setListener(listener);
        }

        void bind(final Movie movie) {
            binding.setMovie(movie);
            binding.executePendingBindings();
        }
    }

    private static class MoviesDiffCallback extends DiffUtil.ItemCallback<Movie> {

        @Override
        public boolean areItemsTheSame(Movie oldItem, Movie newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(Movie oldItem, Movie newItem) {
            return oldItem.equals(newItem);
        }
    }
}
