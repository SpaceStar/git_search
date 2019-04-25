package com.example.gitsearch;

import com.example.gitsearch.models.SearchResults;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class GitHubAPI {
    private final String BASE_API_URL = "https://api.github.com/";

    private static GitHubAPI instance;

    private GitHubService service;

    private GitHubAPI() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(GitHubService.class);
    }

    public static synchronized GitHubAPI getInstance() {
        if (instance == null) {
            instance = new GitHubAPI();
        }
        return instance;
    }

    public GitHubService getService() {
        return service;
    }

    public interface GitHubService {
        @GET("search/repositories?per_page=10")
        Call<SearchResults> searchRepos(@Query("q") String query);

        @GET
        Call<SearchResults> getByUrl(@Url String url);
    }

    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            request = request.newBuilder()
                    .header("Accept", "application/vnd.github.v3+json")
                    .build();

            Response response = chain.proceed(request);
            return response;
        }
    }
}
