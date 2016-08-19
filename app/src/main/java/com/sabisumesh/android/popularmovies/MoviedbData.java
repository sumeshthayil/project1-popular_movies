package com.sabisumesh.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sabisumesh on 8/18/2016.
 */
public class MoviedbData implements Parcelable {
    String movieID;
    String posterPath;
    String overview;
    String originalTitle;
    String voteAverage;
    String releaseDate;

    public MoviedbData(){
        movieID = null;
        posterPath = null;
        overview = null;
        originalTitle = null;
        voteAverage = null;
        releaseDate = null;
    }
    private MoviedbData(Parcel in){
        movieID = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        originalTitle = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents(){return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(movieID);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(originalTitle);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<MoviedbData> CREATOR = new Parcelable.Creator<MoviedbData>(){
        @Override
        public MoviedbData createFromParcel(Parcel parcel){
            return new MoviedbData(parcel);
        }

        @Override
        public MoviedbData[] newArray(int size) {
            return new MoviedbData[size];
        }
    };

    public String getMovieID() {
        return movieID;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }
}
