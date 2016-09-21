package com.example.montoya.popularmoviesstg2;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.montoya.popularmoviesstg2.model.Movie;
import com.example.montoya.popularmoviesstg2.model.Review;
import com.example.montoya.popularmoviesstg2.model.Video;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesDbHelper;

import java.util.HashSet;

/**
 * Created by montoya on 23.08.2016.
 */
public class TestDb extends AndroidTestCase{


    public static final String LOG_TAG = TestDb.class.getSimpleName();


    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(PopularMoviesDbHelper.DATABASE_NAME);
    }


    @Override
    protected void setUp() throws Exception {
        deleteTheDatabase();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateDb() throws Throwable{


        // Add the table names within the HashSet
        final HashSet <String> tablenameHasSet= new HashSet<String>();
        tablenameHasSet.add(PopularMoviesContract.MoviesEntry.TABLE_NAME);
        tablenameHasSet.add(PopularMoviesContract.FavoritesEntry.TABLE_NAME);
        tablenameHasSet.add(PopularMoviesContract.VideosEntry.TABLE_NAME);
        tablenameHasSet.add(PopularMoviesContract.ReviewsEntry.TABLE_NAME);



        mContext.deleteDatabase(PopularMoviesDbHelper.DATABASE_NAME);
        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getWritableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());



        //have we created the tables we want?
        Cursor c= db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);
        assertTrue("Error: Database is not created correctly - No tables created",c.moveToFirst());






        // verify that the tables have been created
        // we remove from the hash table all the records in the cursos which has all the table names, if at the end the hashtable is not empty that means that not all tables were created.
        do {
            tablenameHasSet.remove(c.getString(0));
        }while (c.moveToNext());

        assertTrue("Error: Database is not created correctly - Some tables were created wrongly",tablenameHasSet.isEmpty());


        c.close();
        db.close();


    }


    public void testTableMoviesStructure() throws Throwable{


        final HashSet<String> moviesColumnHashSet=new HashSet<String>();
        moviesColumnHashSet.add(PopularMoviesContract.MoviesEntry._ID);
        moviesColumnHashSet.add(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_TITLE);
        moviesColumnHashSet.add(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL);
        moviesColumnHashSet.add(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_RELEASE_DATE);
        moviesColumnHashSet.add(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_SYSNOPSIS);
        moviesColumnHashSet.add(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_USER_RATING);


        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getReadableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());

        Cursor cursor=db.rawQuery("PRAGMA table_info("+PopularMoviesContract.MoviesEntry.TABLE_NAME+")",null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                cursor.moveToFirst());

        int columnNameIndex= cursor.getColumnIndex("name");

        do {
            String columnName = cursor.getString(columnNameIndex);
            moviesColumnHashSet.remove(columnName);
        } while(cursor.moveToNext());

        assertTrue("Error: The table "+PopularMoviesContract.MoviesEntry.TABLE_NAME +" doesn't contain all of the required  columns",moviesColumnHashSet.isEmpty());

        cursor.close();
        db.close();
    }


    public void testInsertMovieIntoMoviesTable(){




        long movieRowId;
        Movie movie =new Movie(3214L,"Fake title","fakeimageThumbnail","Fake sysnopsis","Fake userRating","Fake release Date");


        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getWritableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());


        ContentValues testValues=TestUtilities.createFakeMovieValues(movie);
        movieRowId=db.insert(PopularMoviesContract.MoviesEntry.TABLE_NAME,null,testValues);

        //Verify that the Inserted movie matches with the expected id
        assertEquals("Error: Expected Movie ID doesn´t match: ",movieRowId,movie.getId());


        Cursor cursor=db.rawQuery("SELECT * FROM "+PopularMoviesContract.MoviesEntry.TABLE_NAME+"" +
                " WHERE "+ PopularMoviesContract.MoviesEntry._ID +"=?"
                , new String []{Long.toString(movieRowId)});


        // Verify if the query got records
        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );


        //Verfy each field

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

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from location query",cursor.moveToNext() );



        //Verify that the inserted record is deleted correctly
        int qtyOfDeletedRecords=db.delete(PopularMoviesContract.MoviesEntry.TABLE_NAME,PopularMoviesContract.MoviesEntry._ID +"=?",new String []{Long.toString(movieRowId)});
        assertEquals("Error: record was not deleted correctly",1,qtyOfDeletedRecords);

        cursor=db.rawQuery("SELECT * FROM "+PopularMoviesContract.MoviesEntry.TABLE_NAME+"" +
                        " WHERE "+ PopularMoviesContract.MoviesEntry._ID +"=?"
                , new String []{Long.toString(movieRowId)});

        // Verify if the query got records
        assertFalse( "Error: The fake record was not deleted", cursor.moveToFirst() );


        cursor.close();
        db.close();


    }





    public void testTableFavoriteStructure() throws Throwable{


        final HashSet<String> moviesColumnHashSet=new HashSet<String>();
        moviesColumnHashSet.add(PopularMoviesContract.FavoritesEntry._ID);
        moviesColumnHashSet.add(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_TITLE);
        moviesColumnHashSet.add(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL);
        moviesColumnHashSet.add(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_RELEASE_DATE);
        moviesColumnHashSet.add(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_SYSNOPSIS);
        moviesColumnHashSet.add(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_USER_RATING);


        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getReadableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());

        Cursor cursor=db.rawQuery("PRAGMA table_info("+PopularMoviesContract.FavoritesEntry.TABLE_NAME+")",null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                cursor.moveToFirst());

        int columnNameIndex= cursor.getColumnIndex("name");

        do {
            String columnName = cursor.getString(columnNameIndex);
            moviesColumnHashSet.remove(columnName);
        } while(cursor.moveToNext());

        assertTrue("Error: The table "+PopularMoviesContract.FavoritesEntry.TABLE_NAME +" doesn't contain all of the required  columns",moviesColumnHashSet.isEmpty());

        cursor.close();
        db.close();
    }





    public void testInsertMovieIntoFavoritesTable(){




        long movieRowId;
        Movie movie =new Movie(3214L,"Fake title","fakeimageThumbnail","Fake sysnopsis","Fake userRating","Fake release Date");


        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getWritableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());


        ContentValues testValues=TestUtilities.createFakeMovieValues(movie);
        movieRowId=db.insert(PopularMoviesContract.FavoritesEntry.TABLE_NAME,null,testValues);

        //Verify that the Inserted movie matches with the expected id
        assertEquals("Error: Expected Movie ID doesn´t match: ",movieRowId,movie.getId());


        Cursor cursor=db.rawQuery("SELECT * FROM "+PopularMoviesContract.FavoritesEntry.TABLE_NAME+"" +
                        " WHERE "+ PopularMoviesContract.FavoritesEntry._ID +"=?"
                , new String []{Long.toString(movieRowId)});


        // Verify if the query got records
        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );


        //Verfy each field

        int indexId=cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry._ID);
        int indexTitle=cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_TITLE);
        int indexImageThumbnail=cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL);
        int indexRelease=cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_RELEASE_DATE);
        int indexSysnopsis=cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_SYSNOPSIS);
        int indexUserRating=cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COULUMN_MOVIE_USER_RATING);



        assertEquals("Error: Movie ID doesn´t Match", movie.getId(),cursor.getLong(indexId));
        assertEquals("Error: Movie Title doesn´t Match",movie.getTitle(),cursor.getString(indexTitle));
        assertEquals("Error: Movie Image Thumbnail doesn´t Match",movie.getImageThumbnail(),cursor.getString(indexImageThumbnail));
        assertEquals("Error: Movie Release date doesn´t Match",movie.getReleaseDate(),cursor.getString(indexRelease));
        assertEquals("Error: Movie Sysnopsis doesn´t Match",movie.getSysnopsis(),cursor.getString(indexSysnopsis));
        assertEquals("Error: Movie User Rating doesn´t Match",movie.getUserRating(),cursor.getString(indexUserRating));

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from location query",cursor.moveToNext() );



        //Verify that the inserted record is deleted correctly
        int qtyOfDeletedRecords=db.delete(PopularMoviesContract.FavoritesEntry.TABLE_NAME,PopularMoviesContract.FavoritesEntry._ID +"=?",new String []{Long.toString(movieRowId)});
        assertEquals("Error: record was not deleted correctly",1,qtyOfDeletedRecords);

        cursor=db.rawQuery("SELECT * FROM "+PopularMoviesContract.FavoritesEntry.TABLE_NAME+"" +
                        " WHERE "+ PopularMoviesContract.MoviesEntry._ID +"=?"
                , new String []{Long.toString(movieRowId)});

        // Verify if the query got records
        assertFalse( "Error: The fake record was not deleted", cursor.moveToFirst() );


        cursor.close();
        db.close();


    }


    public void testTableVideoStructure() throws Throwable{


        final HashSet<String> moviesColumnHashSet=new HashSet<String>();
        moviesColumnHashSet.add(PopularMoviesContract.VideosEntry._ID);
        moviesColumnHashSet.add(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_MOVIE_ID);
        moviesColumnHashSet.add(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_KEY);
        moviesColumnHashSet.add(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_NAME);
        moviesColumnHashSet.add(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_SITE);
        moviesColumnHashSet.add(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_TYPE);


        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getReadableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());

        Cursor cursor=db.rawQuery("PRAGMA table_info("+PopularMoviesContract.VideosEntry.TABLE_NAME+")",null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                cursor.moveToFirst());

        int columnNameIndex= cursor.getColumnIndex("name");

        do {
            String columnName = cursor.getString(columnNameIndex);
            moviesColumnHashSet.remove(columnName);
        } while(cursor.moveToNext());

        assertTrue("Error: The table "+PopularMoviesContract.VideosEntry.TABLE_NAME +" doesn't contain all of the required  columns",moviesColumnHashSet.isEmpty());

        cursor.close();
        db.close();
    }


    public void testInsertVideoIntoVideosTable(){




        long videoRowId;
        long video2RowId;
        Video video =new Video(3214L,"fakeKey","fakeName","fakeSite","fakeType");
        Video video2=new Video(3215L,"fakeKey","fakeName2","fakeSite2","fakeType2");


        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getWritableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());


        ContentValues testValues=video.getVideoValues();
        videoRowId=db.insert(PopularMoviesContract.VideosEntry.TABLE_NAME,null,testValues);

        //Verify that the Inserted Video ID is not -1L
        assertFalse("Error: Expected Video ID is -1L: ",videoRowId==-1L);


        Cursor cursor=db.rawQuery("SELECT * FROM "+PopularMoviesContract.VideosEntry.TABLE_NAME+"" +
                        " WHERE "+ PopularMoviesContract.VideosEntry._ID +"=?"
                , new String []{Long.toString(videoRowId)});


        // Verify if the query got records
        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );


        //Verfy each field

        int indexId=cursor.getColumnIndex(PopularMoviesContract.VideosEntry._ID);
        int indexMovieId=cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_MOVIE_ID);
        int indexKey=cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_KEY);
        int indexName=cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_NAME);
        int indexSite=cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_SITE);
        int indexType=cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_TYPE);


        assertTrue("Error: Movie ID doesn´t Match", video.getMovieId()==cursor.getLong(indexMovieId));
        assertEquals("Error: Key doesn´t Match", video.getKey(),cursor.getString(indexKey));
        assertEquals("Error: Name doesn´t Match", video.getName(),cursor.getString(indexName));
        assertEquals("Error: Site doesn´t Match", video.getSite(),cursor.getString(indexSite));
        assertEquals("Error: Type doesn´t Match", video.getType(),cursor.getString(indexType));

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from location query",cursor.moveToNext() );



        //Veriy that Key fields is unique
        testValues=video2.getVideoValues();
        video2RowId=db.insert(PopularMoviesContract.VideosEntry.TABLE_NAME,null,testValues);
        assertEquals("Error: el registro fue inserado correctamente",video2RowId,-1L);




        //Verify that the inserted record is deleted correctly
        int qtyOfDeletedRecords=db.delete(PopularMoviesContract.VideosEntry.TABLE_NAME,PopularMoviesContract.VideosEntry._ID +"=?",new String []{Long.toString(videoRowId)});
        assertEquals("Error: record was not deleted correctly",1,qtyOfDeletedRecords);

        cursor=db.rawQuery("SELECT * FROM "+PopularMoviesContract.VideosEntry.TABLE_NAME+"" +
                        " WHERE "+ PopularMoviesContract.VideosEntry._ID +"=?"
                , new String []{Long.toString(videoRowId)});

        // Verify if the query got records
        assertFalse( "Error: The fake record was not deleted", cursor.moveToFirst() );


        cursor.close();
        db.close();


    }



    public void testTableReviewStructure() throws Throwable{


        final HashSet<String> reviewsColumnHashSet=new HashSet<String>();
        reviewsColumnHashSet.add(PopularMoviesContract.ReviewsEntry._ID);
        reviewsColumnHashSet.add(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_MOVIE_ID);
        reviewsColumnHashSet.add(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_AUTHOR);
        reviewsColumnHashSet.add(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_CONTENT);
        reviewsColumnHashSet.add(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_URL);



        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getReadableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());

        Cursor cursor=db.rawQuery("PRAGMA table_info("+PopularMoviesContract.ReviewsEntry.TABLE_NAME+")",null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                cursor.moveToFirst());

        int columnNameIndex= cursor.getColumnIndex("name");

        do {
            String columnName = cursor.getString(columnNameIndex);
            reviewsColumnHashSet.remove(columnName);
        } while(cursor.moveToNext());

        assertTrue("Error: The table "+PopularMoviesContract.ReviewsEntry.TABLE_NAME +" doesn't contain all of the required  columns",reviewsColumnHashSet.isEmpty());

        cursor.close();
        db.close();
    }




    public void testInsertReviewsIntoReviewsTable(){


        long reviewRowId;
        Review review =new Review (3214L,"Fake Author","Fake Content","Fake URL");

        SQLiteDatabase db=new PopularMoviesDbHelper(this.mContext).getWritableDatabase();
        assertEquals("Error: Database is not opened correctly",true,db.isOpen());


        ContentValues testValues=review.getReviewValues();
        reviewRowId=db.insert(PopularMoviesContract.ReviewsEntry.TABLE_NAME,null,testValues);

        //Verify that the Inserted Video ID is not -1L
        assertFalse("Error: Expected Review ID is -1L: ",reviewRowId==-1L);


        Cursor cursor=db.rawQuery("SELECT * FROM "+PopularMoviesContract.ReviewsEntry.TABLE_NAME+"" +
                        " WHERE "+ PopularMoviesContract.ReviewsEntry._ID +"=?"
                , new String []{Long.toString(reviewRowId)});


        // Verify if the query got records
        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );


        //Verify each field

        int indexId=cursor.getColumnIndex(PopularMoviesContract.ReviewsEntry._ID);
        int indexMovieId=cursor.getColumnIndex(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_MOVIE_ID);
        int indexAuthor=cursor.getColumnIndex(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_AUTHOR);
        int indexContent=cursor.getColumnIndex(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_CONTENT);
        int indexUrl=cursor.getColumnIndex(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_URL);


        assertTrue("Error: Movie ID doesn´t Match", review.getMovieId()==cursor.getLong(indexMovieId));
        assertEquals("Error: Author doesn´t Match", review.getAuthor(),cursor.getString(indexAuthor));
        assertEquals("Error: Content doesn´t Match", review.getContent(),cursor.getString(indexContent));
        assertEquals("Error: Url doesn´t Match", review.getUrl(),cursor.getString(indexUrl));

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from the query",cursor.moveToNext() );






        //Verify that the inserted record is deleted correctly
        int qtyOfDeletedRecords=db.delete(PopularMoviesContract.ReviewsEntry.TABLE_NAME,PopularMoviesContract.ReviewsEntry._ID +"=?",new String []{Long.toString(reviewRowId)});
        assertEquals("Error: record was not deleted correctly",1,qtyOfDeletedRecords);

        cursor=db.rawQuery("SELECT * FROM "+PopularMoviesContract.ReviewsEntry.TABLE_NAME+"" +
                        " WHERE "+ PopularMoviesContract.ReviewsEntry._ID +"=?"
                , new String []{Long.toString(reviewRowId)});

        // Verify if the query got records
        assertFalse( "Error: The fake record was not deleted", cursor.moveToFirst() );


        cursor.close();
        db.close();


    }

















}
