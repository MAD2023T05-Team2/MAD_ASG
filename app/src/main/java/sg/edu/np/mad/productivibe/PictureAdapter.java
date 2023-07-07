package sg.edu.np.mad.productivibe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {
    private List<Integer> pictureList;

    public PictureAdapter(List<Integer> pictureList) {
        this.pictureList = pictureList;
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        int pictureResId = pictureList.get(position);
        holder.bind(pictureResId);
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    public static class PictureViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(int pictureResId) {
            Glide.with(itemView).load(pictureResId).into(imageView);
        }
    }
}

