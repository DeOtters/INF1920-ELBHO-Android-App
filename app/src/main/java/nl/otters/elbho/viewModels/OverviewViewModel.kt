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
    private var doneAppointments: LiveData<ArrayList<Request.Properties>> = MutableLiveData()
    private val dateParser: DateParser = DateParser()

    init {
        loadAllRequests()
        loadAllUpcomingAppointments()
        loadAllDoneAppointments()
    }

    private fun loadAllRequests(){
        requests = requestRepository.getAllRequests()
    }

    private fun loadAllUpcomingAppointments(){
        upcomingAppointments = appointmentRepository.getAppointments(Appointment.Options(null,null,null, dateParser.getTimestampToday(), null))
    }

    private fun loadAllDoneAppointments(){
        doneAppointments = appointmentRepository.getAppointments(Appointment.Options(null, null,  dateParser.getTimestampToday(), null, "DESC"))
    }

    fun getAllRequests(): LiveData<ArrayList<Request.Properties>> {
        return requests
    }

    fun getAllUpcomingAppointments(): LiveData<ArrayList<Request.Properties>> {
        return upcomingAppointments
    }

    fun getAllDoneAppointments(): LiveData<ArrayList<Request.Properties>> {
        return doneAppointments
    }
}