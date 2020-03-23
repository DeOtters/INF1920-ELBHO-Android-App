package nl.otters.elbho.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import nl.otters.elbho.R

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

    fun start(fragment: Fragment) {
        if (!isRunning) {
            checkPermissionsAndStart()
            isRunning = true

            Snackbar.make(
                fragment.view!!,
                context.getString(R.string.location_snackbar_departed),
                Snackbar.LENGTH_SHORT
            ).show()
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
                super.onLocationResult(locationResult)
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (location != null) {
                        // TODO: Send data to API
                        Log.d(
                            "location",
                            "lat: " + location.latitude + " lon: " + location.longitude
                        )
                        Toast.makeText(
                            context,
                            "lat: " + location.latitude + " lon: " + location.longitude,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
                showLocationDialog(false)
            }
        } else {
            showLocationDialog(true)
        }
    }

    private fun showLocationDialog(hasZeroPermissions: Boolean) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.location_request_location_title))
            .setMessage(context.getString(R.string.location_request_location_message))
            .setPositiveButton(context.getString(R.string.location_request_continue)) { _, _ ->
                if (hasZeroPermissions) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ),
                        LOCATION_REQUEST
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        LOCATION_REQUEST
                    )
                }
            }.setNegativeButton(context.getString(R.string.location_request_cancel), null)
            .show()
    }
}