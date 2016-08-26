package com.example.montoya.popularmoviesstg2;

import android.content.ContentValues;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

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

    //Test the constructor which receives a context and a Long and creates a Movie from the DB
    public void testNewMovieById(){

        final long INVALID_ID=200L;
        final long VALID_ID=100L;



        //Insert new Movies into the DB
        final int QTY_OF_RECORDS=154;
        ContentValues values[]= TestUtilities.generateFakeContentValueArray(QTY_OF_RECORDS);
        Uri allMoviesUri= PopularMoviesContract.MoviesEntry.buildAllMoviesUri();
        mContext.getContentResolver().bulkInsert(allMoviesUri,values);



        //Verify that if the record doesn´t exists the Movie ID is equal to -1 and all its attributes are set to null
        Movie invalidMovie=new Movie(mContext,INVALID_ID);
        assertEquals("Error: Returned id is not equal to -1",invalidMovie.getId(),-1L);
        assertTrue("Error: Title is not null", invalidMovie.getTitle()==null);
        assertTrue("Error: Thumbnail image is not null",invalidMovie.getImageThumbnail()==null);
        assertTrue("Error: release date is not null",invalidMovie.getReleaseDate()==null);
        assertTrue("Error: user rating is not null",invalidMovie.getUserRating()==null);
        assertTrue("Error: Sysnopsis is not null",invalidMovie.getSysnopsis()==null);



        //Verify that if the record exists the Movie ID is equal to the ID sent by parameter and all its attributes are set correctly
        Movie validMovie=new Movie(mContext,VALID_ID);
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

}
