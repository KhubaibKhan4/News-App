package com.example.newsapp;

import android.content.Context;
import android.widget.Toast;

import com.example.newsapp.Listeners.OnFetchListener;
import com.example.newsapp.Models.NewsHeadlines;
import com.example.newsapp.Models.ResponseApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getHeadlines(OnFetchListener listener, String category, String query) {
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        Call<ResponseApi> call = callNewsApi.callHeadlines("us", category, query, context.getString(R.string.api_key));

        try {
            call.enqueue(new Callback<ResponseApi>() {
                @Override
                public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "" + response.code(), Toast.LENGTH_SHORT).show();
                    }

                    listener.onFetch(response.body().getArticles(), response.message());
                }

                @Override
                public void onFailure(Call<ResponseApi> call, Throwable t) {
                    Toast.makeText(context, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RequestManager(Context context) {
        this.context = context;
    }

    public interface CallNewsApi {
        @GET("top-headlines")
        Call<ResponseApi> callHeadlines(
                @Query("country") String country,
                @Query("category") String category,
                @Query("q") String query,
                @Query("apiKey") String apiKey);

    }
}
