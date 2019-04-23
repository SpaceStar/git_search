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

        final ImageView photo = findViewById(R.id.avatar);
        final ProgressBar progressBar = findViewById(R.id.downloadProgress);
        final Group content = findViewById(R.id.content);

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
                            photo.setImageBitmap(bmImage);
                            progressBar.setVisibility(View.GONE);
                            content.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        TextView name = findViewById(R.id.user);
        TextView repository = findViewById(R.id.repository);

        name.setText("Username: " + getIntent().getStringExtra("userName"));
        repository.setText("Repository: " + getIntent().getStringExtra("repositoryName"));
    }
}
