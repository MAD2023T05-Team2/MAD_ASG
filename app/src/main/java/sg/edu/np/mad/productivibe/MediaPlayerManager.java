package sg.edu.np.mad.productivibe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

// Implemented BGM into the app to help set a calming mood for the user
// Singleton design so that the MediaPlayer is accessible across the whole app, increases scope so that the BGM plays outside of just 1 activity.
public class MediaPlayerManager {
    private static MediaPlayerManager instance;
    private MediaPlayer mediaPlayer;
    private String musicSource;

    private MediaPlayerManager() {
        // Initialize MediaPlayer instance
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // Called when the MediaPlayer is prepared and ready to start playback
                mediaPlayer.start();
            }
        });
    }

    public static synchronized MediaPlayerManager getInstance() {
        // Get instance to be used throughout the whole application
        if (instance == null) {
            instance = new MediaPlayerManager();
        }
        return instance;
    }

    public void setMusicSource(Context context, int rawResourceId) {
        String newMusicSource = context.getResources().getResourceName(rawResourceId);

        // Check if the new track is the same as the current one
        if (musicSource != null && musicSource.equals(newMusicSource)) {
            return; // If same, skip setting the new track
        }
        try {
            AssetFileDescriptor fileDescriptor = context.getResources().openRawResourceFd(rawResourceId);
            if (fileDescriptor != null) {
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                mediaPlayer.prepare();
                fileDescriptor.close();
                // Update current music source
                musicSource = newMusicSource;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLooping(boolean isLooping) {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setLooping(isLooping);
                } else if (mediaPlayer.isPlaying() || mediaPlayer.getCurrentPosition() > 0) {
                    mediaPlayer.setLooping(isLooping);
                } else {
                    Log.e(TAG, "MediaPlayer is not in a valid state for setting looping behavior.");
                }
            } else {
                Log.e(TAG, "MediaPlayer is not initialized");
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "Error setting looping behavior: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // To control start/pause/ending of audio lifecycle
    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void resume() {
        mediaPlayer.start();
    }

    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
