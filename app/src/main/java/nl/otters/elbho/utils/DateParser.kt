package nl.otters.elbho.utils

import java.text.SimpleDateFormat
import java.util.*

class DateParser {
    private val locale: Locale = Locale.Builder().setLanguage("nl").build()
    //This is the format we get back from the database
    private val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale)

    /*
    * @params string -> yyyy-MM-dd'T'HH:mm:ss
    * @return string -> EE, e.g. MA
    */
    fun toFormattedDay(dateTime: String) : String{
        val formatter = SimpleDateFormat("EE", locale)
        return formatter.format(parser.parse(dateTime)!!).toUpperCase(locale)
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
   * @params string -> yyyy-MM-dd'T'HH:mm:ss
   * @return string -> HH:mm, e.g. 09.11
   */
    fun toFormattedTime(dateTime: String) : String{
        val formatter = SimpleDateFormat("HH:mm",  Locale("nl"))
        return formatter.format(parser.parse(dateTime)!!)
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
}