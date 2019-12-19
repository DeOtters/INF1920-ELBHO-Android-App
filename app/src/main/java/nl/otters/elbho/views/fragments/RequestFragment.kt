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
import kotlinx.android.synthetic.main.component_textdisplay.view.*
import kotlinx.android.synthetic.main.fragment_request.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Request
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.utils.VehicleLocationProvider

class RequestFragment : DetailFragment() {
    private lateinit var request: Request.Properties
    private lateinit var vehicleLocationProvider: VehicleLocationProvider
    private val dateParser: DateParser = DateParser()
    private var requestingLocationUpdates = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        request = arguments?.getParcelable("KEY_REQUEST")!!
        return inflater.inflate(R.layout.fragment_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFieldLabels()
//        setFieldIcons()
//        setFieldValues(request)
//        setButtonListeners(request)
//        setPrimaryButtons(arguments?.getString("KEY_APP_TITLE")!!)
        vehicleLocationProvider =
            VehicleLocationProvider.getInstance(this.requireActivity(), this.requireContext())
    }

    override fun onResume() {
        setTitle()
        super.onResume()
        if (requestingLocationUpdates) vehicleLocationProvider.start()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.text = (arguments!!.getString("KEY_APP_TITLE"))
    }

    private fun setFieldLabels() {
        textDisplay_address.label.text = resources.getText(R.string.field_company_address)
        textDisplay_appointmentDate.label.text = resources.getText(R.string.field_appointment_date)
        textDisplay_appointmentTime.label.text = resources.getText(R.string.field_appointment_time)
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

//    private fun setFieldValues(request: Request.Properties) {
//        textDisplay_address.value.text = request.address
//        textDisplay_appointmentDate.value.text =
//            dateParser.toFormattedDate(request.appointmentDatetime)
//        textDisplay_appointmentTime.value.text =
//            dateParser.toFormattedTime(request.appointmentDatetime)
//        textDisplay_cocName.value.text = request.cocName
//        textDisplay_comment.value.text = request.comment
//        textDisplay_contactPersonEmail.value.text = request.website
//        textDisplay_contactPersonFunction.value.text = request.contactPersonFunction
//        textDisplay_contactPersonName.value.text = request.contactPersonName
//        textDisplay_contactPersonPhoneNumber.value.text = request.phoneNumber
//    }

    private fun setButtonListeners(request: Request.Properties) {
        textDisplay_contactPersonEmail.icon.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            // TODO: we should put a email address here, but the api doesn't support it at this time
            intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "We can put a email subject here")
            intent.putExtra(Intent.EXTRA_TEXT, "We can put email body here.")

            startActivity(Intent.createChooser(intent, "Send Email"))
        }

        textDisplay_contactPersonPhoneNumber.icon.setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + request.phoneNumber))
//            startActivity(intent)
        }

        // https://developers.google.com/maps/documentation/urls/android-intents
        textDisplay_address.icon.setOnClickListener {
            // TODO: So here we should insert the long lat values from the api, which it doesn't support at the time.
            val gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988")
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
                topButton.setIconResource(R.drawable.ic_directions_car_white_24dp)
                topButton.setText(R.string.button_leave)
                topButton.setOnClickListener { toggleTracking() }
            }

            resources.getString(R.string.navigation_done_requests) -> {
                topButton.visibility = View.GONE
                bottomButton.visibility = View.GONE
            }
        }
    }

    private fun denyRequest() {
        // TODO: Send to API
        findNavController().navigateUp()
    }

    private fun acceptRequest() {
        // TODO: Send to API
        findNavController().navigateUp()
    }

    private fun toggleTracking() {
        // TODO: Ask for confirmation to start or stop
        if (requestingLocationUpdates) {
            topButton.setText(R.string.button_leave)
            topButton.setIconResource(R.drawable.ic_directions_car_white_24dp)
            requestingLocationUpdates = false
            vehicleLocationProvider.stop()
        } else {
            topButton.setText(R.string.button_arrived)
            topButton.setIconResource(R.drawable.ic_done_24dp)
            requestingLocationUpdates = true
            vehicleLocationProvider.start()
        }
    }
}