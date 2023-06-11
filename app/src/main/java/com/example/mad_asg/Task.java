package com.example.mad_asg;
public class Task { private String TAG = "Task Class";

    private int Id;
    private String status;
    private String taskName;
    private String taskDesc;

    private String taskDate;
    private String taskStartTime;
    private String taskEndTime;
    private long taskDuration;
    private String alert;
    private String alertDateTime;
    private String taskType;
    private String repeat;

    private int recurringId;

    private String recurringDuration;

    private int taskUserID;

    public Task(int Id, String status, String taskName, String taskDesc, String taskDate, String taskStartTime, String taskEndTime, long taskDuration, String alert,
                String alertDateTime, String taskType, String repeat, int recurringId, String recurringDuration, int taskUserID) {
        this.Id = Id;
        this.status = status;
        this.taskName = taskName;
        this.taskDesc = taskDesc;

        this.taskDate = taskDate;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskDuration = taskDuration;

        this.alert = alert;
        this.alertDateTime = alertDateTime;

        this.taskType = taskType;
        this.repeat = repeat;

        this.recurringId = recurringId;

        this.recurringDuration = recurringDuration;

        this.taskUserID = taskUserID;

    }
    public String getTaskName() {
        return taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public String getTaskDate() {
        return taskDate;
    }
    public String getTaskStartTime() {
        return taskStartTime;
    }
    public String getTaskEndTime() {
        return taskEndTime;
    }

    public String getAlert() {
        return alert;
    }
    public String getAlertDateTime() {
        return alertDateTime;
    }
    public String getRepeat() {
        return repeat;
    }
    public String getRecurringDuration() {
        return recurringDuration;
    }

    public String getStatus() {
        return status;
    }
}