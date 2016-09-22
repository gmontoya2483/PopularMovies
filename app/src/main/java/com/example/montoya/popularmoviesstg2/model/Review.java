package com.example.montoya.popularmoviesstg2.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

import java.util.ArrayList;

/**
 * Created by montoya on 21.09.2016.
 */

public class Review {

    private Long id;
    private Long movieId;
    private String author;
    private String content;
    private String url;




    public Review(Long movieId, String author, String content, String url) {
        this.movieId = movieId;
        this.author = author;
        this.content = content;
        this.url = url;
    }


    public Review(Long _id, Long movieId, String author, String content, String url) {
        this.id = id;
        this.movieId = movieId;
        this.author = author;
        this.content = content;
        this.url = url;
    }



    public Long getId() {
        return id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }



    public ContentValues getReviewValues (){


        ContentValues videoValues=new ContentValues();
        videoValues.put(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_MOVIE_ID,this.getMovieId());
        videoValues.put(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_AUTHOR,this.getAuthor());
        videoValues.put(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_CONTENT,this.getContent());
        videoValues.put(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_URL,this.getUrl());

        return videoValues;

    }




    public static ContentValues[] getReviewContentValueArray (ArrayList<Review> reviews){
        ContentValues values []=new ContentValues[reviews.size()];
        int i=0;
        for(Review review:reviews){
            values[i]=review.getReviewValues();
            i++;

        }

        return values;


    }


    public static int bulkInsertReviews (Context context, ArrayList<Review> reviews){
        int quantityOfInsertedReviews=0;
        Uri allReviewsUri=PopularMoviesContract.ReviewsEntry.buildAllReviewsUri();
        ContentValues values[]=Review.getReviewContentValueArray(reviews);
        quantityOfInsertedReviews=context.getContentResolver().bulkInsert(allReviewsUri,values);
        return quantityOfInsertedReviews;
    }



    public static int deleteAllReviews(Context context){
        int qtyOfDeletedReviews=0;
        Uri allReviewsUri=PopularMoviesContract.ReviewsEntry.buildAllReviewsUri();
        qtyOfDeletedReviews=context.getContentResolver().delete(allReviewsUri,null,null);
        return qtyOfDeletedReviews;

    }











}



