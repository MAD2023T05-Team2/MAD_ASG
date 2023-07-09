package sg.edu.np.mad.productivibe;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

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
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        int no = 30;
        String taskNo = no + " Tasks Due Today:";
        RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.task_widget);
        widgetViews.setTextViewText(R.id.todayTask, taskNo);
        Log.v("Widget", taskNo);

        // Initialize the database
        db = Database.getInstance(context.getApplicationContext());
        
        // Get UserId from shared preferences and put today's tasks into a list
        //SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", 0);
        //String userId = sharedPreferences.getString("UserId", null);
        //List<Task> widgetTaskList = filterCurrentDate(db.getAllTasksFromUser(userId));

        //RemoteViews taskView = new RemoteViews(context.getPackageName(), R.layout.widget_today_task_item);

        //taskView.setTextViewText(R.id., getTaskName());
        //taskView.setTextViewText(R.id., getTaskDueDateTime());


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
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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