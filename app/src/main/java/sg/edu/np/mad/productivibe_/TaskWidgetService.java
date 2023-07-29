package sg.edu.np.mad.productivibe_;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.v("Service", "onGetViewFactory");
        return new WidgetRemoteViewsFactory(getApplicationContext());
    }

    class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private final Context context;
        //private Database db;
        private ArrayList<Task> widgetTaskList = new ArrayList<>();

        private DatabaseReference taskDBR;
        private FirebaseDatabase fdb;

        public WidgetRemoteViewsFactory(Context context) {
            this.context = context;
            //this.db = Database.getInstance(context.getApplicationContext());;
        }

        @Override
        public void onCreate() {
            loadTasks(new GetTaskData() {

                @Override
                public void onDataLoaded(List<Task> loadedTaskList) {
                    Log.d("Firebase Service", String.valueOf(loadedTaskList.size()));
                    Log.d("Service","onCreate");
                }
                @Override
                public void onError(String errorMsg) {
                    Log.d("loadData",errorMsg);

                }
            });
            Log.d("Service","GetTaskData");
        }

        @Override
        public void onDataSetChanged() {

            //widgetTaskList.clear();

            Log.v("Service", "widgetTaskList, on DataSetChanged");

            // if dataset changed, list is cleared to store the new list of tasks
            //widgetTaskList.clear();
            // getting stored userid
            //SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", 0);
            //String userId = sharedPreferences.getString("UserId", null);

            // create list of today task based on the user
            // for testing
            //Task newTask = new Task(1, "Pending", " ", "taskDesc", "29/07/23 00:34", "29/07/23 13:34", Integer.parseInt("2"), "Type", "Repeat", 0, "", 0);
            //Task newTask2 = new Task(2, "Pending", "for widget 2", "taskDesc", "29/07/23 00:34", "29/07/23 23:34", Integer.parseInt("2"), "Type", "Repeat", 0, "", 0);
            //widgetTaskList.add(newTask2);
            //widgetTaskList.add(newTask);
            //widgetTaskList.remove(newTask);
            //widgetTaskList = (ArrayList<Task>) filterCurrentDate(db.getAllTasksFromUser(userId));

            loadTasks(new GetTaskData() {

                @Override
                public void onDataLoaded(List<Task> loadedTaskList) {
                    Log.d("Firebase Service","onDataSetChanged");
                    Log.d("Firebase Service", String.valueOf(loadedTaskList.size()));
                    Log.d("Firebase Service ", String.valueOf(widgetTaskList.size()));
                }
                @Override
                public void onError(String errorMsg) {
                    Log.d("loadData",errorMsg);

                }
            });
            Log.d("Service","GetTaskData");
        }

        @Override
        public void onDestroy() {
            widgetTaskList.clear();
        }

        @Override
        public int getCount() {
            return widgetTaskList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Task t = widgetTaskList.get(position);
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.task_widget_item);

            remoteView.setTextViewText(R.id.widgetTaskName, t.getTaskName());

            // Extract time from String
            String taskDueDateTime = t.getTaskDueDateTime();
            // Create a SimpleDateFormat object with the desired time format
            SimpleDateFormat firebaseFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            try {
                Date toExtract = firebaseFormat.parse(taskDueDateTime);
                // Format the date to get the time as a string
                String taskDueTime = timeFormat.format(toExtract);
                remoteView.setTextViewText(R.id.widgetTaskTime, taskDueTime);
                Log.v("Service", taskDueTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        // as there is only one widget view, return 1
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return widgetTaskList.get(position).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private void loadTasks(GetTaskData getTaskData){

            widgetTaskList.clear();

            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", 0);
            String userName = sharedPreferences.getString("Username", null);
            fdb = FirebaseDatabase.getInstance();
            taskDBR = fdb.getReference("tasks/" + userName);

            taskDBR.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // filter to current date
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    SimpleDateFormat firebase = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
                    //get current date
                    Date currentDate = new Date();
                    String strDate = format.format(currentDate);
                    for (DataSnapshot sn: snapshot.getChildren()){
                        Task t = sn.getValue(Task.class);
                        Date comparedDate = null;
                        try {
                            comparedDate = firebase.parse(t.getTaskDueDateTime());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        String compareDate = format.format(comparedDate);
                        if (compareDate.equals(strDate)){
                            widgetTaskList.add(t);
                            Log.v("Firebase GetTask",t.getTaskName());
                        }
                    }

                    //widgetTaskList.remove(0);
                    Log.d("FIREBASE Service",String.valueOf(widgetTaskList.size()));
                    getTaskData.onDataLoaded(widgetTaskList);
                    widgetTaskList.clear();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // if cannot connect or firebase returns an error
                    //Toast.makeText(getApplicationContext(), "You're offline :( Cannot reach the database", Toast.LENGTH_SHORT).show();
                    //Log.d(TITLE, error.getMessage());
                }
            });
        }

    }
}
