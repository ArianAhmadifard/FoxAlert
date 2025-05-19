package com.arian.foxalert.presentation.worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.arian.foxalert.data.model.EventEntity
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

object AlarmScheduler {

    fun scheduleAlarm(context: Context, event: EventEntity) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Convert LocalDate to java.util.Date
        val eventDate = Date.from(event.date.atStartOfDay(ZoneId.systemDefault()).toInstant())

        // Calculate the time for the alarm (1 day before the event)
        val calendar = Calendar.getInstance().apply {
            time = eventDate
            add(Calendar.DAY_OF_YEAR, -1)
            // Set the time of day for the alarm (e.g., 9:00 AM the day before)
            set(Calendar.HOUR_OF_DAY, 9) // You can make this configurable
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val alarmTimeMillis = calendar.timeInMillis

        // Ensure the alarm time is in the future
        if (alarmTimeMillis <= System.currentTimeMillis()) {
            // Alarm time is in the past or today, do not schedule
            return
        }

        // Create an intent that will trigger your AlarmReceiver
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.your_app_package.EVENT_ALARM" // Use a unique action
            putExtra("eventTitle", event.title)
            putExtra("eventId", event.id)
            putExtra("eventDate", event.date.toString()) // Pass date as string
        }

        // Create a PendingIntent using the event ID as the request code
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Schedule the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        alarmTimeMillis,
                        pendingIntent
                    )
                } else {
                    // Permission not granted, handle this in your UI
                    // You should guide the user to grant this permission in settings
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmTimeMillis,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmTimeMillis,
                pendingIntent
            )
        }

        // Enable the BootReceiver when an alarm is scheduled
        enableBootReceiver(context)
    }

    fun cancelAlarm(context: Context, eventId: Int, eventTitle: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.your_app_package.EVENT_ALARM"
            putExtra("eventTitle", eventTitle)
            putExtra("eventId", eventId)
        }

        // Use the same request code (event ID) as used for scheduling
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)

        // Optional: Disable the BootReceiver if no other future alarms exist
        // This requires checking the database for any remaining future events
        // CoroutineScope(Dispatchers.IO).launch {
        //     val db = FoxDatabase.getDatabase(context)
        //     val eventDao = db.eventDao()
        //     val hasFutureEvents = eventDao.countFutureEvents(LocalDate.now()) > 0
        //     if (!hasFutureEvents) {
        //         disableBootReceiver(context)
        //     }
        // }
    }

    private fun enableBootReceiver(context: Context) {
        val receiver = ComponentName(context, BootReceiver::class.java)
        if (context.packageManager.getComponentEnabledSetting(receiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            context.packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

    private fun disableBootReceiver(context: Context) {
        val receiver = ComponentName(context, BootReceiver::class.java)
        if (context.packageManager.getComponentEnabledSetting(receiver) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            context.packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}