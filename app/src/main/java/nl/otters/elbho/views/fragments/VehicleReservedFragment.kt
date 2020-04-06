package nl.otters.elbho.views.fragments

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.component_text_display.view.*
import kotlinx.android.synthetic.main.fragment_request.bottomButton
import kotlinx.android.synthetic.main.fragment_vehicle_reserved.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.viewModels.VehicleViewModel
import nl.otters.elbho.views.activities.NavigationActivity

class VehicleReservedFragment : DetailFragment(), OnMapReadyCallback {
    private lateinit var reservation: Vehicle.Reservation
    private lateinit var mapFragment: SupportMapFragment

    private val dateParser: DateParser = DateParser()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reservation = arguments?.getParcelable("KEY_Reservation")!!
        return inflater.inflate(R.layout.fragment_vehicle_reserved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vehicleRepository = VehicleRepository(activity!!.applicationContext)
        val vehicleViewModel = VehicleViewModel(vehicleRepository)

        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setFieldLabels()
        setFieldIcons()
        setFieldValues(reservation)
        setMapsListener(reservation)

        bottomButton.setOnClickListener {
            deleteReservation(vehicleViewModel)

            val snackbarDialog = Snackbar.make(
                it,
                getString(R.string.snackbar_vehicle_cancelled),
                Snackbar.LENGTH_LONG
            )
            val snackbarView = snackbarDialog.view
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    activity!!.applicationContext,
                    R.color.vehicle_snackBar_bg_col
                )
            )
            val snackbarTextView =
                snackbarView.findViewById<TextView>(R.id.snackbar_text)
            snackbarTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_check_circle_24dp,
                0,
                0,
                0
            )
            snackbarTextView.compoundDrawablePadding = 75
            snackbarDialog.show()
        }
    }

    // TODO: Use Synchronize instead of Thread.Sleep
    private fun deleteReservation(vehicleViewModel: VehicleViewModel) {

        vehicleViewModel.removeVehicleReservation(reservation.id)
        Thread.sleep(500)
        super.getFragmentManager()?.popBackStack()

    }

    private fun setFieldLabels() {
        textDisplay_carLicensePlate.label.text = resources.getText(R.string.car_licensePlate)
        textDisplay_carReservationDate.label.text = resources.getText(R.string.car_reservation_date)
        textDisplay_carReservationTime.label.text = resources.getText(R.string.car_reservation_time)
        textDisplay_carLocation.label.text = resources.getText(R.string.car_location)
    }

    private fun setFieldIcons() {
        textDisplay_carLocation.icon.setImageResource(R.drawable.ic_directions_orange_24dp)
    }

    private fun setFieldValues(reservation: Vehicle.Reservation) {
        Glide.with(this)
            .load(reservation.vehicle.image)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_notfound)
            .into(vehicleImage)

        textDisplay_carInfo.text = formatCarTitle(
            reservation.vehicle.brand,
            reservation.vehicle.model,
            reservation.vehicle.transmission
        )
        textDisplay_carLicensePlate.value.text = reservation.vehicle.licensePlate
        textDisplay_carReservationDate.value.text =
            dateParser.toFormattedDateWithYear(reservation.start)
        textDisplay_carReservationTime.value.text = formatTime(
            dateParser.toFormattedTime(reservation.start),
            dateParser.toFormattedTime(reservation.end)
        )
        textDisplay_carLocation.value.text = reservation.vehicle.location
    }

    private fun setMapsListener(reservation: Vehicle.Reservation) {
        textDisplay_carLocation.setOnClickListener {
            val locString: String = ("geo:0,0?q=" + reservation.vehicle.location)
            val gmmIntentUri = Uri.parse(locString)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun formatCarTitle(brand: String, model: String, transmission: String): String {
        return brand
            .plus(" ")
            .plus(model)
            .plus(" ")
            .plus(transmission)
    }

    private fun formatTime(startTime: String, endTime: String): String {
        return startTime
            .plus(" - ")
            .plus(endTime)
    }

    // TODO: Errorhandling
    override fun onMapReady(googleMap: GoogleMap) {

        val coder = Geocoder(context)
        val address: List<Address>

        address = coder.getFromLocationName(reservation.vehicle.location, 5)
        val location: Address = address[0]

        val finalLocation = LatLng(location.latitude, location.longitude)

        googleMap.addMarker(MarkerOptions().position(finalLocation))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(finalLocation, 16f))
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = false
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_vehicle_reservation)
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        (activity as NavigationActivity).setProgressBarVisible(false)
    }
}