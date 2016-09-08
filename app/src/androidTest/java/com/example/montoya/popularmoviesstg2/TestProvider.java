package com.example.montoya.popularmoviesstg2;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.Video;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesDbHelper;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesProvider;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by montoya on 24.08.2016.
 */
public class TestProvider extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestUtilities.deleteAllMoviesRecords(mContext);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        TestUtilities.deleteAllMoviesRecords(mContext);
        TestUtilities.deleteAllFavoritesRecords(mContext);
    }



    /*
    * This method is used to verify that it is possible to insert a movie into the Movie table bx using the PopularMoviesProvider
    *
    * 1 - Create a movie object to insert and get all the attributes Values
    *
    *
    *
    */

    public void testInsertMovieFromTheProvider(){


        Uri insertedMovieUri;
        Uri expectedReturnedUri;



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



        TestUtilities.deleteAllMoviesRecords(mContext);


    }


    public void testQueryMovieFormTheProvider_WithID(){

       Uri insertedMovieUri;

        Movie movie =new Movie(3214L,"Fake title","fakeimageThumbnail","Fake sysnopsis","Fake userRating","Fake release Date");
        ContentValues movieContentValues=movie.getMovieValues();
        insertedMovieUri=mContext.getContentResolver().insert (PopularMoviesContract.MoviesEntry.buildAllMoviesUri(), movieContentValues);


        if(null!=insertedMovieUri){


            UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();


            //Verify the MOVIE_WITH_ID matcher
            assertEquals("Error: MOVIE by ID URI was matched incorrectly.",testMatcher.match(insertedMovieUri), PopularMoviesProvider.MOVIE_WITH_ID);

            Cursor cursor=mContext.getContentResolver().query(insertedMovieUri,null,null,null,null);

            // Verify if the query got records
            assertTrue( "Error: No Records returned from the query", cursor.moveToFirst() );

            //Verify each field
            TestUtilities.VerifyExpectedQueryResults(movie,cursor);


            //Verify there no additional records
            assertFalse("Error: More that 1 record was taken from the DB",cursor.moveToNext());




        }else{
            fail("the returned iserted URI was null");
        }



        TestUtilities.deleteAllMoviesRecords(mContext);



    }


    public void testQueryAllMoviesFromTheProvider(){


        ContentValues movieContentValues;
        HashMap<Long,Uri> QueryUris=new HashMap<Long,Uri>();
        Uri allMoviesUri;
        UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();
        Movie movie;


        //Create an ArrayList with several Movies
        HashMap<Long,Movie> mMovies=new HashMap<Long,Movie>();
        mMovies.put(1L,new Movie(1L,"Fake title1","fakeimageThumbnail1","Fake sysnopsis1","Fake userRating1","Fake release Date1"));
        mMovies.put(2L,new Movie(002L,"Fake title2","fakeimageThumbnail2","Fake sysnopsis2","Fake userRating2","Fake release Date2"));
        mMovies.put(3L,new Movie(003L,"Fake title3","fakeimageThumbnail3","Fake sysnopsis3","Fake userRating3","Fake release Date3"));
        mMovies.put(4L,new Movie(004L,"Fake title4","fakeimageThumbnail4","Fake sysnopsis4","Fake userRating4","Fake release Date4"));
        mMovies.put(5L,new Movie(005L,"Fake title5","fakeimageThumbnail5","Fake sysnopsis5","Fake userRating5","Fake release Date5"));
        mMovies.put(6L,new Movie(006L,"Fake title6","fakeimageThumbnail6","Fake sysnopsis6","Fake userRating6","Fake release Date6"));
        mMovies.put(7L,new Movie(007L,"Fake title7","fakeimageThumbnail7","Fake sysnopsis7","Fake userRating7","Fake release Date7"));

        //Delete all movies records to get an empty Movie table
        TestUtilities.deleteAllMoviesRecords(mContext);


        //Insert all the Movies and get a reference to its query string

        for (long i=1;i<=mMovies.size();i++)
        {
            movie=mMovies.get(i);
            movieContentValues = movie.getMovieValues();
            QueryUris.put(movie.getId(),mContext.getContentResolver().insert (PopularMoviesContract.MoviesEntry.buildAllMoviesUri(), movieContentValues));
        }

        //Verify that all movies where inseted into the dataBase
        assertEquals("Error: Size of the Hashtable and the Arraylist of of movies is not the same",mMovies.size(),QueryUris.size());


        //Verify the MOVIE matcher
        allMoviesUri=PopularMoviesContract.MoviesEntry.buildAllMoviesUri();
        assertEquals("Error: MOVIE by ID URI was matched incorrectly.",testMatcher.match(allMoviesUri), PopularMoviesProvider.MOVIE);

        //Execute the query
        Cursor cursor=mContext.getContentResolver().query(allMoviesUri,null,null,null,null);

        //verify that the cursor is not empty
        assertTrue("Error: Cursor is empty",cursor.moveToFirst());

        //verify that the query got all records
        assertEquals("Error: Size of the Hashtable and the Arraylist of of movies is not the same",mMovies.size(),cursor.getCount());


        //Verify the value of the records
        long id;
        int qtyCompare=0;

        do{

            qtyCompare++;

            id=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry._ID));

            //Verify the ID
            assertEquals("Error: Movie ID doesn't match - ("+id+"): ",mMovies.get(id).getId(),id);

            //verify the Title
            assertEquals("Error: Movie Title doesn't match - ("+id+"): ",
                    mMovies.get(id).getTitle(),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE)));

            //verify the imageThumbnail
            assertEquals("Error: Movie Image Thumbnails doesn't match - ("+id+"): ",
                    mMovies.get(id).getImageThumbnail(),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL)));

            //verify the Release Date
            assertEquals("Error: Movie Image release Date doesn't match - ("+id+"): ",
                    mMovies.get(id).getReleaseDate(),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE)));

            //verify the sysnopsis
            assertEquals("Error: Movie Image Sysnopsiss doesn't match - ("+id+"): ",
                    mMovies.get(id).getSysnopsis(),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS)));

            //verify the User Rating
            assertEquals("Error: Movie Image User Rating doesn't match - ("+id+"): ",
                    mMovies.get(id).getUserRating(),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING)));

        }while (cursor.moveToNext());



        //Verify that all records were analyzed
        assertEquals("Error: Not all the Movie records were compare",qtyCompare,cursor.getCount());


        //Delete all movies records to leave an Empty Movie table for further Test cases
        TestUtilities.deleteAllMoviesRecords(mContext);


        cursor.close();

    }



    public void testDeleteAllMoviesFromTheProvider(){

        ContentValues movieContentValues;
        HashMap<Long,Uri> QueryUris=new HashMap<Long,Uri>();
        Uri allMoviesUri;
        UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();
        Movie movie;
        int recordsCount;
        Cursor cursor;
        int deletedRecordsCount;


        //Create an ArrayList with several Movies
        HashMap<Long,Movie> mMovies=new HashMap<Long,Movie>();
        mMovies.put(1L,new Movie(1L,"Fake title1","fakeimageThumbnail1","Fake sysnopsis1","Fake userRating1","Fake release Date1"));
        mMovies.put(2L,new Movie(002L,"Fake title2","fakeimageThumbnail2","Fake sysnopsis2","Fake userRating2","Fake release Date2"));
        mMovies.put(3L,new Movie(003L,"Fake title3","fakeimageThumbnail3","Fake sysnopsis3","Fake userRating3","Fake release Date3"));
        mMovies.put(4L,new Movie(004L,"Fake title4","fakeimageThumbnail4","Fake sysnopsis4","Fake userRating4","Fake release Date4"));
        mMovies.put(5L,new Movie(005L,"Fake title5","fakeimageThumbnail5","Fake sysnopsis5","Fake userRating5","Fake release Date5"));
        mMovies.put(6L,new Movie(006L,"Fake title6","fakeimageThumbnail6","Fake sysnopsis6","Fake userRating6","Fake release Date6"));
        mMovies.put(7L,new Movie(007L,"Fake title7","fakeimageThumbnail7","Fake sysnopsis7","Fake userRating7","Fake release Date7"));


        //Insert all the Movies and get a reference to its query string

        for (long i=1;i<=mMovies.size();i++)
        {
            movie=mMovies.get(i);
            movieContentValues = movie.getMovieValues();
            QueryUris.put(movie.getId(),mContext.getContentResolver().insert (PopularMoviesContract.MoviesEntry.buildAllMoviesUri(), movieContentValues));
        }


        //Execute the query to get all the records
        allMoviesUri=PopularMoviesContract.MoviesEntry.buildAllMoviesUri();
        cursor=mContext.getContentResolver().query(allMoviesUri,null,null,null,null);


        //Verify that the table is not Empty
        recordsCount=cursor.getCount();
        assertTrue("Error: Movie table already has no records to be deleted",recordsCount>0);


        //delete all records by using the provider
        deletedRecordsCount=mContext.getContentResolver().delete(allMoviesUri,null,null);

        //Verify that the quantity of deleted records matches witht the quantity of records we got in the previous cursor
        assertTrue("Error: Movie table already has no records to be deleted",recordsCount==deletedRecordsCount);



        //Execute the query to get again all the records
        cursor=mContext.getContentResolver().query(allMoviesUri,null,null,null,null);
        assertTrue("Error: Movie table is not empty",cursor.getCount()==0);




        cursor.close();



    }



    public void testBulkInsertMoviesFromTheProvider(){

        final int QTY_OF_RECORDS=154;
        int insertedRecords;
        ContentValues values[]= TestUtilities.generateFakeContentValueArray(QTY_OF_RECORDS);
        Uri allMoviesUri=PopularMoviesContract.MoviesEntry.buildAllMoviesUri();
        Cursor cursor;

        //Verify the generation of the vector
        assertEquals("Error: the quantity of records that the Vector has doesn´t match with the expected",values.length,QTY_OF_RECORDS);


        //Delete all Movies in order to get an empty table
        mContext.getContentResolver().delete(allMoviesUri,null,null);


        //Bulk Insert all the records
        insertedRecords=mContext.getContentResolver().bulkInsert(allMoviesUri,values);

        //Verify the quantity of inserted records
        assertEquals("Error: the quantity of inserted records doesn´t match with the expected",insertedRecords,QTY_OF_RECORDS);



        //Query all records to check how many entries has the movie table
        cursor=mContext.getContentResolver().query(allMoviesUri,null,null,null,null);

        //Verify that the cursor is not empty
        assertTrue("Error: the cursor is empty",cursor.moveToFirst());

        //Verify the quantity of records
        assertTrue("Error: the quantity of inserted records doesnt´match with the expected",cursor.getCount()==QTY_OF_RECORDS);


        //Delete all Movies in order to leave an empty empty table for further tests
        mContext.getContentResolver().delete(allMoviesUri,null,null);

        //Query all records to check how many entries has the movie table
        cursor=mContext.getContentResolver().query(allMoviesUri,null,null,null,null);


        //Verify the quantity of records
        assertTrue("Error: the Movies table still have records",cursor.getCount()==0);


        cursor.close();


    }






    /*
    *
    * FAVORITES TABLE PROVIDERS
    *
     */


    public void testInsertFavoriteFromTheProvider(){


        Uri insertedMovieUri;
        Uri expectedReturnedUri;



        Movie movie =new Movie(3214L,"Fake title","fakeimageThumbnail","Fake sysnopsis","Fake userRating","Fake release Date");
        ContentValues movieContentValues=movie.getMovieValues();


        insertedMovieUri=mContext.getContentResolver().insert (PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri(), movieContentValues);



        expectedReturnedUri=PopularMoviesContract.FavoritesEntry.buildFavoriteByIdUri(movie.getId());



        // Verify that the Returned URI is equal to the expected.
        assertEquals("Error: Movie was not created correctly - Returned Uri doesn't match with the expected one",insertedMovieUri.toString(),expectedReturnedUri.toString());


        //Verify that the record was inserted correctly and the attributes matches
        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getWritableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());

        Cursor cursor=db.rawQuery("SELECT * FROM "+PopularMoviesContract.FavoritesEntry.TABLE_NAME+"" +
                        " WHERE "+ PopularMoviesContract.FavoritesEntry._ID +"=?"
                , new String []{Long.toString(movie.getId())});


        // Verify if the query got records
        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );


        //Verfy each field
        TestUtilities.VerifyExpectedQueryResults(movie,cursor);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from location query",cursor.moveToNext() );



        TestUtilities.deleteAllFavoritesRecords(mContext);


    }



    public void testDeleteFavoriteFromTheProvider(){

        Uri insertedMovieUri;
        int deletedRecords;
        Cursor cursor;




        Movie movie =new Movie(3214L,"Fake title","fakeimageThumbnail","Fake sysnopsis","Fake userRating","Fake release Date");
        ContentValues movieContentValues=movie.getMovieValues();


        insertedMovieUri=mContext.getContentResolver().insert (PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri(), movieContentValues);
        assertFalse ("The inserted uri is null",insertedMovieUri==null);

        cursor=mContext.getContentResolver().query(insertedMovieUri,null,null,null,null);
        assertEquals("The quantity of records is not equal to 1",cursor.getCount(),1);


        deletedRecords=mContext.getContentResolver().delete(PopularMoviesContract.FavoritesEntry.buildFavoriteByIdUri(movie.getId()),null,null);
        assertEquals("The quantity of deleted records is not equal to 1",deletedRecords,1);



        cursor=mContext.getContentResolver().query(insertedMovieUri,null,null,null,null);
        assertEquals("The quantity of records after deleting is not equal to 0",cursor.getCount(),0);




        TestUtilities.deleteAllFavoritesRecords(mContext);


    }




    public void testQueryAllFavoritesFromTheProvider(){


        ContentValues movieContentValues;
        HashMap<Long,Uri> QueryUris=new HashMap<Long,Uri>();
        Uri allFavoritesUri;
        UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();
        Movie movie;


        //Create an ArrayList with several Movies
        HashMap<Long,Movie> mMovies=new HashMap<Long,Movie>();
        mMovies.put(1L,new Movie(1L,"Fake title1","fakeimageThumbnail1","Fake sysnopsis1","Fake userRating1","Fake release Date1"));
        mMovies.put(2L,new Movie(002L,"Fake title2","fakeimageThumbnail2","Fake sysnopsis2","Fake userRating2","Fake release Date2"));
        mMovies.put(3L,new Movie(003L,"Fake title3","fakeimageThumbnail3","Fake sysnopsis3","Fake userRating3","Fake release Date3"));
        mMovies.put(4L,new Movie(004L,"Fake title4","fakeimageThumbnail4","Fake sysnopsis4","Fake userRating4","Fake release Date4"));
        mMovies.put(5L,new Movie(005L,"Fake title5","fakeimageThumbnail5","Fake sysnopsis5","Fake userRating5","Fake release Date5"));
        mMovies.put(6L,new Movie(006L,"Fake title6","fakeimageThumbnail6","Fake sysnopsis6","Fake userRating6","Fake release Date6"));
        mMovies.put(7L,new Movie(007L,"Fake title7","fakeimageThumbnail7","Fake sysnopsis7","Fake userRating7","Fake release Date7"));

        //Delete all favorites records to get an empty Favorites table
        TestUtilities.deleteAllFavoritesRecords(mContext);


        //Insert all the Movies and get a reference to its query string

        for (long i=1;i<=mMovies.size();i++)
        {
            movie=mMovies.get(i);
            movieContentValues = movie.getMovieValues();
            QueryUris.put(movie.getId(),mContext.getContentResolver().insert (PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri(), movieContentValues));
        }

        //Verify that all movies where inseted into the dataBase
        assertEquals("Error: Size of the Hashtable and the Arraylist of of movies is not the same",mMovies.size(),QueryUris.size());


        //Verify the MOVIE matcher
        allFavoritesUri=PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri();
        assertEquals("Error: MOVIE by ID URI was matched incorrectly.",testMatcher.match(allFavoritesUri), PopularMoviesProvider.FAVORITE);

        //Execute the query
        Cursor cursor=mContext.getContentResolver().query(allFavoritesUri,null,null,null,null);

        //verify that the cursor is not empty
        assertTrue("Error: Cursor is empty",cursor.moveToFirst());

        //verify that the query got all records
        assertEquals("Error: Size of the Hashtable and the Arraylist of of movies is not the same",mMovies.size(),cursor.getCount());


        //Verify the value of the records
        long id;
        int qtyCompare=0;

        do{

            qtyCompare++;

            id=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry._ID));

            //Verify the ID
            assertEquals("Error: Movie ID doesn't match - ("+id+"): ",mMovies.get(id).getId(),id);

            //verify the Title
            assertEquals("Error: Movie Title doesn't match - ("+id+"): ",
                    mMovies.get(id).getTitle(),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_TITLE)));

            //verify the imageThumbnail
            assertEquals("Error: Movie Image Thumbnails doesn't match - ("+id+"): ",
                    mMovies.get(id).getImageThumbnail(),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL)));

            //verify the Release Date
            assertEquals("Error: Movie Image release Date doesn't match - ("+id+"): ",
                    mMovies.get(id).getReleaseDate(),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_RELEASE_DATE)));

            //verify the sysnopsis
            assertEquals("Error: Movie Image Sysnopsiss doesn't match - ("+id+"): ",
                    mMovies.get(id).getSysnopsis(),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_SYSNOPSIS)));

            //verify the User Rating
            assertEquals("Error: Movie Image User Rating doesn't match - ("+id+"): ",
                    mMovies.get(id).getUserRating(),
                    cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_USER_RATING)));

        }while (cursor.moveToNext());



        //Verify that all records were analyzed
        assertEquals("Error: Not all the Movie records were compare",qtyCompare,cursor.getCount());


        //Delete all movies records to leave an Empty Movie table for further Test cases
        TestUtilities.deleteAllFavoritesRecords(mContext);


        cursor.close();

    }




    public void testQueryFavoriteFormTheProvider_WithID(){

        Uri insertedMovieUri;

        Movie movie =new Movie(3214L,"Fake title","fakeimageThumbnail","Fake sysnopsis","Fake userRating","Fake release Date");
        ContentValues movieContentValues=movie.getMovieValues();
        insertedMovieUri=mContext.getContentResolver().insert (PopularMoviesContract.FavoritesEntry.buildAllFavoritesUri(), movieContentValues);


        if(null!=insertedMovieUri){

            UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();


            //Verify the MOVIE_WITH_ID matcher
            assertEquals("Error: FAVORITE by ID URI was matched incorrectly.",testMatcher.match(insertedMovieUri), PopularMoviesProvider.FAVORITE_WITH_ID);

            Cursor cursor=mContext.getContentResolver().query(insertedMovieUri,null,null,null,null);

            // Verify if the query got records
            assertTrue( "Error: No Records returned from the query", cursor.moveToFirst() );

            //Verify each field
            TestUtilities.VerifyExpectedQueryResults(movie,cursor);


            //Verify there no additional records
            assertFalse("Error: More that 1 record was taken from the DB",cursor.moveToNext());




        }else{
            fail("the returned inserted URI was null");
        }



        TestUtilities.deleteAllFavoritesRecords(mContext);



    }





    /*
    *
    * Videos section
    *
    */


    public void testInsertVideoFromProvider(){

        final String TESTCASE_NAME="testInsertVideosFromProvider()";

        ContentValues videoValues;
        Uri allVideosUri=PopularMoviesContract.VideosEntry.buildAllVideosUri();
        Uri insertedVideoUri;

        //Delete all movies records to get an empty Movie table
        TestUtilities.deleteAllVideos(mContext);


        Video video=new Video(1L,"FakeKey","FakeName","FakeSite","FakeType");
        videoValues=video.getVideoValues();


        insertedVideoUri=mContext.getContentResolver().insert(allVideosUri,videoValues);

        //Verify theinsertedVideoUri is not null
        assertTrue(TESTCASE_NAME+" - InsertedVideoUri is NULL", insertedVideoUri!=null);


        //Get the Record which was just created
        Cursor cursor;
        cursor=mContext.getContentResolver().query(insertedVideoUri,null,null,null,null);

        //Verify that only 1 record was taken
        assertTrue(TESTCASE_NAME+" - More than 1 record was taken", cursor.getCount()==1);

        //Verify that the cursor is not empty and Move it to the first record
        assertTrue(TESTCASE_NAME+" - Not possible to move the cursor to the first record",cursor.moveToFirst());

        //get the values from the cursor
        Long _id=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.VideosEntry._ID));
        Long movieId=cursor.getLong(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_MOVIE_ID));
        String key=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_KEY));
        String name=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_NAME));
        String type=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_TYPE));
        String site=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_SITE));

        //Compare the values
        assertEquals(TESTCASE_NAME+" - Movie ID doesn´t match",movieId,video.getMovieId());
        assertEquals(TESTCASE_NAME+" - Key doesn´t match",key,video.getKey());
        assertEquals(TESTCASE_NAME+" - Name doesn´t match",name,video.getName());
        assertEquals(TESTCASE_NAME+" - Site doesn´t match",site, video.getSite());
        assertEquals(TESTCASE_NAME+" - Type doesn´t match",type,video.getType());


        //Delete all movies records to leave an Empty Movie table for further Test cases
        TestUtilities.deleteAllVideos(mContext);


        cursor.close();
    }





    public void testQueryAllVideosFromProvider(){

        final String TESTCASE_NAME="testQueryAllVideosFromProvider()";


        ContentValues videoContentValues;
        HashMap<Long,Uri> QueryUris=new HashMap<Long,Uri>();
        Uri allVideosUri;
        UriMatcher testMatcher= PopularMoviesProvider.buildUriMatcher();
        Video video;


        //Create an ArrayList with several Movies
        HashMap<Long,Video> mVideos=new HashMap<Long,Video>();
        mVideos.put(1L,new Video(1L,"Key1","FakeName1", "FakeSite1","FakeType1"));
        mVideos.put(2L,new Video(1L,"Key2","FakeName2", "FakeSite2","FakeType2"));
        mVideos.put(3L,new Video(2L,"Key3","FakeName3", "FakeSite3","FakeType3"));
        mVideos.put(4L,new Video(3L,"Key4","FakeName4", "FakeSite4","FakeType4"));
        mVideos.put(5L,new Video(3L,"Key5","FakeName5", "FakeSite5","FakeType5"));
        mVideos.put(6L,new Video(3L,"Key6","FakeName6", "FakeSite6","FakeType6"));
        mVideos.put(7L,new Video(4L,"Key7","FakeName7", "FakeSite7","FakeType7"));
        mVideos.put(8L,new Video(5L,"Key8","FakeName8", "FakeSite8","FakeType8"));
        mVideos.put(9L,new Video(6L,"Key9","FakeName9", "FakeSite9","FakeType9"));
        mVideos.put(10L,new Video(7L,"Key10","FakeName10", "FakeSite10","FakeType10"));
        mVideos.put(11L,new Video(7L,"Key11","FakeName11", "FakeSite11","FakeType11"));
        mVideos.put(12L,new Video(8L,"Key12","FakeName12", "FakeSite12","FakeType12"));



        //Delete all movies records to get an empty Movie table
        TestUtilities.deleteAllVideos(mContext);


        //Insert all the Movies and get a reference to its query string

        for (long i=1;i<=mVideos.size();i++)
        {
            video=mVideos.get(i);
            videoContentValues = video.getVideoValues();
            QueryUris.put(i,mContext.getContentResolver().insert (PopularMoviesContract.VideosEntry.buildAllVideosUri(), videoContentValues));
        }




        //Verify that all movies where inseted into the dataBase
        assertEquals("Error: Size of the Hashtable and the Arraylist of Videos is not the same",mVideos.size(),QueryUris.size());


        //Verify the Video matcher
        allVideosUri=PopularMoviesContract.VideosEntry.buildAllVideosUri();
        assertEquals("Error: VIDEO was matched incorrectly.",testMatcher.match(allVideosUri), PopularMoviesProvider.VIDEO);

        //Execute the query
        Cursor cursor=mContext.getContentResolver().query(allVideosUri,null,null,null,null);

        //verify that the cursor is not empty
        assertTrue("Error: Cursor is empty",cursor.moveToFirst());

        //verify that the query got all records
        assertEquals("Error: Size of the Hashtable and the Arraylist of of movies is not the same",mVideos.size(),cursor.getCount());




        //Delete all movies records to leave an Empty Movie table for further Test cases
        TestUtilities.deleteAllVideos(mContext);


        cursor.close();

    }



    public void testQueryVideosByMovieID(){

        final String TESTCASE_NAME="testQueryVideosByMovieID()";

        Cursor cursor;
        ArrayList<Video> VideoList =new ArrayList<Video>();
        ContentValues videoValues;
        Uri allVideosUri=PopularMoviesContract.VideosEntry.buildAllVideosUri();



        VideoList.add(new Video(1L,"Key1","FakeName1", "FakeSite1","FakeType1"));
        VideoList.add(new Video(1L,"Key2","FakeName2", "FakeSite2","FakeType2"));
        VideoList.add(new Video(2L,"Key3","FakeName3", "FakeSite3","FakeType3"));
        VideoList.add(new Video(3L,"Key4","FakeName4", "FakeSite4","FakeType4"));
        VideoList.add(new Video(3L,"Key5","FakeName5", "FakeSite5","FakeType5"));
        VideoList.add(new Video(3L,"Key6","FakeName6", "FakeSite6","FakeType6"));
        VideoList.add(new Video(4L,"Key7","FakeName7", "FakeSite7","FakeType7"));
        VideoList.add(new Video(5L,"Key8","FakeName8", "FakeSite8","FakeType8"));
        VideoList.add(new Video(6L,"Key9","FakeName9", "FakeSite9","FakeType9"));
        VideoList.add(new Video(7L,"Key10","FakeName10", "FakeSite10","FakeType10"));
        VideoList.add(new Video(7L,"Key11","FakeName11", "FakeSite11","FakeType11"));
        VideoList.add(new Video(8L,"Key12","FakeName12", "FakeSite12","FakeType12"));



        //Delete all Videos records to ensure that we have an Empty Video table
        TestUtilities.deleteAllVideos(mContext);


        //Instert the test data
        for (Video video:VideoList){

            videoValues=video.getVideoValues();
            mContext.getContentResolver().insert(allVideosUri,videoValues);


        }



        Uri videosByMovieUri=PopularMoviesContract.VideosEntry.buildVideosByMovieIdUri(3L);
        cursor=mContext.getContentResolver().query(videosByMovieUri,null,null,null,null);


        //Verify that the query got only 3 records
        assertEquals(TESTCASE_NAME+" - Quantity of Records doesn´t match",cursor.getCount(),3);



        //Delete all movies records to leave an Empty Video table for further Test cases
        TestUtilities.deleteAllVideos(mContext);


    }










}
