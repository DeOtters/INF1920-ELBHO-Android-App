package nl.otters.elbho.views.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.component_availability_input.view.*
import kotlinx.android.synthetic.main.fragment_create_availability.*
import kotlinx.android.synthetic.main.fragment_vehicle_reservation.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Availability
import nl.otters.elbho.repositories.AvailabilityRepository
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.utils.SharedPreferences
import nl.otters.elbho.viewModels.AvailabilityViewModel
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
    private lateinit var datesOfWeek: ArrayList<String>
    private lateinit var dateParser: DateParser
    private lateinit var availabilityViewModel: AvailabilityViewModel
    private val defaultTimePickerInputValue = "--:--"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_availability, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val availabilityRepository = AvailabilityRepository(activity!!.applicationContext)
        availabilityViewModel = AvailabilityViewModel(availabilityRepository)

        dateParser = DateParser()
        chosenDay = arguments?.getParcelable("KEY_CHOSEN_DATE")!!
        availability = arguments?.getParcelableArrayList("KEY_AVAILABILITY")!!
        datesOfWeek = getDatesOfWeek(chosenDay)

        setDayLabels()
        setOnClickListeners()
    }

    private fun setWeekSelector(date: CalendarDay){
        //TODO: onItemSelected setDayLabels
        val calendar: Calendar =  Calendar.getInstance()
        // TODO: using two lists for the same value is not cool..
        val weeks: ArrayList<String> = ArrayList()
        val weekDates: ArrayList<Date> = ArrayList()

        //Here we fill the selection list with 20 weeks starting from today
        for (week in 0..20){
            weeks.add(resources.getString(R.string.create_availability_week_selector, calendar.get(Calendar.WEEK_OF_YEAR)))
            weekDates.add(calendar.time)
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }
        //Here we setup the adapter
        val adapter: ArrayAdapter<String?> = ArrayAdapter(
            context!!,
            R.layout.component_menu_popup_item,
            weeks as List<String?>
        )

        //We set the default selected value to the chosenDate from the calendar
        calendar.set(date.year, date.month, date.day)
        filled_exposed_dropdown.text = SpannableStringBuilder(resources.getString(R.string.create_availability_week_selector, calendar.get(Calendar.WEEK_OF_YEAR)))
        filled_exposed_dropdown.setAdapter(adapter)
        filled_exposed_dropdown.setOnItemClickListener { _, _, position, _ ->
            val chosenDate = CalendarDay(weekDates[position])
            datesOfWeek = getDatesOfWeek(chosenDate)
            setDayLabels()
        }
    }

    private fun getDatesOfWeek(date: CalendarDay): ArrayList<String>{
        val calendar: Calendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale("nl"))
        val datesOfWeek: ArrayList<String> = ArrayList()

        calendar.time = date.date
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        for(i in 0..4){
            datesOfWeek.add(format.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return datesOfWeek
    }

    private fun setOnClickListeners() {
        inputFieldList = arrayListOf(
            availability_monday,
            availability_tuesday,
            availability_wednesday,
            availability_thursday,
            availability_friday
        )

        availability_copy_week.setOnClickListener { copyWeek() }
        availability_create.setOnClickListener { createAvailability(view!!) }

        @SuppressLint("NewApi")
        for (item in inputFieldList) {
            val calStart = Calendar.getInstance()
            val calEnd = Calendar.getInstance()
            var startReservationTime: String = " "
            var endReservationTime: String = " "

            item.startTime.setOnClickListener {
                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calStart.set(Calendar.HOUR_OF_DAY, hour)
                calStart.set(Calendar.MINUTE, minute)

                calEnd.set(Calendar.HOUR_OF_DAY, hour + 2)
                calEnd.set(Calendar.MINUTE, minute)

                item.startTime.setText(SimpleDateFormat("HH:mm").format(calStart.time))
                startReservationTime = SimpleDateFormat("HH:mm").format(calStart.time)

                item.endTime.setText(SimpleDateFormat("HH:mm").format(calEnd.time))
                endReservationTime = SimpleDateFormat("HH:mm").format(calEnd.time)
            }
                TimePickerDialog(context, timeSetListener, calStart.get(Calendar.HOUR_OF_DAY), calStart.get(Calendar.MINUTE), true).show()
            }

            endTime.setOnClickListener {
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    calEnd.set(Calendar.HOUR_OF_DAY, hour)
                    calEnd.set(Calendar.MINUTE, minute)

                    if(startReservationTime.equals(" ")) {
                        calStart.set(Calendar.HOUR_OF_DAY, hour - 2)
                        calStart.set(Calendar.MINUTE, minute)

                        startTime.setText(SimpleDateFormat("HH:mm").format(calStart.time))
                        startReservationTime = SimpleDateFormat("HH:mm").format(calStart.time)

                        endTime.setText(SimpleDateFormat("HH:mm").format(calEnd.time))
                        endReservationTime = SimpleDateFormat("HH:mm").format(calEnd.time)
                    }
                    if(calEnd.after(calStart)){
                        endTime.setText(SimpleDateFormat("HH:mm").format(calEnd.time))
                        SimpleDateFormat("HH:mm").format(calEnd.time)
                    } else {
                        Toast.makeText(context,R.string.toast_end_after, Toast.LENGTH_SHORT).show()
                    }
                }
                TimePickerDialog(context, timeSetListener, calEnd.get(Calendar.HOUR_OF_DAY), calEnd.get(Calendar.MINUTE), true).show()
            }

//            item.availability_clear.setOnClickListener {
//
//            }
        }
    }

    private fun setDayLabels() {
        availability_monday.availability_dayText.text = dateParser.dateToFormattedDate(datesOfWeek[0])
        availability_monday.availability_dateText.text = dateParser.dateToFormattedDay(datesOfWeek[0])

        availability_tuesday.availability_dayText.text = dateParser.dateToFormattedDate(datesOfWeek[1])
        availability_tuesday.availability_dateText.text = dateParser.dateToFormattedDay(datesOfWeek[1])

        availability_wednesday.availability_dayText.text = dateParser.dateToFormattedDate(datesOfWeek[2])
        availability_wednesday.availability_dateText.text = dateParser.dateToFormattedDay(datesOfWeek[2])

        availability_thursday.availability_dayText.text = dateParser.dateToFormattedDate(datesOfWeek[3])
        availability_thursday.availability_dateText.text = dateParser.dateToFormattedDay(datesOfWeek[3])

        availability_friday.availability_dayText.text = dateParser.dateToFormattedDate(datesOfWeek[4])
        availability_friday.availability_dateText.text = dateParser.dateToFormattedDay(datesOfWeek[4])
    }

    private fun copyWeek() {
        findNavController().navigate(R.id.action_createAvailabilityFragment_to_copyWeekFragment)
    }

    private fun formatDateTime(date: String, time: String): String {
        return date
            .plus("T")
            .plus(time)
    }

    private fun createAvailability(view: View) {
        val sharedPreferences = SharedPreferences(activity!!.applicationContext)
        val adviserId = sharedPreferences.getValueString("adviser-id")!!
        val newAvailabilities : Availability.Availabilities = Availability.Availabilities(ArrayList())

        inputFieldList.forEachIndexed{index, item ->
            if (item.startTime.text.toString() != defaultTimePickerInputValue && item.endTime.text.toString() != defaultTimePickerInputValue){
                val newAvailability: Availability.Slot = Availability.Slot(
                    adviserId,
                    adviserId,
                    datesOfWeek[index],
                    formatDateTime(datesOfWeek[index], item.startTime.text.toString()),
                    formatDateTime(datesOfWeek[index], item.endTime.text.toString()),
                    "2019-12-07T17:00:46.694Z",
                    "2019-12-07T17:00:46.694Z"
                )
                newAvailabilities.availabilities!!.add(newAvailability)
            }
        }
        if (newAvailabilities.availabilities!!.isNotEmpty()){
            availabilityViewModel.createAvailabilities(newAvailabilities)
        }

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
        setWeekSelector(chosenDay)
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.create_new_availability_title)
    }
}

