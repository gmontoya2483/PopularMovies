package com.example.montoya.popularmoviesstg2;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.montoya.popularmoviesstg2.model.Movie;
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













}
