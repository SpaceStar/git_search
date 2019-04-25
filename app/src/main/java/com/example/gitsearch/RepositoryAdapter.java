package com.example.gitsearch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gitsearch.models.SearchResults;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {
    private List<SearchResults.Item> repositories;
    private List<Bitmap> photos;
    private final Activity mActivity;
    private View.OnClickListener repoClick;

    public RepositoryAdapter(Activity activity) {
        this.repositories = new ArrayList<>();
        this.photos = new ArrayList<>();
        this.mActivity = activity;
        repoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView photo = v.findViewById(R.id.userPhoto);
                TextView username = v.findViewById(R.id.username);
                TextView repositoryName = v.findViewById(R.id.repositoryName);

                Intent intent = new Intent(mActivity.getApplicationContext(), InfoActivity.class);
                intent.putExtra("photoUrl", (String) photo.getTag());
                intent.putExtra("userName", username.getText());
                intent.putExtra("repositoryName", repositoryName.getText());
                mActivity.startActivity(intent);
            }
        };
    }

    public void clearItems() {
        repositories.clear();
        photos.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<SearchResults.Item> items) {
        repositories.addAll(items);
        for (int i = 0; i < items.size(); i++)
            photos.add(null);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        v.setOnClickListener(repoClick);
        RepositoryViewHolder repositoryViewHolder = new RepositoryViewHolder(v);
        return repositoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RepositoryViewHolder repositoryViewHolder, int i) {
        if (i == 0) {
            repositoryViewHolder.divider.setVisibility(View.GONE);
        } else {
            repositoryViewHolder.divider.setVisibility(View.VISIBLE);
        }
        repositoryViewHolder.getRepositoryName().setText(repositories.get(i).getName());
        repositoryViewHolder.getUsername().setText(repositories.get(i).getOwner().getLogin());
        repositoryViewHolder.getUserPhoto().setImageResource(android.R.color.transparent);
        final String url = repositories.get(i).getOwner().getAvatarUrl();
        repositoryViewHolder.getUserPhoto().setTag(url);

        boolean downloaded = false;
        final Bitmap bmImage = photos.get(i);
        if (bmImage != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    repositoryViewHolder.getUserPhoto().setImageBitmap(bmImage);
                }
            });
            downloaded = true;
        }
        if (!downloaded) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream in = new java.net.URL(url).openStream();
                        final Bitmap bmImage = BitmapFactory.decodeStream(in);
                        photos.set(repositoryViewHolder.getAdapterPosition(), bmImage);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                repositoryViewHolder.getUserPhoto().setImageBitmap(bmImage);
                            }
                        });
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                }
            }).start();
        }
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
            userPhoto = itemView.findViewById(R.id.userPhoto);
            username = itemView.findViewById(R.id.username);
            repositoryName = itemView.findViewById(R.id.repositoryName);
            divider = itemView.findViewById(R.id.divider);
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
    }
}
