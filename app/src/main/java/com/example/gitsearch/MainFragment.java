package com.example.gitsearch;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.gitsearch.databinding.ToolbarBinding;
import com.example.gitsearch.models.SearchResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class MainFragment extends Fragment {
    private static final String KEY_CONTENT = "CONTENT";
    private static final String KEY_MORE_ITEMS = "MORE_ITEMS";
    private static final String KEY_NEXT_URL = "NEXT_URL";

    private RepositoryAdapter content;
    private TextInputLayout searchTextLayout;
    private TextInputEditText searchText;
    private GitHubAPI api;

    private Toast errorToast;

    private boolean haveMoreItems = false;
    private ObservableBoolean downloading = new ObservableBoolean(false);
    private String nextUrl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            OnListClickListener listener = (OnListClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnListClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorToast = Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT);
        content = new RepositoryAdapter(getActivity());
        api = GitHubAPI.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_main, container, false);

        Toolbar toolbar = fragment.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ToolbarBinding.bind(toolbar).setDownloading(downloading);

        RecyclerView recyclerView = fragment.findViewById(R.id.mainList);
        searchTextLayout = fragment.findViewById(R.id.mainSearchTextLayout);
        searchText = fragment.findViewById(R.id.mainSearchText);

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(content);

        if (savedInstanceState != null) {
            List<SearchResults.Item> list = savedInstanceState.getParcelableArrayList(KEY_CONTENT);
            if (list != null)
                content.addItems(list);
            haveMoreItems = savedInstanceState.getBoolean(KEY_MORE_ITEMS, false);
            nextUrl = savedInstanceState.getString(KEY_NEXT_URL);
        }

        final Callback<SearchResults> resultsHandler = new Callback<SearchResults>() {
            @Override
            public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                if (response.isSuccessful()) {
                    List<SearchResults.Item> items = response.body().getItems();
                    if (items.isEmpty()) {
                        Toast.makeText(getActivity(), getString(R.string.no_results_found), Toast.LENGTH_SHORT).show();
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
                downloading.set(false);
            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                errorToast.show();
                downloading.set(false);
            }
        };

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER &&
                        !downloading.get()) {
                    if (searchText.getText().toString().equals("")) {
                        searchTextLayout.setError(getString(R.string.empty_search_string));
                        return true;
                    }
                    haveMoreItems = false;
                    downloading.set(true);
                    content.clearItems();

                    api.getService().searchRepos(searchText.getText().toString()).enqueue(resultsHandler);
                    hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchTextLayout.setError(null);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (!downloading.get() && haveMoreItems) {
                    if ((visibleItemCount+firstVisibleItems) >= totalItemCount - 2) {
                        downloading.set(true);
                        api.getService().getByUrl(nextUrl).enqueue(resultsHandler);
                    }
                }
            }
        });

        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_CONTENT, new ArrayList<Parcelable>(content.getItems()));
        outState.putBoolean(KEY_MORE_ITEMS, haveMoreItems);
        outState.putString(KEY_NEXT_URL, nextUrl);
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
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

    public interface OnListClickListener {
        void onItemSelect(SearchResults.Item item);
    }
}
