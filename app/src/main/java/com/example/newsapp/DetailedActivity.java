package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newsapp.Models.NewsHeadlines;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailedActivity extends AppCompatActivity {

    NewsHeadlines headlines;
    TextView text_title, text_published, text_source, text_data;
    ImageView img_news;
    private AdView adView;


    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_d);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        MobileAds.initialize(this);
        adView = findViewById(R.id.adViewD);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        ImageView backbtn = (ImageView) findViewById(R.id.back_arrow_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        headlines = (NewsHeadlines) getIntent().getSerializableExtra("data");

        text_data = findViewById(R.id.text_desc_full);
        text_published = findViewById(R.id.text_desc_published);
        text_source = findViewById(R.id.text_desc_source);
        text_title = findViewById(R.id.text_desc_title);
        img_news = findViewById(R.id.img_news);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;

        try {
            date = dateFormat.parse(headlines.getPublishedAt());

        } catch (Exception e) {
            e.printStackTrace();
        }

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd: HH:mm");
        String dateStr = formatter.format(date);

        text_title.setText(headlines.getTitle());
        text_source.setText(headlines.getSource().getName());
        text_published.setText(dateStr);
        text_data.setText(headlines.getContent());

        Glide.with(this)
                .load(headlines.getUrlToImage())
                .into(img_news);


    }
}