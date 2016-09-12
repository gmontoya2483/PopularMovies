package com.example.montoya.popularmoviesstg2;

import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.example.montoya.popularmoviesstg2.model.Video;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

import java.util.ArrayList;

/**
 * Created by montoya on 07.09.2016.
 */
public class TestVideos extends AndroidTestCase{

    private final String LOG_TAG=TestVideos.class.getSimpleName();
    TheMovieDB mTheMovieDB;


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTheMovieDB=new TheMovieDB();
    }



    public void testGetVideoDataFromInternet(){

        final String TESTCASE_NAME="testGetVideoDataFromInternet()";



        //Verify data is gotten from the Internet
        Long movie_id=328111L;
        String JsonString=mTheMovieDB.getVideosFromInternet(movie_id);
        Log.i(LOG_TAG,JsonString);
        assertFalse(TESTCASE_NAME+": Data from Internet is null",JsonString==null);


        //Verify the Parser
        ArrayList<Video> mVideosList=new ArrayList<Video>();
        mVideosList=mTheMovieDB.JSonVideoParser(JsonString);

        //ArrayList is not empty
        assertFalse(TESTCASE_NAME+": Video ArrayList is empty",mVideosList.isEmpty());
        //Show all the Videos
        for (Video myVideo: mVideosList) {
            System.out.println("Movie_ID: "+myVideo.getMovieId()+" Key: "+myVideo.getKey()+" name: "+myVideo.getName()+" site: "+myVideo.getSite()+" type: "+myVideo.getType());

        }


    }


    public void testBulkInsert(){
        int insertedRecords;
        Uri allVideosUri= PopularMoviesContract.VideosEntry.buildAllVideosUri();
        ArrayList<Video> VideoList=new ArrayList<Video>();
        VideoList.add(new Video(1L,"Key1","FakeName1", "FakeSite1","FakeType1"));
        VideoList.add(new Video(2L,"Key2","FakeName2", "FakeSite2","FakeType2"));
        VideoList.add(new Video(1L,"Key3","FakeName3", "FakeSite3","FakeType3"));
        VideoList.add(new Video(3L,"Key4","FakeName4", "FakeSite4","FakeType4"));
        VideoList.add(new Video(1L,"Key5","FakeName2", "FakeSite2","FakeType2"));
        VideoList.add(new Video(5L,"Key6","FakeName3", "FakeSite3","FakeType3"));
        VideoList.add(new Video(7L,"Key7","FakeName4", "FakeSite4","FakeType4"));

        //Delete all Videos in order to get an empty table
        TestUtilities.deleteAllVideos(mContext);

        insertedRecords=Video.bulkInsertVideos(mContext,VideoList);

        //verificar la cantidad de registros insertados
        assertEquals("ERROR: La cantidad de registros insertados diefiero con la cantidad insertada",7,insertedRecords);


        Cursor cursor=mContext.getContentResolver().query(allVideosUri,null,null,null,null);
        //verificar la cantidad de registros insertados
        assertEquals("ERROR: La cantidad de registros insertados diefiero con la cantidad insertada",cursor.getCount(),insertedRecords);



        //Delete all Videos in order to get an empty table
        TestUtilities.deleteAllVideos(mContext);

        cursor.close();




    }



    public void testExecuteAsyncTask(){


        final Long MOVIEID=118340L;
        Cursor cursor;

        //Delete all Videos in order to get an empty table
        TestUtilities.deleteAllVideos(mContext);


        TheMovieDB.updateVideos(mContext,MOVIEID);

        Uri VideosByID=PopularMoviesContract.VideosEntry.buildVideosByMovieIdUri(MOVIEID);

        /*
            do {
             cursor= mContext.getContentResolver().query(VideosByID, null, null, null, null);
        }while (cursor.getCount()>4);

        //Verificar la cantidad de registros. Esta forma no es la correcta
        //Previamente ejecutar "http://api.themoviedb.org/3/movie/118340/videos?api_key=nnnnnnnnnnnnn"
        //Obtener la cantidad de registros
        assertEquals("ERROR: La cantidad de videos difiere a la esperada",cursor.getCount(),4);
        */


    }











}
