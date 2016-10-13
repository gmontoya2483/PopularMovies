package com.example.montoya.popularmoviesstg2.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.montoya.popularmoviesstg2.R;

public class MovieDetailsActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            e.getStackTrace();
        }


        if (savedInstanceState==null){

            Bundle arguments=new Bundle();
            arguments.putParcelable(MovieDetailsFragment.DETAIL_URI,getIntent().getData());


           // Log.e ("DETAILS ON CREATE URI",arguments.getBundle(MovieDetailsFragment.DETAIL_URI).toString());

            MovieDetailsFragment fragment=new MovieDetailsFragment();
                    fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_details_container,fragment)
                    .commit();
        }




    }











}
