package com.example.gitsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class InfoFragment extends Fragment {
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_REPOSITORY_NAME = "repositoryName";
    public static final String EXTRA_PHOTO_URL = "photoUrl";

    public static InfoFragment getInstance(String username, String repositoryName, String photoUrl) {
        InfoFragment instance = new InfoFragment();

        Bundle args = new Bundle();
        args.putString(EXTRA_USERNAME, username);
        args.putString(EXTRA_REPOSITORY_NAME, repositoryName);
        args.putString(EXTRA_PHOTO_URL, photoUrl);
        instance.setArguments(args);

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_info, container, false);

        ImageView userPhoto = fragment.findViewById(R.id.infoUserPhoto);
        String url = getArguments().getString(EXTRA_PHOTO_URL);
        Glide.with(this)
                .load(url)
                .centerInside()
                .into(userPhoto);

        TextView username = fragment.findViewById(R.id.infoUsername);
        TextView repositoryName = fragment.findViewById(R.id.infoRepositoryName);

        username.setText(getString(R.string.username_output, getArguments().getString(EXTRA_USERNAME)));
        repositoryName.setText(getString(R.string.repository_output, getArguments().getString(EXTRA_REPOSITORY_NAME)));

        return fragment;
    }
}
