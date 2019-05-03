package com.example.gitsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.gitsearch.models.SearchResults;

public class MainActivity extends AppCompatActivity implements MainFragment.OnListClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        MainFragment fragment = new MainFragment();
        transaction.replace(R.id.mainContainer, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.mainContainer);
        if (fragment instanceof InfoFragment) {
            InfoFragment infoFragment = (InfoFragment)fragment;
            if (infoFragment.minimizePhoto())
                return;
        }
        super.onBackPressed();
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
