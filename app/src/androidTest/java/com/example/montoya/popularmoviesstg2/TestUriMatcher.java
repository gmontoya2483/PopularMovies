package com.example.montoya.popularmoviesstg2;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesProvider;

import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri;
import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.FavoritesEntry.buildFavoriteByIdUri;
import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.MoviesEntry.buildAllMoviesUri;
import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.MoviesEntry.buildMoviebyIdUri;
import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.VideosEntry.buildAllVideosUri;
import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.VideosEntry.buildVideosByKeyUri;
import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.VideosEntry.buildVideosByMovieIdUri;
import static com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract.ReviewsEntry;

/**
 * Created by montoya on 24.08.2016.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static String EXPECTED_ALL_MOVIES_URI= "content://com.example.montoya.popularmovies/movies";
    private static String EXPECTED_MOVIE_BY_ID_URI="content://com.example.montoya.popularmovies/movies/123";

    private static String EXPECTED_ALL_FAVORITES_URI= "content://com.example.montoya.popularmovies/favorites";
    private static String EXPECTED_FAVORITE_BY_ID_URI="content://com.example.montoya.popularmovies/favorites/123";

    private static String EXPECTED_ALL_VIDEO_URI="content://com.example.montoya.popularmovies/videos";
    private static String EXPECTED_VIDEO_WITH_MOVIE_ID_URI="content://com.example.montoya.popularmovies/videos_movies/123";
    private static String EXPECTED_VIDEO_WITH_KEY_URI="content://com.example.montoya.popularmovies/videos/123Key";

    private static String EXPECTED_ALL_REVIEW_URI="content://com.example.montoya.popularmovies/reviews";
    private static String EXPECTED_REVIEW_WITH_MOVIE_ID_URI="content://com.example.montoya.popularmovies/reviews_movies/123";
    private static String EXPECTED_REVIEW_BY_ID_URI="content://com.example.montoya.popularmovies/reviews/123";






    private Uri CONSTRUCTED_ALL_MOVIES_URI;
    private Uri CONSTRUCTED_MOVIE_BY_ID_URI;
    private Uri CONSTRUCTED_ALL_FAVORITES_URI;
    private Uri CONSTRUCTED_FAVORITE_BY_ID_URI;
    private Uri CONSTRUCTED_ALL_VIDEO_URI;
    private Uri CONSTRUCTED_VIDEO_WITH_MOVIE_ID_URI;
    private Uri CONSTRUCTED_VIDEO_WITH_KEY_URI;
    private Uri CONSTRUCTED_ALL_REVIEW_URI;
    private Uri CONSTRUCTED_REVIEW_WITH_MOVIE_ID_URI;
    private Uri CONSTRUCTED_REVIEW_BY_ID_URI;




    @Override
    protected void setUp() throws Exception {

        super.setUp();
        CONSTRUCTED_ALL_MOVIES_URI=buildAllMoviesUri();
        CONSTRUCTED_MOVIE_BY_ID_URI=buildMoviebyIdUri(123L);

        CONSTRUCTED_ALL_FAVORITES_URI=buildAllFavoritesUri();
        CONSTRUCTED_FAVORITE_BY_ID_URI=buildFavoriteByIdUri(123L);

        CONSTRUCTED_ALL_VIDEO_URI=buildAllVideosUri();
        CONSTRUCTED_VIDEO_WITH_KEY_URI=buildVideosByKeyUri("123Key");
        CONSTRUCTED_VIDEO_WITH_MOVIE_ID_URI=buildVideosByMovieIdUri(123L);


        CONSTRUCTED_ALL_REVIEW_URI=ReviewsEntry.buildAllReviewsUri();
        CONSTRUCTED_REVIEW_WITH_MOVIE_ID_URI=ReviewsEntry.buildReviewsByMovieIdUri(123L);
        CONSTRUCTED_REVIEW_BY_ID_URI=ReviewsEntry.buildReviewsById(123L);








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



    public void testVideoUris() throws Throwable{

        //Verify the construction of the ALL Videos URI
        assertEquals("Error: All Videos URI doesn't match",EXPECTED_ALL_VIDEO_URI,CONSTRUCTED_ALL_VIDEO_URI.toString());


        //Verify the construction of the ALL Videos URI by Key
        assertEquals("Error: Video by Key URI doesn't match",EXPECTED_VIDEO_WITH_KEY_URI,CONSTRUCTED_VIDEO_WITH_KEY_URI.toString());


        ////Verify the construction of the ALL Videos URI by Movies
        assertEquals("Error: Videos by movie URI doesn't match",EXPECTED_VIDEO_WITH_MOVIE_ID_URI,CONSTRUCTED_VIDEO_WITH_MOVIE_ID_URI.toString());


    }




    public void testVideoUriMatcher() throws Throwable{

        UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();

        //Verify the VIDEO matcher
        assertEquals("Error: VIDEO URI was matched incorrectly.",testMatcher.match(CONSTRUCTED_ALL_VIDEO_URI), PopularMoviesProvider.VIDEO);


        //Verify the VIDEO_WITH_KEY matcher
        assertEquals("Error: VIDEO by Key URI was matched incorrectly.",testMatcher.match(CONSTRUCTED_VIDEO_WITH_KEY_URI), PopularMoviesProvider.VIDEO_WITH_KEY);

        //Verify the VIDEO_WITH MOVIE_ID matcher
        assertEquals("Error: VIDEO by Movie URI was matched incorrectly.",testMatcher.match(CONSTRUCTED_VIDEO_WITH_MOVIE_ID_URI), PopularMoviesProvider.VIDEO_WITH_MOVIE_ID);



    }


    public void testReviewUris() throws Throwable{

        //Verify the construction of the ALL Videos URI
        assertEquals("Error: All Reviews URI doesn't match",EXPECTED_ALL_REVIEW_URI,CONSTRUCTED_ALL_REVIEW_URI.toString());


        ////Verify the construction of the ALL Videos URI by Movies
        assertEquals("Error: Reviews by movie URI doesn't match",EXPECTED_REVIEW_WITH_MOVIE_ID_URI,CONSTRUCTED_REVIEW_WITH_MOVIE_ID_URI.toString());

        ////Verify the construction of the ALL Videos URI by Movies
        assertEquals("Error: Reviews by ID URI doesn't match",EXPECTED_REVIEW_BY_ID_URI,CONSTRUCTED_REVIEW_BY_ID_URI.toString());





    }



    public void testReviewUriMatcher() throws Throwable{

        UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();

        //Verify the REVIEW matcher
        assertEquals("Error: REVIEW URI was matched incorrectly.",testMatcher.match(CONSTRUCTED_ALL_REVIEW_URI), PopularMoviesProvider.REVIEW);


        //Verify the REVIEW_WITH MOVIE_ID matcher
        assertEquals("Error: REVIEW by Movie URI was matched incorrectly.",testMatcher.match(CONSTRUCTED_REVIEW_WITH_MOVIE_ID_URI), PopularMoviesProvider.REVIEW_WITH_MOVIE_ID);


        //Verify the REVIEW_BY_ID matcher
        assertEquals("Error: REVIEW by ID URI was matched incorrectly.",testMatcher.match(CONSTRUCTED_REVIEW_BY_ID_URI), PopularMoviesProvider.REVIEW_WITH_ID);




    }







}
