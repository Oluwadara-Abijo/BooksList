package com.example.hp.bookslist;

/*
Helper methods related to requesting and receiving data from Google Books API
 */

import android.app.LoaderManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class QueryUtils {


    /**
     * Return a Book object by parsing out information
     * about the books from the input bookJSON string
     */
    public static List<Book> extractBooksFromJson(String booksJSON) {
        if (TextUtils.isEmpty(booksJSON)) {
            return null;
        }

        //Create an empty ArrayList that we can add books to
        List<Book> books = new ArrayList<>();

        //Create JSONObject from the JSON response string
        try {
            JSONObject jsonRootObject = new JSONObject(booksJSON);

            //Extract "items" JSONArray
            JSONArray itemsArray = jsonRootObject.optJSONArray("items");

            //Loop through each item in the array
            for (int i = 0; i < itemsArray.length(); i++) {

                //Get book JSONObject at position i
                JSONObject currentBook = itemsArray.optJSONObject(i);

                //Get "volumeInfo" JSONObject
                JSONObject volumeInfo = currentBook.optJSONObject("volumeInfo");

                //Extract "title" for title
                String title = volumeInfo.optString("title");

                //Extract authors
                List<String> authors = new ArrayList<>();
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                if (authorsArray != null) {
                    for (int j = 0; j < authorsArray.length(); j++) {
                        authors.add(authorsArray.getString(j));
                    }
                }

                //Extract "publisher" for publishers
                String publisher = volumeInfo.optString("publisher");

                //Add the new Book to the list of books
                books.add(new Book(title, authors, publisher));
            }

        } catch (JSONException e) {
            Log.e("Query Utils", "Problem parsing the book JSON results", e);
        }

        return books;

    }


}
