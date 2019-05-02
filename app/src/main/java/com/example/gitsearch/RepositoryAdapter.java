package com.example.gitsearch;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gitsearch.models.SearchResults;

import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {
    private List<SearchResults.Item> repositories;
    private Activity activity;
    private View.OnClickListener repoClick;

    public RepositoryAdapter(Activity activity) {
        this.repositories = new ArrayList<>();
        this.activity = activity;
        repoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepositoryViewHolder holder = (RepositoryViewHolder) v.findViewById(R.id.itemUsername).getTag();
                SearchResults.Item item = repositories.get(holder.getAdapterPosition());
                ((MainFragment.OnListClickListener) RepositoryAdapter.this.activity).onItemSelect(item);
            }
        };
    }

    public void clearItems() {
        repositories.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<SearchResults.Item> items) {
        repositories.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        v.setOnClickListener(repoClick);
        return new RepositoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RepositoryViewHolder repositoryViewHolder, int i) {
        if (i == 0) {
            repositoryViewHolder.hideDivider();
        } else {
            repositoryViewHolder.showDivider();
        }
        SearchResults.Item repository = repositories.get(i);
        repositoryViewHolder.getRepositoryName().setText(repository.getName());
        repositoryViewHolder.getUsername().setText(repository.getOwner().getLogin());
        String url = repository.getOwner().getAvatarUrl();
        repositoryViewHolder.getUsername().setTag(repositoryViewHolder);
        Glide.with(activity)
                .load(url)
                .circleCrop()
                .into(repositoryViewHolder.getUserPhoto());
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView userPhoto;
        private TextView username;
        private TextView repositoryName;
        private View divider;

        public RepositoryViewHolder(@NonNull View itemView) {
            super(itemView);
            userPhoto = itemView.findViewById(R.id.itemUserPhoto);
            username = itemView.findViewById(R.id.itemUsername);
            repositoryName = itemView.findViewById(R.id.itemRepositoryName);
            divider = itemView.findViewById(R.id.itemDivider);
        }

        public ImageView getUserPhoto() {
            return userPhoto;
        }

        public TextView getUsername() {
            return username;
        }

        public TextView getRepositoryName() {
            return repositoryName;
        }

        public void hideDivider() {
            divider.setVisibility(View.GONE);
        }

        public void showDivider() {
            divider.setVisibility(View.VISIBLE);
        }
    }
}
