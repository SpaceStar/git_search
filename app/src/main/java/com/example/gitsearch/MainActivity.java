package com.example.gitsearch;

import android.os.Bundle;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
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

        Fragment container = manager.findFragmentById(R.id.mainContainer);
        if (container == null) {
            MainFragment fragment = new MainFragment();
            transaction.add(R.id.mainContainer, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.mainContainer), new Fade(Fade.OUT));
        super.onBackPressed();
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
