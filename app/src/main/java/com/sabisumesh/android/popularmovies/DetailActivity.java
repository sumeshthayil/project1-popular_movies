package com.sabisumesh.android.popularmovies;

/**
 * Created by sabisumesh on 8/18/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        public DetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            // The detail Activity called via intent.  Inspect the intent for forecast data.
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("movie details")) {
                MoviedbData movieDetails = intent.getParcelableExtra("movie details");
                ((TextView) rootView.findViewById(R.id.original_title_text))
                        .setText(movieDetails.getOriginalTitle());
                ((TextView) rootView.findViewById(R.id.release_date_text))
                        .setText("Released Date: " + movieDetails.getReleaseDate());
                ((TextView) rootView.findViewById(R.id.rating_text))
                        .setText("Rating: " + movieDetails.getVoteAverage()+"/10");
                ((TextView) rootView.findViewById(R.id.overview_text))
                        .setText(movieDetails.getOverview());
                ImageView iconView = (ImageView)rootView.findViewById(R.id.miniPosterView);
                final String POSTERS_BASE_URL =  "https://image.tmdb.org/t/p/w185/";
                Picasso.with(getContext()).load(POSTERS_BASE_URL + movieDetails.getPosterPath()).into(iconView);

            }
            return rootView;
        }
    }
}