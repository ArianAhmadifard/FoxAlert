package com.arian.foxalert.presentation.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arian.foxalert.data.dao.EventDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint // Annotate with @AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    // Inject the EventDao
    @Inject
    lateinit var eventDao: EventDao

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Use the injected eventDao
            coroutineScope.launch {
                val currentDate = LocalDate.now()
                eventDao.getAllEvents()
                    .collect { events ->
                        events.filter { it.date.isAfter(currentDate) || it.date.isEqual(currentDate) }
                            .forEach { event ->
                                // Re-schedule the alarm for each future event
                                AlarmScheduler.scheduleAlarm(context, event)
                            }
                    }

                // Optional: Disable the BootReceiver if no future events were scheduled.
                // This requires a way to know if scheduleAlarm was actually called for any event.
                // For simplicity, you might keep it enabled if you expect users to add events.
                // If you implement the check, you'd use eventDao.countFutureEvents(currentDate)
                // val hasFutureEvents = eventDao.countFutureEvents(currentDate) > 0
                // if (!hasFutureEvents) {
                //     val receiver = ComponentName(context, BootReceiver::class.java)
                //     context.packageManager.setComponentEnabledSetting(
                //         receiver,
                //         PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                //         PackageManager.DONT_KILL_APP
                //     )
                // }
            }
        }
    }
}