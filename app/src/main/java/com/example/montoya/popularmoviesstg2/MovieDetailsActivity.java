package com.example.montoya.popularmoviesstg2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private String id;
    private String title;
    private String synopsis;
    private String image;
    private String releaseDate;
    private String userRating;


    private ImageView movieImage;
    private TextView movieTitle;
    private TextView movieReleaseDate;
    private TextView movieSynopsis;
    private TextView movieRating;
    private TextView movieId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIntentData();
        setLayoutValues();






    }



    private void getIntentData(){
        Intent intent=getIntent();
        if (intent != null) {
            this.id=intent.getStringExtra("ID");
            this.title = intent.getStringExtra("TITLE");
            this.synopsis=intent.getStringExtra("SYNOPSIS");
            this.image=intent.getStringExtra("IMAGE");
            this.releaseDate=intent.getStringExtra("RELEASE_DATE");
            this.userRating=intent.getStringExtra("USER_RATING");
        }

    }

    private void setLayoutValues(){

        movieImage=(ImageView) findViewById(R.id.details_image);
        String imagePath= TheMovieDB.BuildImageUrl(TheMovieDB.IMAGE_SIZE_W500,this.image);
        Picasso.with(this).load(imagePath).into(movieImage);

        //Set the movie title
        movieTitle=(TextView) findViewById(R.id.details_title);
        movieTitle.setText(this.title);


        //Set the movie release Date
        movieReleaseDate=(TextView) findViewById(R.id.details_release_date);
        movieReleaseDate.setText(this.releaseDate);


        //Set the movie Rating
        movieRating=(TextView) findViewById(R.id.details_user_rating);
        movieRating.setText(this.userRating);


        //Set the movie id
        movieRating=(TextView) findViewById(R.id.details_id);
        movieRating.setText(this.id);




        //Set the movie Synopsis
        movieSynopsis=(TextView) findViewById(R.id.details_synopsys);
        movieSynopsis.setText(this.synopsis);

    }







}
