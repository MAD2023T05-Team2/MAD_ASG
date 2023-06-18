package com.example.mad_asg;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Pictures extends Fragment {

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pictures, container, false);
        return view;
    }
//    imageList = new ArrayList<>();
//    RecyclerView recyclerView = findViewById(R.id.recyclerview);
//    CustomAdapter custAdapter = new CustomAdapter(myList);
//    LinearLayoutManager mylayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mylayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(custAdapter);
}