package com.example.mad_asg;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
//import com.bumptech.glide.Glide;

public class DestressMessage extends Fragment {

    View view;
    ImageView gif;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_destress_message, container, false);
        return view;
    }

    /*gif = findViewById(R.id.gif);

    // Adding the gif here using glide library
        Glide.with(this).load(R.drawable.frog).into(gif);
*/
}