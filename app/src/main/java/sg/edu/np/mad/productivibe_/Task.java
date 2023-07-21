package sg.edu.np.mad.productivibe_;

import java.util.Date;

public class Task {
    private int Id;
    private String status;
    private String taskName;
    private String taskDesc;
    private String taskDateTime;
    private String taskDueDateTime;
    private long taskDuration;
    private String taskType;
    private String repeat;
    private int recurringId;
    private String recurringDuration;
    private int taskUserID;


    public Task(int id, String status, String taskName, String taskDesc, String taskDateTime,
                String taskDueDateTime, long taskDuration, String taskType,
                String repeat, int recurringId, String recurringDuration, int taskUserID) {
        this.Id = id;
        this.status = status;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskDateTime = taskDateTime;
        this.taskDueDateTime = taskDueDateTime;
        this.taskDuration = taskDuration;
        this.taskType = taskType;
        this.repeat = repeat;
        this.recurringId = recurringId;
        this.recurringDuration = recurringDuration;
        this.taskUserID = taskUserID;
    }
    // null task construct
    public Task(){};

    public int getId() {
        return Id;
    }

    public String getStatus() {
        return status;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public String getTaskDateTime() {
        return taskDateTime;
    }

    public String getTaskDueDateTime() {
        return taskDueDateTime;
    }

    public long getTaskDuration() {
        return taskDuration;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getRepeat() {
        return repeat;
    }

    public int getRecurringId() {
        return recurringId;
    }

    public String getRecurringDuration() {
        return recurringDuration;
    }

    public int getTaskUserID() {
        return taskUserID;
    }


    public void setId(int id) {
        Id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public void setTaskDateTime(String taskDateTime) {
        this.taskDateTime = taskDateTime;
    }

    public void setTaskDueDateTime(String taskDueDateTime) {
        this.taskDueDateTime = taskDueDateTime;
    }

    public void setTaskDuration(long taskDuration) {
        this.taskDuration = taskDuration;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public void setRecurringId(int recurringId) {
        this.recurringId = recurringId;
    }

    public void setRecurringDuration(String recurringDuration) {
        this.recurringDuration = recurringDuration;
    }

    public void setTaskUserID(int taskUserID) {
        this.taskUserID = taskUserID;
    }
}





