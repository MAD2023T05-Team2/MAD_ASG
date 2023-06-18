package com.example.mad_asg;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DestressViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    ImageView image;
    public DestressViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView3);
        image = itemView.findViewById(R.id.imageView);
    }
}