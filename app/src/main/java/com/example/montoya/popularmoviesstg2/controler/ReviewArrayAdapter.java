package com.example.montoya.popularmoviesstg2.controler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.montoya.popularmoviesstg2.R;
import com.example.montoya.popularmoviesstg2.model.Review;

import java.util.ArrayList;

/**
 * Created by montoya on 27.09.2016.
 */

public class ReviewArrayAdapter extends ArrayAdapter<Review> {

    public ReviewArrayAdapter(Context context, ArrayList<Review> reviews) {
        super(context, 0,reviews);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review item=getItem(position);
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.review_list_item,parent,false);
        }


        TextView author=(TextView) convertView.findViewById(R.id.review_author);


        author.setText(item.getAuthor());


        return convertView;



    }
}
