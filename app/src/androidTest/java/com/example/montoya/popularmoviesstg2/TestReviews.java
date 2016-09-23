package com.example.montoya.popularmoviesstg2;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.example.montoya.popularmoviesstg2.model.Review;

import java.util.ArrayList;

/**
 * Created by montoya on 23.09.2016.
 */

public class TestReviews extends AndroidTestCase {

    private final String LOG_TAG=TestReviews.class.getSimpleName();
    private TheMovieDB mTheMovieDB;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTheMovieDB=new TheMovieDB();

    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }



    public void testGetReviewDataFromInternet(){

        final String TESTCASE_NAME="testGetReviewDataFromInternet()";



        //Verify data is gotten from the Internet
        Long movie_id=328111L;
        String JsonString=mTheMovieDB.getReviewsFromInternet(movie_id);
        Log.i(LOG_TAG,JsonString);
        assertFalse(TESTCASE_NAME+": Data from Internet is null",JsonString==null);


        //Verify the Parser
        ArrayList<Review> mReviewList=new ArrayList<Review>();
        mReviewList=mTheMovieDB.JSonReviewParser(JsonString);

        //ArrayList is not empty
        assertFalse(TESTCASE_NAME+": Review ArrayList is empty",mReviewList.isEmpty());
        //Show all the Videos
        for (Review myReview: mReviewList) {
            System.out.println("Movie_ID: "+myReview.getMovieId()+" author: "+myReview.getAuthor()+" content: "+myReview.getContent()+" Url: "+myReview.getUrl());

        }


    }
}
