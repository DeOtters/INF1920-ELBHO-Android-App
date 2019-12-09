package nl.otters.elbho.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.component_availability_input.view.*
import kotlinx.android.synthetic.main.fragment_create_availability.*
import nl.otters.elbho.R

class CreateAvailabilityFragment : DetailFragment() {

    private val inputFieldList = arrayListOf<View>(
        availability_monday,
        availability_tuesday,
        availability_wednesday,
        availability_thursday,
        availability_friday
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_availability, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setDayLabels()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        availability_week_selector.setOnClickListener { selectWeek() }
        availability_copy_week.setOnClickListener { copyWeek() }
        availability_create.setOnClickListener { createAvailability(view!!) }

        for (item in inputFieldList) {
            item.availability_time_from.setOnClickListener {
                // TODO: Open time picker and set time to text field
            }
            item.availability_time_to.setOnClickListener {
                // TODO: Open time picker and set time to text field
            }
        }
    }

    private fun setDayLabels() {
        // TODO: Improve this
        availability_monday.availability_listItem_dayTextView.text = "MA"
        availability_monday.availability_listItem_dateTextView.text = "9/12"

        availability_tuesday.availability_listItem_dayTextView.text = "DI"
        availability_tuesday.availability_listItem_dateTextView.text = "10/12"

        availability_wednesday.availability_listItem_dayTextView.text = "WO"
        availability_wednesday.availability_listItem_dateTextView.text = "11/12"

        availability_thursday.availability_listItem_dayTextView.text = "DO"
        availability_thursday.availability_listItem_dateTextView.text = "12/12"

        availability_friday.availability_listItem_dayTextView.text = "VR"
        availability_friday.availability_listItem_dateTextView.text = "13/12"
    }

    private fun selectWeek() {
        // TODO: Select week
    }

    private fun copyWeek() {
        // TODO: Copy week
    }

    private fun createAvailability(view: View) {
        // TODO: Get data from date fields and send to API

        Snackbar.make(
            view,
            getString(R.string.create_availability_saved),
            Snackbar.LENGTH_SHORT
        ).show()
        findNavController().navigateUp()
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.create_new_availability_title)
    }
}