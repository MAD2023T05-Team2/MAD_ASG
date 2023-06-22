package sg.edu.np.mad.producti_vibe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // To navigate back to MainActivity when notification is clicked
        Intent repeatingIntent = new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, repeatingIntent, PendingIntent.FLAG_IMMUTABLE);

        // Building the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Task Reminder Notification")
                .setSmallIcon(R.drawable.baseline_timer_24)
                .setContentTitle("Task Reminder!")
                .setStyle(new NotificationCompat.BigTextStyle() // Will try to add in task name & time remaining/deadline
                        .bigText("Make sure to complete your task before the deadline hits! Good Luck :)"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(200, builder.build());
    }
}
