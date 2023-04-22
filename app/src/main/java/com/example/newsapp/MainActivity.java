package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.newsapp.Adapter.CustomAdapter;
import com.example.newsapp.Listeners.OnFetchListener;
import com.example.newsapp.Listeners.OnSelectListener;
import com.example.newsapp.Models.NewsHeadlines;
import com.example.newsapp.Models.ResponseApi;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSelectListener, View.OnClickListener {
    RecyclerView recyclerView;
    CustomAdapter adapter;
    Button b1, b2, b3, b4, b5, b6, b7;
    ProgressDialog dialog;
    SearchView searchView;
    String category = "";
    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("News Now");


        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
        startActivity(new Intent(MainActivity.this, DetailedActivity.class)
                .putExtra("data", newsHeadlines));
    }
}