package sg.edu.np.mad.productivibe;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "task_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_TASK_NAME = "task_name";
    private static final String COLUMN_TASK_DESC = "task_desc";
    private static final String COLUMN_TASK_DATE = "task_date";
    private static final String COLUMN_TASK_START_TIME = "task_start_time";
    private static final String COLUMN_TASK_END_TIME = "task_end_time";
    private static final String COLUMN_TASK_DURATION = "task_duration";
    private static final String COLUMN_TASK_TYPE = "task_type";
    private static final String COLUMN_REPEAT = "repeat";
    private static final String COLUMN_RECURRING_ID = "recurring_id";
    private static final String COLUMN_RECURRING_DURATION = "recurring_duration";
    private static final String COLUMN_TASK_USER_ID = "task_user_id";
    private static final String COLUMN_DUE_DATE = "due_date";

    public TaskDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_TASK_DESC + " TEXT, " +
                COLUMN_TASK_DATE + " INTEGER, " +
                COLUMN_TASK_START_TIME + " TEXT, " +
                COLUMN_TASK_END_TIME + " TEXT, " +
                COLUMN_TASK_DURATION + " INTEGER, " +
                COLUMN_TASK_TYPE + " TEXT, " +
                COLUMN_REPEAT + " TEXT, " +
                COLUMN_RECURRING_ID + " INTEGER, " +
                COLUMN_RECURRING_DURATION + " TEXT, " +
                COLUMN_TASK_USER_ID + " INTEGER," +
                COLUMN_DUE_DATE + " INTEGER" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, task.getStatus());
        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_TASK_DESC, task.getTaskDesc());
        values.put(COLUMN_TASK_DATE, task.getTaskDate().getTime());
        values.put(COLUMN_TASK_START_TIME, task.getTaskStartTime());
        values.put(COLUMN_TASK_END_TIME, task.getTaskEndTime());
        values.put(COLUMN_TASK_DURATION, task.getTaskDuration());
        values.put(COLUMN_TASK_TYPE, task.getTaskType());
        values.put(COLUMN_REPEAT, task.getRepeat());
        values.put(COLUMN_RECURRING_ID, task.getRecurringId());
        values.put(COLUMN_RECURRING_DURATION, task.getRecurringDuration());
        values.put(COLUMN_TASK_USER_ID, task.getTaskUserID());
        values.put(COLUMN_DUE_DATE, task.getDueDate().getTime());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
            int taskNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
            int taskDescIndex = cursor.getColumnIndex(COLUMN_TASK_DESC);
            int taskDateIndex = cursor.getColumnIndex(COLUMN_TASK_DATE);
            int taskStartTimeIndex = cursor.getColumnIndex(COLUMN_TASK_START_TIME);
            int taskEndTimeIndex = cursor.getColumnIndex(COLUMN_TASK_END_TIME);
            int taskDurationIndex = cursor.getColumnIndex(COLUMN_TASK_DURATION);
            int taskTypeIndex = cursor.getColumnIndex(COLUMN_TASK_TYPE);
            int repeatIndex = cursor.getColumnIndex(COLUMN_REPEAT);
            int recurringIdIndex = cursor.getColumnIndex(COLUMN_RECURRING_ID);
            int recurringDurationIndex = cursor.getColumnIndex(COLUMN_RECURRING_DURATION);
            int taskUserIdIndex = cursor.getColumnIndex(COLUMN_TASK_USER_ID);
            int taskDueDateIndex = cursor.getColumnIndex(COLUMN_DUE_DATE);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idIndex);
                String status = cursor.getString(statusIndex);
                String taskName = cursor.getString(taskNameIndex);
                String taskDesc = cursor.getString(taskDescIndex);
                long taskDateMillis = cursor.getLong(taskDateIndex);
                Date taskDate = new Date(taskDateMillis);
                String taskStartTime = cursor.getString(taskStartTimeIndex);
                String taskEndTime = cursor.getString(taskEndTimeIndex);
                long taskDuration = cursor.getLong(taskDurationIndex);
                String taskType = cursor.getString(taskTypeIndex);
                String repeat = cursor.getString(repeatIndex);
                int recurringId = cursor.getInt(recurringIdIndex);
                String recurringDuration = cursor.getString(recurringDurationIndex);
                int taskUserID = cursor.getInt(taskUserIdIndex);
                long taskDueDateMillis = cursor.getLong(taskDueDateIndex);
                Date taskDueDate = new Date(taskDueDateMillis);

                Task task = new Task(id, status, taskName, taskDesc, taskDate,
                        taskStartTime, taskEndTime, taskDuration, taskType,
                        repeat, recurringId, recurringDuration, taskUserID, taskDueDate);
                taskList.add(task);
            }
            cursor.close();
        }
        db.close();
        return taskList;
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_TASK_DESC, task.getTaskDesc());
        values.put(COLUMN_TASK_START_TIME, task.getTaskStartTime());
        values.put(COLUMN_TASK_END_TIME, task.getTaskEndTime());
        values.put(COLUMN_TASK_DURATION, task.getTaskDuration());
        //  Add other fields to be updated if needed !!!

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(task.getId())};

        int rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }
}


