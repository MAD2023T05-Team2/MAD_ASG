package sg.edu.np.mad.productivibe;

import java.util.Date;

public class Mood {
    private String userId;
    private String mood;
    private String timestamp;

    public Mood(String userId, String mood, String timestamp) {
        this.userId = userId;
        this.mood = mood;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getMood() {
        return mood;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

