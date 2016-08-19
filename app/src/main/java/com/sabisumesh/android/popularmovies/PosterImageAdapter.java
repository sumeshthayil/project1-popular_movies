package com.sabisumesh.android.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sabisumesh on 8/16/2016.
 */
public class PosterImageAdapter extends ArrayAdapter<MoviedbData> {
    private static final String LOG_TAG = PosterImageAdapter.class.getSimpleName();

    public PosterImageAdapter(Activity context, List<MoviedbData> posterImages)
    {
        super(context,0,posterImages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        MoviedbData posterImage = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_poster, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.grid_item_poster_img);

        final String POSTERS_BASE_URL =  "https://image.tmdb.org/t/p/w185/";
        Picasso.with(getContext()).load(POSTERS_BASE_URL + posterImage.getPosterPath() ).into(iconView);
        //iconView.setImageResource(posterImage.image);

        return convertView;
    }
}
