package com.example.montoya.popularmoviesstg2;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesDbHelper;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesProvider;


/**
 * Created by montoya on 24.08.2016.
 */
public class TestProvider extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllMoviesRecords();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        deleteAllMoviesRecords();
    }




    public void testInsertMovieFromTheProvider(){


        Uri insertedMovieUri;
        Uri expectedReturnedUri;

        deleteAllMoviesRecords();

        Movie movie =new Movie(3214L,"Fake title","fakeimageThumbnail","Fake sysnopsis","Fake userRating","Fake release Date");
        ContentValues movieContentValues=movie.getMovieValues();


        insertedMovieUri=mContext.getContentResolver().insert (PopularMoviesContract.MoviesEntry.buildAllMoviesUri(), movieContentValues);


        expectedReturnedUri=PopularMoviesContract.MoviesEntry.buildMoviesUri(movie.getId());

        // Verify that the Returned URI is equal to the expected.
        assertEquals("Error: Movie was not created correctly - Returned Uri doesn't match with the expected one",insertedMovieUri.toString(),expectedReturnedUri.toString());


        //Verify that the record was inserted correctly and the attributes matches
        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getWritableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());

        Cursor cursor=db.rawQuery("SELECT * FROM "+PopularMoviesContract.MoviesEntry.TABLE_NAME+"" +
                        " WHERE "+ PopularMoviesContract.MoviesEntry._ID +"=?"
                , new String []{Long.toString(movie.getId())});


        // Verify if the query got records
        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );


        //Verfy each field
        TestUtilities.VerifyExpectedQueryResults(movie,cursor);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from location query",cursor.moveToNext() );



        deleteAllMoviesRecords();


    }


    public void testQueryMovieFormTheProvider_WithID(){




        Uri insertedMovieUri;
        Cursor cursor;


        deleteAllMoviesRecords();





        Movie movie =new Movie(1234L,"Fake title","fakeimageThumbnail","Fake sysnopsis","Fake userRating","Fake release Date");
        ContentValues movieContentValues=movie.getMovieValues();
        insertedMovieUri=mContext.getContentResolver().insert (PopularMoviesContract.MoviesEntry.buildAllMoviesUri(), movieContentValues);


        if(null!=insertedMovieUri){
            Log.e("TEST CREATED URI:  ", insertedMovieUri.toString());

            UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();


            //Verify the MOVIE_WITH_ID matcher
            assertEquals("Error: MOVIE by ID URI was matched incorrectly.",testMatcher.match(insertedMovieUri), PopularMoviesProvider.MOVIE_WITH_ID);

            /*
            cursor=mContext.getContentResolver().query(insertedMovieUri,null,null,null,null);

            // Verify if the query got records
            assertTrue( "Error: No Records returned from the query", cursor.moveToFirst() );


            //Verify each field
            TestUtilities.VerifyExpectedQueryResults(movie,cursor);

            //Verify there no additional records
            assertFalse("Error: More that 1 record was taken from the DB",cursor.moveToNext());
        */
        }else{
            fail("the returned iserted URI was null");
        }



        deleteAllMoviesRecords();


    }


    private int deleteAllMoviesRecords(){

        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getWritableDatabase();
        int qtyOfDeletedRecords=db.delete(PopularMoviesContract.MoviesEntry.TABLE_NAME,null,null);
        db.close();

        return qtyOfDeletedRecords;
    }



}
