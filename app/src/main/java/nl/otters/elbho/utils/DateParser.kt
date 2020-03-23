package nl.otters.elbho.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

class DateParser {
    private val locale: Locale = Locale.Builder().setLanguage("nl").setRegion("nl").build()
    //This is the format we get back from the database
    private val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale)
    private val dateParser = SimpleDateFormat("yyyy-MM-dd", locale)

    init {
        parser.timeZone = TimeZone.getTimeZone("GMT")
    }
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
   * @params string -> yyyy-MM-dd'T'HH:mm:ss
   * @return string -> dd-MM-yyyy, e.g. 01-11
   */
    fun toFormattedDateWithYear(dateTime: String) : String{
        val formatter = SimpleDateFormat("dd-MM-yyyy",  Locale("nl"))
        return formatter.format(parser.parse(dateTime)!!)
    }

    /*
   * @params string -> yyyy-MM-dd'T'HH:mm:ss
   * @return string ->  e.g. Zondag 22 maart
   */
    fun toFormattedMonthAndDay(dateTime: String): String {
        val formatter = SimpleDateFormat("EEEE dd MMMM", Locale("nl"))
        return formatter.format(parser.parse(dateTime)!!)
    }

    fun toFormattedYear(dateTime: String): String {
        val formatter = SimpleDateFormat("yyyy", Locale("nl"))
        return formatter.format(parser.parse(dateTime)!!)
    }

    fun toFormattedUploadMonth(dateTime: String): String {
        val formatter = SimpleDateFormat("MMMM", Locale("nl"))
        return formatter.format(parser.parse(dateTime)!!)
    }

    /*
   * @params string -> yyyy-MM-dd
   * @return string -> dd-MM, e.g. 01-11
   */
    fun dateToFormattedDate(dateTime: String) : String{
        val formatter = SimpleDateFormat("dd-MM", Locale("nl"))
        return formatter.format(dateParser.parse(dateTime)!!)
    }

    fun dateToFormattedDateYear(dateTime: String): String {
        val formatter = SimpleDateFormat("dd/MM/YY", Locale("nl"))
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

    /*
   * @params string -> yyyy-MM-dd
   * @return string -> dd MMM yyyy
   */
    fun toFormattedNLdate(dateTime: String) : String{
        val parser =  SimpleDateFormat("yyyy-MM-dd", Locale("nl"))
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale("nl"))
        return formatter.format(parser.parse(dateTime)!!)
    }

    fun toCalendarDay(dateTime: String) : CalendarDay {
        return CalendarDay(parser.parse(dateTime))
    }

    fun dateToFormattedMonth(date: Date) : String{
        val formatter = SimpleDateFormat("MMMM",  Locale("nl"))
        return formatter.format(date)
    }

    fun dateToFormattedDatetime(date: Date) : String{
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",  Locale("nl"))
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

    fun dateTimeStringToDate(dateTime: String): Date{
        return parser.parse(dateTime)!!
    }

    /*
   * @return string -> 16 NOVEMBER 2019
   */
    fun getDateToday(): String {
        //Couldnt achieve this with one simpleDateFormat because we need the month to be uppercase
        val dayFormat = SimpleDateFormat("dd", locale)
        val monthFormat = SimpleDateFormat("MMMM", locale)
        val yearFormat = SimpleDateFormat("yyyy", locale)
        val day: String = dayFormat.format(Date())
        val month: String = monthFormat.format(Date()).toUpperCase(locale)
        val year: String = yearFormat.format(Date())

        return "$day $month $year"
    }

    fun getTimestampToday(): String{
        return parser.format(Date())
    }

    fun getTimestampLastDayOfMonthBefore(): String {
        //Last day of month
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("nl"))
        return formatter.format(calendar.time)
    }

    /*
  * @return string -> 2020-02-19
  */
    fun getDateStampToday(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("nl"))
        return formatter.format(Date())
    }

    fun getDateStampTomorrow(): String {
        val currentDate = Date()

        val calendar: Calendar = Calendar.getInstance()
        calendar.time = currentDate

        calendar.add(Calendar.DATE, 1)

        val dateTomorrow: Date = calendar.time

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("nl"))
        return formatter.format(dateTomorrow)
    }
}