package com.example.montoya.popularmoviesstg2.model;

import android.content.ContentValues;

import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

/**
 * Created by montoya on 21.09.2016.
 */

public class Review {

    private Long id;
    private Long movieId;
    private String author;
    private String content;
    private String url;


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

    public Review(Long _id, Long movieId, String author, String content, String url) {
        this.id = id;
        this.movieId = movieId;
        this.author = author;
        this.content = content;
        this.url = url;
    }


    public Review(Long movieId, String author, String content, String url) {
        this.movieId = movieId;
        this.author = author;
        this.content = content;
        this.url = url;
    }



    public ContentValues getReviewValues (){


        ContentValues videoValues=new ContentValues();
        videoValues.put(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_MOVIE_ID,this.getMovieId());
        videoValues.put(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_AUTHOR,this.getAuthor());
        videoValues.put(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_CONTENT,this.getContent());
        videoValues.put(PopularMoviesContract.ReviewsEntry.COLUMN_REVIEW_URL,this.getUrl());

        return videoValues;

    }



}



