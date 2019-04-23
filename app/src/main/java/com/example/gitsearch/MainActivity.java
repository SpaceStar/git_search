package com.example.gitsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gitsearch.models.SearchResults;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ViewGroup content;
    private EditText searchText;
    private Button buttonSearch;
    private ProgressBar progressBar;
    private GitHubAPI api;

    private Toast errorToast;

    private List<String> userNames;
    private List<String> repoNames;
    private List<String> userPhotos;

    private View.OnClickListener repoClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = findViewById(R.id.searchContent);
        searchText = findViewById(R.id.searchText);
        buttonSearch = findViewById(R.id.buttonSearch);
        progressBar = findViewById(R.id.searchProgress);

        errorToast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);

        api = new GitHubAPI();

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.removeAllViews();
                progressBar.setVisibility(View.VISIBLE);

                api.getService().searchRepos(searchText.getText().toString()).enqueue(new Callback<SearchResults>() {
                    @Override
                    public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            userNames = new ArrayList<>();
                            repoNames = new ArrayList<>();
                            userPhotos = new ArrayList<>();
                            int i = 0;
                            for (SearchResults.Item item : response.body().getItems()) {
                                userNames.add(item.getOwner().getLogin());
                                repoNames.add(item.getName());
                                userPhotos.add(item.getOwner().getAvatarUrl());
                                addButton(item.getName(), i);
                                i++;
                            }
                        } else {
                            errorToast.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResults> call, Throwable t) {
                        errorToast.show();
                    }
                });

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    buttonSearch.callOnClick();
                }
                return false;
            }
        });

        repoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                int index = (int) v.getTag();

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("photoUrl", userPhotos.get(index));
                intent.putExtra("userName", userNames.get(index));
                intent.putExtra("repositoryName", repoNames.get(index));
                startActivity(intent);
            }
        };
    }

    private void addButton(String name, int index) {
        Button button = new Button(this);
        button.setText(name);
        button.setTag(index);
        button.setOnClickListener(repoClick);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ((LinearLayout.LayoutParams) layoutParams).setMargins(0, 8, 0, 8);
        button.setLayoutParams(layoutParams);

        content.addView(button);
    }
}
