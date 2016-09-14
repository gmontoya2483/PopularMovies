package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.montoya.popularmoviesstg2.R;
import com.example.montoya.popularmoviesstg2.model.Video;

import java.util.ArrayList;

/**
 * Created by montoya on 14.09.2016.
 */
public class VideoArrayAdapter extends ArrayAdapter<Video> {


    private final String LOG_TAG=VideoArrayAdapter.class.getSimpleName();

    public VideoArrayAdapter(Context context, ArrayList<Video> videos){
        super(context,0,videos);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Video item=getItem(position);
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.video_list_item,parent,false);
        }


        TextView nameView=(TextView) convertView.findViewById(R.id.video_item_name);
        TextView typeView=(TextView) convertView.findViewById(R.id.video_item_type);


        nameView.setText(item.getName());
        typeView.setText(item.getType());

        return convertView;
    }
}
