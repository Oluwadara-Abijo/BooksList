package com.example.hp.bookslist;

import java.util.List;

public class Book {

    //Title of the book
    private String mTitle;

    //Book author
    private List<String> mAuthors;

    //Book publisher
    private String mPublisher;

    /**
    * Constructor which creates an object of the book class
    * @param title is the title of the book
    * @param authors is the book authors
    * @param publisher is the book publisher
     */
    public Book(String title, List<String> authors, String publisher) {
        mTitle = title;
        mAuthors = authors;
        mPublisher = publisher;
    }

    //Get the book title
    public String getTitle() {
        return mTitle;
    }

    //Get the book author
    public List<String> getAuthor() {
        return mAuthors;
    }

    //Get the book publisher

    public String getPublisher() {
        return mPublisher;
    }

    public void setAuthor(List<String> mAuthor) {
        this.mAuthors = mAuthor;
    }

    public void setPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}


