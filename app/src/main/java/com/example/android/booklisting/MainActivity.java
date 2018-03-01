package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.LoaderManager.LoaderCallbacks;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>>,SearchView.OnQueryTextListener{

    private TextView mEmptyTextView;
    private ListView BookListView;
    private SearchView searchView;
//    private EditText mBookSearchText;
//    private ImageView search_view;
    private SearchView editSearch;
    private String[] bookNameList;
    private static final String JSON_URL = "https://www.googleapis.com/books/v1/volumes?q=android";
    private static final int BOOK_LOADER_ID = 1;
    private ArrayList<Book> bookArrayList = new ArrayList<>();
    private List<Book> bookList = new ArrayList<>();
    private BookListAdapter mBookListAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BookListView = (ListView) findViewById(R.id.list);

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        BookListView.setEmptyView(mEmptyTextView);

        mBookListAdapter = new BookListAdapter(this, bookList);
//        BookListView.setAdapter(mBookListAdapter);

        searchView=(SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);


        BookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = mBookListAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getInfoLink());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(webIntent);
            }
        });


        ConnectivityManager connMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMan.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            android.app.LoaderManager loaderManager= getLoaderManager();
//        // Create a new {@link ArrayAdapter} of earthquakes
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }else{
            View progressBar = findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_internet);
        }

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookListLoader(this, JSON_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        View progressBar = findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);
        mEmptyTextView.setText(R.string.no_books);

        mBookListAdapter.clear();
        if(books != null && !books.isEmpty()) {
            Log.e("bookList", books.toString());
//            bookArrayList.addAll(books);
            bookList = books;
            mBookListAdapter.setArrayList(books);
            BookListView.setAdapter(mBookListAdapter);
            mBookListAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBookListAdapter.clear();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("text", newText);
        Log.e("bookArrayList", bookList.toString());
        mBookListAdapter.filter(newText);
//        Log.e("selected bookArrayList", mBookListAdapter.filter(newText, bookArrayList).toString());
     //   BookListView.setAdapter(mBookListAdapter);
        return false;
    }
}
