package nl.otters.elbho.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

object VehicleLocationProvider {
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private const val LOCATION_REQUEST = 1
    @Volatile
    private var instance: VehicleLocationProvider? = null
    private var isRunning = false

    fun getInstance(activity: Activity, context: Context): VehicleLocationProvider {
        return if (instance == null) {
            this.activity = activity
            this.context = context
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            createLocationCallback()
            createLocationRequest()
            instance = this
            this
        } else {
            this
        }
    }

    fun start() {
        if (!isRunning) {
            checkPermissionsAndStart()
            isRunning = true
        }
    }

    fun stop() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        isRunning = false
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 1 * 60 * 1000
            fastestInterval = 1 * 60 * 1000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }!!
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // TODO: Send data to API
                    Toast.makeText(
                        context,
                        "lat: " + location.latitude + " lon: " + location.longitude,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun checkPermissionsAndStart() {
        val permissionAccessCoarseLocationApproved = ActivityCompat
            .checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
                PackageManager.PERMISSION_GRANTED

        if (permissionAccessCoarseLocationApproved) {
            val backgroundLocationPermissionApproved = ActivityCompat
                .checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) ==
                    PackageManager.PERMISSION_GRANTED

            if (backgroundLocationPermissionApproved) {
                startLocationUpdates()
            } else {
                // TODO: App can only access location in the foreground. Display a dialog
                // warning the user that your app must have all-the-time access to location
                // in order to function properly. Then, request background location.
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    LOCATION_REQUEST
                )
            }
        } else {
            // TODO: App doesn't have access to the device's location at all. Make full request
            // for permission.
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                LOCATION_REQUEST
            )
        }
    }
}