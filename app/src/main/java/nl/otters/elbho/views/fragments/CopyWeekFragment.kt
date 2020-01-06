package nl.otters.elbho.views.fragments

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.component_copy_week.view.*
import kotlinx.android.synthetic.main.fragment_copy_week.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Availability
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.utils.SharedPreferences
import java.util.*
import kotlin.collections.ArrayList

class CopyWeekFragment : DetailFragment() {

    private lateinit var weekList: ArrayList<View>
    private lateinit var chosenDay: CalendarDay
    private lateinit var availabilitiesToCopy: Availability.Availabilities


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_copy_week, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        chosenDay = arguments?.getParcelable("KEY_CHOSEN_DATE")!!
        availabilitiesToCopy = arguments?.getParcelable("KEY_NEW_AVAILABILITIES")!!

        weekList = arrayListOf(
            copy_week_A,
            copy_week_B,
            copy_week_C,
            copy_week_D,
            copy_week_E,
            copy_week_F,
            copy_week_G,
            copy_week_H,
            copy_week_I
        )

        setWeekNumberTitle()
        setCheckboxLabels()
        setOnClickListeners()
    }

    private fun setWeekNumberTitle(){
        copy_week_title.text = SpannableStringBuilder(resources.getString(R.string.copy_week_title, chosenDay.calendar.get(Calendar.WEEK_OF_YEAR)))
    }

    private fun setCheckboxLabels(){
        val calendar: Calendar = chosenDay.calendar

        for (item in weekList){
            //We start by adding 1 week to the chosenDate, since we already filled in the availability for that month
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            val weekNumber: Int = calendar.get(Calendar.WEEK_OF_YEAR)

            //Here we check what the first day of that given week is
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            val firstDateOfWeek: String = calendar.get(Calendar.DAY_OF_MONTH).toString().plus(" "
            ).plus(DateParser().dateToFormattedMonth(calendar.time))

            //Then we add 6 days to get the last day of that week
            calendar.add(Calendar.DAY_OF_YEAR, 6)
            val lastDateOfWeek: String =
                calendar.get(Calendar.DAY_OF_MONTH).toString()
                .plus(" "
                ).plus(DateParser().dateToFormattedMonth(calendar.time))

            item.copy_date_range.text = resources.getString(R.string.temp_copy_week, weekNumber, firstDateOfWeek, lastDateOfWeek )
        }
    }

    private fun setOnClickListeners() {
        copy_week_confirm.setOnClickListener { copyWeek(view!!) }
    }

    private fun copyWeek(view: View) {
        val sharedPreferences = SharedPreferences(activity!!.applicationContext)
        val adviserId = sharedPreferences.getValueString("adviser-id")!!
        // TODO: Get selected checkboxes and send to API
        //1. Voor elke checkbox
        for (item in weekList) {
            //2. Check of de checkbox is aangevinkt
            if(item.copy_checkbox.isChecked){
                //3. Als deze is aangevinkt
                availabilitiesToCopy.availabilities!!.forEachIndexed { index, availability ->
                    //4. Kijk welke dag hoort bij de ingevulde availability
                    Log.e("toCopy", availability.toString())

                    //6. Voeg toe aan newAvailabilities lijst
                }

            }
        }
//        Snackbar.make(
//            view,
//            getString(R.string.copy_week_saved),
//            Snackbar.LENGTH_SHORT
//        ).show()
//        findNavController().navigateUp()
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.copy_week_title_bar)
    }
}