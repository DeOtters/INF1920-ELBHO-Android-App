package nl.otters.elbho.repositories

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.R
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Appointment
import nl.otters.elbho.models.Request
import nl.otters.elbho.services.AppointmentService
import nl.otters.elbho.utils.ResponseHandler
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentRepository(private val context: Context, view: View) {
    private val appointmentService = RetrofitFactory.get().create(AppointmentService::class.java)
    private val responseHandler: ResponseHandler = ResponseHandler(context, view)

    fun getAppointments(options: Appointment.Options): LiveData<ArrayList<Request.Properties>> {
        val appointments = MutableLiveData<ArrayList<Request.Properties>>()

        appointmentService.getAppointments(
            getAuthToken(),
            options.page,
            options.limit,
            options.before,
            options.after,
            options.sort
        ).enqueue(object : Callback<ArrayList<Request.Properties>> {
            override fun onResponse(
                call: Call<ArrayList<Request.Properties>>,
                response: Response<ArrayList<Request.Properties>>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    appointments.value = response.body()
                } else {
                    responseHandler.errorMessage(R.string.error_api_vehicle)
                }
            }

            override fun onFailure(call: Call<ArrayList<Request.Properties>>, t: Throwable) {
                responseHandler.errorMessage(R.string.error_api_vehicle)
            }
        })
        return appointments
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}