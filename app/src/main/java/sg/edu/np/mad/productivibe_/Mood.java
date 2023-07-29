package sg.edu.np.mad.productivibe_;

public class Mood {
    private String userName;
    private String mood;
    private long timestamp;

    public Mood(String userName, String mood, long timestamp) {
        this.userName = userName;
        this.mood = mood;
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public String getMood() {
        return mood;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

