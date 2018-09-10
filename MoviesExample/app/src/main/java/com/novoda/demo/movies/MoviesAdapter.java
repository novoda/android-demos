package com.novoda.demo.movies;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.novoda.demo.movies.databinding.MoviesListCardBinding;
import com.novoda.demo.movies.databinding.MoviesListItemBinding;
import com.novoda.demo.movies.model.Movie;

public class MoviesAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {

    private static final int MOVIE_ITEM = 0;
    private static final int NEXT_PAGE_ITEM = 1;

    private NetworkStatus networkStatus = null;

    public interface Listener {

        void onMovieSelected(Movie movie);
    }

    private Listener listener;

    MoviesAdapter(Listener listener) {
        super(new MoviesDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == MOVIE_ITEM) {
            MoviesListItemBinding itemBinding = MoviesListItemBinding.inflate(layoutInflater, parent, false);
            return new MovieItem(itemBinding, listener);
        } else {
            MoviesListCardBinding itemBinding = MoviesListCardBinding.inflate(layoutInflater, parent, false);
            return new LoadPageItem(itemBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieItem) {
            Movie item = getItem(position);
            ((MovieItem) holder).bind(item);
        } else {
            ((LoadPageItem) holder).bind();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return NEXT_PAGE_ITEM;
        } else {
            return MOVIE_ITEM;
        }
    }

    private boolean hasExtraRow() {
        return networkStatus != null && networkStatus != NetworkStatus.LOADED;
    }

    public void setNetworkStatus(NetworkStatus newNetworkStatus) {
        NetworkStatus previousState = this.networkStatus;
        boolean didShowLoading = hasExtraRow();
        this.networkStatus = newNetworkStatus;
        boolean willShowLoading = hasExtraRow();
        if (didShowLoading != willShowLoading) {
            if (didShowLoading) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (willShowLoading && previousState != newNetworkStatus) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    static class MovieItem extends RecyclerView.ViewHolder {
        MoviesListItemBinding binding;

        MovieItem(MoviesListItemBinding binding, final Listener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setListener(listener);
        }

        void bind(final Movie movie) {
            binding.setMovie(movie);
            binding.executePendingBindings();
        }
    }

    static class LoadPageItem extends RecyclerView.ViewHolder {

        MoviesListCardBinding binding;

        LoadPageItem(MoviesListCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            Movie movie = new Movie();
            movie.title = "Loading next page";

            binding.setMovie(movie);
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
