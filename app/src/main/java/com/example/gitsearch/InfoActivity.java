package com.example.gitsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ImageView userPhoto = findViewById(R.id.infoUserPhoto);
        String url = getIntent().getStringExtra("photoUrl");
        Glide.with(this)
                .load(url)
                .centerInside()
                .into(userPhoto);

        TextView name = findViewById(R.id.infoUsername);
        TextView repository = findViewById(R.id.infoRepositoryName);

        name.setText(getString(R.string.username_output, getIntent().getStringExtra("username")));
        repository.setText(getString(R.string.repository_output, getIntent().getStringExtra("repositoryName")));
    }
}
