package com.sabisumesh.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private PosterImageAdapter posterImageAdapter;

    private ArrayList<MoviedbData> posterList;

//    PosterImage[] posterImages = {
//            new PosterImage(R.drawable.image_1),
//            new PosterImage(R.drawable.image_2),
//            new PosterImage(R.drawable.image_3),
//            new PosterImage(R.drawable.image_4)
//
//    };

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            posterList = new ArrayList<MoviedbData>();
                    //Arrays.asList(new MoviedbData()));
        }
        else {
            posterList = savedInstanceState.getParcelableArrayList("Posters");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Posters",posterList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        posterImageAdapter = new PosterImageAdapter(getActivity(), posterList);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_poster);
        gridView.setAdapter(posterImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //String movie_title = posterImageAdapter.getItem(position).getOriginalTitle();
                MoviedbData movieData = posterImageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("movie details", movieData);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        updateMovies();
    }

    private void updateMovies(){
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //String location = prefs.getString(getString(R.string.pref_location_key),
         //       getString(R.string.pref_location_default));

        moviesTask.execute();
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, MoviedbData[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        MoviedbData[] getMovieDataFromJson(String rawJsonString) throws JSONException {
                        // These are the names of the JSON objects that need to be extracted.
            final String MDB_RESULTS = "results";
            final String MDB_POSTER_PATH = "poster_path";
            final String MDB_OVERVIEW = "overview";
                        final String MDB_RELEASE_DATE = "release_date";
                        final String MDB_ID = "id";
                        final String MDB_ORIGINAL_TITLE = "original_title";
                        final String MDB_VOTE_AVERAGE = "vote_average";

            JSONObject moviedbJson = new JSONObject(rawJsonString);
            JSONArray movieArray = moviedbJson.getJSONArray(MDB_RESULTS);
            MoviedbData[] moviedbData = new MoviedbData[movieArray.length()];

            for(int i = 0; i < movieArray.length(); i++){

                moviedbData[i] = new MoviedbData();
                JSONObject singleMovieJson = movieArray.getJSONObject(i);

                moviedbData[i].setOriginalTitle(singleMovieJson.getString(MDB_ORIGINAL_TITLE));

                moviedbData[i].setMovieID(Long.toString(singleMovieJson.getLong(MDB_ID)));
                moviedbData[i].setOverview(singleMovieJson.getString(MDB_OVERVIEW));
                moviedbData[i].setPosterPath(singleMovieJson.getString(MDB_POSTER_PATH));
                moviedbData[i].setReleaseDate(singleMovieJson.getString(MDB_RELEASE_DATE));
                moviedbData[i].setVoteAverage(singleMovieJson.getString(MDB_VOTE_AVERAGE));
            }
            return moviedbData;
        }
        @Override
        protected MoviedbData[] doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviedbJsonStr = null;

            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort_by = sharedPrefs.getString(
                    getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_order_popularity));

            String app_key = "put_key_here";

            try {

                // Construct the URL for the moviedb query

                final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";

                final String SORT_BY = "sort_by";
                final String APPKEY = "api_key";

                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                                                .appendQueryParameter(SORT_BY, sort_by)
                                                .appendQueryParameter(APPKEY, app_key)
                                                .build();

                URL url = new URL(builtUri.toString());

                // Create the request to MovieDB API    , and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviedbJsonStr = buffer.toString();
            } catch (
                    IOException e
                    )

            {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally{

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(moviedbJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MoviedbData[] result) {
            if (result != null) {
                posterImageAdapter.clear();
                       //for(String dayForecastStr : result) {
                posterImageAdapter.addAll(result);
                                  //  }
                                // New data is back from the server.  Hooray!
            }
        }
    }
}
