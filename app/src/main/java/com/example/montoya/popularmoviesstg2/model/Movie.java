package com.example.montoya.popularmoviesstg2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

/**
 * Created by montoya on 05.05.2016.
 */
public class Movie {
    private long id;
    private String title;
    private String imageThumbnail;
    private String sysnopsis;
    private String userRating;
    private String releaseDate;



    public Movie(long id, String title, String imageThumbnail, String sysnopsis, String userRating, String releaseDate) {
        this.id=id;
        this.title = title;
        this.imageThumbnail = imageThumbnail;
        this.sysnopsis = sysnopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }


    // This constructor receives a Movie ID andthe context and creates a Movie Objects from the database
    // If the user ID is not found in the database it set id= -1L
    public Movie(Context context, long id){

        Cursor cursor;
        Uri uriMovieByID=PopularMoviesContract.MoviesEntry.buildMoviebyIdUri(id);
        cursor=context.getContentResolver().query(uriMovieByID,null,null,null,null);

        if (cursor.getCount()!=1){
            this.id=-1L;
            this.title=null;
            this.imageThumbnail=null;
            this.sysnopsis=null;
            this.userRating=null;
            this.releaseDate=null;

        }else{
            cursor.moveToFirst();
            this.id=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID));
            this.title=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE));
            this.imageThumbnail=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL));
            this.sysnopsis=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS));
            this.userRating=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING));
            this.releaseDate=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE));

        }

        cursor.close();

    }




    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public String getSysnopsis() {
        return sysnopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }



    public ContentValues getMovieValues (){

        ContentValues testValues=new ContentValues();
        testValues.put(PopularMoviesContract.MoviesEntry._ID,this.getId());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE,this.getTitle());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL,this.getImageThumbnail());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS,this.getSysnopsis());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING,this.getUserRating());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE,this.getReleaseDate());

        return testValues;

    }


}
