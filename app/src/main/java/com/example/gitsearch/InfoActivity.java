package com.example.gitsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        final ImageView userPhoto = findViewById(R.id.infoUserPhoto);
        final ProgressBar progressBar = findViewById(R.id.infoDownloadProgress);
        final Group content = findViewById(R.id.infoContent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = getIntent().getStringExtra("photoUrl");
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    final Bitmap bmImage = BitmapFactory.decodeStream(in);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userPhoto.setImageBitmap(bmImage);
                            progressBar.setVisibility(View.GONE);
                            content.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(InfoActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        TextView name = findViewById(R.id.infoUsername);
        TextView repository = findViewById(R.id.infoRepositoryName);

        name.setText(getString(R.string.username_output, getIntent().getStringExtra("username")));
        repository.setText(getString(R.string.repository_output, getIntent().getStringExtra("repositoryName")));
    }
}
