package com.example.montoya.popularmoviesstg2;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesProvider;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by montoya on 25.08.2016.
 */
public class TestMovieClass extends AndroidTestCase{

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        TestUtilities.deleteAllMoviesRecords(mContext);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestUtilities.deleteAllMoviesRecords(mContext);


    }

    //Test the constructor which receives a context, an id and a Path and creates a Movie instance from the DB
    public void testNewMovieByIdPATH_MOVIES(){

        final long INVALID_ID=200L;
        final long VALID_ID=100L;



        //Insert new Movies into the DB
        final int QTY_OF_RECORDS=154;
        ContentValues values[]= TestUtilities.generateFakeContentValueArray(QTY_OF_RECORDS);
        Uri allMoviesUri= PopularMoviesContract.MoviesEntry.buildAllMoviesUri();
        mContext.getContentResolver().bulkInsert(allMoviesUri,values);



        //Verify that if the record doesn´t exists the Movie ID is equal to -1 and all its attributes are set to null
        Movie invalidMovie=new Movie(mContext,INVALID_ID,PopularMoviesContract.PATH_MOVIES);
        assertEquals("Error: Returned id is not equal to -1",invalidMovie.getId(),-1L);
        assertTrue("Error: Title is not null", invalidMovie.getTitle()==null);
        assertTrue("Error: Thumbnail image is not null",invalidMovie.getImageThumbnail()==null);
        assertTrue("Error: release date is not null",invalidMovie.getReleaseDate()==null);
        assertTrue("Error: user rating is not null",invalidMovie.getUserRating()==null);
        assertTrue("Error: Sysnopsis is not null",invalidMovie.getSysnopsis()==null);



        //Verify that if the record exists the Movie ID is equal to the ID sent by parameter and all its attributes are set correctly
        Movie validMovie=new Movie(mContext,VALID_ID,PopularMoviesContract.PATH_MOVIES);
        assertEquals("Error: Returned id is not equal to -1",validMovie.getId(),VALID_ID);
        assertEquals("Error: Title is not the correct", validMovie.getTitle(),TestUtilities.BASE_TITLE+" "+VALID_ID);
        assertEquals("Error: Thumbnail image is not the correct",validMovie.getImageThumbnail(),TestUtilities.BASE_IMAGE_THUMNAIL+" "+VALID_ID);
        assertEquals("Error: release date is not the correct",validMovie.getReleaseDate(),TestUtilities.BASE_RELEASE_DATE+" "+VALID_ID);
        assertEquals("Error: user rating is not correct",validMovie.getUserRating(),TestUtilities.BASE_USR_RATING+" "+VALID_ID);
        assertEquals("Error: Sysnopsis is not correct",validMovie.getSysnopsis(),TestUtilities.BASE_SYSNOPSIS+" "+VALID_ID);




        //delete all the Movie Records for further tests
        TestUtilities.deleteAllMoviesRecords(mContext);
        validMovie=null;
        invalidMovie=null;

    }

    public void testNewMovieByIdPATH_FAVORITES(){

        final long INVALID_ID=200L;
        final long VALID_ID=1L;



        ContentValues movieContentValues;
        HashMap<Long,Uri> QueryUris=new HashMap<Long,Uri>();
        Uri allFavoritesUri;
        UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();
        Movie movie;


        //Create an ArrayList with several Movies
        HashMap<Long,Movie> mMovies=new HashMap<Long,Movie>();
        mMovies.put(1L,new Movie(1L,TestUtilities.BASE_TITLE+" "+1L,TestUtilities.BASE_IMAGE_THUMNAIL+" "+1L,TestUtilities.BASE_SYSNOPSIS+" "+1L,TestUtilities.BASE_USR_RATING+" "+1L,TestUtilities.BASE_RELEASE_DATE+" "+1L));
        mMovies.put(2L,new Movie(2L,TestUtilities.BASE_TITLE+" "+2L,TestUtilities.BASE_IMAGE_THUMNAIL+" "+2L,TestUtilities.BASE_SYSNOPSIS+" "+2L,TestUtilities.BASE_USR_RATING+" "+2L,TestUtilities.BASE_RELEASE_DATE+" "+2L));
        mMovies.put(3L,new Movie(3L,TestUtilities.BASE_TITLE+" "+3L,TestUtilities.BASE_IMAGE_THUMNAIL+" "+3L,TestUtilities.BASE_SYSNOPSIS+" "+3L,TestUtilities.BASE_USR_RATING+" "+3L,TestUtilities.BASE_RELEASE_DATE+" "+3L));
        mMovies.put(4L,new Movie(4L,TestUtilities.BASE_TITLE+" "+4L,TestUtilities.BASE_IMAGE_THUMNAIL+" "+4L,TestUtilities.BASE_SYSNOPSIS+" "+4L,TestUtilities.BASE_USR_RATING+" "+4L,TestUtilities.BASE_RELEASE_DATE+" "+4L));

        //Delete all favorites records to get an empty Favorites table
        TestUtilities.deleteAllFavoritesRecords(mContext);


        //Insert all the Movies and get a reference to its query string

        for (long i=1;i<=mMovies.size();i++)
        {
            movie=mMovies.get(i);
            movieContentValues = movie.getMovieValues();
            QueryUris.put(movie.getId(),mContext.getContentResolver().insert (PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri(), movieContentValues));
        }




        //Verify that if the record doesn´t exists the Movie ID is equal to -1 and all its attributes are set to null
        Movie invalidMovie=new Movie(mContext,INVALID_ID,PopularMoviesContract.PATH_FAVORITES);
        assertEquals("Error: Returned id is not equal to -1",invalidMovie.getId(),-1L);
        assertTrue("Error: Title is not null", invalidMovie.getTitle()==null);
        assertTrue("Error: Thumbnail image is not null",invalidMovie.getImageThumbnail()==null);
        assertTrue("Error: release date is not null",invalidMovie.getReleaseDate()==null);
        assertTrue("Error: user rating is not null",invalidMovie.getUserRating()==null);
        assertTrue("Error: Sysnopsis is not null",invalidMovie.getSysnopsis()==null);



        //Verify that if the record exists the Movie ID is equal to the ID sent by parameter and all its attributes are set correctly
        Movie validMovie=new Movie(mContext,VALID_ID,PopularMoviesContract.PATH_FAVORITES);
        assertEquals("Error: Returned id is not equal to -1",validMovie.getId(),VALID_ID);
        assertEquals("Error: Title is not the correct", validMovie.getTitle(),TestUtilities.BASE_TITLE+" "+VALID_ID);
        assertEquals("Error: Thumbnail image is not the correct",validMovie.getImageThumbnail(),TestUtilities.BASE_IMAGE_THUMNAIL+" "+VALID_ID);
        assertEquals("Error: release date is not the correct",validMovie.getReleaseDate(),TestUtilities.BASE_RELEASE_DATE+" "+VALID_ID);
        assertEquals("Error: user rating is not correct",validMovie.getUserRating(),TestUtilities.BASE_USR_RATING+" "+VALID_ID);
        assertEquals("Error: Sysnopsis is not correct",validMovie.getSysnopsis(),TestUtilities.BASE_SYSNOPSIS+" "+VALID_ID);




        //delete all the Movie Records for further tests
        TestUtilities.deleteAllFavoritesRecords(mContext);
        validMovie=null;
        invalidMovie=null;

    }

    public void testNewMovieByIdINVALID_PATH(){

        final long INVALID_ID=200L;
        final long VALID_ID=1L;



        ContentValues movieContentValues;
        HashMap<Long,Uri> QueryUris=new HashMap<Long,Uri>();
        Uri allFavoritesUri;
        UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();
        Movie movie;


        //Create an ArrayList with several Movies
        HashMap<Long,Movie> mMovies=new HashMap<Long,Movie>();
        mMovies.put(1L,new Movie(1L,TestUtilities.BASE_TITLE+" "+1L,TestUtilities.BASE_IMAGE_THUMNAIL+" "+1L,TestUtilities.BASE_SYSNOPSIS+" "+1L,TestUtilities.BASE_USR_RATING+" "+1L,TestUtilities.BASE_RELEASE_DATE+" "+1L));
        mMovies.put(2L,new Movie(2L,TestUtilities.BASE_TITLE+" "+2L,TestUtilities.BASE_IMAGE_THUMNAIL+" "+2L,TestUtilities.BASE_SYSNOPSIS+" "+2L,TestUtilities.BASE_USR_RATING+" "+2L,TestUtilities.BASE_RELEASE_DATE+" "+2L));
        mMovies.put(3L,new Movie(3L,TestUtilities.BASE_TITLE+" "+3L,TestUtilities.BASE_IMAGE_THUMNAIL+" "+3L,TestUtilities.BASE_SYSNOPSIS+" "+3L,TestUtilities.BASE_USR_RATING+" "+3L,TestUtilities.BASE_RELEASE_DATE+" "+3L));
        mMovies.put(4L,new Movie(4L,TestUtilities.BASE_TITLE+" "+4L,TestUtilities.BASE_IMAGE_THUMNAIL+" "+4L,TestUtilities.BASE_SYSNOPSIS+" "+4L,TestUtilities.BASE_USR_RATING+" "+4L,TestUtilities.BASE_RELEASE_DATE+" "+4L));

        //Delete all favorites records to get an empty Favorites table
        TestUtilities.deleteAllFavoritesRecords(mContext);


        //Insert all the Movies and get a reference to its query string

        for (long i=1;i<=mMovies.size();i++)
        {
            movie=mMovies.get(i);
            movieContentValues = movie.getMovieValues();
            QueryUris.put(movie.getId(),mContext.getContentResolver().insert (PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri(), movieContentValues));
        }

        //Verify that if the record doesn´t exists the Movie ID is equal to -1 and all its attributes are set to null
        Movie invalidMovie=new Movie(mContext,VALID_ID,"INVALID_PATH");
        assertEquals("Error: Returned id is not equal to -1",invalidMovie.getId(),-1L);
        assertTrue("Error: Title is not null", invalidMovie.getTitle()==null);
        assertTrue("Error: Thumbnail image is not null",invalidMovie.getImageThumbnail()==null);
        assertTrue("Error: release date is not null",invalidMovie.getReleaseDate()==null);
        assertTrue("Error: user rating is not null",invalidMovie.getUserRating()==null);
        assertTrue("Error: Sysnopsis is not null",invalidMovie.getSysnopsis()==null);


        //delete all the Movie Records for further tests
        TestUtilities.deleteAllFavoritesRecords(mContext);

        invalidMovie=null;

    }










    //Verify insert a movie in the database
    public void testInsertMovie(){
        Uri insertedMovieUri;
        final long ID=100L;

        //delete all the Movie Records for further tests
        TestUtilities.deleteAllMoviesRecords(mContext);

        Movie movieToInsert=new Movie(ID,"Fake Title","Fake Image Thumbnail","Fake Sysnopsis","Fake userRating","Fake releaseDate");


        //Verify that the movie was inserted
        insertedMovieUri=movieToInsert.insertMovie(mContext);
        assertTrue("Error: returned uri is null",insertedMovieUri!=null);




        //Verify that the movie was inserted correctly
        Movie insertedMovie= new Movie(mContext,ID,PopularMoviesContract.PATH_MOVIES);
        assertTrue("Error: Movie was nor inseted correctly",insertedMovie.isEqual(movieToInsert));


        //delete all the Movie Records for further tests
        TestUtilities.deleteAllMoviesRecords(mContext);


    }


    public void testBulkInsertMovies(){

        final int QTY_OF_MOVIES=154;
        int insertedMovies=0;
        Uri allMovies=PopularMoviesContract.MoviesEntry.buildAllMoviesUri();
        Cursor cursor;


        ArrayList<Movie> movies=new ArrayList<Movie>();

        movies=TestUtilities.generateFakeArrayList(QTY_OF_MOVIES);

        //delete all Movie records
        TestUtilities.deleteAllMoviesRecords(mContext);

        //BUlk inseert the movies through the Movie Class
        insertedMovies=Movie.bulkInsertMovies(mContext,movies);
        assertEquals("Error: quantity of inserted Movies doesn´t match with the expected quantity",insertedMovies,QTY_OF_MOVIES);



        //Verify get all Movies static method
        cursor=Movie.getAllMovies(mContext);
        //Verify the quantity of records
        assertEquals("Error: Quantity of records doesn´t match with the expected qty",cursor.getCount(),QTY_OF_MOVIES);

        //Verify the entered records
        Long id=0L;
        assertTrue("Error: not possible to move to first record",cursor.moveToFirst());

        do{

            id=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID));


            //verify the Title
            assertEquals("Error: Movie Title doesn't match - ("+id+"): ",
                    TestUtilities.BASE_TITLE+" "+id,
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE)));

            //verify the imageThumbnail
            assertEquals("Error: Movie Image Thumbnails doesn't match - ("+id+"): ",
                    TestUtilities.BASE_IMAGE_THUMNAIL+" "+id,
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL)));

            //verify the Release Date
            assertEquals("Error: Movie Image release Date doesn't match - ("+id+"): ",
                    TestUtilities.BASE_RELEASE_DATE+" "+id,
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE)));

            //verify the sysnopsis
            assertEquals("Error: Movie Image Sysnopsiss doesn't match - ("+id+"): ",
                    TestUtilities.BASE_SYSNOPSIS+" "+id,
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS)));

            //verify the User Rating
            assertEquals("Error: Movie Image User Rating doesn't match - ("+id+"): ",
                    TestUtilities.BASE_USR_RATING+" "+id,
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING)));

        }while (cursor.moveToNext());


        //delete all Movie records
        TestUtilities.deleteAllMoviesRecords(mContext);


    }




    //Test the constructor which receives a context, an id and a Path and creates a Movie instance from the DB
    public void testFavorites() {




        //Insert new Movies into the DB
        final int QTY_OF_RECORDS = 154;
        ContentValues values[] = TestUtilities.generateFakeContentValueArray(QTY_OF_RECORDS);
        Uri allMoviesUri = PopularMoviesContract.MoviesEntry.buildAllMoviesUri();
        mContext.getContentResolver().bulkInsert(allMoviesUri, values);


        //Add movies to favorite
        Movie movie1=new Movie(mContext,100L,PopularMoviesContract.PATH_MOVIES);

        //Movie is not in favorites
        assertFalse("ERROR: Movie shouldn´t be in favorites ",movie1.getIsFavorite(mContext));

        //Set the movie into favorites
        movie1.setToFavorite(mContext);
        assertTrue("ERROR: Movie should be in favorites ",movie1.getIsFavorite(mContext));

        //Set the movie back to favorites to check that systen doesnt crash
        movie1.setToFavorite(mContext);
        assertTrue("ERROR: Movie should be in favorites ",movie1.getIsFavorite(mContext));


        //Remove from favorites
        movie1.removeFromFavorite(mContext);
        assertFalse("ERROR: Movie shouldn´t be in favorites ",movie1.getIsFavorite(mContext));

        //Remove the movie once again from favorites to check the syztem doesnt crash
        movie1.removeFromFavorite(mContext);
        assertFalse("ERROR: Movie shouldn´t be in favorites ",movie1.getIsFavorite(mContext));



        //delete all Movie records
        TestUtilities.deleteAllMoviesRecords(mContext);
        TestUtilities.deleteAllFavoritesRecords(mContext);




    }







}
