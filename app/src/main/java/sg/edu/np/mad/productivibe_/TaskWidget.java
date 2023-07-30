package sg.edu.np.mad.productivibe_;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

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

// This class is called when user adds the widget to their home screen
/**
 * Implementation of App Widget functionality.
 */
public class TaskWidget extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Get UserId from shared preferences and put today's tasks into a list
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", 0);
        //String userName = sharedPreferences.getString("Username", null);
        String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create list of today task based on the user
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference taskDBR = fdb.getReference("tasks/" + userName);

        // Collects all the tasks saved in the firebase for that particular user
        ArrayList<Task> widgetTaskList = new ArrayList<>();
        taskDBR.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Filter to current date
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat firebase = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
                // Get current date
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
                    }
                }

                // Construct the RemoteViews object
                int no = widgetTaskList.size();
                String taskNo = no + " Tasks Due Today:";
                RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.task_widget);
                widgetViews.setTextViewText(R.id.todayTask, taskNo);
                Log.v("Widget", taskNo);

                // To display tasks using list view
                Intent serviceIntent = new Intent(context, TaskWidgetService.class); // Create intent to start TaskWidgetService class, which extends RemoteViewsService, providing data to populate the ListView in the widget
                serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId); // Let service know which widget instance is requesting the data
                serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))); // Sets the data to a URI representation of the intent to differentiate different instances of the same TaskWidgetService
                widgetViews.setRemoteAdapter(R.id.widgetTaskView, serviceIntent); // Sets RemoteViewsService as the adapter for the ListView, meaning that it will be responsible for populating data into the ListView
                widgetViews.setEmptyView(R.id.widgetTaskView, R.id.noTasks); // When Listview is empty, the empty view, R.id.noTasks will be displayed

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, widgetViews);

                // Create an Intent to launch HomePage when widget is clicked
                Intent intent = new Intent(context, HomePage.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                widgetViews.setOnClickPendingIntent(R.id.todayTask, pendingIntent);
                widgetViews.setOnClickPendingIntent(R.id.widgetTaskView, pendingIntent);
                widgetViews.setOnClickPendingIntent(R.id.widgetTaskTime, pendingIntent);
                widgetViews.setOnClickPendingIntent(R.id.widgetTaskName, pendingIntent);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if cannot connect or firebase returns an error
                //Toast.makeText(getApplicationContext(), "You're offline :( Cannot reach the database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Update the views and content of the widget
            updateAppWidget(context, appWidgetManager, appWidgetId);

            // Notify appWidgetManager that the data has changed for the task list view in the widget
            // Triggers the onDataSetChanged() method in the RemoteViewsService
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetTaskView);
        }

        // Ensures that the superclass implementation of the onUpdate() is also executed
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, TaskWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetTaskView);
        }
        // Call onUpdate() to ensure that the widget view is properly set up
        // This is to handle cases where the widget is created but not updated automatically
        // onUpdate() will update the views and trigger RemoteViewsService to load the list of tasks in the widget's ListView
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
