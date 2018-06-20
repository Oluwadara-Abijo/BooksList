package com.example.hp.bookslist;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Custom constructor whose context is used to inflate the layout file
     * and the list is the data we want to populate into the lists
     *
     * @param context   The current context. It's used to inflate xml
     * @param books     A list of Book objects to display in a list
     */
    public BookAdapter(@NonNull Activity context, @NonNull ArrayList<Book> books) {
        super(context, 0, books);
    }

    /**
     * Provides a view for an AdapterView - a ListView in this case
     * @param position      The position in the list of items that should be displayed
     *                      in the list item view
     * @param convertView   The recycled view to populate
     * @param parent        The parent viewGroup that is inflated
     * @return              The view for the position in the AdapterView
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }

        //Get the Book object located at this position in the list
        Book currentBook = getItem(position);

        //Find the text view with view id title
        TextView titleView = listItemView.findViewById(R.id.book_title_text_view);
        //Display the title of the current book in that text view
        titleView.setText(currentBook.getTitle());

        //Find the text view with view id author
        TextView authorView = listItemView.findViewById(R.id.book_author_text_view);
        //Display the author of the current book in that text view
        authorView.setText(currentBook.getAuthor());

        //Find the text view with view id publisher
        TextView publisherView = listItemView.findViewById(R.id.book_publisher_text_view);
        //Display the publisher of the current book in that text view
        publisherView.setText(currentBook.getPublisher());

        //Return the whole list item layout containing three TextViews
        //so that it can be shown in the ListView
        return listItemView;
    }
}
