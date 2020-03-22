package nl.otters.elbho.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

/**
 * Disable weekends in the calendar
 */
class AvailableDayDecorator(private val daysToCheck: String) : DayViewDecorator {
    private val highlightDrawable: Drawable
    private val dateParser: DateParser = DateParser()

    init {
        highlightDrawable = ColorDrawable(color)
    }

    companion object {
        private val color = Color.parseColor("#86C8F3")
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        if (day.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || day.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || day.isBefore(
                CalendarDay.today()
            )
        ) {
           return false
        } else{
            return day == dateParser.toCalendarDay(daysToCheck)
        }
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(highlightDrawable)
    }
}