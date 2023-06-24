package sg.edu.np.mad.producti_vibe;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Integer> videoList;
    public VideoAdapter(List<Integer> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        int videoResId = videoList.get(position);
        holder.bindVideo(videoResId);

        // Setting desired bottom margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position < getItemCount() - 1) {
            int bottomMargin = holder.itemView.getContext().getResources()
                    .getDimensionPixelSize(R.dimen.video_item_bottom_margin);
            layoutParams.bottomMargin = bottomMargin;
        } else {
            layoutParams.bottomMargin = 0; // No bottom margin for the last item
        }
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private VideoView videoView;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
        }

        public void bindVideo(int videoResId) {
            Context context = itemView.getContext();
            // Set the video resource for the VideoView
            videoView.setVideoPath("android.resource://" + context.getPackageName() + "/" + videoResId);

            // Optional: Add media controller for playback controls
            MediaController mediaController = new MediaController(context);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);

            // Don't start playing the video upon launching the fragment
        }
    }
}