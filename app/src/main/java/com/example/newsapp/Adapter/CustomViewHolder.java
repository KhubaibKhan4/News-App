package com.example.newsapp.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;


public class CustomViewHolder extends RecyclerView.ViewHolder {
    ImageView news_image;
    TextView news_title, news_source;
    CardView cardView;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);

        news_image = itemView.findViewById(R.id.img_staggerd_layout);
        news_title = itemView.findViewById(R.id.news_title_staggered);
        news_source = itemView.findViewById(R.id.news_source_staggered);
        cardView = itemView.findViewById(R.id.main_container_staggered);
    }
}
