package nl.otters.elbho.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.component_textdisplay.view.*
import kotlinx.android.synthetic.main.fragment_request_refactor.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Request
import nl.otters.elbho.utils.DateParser

class RequestFragment : Fragment() {
    private var request: Request.Properties? = null
    private val dateParser: DateParser = DateParser()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        request = arguments!!.getParcelable("KEY_REQUEST")
        return inflater.inflate(R.layout.fragment_request_refactor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFieldLabels()
        setFieldIcons()
        setFieldValues(request!!)
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.temp_request)
    }

    private fun setFieldLabels(){
        textDisplay_address.label.text = resources.getText(R.string.field_company_address)
        textDisplay_appointmentDate.label.text = resources.getText(R.string.field_appointment_date)
        textDisplay_appointmentTime.label.text = resources.getText(R.string.field_appointment_time)
        textDisplay_cocName.label.text = resources.getText(R.string.field_company_name)
        textDisplay_comment.label.text = resources.getText(R.string.field_appointment_notes)
        textDisplay_contactPersonEmail.label.text = resources.getText(R.string.field_contact_email)
        textDisplay_contactPersonFunction.label.text = resources.getText(R.string.field_contact_title)
        textDisplay_contactPersonName.label.text = resources.getText(R.string.field_contact_name)
        textDisplay_contactPersonPhoneNumber.label.text = resources.getText(R.string.field_contact_phone)
    }

    private fun setFieldIcons(){
        textDisplay_contactPersonEmail.icon.setImageResource(R.drawable.ic_email_orange_24dp)
        textDisplay_contactPersonPhoneNumber.icon.setImageResource(R.drawable.ic_phone_orange_24dp)
        textDisplay_address.icon.setImageResource(R.drawable.ic_directions_orange_24dp)
    }
    
    private fun setFieldValues(request: Request.Properties){
        textDisplay_address.value.text = request.address
        textDisplay_appointmentDate.value.text = dateParser.toFormattedDate(request.appointmentDatetime)
        textDisplay_appointmentTime.value.text = dateParser.toFormattedTime(request.appointmentDatetime)
        textDisplay_cocName.value.text = request.cocName
        textDisplay_comment.value.text = request.comment
        textDisplay_contactPersonEmail.value.text = request.phoneNumber
        textDisplay_contactPersonFunction.value.text = request.contactPersonFunction
        textDisplay_contactPersonName.value.text = request.contactPersonName
        textDisplay_contactPersonPhoneNumber.value.text = request.phoneNumber
    }

    //TODO: private fun set buttonListeners to start mail, phone and maps intent
}