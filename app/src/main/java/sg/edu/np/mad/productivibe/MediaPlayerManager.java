package sg.edu.np.mad.productivibe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

// Singleton design so that the MediaPlayer is accessible across the whole app
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

//    // To handle scenarios where another audio is playing within the app
//    // Utilising audio focus management, this pauses the active mediaPlayer
//    public void requestAudioFocus() {
//        audioManager.requestAudioFocus(
//                focusChangeListener,
//                AudioManager.STREAM_MUSIC,
//                AudioManager.AUDIOFOCUS_GAIN);
//    }
//
//    public void MediaPlayer(Context context) {
//        // Initialize AudioManager and MediaFocusChange
//        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        focusChangeListener = new MediaFocusChange(this);
//    }
//
//    public void abandonAudioFocus() {
//        audioManager.abandonAudioFocus(focusChangeListener);
//    }
//
//    public void lowerVolume() {
//        // Lower the volume of your MediaPlayer instance
//        mediaPlayer.setVolume(0.5f, 0.5f);
//    }
}
