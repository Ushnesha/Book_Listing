package com.example.android.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by darip on 06-02-2018.
 */

public class BookListAdapter extends ArrayAdapter<Book> {

    private List<Book> mBooks;
    private ArrayList<Book> arrayList;

    public BookListAdapter(Context context, List<Book> books){
        super(context, 0, books);
        mBooks = books;
        arrayList = new ArrayList<>();
        arrayList.addAll(mBooks);
    }

    public void setArrayList(List<Book> books){
        arrayList = new ArrayList<>();
        arrayList.addAll(books);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ItemListView = convertView;
        if(ItemListView == null)
            ItemListView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        Book currentBook = getItem(position);
        TextView title = (TextView) ItemListView.findViewById(R.id.item_title);
        TextView author = (TextView) ItemListView.findViewById(R.id.item_author);
        TextView publisher = (TextView) ItemListView.findViewById(R.id.publisher);
        title.setText(currentBook.getTitle());
        String[] auths = currentBook.getAuthors();
        String auth = "";
        for(int i =0; i<auths.length-1; i++)
            auth=auth+auths[i]+",";
        auth=auth+auths[auths.length-1];
        author.setText(auth);
        publisher.setText(currentBook.getPublisher());
        return ItemListView;
    }

    public void filter(String text){
        text = text.toLowerCase(Locale.getDefault());
        mBooks.clear();
        if(text.length() == 0){
            mBooks.addAll(arrayList);
        }else{
            for(Book wp : arrayList){
                for(int i = 0; i < wp.getAuthors().length; i++) {
                    if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(text) || wp.getAuthors()[i].toLowerCase(Locale.getDefault()).contains(text) || wp.getPublisher().toLowerCase(Locale.getDefault()).contains(text)){
                        mBooks.add(wp);
                    }
                }
            }
        }
        Log.e("array list filter", arrayList.toString());
        Log.e("selected books", mBooks.toString());
        notifyDataSetChanged();
    }

}
