package sg.edu.np.mad.productivibe_;

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

        private DatabaseReference taskDBR;
        private FirebaseDatabase fdb;
        private ArrayList<Task> widgetTaskList = new ArrayList<>();

        public WidgetRemoteViewsFactory(Context context) {
            this.context = context;
            // getting db
            this.fdb = FirebaseDatabase.getInstance();

        }

        @Override
        public void onCreate() {

            // getting stored userid
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", 0);
            String userName = sharedPreferences.getString("Username", null);
            // create list of today task based on the user
            fdb = FirebaseDatabase.getInstance();
            taskDBR = fdb.getReference("tasks/" + userName);
            // get list of tasks
            taskDBR.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // filter to current date
                    SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
                    //get current date
                    Date currentDate = new Date();
                    String strDate = format.format(currentDate);
                    for (DataSnapshot sn: snapshot.getChildren()){
                        Task t = sn.getValue(Task.class);
                        if (t.getTaskDueDateTime().equals(strDate)){
                            widgetTaskList.add(t);
                        }
                    }
                    Log.d("FIREBASE",String.valueOf(widgetTaskList.size()));
                    // collects all the tasks saved in the firebase
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // if cannot connect or firebase returns an error
                    //Toast.makeText(getApplicationContext(), "You're offline :( Cannot reach the database", Toast.LENGTH_SHORT).show();
                }
            });
            //widgetTaskList = (ArrayList<Task>) filterCurrentDate(db.getAllTasksFromUser(userId));
            Log.v("Service", "widgetTaskList");
        }

        @Override
        public void onDataSetChanged() {

            // if dataset changed, list is cleared to store the new list of tasks
            widgetTaskList.clear();

            // getting stored userid
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", 0);
            String userName = sharedPreferences.getString("Username", null);
            // create list of today task based on the user
            fdb = FirebaseDatabase.getInstance();
            taskDBR = fdb.getReference("tasks/" + userName);
            // get list of tasks
            taskDBR.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // filter to current date
                    SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
                    //get current date
                    Date currentDate = new Date();
                    String strDate = format.format(currentDate);
                    for (DataSnapshot sn: snapshot.getChildren()){
                        Task t = sn.getValue(Task.class);
                        if (t.getTaskDueDateTime().equals(strDate)){
                            widgetTaskList.add(t);
                        }
                    }
                    Log.d("FIREBASE",String.valueOf(widgetTaskList.size()));
                    // collects all the tasks saved in the firebase
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // if cannot connect or firebase returns an error
                    //Toast.makeText(getApplicationContext(), "You're offline :( Cannot reach the database", Toast.LENGTH_SHORT).show();
                }
            });

            // getting stored userid
            //SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", 0);
            //String userId = sharedPreferences.getString("UserId", null);

            // create list of today task based on the user
            //widgetTaskList = (ArrayList<Task>) filterCurrentDate(db.getAllTasksFromUser(userId));
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

            // Convert from date to string datatype
            String taskDueDateTime = t.getTaskDueDateTime();
            // Create a SimpleDateFormat object with the desired time format
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            // Format the date to get the time as a string
            String taskDueTime = timeFormat.format(taskDueDateTime);
            remoteView.setTextViewText(R.id.widgetTaskTime, taskDueTime);
            Log.v("Service", taskDueTime);

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

        public static List<Task> filterCurrentDate(List<Task> filteredTaskList){
            // filter out tasks based on due data
            List<Task> temp = new ArrayList<>();
            // convert date object to a string with a nicer format
            SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
            //get current date
            Date currentDate = new Date();
            String strDate = format.format(currentDate);
            for (Task t : filteredTaskList){
                // check if it contains the date
                String comparedDate = format.format(t.getTaskDueDateTime());
                if (comparedDate.equals(strDate)){
                    temp.add(t);
                }
            }
            return temp;
        }

    }
}

