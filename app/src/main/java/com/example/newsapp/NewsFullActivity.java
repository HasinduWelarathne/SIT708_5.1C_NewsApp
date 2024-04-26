package com.example.newsapp;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;

import java.util.ArrayList;
import java.util.List;

public class NewsFullActivity extends AppCompatActivity {

    WebView webView;
    RecyclerView relatedStoriesRecycleView;
    NewsRecyclerAdapter adapter;
    List<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_full);
        String url = getIntent().getStringExtra("url");
        articleList = (List<Article>) getIntent().getSerializableExtra("relatedArticles");

        webView = findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        relatedStoriesRecycleView = findViewById(R.id.related_stories_recycler_view);
        setupRelatedStoriesRecyclerView();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    void setupRelatedStoriesRecyclerView() {
        if (articleList != null) {
            relatedStoriesRecycleView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new NewsRecyclerAdapter(articleList);
            relatedStoriesRecycleView.setAdapter(adapter);
        }
    }
}
