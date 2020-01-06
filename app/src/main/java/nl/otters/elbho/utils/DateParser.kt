package nl.otters.elbho.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

class DateParser {
    private val locale: Locale = Locale.Builder().setLanguage("nl").setRegion("nl").build()
    //This is the format we get back from the database
    private val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale)
    private val dateParser = SimpleDateFormat("yyyy-MM-dd", locale)

    /*
    * @params string -> yyyy-MM-dd'T'HH:mm:ss
    * @return string -> EE, e.g. MA
    */
    fun toFormattedDay(dateTime: String) : String{
        val formatter = SimpleDateFormat("EE", locale)
        return formatter.format(parser.parse(dateTime)!!).toUpperCase(locale)
    }

    /*
   * @params string -> yyyy-MM-dd
   * @return string -> EE, e.g. MA
   */
    fun dateToFormattedDay(dateTime: String) : String{
        val formatter = SimpleDateFormat("EE", locale)
        return formatter.format(dateParser.parse(dateTime)!!).toUpperCase(locale)
    }

    /*
   * @params string -> yyyy-MM-dd'T'HH:mm:ss
   * @return string -> dd-MM, e.g. 01-11
   */
    fun toFormattedDate(dateTime: String) : String{
        val formatter = SimpleDateFormat("dd-MM",  Locale("nl"))
        return formatter.format(parser.parse(dateTime)!!)
    }

    /*
   * @params string -> yyyy-MM-dd
   * @return string -> dd-MM, e.g. 01-11
   */
    fun dateToFormattedDate(dateTime: String) : String{
        val formatter = SimpleDateFormat("dd-MM",  Locale("nl"))
        return formatter.format(dateParser.parse(dateTime)!!)
    }

    /*
   * @params string -> yyyy-MM-dd'T'HH:mm:ss
   * @return string -> HH:mm, e.g. 09.11
   */
    fun toFormattedTime(dateTime: String) : String{
        val formatter = SimpleDateFormat("HH:mm",  Locale("nl"))
        return formatter.format(parser.parse(dateTime)!!)
    }

    fun toCalendarDay(dateTime: String) : CalendarDay {
        return CalendarDay(parser.parse(dateTime))
    }

    fun toFormattedLong(dateTime: String) : Long{
        return parser.parse(dateTime).time
    }

    fun toMilliseconds(dateTime: String) : Long{
        val formatter = SimpleDateFormat("ss.SSS",  Locale("nl"))
        return formatter.parse(dateTime).time
    }

    fun dateToFormattedMonth(date: Date) : String{
        val formatter = SimpleDateFormat("MMMM",  Locale("nl"))
        return formatter.format(date)
    }


    /*
   * @params string -> yyyy-MM-dd'T'HH:mm:ss
   * @return string -> yyyy-MM-dd'T'HH:mm:ss + n HH
   */
    fun addHours(startTime: String, hoursToAdd: Int): String{
        val date: Date = parser.parse(startTime)!!
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, hoursToAdd)

        return parser.format(calendar.time)
    }

    /*
    * @params string -> yyyy-MM-dd'T'HH:mm:ss
    * @return string -> yyyy-MM-dd'T'HH:mm:ss + n HH
    */
    fun addHoursAndMinutes(startTime: String, hours: Int, minutes: Int): String{
        val date: Date = parser.parse(startTime)!!
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, hours)
        calendar.add(Calendar.MINUTE, minutes)

        return parser.format(calendar.time)
    }

    /*
   * @return string -> 16 NOVEMBER 2019
   */
    fun getDateToday(): String {
        //Couldnt achieve this with one simpleDateFormat because we need the month to be uppercase
        val dayFormat = SimpleDateFormat("dd", locale)
        val monthFormat = SimpleDateFormat("MMMM", locale)
        val yearFormat = SimpleDateFormat("yy", locale)
        val day: String = dayFormat.format(Date())
        val month: String = monthFormat.format(Date()).toUpperCase(locale)
        val year: String = yearFormat.format(Date())

        return "$day $month $year"
    }

    fun getTimestampToday(): String{
        return parser.format(Date())
    }
}