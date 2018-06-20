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

    final static String BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/volumes";

    private QueryUtils() {
    }

    //Invoke the API and return a list of Book objects
    public static List<Book> getBooks(String searchWord) {

        //Create a URL object
        URL url = buildUrl(searchWord);

        //Perform HTTP request to the Url and receive a json response
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Extract relevant fields from JSON response and create a Book object
        return extractBooksFromJson(jsonResponse);
    }

    /**
     * Returns a new URL object from given search keyword
     */
    public static URL buildUrl(String searchQuery) {

        URL queryUrl = null;

        Uri builtUri = Uri.parse(BOOKS_BASE_URL)
                .buildUpon()
                .appendQueryParameter("q", searchQuery)
                .build();
        try {
            queryUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return queryUrl;
    }

    /**
     * Make an HTTP request to the given URL and returns a String as response
     */
    public static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = null;

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            //If request was successful (Response code 200)
            //read the input stream and parse the response
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Response code error>>>", "Error response code: "
                        + httpURLConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e("No results", "Problem receiving the JSON results");
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    /**
     * Convert the Input stream into a string which contains
     * the entire JSON response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new
                    InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();

    }

    /**
     * Return an Book object by parsing out information
     * about the books from the input bookJSON string
     */
    private static List<Book> extractBooksFromJson(String booksJSON) {
        if (TextUtils.isEmpty(booksJSON)) {
            return null;
        }

        //Create an empty ArrayList that we can add books to
        List<Book> books = new ArrayList<>();
        Book book = new Book();

        //Create JSONObject from the JSON response string
        try {
            JSONObject jsonRootObject = new JSONObject(booksJSON);

            //Extract "items" JSONArray
            JSONArray itemsArray = jsonRootObject.getJSONArray("items");

            //Loop through each item in the array
            for (int i = 0; i <itemsArray.length(); i++) {
                //Get book JSONObject at position i
                JSONObject currentBook = itemsArray.getJSONObject(i);
                //Get "volumeInfo" JSONObject
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                //Extract "title" for title
                String title = volumeInfo.getString("title");
                book.setTitle(title);

                //Extract "authors" for authors
//                JSONArray authors = currentBook.getJSONArray("authors");
//                String author = authors.getString(0);
                //Extract "publisher" for publishers
                String publisher = currentBook.getString("publisher");
                book.setPublisher(publisher);

                //Create a new Book object with the title, author and
                //publisher from the JSON response
                //book = new Book(title, "As", publisher);

                //Add the new Book to the list of books
                books.add(book);
            }
        } catch (JSONException e) {
            Log.e("Query Utils", "Problem parsing the book JSON results", e );
        }

        return books;

    }


}
