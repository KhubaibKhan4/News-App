package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.newsapp.Adapter.CustomAdapter;
import com.example.newsapp.Listeners.OnFetchListener;
import com.example.newsapp.Listeners.OnSelectListener;
import com.example.newsapp.Models.NewsHeadlines;
import com.example.newsapp.Models.ResponseApi;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSelectListener, View.OnClickListener {
    RecyclerView recyclerView;
    CustomAdapter adapter;
    Button b1, b2, b3, b4, b5, b6, b7;
    ProgressDialog dialog;
    SearchView searchView;
    String category = "";
    Toolbar toolbar;
    private AdView adView;
    private InterstitialAd mInterstitial;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("News Now");

        MobileAds.initialize(this);
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        InterstitialAd.load(this, getString(R.string.IntersId), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(MainActivity.this, "" + loadAdError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitial = interstitialAd;

                mInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        mInterstitial = null;
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        mInterstitial = null;
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        mInterstitial = null;
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        mInterstitial = null;
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        mInterstitial = null;
                    }
                });
            }
        });


        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mInterstitial != null) {
                    mInterstitial.show(MainActivity.this);
                } else {
                    Log.d("SearchView_Ads", "AdsPending");
                }

                RequestManager requestManager = new RequestManager(MainActivity.this);
                requestManager.getHeadlines(listener, category, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        b1 = (Button) findViewById(R.id.business);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.entertainment);
        b2.setOnClickListener(this);
        b3 = (Button) findViewById(R.id.general);
        b3.setOnClickListener(this);
        b4 = (Button) findViewById(R.id.health);
        b4.setOnClickListener(this);
        b5 = (Button) findViewById(R.id.science);
        b5.setOnClickListener(this);
        b6 = (Button) findViewById(R.id.sports);
        b6.setOnClickListener(this);
        b7 = (Button) findViewById(R.id.technology);
        b7.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading News ....");
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait, News will be displayed in few Seconds...");
        dialog.show();

        RequestManager requestManager = new RequestManager(this);
        requestManager.getHeadlines(listener, "general", null);
    }

    private final OnFetchListener<ResponseApi> listener = new OnFetchListener<ResponseApi>() {
        @Override
        public void onFetch(List<NewsHeadlines> list, String message) {
            showData(list);
            if (list.isEmpty()) {
                Toast.makeText(MainActivity.this, "No Data Found...", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, "" + message.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private void showData(List<NewsHeadlines> list) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        adapter = new CustomAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (mInterstitial != null) {
            mInterstitial.show(MainActivity.this);
        } else {
            Log.d("SearchView_Ads", "AdsPending");
        }
        Button button = (Button) view;
        String category = button.getText().toString();
        dialog.setTitle("Please Wait, We're Loading Data...");
        dialog.setMessage("News of " + category);
        dialog.show();
        RequestManager requestManager = new RequestManager(this);
        requestManager.getHeadlines(listener, category, null);
    }

    @Override
    public void OnNewsClicked(NewsHeadlines newsHeadlines) {
        if (mInterstitial != null) {
            mInterstitial.show(MainActivity.this);
        } else {
            Log.d("SearchView_Ads", "AdsPending");
        }
        startActivity(new Intent(MainActivity.this, DetailedActivity.class)
                .putExtra("data", newsHeadlines));
    }
}