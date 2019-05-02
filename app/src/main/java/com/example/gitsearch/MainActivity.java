package com.example.gitsearch;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.gitsearch.models.SearchResults;

public class MainActivity extends AppCompatActivity implements
        MainFragment.ProgressInterface, MainFragment.OnListClickListener {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.toolbarProgress);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        MainFragment fragment = new MainFragment();
        transaction.replace(R.id.mainContainer, fragment);
        transaction.commit();
    }

    @Override
    public void startProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelect(SearchResults.Item item) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        InfoFragment fragment = InfoFragment.getInstance(item.getOwner().getLogin(), item.getName(),
                item.getOwner().getAvatarUrl());
        transaction.replace(R.id.mainContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
