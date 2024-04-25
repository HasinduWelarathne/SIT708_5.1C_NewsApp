package com.example.newsapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.news_recycler_view);
        progressIndicator = findViewById(R.id.progress_bar);
        searchView = findViewById(R.id.search_view);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);


        if (searchView != null) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getNews("GENERAL", query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        setupRecyclerView();
        getNews("GENERAL", null);
    }

    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    void changeInProgress(boolean show) {
        if (show)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }

    void getNews(String category, String query) {
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("6bbf7a88d1b74ec38ca0162da57d544a");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category(category)
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        runOnUiThread(() -> {
                            changeInProgress(false);
                            articleList = response.getArticles();
                            if (adapter != null) {
                                adapter.updateData(articleList);
                                adapter.notifyDataSetChanged();
                            }else {
                                Log.e("MainActivity", "Adapter is null");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("Got Failure", throwable.getMessage());
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        Button btn =(Button) v;
        String category = btn.getText().toString();
        getNews(category,null);
    }
}
