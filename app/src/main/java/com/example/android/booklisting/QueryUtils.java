package com.example.android.booklisting;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by darip on 06-02-2018.
 */

public class QueryUtils {

    private static final String TAG = QueryUtils.class.getSimpleName();

    public QueryUtils(){

    }

    public static ArrayList<Book> fetchBookData(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Error closing input stream", e);
        }

        ArrayList<Book> book = extractbookLists(jsonResponse);

        return book;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse="";

        if(url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<Book> extractbookLists(String bookJSON) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonObject = new JSONObject(bookJSON);
            JSONArray items = jsonObject.getJSONArray("items");
            Log.e(TAG, bookJSON);


            for(int i = 0; i < items.length(); i++) {
                JSONObject f = items.getJSONObject(i);
                JSONObject vol = f.getJSONObject("volumeInfo");
                String title = vol.getString("title");
                JSONArray authors = vol.getJSONArray("authors");
                String[] authorsList = new String[authors.length()];
                for(int j = 0; j < authors.length(); j++){
                    String list = authors.getString(j);
                    if(list != null)
                        authorsList[j] = list;
                }
                String publisher = vol.getString("publisher");
                String infoUrl = vol.getString("infoLink");

                Log.e(TAG, title + " , " + authorsList.toString() + " , " + publisher);

                books.add(new Book(title, authorsList, publisher, infoUrl));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of earthquakes
        return books;
    }

}
