package sg.edu.np.mad.producti_vibe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;


public class DIAdpater extends RecyclerView.Adapter<DIAdpater.ViewHolder> {
    Context context;
    int[] destressImages;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView rowImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Accepts the entire row & searches for the subview
            rowImage = itemView.findViewById(R.id.imageView);
        }
    }

    public DIAdpater(Context context, int[] destressImages) {
        // Initialise the variables with values received
        this.context = context;
        this.destressImages = destressImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creating new views to be invoked
        View view = LayoutInflater.from(context).
                inflate(R.layout.di_singular, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Replaces content in the view
        holder.rowImage.setImageResource(destressImages[position]);
    }

    @Override
    public int getItemCount() {
        return destressImages.length;
    }
}
