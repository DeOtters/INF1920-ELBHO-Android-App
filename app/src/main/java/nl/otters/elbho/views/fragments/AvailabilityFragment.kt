package nl.otters.elbho.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.fragment_availability.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Availability
import nl.otters.elbho.repositories.AvailabilityRepository
import nl.otters.elbho.utils.AvailableDayDecorator
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.utils.DisableWeekendsDecorator
import nl.otters.elbho.viewModels.AvailabilityViewModel
import nl.otters.elbho.views.activities.NavigationActivity
import java.util.*
import kotlin.collections.ArrayList

class AvailabilityFragment : BaseFragment(), OnDateSelectedListener {
    private var availability: ArrayList<Availability.Slot> = ArrayList()
    private val dateParser: DateParser = DateParser()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_availability, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val availabilityRepository = AvailabilityRepository(activity!!.applicationContext)
        val availabilityViewModel = AvailabilityViewModel(availabilityRepository)

        setupCalendar()
        (activity as NavigationActivity).setProgressBarVisible(true)

        val timePeriod: Availability.TimePeriod =
            Availability.TimePeriod(null, dateParser.getTimestampLastDayOfMonthBefore())
        availabilityViewModel.getAllAvailabilities(timePeriod)?.observe(this, Observer {
            availability = it
            for (timeSlot in it){
                //Here we add the ui for a available day from database
                calendarView.addDecorator(AvailableDayDecorator(timeSlot.start))
                (activity as NavigationActivity).setProgressBarVisible(false)
            }
        })
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_availability)
    }

    private fun setupCalendar() {
        val disableWeekendsDecorator = DisableWeekendsDecorator()

        calendarView.addDecorator(disableWeekendsDecorator)
        calendarView.setOnDateChangedListener(this)
        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        if(date.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || date.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return
        }
        val bundle = Bundle()
        bundle.putParcelable("KEY_CHOSEN_DATE", date)
        bundle.putParcelableArrayList("KEY_AVAILABILITY", availability)
        findNavController().navigate(R.id.action_availabilityFragment_to_createAvailabilityFragment, bundle)
    }
}