package com.example.officeapp.screens.reusableComponents

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.officeapp.models.location.DriverTrackingPermissionResult

@Composable
fun LocationTrackingPermissionHandler(
    context: Context,
    activity: Activity?,
    requestPermissions: Boolean,
    onPermissionsResult: (DriverTrackingPermissionResult) -> Unit,
    onRequestConsumed: () -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        when {
            hasRequiredDriverTrackingPermissions(context) -> {
                onPermissionsResult(DriverTrackingPermissionResult.GRANTED)
            }

            activity != null && hasPermanentlyDeniedPermission(context, activity) -> {
                onPermissionsResult(DriverTrackingPermissionResult.PERMANENTLY_DENIED)
            }

            else -> {
                onPermissionsResult(DriverTrackingPermissionResult.DENIED)
            }
        }

        onRequestConsumed()
    }

    LaunchedEffect(requestPermissions) {
        if (!requestPermissions) {
            return@LaunchedEffect
        }

        val missingPermissions = getMissingDriverTrackingPermissions(context)

        if (missingPermissions.isEmpty()) {
            onPermissionsResult(DriverTrackingPermissionResult.GRANTED)
            onRequestConsumed()
            return@LaunchedEffect
        }

        if (activity != null && hasPermanentlyDeniedPermission(context, activity)) {
            onPermissionsResult(DriverTrackingPermissionResult.PERMANENTLY_DENIED)
            onRequestConsumed()
            return@LaunchedEffect
        }

        permissionLauncher.launch(missingPermissions.toTypedArray())
    }
}

fun hasRequiredDriverTrackingPermissions(context: Context): Boolean {
    val hasLocationPermission =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    val hasNotificationPermission =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

    return hasLocationPermission && hasNotificationPermission
}

fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

    context.startActivity(intent)
}

private fun getMissingDriverTrackingPermissions(context: Context): List<String> {
    val missingPermissions = mutableListOf<String>()

    val hasFineLocation = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val hasCoarseLocation = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (!hasFineLocation && !hasCoarseLocation) {
        missingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        missingPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    val hasNotificationPermission =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

    if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        missingPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    return missingPermissions
}

private fun hasPermanentlyDeniedPermission(
    context: Context,
    activity: Activity
): Boolean {
    return getMissingDriverTrackingPermissions(context).any { permission ->
        !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}