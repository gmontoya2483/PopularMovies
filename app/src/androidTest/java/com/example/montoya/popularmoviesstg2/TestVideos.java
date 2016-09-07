package com.example.montoya.popularmoviesstg2;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.example.montoya.popularmoviesstg2.model.Video;

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







}
