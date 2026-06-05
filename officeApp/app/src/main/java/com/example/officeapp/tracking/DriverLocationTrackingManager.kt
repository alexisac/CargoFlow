package com.example.officeapp.tracking

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DriverLocationTrackingManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun startTracking(
        intervalMillis: Long = DEFAULT_INTERVAL_MILLIS
    ): Boolean {
        if (!hasLocationPermission()) {
            return false
        }

        if (!hasNotificationPermission()) {
            return false
        }

        val intent = Intent(
            context,
            DriverLocationForegroundService::class.java
        ).apply {
            putExtra(
                DriverLocationForegroundService.EXTRA_INTERVAL_MILLIS,
                intervalMillis
            )
        }

        ContextCompat.startForegroundService(context, intent)

        return true
    }

    fun stopTracking() {
        val intent = Intent(
            context,
            DriverLocationForegroundService::class.java
        )

        context.stopService(intent)
    }

    private fun hasLocationPermission(): Boolean {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return hasFineLocation || hasCoarseLocation
    }

    private fun hasNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val FIVE_MINUTES = 5 * 60 * 1000L
        private const val DEFAULT_INTERVAL_MILLIS = FIVE_MINUTES
    }
}