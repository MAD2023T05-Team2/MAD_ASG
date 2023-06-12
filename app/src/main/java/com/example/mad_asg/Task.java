package com.example.mad_asg;

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

    public Task(int id, String status, String taskName, String taskDesc, Date taskDate,
                String taskStartTime, String taskEndTime, long taskDuration, String taskType,
                String repeat, int recurringId, String recurringDuration, int taskUserID) {
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
}
