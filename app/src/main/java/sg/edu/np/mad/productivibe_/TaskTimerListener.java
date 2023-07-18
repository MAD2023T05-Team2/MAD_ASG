package sg.edu.np.mad.productivibe_;

// Listener to connect the TaskTimerPage and TaskTimerView
// Listener acts as a connector and tells the two pages what to do
public interface TaskTimerListener {
    void onTaskTimerUpdate(long duration);

}
