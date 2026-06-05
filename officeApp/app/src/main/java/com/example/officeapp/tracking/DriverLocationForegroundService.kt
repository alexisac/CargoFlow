package com.example.officeapp.tracking

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.officeapp.R
import com.example.officeapp.services.LocationService
import com.example.officeapp.utils.ApiResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class DriverLocationForegroundService : Service() {

    @Inject
    lateinit var locationService: LocationService

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var trackingJob: kotlinx.coroutines.Job? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        val intervalMillis = intent?.getLongExtra(
            EXTRA_INTERVAL_MILLIS,
            DEFAULT_INTERVAL_MILLIS
        ) ?: DEFAULT_INTERVAL_MILLIS

        if (trackingJob?.isActive != true) {
            trackingJob = serviceScope.launch {
                startLocationLoop(intervalMillis)
            }
        }

        return START_STICKY
    }

    private suspend fun startLocationLoop(intervalMillis: Long) {
        while (serviceScope.isActive) {
            sendCurrentLocation()
            delay(intervalMillis)
        }
    }

    private suspend fun sendCurrentLocation() {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocation && !hasCoarseLocation) {
            stopSelf()
            return
        }

        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            val location = fusedLocationClient.lastLocation.await() ?: return

            when (
                locationService.updateMyLocation(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            ) {
                is ApiResult.Success -> Unit
                is ApiResult.Error -> Unit
                ApiResult.Loading -> Unit
            }
        } catch (_: SecurityException) {
            stopSelf()
        } catch (_: Exception) { }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("CargoFlow location tracking")
            .setContentText("Your location is being updated while you are logged in.")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        trackingJob?.cancel()
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val EXTRA_INTERVAL_MILLIS = "EXTRA_INTERVAL_MILLIS"

        private const val CHANNEL_ID = "driver_location_tracking_channel"
        private const val CHANNEL_NAME = "Driver location tracking"
        private const val NOTIFICATION_ID = 1001

        private const val DEFAULT_INTERVAL_MILLIS = 5 * 60 * 1000L
    }
}