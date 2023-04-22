package com.example.newsapp.Listeners;

import com.example.newsapp.Models.NewsHeadlines;

import java.util.List;

public interface OnFetchListener<ResponseApi> {

    void onFetch(List<NewsHeadlines> list, String message);

    void onError(String message);
}
