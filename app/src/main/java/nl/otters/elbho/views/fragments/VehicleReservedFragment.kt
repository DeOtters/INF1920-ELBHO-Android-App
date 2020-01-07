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
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.component_textdisplay.view.*
import kotlinx.android.synthetic.main.fragment_request.bottomButton
import kotlinx.android.synthetic.main.fragment_vehicle_reserved.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.viewModels.VehicleViewModel

class VehicleReservedFragment : DetailFragment(), OnMapReadyCallback{
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

        textDisplay_carLocation.setOnClickListener {
            val loc: Uri = Uri.parse("google.navigation:q=" + reservation.vehicle.location)
            val mapIntent = Intent(Intent.ACTION_VIEW, loc)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        bottomButton.setOnClickListener {
            showDeleteAlert(vehicleViewModel)
        }
    }

    // TODO: Use Synchronize instead of Thread.Sleep
    private fun showDeleteAlert(vehicleViewModel: VehicleViewModel) {
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.vehicle_delete_message))
            .setPositiveButton(getString(R.string.vehicle_delete_message_true)) { _, _ ->

                vehicleViewModel.removeVehicleReservation(reservation.id)
                Thread.sleep(500)
                super.getFragmentManager()?.popBackStack()

            }.setNegativeButton(getString(R.string.vehicle_delete_message_false), null)
            .show()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_vehicle)
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
            dateParser.toFormattedDate(reservation.start)
        textDisplay_carReservationTime.value.text = formatTime(
            dateParser.toFormattedTime(reservation.start),
            dateParser.toFormattedTime(reservation.end)
        )
        textDisplay_carLocation.value.text = reservation.vehicle.location
    }

    private fun formatCarTitle(brand: String, model: String, transmission: String): String {
        val trans: String
        if (transmission.equals("Automaat")) {
            trans = resources.getString(R.string.vehicle_transmission_true)
        } else {
            trans = resources.getString(R.string.vehicle_transmission_false)
        }

        return brand
            .plus(" ")
            .plus(model)
            .plus(" ")
            .plus(trans)
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
        val location: Address = address.get(0)

        val finalLocation = LatLng(location.latitude, location.longitude)

        googleMap.addMarker(MarkerOptions().position(finalLocation))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(finalLocation, 16f))
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = false
    }
}