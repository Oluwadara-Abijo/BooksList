package com.example.hp.bookslist;

public class Book {

    //Title of the book
    private String mTitle;

    //Book author
    private String mAuthor;

    //Book publisher
    private String mPublisher;

    /**
    * Constructor which creates an object of the book class
    * @param title is the title of the book
    * @param author is the book author
    * @param publisher is the book publisher
     */
//    public Book(String title, String author, String publisher) {
//        mTitle = title;
//        mAuthor = author;
//        mPublisher = publisher;
//    }

    //Get the book title
    public String getTitle() {
        return mTitle;
    }

    //Get the book author
    public String getAuthor() {
        return mAuthor;
    }

    //Get the book publisher

    public String getPublisher() {
        return mPublisher;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public void setPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}


