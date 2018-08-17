package com.example.hp.bookslist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchEditText;

    private TextView mErrorMessageTextView;

    private TextView mEmptyKeywordWarning;

    private ProgressBar mLoadingIndicator;

    private List<Book> mList;

    private ListView mBooksListView;

    private ViewGroup mRootView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = new ArrayList<>();

        mSearchEditText = findViewById(R.id.search_edit_text);

        mErrorMessageTextView = findViewById(R.id.tv_error_message);

        mEmptyKeywordWarning = findViewById(R.id.empty_keyword_warning);

        mRootView = findViewById(R.id.mainLayout);

        mLoadingIndicator = findViewById(R.id.loading_indicator);

        mBooksListView = findViewById(R.id.list);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void makeBookSearchQuery(View v) {
        if (!isNetworkAvailable()) {
            showError();
            mErrorMessageTextView.setText(R.string.network_error_message);
        } else {
            boolean visible = false;
            String searchQuery = mSearchEditText.getText().toString();
            if (searchQuery.equals("")) {
                TransitionManager.beginDelayedTransition(mRootView);
                visible = !visible;
                mEmptyKeywordWarning.setVisibility(visible ? View.VISIBLE : View.GONE);
            } else {
                mEmptyKeywordWarning.setVisibility(View.GONE);
                URL queryURL = NetworkUtils.buildUrl(searchQuery);
                Log.d(">>>", "Url: " + queryURL);
                new BooksAsyncTask().execute(queryURL);
            }
        }
    }

    public void showData() {
        mBooksListView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.GONE);
    }

    public void showError() {
        mBooksListView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    public class BooksAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String booksSearchResults = null;
            try {
                booksSearchResults = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return booksSearchResults;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result != null && !result.equals("")) {
                mList = QueryUtils.extractBooksFromJson(result);
                BookAdapter mAdapter = new BookAdapter(MainActivity.this, (ArrayList<Book>) mList);
                mBooksListView.setAdapter(mAdapter);
                showData();
            } else {
                showError();
            }
        }
    }

}
