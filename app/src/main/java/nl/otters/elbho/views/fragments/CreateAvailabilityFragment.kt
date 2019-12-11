package nl.otters.elbho.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.component_availability_input.view.*
import kotlinx.android.synthetic.main.fragment_create_availability.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Availability
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: use ThreeTen library
// We are using, Date, CalendarDay, Calendar and Strings all for days. This code is getting really messy.
// LocalDate is not an options since we need to support API 21 >
class CreateAvailabilityFragment : DetailFragment() {
    private lateinit var chosenDay: CalendarDay
    private lateinit var availability: ArrayList<Availability.Slot>
    private lateinit var inputFieldList: ArrayList<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_availability, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        chosenDay = arguments?.getParcelable("KEY_CHOSEN_DATE")!!
        availability = arguments?.getParcelableArrayList("KEY_AVAILABILITY")!!

        setWeekSelector(chosenDay)
        setDayLabels()
        setOnClickListeners()
    }

    private fun setWeekSelector(date: CalendarDay){
        val calendar: Calendar =  Calendar.getInstance()
        calendar.set(date.year, date.month, date.day)
        availability_week_selector.text = resources.getString(R.string.create_availability_week_selector, calendar.get(Calendar.WEEK_OF_YEAR))
    }

    private fun getDaysOfWeek(date: CalendarDay): ArrayList<String>{
        val calendar: Calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd/MM", Locale("nl"))

        calendar.time = date.date
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        val daysOfWeek: ArrayList<String> = ArrayList()

        for(i in 0..4){
            daysOfWeek.add(format.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return daysOfWeek
    }

    private fun setOnClickListeners() {
        inputFieldList = arrayListOf(
            availability_monday,
            availability_tuesday,
            availability_wednesday,
            availability_thursday,
            availability_friday
        )

        availability_week_selector.setOnClickListener { selectWeek() }
        availability_copy_week.setOnClickListener { copyWeek() }
        availability_create.setOnClickListener { createAvailability(view!!) }

        for (item in inputFieldList) {
//            item.availability_time_from.setOnClickListener {
//                // TODO: Open time picker and set time to text field
//            }
//            item.availability_time_to.setOnClickListener {
//                // TODO: Open time picker and set time to text field
//            }
//            item.availability_clear.setOnClickListener {
//                // TODO: Ask for confirmation
//                item.availability_time_from.setText("")
//                item.availability_time_to.setText("")
//            }
        }
    }

    private fun setDayLabels() {
        // TODO: Improve this
        val daysOfWeek: ArrayList<String> = getDaysOfWeek(chosenDay)

        availability_monday.availability_dayText.text = "MA"
        availability_monday.availability_dateText.text = daysOfWeek[0]

        availability_tuesday.availability_dayText.text = "DI"
        availability_tuesday.availability_dateText.text = daysOfWeek[1]

        availability_wednesday.availability_dayText.text = "WO"
        availability_wednesday.availability_dateText.text = daysOfWeek[2]

        availability_thursday.availability_dayText.text = "DO"
        availability_thursday.availability_dateText.text = daysOfWeek[3]

        availability_friday.availability_dayText.text = "VR"
        availability_friday.availability_dateText.text = daysOfWeek[4]
    }

    private fun selectWeek() {
        // TODO: Select week
    }

    private fun copyWeek() {
        findNavController().navigate(R.id.action_createAvailabilityFragment_to_copyWeekFragment)
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

