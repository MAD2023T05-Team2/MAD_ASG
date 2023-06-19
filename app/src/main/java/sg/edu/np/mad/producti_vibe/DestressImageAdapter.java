//package com.example.mad_asg;
//
//import android.media.Image;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
//    private List<Image> imageList;
//    //private int selectedPosition = RecyclerView.NO_POSITION;
//
//    @NonNull
//    @Override
//    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.task_item, parent, false);
//        return new ImageAdapter.ViewHolder(imageView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//}
//public class ImageViewHolder extends RecyclerView.ViewHolder {
//    TextView textView;
//    ImageView image;
//    public ImageViewHolder(@NonNull View itemView) {
//        super(itemView);
//        textView = itemView.findViewById(R.id.textView3);
//        image = itemView.findViewById(R.id.imageView);
//    }
//}