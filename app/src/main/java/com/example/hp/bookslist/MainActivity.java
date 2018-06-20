package com.example.hp.bookslist;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    final static String BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/volumes";

    private EditText mSearchEditText;

    int ID = 1;

    private LoaderManager loaderManager;

    private boolean isConnected;

    private ProgressBar mLoadingIndicator;

    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchEditText = findViewById(R.id.search_edit_text);

        Button mSearchButton = findViewById(R.id.search_button);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        ListView bookListView = findViewById(R.id.list);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(mAdapter);

        ConnectivityManager connectivityManager =
                (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        //Start loading
        loaderManager = getSupportLoaderManager();


    }

    public void makeBookSearchQuery(View v) {
        if (isConnected) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            loaderManager.initLoader(ID, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
        }
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        String keyword = mSearchEditText.getText().toString();

        Uri baseUri = Uri.parse(BOOKS_BASE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter("q", keyword);

        String builtUrl = builder.toString();

        String url = BOOKS_BASE_URL + keyword;

        return new BookLoader(this, builtUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mLoadingIndicator.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
