package com.example.montoya.popularmoviesstg2;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesProvider;

import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.MoviesEntry.*;
import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.FavoritesEntry.*;

/**
 * Created by montoya on 24.08.2016.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static String EXPECTED_ALL_MOVIES_URI= "content://com.example.montoya.popularmovies/movies";
    private static String EXPECTED_MOVIE_BY_ID_URI="content://com.example.montoya.popularmovies/movies/123";
    private static String EXPECTED_ALL_FAVORITES_URI= "content://com.example.montoya.popularmovies/favorites";
    private static String EXPECTED_FAVORITE_BY_ID_URI="content://com.example.montoya.popularmovies/favorites/123";

    private Uri CONSTRUCTED_ALL_MOVIES_URI;
    private Uri CONSTRUCTED_MOVIE_BY_ID_URI;
    private Uri CONSTRUCTED_ALL_FAVORITES_URI;
    private Uri CONSTRUCTED_FAVORITE_BY_ID_URI;



    @Override
    protected void setUp() throws Exception {

        super.setUp();
        CONSTRUCTED_ALL_MOVIES_URI=buildAllMoviesUri();
        CONSTRUCTED_MOVIE_BY_ID_URI=buildMoviebyIdUri(123L);
        CONSTRUCTED_ALL_FAVORITES_URI=buildAllFavoritesUri();
        CONSTRUCTED_FAVORITE_BY_ID_URI=buildFavoriteByIdUri(123L);



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


    public void testFavoritesUris() throws Throwable{

        //Verify the construction of the ALL Favorites URI
        assertEquals("Error: All Favorites URI doesn't match",EXPECTED_ALL_FAVORITES_URI,CONSTRUCTED_ALL_FAVORITES_URI.toString());


        //Verify the construction of the Favorites by ID URI
        assertEquals("Error: Favorite by ID URI doesn't match",EXPECTED_FAVORITE_BY_ID_URI,CONSTRUCTED_FAVORITE_BY_ID_URI.toString());


    }


    public void testFavoriteUriMatcher() throws Throwable{

        UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();

        //Verify the FAVIORITE matcher
        assertEquals("Error: FAVORITE URI was matched incorrectly.",testMatcher.match(CONSTRUCTED_ALL_FAVORITES_URI), PopularMoviesProvider.FAVORITE);


        //Verify the FAVORITE_WITH_ID matcher
        assertEquals("Error: FAVORITE by ID URI was matched incorrectly.",testMatcher.match(CONSTRUCTED_FAVORITE_BY_ID_URI), PopularMoviesProvider.FAVORITE_WITH_ID);



    }





}
