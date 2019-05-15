package com.example.gitsearch;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.gitsearch.databinding.ListItemBinding;
import com.example.gitsearch.models.SearchResults;

import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {
    private List<SearchResults.Item> repositories;
    private Activity activity;

    public RepositoryAdapter(Activity activity) {
        this.repositories = new ArrayList<>();
        this.activity = activity;
    }

    public void clearItems() {
        repositories.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<SearchResults.Item> items) {
        repositories.addAll(items);
        notifyDataSetChanged();
    }

    public List<SearchResults.Item> getItems() {
        return repositories;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ListItemBinding binding = ListItemBinding.inflate(inflater, viewGroup, false);
        binding.setClickListener((MainFragment.OnListClickListener)activity);
        return new RepositoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RepositoryViewHolder repositoryViewHolder, int i) {
        repositoryViewHolder.bind(repositories.get(i), i);
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding binding;

        public RepositoryViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SearchResults.Item repository, int i) {
            binding.setRepository(repository);
            binding.setPosition(i);
            binding.executePendingBindings();
        }
    }
}
