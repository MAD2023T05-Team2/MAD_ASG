package sg.edu.np.mad.producti_vibe;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.CaseMap;
import android.util.Log;

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
    private static final String COLUMN_TASK_DATE_TIME = "task_date_time";
    private static final String COLUMN_TASK_DUE_DATE_TIME = "task_due_date_time";
    private static final String COLUMN_TASK_DURATION = "task_duration";
    private static final String COLUMN_TASK_TYPE = "task_type";
    private static final String COLUMN_REPEAT = "repeat";
    private static final String COLUMN_RECURRING_ID = "recurring_id";
    private static final String COLUMN_RECURRING_DURATION = "recurring_duration";
    private static final String COLUMN_TASK_USER_ID = "task_user_id";


    private static TaskDatabase sInstance;
    public static synchronized TaskDatabase getInstance(Context context){
        // uses the app's context, ensuring no accidental data / activity's context leakage
        if (sInstance == null){
            sInstance = new TaskDatabase(context.getApplicationContext());
        }
        return sInstance;
    }
    private TaskDatabase(Context context) {
        // private instead of public to force usage of getInstance()
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_TASK_DESC + " TEXT, " +
                COLUMN_TASK_DATE_TIME + " INTEGER, " +
                COLUMN_TASK_DUE_DATE_TIME + " INTEGER," +
                COLUMN_TASK_DURATION + " INTEGER, " +
                COLUMN_TASK_TYPE + " TEXT, " +
                COLUMN_REPEAT + " TEXT, " +
                COLUMN_RECURRING_ID + " INTEGER, " +
                COLUMN_RECURRING_DURATION + " TEXT, " +
                COLUMN_TASK_USER_ID + " INTEGER" +
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
        values.put(COLUMN_TASK_DATE_TIME, task.getTaskDateTime().getTime());
        values.put(COLUMN_TASK_DUE_DATE_TIME, task.getTaskDueDateTime().getTime());
        values.put(COLUMN_TASK_DURATION, task.getTaskDuration());
        values.put(COLUMN_TASK_TYPE, task.getTaskType());
        values.put(COLUMN_REPEAT, task.getRepeat());
        values.put(COLUMN_RECURRING_ID, task.getRecurringId());
        values.put(COLUMN_RECURRING_DURATION, task.getRecurringDuration());
        values.put(COLUMN_TASK_USER_ID, task.getTaskUserID());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_TASK_DESC, task.getTaskDesc());
        values.put(COLUMN_TASK_DATE_TIME, task.getTaskDateTime().getTime());
        values.put(COLUMN_TASK_DUE_DATE_TIME, task.getTaskDateTime().getTime());
        values.put(COLUMN_TASK_DURATION, task.getTaskDuration());
        //  Add other fields to be updated if needed !!!

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(task.getId())};

        int rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);
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
            int taskDateTimeIndex = cursor.getColumnIndex(COLUMN_TASK_DATE_TIME);
            int taskDueDateTimeIndex = cursor.getColumnIndex(COLUMN_TASK_DUE_DATE_TIME);
            int taskDurationIndex = cursor.getColumnIndex(COLUMN_TASK_DURATION);
            int taskTypeIndex = cursor.getColumnIndex(COLUMN_TASK_TYPE);
            int repeatIndex = cursor.getColumnIndex(COLUMN_REPEAT);
            int recurringIdIndex = cursor.getColumnIndex(COLUMN_RECURRING_ID);
            int recurringDurationIndex = cursor.getColumnIndex(COLUMN_RECURRING_DURATION);
            int taskUserIdIndex = cursor.getColumnIndex(COLUMN_TASK_USER_ID);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idIndex);
                String status = cursor.getString(statusIndex);
                String taskName = cursor.getString(taskNameIndex);
                String taskDesc = cursor.getString(taskDescIndex);
                long taskDateTimeMillis = cursor.getLong(taskDateTimeIndex);
                Date taskDateTime = new Date(taskDateTimeMillis);
                long taskDueDateTimeMillis = cursor.getLong(taskDueDateTimeIndex);
                Date taskDueDateTime = new Date(taskDueDateTimeMillis);
                long taskDuration = cursor.getLong(taskDurationIndex);
                String taskType = cursor.getString(taskTypeIndex);
                String repeat = cursor.getString(repeatIndex);
                int recurringId = cursor.getInt(recurringIdIndex);
                String recurringDuration = cursor.getString(recurringDurationIndex);
                int taskUserID = cursor.getInt(taskUserIdIndex);


                Task task = new Task(id, status, taskName, taskDesc, taskDateTime,
                        taskDueDateTime, taskDuration, taskType,
                        repeat, recurringId, recurringDuration, taskUserID);
                taskList.add(task);
            }
            cursor.close();
        }
        db.close();
        return taskList;


        // query for rows matching the given value
        // part of R in CRUD
        // since filtering a list is only available for API 24++

        public List<Task> getFilteredTasks(String colName, String compare,String value){
            List<Task> filteredList = new ArrayList<>();
            String QUERY_TASKS = String.format("SELECT * FROM %s WHERE %s %s %s;",
                    TABLE_NAME,
                    colName,
                    compare,
                    value);
            if (compare == "date") {
                // need to retrieve rows of tasks due that day
                // format of new Date() = Mon Jun 19 14:26:59 GMT+08:00 2023
                QUERY_TASKS = String.format("SELECT * FROM %1$s " +
                                "WHERE substr(%2$s, 8, 10) || '-' || substr(%2$s , 4, 7) || '-' || substr(%2$s , 30, 34)" +
                                " LIKE '%3$s' ;",
                        TABLE_NAME,
                        colName,
                        value);
            }
            Log.d(TABLE_NAME,QUERY_TASKS);
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(QUERY_TASKS,null);
            // instead of crashing the app, push the issues to appear in Log
            try{
                if(cursor.moveToFirst()){
                    // if it can means there is a value to retrieve
                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
                    int taskNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
                    int taskDescIndex = cursor.getColumnIndex(COLUMN_TASK_DESC);
                    int taskDateTimeIndex = cursor.getColumnIndex(COLUMN_TASK_DATE_TIME);
                    int taskDueDateTimeIndex = cursor.getColumnIndex(COLUMN_TASK_DUE_DATE_TIME);
                    int taskDurationIndex = cursor.getColumnIndex(COLUMN_TASK_DURATION);
                    int taskTypeIndex = cursor.getColumnIndex(COLUMN_TASK_TYPE);
                    int repeatIndex = cursor.getColumnIndex(COLUMN_REPEAT);
                    int recurringIdIndex = cursor.getColumnIndex(COLUMN_RECURRING_ID);
                    int recurringDurationIndex = cursor.getColumnIndex(COLUMN_RECURRING_DURATION);
                    int taskUserIdIndex = cursor.getColumnIndex(COLUMN_TASK_USER_ID);

                    do{
                        int id = cursor.getInt(idIndex);
                        String status = cursor.getString(statusIndex);
                        String taskName = cursor.getString(taskNameIndex);
                        String taskDesc = cursor.getString(taskDescIndex);
                        long taskDateTimeMillis = cursor.getLong(taskDateTimeIndex);
                        Date taskDateTime = new Date(taskDateTimeMillis);
                        long taskDueDateTimeMillis = cursor.getLong(taskDueDateTimeIndex);
                        Date taskDueDateTime = new Date(taskDueDateTimeMillis);
                        long taskDuration = cursor.getLong(taskDurationIndex);
                        String taskType = cursor.getString(taskTypeIndex);
                        String repeat = cursor.getString(repeatIndex);
                        int recurringId = cursor.getInt(recurringIdIndex);
                        String recurringDuration = cursor.getString(recurringDurationIndex);
                        int taskUserID = cursor.getInt(taskUserIdIndex);

                        Log.v(TABLE_NAME,String.format("%s %s %s", taskDueDateTime.toString().substring(8, 2),
                                taskDueDateTime.toString().substring(4, 3),
                                taskDueDateTime.toString().substring(24, 4)));

                        Task task = new Task(id, status, taskName, taskDesc, taskDateTime,
                                taskDueDateTime,taskDuration, taskType,
                                repeat, recurringId, recurringDuration, taskUserID);
                        filteredList.add(task);
                    }
                    while (cursor.moveToNext());
                }
            } catch (Exception e){
                Log.d(TABLE_NAME,"Error retrieving values from database");
            }
            finally {
                if(cursor != null && !cursor.isClosed()){
                    cursor.close();
                    // close connection
                }
            }
            return filteredList;
        }


    }}

//public class TaskDatabase extends SQLiteOpenHelper {
//    private static final String DATABASE_NAME = "task_database";
//    private static final int DATABASE_VERSION = 1;
//    private static final String TABLE_NAME = "tasks";
//    private static final String COLUMN_ID = "id";
//    private static final String COLUMN_STATUS = "status";
//    private static final String COLUMN_TASK_NAME = "task_name";
//    private static final String COLUMN_TASK_DESC = "task_desc";
//    private static final String COLUMN_TASK_DATE = "task_date";
//    private static final String COLUMN_TASK_START_TIME = "task_start_time";
//    private static final String COLUMN_TASK_END_TIME = "task_end_time";
//    private static final String COLUMN_TASK_DURATION = "task_duration";
//    private static final String COLUMN_TASK_TYPE = "task_type";
//    private static final String COLUMN_REPEAT = "repeat";
//    private static final String COLUMN_RECURRING_ID = "recurring_id";
//    private static final String COLUMN_RECURRING_DURATION = "recurring_duration";
//    private static final String COLUMN_TASK_USER_ID = "task_user_id";
//    private static final String COLUMN_DUE_DATE = "due_date";
//
//    private static TaskDatabase sInstance;
//    public static synchronized TaskDatabase getInstance(Context context){
//        // uses the app's context, ensuring no accidental data / activity's context leakage
//        if (sInstance == null){
//            sInstance = new TaskDatabase(context.getApplicationContext());
//        }
//        return sInstance;
//    }
//    private TaskDatabase(Context context) {
//        // private instead of public to force usage of getInstance()
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
//                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COLUMN_STATUS + " TEXT, " +
//                COLUMN_TASK_NAME + " TEXT, " +
//                COLUMN_TASK_DESC + " TEXT, " +
//                COLUMN_TASK_DATE + " INTEGER, " +
//                COLUMN_TASK_START_TIME + " TEXT, " +
//                COLUMN_TASK_END_TIME + " TEXT, " +
//                COLUMN_TASK_DURATION + " INTEGER, " +
//                COLUMN_TASK_TYPE + " TEXT, " +
//                COLUMN_REPEAT + " TEXT, " +
//                COLUMN_RECURRING_ID + " INTEGER, " +
//                COLUMN_RECURRING_DURATION + " TEXT, " +
//                COLUMN_TASK_USER_ID + " INTEGER," +
//                COLUMN_DUE_DATE + " INTEGER" +
//                ")";
//        db.execSQL(createTableQuery);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
//        db.execSQL(dropTableQuery);
//        onCreate(db);
//    }
//
//    public void addTask(Task task) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_STATUS, task.getStatus());
//        values.put(COLUMN_TASK_NAME, task.getTaskName());
//        values.put(COLUMN_TASK_DESC, task.getTaskDesc());
//        values.put(COLUMN_TASK_DATE, task.getTaskDate().getTime());
//        values.put(COLUMN_TASK_START_TIME, task.getTaskStartTime());
//        values.put(COLUMN_TASK_END_TIME, task.getTaskEndTime());
//        values.put(COLUMN_TASK_DURATION, task.getTaskDuration());
//        values.put(COLUMN_TASK_TYPE, task.getTaskType());
//        values.put(COLUMN_REPEAT, task.getRepeat());
//        values.put(COLUMN_RECURRING_ID, task.getRecurringId());
//        values.put(COLUMN_RECURRING_DURATION, task.getRecurringDuration());
//        values.put(COLUMN_TASK_USER_ID, task.getTaskUserID());
//        values.put(COLUMN_DUE_DATE, task.getDueDate().getTime());
//        db.insert(TABLE_NAME, null, values);
//        db.close();
//    }
//
//    public List<Task> getAllTasks() {
//        List<Task> taskList = new ArrayList<>();
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
//        if (cursor != null) {
//            int idIndex = cursor.getColumnIndex(COLUMN_ID);
//            int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
//            int taskNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
//            int taskDescIndex = cursor.getColumnIndex(COLUMN_TASK_DESC);
//            int taskDateIndex = cursor.getColumnIndex(COLUMN_TASK_DATE);
//            int taskStartTimeIndex = cursor.getColumnIndex(COLUMN_TASK_START_TIME);
//            int taskEndTimeIndex = cursor.getColumnIndex(COLUMN_TASK_END_TIME);
//            int taskDurationIndex = cursor.getColumnIndex(COLUMN_TASK_DURATION);
//            int taskTypeIndex = cursor.getColumnIndex(COLUMN_TASK_TYPE);
//            int repeatIndex = cursor.getColumnIndex(COLUMN_REPEAT);
//            int recurringIdIndex = cursor.getColumnIndex(COLUMN_RECURRING_ID);
//            int recurringDurationIndex = cursor.getColumnIndex(COLUMN_RECURRING_DURATION);
//            int taskUserIdIndex = cursor.getColumnIndex(COLUMN_TASK_USER_ID);
//            int taskDueDateIndex = cursor.getColumnIndex(COLUMN_DUE_DATE);
//
//            while (cursor.moveToNext()) {
//                int id = cursor.getInt(idIndex);
//                String status = cursor.getString(statusIndex);
//                String taskName = cursor.getString(taskNameIndex);
//                String taskDesc = cursor.getString(taskDescIndex);
//                long taskDateMillis = cursor.getLong(taskDateIndex);
//                Date taskDate = new Date(taskDateMillis);
//                String taskStartTime = cursor.getString(taskStartTimeIndex);
//                String taskEndTime = cursor.getString(taskEndTimeIndex);
//                long taskDuration = cursor.getLong(taskDurationIndex);
//                String taskType = cursor.getString(taskTypeIndex);
//                String repeat = cursor.getString(repeatIndex);
//                int recurringId = cursor.getInt(recurringIdIndex);
//                String recurringDuration = cursor.getString(recurringDurationIndex);
//                int taskUserID = cursor.getInt(taskUserIdIndex);
//                long taskDueDateMillis = cursor.getLong(taskDueDateIndex);
//                Date taskDueDate = new Date(taskDueDateMillis);
//                Log.v(TABLE_NAME,String.format("%s-%s-%s", taskDueDate.toString().substring(8, 10),
//                        taskDueDate.toString().substring(4, 7),
//                        taskDueDate.toString().substring(30, 34)));
//
//                Task task = new Task(id, status, taskName, taskDesc, taskDate,
//                        taskStartTime, taskEndTime, taskDuration, taskType,
//                        repeat, recurringId, recurringDuration, taskUserID, taskDueDate);
//                taskList.add(task);
//            }
//            cursor.close();
//        }
//        db.close();
//        return taskList;
//    }
//
//    // query for rows matching the given value
//    // part of R in CRUD
//    // since filtering a list is only available for API 24++
//
//    public List<Task> getFilteredTasks(String colName, String compare,String value){
//        List<Task> filteredList = new ArrayList<>();
//        String QUERY_TASKS = String.format("SELECT * FROM %s WHERE %s %s %s;",
//                TABLE_NAME,
//                colName,
//                compare,
//                value);
//        if (compare == "date") {
//            // need to retrieve rows of tasks due that day
//            // format of new Date() = Mon Jun 19 14:26:59 GMT+08:00 2023
//            QUERY_TASKS = String.format("SELECT * FROM %1$s " +
//                            "WHERE substr(%2$s, 8, 10) || '-' || substr(%2$s , 4, 7) || '-' || substr(%2$s , 30, 34)" +
//                            " LIKE '%3$s' ;",
//                    TABLE_NAME,
//                    colName,
//                    value);
//        }
//        Log.d(TABLE_NAME,QUERY_TASKS);
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery(QUERY_TASKS,null);
//        // instead of crashing the app, push the issues to appear in Log
//        try{
//            if(cursor.moveToFirst()){
//                // if it can means there is a value to retrieve
//                int idIndex = cursor.getColumnIndex(COLUMN_ID);
//                int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
//                int taskNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
//                int taskDescIndex = cursor.getColumnIndex(COLUMN_TASK_DESC);
//                int taskDateIndex = cursor.getColumnIndex(COLUMN_TASK_DATE);
//                int taskStartTimeIndex = cursor.getColumnIndex(COLUMN_TASK_START_TIME);
//                int taskEndTimeIndex = cursor.getColumnIndex(COLUMN_TASK_END_TIME);
//                int taskDurationIndex = cursor.getColumnIndex(COLUMN_TASK_DURATION);
//                int taskTypeIndex = cursor.getColumnIndex(COLUMN_TASK_TYPE);
//                int repeatIndex = cursor.getColumnIndex(COLUMN_REPEAT);
//                int recurringIdIndex = cursor.getColumnIndex(COLUMN_RECURRING_ID);
//                int recurringDurationIndex = cursor.getColumnIndex(COLUMN_RECURRING_DURATION);
//                int taskUserIdIndex = cursor.getColumnIndex(COLUMN_TASK_USER_ID);
//                int taskDueDateIndex = cursor.getColumnIndex(COLUMN_DUE_DATE);
//                do{
//                    int id = cursor.getInt(idIndex);
//                    String status = cursor.getString(statusIndex);
//                    String taskName = cursor.getString(taskNameIndex);
//                    String taskDesc = cursor.getString(taskDescIndex);
//                    long taskDateMillis = cursor.getLong(taskDateIndex);
//                    Date taskDate = new Date(taskDateMillis);
//                    String taskStartTime = cursor.getString(taskStartTimeIndex);
//                    String taskEndTime = cursor.getString(taskEndTimeIndex);
//                    long taskDuration = cursor.getLong(taskDurationIndex);
//                    String taskType = cursor.getString(taskTypeIndex);
//                    String repeat = cursor.getString(repeatIndex);
//                    int recurringId = cursor.getInt(recurringIdIndex);
//                    String recurringDuration = cursor.getString(recurringDurationIndex);
//                    int taskUserID = cursor.getInt(taskUserIdIndex);
//                    long taskDueDateMillis = cursor.getLong(taskDueDateIndex);
//                    Date taskDueDate = new Date(taskDueDateMillis);
//
//                    Log.v(TABLE_NAME,String.format("%s %s %s", taskDueDate.toString().substring(8, 2),
//                            taskDueDate.toString().substring(4, 3),
//                            taskDueDate.toString().substring(24, 4)));
//
//                    Task task = new Task(id, status, taskName, taskDesc, taskDate,
//                            taskStartTime, taskEndTime, taskDuration, taskType,
//                            repeat, recurringId, recurringDuration, taskUserID, taskDueDate);
//                    filteredList.add(task);
//                }
//                while (cursor.moveToNext());
//            }
//        } catch (Exception e){
//            Log.d(TABLE_NAME,"Error retrieving values from database");
//        }
//        finally {
//            if(cursor != null && !cursor.isClosed()){
//                cursor.close();
//                // close connection
//            }
//        }
//        return filteredList;
//    }
//
//
//    public void updateTask(Task task) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_TASK_NAME, task.getTaskName());
//        values.put(COLUMN_TASK_DESC, task.getTaskDesc());
//        values.put(COLUMN_TASK_START_TIME, task.getTaskStartTime());
//        values.put(COLUMN_TASK_END_TIME, task.getTaskEndTime());
//        values.put(COLUMN_TASK_DURATION, task.getTaskDuration());
//        //  Add other fields to be updated if needed !!!
//
//        String selection = COLUMN_ID + " = ?";
//        String[] selectionArgs = {String.valueOf(task.getId())};
//
//        int rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);
//        db.close();
//    }
//}


