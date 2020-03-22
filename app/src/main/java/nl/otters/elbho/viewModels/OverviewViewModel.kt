package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Appointment
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.AppointmentRepository
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.utils.DateParser
import java.util.*


class OverviewViewModel(private val requestRepository: RequestRepository, private val appointmentRepository: AppointmentRepository) : ViewModel() {
    private var requests: LiveData<ArrayList<Request.Properties>> = MutableLiveData()
    private var upcomingAppointments: LiveData<ArrayList<Request.Properties>> = MutableLiveData()
    private var todaysAppointments: LiveData<ArrayList<Request.Properties>> = MutableLiveData()
    private var doneAppointments: LiveData<ArrayList<Request.Properties>> = MutableLiveData()
    private val dateParser: DateParser = DateParser()

    init {
        loadAllRequests()
        loadAllUpcomingAppointments()
        loadTodaysAppointments()
        loadAllDoneAppointments()
    }

    fun loadAllRequests() {
        requests = requestRepository.getAllRequests()
    }

    private fun loadAllUpcomingAppointments(){
        upcomingAppointments = appointmentRepository.getAppointments(
            Appointment.Options(
                null,
                null,
                null,
                dateParser.getDateStampToday(),
                null
            )
        )
    }

    private fun loadTodaysAppointments() {
        todaysAppointments = appointmentRepository.getAppointments(
            Appointment.Options(
                null,
                null,
                dateParser.getDateStampTomorrow(),
                dateParser.getDateStampToday(),
                null
            )
        )
    }

    private fun loadAllDoneAppointments(){
        doneAppointments = appointmentRepository.getAppointments(
            Appointment.Options(
                null,
                null,
                dateParser.getDateStampToday(),
                null,
                "DESC"
            )
        )
    }

    fun getAllRequests(): LiveData<ArrayList<Request.Properties>> {
        loadAllRequests()
        return requests
    }

    fun getAllUpcomingAppointments(): LiveData<ArrayList<Request.Properties>> {
        loadAllUpcomingAppointments()
        return upcomingAppointments
    }

    fun getTodaysAppointments(): LiveData<ArrayList<Request.Properties>> {
        loadTodaysAppointments()
        return todaysAppointments
    }

    fun getAllDoneAppointments(): LiveData<ArrayList<Request.Properties>> {
        loadAllDoneAppointments()
        return doneAppointments
    }
}