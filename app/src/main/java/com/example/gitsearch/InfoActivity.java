package com.example.gitsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class InfoActivity extends AppCompatActivity {
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_REPOSITORY_NAME = "repositoryName";
    public static final String EXTRA_PHOTO_URL = "photoUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ImageView userPhoto = findViewById(R.id.infoUserPhoto);
        String url = getIntent().getStringExtra(EXTRA_PHOTO_URL);
        Glide.with(this)
                .load(url)
                .centerInside()
                .into(userPhoto);

        TextView username = findViewById(R.id.infoUsername);
        TextView repositoryName = findViewById(R.id.infoRepositoryName);

        username.setText(getString(R.string.username_output, getIntent().getStringExtra(EXTRA_USERNAME)));
        repositoryName.setText(getString(R.string.repository_output, getIntent().getStringExtra(EXTRA_REPOSITORY_NAME)));
    }
}
