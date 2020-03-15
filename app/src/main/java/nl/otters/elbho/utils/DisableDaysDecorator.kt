package nl.otters.elbho.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

/**
 * Disable weekends in the calendar
 * Disable dates before today in the calendar
 */
class DisableDaysDecorator : DayViewDecorator {
    private val highlightDrawable: Drawable

    init {
        highlightDrawable = ColorDrawable(color)
    }
    companion object {
        private val color = Color.parseColor("#228BC34A")
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val sdf = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val weekDay: String = sdf.format(day.date).toUpperCase(Locale.ENGLISH)

        return weekDay == DayOfWeek.SATURDAY.toString() || weekDay == DayOfWeek.SUNDAY.toString() || day.isBefore(
            CalendarDay.today()
        )
    }

    override fun decorate(view: DayViewFacade) {
        view.setDaysDisabled(true)
    }
}