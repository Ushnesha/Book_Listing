package com.example.android.booklisting;

/**
 * Created by darip on 06-02-2018.
 */

public class Book {

    private String title, publisher, infoLink;
    private String[] authors;

    public Book(String title, String[] author, String publisher, String infoLink){
        this.title = title;
        this.publisher = publisher;
        this.infoLink = infoLink;
        this.authors = author;
    }

    public String getTitle(){
        return title;
    }

    public String[] getAuthors(){
        return authors;
    }

    public String getPublisher(){
        return publisher;
    }

    public String getInfoLink(){
        return infoLink;
    }

    public void setTitle(String t){
        this.title = t;
    }
    public void setAuthors(String[] a){
        this.authors = a;
    }

    public void setInfoLink(String i){
        this.infoLink = i;
    }

    public void setPublisher(String p){
        this.publisher = p;
    }
}
