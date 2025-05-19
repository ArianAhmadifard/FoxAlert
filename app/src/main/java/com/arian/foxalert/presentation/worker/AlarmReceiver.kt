// In com/your_app_package/alarm/AlarmReceiver.kt
package com.arian.foxalert.presentation.worker

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arian.foxalert.R
import com.arian.foxalert.presentation.main.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val eventTitle = intent.getStringExtra("eventTitle") ?: "Calendar Event"
            val eventDateString = intent.getStringExtra("eventDate") ?: "Upcoming Event"
            val eventId = intent.getIntExtra("eventId", -1)

            showNotification(context, eventTitle, eventDateString, eventId)
        }
    }

    private fun showNotification(context: Context, title: String, dateString: String, eventId: Int) {
        val channelId = "event_alarm_channel"
        val notificationId = eventId // Use event ID as notification ID

        // Create an intent to open your app when the notification is tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            // Optional: Pass data to your activity to navigate to the specific event
            putExtra("navigateToEventId", eventId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            eventId, // Use event ID as request code for the activity intent
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your notification icon resource
            .setContentTitle("Reminder: $title")
            .setContentText("$dateString is tomorrow!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // You need to ensure the notification channel is created before showing the notification
            notify(notificationId, builder.build())
        }
    }
}