package com.example.gitsearch;

import android.content.Context;
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

    private ToolbarInterface toolbar;

    private ImageView userPhoto;
    private boolean isFullscreen = false;

    public static InfoFragment getInstance(String username, String repositoryName, String photoUrl) {
        InfoFragment instance = new InfoFragment();

        Bundle args = new Bundle();
        args.putString(EXTRA_USERNAME, username);
        args.putString(EXTRA_REPOSITORY_NAME, repositoryName);
        args.putString(EXTRA_PHOTO_URL, photoUrl);
        instance.setArguments(args);

        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            toolbar = (ToolbarInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ToolbarInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_info, container, false);

        userPhoto = fragment.findViewById(R.id.infoUserPhoto);
        String url = getArguments().getString(EXTRA_PHOTO_URL);
        Glide.with(this)
                .load(url)
                .centerInside()
                .into(userPhoto);

        TextView username = fragment.findViewById(R.id.infoUsername);
        TextView repositoryName = fragment.findViewById(R.id.infoRepositoryName);

        username.setText(getArguments().getString(EXTRA_USERNAME));
        repositoryName.setText(getString(R.string.repository_output, getArguments().getString(EXTRA_REPOSITORY_NAME)));

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maximizePhoto();
            }
        });

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        toolbar.hideToolbar();
    }

    @Override
    public void onStop() {
        super.onStop();
        toolbar.showToolbar();
    }

    public boolean maximizePhoto() {
        if (!isFullscreen) {
            isFullscreen=true;
            ViewGroup.LayoutParams params = userPhoto.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            userPhoto.setLayoutParams(params);
            userPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return true;
        }
        return false;
    }

    public boolean minimizePhoto() {
        if (isFullscreen) {
            isFullscreen=false;
            ViewGroup.LayoutParams params = userPhoto.getLayoutParams();
            params.width = 0;
            params.height = 0;
            userPhoto.setLayoutParams(params);
            userPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return true;
        }
        return false;
    }

    public interface ToolbarInterface {
        void hideToolbar();
        void showToolbar();
    }
}
