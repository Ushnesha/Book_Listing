package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by darip on 06-02-2018.
 */

public class BookListLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;
    private static final String TAG = BookListLoader.class.getSimpleName();


    public BookListLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    public List<Book> loadInBackground() {
        return QueryUtils.fetchBookData(mUrl);

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
