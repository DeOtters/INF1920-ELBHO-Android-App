package nl.otters.elbho.views.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.component_textdisplay.view.*
import kotlinx.android.synthetic.main.fragment_request.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.utils.SharedPreferences
import nl.otters.elbho.utils.VehicleLocationProvider
import nl.otters.elbho.viewModels.RequestViewModel
import java.util.*

class RequestFragment : DetailFragment() {
    private lateinit var request: Request.Properties
    private lateinit var vehicleLocationProvider: VehicleLocationProvider
    private lateinit var requestRepository: RequestRepository
    private lateinit var requestViewModel: RequestViewModel
    private lateinit var sharedPreferences: SharedPreferences

    private val dateParser: DateParser = DateParser()
    private var requestingLocationUpdates = false
    private var requestIsToday = false
    private var adviserLeftToAppointment = false

    //TODO: after user pressed "vertrek", this should be saved somewhere
    //API does not support this right now

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        request = arguments?.getParcelable("KEY_REQUEST")!!
        return inflater.inflate(R.layout.fragment_request, container, false)
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestRepository = RequestRepository(activity!!.applicationContext)
        requestViewModel = RequestViewModel(requestRepository)
        sharedPreferences = SharedPreferences(this.context!!)

        val leftAppointmentId: String? = sharedPreferences.getValueString("leftAppointment")
        if (leftAppointmentId != null && leftAppointmentId == request.id) {
            adviserLeftToAppointment = true
        }

        requestIsToday = isAppointmentToday()

        setFieldLabels()
        setFieldIcons()
        setFieldValues(request)
        setButtonListeners(request)
        setPrimaryButtons(arguments?.getString("KEY_APP_TITLE")!!)
        vehicleLocationProvider =
            VehicleLocationProvider.getInstance(this.requireActivity(), this.requireContext())
    }

    override fun onResume() {
        setTitle()
        super.onResume()
        if (requestingLocationUpdates) vehicleLocationProvider.start()
    }

    private fun isAppointmentToday(): Boolean {
        val appointments: ArrayList<String>? = sharedPreferences.getArrayPrefs("todaysAppointments")
        var isToday = false

        if (appointments != null) {
            for (appointment in appointments) {
                if (request.id == appointment) {
                    isToday = true
                }
            }
        }

        return isToday
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        val leftAppointmentId: String? = sharedPreferences.getValueString("leftAppointment")

        if (leftAppointmentId != null && leftAppointmentId == request.id) {
            appTitle.text = getString(R.string.app_title_hasLeft)
        } else {
            appTitle.text = (arguments!!.getString("KEY_APP_TITLE"))
        }
    }

    private fun setFieldLabels() {
        textDisplay_address.label.text = resources.getText(R.string.field_company_address)
        textDisplay_appointmentDate.label.text = resources.getText(R.string.field_appointment_date)
        textDisplay_cocName.label.text = resources.getText(R.string.field_company_name)
        textDisplay_comment.label.text = resources.getText(R.string.field_appointment_notes)
        textDisplay_contactPersonEmail.label.text = resources.getText(R.string.field_contact_email)
        textDisplay_contactPersonFunction.label.text =
            resources.getText(R.string.field_contact_title)
        textDisplay_contactPersonName.label.text = resources.getText(R.string.field_contact_name)
        textDisplay_contactPersonPhoneNumber.label.text =
            resources.getText(R.string.field_contact_phone)
    }

    private fun setFieldIcons() {
        textDisplay_contactPersonEmail.icon.setImageResource(R.drawable.ic_email_orange_24dp)
        textDisplay_contactPersonPhoneNumber.icon.setImageResource(R.drawable.ic_phone_orange_24dp)
        textDisplay_address.icon.setImageResource(R.drawable.ic_directions_orange_24dp)
    }

    @ExperimentalStdlibApi
    private fun setFieldValues(request: Request.Properties) {
        textDisplay_address.value.text = request.address
        textDisplay_appointmentDate.value.text =
            dateParser.toFormattedMonthAndDay(request.startTime).capitalize(Locale("nl")).plus(", ")
                .plus(
                    dateParser.toFormattedTimeString(request.startTime).plus(" - ")
                        .plus(dateParser.toFormattedTimeString(request.endTime))
                )
        textDisplay_cocName.value.text = request.cocName
        textDisplay_comment.value.text = request.comment
        textDisplay_contactPersonEmail.value.text = request.contactPersonEmail
        textDisplay_contactPersonFunction.value.text = request.contactPersonFunction
        textDisplay_contactPersonName.value.text = request.contactPersonName
        textDisplay_contactPersonPhoneNumber.value.text = request.contactPersonPhoneNumber
    }

    private fun setButtonListeners(request: Request.Properties) {
        textDisplay_contactPersonEmail.icon.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, request.contactPersonEmail)
            intent.putExtra(Intent.EXTRA_SUBJECT, "We can put a email subject here")
            intent.putExtra(Intent.EXTRA_TEXT, "We can put email body here.")

            startActivity(Intent.createChooser(intent, "Send Email"))
        }

        textDisplay_contactPersonPhoneNumber.icon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + request.contactPersonPhoneNumber))
            startActivity(intent)
        }

        // https://developers.google.com/maps/documentation/urls/android-intents
        textDisplay_address.icon.setOnClickListener {
            val locString: String = ("geo:0,0?q=" + request.address)
            val gmmIntentUri = Uri.parse(locString)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun setPrimaryButtons(parentFragmentTitle: String) {
        // When showing only 1 button, use topButton because of constrains :)
        // when() is just a switch with superpowers
        when (parentFragmentTitle) {
            resources.getString(R.string.navigation_open_requests) -> {
                topButton.setIconResource(R.drawable.ic_close_24dp)
                topButton.setText(R.string.button_deny_appointment)
                topButton.setBackgroundColor(
                    ContextCompat.getColor(
                        this.context!!,
                        R.color.red_button
                    )
                )
                topButton.setOnClickListener { denyRequest() }

                bottomButton.setIconResource(R.drawable.ic_done_24dp)
                bottomButton.setText(R.string.button_accept_appointment)
                bottomButton.setOnClickListener { acceptRequest() }
            }

            resources.getString(R.string.navigation_upcoming_requests) -> {
                bottomButton.visibility = View.GONE
                topButton.visibility = View.GONE

                if (requestIsToday) {
                    topButton.visibility = View.VISIBLE

                    if (adviserLeftToAppointment) {
                        topButton.setIconResource(R.drawable.ic_done_24dp)
                        topButton.setText(R.string.button_arrived)
                        topButton.setOnClickListener { arrivedAtDestination() }
                    } else {
                        topButton.setIconResource(R.drawable.ic_directions_car_white_24dp)
                        topButton.setText(R.string.button_leave)
                        topButton.setOnClickListener { goToDestination() }
                    }
                }
            }

            resources.getString(R.string.navigation_done_requests) -> {
                topButton.visibility = View.GONE
                bottomButton.visibility = View.GONE
            }
        }
    }

    private fun denyRequest() {
        requestViewModel.respondToRequest(request.id, Request.Respond(false))
        Snackbar.make(
            view!!,
            getString(R.string.snackbar_request_denied, request.cocName),
            Snackbar.LENGTH_SHORT
        ).show()
        findNavController().navigateUp()
    }

    private fun acceptRequest() {
        requestViewModel.respondToRequest(request.id, Request.Respond(true))
        Snackbar.make(
            view!!,
            getString(R.string.snackbar_request_accepted, request.cocName),
            Snackbar.LENGTH_SHORT
        ).show()
        findNavController().navigateUp()
    }

    private fun arrivedAtDestination() {
        sharedPreferences.clear("leftAppointment")
        topButton.setText(R.string.button_leave)
        topButton.setIconResource(R.drawable.ic_directions_car_white_24dp)
        requestingLocationUpdates = false
        vehicleLocationProvider.stop()
        setTitle()
    }

    private fun goToDestination() {
        sharedPreferences.save("leftAppointment", request.id)
        topButton.setText(R.string.button_arrived)
        topButton.setIconResource(R.drawable.ic_done_24dp)
        requestingLocationUpdates = true
        vehicleLocationProvider.start()
        Snackbar.make(
            view!!,
            getString(R.string.snackbar_departed),
            Snackbar.LENGTH_SHORT
        ).show()
        setTitle()
    }

    private fun toggleTracking() {
        // TODO: Ask for confirmation to start or stop
        if (requestingLocationUpdates) {
            topButton.setText(R.string.button_leave)
            topButton.setIconResource(R.drawable.ic_directions_car_white_24dp)
            requestingLocationUpdates = false
            vehicleLocationProvider.stop()
        } else {
            sharedPreferences.save("leftAppointment", request.id)
            topButton.setText(R.string.button_arrived)
            topButton.setIconResource(R.drawable.ic_done_24dp)
            setTitle()
            requestingLocationUpdates = true
            vehicleLocationProvider.start()
            Snackbar.make(
                view!!,
                getString(R.string.snackbar_departed),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}