package com.example.montoya.popularmoviesstg2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.montoya.popularmoviesstg2.controler.TheMovieDB;
import com.example.montoya.popularmoviesstg2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by montoya on 06.05.2016.
 */
public class CustomGridArrayAdapter extends ArrayAdapter<Movie> {

    private final String LOG_TAG=CustomGridArrayAdapter.class.getSimpleName();

    public CustomGridArrayAdapter(Context context, ArrayList<Movie> movies){
        super(context,0,movies);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Movie item=getItem(position);


        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item,parent,false);
        }

        ImageView movieImage=(ImageView) convertView.findViewById(R.id.movie_list_Item_ImgView);
        String imagePath= TheMovieDB.BuildImageUrl(TheMovieDB.IMAGE_SIZE_W342,item.getImageThumbnail());




        Picasso.with(this.getContext())
                .load(imagePath)
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_poster)
                .into(movieImage);


        return convertView;







    }
}
