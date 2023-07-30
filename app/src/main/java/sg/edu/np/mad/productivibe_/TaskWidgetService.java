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

import com.google.firebase.auth.FirebaseAuth;
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

// This class helps to provide data for the widget displayed on the home screen of the device
// It is responsible for updating the content of a widget with a list of tasks due for the current date.

public class TaskWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.v("Service", "onGetViewFactory");
        return new WidgetRemoteViewsFactory(getApplicationContext());
    }

    // Factory class that provides data for the ListView in the widget
    class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private final Context context;
        private List<Task> widgetTaskList = new ArrayList<>();
        private DatabaseReference taskDBR;
        private FirebaseDatabase fdb;

        public WidgetRemoteViewsFactory(Context context) {
            this.context = context;
                   }

        @Override
        public void onCreate() {
            loadTasks(new GetTaskData() {

                @Override
                public void onDataLoaded(List<Task> loadedTaskList) {
                    widgetTaskList.clear();
                    Log.d("widget Service ", String.valueOf(widgetTaskList.size()));
                    Log.d("loaded Service", String.valueOf(loadedTaskList.size()));
                    widgetTaskList = loadedTaskList;
                    Log.d("widget Service ", String.valueOf(widgetTaskList.size()));
                    Log.d("loaded Service", String.valueOf(loadedTaskList.size()));
                }
                @Override
                public void onError(String errorMsg) {
                    Log.d("loadData",errorMsg);

                }
            });
            Log.d("Service","GetTaskData");
        }

        // Called when there is a change in the data set, such as the ListView being updated.
        @Override
        public void onDataSetChanged() {

            Log.v("Service", "widgetTaskList, onDataSetChanged");
            loadTasks(new GetTaskData() {

                @Override
                public void onDataLoaded(List<Task> loadedTaskList) {
                    widgetTaskList.clear();
                    Log.d("widget Service ", String.valueOf(widgetTaskList.size()));
                    Log.d("loaded Service", String.valueOf(loadedTaskList.size()));

                    widgetTaskList = loadedTaskList;
                    Log.d("widget Service ", String.valueOf(widgetTaskList.size()));
                    Log.d("loaded Service", String.valueOf(loadedTaskList.size()));
                }
                @Override
                public void onError(String errorMsg) {
                    Log.d("loadData",errorMsg);

                }
            });
            Log.d("Service","GetTaskData");
        }

        // Called when the factory is destroyed
        @Override
        public void onDestroy() {
            widgetTaskList.clear();
        }

        // Get the number of items in the ListView
        @Override
        public int getCount() {
            return widgetTaskList.size();
        }

        // Called to get the RemoteViews associated with a single list item in the ListView
        @Override
        public RemoteViews getViewAt(int position) {
            Task t = widgetTaskList.get(position);
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.task_widget_item);

            // Populate the RemoteViews with task data
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
        // Since there is only one widget view, return 1
        public int getViewTypeCount() {
            return 1;
        }

        // Return the ID of the item at the given position
        @Override
        public long getItemId(int position) {
            return widgetTaskList.get(position).getId();
        }

        // Indicate whether the item at the given position is unique and stable
        @Override
        public boolean hasStableIds() {
            return true;
        }

        // Load tasks from Firebase and notify the GetTaskData callback when data is loaded
        private void loadTasks(GetTaskData getTaskData){

            List<Task> loadTaskList = new ArrayList<>();

            // Get username from firebase and put today's tasks into a list
            String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                            loadTaskList.add(t);
                            Log.v("Firebase GetTask",t.getTaskName());
                        }
                    }

                    Log.d("Firebase Service",String.valueOf(loadTaskList.size()));
                    getTaskData.onDataLoaded(loadTaskList);
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
