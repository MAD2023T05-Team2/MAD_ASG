package sg.edu.np.mad.producti_vibe;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "task_database";
    private static final int DATABASE_VERSION = 1;
    public static final String SHARED_PREFS = "sharedPrefs";

    // for tasks
    //private static final String TABLE_NAME = "tasks";

    // Columns names used in the database tables
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
    private static String COLUMN_TASK_USER_ID = "task_user_id";

    // for user accounts
    private static final String COLUMN_USERID = "user_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";


    //ensures that only one instance of the database is created
    private static Database sInstance;
    public static synchronized Database getInstance(Context context){
        // uses the app's context, ensuring no accidental data / activity's context leakage
        if (sInstance == null){
            sInstance = new Database(context.getApplicationContext());
        }
        return sInstance;
    }
    private Database(Context context) {
        // private instead of public to force usage of getInstance()
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTasksTable = "CREATE TABLE " + "tasks" + " (" +
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
                COLUMN_TASK_USER_ID + " INTEGER," + "FOREIGN KEY (" + COLUMN_TASK_USER_ID + ") REFERENCES users(" + COLUMN_USERID +")" +
                ")";
        db.execSQL(createTasksTable);

        String createUserAccountsTable = "CREATE TABLE " + "user_accounts" + " (" +
                COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_NAME + " TEXT, "+
                COLUMN_USERNAME + " TEXT, "+
                COLUMN_PASSWORD + " TEXT"+
                ")";
        db.execSQL(createUserAccountsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "tasks");
        db.execSQL("DROP TABLE IF EXISTS " + "user_accounts");
        onCreate(db);
    }

    // adds task to database when user creates a task
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
        db.insert("tasks", null, values);
        db.close();
    }

    // updates and existing task in the database
    public void updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase(); //obtains writable instance of database
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_TASK_DESC, task.getTaskDesc());
        values.put(COLUMN_TASK_DATE_TIME, task.getTaskDateTime().getTime());
        values.put(COLUMN_TASK_DUE_DATE_TIME, task.getTaskDueDateTime().getTime());
        values.put(COLUMN_TASK_DURATION, task.getTaskDuration());
        values.put(COLUMN_STATUS, task.getStatus());

        String selection = COLUMN_ID + " = ?"; //update applied to row with matchingID
        String[] selectionArgs = {String.valueOf(task.getId())}; //values to be substituted into selection criteria

        int rowsAffected = db.update("tasks", values, selection, selectionArgs);
        db.close();
    }

    // deleting task
    public void deleteTask(int taskId) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};
        db.delete("tasks", selection, selectionArgs); //performs the delete operation
        db.close();
    }

    // getAllTasksFromUser grabs the task for the user based on the user ID
    public List<Task> getAllTasksFromUser(String userId) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tasks where "+COLUMN_TASK_USER_ID+"='" +userId+"'", null);
        // Cursor cursor = db.query("tasks", null, null, null, null, null, null);
        // SQL query fixed so it retrieves based on the unique user ID
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
    }

    // Add new user data to the database
    public void addUser(User userData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, userData.getName());
        values.put(COLUMN_USERNAME, userData.getUserName());
        values.put(COLUMN_PASSWORD, userData.getPassWord());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("user_accounts", null, values);
        db.close();
    }

    // Find the username from the database and retrieve the relevant user object
    public User findUserData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        // SQL query fixed so it retrieves based on the unique username
        Cursor cursor = db.rawQuery("SELECT * FROM user_accounts WHERE " + COLUMN_USERNAME + " = '" + username + "'", null);
        User queryUserData = new User();

        if (cursor.moveToFirst()){
            queryUserData.setUserId(cursor.getInt(0));
            queryUserData.setName(cursor.getString(1));
            queryUserData.setUserName(cursor.getString(2));
            queryUserData.setPassWord(cursor.getString(3));
        }
        else{
            queryUserData = null;
        }
        cursor.close();
        db.close();
        return queryUserData;
    }
}

// CODE BELOW NOT USED ANYMORE cuz it grabs all tasks from database instead of only the userID
// use the getAllTasksFromUser instead to retrieve
//    public List<Task> getAllTasks() {
//        List<Task> taskList = new ArrayList<>();
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query("tasks", null, null, null, null, null, null);
//        if (cursor != null) {
//            int idIndex = cursor.getColumnIndex(COLUMN_ID);
//            int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
//            int taskNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
//            int taskDescIndex = cursor.getColumnIndex(COLUMN_TASK_DESC);
//            int taskDateTimeIndex = cursor.getColumnIndex(COLUMN_TASK_DATE_TIME);
//            int taskDueDateTimeIndex = cursor.getColumnIndex(COLUMN_TASK_DUE_DATE_TIME);
//            int taskDurationIndex = cursor.getColumnIndex(COLUMN_TASK_DURATION);
//            int taskTypeIndex = cursor.getColumnIndex(COLUMN_TASK_TYPE);
//            int repeatIndex = cursor.getColumnIndex(COLUMN_REPEAT);
//            int recurringIdIndex = cursor.getColumnIndex(COLUMN_RECURRING_ID);
//            int recurringDurationIndex = cursor.getColumnIndex(COLUMN_RECURRING_DURATION);
//            int taskUserIdIndex = cursor.getColumnIndex(COLUMN_TASK_USER_ID);
//
//            while (cursor.moveToNext()) {
//                int id = cursor.getInt(idIndex);
//                String status = cursor.getString(statusIndex);
//                String taskName = cursor.getString(taskNameIndex);
//                String taskDesc = cursor.getString(taskDescIndex);
//                long taskDateTimeMillis = cursor.getLong(taskDateTimeIndex);
//                Date taskDateTime = new Date(taskDateTimeMillis);
//                long taskDueDateTimeMillis = cursor.getLong(taskDueDateTimeIndex);
//                Date taskDueDateTime = new Date(taskDueDateTimeMillis);
//                long taskDuration = cursor.getLong(taskDurationIndex);
//                String taskType = cursor.getString(taskTypeIndex);
//                String repeat = cursor.getString(repeatIndex);
//                int recurringId = cursor.getInt(recurringIdIndex);
//                String recurringDuration = cursor.getString(recurringDurationIndex);
//                int taskUserID = cursor.getInt(taskUserIdIndex);
//
//                Task task = new Task(id, status, taskName, taskDesc, taskDateTime,
//                        taskDueDateTime, taskDuration, taskType,
//                        repeat, recurringId, recurringDuration, taskUserID);
//                taskList.add(task);
//            }
//            cursor.close();
//        }
//        db.close();
//        return taskList;
//    }
