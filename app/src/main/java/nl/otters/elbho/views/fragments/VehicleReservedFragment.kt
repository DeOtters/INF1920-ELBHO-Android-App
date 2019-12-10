package nl.otters.elbho.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.component_textdisplay.view.*
import kotlinx.android.synthetic.main.fragment_request.bottomButton
import kotlinx.android.synthetic.main.fragment_vehicle_reserved.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.viewModels.VehicleViewModel

class VehicleReservedFragment : DetailFragment() {
    private lateinit var claim: Vehicle.Claim
    private val dateParser: DateParser = DateParser()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        claim = arguments?.getParcelable("KEY_CLAIM")!!
        return inflater.inflate(R.layout.fragment_vehicle_reserved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vehicleRepository = VehicleRepository(activity!!.applicationContext)
        val vehicleViewModel = VehicleViewModel(vehicleRepository)

        setFieldLabels()
        setFieldIcons()
        setFieldValues(claim)

        bottomButton.setOnClickListener {
            showAlert(vehicleViewModel)
        }
    }

    private fun showAlert(vehicleViewModel: VehicleViewModel) {
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.vehicle_delete_message))
            .setPositiveButton(getString(R.string.vehicle_delete_message_true)) { _, _ ->

                vehicleViewModel.deleteClaim(claim.reservation.id)
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

    private fun setFieldValues(claim: Vehicle.Claim) {
        textDisplay_carInfo.text = formatCarTitle(
            claim.car.brand,
            claim.car.model,
            claim.car.transmission
        )
        textDisplay_carLicensePlate.value.text = claim.car.licensePlate
        textDisplay_carReservationDate.value.text =
            dateParser.toFormattedDate(claim.reservation.startDateTime)
        textDisplay_carReservationTime.value.text = formatTime(
            dateParser.toFormattedTime(claim.reservation.startDateTime),
            dateParser.toFormattedTime(claim.reservation.endDateTime)
        )
        textDisplay_carLocation.value.text = claim.car.location
    }

    private fun formatCarTitle(brand: String, model: String, transmissionBool: Boolean): String {
        val transmission: String
        if (transmissionBool) {
            transmission = resources.getString(R.string.vehicle_transmission_true)
        } else {
            transmission = resources.getString(R.string.vehicle_transmission_false)
        }

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
}