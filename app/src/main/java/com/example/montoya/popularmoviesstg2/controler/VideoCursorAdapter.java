package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.montoya.popularmoviesstg2.R;
import com.example.montoya.popularmoviesstg2.model.data.PopularMoviesContract;

/**
 * Created by montoya on 13.09.2016.
 */
public class VideoCursorAdapter extends CursorAdapter {


    public VideoCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view= LayoutInflater.from(context).inflate(R.layout.video_list_item,viewGroup,false);
        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_NAME));
        String type=cursor.getString(cursor.getColumnIndex(PopularMoviesContract.VideosEntry.COLUMN_VIDEO_TYPE));

        TextView nameView=(TextView) view.findViewById(R.id.video_item_name);
        TextView typeView=(TextView) view.findViewById(R.id.video_item_type);

        nameView.setText(name);
        typeView.setText(type);


    }
}
