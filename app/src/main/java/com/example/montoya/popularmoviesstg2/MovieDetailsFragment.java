package com.example.montoya.popularmoviesstg2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.squareup.picasso.Picasso;

/**
 * Created by montoya on 30.08.2016.
 */
public class MovieDetailsFragment extends Fragment{


    private Long id;
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


    View mRootView;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
         mRootView= inflater.inflate(R.layout.fragment_movie_details, container, false);



        getIntentData();
        setLayoutValues();

        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }




    private void getIntentData(){
        Intent intent=getActivity().getIntent();
        if (intent != null) {
            this.id=intent.getLongExtra("ID",0L);
            this.title = intent.getStringExtra("TITLE");
            this.synopsis=intent.getStringExtra("SYNOPSIS");
            this.image=intent.getStringExtra("IMAGE");
            this.releaseDate=intent.getStringExtra("RELEASE_DATE");
            this.userRating=intent.getStringExtra("USER_RATING");

            System.out.println(intent.getDataString());
        }

    }

    private void setLayoutValues(){

        movieImage=(ImageView) mRootView.findViewById(R.id.details_image);
        String imagePath= TheMovieDB.BuildImageUrl(TheMovieDB.IMAGE_SIZE_W500,this.image);
        Picasso.with(getActivity()).load(imagePath).into(movieImage);

        //Set the movie title
        movieTitle=(TextView) mRootView.findViewById(R.id.details_title);
        movieTitle.setText(this.title);


        //Set the movie release Date
        movieReleaseDate=(TextView) mRootView.findViewById(R.id.details_release_date);
        movieReleaseDate.setText(this.releaseDate);


        //Set the movie Rating
        movieRating=(TextView) mRootView.findViewById(R.id.details_user_rating);
        movieRating.setText(this.userRating);


        //Set the movie id
        movieId=(TextView) mRootView.findViewById(R.id.details_id);
        movieId.setText(Long.toString(this.id));




        //Set the movie Synopsis
        movieSynopsis=(TextView) mRootView.findViewById(R.id.details_synopsys);
        movieSynopsis.setText(this.synopsis);




    }




}
