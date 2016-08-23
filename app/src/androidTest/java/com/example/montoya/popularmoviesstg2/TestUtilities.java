package com.example.montoya.popularmoviesstg2;

import android.content.ContentValues;

import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

/**
 * Created by montoya on 23.08.2016.
 */
public class TestUtilities {


    static ContentValues createFakeMovieValues (Movie movie){


        ContentValues testValues=new ContentValues();
        testValues.put(PopularMoviesContract.MoviesEntry._ID,movie.getId());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE,movie.getTitle());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL,movie.getImageThumbnail());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS,movie.getSysnopsis());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING,movie.getUserRating());
        testValues.put(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE,movie.getReleaseDate());

        return testValues;

    }


}
