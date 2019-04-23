package com.example.gitsearch;

import com.example.gitsearch.models.SearchResults;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class GitHubAPI {
    private GitHubService service;

    public GitHubAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GitHubService.class);
    }

    public GitHubService getService() {
        return service;
    }

    public interface GitHubService {
        @Headers("Accept: application/vnd.github.v3+json")
        @GET("search/repositories?per_page=10")
        Call<SearchResults> searchRepos(@Query("q") String query);
        @Headers("Accept: application/vnd.github.v3+json")
        @GET
        Call<SearchResults> getByUrl(@Url String url);
    }
}
