package com.sabisumesh.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sabisumesh on 8/16/2016.
 */
public class PosterImage implements Parcelable {

    int image; // drawable reference id

//    public PosterImage(int image)
//    {
//        this.image = image;
//    }

    private PosterImage(Parcel in)
    {

        image = in.readInt();
    }

    @Override
    public int describeContents(){return 0;}

    @Override
    public String toString() {
        return Integer.toString(image);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
    }

    public final Parcelable.Creator<PosterImage> CREATOR = new Parcelable.Creator<PosterImage>(){
        @Override
        public PosterImage createFromParcel(Parcel parcel){
            return new PosterImage(parcel);
        }

        @Override
        public PosterImage[] newArray(int size) {
            return new PosterImage[size];
        }
    };
}
