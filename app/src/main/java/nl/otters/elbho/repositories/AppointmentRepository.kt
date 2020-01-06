package nl.otters.elbho.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Appointment
import nl.otters.elbho.models.Request
import nl.otters.elbho.services.AppointmentService
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentRepository(private val context: Context) {
    private val appointmentService = RetrofitFactory.get().create(AppointmentService::class.java)

    fun getAppointments(options: Appointment.Options): LiveData<ArrayList<Request.Properties>>{
        val appointments = MutableLiveData<ArrayList<Request.Properties>>()

        appointmentService.getAppointments(getAuthToken(), options.page, options.limit, options.before, options.after, options.sort).enqueue(object : Callback<ArrayList<Request.Properties>> {
            override fun onResponse(
                call: Call<ArrayList<Request.Properties>>,
                response: Response<ArrayList<Request.Properties>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    appointments.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<Request.Properties>>, t: Throwable) {
                //TODO: implement error handling
                //TODO: with current api state, show message like: couldn't establish network connection
                //TODO: with that in mind I think we shouldn't only return LiveData<Boolean>, but something in the line of LiveData<success, message>.
                Log.e("HTTP", "Could not fetch data" , t)
            }
        })
        return appointments
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}