package com.example.montoya.popularmoviesstg2;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesProvider;

import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.MoviesEntry.*;

/**
 * Created by montoya on 24.08.2016.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static String EXPECTED_ALL_MOVIES_URI= "content://com.example.montoya.popularmovies/movies";
    private static String EXPECTED_MOVIE_BY_ID_URI="content://com.example.montoya.popularmovies/movies/123";

    private Uri CONSTRUCTED_ALL_MOVIES_URI;
    private Uri CONSTRUCTED_MOVIE_BY_ID_URI;



    @Override
    protected void setUp() throws Exception {

        super.setUp();
        CONSTRUCTED_ALL_MOVIES_URI=buildAllMoviesUri();
        CONSTRUCTED_MOVIE_BY_ID_URI=buildMoviebyIdUri(123L);



    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }



    public void testMovieUris() throws Throwable{

        //Verify the construction of the ALL Movies URI
        assertEquals("Error: All Movies URI doesn't match",EXPECTED_ALL_MOVIES_URI,CONSTRUCTED_ALL_MOVIES_URI.toString());


        //Verify the construction of the ALL Movies URI
        assertEquals("Error: Movie by ID URI doesn't match",EXPECTED_MOVIE_BY_ID_URI,CONSTRUCTED_MOVIE_BY_ID_URI.toString());


    }


    public void testMovieUriMatcher() throws Throwable{

        UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();

        //Verify the MOVIE matcher
        assertEquals("Error: MOVIES URI was matched incorrectly.",testMatcher.match(CONSTRUCTED_ALL_MOVIES_URI), PopularMoviesProvider.MOVIE);


        //Verify the MOVIE_WITH_ID matcher
        assertEquals("Error: MOVIE by ID URI was matched incorrectly.",testMatcher.match(CONSTRUCTED_MOVIE_BY_ID_URI), PopularMoviesProvider.MOVIE_WITH_ID);



    }





}
