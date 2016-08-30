package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.montoya.popularmoviesstg2.R;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;
import com.squareup.picasso.Picasso;

/**
 * Created by montoya on 30.08.2016.
 */
public class MovieCursorAdapter extends CursorAdapter{
    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view= LayoutInflater.from(context).inflate(R.layout.movie_list_item,viewGroup,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String image= cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MoviesEntry.COULUMN_MOVIE_IMAGE_THUMBNAIL));

        ImageView movieImage=(ImageView) view.findViewById(R.id.movie_list_Item_ImgView);
        //ImageView movieImage=(ImageView) view;
        String imagePath= TheMovieDB.BuildImageUrl(TheMovieDB.IMAGE_SIZE_W342,image);

        Picasso.with(context)
                .load(imagePath)
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_poster)
                .into(movieImage);



    }
}
