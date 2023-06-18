package sg.edu.np.mad.productivibe;

import java.util.Date;

public class Task {
    private int Id;
    private String status;
    private String taskName;
    private String taskDesc;
    private Date taskDate;
    private String taskStartTime;
    private String taskEndTime;
    private long taskDuration;
    private String taskType;
    private String repeat;
    private int recurringId;
    private String recurringDuration;
    private int taskUserID;

    private Date dueDate;

    public Task(int id, String status, String taskName, String taskDesc, Date taskDate,
                String taskStartTime, String taskEndTime, long taskDuration, String taskType,
                String repeat, int recurringId, String recurringDuration, int taskUserID, Date dueDate) {
        this.Id = id;
        this.status = status;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskDate = taskDate;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskDuration = taskDuration;
        this.taskType = taskType;
        this.repeat = repeat;
        this.recurringId = recurringId;
        this.recurringDuration = recurringDuration;
        this.taskUserID = taskUserID;
        this.dueDate = dueDate;
    }

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

    public Date getTaskDate() {
        return taskDate;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
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
    public Date getDueDate() {
        return dueDate;
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

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
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

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }}


