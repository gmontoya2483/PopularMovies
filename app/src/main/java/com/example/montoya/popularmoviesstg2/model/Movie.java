package com.example.montoya.popularmoviesstg2.model;

import android.content.ContentValues;

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
