package nl.otters.elbho.repositories

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.R
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Availability
import nl.otters.elbho.services.AvailabilityService
import nl.otters.elbho.utils.ResponseHandler
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AvailabilityRepository(private val context: Context, view: View) {
    private val availabilityService = RetrofitFactory.get().create(AvailabilityService::class.java)
    private val responseHandler: ResponseHandler = ResponseHandler(context, view)

    fun getAvailabilities(timePeriod: Availability.TimePeriod?): LiveData<ArrayList<Availability.Slot>>? {
        val availabilities = MutableLiveData<ArrayList<Availability.Slot>>()
        availabilityService.getAvailabilities(getAuthToken(), timePeriod?.before, timePeriod?.after)
            .enqueue(object : Callback<ArrayList<Availability.Slot>> {
                override fun onResponse(
                    call: Call<ArrayList<Availability.Slot>>,
                    response: Response<ArrayList<Availability.Slot>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        availabilities.value = response.body()
                    } else {
                        responseHandler.errorMessage(R.string.error_api)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Availability.Slot>>, t: Throwable) {
                    responseHandler.errorMessage(R.string.error_api)
                }
            })

        return availabilities
    }

    fun createAvailability(availability: Availability.Availabilities): MutableLiveData<Boolean> {
        val success = MutableLiveData<Boolean>()

        availabilityService.createAvailability(getAuthToken(), availability)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    success.value = response.isSuccessful && response.body() != null
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    success.value = false
                    responseHandler.errorMessage(R.string.error_api)
                }
            })

        return success
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}