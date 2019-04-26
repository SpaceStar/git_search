package com.example.gitsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gitsearch.models.SearchResults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RepositoryAdapter content;
    private EditText searchText;
    private ProgressBar progressBar;
    private GitHubAPI api;

    private Toast errorToast;

    private boolean haveMoreItems = false;
    private boolean downloading = false;
    private String nextUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.mainList);
        searchText = findViewById(R.id.mainSearchText);
        progressBar = findViewById(R.id.toolbarProgress);

        errorToast = Toast.makeText(this, "error", Toast.LENGTH_SHORT);

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        content = new RepositoryAdapter(this);
        recyclerView.setAdapter(content);

        api = GitHubAPI.getInstance();
        final Callback<SearchResults> resultsHandler = new Callback<SearchResults>() {
            @Override
            public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<SearchResults.Item> items = response.body().getItems();
                    if (items.isEmpty()) {
                        Toast.makeText(MainActivity.this, "no results found", Toast.LENGTH_SHORT).show();
                    } else {
                        content.addItems(items);
                        String links = response.headers().get("Link");
                        Map<String, String> map = parseLinkHeader(links);
                        if (map.containsKey("next")) {
                            nextUrl = map.get("next");
                            haveMoreItems = true;
                        } else {
                            haveMoreItems = false;
                        }
                    }
                } else {
                    errorToast.show();
                }
                downloading = false;
            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                errorToast.show();
                progressBar.setVisibility(View.GONE);
                downloading = false;
            }
        };

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    haveMoreItems = false;
                    downloading = true;
                    content.clearItems();
                    progressBar.setVisibility(View.VISIBLE);

                    api.getService().searchRepos(searchText.getText().toString()).enqueue(resultsHandler);
                    hideKeyboard(v);
                }
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (!downloading && haveMoreItems) {
                    if ((visibleItemCount+firstVisibleItems) >= totalItemCount - 2) {
                        downloading = true;
                        progressBar.setVisibility(View.VISIBLE);
                        api.getService().getByUrl(nextUrl).enqueue(resultsHandler);
                    }
                }
            }
        });
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private Map<String, String> parseLinkHeader(String string) {
        Map<String, String> map = new HashMap<>();
        if (string != null) {
            String[] links = string.split(", ");
            for (String link : links) {
                String[] pair = link.split("; ");
                String key = pair[1].substring(5, pair[1].length() - 1);
                String value = pair[0].substring(1, pair[0].length() - 1);
                map.put(key, value);
            }
        }
        return map;
    }
}
