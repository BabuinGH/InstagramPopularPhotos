package com.example.babusr.instagramclient;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    // What data do we need from the activity
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //what our items look like
    //Use the template to display each photo

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for this position
        InstagramPhoto photo = getItem(position);
        //Check if we are using the recycled view, if not we will inflate
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent,false);
        }
        //Lookup the views for populating the data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        //Insert the model data into each of the view items
        tvCaption.setText(photo.caption);
        //Clear out the image view
        ivPhoto.setImageResource(0);
        //Insert the image using Picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        //Return the created item as a view
        return convertView;

    }
}
