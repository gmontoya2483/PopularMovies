package com.example.montoya.popularmoviesstg2;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

/**
 * Created by montoya on 23.08.2016.
 */
public class TestUtilities extends AndroidTestCase{


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


    static void VerifyExpectedQueryResults (Movie movie, Cursor cursor){

        int indexId=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID);
        int indexTitle=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE);
        int indexImageThumbnail=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL);
        int indexRelease=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE);
        int indexSysnopsis=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS);
        int indexUserRating=cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING);



        assertEquals("Error: Movie ID doesn´t Match", movie.getId(),cursor.getLong(indexId));
        assertEquals("Error: Movie Title doesn´t Match",movie.getTitle(),cursor.getString(indexTitle));
        assertEquals("Error: Movie Image Thumbnail doesn´t Match",movie.getImageThumbnail(),cursor.getString(indexImageThumbnail));
        assertEquals("Error: Movie Release date doesn´t Match",movie.getReleaseDate(),cursor.getString(indexRelease));
        assertEquals("Error: Movie Sysnopsis doesn´t Match",movie.getSysnopsis(),cursor.getString(indexSysnopsis));
        assertEquals("Error: Movie User Rating doesn´t Match",movie.getUserRating(),cursor.getString(indexUserRating));

    }





}
