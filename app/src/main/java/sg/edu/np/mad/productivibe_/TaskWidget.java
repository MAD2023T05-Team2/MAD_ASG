package sg.edu.np.mad.productivibe_;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

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

/**
 * Implementation of App Widget functionality.
 */
public class TaskWidget extends AppWidgetProvider {
    private static Database db;
    private DatabaseReference taskDBR;
    private FirebaseDatabase fdb;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String myPrefs) {

        // Initialize the database
        //TaskWidget.db = Database.getInstance(context.getApplicationContext());

        // Get UserId from shared preferences and put today's tasks into a list
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", 0);
        String userName = sharedPreferences.getString("Username", null);
        // create list of today task based on the user
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference taskDBR = fdb.getReference("tasks/" + userName);
        // get list of tasks
        ArrayList<Task> widgetTaskList = new ArrayList<>();
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

        // Construct the RemoteViews object
        int no = widgetTaskList.size();
        String taskNo = no + " Tasks Due Today:";
        RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.task_widget);
        widgetViews.setTextViewText(R.id.todayTask, taskNo);
        Log.v("Widget", taskNo);

        // To display tasks using list view
        Intent serviceIntent = new Intent(context, TaskWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        widgetViews.setRemoteAdapter(R.id.widgetTaskView, serviceIntent);
        widgetViews.setEmptyView(R.id.widgetTaskView, R.id.noTasks);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widgetViews);

        // Create an Intent to launch HomePage when widget is clicked
        Intent intent = new Intent(context, HomePage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        widgetViews.setOnClickPendingIntent(R.id.todayTask, pendingIntent);
        widgetViews.setOnClickPendingIntent(R.id.widgetTaskView, pendingIntent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // update the views and content of the widget
            updateAppWidget(context, appWidgetManager, appWidgetId, "MyPrefs");

            // notify appWidgetManager that the data has changed for the task list view in the widget
            // triggers the onDataSetChanged() method in the RemoteViewsService
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetTaskView);
        }
        // ensures that the superclass implementation of the onUpdate() is also executed
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
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