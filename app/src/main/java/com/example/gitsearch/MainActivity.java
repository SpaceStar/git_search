package com.example.gitsearch;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.gitsearch.models.SearchResults;

public class MainActivity extends AppCompatActivity implements
        MainFragment.OnListClickListener, InfoFragment.PhotoClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        MainFragment fragment = new MainFragment();
        transaction.add(R.id.mainContainer, fragment);
        transaction.commit();
    }

    @Override
    public void onItemSelect(SearchResults.Item item) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        InfoFragment fragment = InfoFragment.getInstance(item.getOwner().getLogin(), item.getName(),
                item.getOwner().getAvatarUrl());
        transaction.add(R.id.mainContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(ImageView v) {
        PhotoFragment fragment = PhotoFragment.getInstance(v);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mainContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
